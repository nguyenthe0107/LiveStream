package olmo.wellness.android.domain.use_case.socket.booking

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.ServiceBookingForCart
import olmo.wellness.android.domain.repository.RTCBookingServiceRepository
import javax.inject.Inject

class GetServicesOfLiveStreamUseCase @Inject constructor(private val repository: RTCBookingServiceRepository) : BaseUseCase<Unit , StateFlow<List<ServiceBookingForCart>>>() {
    override fun execute(arg: Unit): Either<Exception, StateFlow<List<ServiceBookingForCart>>> = repository.servicesOfLiveStream()
}