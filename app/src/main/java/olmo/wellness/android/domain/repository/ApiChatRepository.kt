package olmo.wellness.android.domain.repository

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.BaseResponse
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.fcm.AppUserRequest
import olmo.wellness.android.data.model.upload.UploadFilesRequest
import olmo.wellness.android.domain.model.notification.CountNotificationInfo
import olmo.wellness.android.domain.model.notification.NotificationInfo

interface ApiChatRepository {
    suspend fun getNotification(page : Int,limit : Int, userId: Int?) : List<NotificationInfo>
    suspend fun getNotificationUpdate(page : Int,limit : Int, userId: Int?) : Flow<Result<List<NotificationInfo>>>
    fun sendAppUser(appUserRequest: AppUserRequest) : Flow<Result<Boolean>>
    fun deleteAppUser(appUserRequest: AppUserRequest) : Flow<Result<Boolean>>
    suspend fun getCountNotSeen() : Flow<Result<CountNotificationInfo>>
    suspend fun seenNotification(notiId: String) : Flow<Result<Boolean>>
    suspend fun deleteNotification(notiId: String) : Flow<Result<Boolean>>
    suspend fun getRoomChatSingle(userId: Int?): Flow<Result<DetailRoom>>
    suspend fun getRoomListChatSingle(page: Int, search : String?,limit : Int?=20) : Flow<Result<BaseResponse<DetailRoom>>>
    suspend fun getCountNotificationNotSeen() : Flow<Result<BaseResponse<DetailRoom>>>
    suspend fun uploadFiles(rqt : UploadFilesRequest) : Flow<Result<List<String>>>
}