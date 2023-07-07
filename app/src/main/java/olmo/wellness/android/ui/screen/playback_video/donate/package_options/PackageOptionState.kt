package olmo.wellness.android.ui.screen.playback_video.donate.package_options
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.domain.tips.PricePackageInfo

data class PackageOptionState(
    val showLoading: Boolean = false,
    val listOptions : List<PackageOptionInfo> = mutableListOf(),
    val tipOptionSelected: PackageOptionInfo ?= null,
    val isNavigationPackage: Boolean ?= null,
    val isNavigationPaymentMethods: Boolean ?= null,
    val balance: Float? = 0F
)

sealed class PackageOptionEvent {
    data class ShowLoading(val isLoading: Boolean) : PackageOptionEvent()
    data class UpdateListPackageOption(val data: List<PackageOptionInfo>) : PackageOptionEvent()
    data class SelectedPackageOption(val tipOptionSelected: PackageOptionInfo?) : PackageOptionEvent()
    object OnRecharge : PackageOptionEvent()
    data class UpdateBalanceWallet(val balanceInput: Float?): PackageOptionEvent()
    data class UpdatePricePackage(val pricePackage: PricePackageInfo): PackageOptionEvent()
    object NavigationPackage: PackageOptionEvent()
    object ResetCurrentState: PackageOptionEvent()
    data class NavigationPaymentMethods(val tipOptionSelected: PackageOptionInfo?): PackageOptionEvent()
}