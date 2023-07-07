package olmo.wellness.android.domain.repository

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.ServiceBookingForCart

interface RTCBookingServiceRepository {
    fun sendBookmark(event : EventRtc) : Either<Exception, Unit>
    fun sendAddService(event: EventRtc) : Either<Exception,Unit>
    fun servicesOfLiveStream() : Either<Exception,StateFlow<List<ServiceBookingForCart>>>
    fun deleteServiceOfLiveStream(event: EventRtc) : Either<Exception,Unit>
}