package olmo.wellness.android.ui.booking.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import olmo.wellness.android.OlmoApplication
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.PostRequest
import olmo.wellness.android.data.model.booking.RequestAddCart
import olmo.wellness.android.domain.model.booking.*
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.domain.use_case.CartBookingUseCase
import olmo.wellness.android.domain.use_case.socket.booking.GetServicesOfLiveStreamUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.webrtc.rtc.RtcUtils
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookingPlayBackViewModel @Inject constructor(
    private val application: Application,
    private val bookingUseCase: BookingUseCase,
    private val cartBookingUseCase: CartBookingUseCase,
    private val getServicesOfLiveStreamUseCase: GetServicesOfLiveStreamUseCase,
) : BaseViewModel<BookingPlaybackState, BookingPlaybackEvent>() {


    init {
        getServiceOfLiveStream()
    }

    override fun initState(): BookingPlaybackState {
        return BookingPlaybackState()
    }

    override fun onTriggeredEvent(event: BookingPlaybackEvent) {
        when(event){

            is BookingPlaybackEvent.UpdateServiceBookings -> {
                setState(
                    uiState.value.copy(
                        serviceBookings = event.data
                    )
                )
            }

            is BookingPlaybackEvent.UpdateShowServiceBooking -> {
                setState(uiState.value.copy(serviceBookingShow = event.data))
            }

            is BookingPlaybackEvent.UpdateRoomLiveStream ->{
                setState(uiState.value.copy(roomId = event.data))
            }

            is BookingPlaybackEvent.UpdateUserCart -> {
                setState(
                    uiState.value.copy(
                        userCarts = event.data
                    )
                )
            }

            is BookingPlaybackEvent.UpdateServiceSessionConfirmInfo -> {
                setState(
                    uiState.value.copy(
                        serviceSessionConfirmInfo = event.serviceSessionConfirmInfo
                    )
                )
            }

            is BookingPlaybackEvent.ConfirmAction -> {
                setState(
                    uiState.value.copy(
                        confirmedAction = event.confirmedAction
                    )
                )
            }

            is BookingPlaybackEvent.UpdateBookingIdResponseInfo -> {
                setState(
                    uiState.value.copy(
                        bookingIdResponseInfo = event.bookingIdResponseInfo
                    )
                )
            }
        }
    }

    fun dismissDialog(){
        triggerStateEvent(BookingPlaybackEvent.UpdateBookingIdResponseInfo(null))
        triggerStateEvent(BookingPlaybackEvent.ConfirmAction(false))
    }

    fun getServiceLivestream(livestreamId: Int?, roomId : String?) {
        triggerStateEvent(BookingPlaybackEvent.UpdateRoomLiveStream(roomId))
        viewModelScope.launch(Dispatchers.IO) {
            bookingUseCase.getServicesLivestream(livestreamId = livestreamId).collectLatest { res ->
                res.onResultReceived(
                    onSuccess = { serviceBooking ->
                        val serviceForCarts = serviceBooking?.map {
                            it.toServiceBookingForCart()
                        }
                        triggerStateEvent(BookingPlaybackEvent.UpdateServiceBookings(serviceForCarts))
                        filterServiceListBooking(serviceForCarts)
                    },
                    onError = {
                    }
                )
            }
        }

    }

    private fun filterServiceListBooking(data: List<ServiceBookingForCart>?) {
        data?.forEach {
            if (it.bookmark == 1) {
                triggerStateEvent(BookingPlaybackEvent.UpdateShowServiceBooking(it))
                return
            }
        }
        if (data?.isNotEmpty() == true) {
            data[0].let {
                triggerStateEvent(BookingPlaybackEvent.UpdateShowServiceBooking(it))
                return
            }
        }
    }

    fun closeShowBookingService(){
        triggerStateEvent(BookingPlaybackEvent.UpdateShowServiceBooking(null))
    }

    private fun getServiceOfLiveStream() {
        RtcUtils.getServiceOfLiveStream(getServicesOfLiveStreamUseCase) {
            triggerStateEvent(BookingPlaybackEvent.UpdateServiceBookings(it))
            filterServiceListBooking(it)
        }
    }


    fun addToCart(serviceBooking: ServiceBooking) {
        viewModelScope.launch(Dispatchers.IO) {
            val _serviceId = listOf(serviceBooking.id) //serviceId 47 to test
            val query = "{\"serviceId\":${_serviceId}}"
            bookingUseCase.getServiceLocation(query).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        if (result.data?.isNotEmpty() == true) {
                            val cart = RequestAddCart().apply {
                                serviceId = serviceBooking.id
                                numberOfAdult = 1
                                numberOfChild = 0
                                numberOfOptionalAdult = 0
                                numberOfOptionalChild = 0
                                locationId = result.data.firstOrNull()?.id // firstOrNull will be safe
                                sessionTimestamp = System.currentTimeMillis()
                            }
                            val temp = mutableListOf<RequestAddCart>().apply {
                                add(cart)
                            }
                            val rqt = PostRequest(temp.toList())
                            triggerStateEvent(BookingPlaybackEvent.ShowLoading(true))
                            viewModelScope.launch(Dispatchers.IO) {
                                cartBookingUseCase.addToCart(rqt).collectLatest { res ->
                                    res.onResultReceived(
                                        onSuccess = {
                                        },
                                        onError = {

                                        }
                                    )
                                }
                            }

                        }
                    }

                    is Result.Loading -> {
                    }

                    else -> {
                    }
                }
            }
        }

    }

    fun deleteUserCart(serviceId : Int){
        val userId = listOf(sharedPrefs.getUserInfoLocal().userId) //serviceId 47 to test
        val query = "{\"userId\":${userId},\"serviceId\":${listOf(serviceId)}}"
        viewModelScope.launch (Dispatchers.IO){
            cartBookingUseCase.deleteUserCart(fields = query).collectLatest { res->
                res.onResultReceived(
                    onSuccess = {

                    },
                    onError = {

                    }
                )
             }
        }
    }

    fun getUserCart(page: Int) {
        val userId = listOf(sharedPrefs.getUserInfoLocal().userId) //serviceId 47 to test
        val query = "{\"userId\":${userId}}"
        viewModelScope.launch(Dispatchers.IO) {
            cartBookingUseCase.getUserCart(fields = query, page = page).collectLatest { res ->
                res.onResultReceived(
                    onSuccess = { cartBooking ->
                        if (cartBooking?.isNotEmpty() == true) {
                            viewModelScope.launch(Dispatchers.IO) {
                                val results = cartBooking.distinctBy { it.id }.map {
                                    async {
                                        bookingUseCase.getServicePublicById(it.id).last()
                                    }
                                }.awaitAll()
                                results.forEach { resultInfo ->
                                    when(resultInfo){
                                        is Result.Success -> {
                                            cartBooking.map {
                                                  it.serviceBooking = resultInfo.data
                                                //remember map id
                                            }
                                            triggerStateEvent(BookingPlaybackEvent.UpdateUserCart(cartBooking))
                                        }
                                        is Result.Loading -> {

                                        }
                                        is Result.Error -> {
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onError = {
                    }
                )
            }
        }
    }

    fun createServiceSessionConfirm(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(BookingPlaybackEvent.ConfirmAction(true))
            triggerStateEvent(BookingPlaybackEvent.ShowLoading(true))
            val listCarts = uiState.value.userCarts
            val listBodyRequest : MutableList<ServiceSessionInfo> = mutableListOf()
            listCarts?.map { cartBooking ->
                if(cartBooking.isChecked == true){
                    val bodyRequest = ServiceSessionInfo(
                        serviceId = cartBooking.serviceId,
                        locationId = cartBooking.locationId,
                        numberOfAdult = cartBooking.numberOfAdult,
                        numberOfChild = 0, //numberCustomerWrapper?.numberOfChild // backend not support, default is 0
                        sessionTimestamp = cartBooking.sessionTimestamp,
                        numberOfOptionalAdult = 0, //numberCustomerWrapper?.numberOfOptionalAdult // backend not support, default is 0
                        numberOfOptionalChild = 0
                    )
                    listBodyRequest.add(bodyRequest)
                }
            }
            bookingUseCase.postServiceSessionConfirm(listBodyRequest).collectLatest { result ->
                when(result){

                    is Result.Success -> {
                        if(result.data != null){
                            triggerStateEvent(BookingPlaybackEvent.UpdateServiceSessionConfirmInfo(result.data))
                            result.data.id?.let { createBookingId(it) }
                        }
                    }

                    is Result.Loading -> {
                        triggerStateEvent(BookingPlaybackEvent.ShowLoading(true))
                    }

                    is Result.Error -> {
                        triggerStateEvent(BookingPlaybackEvent.ShowLoading(false))
                        viewModelScope.launch(Dispatchers.Main){
                            Toast.makeText(application, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun createBookingId(sessionResponseId: Double){
        viewModelScope.launch(Dispatchers.IO) {
            val sessionSecretId: String = UUID.randomUUID().toString()
            val bodyRequest = BookingRequestInfo(
                sessionConfirmId = sessionResponseId,
                sessionSecret = sessionSecretId
            )
            bookingUseCase.createBookingId(
                bodyRequest
            ).collectLatest { result ->
                when(result){
                    is Result.Error -> {
                        triggerStateEvent(BookingPlaybackEvent.ShowLoading(false))
                        viewModelScope.launch(Dispatchers.Main){
                            Toast.makeText(application, result.message, Toast.LENGTH_SHORT).show()
                        }                    }
                    is Result.Success -> {
                        if(result.data != null){
                            triggerStateEvent(BookingPlaybackEvent.UpdateBookingIdResponseInfo(result.data))
                        }
                        triggerStateEvent(BookingPlaybackEvent.ShowLoading(false))
                    }
                    is Result.Loading -> {
                        triggerStateEvent(BookingPlaybackEvent.ShowLoading(true))
                    }
                }
            }
        }
    }

    fun clearConfirmAction(){
        triggerStateEvent(BookingPlaybackEvent.ConfirmAction(false))
    }

    fun updateListCart(cartBooking: CartBooking){
        viewModelScope.launch(Dispatchers.IO) {
            val listCarts = uiState.value.userCarts
            val listModified : List<CartBooking> = listCarts?.map { internalCart->
                if(internalCart.id == cartBooking.id){
                    internalCart.isChecked = cartBooking.isChecked
                }
                internalCart
            }?: emptyList()

            triggerStateEvent(
                BookingPlaybackEvent.UpdateUserCart(listModified)
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}