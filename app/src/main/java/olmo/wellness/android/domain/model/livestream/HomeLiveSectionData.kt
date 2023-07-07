package olmo.wellness.android.domain.model.livestream

import olmo.wellness.android.data.model.live_stream.SectionType

data class HomeLiveSectionData(
    val title: String?,
    val type: String?,
    val data: List<LiveSteamShortInfo>?,
){
    fun getSectionType(): SectionType {
       return when(this.type){
            SectionType.UPCOMING.value -> SectionType.UPCOMING
            SectionType.LIVE_NOW.value -> SectionType.LIVE_NOW
            SectionType.EVENT.value -> SectionType.EVENT
            SectionType.RECOMMENDED.value -> SectionType.RECOMMENDED
            else -> SectionType.UN_KNOW
        }
    }
}