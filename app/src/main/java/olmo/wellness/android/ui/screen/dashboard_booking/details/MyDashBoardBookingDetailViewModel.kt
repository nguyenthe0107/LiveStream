package olmo.wellness.android.ui.screen.dashboard_booking.details

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Constants.ERROR_COMMON
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.booking.RequestCancelBooking
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.booking.BookingHistoryInfo
import olmo.wellness.android.domain.model.booking.ServiceLocationInfo
import olmo.wellness.android.domain.model.booking.ServiceSessionInfo
import olmo.wellness.android.domain.model.wrapper.NumberCustomerWrapper
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.ui.toDate
import javax.inject.Inject

@HiltViewModel
class MyDashBoardBookingDetailViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase
) : BaseViewModel<MyDashBoardDetailsBookingState, MyDashBoardDetailsBookingEvent>(){

    override fun initState(): MyDashBoardDetailsBookingState {
        return MyDashBoardDetailsBookingState()
    }

    override fun onTriggeredEvent(event: MyDashBoardDetailsBookingEvent) {
        when(event){

            is MyDashBoardDetailsBookingEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        isLoading = event.isLoading
                    )
                )
            }

            is MyDashBoardDetailsBookingEvent.GetBookingDetail -> {
                setState(
                    uiState.value.copy(
                        bookingDetailItem = event.bookingModel
                    )
                )
            }

            is MyDashBoardDetailsBookingEvent.UpdatePlaceBookedInfo -> {
                setState(
                    uiState.value.copy(
                        placeInfoBooked = event.addressInfoBooked,
                        listPlaceInfo = event.listAddress
                    )
                )
            }

            is MyDashBoardDetailsBookingEvent.UpdateServiceInfo -> {
                setState(
                    uiState.value.copy(
                        storeBookingInfo = event.storeBookingInfo
                    )
                )
            }

            is MyDashBoardDetailsBookingEvent.UpdatePlaceInfoSelectedFromPopup -> {
                setState(
                    uiState.value.copy(
                        placeInfoSelectedFromPopup = event.placeInfoSelectedFromPopup
                    )
                )
            }

            is MyDashBoardDetailsBookingEvent.UpdateDateSelectedInfo -> {
                setState(
                    uiState.value.copy(
                        kalendarMoney = event.kalendarMoney
                    )
                )
            }

            is MyDashBoardDetailsBookingEvent.UpdateDateSelected -> {
                setState(
                    uiState.value.copy(
                        kalendarMoney = event.datedPicked
                    )
                )
            }

            is MyDashBoardDetailsBookingEvent.UpdateBookingDetail -> {
                setState(
                    uiState.value.copy(
                        bookingDetailItem = event.bookingModel
                    )
                )
            }

            is MyDashBoardDetailsBookingEvent.UpdateStatusEditBooking -> {
                setState(
                    uiState.value.copy(
                        updateSuccess = event.updateSuccess
                    )
                )
            }

            is MyDashBoardDetailsBookingEvent.ShowError -> {
                setState(
                    uiState.value.copy(
                        contentError = event.errContent
                    )
                )
            }

        }
    }

    fun onBindBookingDefault(bookingHistoryInfo: BookingHistoryInfo){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(true))
            triggerStateEvent(MyDashBoardDetailsBookingEvent.GetBookingDetail(bookingHistoryInfo))
            getServiceById(bookingHistoryInfo)
            getServiceLocationBooking(bookingHistoryInfo)
        }
    }

    fun onBindPlaceSelected(placeInfo : ServiceLocationInfo){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(MyDashBoardDetailsBookingEvent.UpdatePlaceInfoSelectedFromPopup(placeInfo))
        }
    }


    fun updateNumberCustomer(numberCustomerWrapper: NumberCustomerWrapper){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(true))
            val bookingDetail = uiState.value.bookingDetailItem
            val serviceSessionInfo = bookingDetail?.serviceSessionInfo?.copy(
                numberOfChild = numberCustomerWrapper.numberOfChild,
                numberOfAdult = numberCustomerWrapper.numberOfAdult,
                numberOfOptionalAdult = numberCustomerWrapper.numberOfOptionalAdult,
                numberOfOptionalChild = numberCustomerWrapper.numberOfOptionalChild
            )
            triggerStateEvent(MyDashBoardDetailsBookingEvent.UpdateBookingDetail(
                bookingDetail?.copy(serviceSessionInfo = serviceSessionInfo,
                    timeStamp = System.currentTimeMillis())
            ))
            triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
        }
    }

    private fun getServiceById(bookingModel: BookingHistoryInfo){
        viewModelScope.launch(Dispatchers.IO) {
            bookingUseCase.getServicePublicById(bookingModel.serviceSessionInfo?.serviceId).collectLatest { result ->
                when(result){
                    is Result.Success -> {
                        if(result.data != null){
                            triggerStateEvent(MyDashBoardDetailsBookingEvent.UpdateServiceInfo(result.data))
                            triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
                        }
                    }

                    is Result.Error -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    private fun getServiceLocationBooking(bookingModel: BookingHistoryInfo){
        viewModelScope.launch(Dispatchers.IO) {
            val serviceId  = listOf(bookingModel.serviceSessionInfo?.serviceId)
            val query =  "{\"serviceId\":${serviceId}}"
            val locationId = bookingModel.serviceSessionInfo?.locationId
            bookingUseCase.getServiceLocation(query).collectLatest { result ->
                when(result){
                    is Result.Success -> {
                        if(result.data != null){
                            result.data.map { location ->
                                if(location.id == locationId){
                                    triggerStateEvent(
                                        MyDashBoardDetailsBookingEvent.UpdatePlaceBookedInfo(
                                        addressInfoBooked = location,
                                            listAddress = result.data
                                    ))
                                }else{
                                    triggerStateEvent(
                                        MyDashBoardDetailsBookingEvent.UpdatePlaceBookedInfo(
                                            listAddress = result.data
                                        ))
                                }
                            }
                        }
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
                    }
                    else -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    /* Get DateTime */
    fun updateKalendarMoney(kalendarMoney: KalendarMoney){
        triggerStateEvent(MyDashBoardDetailsBookingEvent.UpdateDateSelectedInfo(
            kalendarMoney
        ))
    }

    fun updateBookingInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            val bookingInfo = uiState.value.bookingDetailItem
            val address = uiState.value.placeInfoSelectedFromPopup
            val serviceSessionInfo = bookingInfo?.serviceSessionInfo
            val datePicked = uiState.value.kalendarMoney
            val bodyRequest = ServiceSessionInfo(
                serviceId = bookingInfo?.serviceSessionInfo?.serviceId,
                locationId = address?.id ?: 0L,
                numberOfAdult = serviceSessionInfo?.numberOfAdult,
                numberOfChild = 0, //serviceSessionInfo?.numberOfChild
                sessionTimestamp = datePicked?.date?.toDate()?.time,
                numberOfOptionalAdult = 0, //serviceSessionInfo?.numberOfOptionalAdult
                numberOfOptionalChild = 0
            )
            bookingUseCase.postServiceSessionConfirm(listOf(bodyRequest)).collectLatest { result ->
                when(result){

                    is Result.Loading -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(true))
                    }

                    is Result.Success -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.UpdateStatusEditBooking(true))
                    }

                    is Result.Error -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    fun updateReScheduleBooking(){
        viewModelScope.launch(Dispatchers.IO) {
            val bookingInfo = uiState.value.bookingDetailItem
            val address = uiState.value.placeInfoSelectedFromPopup
            val serviceSessionInfo = bookingInfo?.serviceSessionInfo
            val datePicked = uiState.value.kalendarMoney
            val requestBody = RequestCancelBooking(
                serviceSessionConfirmId = serviceSessionInfo?.id ?: 0,
            )
            bookingUseCase.requestRescheduleBooking((requestBody)).collectLatest { result ->
                when(result){

                    is Result.Loading -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(true))
                    }

                    is Result.Success -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.UpdateStatusEditBooking(true))
                    }

                    is Result.Error -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowError(result.message ?: ERROR_COMMON))
                    }
                }
            }
        }
    }

    fun requestDeleteBooking(){
        viewModelScope.launch(Dispatchers.IO) {
            val bookingInfo = uiState.value.bookingDetailItem
            val requestBody = RequestCancelBooking(
                serviceSessionConfirmId = bookingInfo?.serviceSessionInfo?.id ?: 0,
            )
            bookingUseCase.requestCancelBooking((requestBody)).collectLatest { result ->
                when(result){

                    is Result.Loading -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(true))
                    }

                    is Result.Success -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.UpdateStatusEditBooking(true))
                    }

                    is Result.Error -> {
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowError(result.message ?: ERROR_COMMON))
                    }
                }
            }
        }
    }

    /* Backup */
    private fun getBookingInfo(type: String, page: Int, limit : Int) {
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

    fun resetStatus(){
        triggerStateEvent(MyDashBoardDetailsBookingEvent.UpdateStatusEditBooking(false))
    }

    private suspend fun getDataBookingForBuyer(type : String, page: Int, limit: Int){
        val typeTitle = "\"${type}\""
        val bookingId = 137
        bookingUseCase.getListBookingBuyer(bookingTitle = typeTitle, bookingId = bookingId,page =  page, limit = limit)
            .collectLatest { result ->
                handleResultApi(type = type, result = result)
            }
    }

    private suspend fun getDataBookingForSeller(type : String, page: Int, limit: Int){
        val typeTitle = "\"${type}\""
        val bookingId = 137
        bookingUseCase.getListBookingSeller(bookingTitle = typeTitle,bookingId = bookingId, page =  page, limit = limit)
            .collectLatest { result ->
                handleResultApi(type = type, result = result)
            }
    }

    private fun handleResultApi(type : String, result : Result<List<BookingHistoryInfo>>){
        when (result) {

            is Result.Loading -> {
                triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(true))
            }

            is Result.Success -> {
                if (result.data != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val temp = result.data.associate {
                            return@associate (type as String?) to result.data
                        }.toMap()
                        temp[type]?.distinctBy { it.bookingInfo?.id }
                        triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
                    }
                }else{
                    triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
                }
            }

            is Result.Error -> {
                triggerStateEvent(MyDashBoardDetailsBookingEvent.ShowLoading(false))
            }
        }
    }

    fun updateDateSelected(kalendarMoney: KalendarMoney){
        triggerStateEvent(MyDashBoardDetailsBookingEvent.UpdateDateSelected(kalendarMoney))
    }

}