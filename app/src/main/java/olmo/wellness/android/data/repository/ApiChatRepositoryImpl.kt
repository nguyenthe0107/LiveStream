package olmo.wellness.android.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import olmo.wellness.android.R
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.ApiChatService
import olmo.wellness.android.data.model.*
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.toDetailRoom
import olmo.wellness.android.data.model.chat.toRoomChatSingleDomain
import olmo.wellness.android.data.model.fcm.AppUserRequest
import olmo.wellness.android.data.model.notification.toNotificationInfoDomain
import olmo.wellness.android.data.model.upload.UploadFilesRequest
import olmo.wellness.android.domain.model.notification.CountNotificationInfo
import olmo.wellness.android.domain.model.notification.NotificationInfo
import olmo.wellness.android.domain.repository.ApiChatRepository
import java.lang.reflect.Type
import javax.inject.Inject

class ApiChatRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: ApiChatService
) : ApiChatRepository {

    override suspend fun getNotification(
        page: Int,
        limit: Int,
        userId: Int?
    ): List<NotificationInfo> {
        val listNotificationInfo: MutableList<NotificationInfo> = mutableListOf()
        kotlin.runCatching {
            apiService.getNotification(
                page, limit, userId
            )
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    Result.Success(response.records.map { obj ->
                        obj.toNotificationInfoDomain()
                        listNotificationInfo.add(obj.toNotificationInfoDomain())
                    })
                }
            }
        }
        return listNotificationInfo ?: emptyList()
    }

    override suspend fun getNotificationUpdate(page : Int,limit : Int, userId: Int?): Flow<Result<List<NotificationInfo>>> =
        flow<Result<List<NotificationInfo>>> {
            emit(Result.Loading())
            kotlin.runCatching {
                apiService.getNotification(
                    page, limit, userId
                )
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toNotificationInfoDomain()
                        }))
                    }
                }
            }
        }


    override fun sendAppUser(appUserRequest: AppUserRequest): Flow<Result<Boolean>> =
        flow<Result<Boolean>> {
            emit(Result.Loading())
            kotlin.runCatching {
                apiService.sendAppUsers(
                    appUserRequest
                )
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(
                            Result.Success(true)
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun deleteAppUser(appUserRequest: AppUserRequest): Flow<Result<Boolean>> =
        flow<Result<Boolean>> {
            emit(Result.Loading())
            kotlin.runCatching {
                apiService.deleteAppUsers(
                    appUserRequest
                )
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let {
                        emit(
                            Result.Success(true)
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override suspend fun getCountNotSeen(): Flow<Result<CountNotificationInfo>> =
        flow<Result<CountNotificationInfo>> {
            emit(Result.Loading())
            kotlin.runCatching {
                apiService.getCountNotSeen()
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(
                            Result.Success(response.toNotificationInfoDomain())
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override suspend fun getRoomChatSingle(userId: Int?): Flow<Result<DetailRoom>> =
        flow<Result<DetailRoom>> {
            emit(Result.Loading())
            kotlin.runCatching {
                apiService.getRoomChatSingle(userId)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(
                            Result.Success(response.toRoomChatSingleDomain())
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override suspend fun getRoomListChatSingle(
        page: Int,
        search: String?,
        limit: Int?
    ): Flow<Result<BaseResponse<DetailRoom>>> = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.getListRoomSingle(page, limit, search)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    val temp = BaseResponse<DetailRoom>(
                        total = response.total,
                        records = response.records.map { it.toDetailRoom() })
                    emit(
                        Result.Success(temp)
                    )
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun getCountNotificationNotSeen(): Flow<Result<BaseResponse<DetailRoom>>> = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.getNotSeenNotification()
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    val temp = BaseResponse<DetailRoom>(
                        total = response.total,
                        records = response.records.map { it.toDetailRoom() })
                    emit(
                        Result.Success(temp)
                    )
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun uploadFiles(rqt: UploadFilesRequest): Flow<Result<List<String>>> = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.uploadFiles(rqt)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
//                    val temp = BaseResponse<String>(
//                        total = response.total,
//                        records = response.records.map { it })
                    response.fullPath?.let {
                        emit(
                            Result.Success(it)
                        )
                    }

                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun seenNotification(notiId: String): Flow<Result<Boolean>> =
        flow<Result<Boolean>> {
            emit(Result.Loading())
            kotlin.runCatching {
                apiService.seenNotification(notiId)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let {
                        emit(
                            Result.Success(true)
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override suspend fun deleteNotification(notiId: String): Flow<Result<Boolean>> =
        flow<Result<Boolean>> {
            emit(Result.Loading())
            kotlin.runCatching {
                apiService.deleteNotification(notiId)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let {
                        emit(
                            Result.Success(true)
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }
}