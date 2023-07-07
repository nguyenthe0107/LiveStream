package olmo.wellness.android.ui.booking.viewmodel

import olmo.wellness.android.core.BaseState
import olmo.wellness.android.domain.model.booking.*

data class BookingPlaybackState(
    val isLoading: Boolean = false,
    val serviceBookingShow: ServiceBookingForCart? = null,
    var serviceBookings: List<ServiceBookingForCart>? = null,
    var roomId : String? =null,
    val userCarts: List<CartBooking>? = emptyList(),
    val serviceSessionConfirmInfo: ServiceSessionConfirmInfo ?= null,
    val confirmedAction: Boolean? = false,
    val bookingIdResponseInfo: BookingIdResponseInfo ?= null,
    override var timeUpdate: Long? = System.currentTimeMillis(),
) : BaseState()


sealed class BookingPlaybackEvent {
    data class ShowLoading(
        val isLoading: Boolean = false,
    ) : BookingPlaybackEvent()

    data class UpdateRoomLiveStream(val data : String?) : BookingPlaybackEvent()

    data class UpdateShowServiceBooking(val data: ServiceBookingForCart?) : BookingPlaybackEvent()

    data class UpdateServiceBookings(val data: List<ServiceBookingForCart>?) : BookingPlaybackEvent()

    data class UpdateUserCart(val data: List<CartBooking>) : BookingPlaybackEvent()

    data class UpdateServiceSessionConfirmInfo(val serviceSessionConfirmInfo: ServiceSessionConfirmInfo) : BookingPlaybackEvent()

    data class ConfirmAction(val confirmedAction: Boolean) : BookingPlaybackEvent()

    data class UpdateBookingIdResponseInfo(val bookingIdResponseInfo: BookingIdResponseInfo?) : BookingPlaybackEvent()
}