package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.voucher_service

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.voucher.VoucherInfo
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class VoucherServiceViewModel @Inject constructor(private val bookingUseCase: BookingUseCase):
    BaseViewModel<VoucherServiceState, VoucherServiceEvent>() {

    override fun initState(): VoucherServiceState {
        return VoucherServiceState()
    }

    override fun onTriggeredEvent(event: VoucherServiceEvent) {
        when(event){
            is VoucherServiceEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(isLoading = event.isLoading)
                )
            }

            is VoucherServiceEvent.UpdateSessionConfirmId -> {
                setState(
                    uiState.value.copy(sessionConfirmId = event.sessionConfirmId)
                )
            }

           is VoucherServiceEvent.UpdateListVoucher -> {
               setState(
                   uiState.value.copy(
                       listVoucher = event.listVoucher
                   )
               )
           }

            is VoucherServiceEvent.OnSelectVoucher -> {
                setState(
                    uiState.value.copy(
                        voucherSelected = event.voucherSelected
                    )
                )
            }

            else -> {
            }
        }
    }

    fun bindDataInput(sessionConfirmId: Double? = null,){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(VoucherServiceEvent.UpdateSessionConfirmId(sessionConfirmId))
        }
    }

    fun fetchListVoucher(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(VoucherServiceEvent.ShowLoading(true))
            bookingUseCase.getVoucherList().collectLatest { result ->
                when(result){

                    is Result.Success -> {
                        if(result.data?.isNotEmpty() == true){
                            triggerStateEvent(VoucherServiceEvent.UpdateListVoucher(result.data))
                        }
                        triggerStateEvent(VoucherServiceEvent.ShowLoading(false))
                    }

                    is Result.Loading -> {
                        triggerStateEvent(VoucherServiceEvent.ShowLoading(true))
                    }

                    is Result.Error -> {
                        triggerStateEvent(VoucherServiceEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    fun onSelectedVoucher(voucher: VoucherInfo){
        viewModelScope.launch(Dispatchers.IO) {
            val listDefault = uiState.value.listVoucher?.map { old ->
                if(voucher.id == old.id){
                    old.copy(isSelected = true)
                }else{
                    old.copy(isSelected = false)
                }
            }
            bookingUseCase.getValidateVoucher(
                voucherCode = "\"${voucher.voucher}\"",
                sessionConfirmId = uiState.value.sessionConfirmId
            ).collectLatest {  result ->
                when(result){
                    is Result.Loading -> {
                        triggerStateEvent(VoucherServiceEvent.ShowLoading(true))
                    }
                    is Result.Success -> {
                        if(result.data != null){
                            val listDefaultFinal = uiState.value.listVoucher?.map { old ->
                                if(voucher.id == old.id){
                                    old.copy(isSelected = true, active = result.data.valid)
                                }else{
                                    old.copy(isSelected = false)
                                }
                            }
                            if(listDefaultFinal?.isNotEmpty() == true){
                                triggerStateEvent(VoucherServiceEvent.UpdateListVoucher(listDefaultFinal))
                            }
                            if(result.data.valid){
                                triggerStateEvent(VoucherServiceEvent.OnSelectVoucher(voucher))
                            }
                        }
                        triggerStateEvent(VoucherServiceEvent.ShowLoading(false))
                    }
                    is Result.Error -> {
                        triggerStateEvent(VoucherServiceEvent.ShowLoading(false))
                        if(listDefault?.isNotEmpty() == true){
                            triggerStateEvent(VoucherServiceEvent.UpdateListVoucher(listDefault))
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}