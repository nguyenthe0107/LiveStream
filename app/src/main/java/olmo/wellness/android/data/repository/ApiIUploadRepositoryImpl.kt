package olmo.wellness.android.data.repository

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.flow
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants.MIME_IMAGE
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.ApiUploadService
import olmo.wellness.android.data.model.*
import olmo.wellness.android.data.model.upload.toUploadUrl
import olmo.wellness.android.domain.model.upload.UploadUrlInfo
import olmo.wellness.android.domain.repository.ApiUploadRepository
import olmo.wellness.android.extension.readAsRequestBody
import java.lang.reflect.Type
import javax.inject.Inject

class ApiIUploadRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: ApiUploadService
) : ApiUploadRepository {

    override fun getUploadUrlInfo(mimeType: String) =
        flow {
            val format = "\"$mimeType\""
            kotlin.runCatching { apiService.getUploadUrlInfo(format) }
                .onSuccess {
                    if (it.isSuccessful) {
                        it.body()?.let { response ->
                            emit(Result.Success(response.toUploadUrl()))
                        }
                    } else {
                        val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<UploadUrlInfo>(errorResponse.message))
                    }
                }
                .onFailure {
                    emit(
                        Result.Error<UploadUrlInfo>(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun getMultiUploadUrlInfo(mimeTypes: List<String>) =  flow {
//        val format = "\"$mimeType\""
        val temp = mimeTypes.map { "\"$it\"" }
        kotlin.runCatching { apiService.getUploadUrlInfos(temp.toString()) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        val temp = mutableListOf<UploadUrlInfo>()
                        response.files?.forEach {
                            temp.add(it.toUploadUrl())
                        }
                        emit(Result.Success(temp.toList()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<UploadUrlInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<UploadUrlInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun uploadFile(presignedUrl: String, fileUri: Uri) =
        flow {
            kotlin.runCatching {
                val mimeTypeInput = context.contentResolver.getType(fileUri)?: "image/$MIME_IMAGE"
                mimeTypeInput.let { mimeType ->
                    val requestBody = context.contentResolver.readAsRequestBody(fileUri)
                    apiService.uploadFile(mimeType, presignedUrl, requestBody)
                }
            }.onSuccess {
                    if (it?.isSuccessful) {
                        it.body()?.let {
                            emit(Result.Success(true))
                        }
                    } else {
                        emit(Result.Error<Boolean>(context.getString(R.string.upload_fail)))
                    }
                }
                .onFailure {
                    it.stackTrace
                    emit(
                        Result.Error<Boolean>(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

}