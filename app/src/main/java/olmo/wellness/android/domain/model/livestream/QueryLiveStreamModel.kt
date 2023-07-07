package olmo.wellness.android.domain.model.livestream

data class QueryLiveStreamModel(
    val userId: List<Int>? = null,
    val isPin : Boolean? = null,
    val id : List<Int> ?= null,
)
