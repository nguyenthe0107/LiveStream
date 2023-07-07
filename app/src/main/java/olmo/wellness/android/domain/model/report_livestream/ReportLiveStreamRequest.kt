package olmo.wellness.android.domain.model.report_livestream

data class ReportLiveStreamRequest(
    val livestreamId: List<Int> ?= null,
    val userId: List<Int> ?= null,
)

