package olmo.wellness.android.ui.screen.dashboard_booking.details

import olmo.wellness.android.domain.model.booking.BookingHistoryInfo
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.ServiceBookingForCart
import olmo.wellness.android.domain.model.booking.ServiceLocationInfo
import olmo.wellness.android.domain.model.wrapper.NumberCustomerWrapper
import olmo.wellness.android.ui.booking.book_now.state_event.BookNowViewEvent
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney


data class MyDashBoardDetailsBookingState(
    val isLoading: Boolean = false,
    val bookingDetailItem: BookingHistoryInfo? = null,
    val storeBookingInfo: ServiceBookingForCart ?= null,
    val placeInfoBooked: ServiceLocationInfo ?= null,
    val placeInfoSelectedFromPopup: ServiceLocationInfo ?= null,
    val listPlaceInfo: List<ServiceLocationInfo> ?= null,
    val customerWrapper: NumberCustomerWrapper ?= null,
    val kalendarMoney: KalendarMoney ?= null,
    val updateSuccess: Boolean ?= false,
    val contentError: String ?= ""
)

sealed class MyDashBoardDetailsBookingEvent {
    data class ShowLoading(val isLoading: Boolean) : MyDashBoardDetailsBookingEvent()
    data class GetBookingDetail(val bookingModel: BookingHistoryInfo?) : MyDashBoardDetailsBookingEvent()
    data class UpdateBookingDetail(val bookingModel: BookingHistoryInfo?) : MyDashBoardDetailsBookingEvent()
    data class UpdateServiceInfo(val storeBookingInfo: ServiceBookingForCart) : MyDashBoardDetailsBookingEvent()
    data class UpdatePlaceBookedInfo(val addressInfoBooked: ServiceLocationInfo?=null, val listAddress: List<ServiceLocationInfo>?) : MyDashBoardDetailsBookingEvent()
    data class UpdatePlaceInfoSelectedFromPopup(val placeInfoSelectedFromPopup: ServiceLocationInfo?=null) : MyDashBoardDetailsBookingEvent()
    data class UpdateDateSelectedInfo(val kalendarMoney: KalendarMoney) : MyDashBoardDetailsBookingEvent()
    data class UpdateStatusEditBooking(val updateSuccess: Boolean) : MyDashBoardDetailsBookingEvent()
    data class UpdateListPlaceBooking(val listPlaceBooking: List<ServiceLocationInfo>) : MyDashBoardDetailsBookingEvent()
    data class UpdateDateSelected(val datedPicked: KalendarMoney) : MyDashBoardDetailsBookingEvent()
    data class ShowError(val errContent: String?) : MyDashBoardDetailsBookingEvent()
}