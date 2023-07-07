package olmo.wellness.android.data.model.schedule

import olmo.wellness.android.domain.model.livestream.LiveCategory

data class FillDataSchedule(
    var title: String? = "",
    var description: String? = null,
    var isPrivate: Boolean? = null,
    var isEvent: Boolean? = null,
    var listCategory: List<LiveCategory>? = null,
    var dateCreate : Long?=null,
    var thumbnailUrl : String?=null,
    val servicesId : List<Int> ?= emptyList()
)