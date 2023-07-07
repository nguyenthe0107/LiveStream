package olmo.wellness.android.ui.booking.book_now.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.booking.BookingRequestInfo
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.ServiceLocationInfo
import olmo.wellness.android.domain.model.booking.ServiceSessionInfo
import olmo.wellness.android.domain.model.wrapper.FieldsWrapper
import olmo.wellness.android.domain.model.wrapper.NumberCustomerWrapper
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.booking.book_now.state_event.BookNowViewEvent
import olmo.wellness.android.ui.booking.book_now.state_event.BookNowViewState
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookNowViewModel @Inject constructor(
    val bookingUseCase: BookingUseCase
) :  BaseViewModel<BookNowViewState, BookNowViewEvent>(){

    override fun initState(): BookNowViewState {
        return BookNowViewState()
    }

    override fun onTriggeredEvent(event: BookNowViewEvent) {
        when(event){
            is BookNowViewEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        isLoading = event.isLoading
                    )
                )
            }

            is BookNowViewEvent.OnBindServiceBooking -> {
                setState(
                    uiState.value.copy(
                        serviceBooking = event.serviceBooking
                    )
                )
            }

            is BookNowViewEvent.UpdateServiceSessionInfo -> {
                setState(
                    uiState.value.copy(
                        serviceSessionInfo = event.serviceSessionInfo
                    )
                )
            }

            is BookNowViewEvent.UpdateNumberCustomerSelected -> {
                setState(
                    uiState.value.copy(
                        numberCustomerWrapper = event.numberCustomerWrapper
                    )
                )
            }

            is BookNowViewEvent.UpdateListPlaceBooking -> {
                setState(
                    uiState.value.copy(
                        listPlaceBooking = event.listPlaceBooking
                    )
                )
            }

            is BookNowViewEvent.UpdateDateSelected -> {
                setState(
                    uiState.value.copy(
                        datedPicked = event.datedPicked
                    )
                )
            }

            is BookNowViewEvent.UpdateKalendarHourSelected -> {
                setState(
                    uiState.value.copy(
                        kalendarHourMoney = event.kalendarHourMoney
                    )
                )
            }

            is BookNowViewEvent.UpdateLocationSelected -> {
                setState(
                    uiState.value.copy(
                        serviceLocationSelected = event.serviceLocationSelected
                    )
                )
            }

            is BookNowViewEvent.UpdateServiceSessionConfirmInfo -> {
                setState(
                    uiState.value.copy(
                        serviceSessionConfirmInfo = event.serviceSessionConfirmInfo
                    )
                )
            }

            is BookNowViewEvent.UpdateBookingIdResponseInfo -> {
                setState(
                    uiState.value.copy(
                        bookingIdResponseInfo = event.bookingIdResponseInfo,
                        sessionSecret = event.sessionSecret
                    )
                )
            }

            is BookNowViewEvent.UpdateStateBooking -> {
                setState(
                    uiState.value.copy(
                        confirmedBooking = event.confirmedBooking
                    )
                )
            }

            is BookNowViewEvent.UpdateBookingError -> {
                setState(
                    uiState.value.copy(
                        errorMes = event.errorMes
                    )
                )
            }
        }
    }

    fun onBindServiceBooking(booking: ServiceBooking?){
        if(booking != null){
            triggerStateEvent(BookNowViewEvent.OnBindServiceBooking(booking))
            getServiceLocationBooking(booking)
            booking.id?.let { getServiceById(it) }
        }
    }

    fun updateLocationBooking(serviceLocationInfo: ServiceLocationInfo?){
        if(serviceLocationInfo != null){
            triggerStateEvent(BookNowViewEvent.UpdateLocationSelected(serviceLocationInfo))
            getTotalMoney(serviceLocationInfo = serviceLocationInfo)
        }
    }

    fun updateNumberCustomer(updateNumberCustomer: NumberCustomerWrapper){
        triggerStateEvent(BookNowViewEvent.UpdateNumberCustomerSelected(updateNumberCustomer.copy(
            timeStamp = System.currentTimeMillis()
        )))
        getTotalMoney(updateNumberCustomer = updateNumberCustomer)
    }

    fun updateDateSelected(kalendarMoney: KalendarMoney){
        triggerStateEvent(BookNowViewEvent.UpdateDateSelected(kalendarMoney))
    }

    fun updateHourTime(kalendarMoney: KalendarMoney){
        triggerStateEvent(BookNowViewEvent.UpdateKalendarHourSelected(kalendarMoney))
        getTotalMoney(kalendarMoney = kalendarMoney)
    }

    private fun getServiceById(serviceId: Int){
        val fields = FieldsWrapper(id = arrayListOf<Int?>().apply { add(serviceId) })
        viewModelScope.launch(Dispatchers.IO) {
            bookingUseCase.getServices(fields = Gson().toJson(fields)).collect {
                when (it) {
                    is Result.Success -> {
                        if(it.data?.isNotEmpty() == true){
                            val sessionInfo = it.data.firstOrNull()?.sessionInfoDTO?.firstOrNull()
                            if(sessionInfo != null){
                                val numberCustomerWrapper = NumberCustomerWrapper(
                                    numberOfOptionalChild = sessionInfo.numberOfOptionalChild ?: 0,
                                    numberOfOptionalAdult = sessionInfo.numberOfOptionalAdult ?: 0,
                                    numberOfChild = sessionInfo.numberOfChild ?: 0,
                                    numberOfAdult = sessionInfo.numberOfAdult ?: 0
                                )
                                triggerStateEvent(BookNowViewEvent.UpdateNumberCustomerSelected(numberCustomerWrapper))
                                triggerStateEvent(BookNowViewEvent.UpdateServiceSessionInfo(sessionInfo))
                            }
                        }
                    }

                    is Result.Error -> {
                        triggerStateEvent(BookNowViewEvent.ShowLoading(false))
                    }

                    is Result.Loading -> {
                        triggerStateEvent(BookNowViewEvent.ShowLoading(true))
                    }

                }
            }
        }
    }

    private fun getServiceLocationBooking(bookingModel: ServiceBooking){
        viewModelScope.launch(Dispatchers.IO) {
            val serviceId  = listOf(bookingModel.id)
            val query =  "{\"serviceId\":${serviceId}}"
            bookingUseCase.getServiceLocation(query).collectLatest { result ->
                when(result){
                    is Result.Success -> {
                        if(result.data != null){
                            triggerStateEvent(
                                BookNowViewEvent.UpdateListPlaceBooking(
                                    listPlaceBooking = result.data
                                ))
                        }
                        triggerStateEvent(BookNowViewEvent.ShowLoading(false))
                    }

                    is Result.Loading -> {
                        triggerStateEvent(BookNowViewEvent.ShowLoading(true))
                    }

                    else -> {
                        triggerStateEvent(BookNowViewEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    private fun getTotalMoney(serviceLocationInfo: ServiceLocationInfo?=null,
                              updateNumberCustomer: NumberCustomerWrapper?=null,
                              kalendarMoney: KalendarMoney ?= null){
        viewModelScope.launch(Dispatchers.IO) {
            val serviceBooking = uiState.value.serviceBooking
            val locationInfo = serviceLocationInfo ?: uiState.value.serviceLocationSelected
            val numberCustomerWrapper = updateNumberCustomer ?: uiState.value.numberCustomerWrapper
            val kalendarHourMoney = kalendarMoney ?: uiState.value.kalendarHourMoney
            val bodyRequest = ServiceSessionInfo(
                serviceId = serviceBooking?.id,
                locationId = locationInfo?.id ?: 0L,
                numberOfAdult = numberCustomerWrapper?.numberOfAdult,
                numberOfChild = 0, //numberCustomerWrapper?.numberOfChild
                sessionTimestamp = kalendarHourMoney?.timeStamp,
                numberOfOptionalAdult = 0, //numberCustomerWrapper?.numberOfOptionalAdult
                numberOfOptionalChild = 0
            )

            if(locationInfo == null ||  numberCustomerWrapper == null || kalendarHourMoney == null )
                return@launch

            bookingUseCase.postServiceSessionConfirm(listOf(bodyRequest)).collectLatest { result ->
                when(result){

                    is Result.Success -> {
                        if(result.data != null){
                            triggerStateEvent(BookNowViewEvent.UpdateServiceSessionConfirmInfo(result.data))
                        }
                        triggerStateEvent(BookNowViewEvent.ShowLoading(false))
                    }

                    is Result.Loading -> {
                        triggerStateEvent(BookNowViewEvent.ShowLoading(true))
                    }

                    is Result.Error -> {
                        triggerStateEvent(BookNowViewEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    fun createBookingId(){
        viewModelScope.launch(Dispatchers.IO) {
            val sessionResponseInfo = uiState.value.serviceSessionConfirmInfo
            val sessionSecretId: String = UUID.randomUUID().toString()
            val bodyRequest = BookingRequestInfo(
                sessionConfirmId = sessionResponseInfo?.id,
                sessionSecret = sessionSecretId
            )
            triggerStateEvent(BookNowViewEvent.UpdateStateBooking(true))
            bookingUseCase.createBookingId(
                bodyRequest
            ).collectLatest { result ->
                when(result){
                    is Result.Error -> {
                        if(result.message?.isNotEmpty() == true){
                            triggerStateEvent(BookNowViewEvent.UpdateBookingError(result.message))
                        }
                        triggerStateEvent(BookNowViewEvent.ShowLoading(false))
                    }
                    is Result.Success -> {
                        if(result.data != null){
                            triggerStateEvent(BookNowViewEvent.UpdateBookingIdResponseInfo(bookingIdResponseInfo = result.data,
                                sessionSecret = sessionSecretId))
                        }
                        triggerStateEvent(BookNowViewEvent.ShowLoading(false))
                    }
                    is Result.Loading -> {
                        triggerStateEvent(BookNowViewEvent.ShowLoading(true))
                    }
                }
            }
        }
    }

    fun clearBooking(){
        triggerStateEvent(BookNowViewEvent.UpdateStateBooking(false))
    }
}