package olmo.wellness.android.ui.screen.dashboard_booking

import olmo.wellness.android.domain.model.booking.BookingHistoryInfo
import olmo.wellness.android.domain.model.booking.ServiceSessionInfo
import olmo.wellness.android.domain.model.state_dashboard_booking.StatusBookingDashBoardModel


data class MyDashBoardBookingState(
    val isLoading: Boolean = false,
    val listTabBookingServices: List<StatusBookingDashBoardModel>? = null,
    val listBookingItem: HashMap<String?, MutableList<BookingHistoryInfo>?>? = hashMapOf(),
    val timeEvent: Long? = null,
)

sealed class MyDashBoardBookingEvent {
    data class ShowLoading(val isLoading: Boolean) : MyDashBoardBookingEvent()
    data class GetListStateBooking(val listTabBookingServices: List<StatusBookingDashBoardModel>) : MyDashBoardBookingEvent()
    data class GetListBooking(val listBookingItem: List<BookingHistoryInfo>) : MyDashBoardBookingEvent()
    data class UpdateListBooking(val listBookingItem: Map<String?, List<BookingHistoryInfo>?>) : MyDashBoardBookingEvent()
    data class UpdateListItemTypes(val listItemTypes: Map<String?, List<BookingHistoryInfo>?>) : MyDashBoardBookingEvent()
}