package olmo.wellness.android.domain.model.livestream

import olmo.wellness.android.ui.livestream.stream.data.TypeTitleLivestream

data class QueryLiveStreamWrap(
    val typeTitle: TypeTitleLivestream,
    val userId: Int?=null,
    val limit : Int?=null,
    val page : Int?=null
)
