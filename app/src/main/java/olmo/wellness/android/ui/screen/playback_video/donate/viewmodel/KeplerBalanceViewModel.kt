package olmo.wellness.android.ui.screen.playback_video.donate.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.use_case.GetTipAndDonateUseCase
import olmo.wellness.android.domain.use_case.PaymentUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.screen.playback_video.donate.event.KeplerBalanceEvent
import olmo.wellness.android.ui.screen.playback_video.donate.event.KeplerBalanceState
import javax.inject.Inject

@HiltViewModel
class KeplerBalanceViewModel @Inject constructor(
    private val paymentUseCase: PaymentUseCase,
    private val tipsUseCase: GetTipAndDonateUseCase,
) :
    BaseViewModel<KeplerBalanceState, KeplerBalanceEvent>() {
    override fun initState(): KeplerBalanceState {
        return KeplerBalanceState()
    }

    init {
        getBalanceWallet()
        getPackageOption()
        getUserBankAccounts()
    }


    override fun onTriggeredEvent(event: KeplerBalanceEvent) {
        when (event) {
            is KeplerBalanceEvent.ShowLoading -> {
                setState(uiState.value.copy(showLoading = event.isLoading))
            }
            is KeplerBalanceEvent.UpdateBanks -> {
                setState(
                    uiState.value.copy(
                        listBankInfo = uiState.value.listBankInfo.apply {
                            event.data?.let {
                                addAll(
                                    it
                                )
                            }
                        },
                        showLoading = false
                    )
                )
            }
            is KeplerBalanceEvent.UpdateBalanceWallet -> {
                setState(uiState.value.copy(showLoading = false, myBalance = event.balanceInput, myReward = event.myReward))
            }
            is KeplerBalanceEvent.UpdateListPackageOption -> {
                setState(uiState.value.copy(showLoading = false, listOptions = event.data))
            }
        }
    }

    private fun getBalanceWallet() {
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(KeplerBalanceEvent.ShowLoading(true))
            paymentUseCase.getTotalCoin().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        triggerStateEvent(
                            KeplerBalanceEvent.UpdateBalanceWallet(
                                balanceInput = result.data?.total ?: 0F,
                                myReward = result.data?.price ?: 0F
                            )
                        )
                    }
                    is Result.Loading -> {
                        triggerStateEvent(KeplerBalanceEvent.ShowLoading(true))
                    }
                    is Result.Error -> {
                        triggerStateEvent(KeplerBalanceEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    private fun getUserBankAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            paymentUseCase.getUserBankAccounts().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        triggerStateEvent(KeplerBalanceEvent.UpdateBanks(result.data))
                    }
                    is Result.Loading -> {
                        triggerStateEvent(KeplerBalanceEvent.ShowLoading(true))
                    }
                    is Result.Error -> {
                        triggerStateEvent(KeplerBalanceEvent.ShowLoading(false))
                    }
                }
            }
        }
    }


    private fun getPackageOption() {
        viewModelScope.launch(Dispatchers.IO) {
            tipsUseCase.getPackageOptions().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        if (result.data?.isNotEmpty() == true) {
                            triggerStateEvent(KeplerBalanceEvent.UpdateListPackageOption(result.data))
                        }
                    }
                    is Result.Loading -> {
                        triggerStateEvent(KeplerBalanceEvent.ShowLoading(true))
                    }
                    is Result.Error -> {
                        triggerStateEvent(KeplerBalanceEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

}