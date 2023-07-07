package olmo.wellness.android.domain.use_case.socket.livestream

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.data.model.chat.ReactionCount
import olmo.wellness.android.domain.repository.RTCLiveStreamRepository
import javax.inject.Inject

class GetReactionRoomLSUserCase @Inject constructor(private val repository: RTCLiveStreamRepository)
    : BaseUseCase<Unit,StateFlow<ReactionCount?>>(){
    override fun execute(arg: Unit): Either<Exception, StateFlow<ReactionCount?>> = repository.reactionRoom()
}