package olmo.wellness.android.domain.model.booking

data class TimeBookingInfo(
    val type: String?,
    val duration : Long?,
    val timestamps : List<Long>?
)
