package olmo.wellness.android.ui.screen.playback_video.donate.event

import olmo.wellness.android.domain.model.bank.BankInfo
import olmo.wellness.android.domain.tips.PackageOptionInfo

data class KeplerBalanceState(
    val showLoading: Boolean = false,
    val listBankInfo: MutableList<BankInfo> = mutableListOf(),
    val myBalance : Float? = 0f,
    val myReward : Float? = 0f,
    val listOptions : List<PackageOptionInfo> = mutableListOf(),
)

sealed class KeplerBalanceEvent{
    data class ShowLoading(val isLoading : Boolean): KeplerBalanceEvent()
    data class UpdateBanks(val data : List<BankInfo>?) : KeplerBalanceEvent()
    data class UpdateBalanceWallet(val balanceInput: Float?, val myReward : Float?): KeplerBalanceEvent()
    data class UpdateListPackageOption(val data: List<PackageOptionInfo>) : KeplerBalanceEvent()
}