package olmo.wellness.android.domain.model.payment

import olmo.wellness.android.R
import olmo.wellness.android.domain.model.state_dashboard_booking.StatusBookingDashBoardModel

sealed class PaymentStatus(val type: String, val name : Int){
    object Draft : PaymentStatus("Draft", R.string.state_draft_booking)
    object SUCCESS : PaymentStatus("SUCCESS", R.string.state_draft_booking)
    object PaymentPending : PaymentStatus("PAYMENT_PENDING", R.string.state_payment_pending_booking)
    object Fail : PaymentStatus("FAILED", R.string.state_failed_booking)
}
