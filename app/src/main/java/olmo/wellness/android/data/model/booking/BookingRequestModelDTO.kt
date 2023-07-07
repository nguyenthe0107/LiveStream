package olmo.wellness.android.data.model.booking

import olmo.wellness.android.domain.model.booking.BookingRequestInfo

data class BookingRequestModelDTO(
    val sessionConfirmId: Double?,
    val sessionSecret: String?
)

fun BookingRequestModelDTO.toBookingRequestDomain(): BookingRequestInfo{
    return BookingRequestInfo(
        sessionConfirmId,
        sessionSecret
    )
}
