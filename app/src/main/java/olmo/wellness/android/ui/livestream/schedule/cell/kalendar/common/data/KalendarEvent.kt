package olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data

import java.time.LocalDate

/**
 * [KalendarEvent] handles the event marked on any
 * @param[date] with specific
 * @param[eventName] and its
 * @param[eventDescription]
 */
data class KalendarEvent(
    val date: LocalDate,
    val eventName: String?,
    var number : Int,
    val eventDescription: String? = null,
)
