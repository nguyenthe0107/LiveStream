package olmo.wellness.android.webrtc.rtc

import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.ServiceBookingForCart

interface RtcBookingApi {
    fun sendBookmark(event : EventRtc)
    fun sendAddService(event: EventRtc)
    fun servicesOfLiveStream() : StateFlow<List<ServiceBookingForCart>>
    fun deleteServiceOfLiveStream(event: EventRtc)
}