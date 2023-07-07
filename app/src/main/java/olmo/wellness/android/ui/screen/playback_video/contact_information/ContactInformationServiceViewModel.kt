package olmo.wellness.android.ui.screen.playback_video.contact_information

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
import olmo.wellness.android.domain.model.voucher.VoucherInfo
import olmo.wellness.android.domain.model.voucher.VoucherRequest
import olmo.wellness.android.domain.use_case.PaymentUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.common.validate.emailValidator
import olmo.wellness.android.ui.common.validate.validateRegexPhone
import javax.inject.Inject

@HiltViewModel
class ContactInformationServiceViewModel @Inject constructor(private val paymentUseCase: PaymentUseCase):
    BaseViewModel<ContactInformationServiceState, ContactInformationServiceEvent>() {

    override fun initState(): ContactInformationServiceState {
        return ContactInformationServiceState()
    }

    override fun onTriggeredEvent(event: ContactInformationServiceEvent) {
        when(event){
            is ContactInformationServiceEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(isLoading = event.isLoading)
                )
            }

            is ContactInformationServiceEvent.UpdateFirstName -> {
                setState(
                    uiState.value.copy(firstName = event.input)
                )
            }

            is ContactInformationServiceEvent.UpdateLastName -> {
                setState(
                    uiState.value.copy(lastName = event.input)
                )
            }

            is ContactInformationServiceEvent.UpdatePhone -> {
                setState(
                    uiState.value.copy(phone = event.input)
                )
            }

            is ContactInformationServiceEvent.UpdateEmail -> {
                setState(
                    uiState.value.copy(email = event.input)
                )
            }

            is ContactInformationServiceEvent.ValidateFirstName -> {
                setState(
                    uiState.value.copy(isValidateFirstName = event.status)
                )
            }

            is ContactInformationServiceEvent.ValidateLastName -> {
                setState(
                    uiState.value.copy(isValidateLastName = event.status)
                )
            }

            is ContactInformationServiceEvent.ValidatePhone -> {
                setState(
                    uiState.value.copy(isValidatePhone = event.status)
                )
            }

            is ContactInformationServiceEvent.ValidateEmail -> {
                setState(
                    uiState.value.copy(isValidateEmail = event.status)
                )
            }

            is ContactInformationServiceEvent.UpdateVoucher -> {
                setState(
                    uiState.value.copy(voucherInfo = event.voucherInfo)
                )
            }

            ContactInformationServiceEvent.ClearVoucher -> {
                setState(
                    uiState.value.copy(voucherInfo = null)
                )
            }

            is ContactInformationServiceEvent.NavigationWebView -> {
                setState(
                    uiState.value.copy(urlPayment = event.urlPayment)
                )
            }

            ContactInformationServiceEvent.ClearUrlPayment -> {
                setState(
                    uiState.value.copy(urlPayment = null)
                )
            }

            is ContactInformationServiceEvent.UpdateMoney -> {
                setState(
                    uiState.value.copy(totalMoney = event.totalMoney)
                )
            }

            else -> {

            }
        }
    }

    fun updateFirstName(input: String){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(ContactInformationServiceEvent.UpdateFirstName(input = input))
            if(input.isNotEmpty()){
                triggerStateEvent(ContactInformationServiceEvent.ValidateFirstName(status = true))
            }else{
                triggerStateEvent(ContactInformationServiceEvent.ValidateFirstName(status = false))
            }
        }
    }

    fun updateLastName(input: String){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(ContactInformationServiceEvent.UpdateLastName(input = input))
            if(input.isNotEmpty()){
                triggerStateEvent(ContactInformationServiceEvent.ValidateLastName(status = true))
            }else{
                triggerStateEvent(ContactInformationServiceEvent.ValidateLastName(status = false))
            }
        }
    }

    fun updatePhone(input: String){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(ContactInformationServiceEvent.UpdatePhone(input = input))
            if(input.isNotEmpty()){
                val status = validateRegexPhone(input)
                triggerStateEvent(ContactInformationServiceEvent.ValidatePhone(status = status))
            }
        }
    }

    fun updateEmail(input: String){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(ContactInformationServiceEvent.UpdateEmail(input = input))
            if(input.isNotEmpty()){
                val status = emailValidator(input)
                triggerStateEvent(ContactInformationServiceEvent.ValidateEmail(status = status))
            }
        }
    }

    fun updateVoucherSelected(voucherInfo: VoucherInfo){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(ContactInformationServiceEvent.UpdateVoucher(voucherInfo = voucherInfo))
        }
    }

    fun clearVoucher(){
        triggerStateEvent(ContactInformationServiceEvent.ClearVoucher)
    }

    fun getContactWrapper(): PaymentRequireWrapper {
        val voucher = uiState.value.voucherInfo
        return PaymentRequireWrapper(
            billingFirstName = uiState.value.firstName,
            billingLastName = uiState.value.lastName,
            billingPhoneNumber = uiState.value.phone,
            billingEmail = uiState.value.email,
            voucherRequest = if(voucher != null){
                   VoucherRequest(voucherCode = voucher.voucher, voucherDbId = voucher.voucherDbId)
            }else {
                null
            },
        )
    }

    fun updateMoney(money: Float) {
        triggerStateEvent(ContactInformationServiceEvent.UpdateMoney(money))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}