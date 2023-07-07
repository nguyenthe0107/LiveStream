package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.voucher_service

import olmo.wellness.android.domain.model.voucher.VoucherInfo
import olmo.wellness.android.domain.model.voucher.VoucherValidateInfo

data class VoucherServiceState(
    val isLoading: Boolean = false,
    val surname: String? = null,
    val voucherSelected: VoucherInfo?= null,
    val listVoucher: List<VoucherInfo> ?= emptyList(),
    val validateInfo: VoucherValidateInfo ?= null,
    val sessionConfirmId: Double?=null
)

sealed class VoucherServiceEvent {
    data class ShowLoading(
        val isLoading: Boolean
    ) : VoucherServiceEvent()
    data class UpdateListVoucher(val listVoucher: List<VoucherInfo>) : VoucherServiceEvent()
    data class OnSelectVoucher(val voucherSelected: VoucherInfo) : VoucherServiceEvent()
    data class UpdateSessionConfirmId(val sessionConfirmId: Double?) : VoucherServiceEvent()
    object ClearVoucher : VoucherServiceEvent()
}
