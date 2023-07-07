package olmo.wellness.android.data.model.booking

import olmo.wellness.android.domain.model.booking.TimeBookingInfo

data class TimeBookingDTO(
    val type: String?,
    val duration : Long?,
    val timestamps : List<Long>?
)

fun TimeBookingDTO.toTimeBookingDomain() : TimeBookingInfo {
    return TimeBookingInfo(
        type = type,
        duration = duration,
        timestamps = timestamps
    )
}
