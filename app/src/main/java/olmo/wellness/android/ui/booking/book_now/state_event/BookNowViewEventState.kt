package olmo.wellness.android.ui.booking.book_now.state_event

import olmo.wellness.android.domain.model.booking.*
import olmo.wellness.android.domain.model.wrapper.NumberCustomerWrapper
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney

data class BookNowViewState (
    val isLoading: Boolean ?= false,
    val serviceBooking: ServiceBooking ?= null,
    val serviceSessionInfo: ServiceSessionInfo?= null,
    val listPlaceBooking: List<ServiceLocationInfo> ?= emptyList(),
    val serviceLocationSelected: ServiceLocationInfo ?= null,
    val numberCustomerWrapper: NumberCustomerWrapper ?= null,
    val datedPicked: KalendarMoney ?= null,
    val kalendarHourMoney: KalendarMoney ?= null,
    val serviceSessionConfirmInfo: ServiceSessionConfirmInfo?= null,
    val bookingIdResponseInfo: BookingIdResponseInfo ?= null,
    val errorMes: String ?= null,
    val confirmedBooking: Boolean ?= null,
    val sessionSecret: String ?= null
)

sealed class BookNowViewEvent{
    data class ShowLoading(val isLoading: Boolean) : BookNowViewEvent()
    data class OnBindServiceBooking(val serviceBooking: ServiceBooking) : BookNowViewEvent()
    data class UpdateServiceSessionInfo(val serviceSessionInfo: ServiceSessionInfo) : BookNowViewEvent()
    data class UpdateListPlaceBooking(val listPlaceBooking: List<ServiceLocationInfo>) : BookNowViewEvent()
    data class UpdateLocationSelected(val serviceLocationSelected: ServiceLocationInfo) : BookNowViewEvent()
    data class UpdateNumberCustomerSelected(val numberCustomerWrapper: NumberCustomerWrapper) : BookNowViewEvent()
    data class UpdateDateSelected(val datedPicked: KalendarMoney) : BookNowViewEvent()
    data class UpdateKalendarHourSelected(val kalendarHourMoney: KalendarMoney) : BookNowViewEvent()
    data class UpdateServiceSessionConfirmInfo(val serviceSessionConfirmInfo: ServiceSessionConfirmInfo) : BookNowViewEvent()
    data class UpdateBookingIdResponseInfo(val bookingIdResponseInfo: BookingIdResponseInfo, val sessionSecret: String ?= null) : BookNowViewEvent()
    data class UpdateStateBooking(val confirmedBooking: Boolean) : BookNowViewEvent()
    data class UpdateBookingError(val errorMes: String) : BookNowViewEvent()
}