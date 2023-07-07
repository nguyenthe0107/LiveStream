package olmo.wellness.android.data.model.live_stream

enum class SectionType(val value: String) {
    UPCOMING("Upcoming"),
    LIVE_NOW("LiveNow"),
    TOP_TRENDING("TopTrending"),
    EVENT("Event"),
    RECOMMENDED("Recommended"),
    ALL("All"),
    UN_KNOW("UnKnow");

    companion object {
        operator fun invoke(rawValue: String) = values().find { it.value == rawValue } ?: UPCOMING
    }
}