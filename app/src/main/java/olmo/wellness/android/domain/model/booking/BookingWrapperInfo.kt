package olmo.wellness.android.domain.model.booking

data class BookingWrapperInfo(
    val totalMoney: Float?,
    val bookingId: Double?=null,
    val serviceSessionConfirmId: Double?,
    val sessionSecretId: String ?= null
)