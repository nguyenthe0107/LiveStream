package olmo.wellness.android.ui.booking.book_now.time_booking

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.ui.toDate
import javax.inject.Inject

@HiltViewModel
class TimeBookingViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase
) : BaseViewModel<TimeBookingState, TimeBookingEvent>(){

    override fun initState(): TimeBookingState {
        return TimeBookingState()
    }

    override fun onTriggeredEvent(event: TimeBookingEvent) {
       when(event){
           is TimeBookingEvent.ShowLoading -> {
               setState(
                   uiState.value.copy(
                       isLoading = event.isLoading
                   )
               )
           }

           is TimeBookingEvent.OnBindServiceId -> {
               setState(
                   uiState.value.copy(
                       serviceId = event.serviceId,
                       kalendarMoney = event.kalendarMoney
                   )
               )
           }

           is TimeBookingEvent.FetchBookingByTime -> {
               setState(
                   uiState.value.copy(
                       timeBookingInfo = event.timeBookingInfo
                   )
               )
           }
       }
    }

    fun onBindServiceId(kalendarMoney: KalendarMoney, serviceId: Int){
        triggerStateEvent(TimeBookingEvent.OnBindServiceId(serviceId, kalendarMoney))
        fetchDataByTime(kalendarMoney, serviceId)
    }

    private fun fetchDataByTime(kalendarMoney: KalendarMoney, serviceId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val typeSession =  "\"${kalendarMoney.typeSession}\""
            bookingUseCase.getServiceSessionByDate(fromDate = kalendarMoney.date.toDate().time,
                serviceId = serviceId, id = kalendarMoney.id ?: 0.0,
                typeSession = typeSession).collectLatest { result ->
                    when(result){
                        is olmo.wellness.android.core.Result.Loading -> {
                            triggerStateEvent(TimeBookingEvent.ShowLoading(true))
                        }

                        is olmo.wellness.android.core.Result.Success -> {
                            triggerStateEvent(TimeBookingEvent.ShowLoading(false))
                            result.data?.let{
                                triggerStateEvent(TimeBookingEvent.FetchBookingByTime(it))
                            }
                        }

                        is olmo.wellness.android.core.Result.Error -> {
                            triggerStateEvent(TimeBookingEvent.ShowLoading(false))
                        }
                    }
                }
        }
    }
}