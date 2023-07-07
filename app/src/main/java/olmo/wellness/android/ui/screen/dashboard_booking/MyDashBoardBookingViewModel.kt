package olmo.wellness.android.ui.screen.dashboard_booking

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.booking.BookingHistoryInfo
import olmo.wellness.android.domain.model.booking.ServiceSessionInfo
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.state_dashboard_booking.StatusBookingDashBoardModel
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.base.BaseViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MyDashBoardBookingViewModel  @Inject constructor(
    private val bookingUseCase: BookingUseCase
): BaseViewModel<MyDashBoardBookingState, MyDashBoardBookingEvent>(){

    override fun initState(): MyDashBoardBookingState {
        return MyDashBoardBookingState()
    }

    @SuppressLint("NewApi")
    override fun onTriggeredEvent(event: MyDashBoardBookingEvent) {
        when(event){
            is MyDashBoardBookingEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        isLoading = event.isLoading
                    )
                )
            }

            is MyDashBoardBookingEvent.GetListStateBooking -> {
                setState(
                    uiState.value.copy(
                        listTabBookingServices = event.listTabBookingServices
                    )
                )
            }

            is MyDashBoardBookingEvent.UpdateListBooking -> {
                setState(
                    uiState.value.copy(
                        timeEvent = System.currentTimeMillis(),
                        isLoading = false,
                        listBookingItem = uiState.value.listBookingItem?.apply {
                            event.listBookingItem.forEach { (k, v) ->
                                if (this[k] == null) {
                                    set(k, v?.toMutableList())
                                } else {
                                    set(k, get(k)?.apply {
                                        v?.toMutableList()?.distinctBy { it.serviceSessionInfo?.id }?.let { addAll(it) }
                                    }?.map { it }?.distinctBy { it.serviceSessionInfo?.id  } as MutableList<BookingHistoryInfo>)
                                }
                            }
                        }
                    )
                )
            }
        }
    }

    init {
        getMenuStateBooking()
    }

    private fun getMenuStateBooking(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(MyDashBoardBookingEvent.ShowLoading(true))
            val listState = listOf(
                StatusBookingDashBoardModel.Draft,
                StatusBookingDashBoardModel.PaymentPending,
                StatusBookingDashBoardModel.Fail,
                StatusBookingDashBoardModel.RefundPending,
                StatusBookingDashBoardModel.Completed,
                StatusBookingDashBoardModel.Used,
                StatusBookingDashBoardModel.NoUsed,
                StatusBookingDashBoardModel.RequestToCancel,
                StatusBookingDashBoardModel.NoRefund,
                StatusBookingDashBoardModel.Refunded,
            )
            triggerStateEvent(MyDashBoardBookingEvent.GetListStateBooking(listState))
            triggerStateEvent(MyDashBoardBookingEvent.ShowLoading(false))
        }
    }

    fun getDataBooking(type: String, page: Int, limit : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when(sharedPrefs.getUserInfoLocal().userTypeModel){
                UserTypeModel.BUYER -> {
                    getDataBookingForBuyer(type, page, limit)
                }
                else -> {
                    getDataBookingForSeller(type, page, limit)
                }
            }
        }
    }

    private suspend fun getDataBookingForBuyer(type : String, page: Int, limit: Int){
        val typeTitle = "\"${type}\""
        bookingUseCase.getListBookingBuyer(bookingTitle = typeTitle, page =  page, limit = limit)
            .collectLatest { result ->
                handleResultApi(type = type, result = result)
            }
    }

    private suspend fun getDataBookingForSeller(type : String, page: Int, limit: Int){
        val typeTitle = "\"${type}\""
        bookingUseCase.getListBookingSeller(bookingTitle = typeTitle, page =  page, limit = limit)
            .collectLatest { result ->
                handleResultApi(type = type, result = result)
            }
    }

    private fun handleResultApi(type : String, result : Result<List<BookingHistoryInfo>>){
        when (result) {

            is Result.Loading -> {
                triggerStateEvent(MyDashBoardBookingEvent.ShowLoading(true))
            }

            is Result.Success -> {
                if (result.data != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val temp = result.data.associate {
                            return@associate (type as String?) to result.data.distinctBy { it.serviceSessionInfo?.id }
                        }.toMap()
                        temp[type]?.distinctBy { it.bookingInfo?.id }
                        triggerStateEvent(MyDashBoardBookingEvent.UpdateListBooking(temp))
                        triggerStateEvent(MyDashBoardBookingEvent.ShowLoading(false))
                    }
                }else{
                    triggerStateEvent(MyDashBoardBookingEvent.ShowLoading(false))
                }
            }

            is Result.Error -> {
                triggerStateEvent(MyDashBoardBookingEvent.ShowLoading(false))
            }
        }
    }

}