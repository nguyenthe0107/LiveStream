package olmo.wellness.android.domain.model.booking

data class WrapperUrlPayment(
    val url: String,
    val bookingId: Double?=null,
): java.io.Serializable
