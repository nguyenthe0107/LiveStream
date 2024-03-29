package olmo.wellness.android.domain.use_case.socket.livestream

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.domain.repository.RTCLiveStreamRepository
import javax.inject.Inject

class GetTipPackageUseCase @Inject constructor(private val repository: RTCLiveStreamRepository) : BaseUseCase<Unit, StateFlow<ChatMessage?>>() {
    override fun execute(arg: Unit): Either<Exception, StateFlow<ChatMessage?>> = repository.tipPackage()
}