package olmo.wellness.android.ui.screen.playback_video.donate.package_options

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.domain.use_case.GetTipAndDonateUseCase
import olmo.wellness.android.domain.use_case.PaymentUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class PackageOptionViewModel @Inject constructor(
    private val tipsUseCase: GetTipAndDonateUseCase,
    private val paymentUseCase: PaymentUseCase) :
    BaseViewModel<PackageOptionState, PackageOptionEvent>() {

    init {
        getPackageOption()
        getBalanceWallet()
    }

    private fun getPackageOption() {
        viewModelScope.launch(Dispatchers.IO) {
            tipsUseCase.getPackageOptions().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        if(result.data?.isNotEmpty() == true){
                            triggerStateEvent(PackageOptionEvent.UpdateListPackageOption(result.data))
                        }
                    }
                    is Result.Loading -> {
                        triggerStateEvent(PackageOptionEvent.ShowLoading(true))
                    }
                    is Result.Error -> {
                        triggerStateEvent(PackageOptionEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    override fun initState(): PackageOptionState {
        return PackageOptionState()
    }

    override fun onTriggeredEvent(event: PackageOptionEvent) {
        when (event) {
            is PackageOptionEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        showLoading = event.isLoading
                    )
                )
            }
            is PackageOptionEvent.UpdateListPackageOption -> {
                setState(
                    uiState.value.copy(
                        listOptions = event.data
                    )
                )
            }
            is PackageOptionEvent.SelectedPackageOption -> {
                setState(
                    uiState.value.copy(
                        tipOptionSelected = event.tipOptionSelected
                    )
                )
            }
            is PackageOptionEvent.OnRecharge -> {
                setState(
                    uiState.value.copy(
                    )
                )
            }
            is PackageOptionEvent.NavigationPackage -> {
                setState(
                    uiState.value.copy(
                        isNavigationPackage = true
                    )
                )
            }
            is PackageOptionEvent.NavigationPaymentMethods -> {
                setState(
                    uiState.value.copy(
                        isNavigationPaymentMethods = true
                    )
                )
            }
            is PackageOptionEvent.UpdateBalanceWallet -> {
                setState(
                    uiState.value.copy(
                        balance = event.balanceInput
                    )
                )
            }
            is PackageOptionEvent.ResetCurrentState -> {
                setState(
                    uiState.value.copy(
                        isNavigationPackage = false,
                        isNavigationPaymentMethods = false,
                        tipOptionSelected = null
                    )
                )
            }
        }
    }

    fun selectedTipOption(tipsOption: PackageOptionInfo?){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(PackageOptionEvent.SelectedPackageOption(tipOptionSelected = tipsOption))
        }
    }

    fun onRecharge(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(PackageOptionEvent.ShowLoading(true))
            val itemSelected = uiState.value.tipOptionSelected
            triggerStateEvent(PackageOptionEvent.NavigationPaymentMethods(itemSelected))
            triggerStateEvent(PackageOptionEvent.ShowLoading(false))
        }
    }

    fun clearCurrentState(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(PackageOptionEvent.ResetCurrentState)
        }
    }

    private fun getBalanceWallet(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(PackageOptionEvent.ShowLoading(true))
            paymentUseCase.getTotalCoin().collectLatest { result ->
                when(result){
                    is Result.Success -> {
                        triggerStateEvent(PackageOptionEvent.UpdateBalanceWallet(result.data?.total ?: 0F))
                    }
                    is Result.Loading -> {
                        triggerStateEvent(PackageOptionEvent.ShowLoading(true))
                    }
                    is Result.Error -> {
                        triggerStateEvent(PackageOptionEvent.ShowLoading(false))
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