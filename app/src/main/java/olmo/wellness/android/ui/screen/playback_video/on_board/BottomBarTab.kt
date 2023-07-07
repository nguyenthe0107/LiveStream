package olmo.wellness.android.ui.screen.playback_video.on_board

import androidx.annotation.DrawableRes

data class BottomBarOnboardTabData(
    @DrawableRes val icon: Int,
    @DrawableRes val iconSelected: Int,
    val type: TabType,
){
    enum class TabType(val tabName: String) {
        HOME("Home"),
        FOR_YOU("For You"),
        LIVE("Live"),
        ACTIVITIES("Activities"),
        NOTIFICATION("Notification"),
        CALENDAR("Calendar"),
        PROFILE("Profile")
    }
}