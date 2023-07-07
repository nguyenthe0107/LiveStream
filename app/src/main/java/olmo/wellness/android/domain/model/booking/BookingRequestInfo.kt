package olmo.wellness.android.domain.model.booking

import olmo.wellness.android.data.model.booking.BookingRequestModelDTO

data class BookingRequestInfo(
    val sessionConfirmId: Double?,
    val sessionSecret: String?
)

fun BookingRequestInfo.toBookingDTO(): BookingRequestModelDTO{
    return BookingRequestModelDTO(
        sessionConfirmId = sessionConfirmId,
        sessionSecret = sessionSecret
    )
}
