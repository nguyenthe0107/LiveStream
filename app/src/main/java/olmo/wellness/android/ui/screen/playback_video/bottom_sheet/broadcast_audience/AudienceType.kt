package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.broadcast_audience

enum class AudienceType constructor(val value: String) {
    EVERYONE("Everyone"),
    FOLLOWERS("Followers Only");

    companion object {
        operator fun invoke(rawValue: String) = AudienceType.values().find { it.value == rawValue } ?: AudienceType.EVERYONE
    }
}