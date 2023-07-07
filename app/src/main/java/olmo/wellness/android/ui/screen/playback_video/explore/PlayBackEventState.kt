package olmo.wellness.android.ui.screen.playback_video.explore

import olmo.wellness.android.data.model.chat.ConnectionState
import olmo.wellness.android.data.model.chat.ReactionCount
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.WrapperUrlPayment
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.tips.PackageOptionInfo

data class PlayBackState(
    val errMessage: String = "",
    val message: String? = null,
    val showLoading: Boolean = false,
    val connectionState : ConnectionState?=null,
    val livestreamList: List<LiveSteamShortInfo>? = null,
    val reactionCount : ReactionCount?=null,
    val totalUserFollow: Int ?= null,
    val packageOptionSelected: PackageOptionInfo ?= null,
    val bookingServiceFromSelection: ServiceBooking ?= null,
    val wrapperUrlPayment: WrapperUrlPayment?=null,
)

sealed class PlayBackEvent{
    data class ShowLoading(val isLoading: Boolean = false): PlayBackEvent()

    data class LoadDefaultValueSuccess(val livestreamList: List<LiveSteamShortInfo>? = null): PlayBackEvent()

    data class AddFirstDefaultLiveInfo(val livestream: LiveSteamShortInfo): PlayBackEvent()

    data class DeleteUserFollow(val totalUserFollow: Int?) : PlayBackEvent()

    data class UpdateConnectionState(val connectionState: ConnectionState?): PlayBackEvent()

    data class GetUserFollow(val totalUserFollow: Int?) : PlayBackEvent()

    data class PostUserFollow(val totalUserFollow: Int?) : PlayBackEvent()

    data class GetError(val errMessage: String?) : PlayBackEvent()

    data class UpdatePackageSelected(val packageOptionInfo: PackageOptionInfo): PlayBackEvent()

    data class UpdateServiceBookingSelectFromList(val serviceBooking: ServiceBooking?): PlayBackEvent()
    data class UpdateWrapperPayment(val wrapperUrlPayment: WrapperUrlPayment?) : PlayBackEvent()

}
