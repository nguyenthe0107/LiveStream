package olmo.wellness.android.domain.use_case.socket.livestream

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.domain.repository.RTCLiveStreamRepository
import javax.inject.Inject

class GetUserLeaveRoomLSUseCase @Inject constructor(private val repository: RTCLiveStreamRepository) : BaseUseCase<Unit, StateFlow<User?>>(){
    override fun execute(arg: Unit): Either<Exception, StateFlow<User?>>  = repository.userLeaveRoom()
}