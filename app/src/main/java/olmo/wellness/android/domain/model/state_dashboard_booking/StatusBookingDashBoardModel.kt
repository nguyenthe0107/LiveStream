package olmo.wellness.android.domain.model.state_dashboard_booking

import olmo.wellness.android.R

sealed class StatusBookingDashBoardModel(val type: String, val name : Int){
    object Draft : StatusBookingDashBoardModel("Draft", R.string.state_draft_booking)
    object PaymentPending : StatusBookingDashBoardModel("PAYMENT_PENDING", R.string.state_payment_pending_booking)
    object Fail : StatusBookingDashBoardModel("FAILED", R.string.state_failed_booking)
    object Completed : StatusBookingDashBoardModel("COMPLETED", R.string.state_completed_booking)
    object NoUsed : StatusBookingDashBoardModel("NOT_USED", R.string.state_not_used_booking)
    object Used : StatusBookingDashBoardModel("USED", R.string.state_used_booking)
    object RequestToCancel : StatusBookingDashBoardModel("REQUEST_CANCEL", R.string.state_request_to_cancel_booking)
    object NoRefund : StatusBookingDashBoardModel("NOT_REFUNDED", R.string.state_no_refund_booking)
    object Refunded : StatusBookingDashBoardModel("REFUNDED", R.string.state_refunded_booking)
    object RefundPending : StatusBookingDashBoardModel("REFUND_PENDING", R.string.state_refund_pending_booking)
}
