package olmo.wellness.android.ui.screen.booking_service.add_booking

import olmo.wellness.android.core.BaseState
import olmo.wellness.android.domain.model.booking.ServiceBooking

data class AddBookingEventState(
    val errMessage: String = "",
    val message: String? = null,
    val isLoading: Boolean = false,
    val listServices: List<ServiceBooking>? = null,
    val serviceSelected: ServiceBooking? = null,
    val livestreamId: Int? = null,
    val roomId: String? = null,
    val isConfirm: Boolean? = null,
    override var timeUpdate: Long? = System.currentTimeMillis(),
) : BaseState()

sealed class AddBookingEvent {
    data class ShowLoading(
        val isLoading: Boolean = false,
    ) : AddBookingEvent()

    data class BindLiveStreamValue(
        val livestreamId: Int? = null,
        val roomId: String? = null
    ) : AddBookingEvent()

    data class UpdateServiceOfLiveStream(val livestreamList: List<ServiceBooking>? = null) :
        AddBookingEvent()

    data class GetError(val errMessage: String?) : AddBookingEvent()

    data class UpdateServiceSelected(val serviceSelected: ServiceBooking) : AddBookingEvent()

    data class ConfirmAction(val isConfirm: Boolean) : AddBookingEvent()
}
