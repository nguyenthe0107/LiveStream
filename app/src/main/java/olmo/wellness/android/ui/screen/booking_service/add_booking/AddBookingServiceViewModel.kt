package olmo.wellness.android.ui.screen.booking_service.add_booking

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.toServiceBooking
import olmo.wellness.android.domain.model.booking.toServiceBookingForCart
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.domain.use_case.socket.booking.DeleteServiceOfLiveStreamUseCase
import olmo.wellness.android.domain.use_case.socket.booking.GetServicesOfLiveStreamUseCase
import olmo.wellness.android.domain.use_case.socket.booking.SendBookmarkUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.webrtc.rtc.RtcUtils
import javax.inject.Inject

@HiltViewModel
class AddBookingServiceViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val sendBookmarkUseCase: SendBookmarkUseCase,
    private val getServicesOfLiveStreamUseCase: GetServicesOfLiveStreamUseCase,
    private val deleteServiceOfLiveStreamUseCase: DeleteServiceOfLiveStreamUseCase,
) : BaseViewModel<AddBookingEventState, AddBookingEvent>() {

    init {
        getServiceOfLiveStream()
    }

    fun bindServiceSelected(inputService: List<ServiceBooking>? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(AddBookingEvent.ShowLoading(true))
            if (inputService?.isNotEmpty() == true) {
                triggerStateEvent(AddBookingEvent.UpdateServiceOfLiveStream(inputService))
            }

            triggerStateEvent(AddBookingEvent.ShowLoading(false))
        }
    }

    fun bindLiveStream(liveStreamId: Int, roomId: String) {
        triggerStateEvent(AddBookingEvent.BindLiveStreamValue(livestreamId = liveStreamId,
            roomId = roomId))
        getServiceLivestream(liveStreamId)
    }

    private fun getServiceLivestream(livestreamId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            bookingUseCase.getServicesLivestream(livestreamId = livestreamId).collectLatest { res ->
                res.onResultReceived(
                    onSuccess = {
                        triggerStateEvent(AddBookingEvent.UpdateServiceOfLiveStream(it))
                        setServiceSelect(it)
                    },
                    onError = {

                    }
                )
            }
        }
    }

    private fun setServiceSelect(data: List<ServiceBooking>?) {
        if (data?.isNotEmpty() == true) {
            val itemSelect = data.find { it.bookmark == 1 }
            if (itemSelect != null) {
                updateServiceSelected(itemSelect,data)
                return
            }
            updateServiceSelected(data[0],data)
        }
    }

    fun fetchServices() {
        if (uiState.value.livestreamId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                triggerStateEvent(AddBookingEvent.ShowLoading(true))
                val query = uiState.value.livestreamId
                bookingUseCase.getServicesLivestream(query).collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {
                            triggerStateEvent(AddBookingEvent.ShowLoading(true))
                        }

                        is Result.Success -> {
                            triggerStateEvent(AddBookingEvent.ShowLoading(false))
                            triggerStateEvent(AddBookingEvent.UpdateServiceOfLiveStream(result.data))
                        }

                        is Result.Error -> {
                            triggerStateEvent(AddBookingEvent.ShowLoading(false))
                        }
                    }
                }
            }
        }
    }

    private fun updateServiceSelected(inputService: ServiceBooking? = null,data: List<ServiceBooking>?) {
//        viewModelScope.launch(Dispatchers.IO) {
        triggerStateEvent(AddBookingEvent.ShowLoading(true))
        if (inputService != null) {
            triggerStateEvent(AddBookingEvent.UpdateServiceSelected(inputService.copy(bookmark = 1)))
        }
        val list = data?.map { oldInfo ->
            if (inputService?.id == oldInfo.id) {
                oldInfo.copy(bookmark = 1)
            } else {
                oldInfo.copy(bookmark = 0)
            }
        }
        triggerStateEvent(AddBookingEvent.UpdateServiceOfLiveStream(list))
        triggerStateEvent(AddBookingEvent.ShowLoading(false))
//        }
    }

    fun confirmAction(status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(AddBookingEvent.ConfirmAction(status))
        }
    }

    fun deleteService(serviceId: Int, isLiveStream: Boolean ?= false) {
        if(isLiveStream == true){
            RtcUtils.deleteServiceOfLiveStream(liveStreamId = uiState.value.livestreamId,
                roomId = uiState.value.roomId,
                serviceId = serviceId,
                deleteServiceOfLiveStreamUseCase = deleteServiceOfLiveStreamUseCase)
        }else{
            val listService = uiState.value.listServices
            val listFilter = listService?.filterNot { it.id == serviceId }
            triggerStateEvent(AddBookingEvent.UpdateServiceOfLiveStream(listFilter))
        }
    }

    override fun initState(): AddBookingEventState {
        return AddBookingEventState()
    }

    override fun onTriggeredEvent(event: AddBookingEvent) {
        when (event) {
            is AddBookingEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        isLoading = event.isLoading
                    )
                )
            }

            is AddBookingEvent.BindLiveStreamValue -> {
                setState(
                    uiState.value.copy(
                        livestreamId = event.livestreamId,
                        roomId = event.roomId
                    )
                )
            }

            is AddBookingEvent.UpdateServiceOfLiveStream -> {
                setState(
                    uiState.value.copy(
                        listServices = event.livestreamList,
                        timeUpdate = System.currentTimeMillis()
                    )
                )
            }

            is AddBookingEvent.UpdateServiceSelected -> {
                setState(
                    uiState.value.copy(
                        serviceSelected = event.serviceSelected
                    )
                )
            }

            is AddBookingEvent.ConfirmAction -> {
                setState(
                    uiState.value.copy(
                        isConfirm = event.isConfirm
                    )
                )
            }

            else -> {}
        }
    }

    fun sendBookMark(serviceId: Int?) {
        RtcUtils.sendBookMark(liveStreamId = uiState.value.livestreamId,
            roomId = uiState.value.roomId,
            serviceId = serviceId,
            sendBookmarkUseCase = sendBookmarkUseCase)
    }

    private fun getServiceOfLiveStream() {
        RtcUtils.getServiceOfLiveStream(getServicesOfLiveStreamUseCase) { serviceBookingForCarts ->
            val serviceBookings = serviceBookingForCarts.map {
                it.toServiceBooking()
            }
            triggerStateEvent(AddBookingEvent.UpdateServiceOfLiveStream(serviceBookings))
            setServiceSelect(serviceBookings)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}