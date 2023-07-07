package olmo.wellness.android.domain.use_case

import olmo.wellness.android.data.model.fcm.AppUserRequest
import olmo.wellness.android.domain.repository.ApiChatRepository
import javax.inject.Inject

class NotificationUseCase @Inject constructor(
    private val apiRepository: ApiChatRepository){
    suspend operator fun invoke(page : Int, limit : Int, userId: Int?) = apiRepository.getNotificationUpdate(page, limit, userId)
    fun sendAppUser(appUserRequest: AppUserRequest) = apiRepository.sendAppUser(appUserRequest)
    fun deleteAppUser(appUserRequest: AppUserRequest) = apiRepository.deleteAppUser(appUserRequest)
    suspend fun getCountNotSeen() = apiRepository.getCountNotSeen()
    suspend fun seenNotification(notiId: String) = apiRepository.seenNotification(notiId)
    suspend fun deleteNotification(notiId: String) = apiRepository.deleteNotification(notiId)
}