package olmo.wellness.android.data.model.booking

import olmo.wellness.android.data.model.user_info.UserInfoDTO
import olmo.wellness.android.data.model.user_info.toUserInfoDomain
import olmo.wellness.android.domain.model.booking.BookingHistoryInfo

data class BookingHistoryDTO(
    val serviceSessionInfo: ServiceSessionInfoDTO?,
    val userInfo: UserInfoDTO?,
    val billingInfo : BillingInfoDTO?,
    val paymentInfo: PaymentInfoDTO?,
    val bookingInfo: BookingDTO?
)

fun BookingHistoryDTO.toBookingHistoryDomain(): BookingHistoryInfo {
    return BookingHistoryInfo(
        serviceSessionInfo = serviceSessionInfo?.toServiceSessionDomain(),
        userInfo = userInfo?.toUserInfoDomain(),
        billingInfo = billingInfo?.toConvertBillingInfoDomain(),
        paymentInfo = paymentInfo?.totPaymentInfoDomain(),
        bookingInfo = bookingInfo?.toBookingDomain()
    )
}
