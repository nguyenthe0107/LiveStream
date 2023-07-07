package olmo.wellness.android.domain.model.booking

data class BookingInputWrapper(
    val serviceBooking: ServiceBooking?,
    val bookingIdResponse: Double?=null,
    val sessionConfirmId: Double?=null,
)