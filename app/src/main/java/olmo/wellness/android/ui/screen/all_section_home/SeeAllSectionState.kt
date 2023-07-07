package olmo.wellness.android.ui.screen.all_section_home

import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import java.util.*

data class SeeAllSectionState(
    var isLoading: Boolean? = null,
    val listUpComing: List<LiveSteamShortInfo>? = null,
)

sealed class SeeAllSectionEvent {
    data class ShowLoading(val isLoading: Boolean?) : SeeAllSectionEvent()

    data class ListUpComing(val listUpComing: List<LiveSteamShortInfo>?) : SeeAllSectionEvent()

}