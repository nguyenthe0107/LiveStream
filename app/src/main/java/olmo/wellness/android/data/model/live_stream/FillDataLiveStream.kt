package olmo.wellness.android.data.model.live_stream

import olmo.wellness.android.domain.model.livestream.LiveCategory

data class FillDataLiveStream(
    var title: String? = "",
    var hashtag: String? = "", var description: String? = "",
    var isPrivate: Boolean? = null, var isEvent: Boolean? = null,
    var listCategory : List<LiveCategory> ?= null,
    var listServiceIds : List<Int?>?=null,
    var serviceIds : List<Int> ?= null
)