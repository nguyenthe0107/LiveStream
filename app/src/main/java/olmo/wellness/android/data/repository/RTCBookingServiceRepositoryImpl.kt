package olmo.wellness.android.data.repository

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.ServiceBookingForCart
import olmo.wellness.android.domain.repository.RTCBookingServiceRepository
import olmo.wellness.android.extension.withAvailableNetwork
import olmo.wellness.android.webrtc.network.NetworkHandler
import olmo.wellness.android.webrtc.rtc.RtcServiceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RTCBookingServiceRepositoryImpl @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val bookingApi: RtcServiceApi,
) : RTCBookingServiceRepository{
    override fun sendBookmark(event: EventRtc): Either<Exception, Unit>  = networkHandler.withAvailableNetwork(bookingApi.sendBookmark(event))
    override fun sendAddService(event: EventRtc): Either<Exception, Unit> =  networkHandler.withAvailableNetwork(bookingApi.sendAddService(event))

    override fun servicesOfLiveStream(): Either<Exception, StateFlow<List<ServiceBookingForCart>>> = networkHandler.withAvailableNetwork(bookingApi.servicesOfLiveStream())
    override fun deleteServiceOfLiveStream(event: EventRtc): Either<Exception, Unit> =networkHandler.withAvailableNetwork(bookingApi.deleteServiceOfLiveStream(event))
}