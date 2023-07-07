package olmo.wellness.android.data

import okhttp3.RequestBody
import olmo.wellness.android.data.model.upload.MultiUploadResponse
import olmo.wellness.android.data.model.upload.UploadResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiUploadService {

    @GET("upload-url")
    suspend fun getUploadUrlInfo(
        @Query("&extension", encoded = true) extension: String
    ): Response<UploadResponse>

    @GET("upload-urls")
    suspend fun getUploadUrlInfos(
        @Query("&extensions", encoded = true) extension: String
    ): Response<MultiUploadResponse>

    @Headers(
        "isAuthorizable: false"
    )
    @PUT
    suspend fun uploadFile(
        @Header("Content-Type") contentType: String,
        @Url urlUpload: String,
        @Body file: RequestBody
    ): Response<Unit>
}