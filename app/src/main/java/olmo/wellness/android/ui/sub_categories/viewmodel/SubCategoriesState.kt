package olmo.wellness.android.ui.sub_categories.viewmodel

import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo

data class SubCategoriesState(
    var isLoading: Boolean? = null,
    val listLiveNow: List<LiveSteamShortInfo>? = null,
    val listTab: List<String>? = null,
)

sealed class SubCategoriesEvent {
    data class ShowLoading(val isLoading: Boolean?) : SubCategoriesEvent()

    data class ListLiveNow(val listLiveNow: List<LiveSteamShortInfo>) : SubCategoriesEvent()

    data class ListTab(val listTab: List<String>) : SubCategoriesEvent()
}