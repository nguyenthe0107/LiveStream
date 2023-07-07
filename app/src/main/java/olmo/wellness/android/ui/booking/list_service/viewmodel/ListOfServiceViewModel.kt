package olmo.wellness.android.ui.booking.list_service.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.wrapper.FieldsWrapper
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.booking.list_service.event.ListOfServiceEvent
import olmo.wellness.android.ui.booking.list_service.event.ListOfServiceState
import javax.inject.Inject

@HiltViewModel
class ListOfServiceViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase
) : BaseViewModel<ListOfServiceState, ListOfServiceEvent>() {

    override fun initState(): ListOfServiceState {
        return ListOfServiceState()
    }

    override fun onTriggeredEvent(event: ListOfServiceEvent) {
        when (event) {
            is ListOfServiceEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        showLoading = event.isLoading
                    )
                )
            }
            is ListOfServiceEvent.UpdateListOfServices -> {
                setState(
                    uiState.value.copy(
                        showLoading = false,
                        listOfServices = event.data
                    )
                )
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getServices(page: Int?, _search: String?) {
        triggerStateEvent(ListOfServiceEvent.ShowLoading(true))
        val search = (if (_search?.isNotEmpty() == true) "\"$_search\"" else null)
        val storeId = sharedPrefs.getUserInfoLocal().store?.id
        val fields = FieldsWrapper(storeId= arrayListOf<Int?>().apply { add(storeId) })
            viewModelScope.launch {
                bookingUseCase.getServices(fields = Gson().toJson(fields), search = search, page = page).collect {
                    when (it) {
                        is Result.Success -> {
                            if (page==1){
                                it.data?.let { data ->
                                    triggerStateEvent(ListOfServiceEvent.UpdateListOfServices(data))
                                }
                            }else{
                                val temp= uiState.value.listOfServices as? MutableList
                                it.data?.let { it1 -> temp?.addAll(it1) }
                                temp?.let {list->
                                    triggerStateEvent(ListOfServiceEvent.UpdateListOfServices(list))
                                }
                            }
                            triggerStateEvent(ListOfServiceEvent.ShowLoading(false))

                        }
                        is Result.Error -> {
                            triggerStateEvent(ListOfServiceEvent.ShowLoading(false))
                        }
                        is Result.Loading -> {

                        }

                    }
                }
            }
    }
}