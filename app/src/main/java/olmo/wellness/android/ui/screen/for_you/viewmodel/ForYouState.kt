package olmo.wellness.android.ui.screen.for_you.viewmodel

import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.screen.for_you.data.TabData
import java.util.Date

data class ForYouState(
    var isLoading: Boolean = true,
    val listCategories: List<LiveCategory>? = null,
    val title: String? = null,
    val listSubCategories: List<LiveCategory>? = null,
    val listTab: List<TabData?>? = null,
    val listItemTypes: HashMap<String?, MutableList<LiveSteamShortInfo>?>? = hashMapOf(),
    val listFilterItem: MutableList<LiveSteamShortInfo>? = mutableListOf(),
    val dateSelect: Date = Date(),
    val listEventCalendar: List<KalendarEvent>? = null,
    val timeEvent: Long? = null,
)

sealed class ForYouEvent {
    data class ShowLoading(val isLoading: Boolean) : ForYouEvent()

    data class ListCategoriesLoaded(val listCategories: List<LiveCategory>) : ForYouEvent()

    data class UpdateListItemTypes(val listItemTypes: Map<String?, List<LiveSteamShortInfo>?>) : ForYouEvent()

    data class UpdateListUpcomingItem(val listFilter: List<LiveSteamShortInfo>?) : ForYouEvent()

    data class UpdateDateSelect(val dateSelect: Date) : ForYouEvent()

    data class SubListCategories(val subListCategories: List<LiveCategory>?, val title: String?) : ForYouEvent()

    data class UpdateListTab(val listTab: List<TabData?>) : ForYouEvent()

    data class UpdateEventCalendar(var data: List<KalendarEvent>) : ForYouEvent()

}