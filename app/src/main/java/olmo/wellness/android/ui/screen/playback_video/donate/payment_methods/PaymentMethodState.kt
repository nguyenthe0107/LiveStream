package olmo.wellness.android.ui.screen.playback_video.donate.payment_methods

import olmo.wellness.android.domain.model.payment.PaymentRequireWrapper
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.domain.tips.PricePackageInfo
import olmo.wellness.android.ui.screen.playback_video.donate.data.PaymentMethodModel

data class PaymentMethodState(
    val showLoading: Boolean = false,
    val paymentMethodViewSelected: PaymentMethodModel ?= null,
    val paymentMethods: List<PaymentMethodModel> ?= emptyList(),
    val packageOptionSelected: PackageOptionInfo ?= null,
    val isNavigationWebView: Boolean ?= false,
    val pricePackageInfo: PricePackageInfo ?= null,
    val urlPayment: String ?= null,
    val paymentRequireWrapper: PaymentRequireWrapper?= null
)

sealed class PaymentMethodEvent {
    data class ShowLoading(val isLoading: Boolean) : PaymentMethodEvent()
    object OnPay : PaymentMethodEvent()
    data class UpdatePaymentMethod(val paymentMethodViewModel: List<PaymentMethodModel>?): PaymentMethodEvent()
    data class OnPaymentMethodSelected(val paymentMethodViewModel: PaymentMethodModel): PaymentMethodEvent()
    data class UpdatePackageSelected(val packageOptionSelected: PackageOptionInfo?) : PaymentMethodEvent()
    data class UpdatePaymentRequired(val paymentRequireWrapper: PaymentRequireWrapper?= null) : PaymentMethodEvent()
    data class NavigationWebView(val urlPayment: String?) : PaymentMethodEvent()
    data class UpdatePricePackage(val pricePackageInfo: PricePackageInfo?): PaymentMethodEvent()
    object ResetNavigationWebView : PaymentMethodEvent()
}