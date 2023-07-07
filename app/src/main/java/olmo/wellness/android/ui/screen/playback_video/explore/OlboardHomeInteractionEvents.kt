package olmo.wellness.android.ui.screen.playback_video.explore

import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo

sealed class OlboardHomeInteractionEvents {
    data class OpenProfile(val album: LiveSteamShortInfo?=null) : OlboardHomeInteractionEvents()
    data class ShareVideo(val album: LiveSteamShortInfo?=null) : OlboardHomeInteractionEvents()
    object OpenComments : OlboardHomeInteractionEvents()
    object GiftDonate : OlboardHomeInteractionEvents()
    object CloseComments : OlboardHomeInteractionEvents()
    object FollowAction : OlboardHomeInteractionEvents()
    object UnFollowAction : OlboardHomeInteractionEvents()
    object CloseLiveStream : OlboardHomeInteractionEvents()
    object SetInformationLiveStream : OlboardHomeInteractionEvents()
    object OpenModeBroadcast : OlboardHomeInteractionEvents()
    object SwitchCamera : OlboardHomeInteractionEvents()
    object ShowInfoStream : OlboardHomeInteractionEvents()
    object FilterLiveStream : OlboardHomeInteractionEvents()
    data class OpenPipMode(val lifeStreamInfo: LiveSteamShortInfo?, val duration: Long? = 0) : OlboardHomeInteractionEvents()
    object ClosePipMode : OlboardHomeInteractionEvents()
    object MuteSound : OlboardHomeInteractionEvents()
    object ImMuteSound : OlboardHomeInteractionEvents()
    object ShowListUser : OlboardHomeInteractionEvents()
    object ReactionHeart : OlboardHomeInteractionEvents()
    object MoreOption : OlboardHomeInteractionEvents()
    object NavigationToFollowProfile : OlboardHomeInteractionEvents()
    object AddBookService : OlboardHomeInteractionEvents()
    object BookNowService: OlboardHomeInteractionEvents()
    object CloseShowBookService: OlboardHomeInteractionEvents()
    data class OnBookingService(val booking: ServiceBooking?): OlboardHomeInteractionEvents()
}