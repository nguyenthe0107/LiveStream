package olmo.wellness.android.domain.model.wrapper

import olmo.wellness.android.domain.model.livestream.LivestreamInfo

data class LiveStreamInfoWrapper(
    val isSelected : Boolean,
    val livestreamInfo: LivestreamInfo
)
