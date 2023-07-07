package olmo.wellness.android.data.model.booking

import olmo.wellness.android.domain.model.booking.BookingIdResponseInfo

data class BookingIdResponseDTO(
    val bookingId: Double?
)

fun BookingIdResponseDTO.toBookingIdDomain(): BookingIdResponseInfo{
    return BookingIdResponseInfo(
        bookingId
    )
}