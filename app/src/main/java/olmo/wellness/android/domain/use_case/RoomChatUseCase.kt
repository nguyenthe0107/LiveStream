package olmo.wellness.android.domain.use_case

import olmo.wellness.android.core.Constants
import olmo.wellness.android.data.model.upload.UploadFilesRequest
import olmo.wellness.android.domain.repository.ApiChatRepository
import javax.inject.Inject

class RoomChatUseCase @Inject constructor(private val apiRepository: ApiChatRepository) {

    suspend fun getRoomChatSingle(userId: Int?) = apiRepository.getRoomChatSingle(userId)

    suspend fun getRoomListChatSingle(page : Int,search : String?,limit : Int?= Constants.PAGE_SIZE) = apiRepository.getRoomListChatSingle(page, search, limit)

    suspend fun getTotalNotificationNotSeen() = apiRepository.getCountNotificationNotSeen()

    suspend fun uploadFiles(rqt : UploadFilesRequest) = apiRepository.uploadFiles(rqt)
}