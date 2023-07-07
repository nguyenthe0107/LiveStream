package olmo.wellness.android.ui.screen.playback_video.contact_information

import olmo.wellness.android.domain.model.payment.PaymentRequireWrapper
import olmo.wellness.android.domain.model.voucher.VoucherInfo

data class ContactInformationServiceState(
    val isLoading: Boolean = false,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val isValidatePhone: Boolean? = null,
    val isValidateEmail: Boolean? = null,
    val isValidateFirstName: Boolean? = null,
    val isValidateLastName: Boolean? = null,
    val voucherInfo: VoucherInfo ?= null,
    val urlPayment: String ?= null,
    val totalMoney: Float ?= null
)

sealed class ContactInformationServiceEvent {
    data class ShowLoading(
        val isLoading: Boolean
    ) : ContactInformationServiceEvent()
    data class OnBindData(val isLoading: Boolean) : ContactInformationServiceEvent()
    data class UpdateFirstName(val input: String): ContactInformationServiceEvent()
    data class ValidateFirstName(val status: Boolean): ContactInformationServiceEvent()
    data class UpdateLastName(val input: String): ContactInformationServiceEvent()
    data class ValidateLastName(val status: Boolean): ContactInformationServiceEvent()
    data class UpdatePhone(val input: String): ContactInformationServiceEvent()
    data class UpdateEmail(val input: String): ContactInformationServiceEvent()
    data class ValidatePhone(val status: Boolean): ContactInformationServiceEvent()
    data class ValidateEmail(val status: Boolean): ContactInformationServiceEvent()
    data class UpdateVoucher(val voucherInfo: VoucherInfo?): ContactInformationServiceEvent()
    object ClearVoucher : ContactInformationServiceEvent()
    data class UpdateMoney(val totalMoney: Float) : ContactInformationServiceEvent()
    data class NavigationWebView(val urlPayment: String?) : ContactInformationServiceEvent()
    object ClearUrlPayment : ContactInformationServiceEvent()
}
