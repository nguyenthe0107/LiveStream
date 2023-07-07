package olmo.wellness.android.ui.screen.playback_video.onlive

import olmo.wellness.android.core.BaseState
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.WrapperUrlPayment
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.tips.PackageOptionInfo

data class PlayBackOnLiveState(
    val errMessage: String = "",
    val message: String? = null,
    val showLoading: Boolean = false,
    val livestreamList: List<LiveSteamShortInfo>? = null,
    val totalFollower: Int? = null,
    val packageOptionSelected: PackageOptionInfo? = null,
    val serviceBookingShow: ServiceBooking? = null,
    var serviceBookings: List<ServiceBooking>?= null,
    val bookingServiceFromSelection: ServiceBooking ?= null,
    val wrapperUrlPayment: WrapperUrlPayment?=null,
    override var timeUpdate: Long? = System.currentTimeMillis(),
) : BaseState()

sealed class PlayBackOnLiveEvent {
    data class ShowLoading(
        val isLoading: Boolean = false,
    ) : PlayBackOnLiveEvent()

    data class LoadDefaultValueSuccess(
        val livestreamList: List<LiveSteamShortInfo>? = null,
    ) : PlayBackOnLiveEvent()

    data class AddFirstDefaultLiveInfo(
        val livestream: LiveSteamShortInfo,
    ) : PlayBackOnLiveEvent()

    data class GetUserFollow(val totalFollower: Int?) : PlayBackOnLiveEvent()

    data class GetError(
        val errMessage: String?,
    ) : PlayBackOnLiveEvent()

    data class UpdatePackageSelected(val packageOptionInfo: PackageOptionInfo?) :
        PlayBackOnLiveEvent()

    data class UpdateServiceBookingShow(val data: ServiceBooking?) : PlayBackOnLiveEvent()
    data class UpdateServiceBookings(val data : List<ServiceBooking>?) : PlayBackOnLiveEvent()
    data class UpdateServiceBookingSelectFromList(val serviceBooking: ServiceBooking?): PlayBackOnLiveEvent()
    data class UpdateWrapperPayment(val wrapperUrlPayment: WrapperUrlPayment?) : PlayBackOnLiveEvent()
}
