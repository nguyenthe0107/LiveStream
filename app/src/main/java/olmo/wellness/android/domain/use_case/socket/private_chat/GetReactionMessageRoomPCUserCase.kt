package olmo.wellness.android.domain.use_case.socket.private_chat

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.domain.repository.RTCPrivateChatRepository
import javax.inject.Inject

class GetReactionMessageRoomPCUserCase @Inject constructor(private val repository: RTCPrivateChatRepository) :
    BaseUseCase<Unit, StateFlow<ChatMessage?>>() {
    override fun execute(arg: Unit): Either<Exception, StateFlow<ChatMessage?>> =
        repository.reactionMessageRoom()
}