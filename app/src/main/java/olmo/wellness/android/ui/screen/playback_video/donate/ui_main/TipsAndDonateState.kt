package olmo.wellness.android.ui.screen.playback_video.donate.ui_main

import olmo.wellness.android.domain.tips.TipsPackageOptionInfo
import olmo.wellness.android.ui.screen.playback_video.donate.data.TipsType

data class TipsAndDonateState(
    val showLoading: Boolean = false,
    val listTips: List<TipsType> = mutableListOf(),
    val listOptions : List<TipsPackageOptionInfo> = mutableListOf(),
    val tipOptionSelected: TipsPackageOptionInfo ?= null,
    val isNavigationPackage: Boolean ?= null,
    val isNavigationPaymentMethods: Boolean ?= null,
    val balance : Float ?= 0F,
    val sendSuccessTipOption : Boolean ?= null
)

sealed class TipsAndDonateEvent {
    data class ShowLoading(val isLoading: Boolean) : TipsAndDonateEvent()
    data class UpdateListTipsOption(val data: List<TipsPackageOptionInfo>) : TipsAndDonateEvent()
    data class SelectedTipOption(val tipOptionSelected: TipsPackageOptionInfo?) : TipsAndDonateEvent()
    object OnRecharge : TipsAndDonateEvent()
    object NavigationPackage: TipsAndDonateEvent()
    object NavigationPaymentMethods: TipsAndDonateEvent()
    object SendTipOptionSuccess: TipsAndDonateEvent()
    data class UpdateBalance(val balanceInput: Float): TipsAndDonateEvent()
    object ResetState: TipsAndDonateEvent()
}