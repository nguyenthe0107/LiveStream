package olmo.wellness.android.ui.screen.playback_video.home

import olmo.wellness.android.domain.model.livestream.HashTag
import olmo.wellness.android.domain.model.livestream.HomeLiveSectionData
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.model.profile.ProfileInfo

data class NewsFeedsState(
    val isLoading: Boolean? = null,
    val listBodySection: List<HomeLiveSectionData>? = null,
    val listCategories: List<LiveCategory>? = null,
    val listSubCategories: List<LiveCategory>? = null,
    val listHashTags: List<HashTag>? = null,
    val title: String? = null,
    val profile: ProfileInfo? = null,
    val totalUnseenNotification: Int = 0,
)


sealed class NewsFeedsEvent {
    data class ShowLoading(
        val isLoading: Boolean
    ) : NewsFeedsEvent()

    data class ListCategoriesLoaded(
        val listCategories: List<LiveCategory>
    ) : NewsFeedsEvent()

    data class ListBodySectionsLoaded(
        val listBodySection: List<HomeLiveSectionData>
    ) : NewsFeedsEvent()

    data class SubListCategories(val subListCategories: List<LiveCategory>?,val title: String?) : NewsFeedsEvent()

    data class ListHashTagsLoaded(
        val listHashTags: List<HashTag>
    ) : NewsFeedsEvent()

    data class ProfileInfoLoaded(
        val profile: ProfileInfo
    ) : NewsFeedsEvent()

    data class UpdateTotalUnseenNotification(
        val totalUnseenNotification: Int
    ) : NewsFeedsEvent()
}