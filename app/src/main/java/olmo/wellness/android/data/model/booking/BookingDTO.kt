package olmo.wellness.android.data.model.booking

import olmo.wellness.android.domain.model.booking.BookingInfo

data class BookingDTO(
    val id: Double,
    val status: String,
    val createAt: Long
)

fun BookingDTO.toBookingDomain(): BookingInfo {
    return BookingInfo(
        id, status, createAt
    )
}
