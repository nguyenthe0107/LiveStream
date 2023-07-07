package olmo.wellness.android.domain.model.booking

import olmo.wellness.android.domain.model.user_info.UserInfoDomain
import java.io.Serializable

data class BookingHistoryInfo(
    val serviceSessionInfo: ServiceSessionInfo?,
    val userInfo: UserInfoDomain?,
    val billingInfo : BillingInfo?,
    val paymentInfo: PaymentInfo?,
    val bookingInfo: BookingInfo?,
    val timeStamp : Long ?= null,
    val type: String ?= null
): Serializable