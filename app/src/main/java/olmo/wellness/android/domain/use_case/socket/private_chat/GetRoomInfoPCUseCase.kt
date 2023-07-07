package olmo.wellness.android.domain.use_case.socket.private_chat

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.domain.repository.RTCPrivateChatRepository
import javax.inject.Inject

class GetRoomInfoPCUseCase @Inject constructor(private val repository: RTCPrivateChatRepository) : BaseUseCase<Unit, StateFlow<DetailRoom?>>()  {
    override fun execute(arg: Unit): Either<Exception, StateFlow<DetailRoom?>>  = repository.getRoomDetail()
}