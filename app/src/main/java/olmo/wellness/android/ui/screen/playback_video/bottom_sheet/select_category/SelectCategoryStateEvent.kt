package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category

import olmo.wellness.android.domain.model.livestream.LiveCategory

data class SelectCategoryState(
    val isLoading: Boolean? = true,
    val listCategories: List<LiveCategory>? = null,
)

sealed class SelectCategoryEvent {
    data class ShowLoading(
        val isLoading: Boolean
    ): SelectCategoryEvent()

    data class ListCategoriesLoaded(
        val listCategories: List<LiveCategory>
    ): SelectCategoryEvent()
}