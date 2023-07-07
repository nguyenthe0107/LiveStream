package olmo.wellness.android.ui.screen.see_all_upcoming

import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.screen.for_you.viewmodel.ForYouEvent
import java.util.*

data class SeeAllState(
    var isLoading: Boolean = true,
    val listUpComing: List<LiveSteamShortInfo>? = null,
    val dateSelect : Date = Date(),
    val listEventCalendar : List<KalendarEvent>? = null
)

sealed class SeeAllEvent {
    data class ShowLoading(val isLoading: Boolean) : SeeAllEvent()

    data class ListUpComing(val listUpComing: List<LiveSteamShortInfo>?) : SeeAllEvent()

    data class DateSelect(val dateSelect: Date) : SeeAllEvent()

    data class UpdateEventCalendar (var data : List<KalendarEvent>) : SeeAllEvent()

}