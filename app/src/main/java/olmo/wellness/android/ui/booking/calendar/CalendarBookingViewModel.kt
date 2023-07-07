package olmo.wellness.android.ui.booking.calendar

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.ui.toLocalDate
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarBookingViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase
) : BaseViewModel<CalendarBookingState, CalendarBookingEvent>(){

    override fun initState(): CalendarBookingState {
        return CalendarBookingState()
    }

    override fun onTriggeredEvent(event: CalendarBookingEvent) {
       when(event){
           is CalendarBookingEvent.ShowLoading -> {
               setState(
                   uiState.value.copy(
                       isLoading = event.isLoading
                   )
               )
           }

           is CalendarBookingEvent.OnBindServiceId -> {
               setState(
                   uiState.value.copy(
                       serviceId = event.serviceId
                   )
               )
           }

           is CalendarBookingEvent.FetchBookingByTime -> {
                setState(
                    uiState.value.copy(
                        listCalendar = event.listCalendar,
                        listCalendarMoney = event.listCalendarMoney,
                        kalendarMoneyDefault = event.kalendarMoneyDefault
                    )
                )
           }
       }
    }

    fun onBindServiceId(serviceId: Int){
        triggerStateEvent(CalendarBookingEvent.ShowLoading(true))
        triggerStateEvent(CalendarBookingEvent.OnBindServiceId(serviceId))
        triggerStateEvent(CalendarBookingEvent.ShowLoading(false))
    }

    @SuppressLint("NewApi")
    fun fetchDataByRangeTime(startTime: Long, endTime: Long, serviceId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            bookingUseCase.getServiceCalendar(fromDate = startTime, toDate = endTime, serviceId = serviceId).collectLatest { result ->
                    when(result){
                        is olmo.wellness.android.core.Result.Loading -> {
                            triggerStateEvent(CalendarBookingEvent.ShowLoading(true))
                        }

                        is olmo.wellness.android.core.Result.Success -> {
                            if(result.data?.isNotEmpty() == true){
                                val listCalendarMoney = mutableListOf<KalendarMoney>()
                                var kalendarMoneyDefault : KalendarMoney ?= null
                                result.data.map {
                                    val localDate = DateTimeHelper.convertLongToDate(it.startTimestamp)
                                    if(localDate.toLocalDate() == LocalDate.now()){
                                        kalendarMoneyDefault = KalendarMoney(date = localDate.toLocalDate(),
                                            money = it.adultPrice, id = it.id, typeSession = it.typeSession)
                                    }
                                    listCalendarMoney.add(KalendarMoney(date = localDate.toLocalDate(),
                                        money = it.adultPrice, id = it.id, typeSession = it.typeSession))
                                }
                                triggerStateEvent(
                                    CalendarBookingEvent.FetchBookingByTime(
                                        listCalendar =  result.data,
                                        listCalendarMoney = listCalendarMoney,
                                        kalendarMoneyDefault = kalendarMoneyDefault
                                    )
                                )
                            }
                            triggerStateEvent(CalendarBookingEvent.ShowLoading(false))
                        }

                        is olmo.wellness.android.core.Result.Error -> {
                            triggerStateEvent(CalendarBookingEvent.ShowLoading(false))
                        }
                    }
                }
        }
    }
}