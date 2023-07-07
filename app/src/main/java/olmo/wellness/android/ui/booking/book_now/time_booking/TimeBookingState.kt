package olmo.wellness.android.ui.booking.book_now.time_booking

import olmo.wellness.android.domain.model.booking.TimeBookingInfo
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney

data class TimeBookingState(
    val isLoading: Boolean ?= false,
    val serviceId: Int?=null,
    val kalendarMoney: KalendarMoney?= null,
    val timeBookingInfo: TimeBookingInfo ?= null
)

sealed class TimeBookingEvent{
    data class ShowLoading(val isLoading: Boolean) : TimeBookingEvent()
    data class OnBindServiceId(val serviceId: Int, val kalendarMoney: KalendarMoney) : TimeBookingEvent()
    data class FetchBookingByTime(val timeBookingInfo: TimeBookingInfo) : TimeBookingEvent()
    data class UpdateBooking(val timeBookingInfo: TimeBookingInfo) : TimeBookingEvent()
}

