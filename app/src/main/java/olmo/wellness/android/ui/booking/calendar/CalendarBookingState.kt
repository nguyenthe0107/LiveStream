package olmo.wellness.android.ui.booking.calendar

import olmo.wellness.android.domain.model.booking.DatePriceInfo
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney

data class CalendarBookingState(
    val isLoading: Boolean ?= false,
    val serviceId: Int?=null,
    val listCalendar: List<DatePriceInfo> ?= null,
    val listCalendarMoney: List<KalendarMoney> ?= null,
    val kalendarMoneyDefault: KalendarMoney?=null
)

sealed class CalendarBookingEvent{
    data class ShowLoading(val isLoading: Boolean) : CalendarBookingEvent()
    data class OnBindServiceId(val serviceId: Int) : CalendarBookingEvent()
    data class FetchBookingByTime(
        val listCalendar: List<DatePriceInfo>,
        val listCalendarMoney: List<KalendarMoney>,
        val kalendarMoneyDefault: KalendarMoney?
    ) : CalendarBookingEvent()
    data class UpdateBooking(val listCalendar: List<DatePriceInfo>) : CalendarBookingEvent()
}

