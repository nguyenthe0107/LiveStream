package olmo.wellness.android.ui.screen.playback_video

import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo

data class MoreOptionWrapper(
    val isSelected : Boolean = false,
    val livestreamInfo: LiveSteamShortInfo?= null,
    val user: ChatMessage?= null
)