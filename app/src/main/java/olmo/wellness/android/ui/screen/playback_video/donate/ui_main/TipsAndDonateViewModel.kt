package olmo.wellness.android.ui.screen.playback_video.donate.ui_main

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.domain.tips.TipsPackageOptionInfo
import olmo.wellness.android.domain.use_case.GetTipAndDonateUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.use_case.PaymentUseCase
import javax.inject.Inject

@HiltViewModel
class TipsAndDonateViewModel @Inject constructor(
    private val tipsUseCase: GetTipAndDonateUseCase,
    private val paymentUseCase: PaymentUseCase) :
    BaseViewModel<TipsAndDonateState, TipsAndDonateEvent>() {

    init {
        getTipsOption()
        getBalanceWallet()
    }

    private fun getTipsOption(){
        viewModelScope.launch(Dispatchers.IO) {
            tipsUseCase.getTipsPackage().collectLatest { result ->
                when(result){
                    is Result.Success -> {
                        result.data?.let {
                            TipsAndDonateEvent.UpdateListTipsOption(
                                it
                            )
                        }?.let { triggerStateEvent(it) }
                        triggerStateEvent(TipsAndDonateEvent.ShowLoading(true))
                    }
                    is Result.Loading -> {
                        triggerStateEvent(TipsAndDonateEvent.ShowLoading(true))
                    }
                    is Result.Error -> {
                        triggerStateEvent(TipsAndDonateEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    override fun initState(): TipsAndDonateState {
        return TipsAndDonateState()
    }

    override fun onTriggeredEvent(event: TipsAndDonateEvent) {
        when (event) {
            is TipsAndDonateEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        showLoading = event.isLoading
                    )
                )
            }
            is TipsAndDonateEvent.UpdateListTipsOption -> {
                setState(
                    uiState.value.copy(
                        listOptions = event.data
                    )
                )
            }
            is TipsAndDonateEvent.SelectedTipOption -> {
                setState(
                    uiState.value.copy(
                        tipOptionSelected = event.tipOptionSelected
                    )
                )
            }
            is TipsAndDonateEvent.OnRecharge -> {
                setState(
                    uiState.value.copy(
                    )
                )
            }
            is TipsAndDonateEvent.NavigationPackage -> {
                setState(
                    uiState.value.copy(
                        isNavigationPackage = true
                    )
                )
            }
            is TipsAndDonateEvent.NavigationPaymentMethods -> {
                setState(
                    uiState.value.copy(
                        isNavigationPaymentMethods = true
                    )
                )
            }
            is TipsAndDonateEvent.ResetState -> {
                setState(
                    uiState.value.copy(
                        isNavigationPaymentMethods = false,
                        isNavigationPackage = false,
                        sendSuccessTipOption = false
                    )
                )
            }
            is TipsAndDonateEvent.UpdateBalance -> {
                setState(
                    uiState.value.copy(
                        balance = event.balanceInput
                    )
                )
            }
            is TipsAndDonateEvent.SendTipOptionSuccess -> {
                setState(
                    uiState.value.copy(
                        sendSuccessTipOption = true
                    )
                )
            }
        }
    }

    fun selectedTipOption(tipsOption: TipsPackageOptionInfo?){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(TipsAndDonateEvent.SelectedTipOption(tipOptionSelected = tipsOption))
        }
    }

    fun sendAction(item: TipsPackageOptionInfo){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(TipsAndDonateEvent.ShowLoading(true))
            /* Check Balance */
            if(availableBalance(item)){
                triggerStateEvent(TipsAndDonateEvent.SendTipOptionSuccess)
            }else{
                triggerStateEvent(TipsAndDonateEvent.NavigationPackage)
            }
            triggerStateEvent(TipsAndDonateEvent.ShowLoading(false))
        }
    }

    private fun availableBalance(tipOption: TipsPackageOptionInfo) : Boolean{
        val balanceWallet = uiState.value.balance ?: 0F
        val priceOption = tipOption.coin?:0F
        if(balanceWallet >= priceOption && balanceWallet != 0F){
            return true
        }
        return false
    }

    fun onRechargeNavigation(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(TipsAndDonateEvent.ShowLoading(true))
            /* Check Balance */
            triggerStateEvent(TipsAndDonateEvent.NavigationPackage)
            triggerStateEvent(TipsAndDonateEvent.ShowLoading(false))
        }
    }

    private fun getBalanceWallet(){
        viewModelScope.launch(Dispatchers.IO) {
            paymentUseCase.getTotalCoin().collectLatest { result ->
                when(result){
                    is Result.Loading -> {
                        triggerStateEvent(TipsAndDonateEvent.ShowLoading(true))
                    }
                    is Result.Error -> {
                        triggerStateEvent(TipsAndDonateEvent.ShowLoading(false))
                    }
                    is Result.Success -> {
                        triggerStateEvent(TipsAndDonateEvent.UpdateBalance(result.data?.total ?: 0F))
                        triggerStateEvent(TipsAndDonateEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    fun reCharge(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(TipsAndDonateEvent.ShowLoading(true))
            val itemSelected = uiState.value.tipOptionSelected
            val balanceItemSelected = itemSelected?.coin ?: 0F
            val balanceWallet = uiState.value.balance ?: 0F
            if(balanceItemSelected < balanceWallet  || balanceWallet == 0F){
                triggerStateEvent(TipsAndDonateEvent.NavigationPackage)
            }else{
                triggerStateEvent(TipsAndDonateEvent.NavigationPaymentMethods)
            }
            triggerStateEvent(TipsAndDonateEvent.ShowLoading(false))
        }
    }

    fun clear(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(TipsAndDonateEvent.ResetState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}