package olmo.wellness.android.ui.screen.playback_video.donate.payment_methods

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.order.ModelableType
import olmo.wellness.android.data.model.order.OrderRequestBody
import olmo.wellness.android.data.model.order.PaymentMethodType
import olmo.wellness.android.domain.model.payment.PaymentRequireWrapper
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.domain.use_case.PaymentUseCase
import olmo.wellness.android.extension.showToast
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.screen.playback_video.donate.data.PaymentMethodModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PaymentMethodViewModel @Inject constructor(
    private val paymentUseCase: PaymentUseCase,
    private val application: Application,
) :
    BaseViewModel<PaymentMethodState, PaymentMethodEvent>() {

    init {
        getPaymentMethods()
    }

    private fun getPaymentMethods() {
        viewModelScope.launch(Dispatchers.IO) {
            val listPaymentMethods = listOf(
                PaymentMethodModel(
                    id = 1, name = "Domestic ATM",
                    description = "Onepay Payment Gateway", image = ""
                ),
                PaymentMethodModel(
                    id = 2, name = "International ATM (Visa/ Master Card/...)",
                    description = "Onepay Payment Gateway", image = ""
                )
            )
            triggerStateEvent(PaymentMethodEvent.UpdatePaymentMethod(listPaymentMethods))
        }
    }

    override fun initState(): PaymentMethodState {
        return PaymentMethodState()
    }

    override fun onTriggeredEvent(event: PaymentMethodEvent) {
        when (event) {
            is PaymentMethodEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        showLoading = event.isLoading
                    )
                )
            }

            is PaymentMethodEvent.OnPaymentMethodSelected -> {
                setState(
                    uiState.value.copy(
                        paymentMethodViewSelected = event.paymentMethodViewModel
                    )
                )
            }

            is PaymentMethodEvent.UpdatePaymentMethod -> {
                setState(
                    uiState.value.copy(
                        paymentMethods = event.paymentMethodViewModel
                    )
                )
            }

            is PaymentMethodEvent.UpdatePackageSelected -> {
                setState(
                    uiState.value.copy(
                        packageOptionSelected = event.packageOptionSelected
                    )
                )
            }

            is PaymentMethodEvent.UpdatePaymentRequired -> {
                setState(
                    uiState.value.copy(
                        paymentRequireWrapper = event.paymentRequireWrapper
                    )
                )
            }

            is PaymentMethodEvent.UpdatePricePackage -> {
                if (event.pricePackageInfo != null) {
                    setState(
                        uiState.value.copy(
                            pricePackageInfo = event.pricePackageInfo
                        )
                    )
                }
            }

            is PaymentMethodEvent.NavigationWebView -> {
                setState(
                    uiState.value.copy(
                        isNavigationWebView = true,
                        urlPayment = event.urlPayment
                    )
                )
            }

            is PaymentMethodEvent.ResetNavigationWebView -> {
                setState(
                    uiState.value.copy(
                        isNavigationWebView = false,
                        paymentRequireWrapper = null,
                        packageOptionSelected = null,
                        pricePackageInfo = null
                    )
                )
            }
        }
    }

    fun onPay(packageOptionInfo: PackageOptionInfo?){
        if (packageOptionInfo != null) {
            viewModelScope.launch(Dispatchers.IO) {
                triggerStateEvent(PaymentMethodEvent.ShowLoading(true))
                val bodyRequest = OrderRequestBody(
                    paymentMethod = PaymentMethodType.ONE_PAY.name,
                    modelableId = packageOptionInfo?.id ?: 0,
                    modelableType = packageOptionInfo?.option,
                    sessionSecret = UUID.randomUUID().toString()
                )
                paymentUseCase.createOrder(bodyRequest).collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {
                            triggerStateEvent(PaymentMethodEvent.ShowLoading(true))
                        }
                        is Result.Error -> {
                            triggerStateEvent(PaymentMethodEvent.ShowLoading(false))
                            viewModelScope.showToast(application = application,
                                text = result.message)
                        }
                        is Result.Success -> {
                            result.data?.redirectUrl?.let { sharedPrefs.setUrlPaymentMethod(it) }
                            triggerStateEvent(PaymentMethodEvent.NavigationWebView(result.data?.redirectUrl))
                        }
                    }
                }
            }
        } else {
            onPayBooking()
        }
    }

    private fun onPayBooking() {
        viewModelScope.launch(Dispatchers.IO) {
            val paymentRequireWrapperBooking = uiState.value.paymentRequireWrapper
            val voucherInfo = paymentRequireWrapperBooking?.voucherRequest
            val bookingId = paymentRequireWrapperBooking?.bookingId
            triggerStateEvent(PaymentMethodEvent.ShowLoading(true))
            val bodyRequest = OrderRequestBody(
                paymentMethod = PaymentMethodType.ONE_PAY.name,
                voucherInfo = if (voucherInfo?.voucherCode.isNullOrBlank()) {
                    null
                } else {
                    voucherInfo
                },
                modelableType = ModelableType.KP_BOOK.value,
                modelableId = bookingId?.toInt() ?: 0,
                sessionSecret = paymentRequireWrapperBooking?.sessionSecret
            )
            paymentUseCase.createOrder(bodyRequest).collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        triggerStateEvent(PaymentMethodEvent.ShowLoading(true))
                    }
                    is Result.Error -> {
                        triggerStateEvent(PaymentMethodEvent.ShowLoading(false))
                    }
                    is Result.Success -> {
                        result.data?.redirectUrl?.let { sharedPrefs.setUrlPaymentMethod(it) }
                        triggerStateEvent(PaymentMethodEvent.NavigationWebView(result.data?.redirectUrl))
                        triggerStateEvent(PaymentMethodEvent.ShowLoading(false))
                    }
                }
            }
        }
    }

    fun onBindData(
        packageOptionInfo: PackageOptionInfo? = null,
        paymentRequireWrapper: PaymentRequireWrapper? = null,
    ) {
        if (packageOptionInfo != null) {
            triggerStateEvent(PaymentMethodEvent.UpdatePackageSelected(packageOptionInfo))
            getPriceInfo(packageOptionInfo)
        }

        if (paymentRequireWrapper != null) {
            triggerStateEvent(PaymentMethodEvent.UpdatePaymentRequired(paymentRequireWrapper.copy(
                timeStamp = System.currentTimeMillis()
            )))
        }
    }

    private fun getPriceInfo(packageOptionInfo: PackageOptionInfo?=null){
        if (packageOptionInfo != null) {
            viewModelScope.launch(Dispatchers.IO) {
                paymentUseCase.getPricePackageInfo(packageOptionInfo.id).collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {
                            triggerStateEvent(PaymentMethodEvent.ShowLoading(true))
                        }
                        is Result.Error -> {
                            triggerStateEvent(PaymentMethodEvent.ShowLoading(false))
                        }
                        is Result.Success -> {
                            triggerStateEvent(PaymentMethodEvent.UpdatePricePackage(result.data?.copy(
                                timeStamp = System.currentTimeMillis()
                            )))
                            triggerStateEvent(PaymentMethodEvent.ShowLoading(false))
                        }
                    }
                }
            }
        }
    }

    fun onPaymentMethodSelected(paymentMethodViewModel: PaymentMethodModel) {
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(PaymentMethodEvent.OnPaymentMethodSelected(paymentMethodViewModel = paymentMethodViewModel.copy(
                selected = true)))
        }
    }

    fun resetState() {
        triggerStateEvent(PaymentMethodEvent.ResetNavigationWebView)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}