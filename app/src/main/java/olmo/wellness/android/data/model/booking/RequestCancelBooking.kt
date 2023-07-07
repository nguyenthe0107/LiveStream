package olmo.wellness.android.data.model.booking

data class RequestCancelBooking(
    val serviceSessionConfirmId : Int,
    val reason : String ?= null
)
