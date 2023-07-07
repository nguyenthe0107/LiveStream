package olmo.wellness.android.data

import olmo.wellness.android.core.Constants
import olmo.wellness.android.data.model.*
import olmo.wellness.android.data.model.chat.RoomChatSingleDTO
import olmo.wellness.android.data.model.chat.RoomListChatSingleDTO
import olmo.wellness.android.data.model.fcm.AppUserRequest
import olmo.wellness.android.data.model.notification.CountNotificationDTOModel
import olmo.wellness.android.data.model.notification.NotificationDTOModel
import olmo.wellness.android.data.model.upload.UploadFilesRequest
import olmo.wellness.android.data.model.upload.UploadFilesResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiChatService {
    @GET("notifications")
    suspend fun getNotification(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("userId") userId: Int?,
    ): Response<BaseResponse<NotificationDTOModel>>

    @POST("appUsers")
    suspend fun sendAppUsers(
       @Body appUserRequest: AppUserRequest
    ): Response<BaseResponse<NotificationDTOModel>>

    @HTTP(method = "DELETE", path = "appUsers", hasBody = true)
    suspend fun deleteAppUsers(
        @Body appUserRequest: AppUserRequest
    ): Response<BaseResponse<NotificationDTOModel>>

    @GET("notifications/countNotSeen")
    suspend fun getCountNotSeen(): Response<CountNotificationDTOModel>


    @GET("roomChat/single")
    suspend fun getRoomChatSingle(@Query("userId") userId: Int?) : Response<RoomChatSingleDTO>

      @GET("roomChat/listSingle")
    suspend fun getListRoomSingle(@Query("page") page: Int?, @Query("limit") limit: Int?= Constants.PAGE_SIZE, @Query("search") search: String?) : Response<BaseResponse<RoomListChatSingleDTO>>

    @GET("roomChat/single/notSeen")
    suspend fun getNotSeenNotification() : Response<BaseResponse<RoomListChatSingleDTO>>


    @PUT("notifications/{notiId}/seen")
    suspend fun seenNotification(@Path("notiId") notiId: String): Response<Boolean>

    @PUT("notifications/{notiId}/hide")
    suspend fun deleteNotification(@Path("notiId") notiId: String): Response<Boolean>

     @POST("upload-files")
    suspend fun uploadFiles(@Body rqt : UploadFilesRequest): Response<UploadFilesResponse>


}