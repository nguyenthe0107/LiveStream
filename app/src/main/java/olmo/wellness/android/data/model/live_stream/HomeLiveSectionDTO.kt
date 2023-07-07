package olmo.wellness.android.data.model.live_stream

import olmo.wellness.android.domain.model.livestream.HomeLiveSectionData
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo

data class HomeLiveSectionDTO(
    val title: String?,
    val type: String?,
    val data: List<LiveSteamShortInfo>?
)

fun HomeLiveSectionDTO.toDomain(): HomeLiveSectionData {
    return HomeLiveSectionData(this.title, this.type, this.data)
}