package olmo.wellness.android.ui.screen.playback_video.onlive

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.data.model.profile.BodyProfileRequest
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.WrapperUrlPayment
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.livestream.QueryLiveStreamWrap
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.model.user_follow.UserFollowRequest
import olmo.wellness.android.domain.model.user_follow.UserFollowRequestInfo
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.domain.use_case.LivestreamUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsKey
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.livestream.stream.data.LivestreamStatus
import olmo.wellness.android.ui.livestream.stream.data.TypeTitleLivestream
import javax.inject.Inject

@HiltViewModel
class PlayBackOnLiveViewModel @Inject constructor(
    private val livestreamUseCase: LivestreamUseCase,
    private val bookingUseCase: BookingUseCase
) : BaseViewModel<PlayBackOnLiveState, PlayBackOnLiveEvent>() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _unFollowSuccess = MutableStateFlow(false)
    val unFollowSuccess : StateFlow<Boolean> = _unFollowSuccess
    fun resetStatus(){
        _unFollowSuccess.update {
            false
        }
    }

    override fun initState(): PlayBackOnLiveState {
        return PlayBackOnLiveState()
    }

    private val _currentIndex: MutableStateFlow<Int> = MutableStateFlow(-1)
    val currentIndex: StateFlow<Int> = _currentIndex

    private fun fetchPlayBacks(liveInput: LiveSteamShortInfo) {
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            try {
                val query = QueryLiveStreamWrap(TypeTitleLivestream.LiveNow)
                livestreamUseCase.getAllLiveStream(query).collectLatest { res ->
                    res.onResultReceived(
                        onSuccess = {
                            if (res.data?.isNotEmpty() == true) {
                                var listData = res.data.map { it.copy(isLiveStream = true) }
                                val videoById = listData.find { it.id == liveInput.id }
                                if (videoById != null) {
                                    listData = listData.toMutableList().apply {
                                        add(
                                            0, videoById.copy(
                                                title = liveInput.title,
                                                roomChat = liveInput.roomChat,
                                                roomChatId = liveInput.roomChatId,
                                                heartCount = videoById.heartCount,
                                                viewCount = videoById.viewCount,
                                                isStreaming = true,
                                                recordUrl = if (videoById.status == LivestreamStatus.ON_LIVE.name) {
                                                    if(liveInput.playbackUrl.isNullOrEmpty()){
                                                        videoById.playbackUrl
                                                    }else{
                                                        liveInput.playbackUrl
                                                    }
                                                } else {
                                                    if(liveInput.recordUrl.isNullOrEmpty()){
                                                        videoById.recordUrl
                                                    }else{
                                                        liveInput.recordUrl
                                                    }
                                                }
                                            )
                                        )
                                    }
                                } else {
                                    listData = listData.toMutableList().apply {
                                        add(
                                            0, liveInput.copy(
                                                title = liveInput.title,
                                                roomChat = liveInput.roomChat,
                                                roomChatId = liveInput.roomChatId,
                                                heartCount = liveInput.heartCount,
                                                viewCount = liveInput.viewCount,
                                                isStreaming = true,
                                                recordUrl = if (videoById?.status == LivestreamStatus.ON_LIVE.name) {
                                                    liveInput.playbackUrl
                                                } else {
                                                    liveInput.recordUrl
                                                }
                                            )
                                        )
                                    }
                                }
                                triggerStateEvent(
                                    PlayBackOnLiveEvent.LoadDefaultValueSuccess(
                                        livestreamList = listData
                                    )
                                )
                            }
                            _isLoading.value = false
                            triggerStateEvent(PlayBackOnLiveEvent.ShowLoading(false))
                        },
                        onError = {
                            triggerStateEvent(PlayBackOnLiveEvent.ShowLoading(false))
                            _isLoading.value = false
                        },
                        onLoading = {
                            triggerStateEvent(PlayBackOnLiveEvent.ShowLoading(true))
                            _isLoading.value = false
                        }
                    )
                }
            } catch (throwable: Exception) {
                print(throwable.stackTrace)
                _isLoading.value = false
            }
        }
    }

    override fun onTriggeredEvent(event: PlayBackOnLiveEvent) {
        when (event) {
            is PlayBackOnLiveEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        showLoading = event.isLoading
                    )
                )
            }

            is PlayBackOnLiveEvent.LoadDefaultValueSuccess -> {
                setState(
                    uiState.value.copy(
                        showLoading = false,
                        livestreamList = uiState.value.livestreamList?.toMutableList()?.apply {
                            addAll(0, event.livestreamList ?: emptyList())
                        }?.toList() ?: event.livestreamList
                    )
                )
            }

            is PlayBackOnLiveEvent.AddFirstDefaultLiveInfo -> {
                if (event.livestream.id != null) {
                    setState(
                        uiState.value.copy(
                            showLoading = false,
                            livestreamList = uiState.value.livestreamList?.toMutableList()?.apply {
                                add(0, event.livestream)
                            }?.toList() ?: listOf(
                                event.livestream
                            )
                        )
                    )
                }
            }
            is PlayBackOnLiveEvent.UpdatePackageSelected -> {
                setState(
                    uiState.value.copy(
                        packageOptionSelected = event.packageOptionInfo
                    )
                )
            }

            is PlayBackOnLiveEvent.UpdateServiceBookings ->{
                setState(
                    uiState.value.copy(
                        serviceBookings = event.data
                    )
                )
            }

            is PlayBackOnLiveEvent.UpdateServiceBookingSelectFromList -> {
                setState(
                    uiState.value.copy(
                        bookingServiceFromSelection = event.serviceBooking
                    )
                )
            }

            is PlayBackOnLiveEvent.UpdateWrapperPayment -> {
                setState(
                    uiState.value.copy(
                        wrapperUrlPayment = event.wrapperUrlPayment
                    )
                )
            }
        }
    }

    fun pushTopDefaultLiveInfo(liveData: LiveSteamShortInfo) {
        fetchPlayBacks(liveData)
    }

    fun getUserFollow() {
        viewModelScope.launch {
            triggerStateEvent(PlayBackOnLiveEvent.ShowLoading(true))
            val projection = "[\"id\",\"createdAt\",\"lastModified\",\"userId\",\"businessId\"]"
            livestreamUseCase.getUserFollow(projection = projection).collectLatest { res ->
                res.onResultReceived(
                    onSuccess = {
                        triggerStateEvent(PlayBackOnLiveEvent.ShowLoading(false))
                        triggerStateEvent(
                            PlayBackOnLiveEvent.GetUserFollow(it?.size)
                        )
                    },
                    onError = { err ->
                        triggerStateEvent(PlayBackOnLiveEvent.ShowLoading(false))
                        triggerStateEvent(
                            PlayBackOnLiveEvent.GetError(err)
                        )
                    },
                    onLoading = {
                        triggerStateEvent(PlayBackOnLiveEvent.ShowLoading())
                    }
                )
            }
        }
    }

    fun postUserFollow(userId: Int?) {
        viewModelScope.launch {
            userId?.let {
                triggerStateEvent(PlayBackOnLiveEvent.ShowLoading(true))
                livestreamUseCase.postUserFollow(UserFollowRequest((it))).collectLatest { res ->
                    res.onResultReceived(
                        onSuccess = {
                            triggerStateEvent(PlayBackOnLiveEvent.ShowLoading(false))
                        },
                        onError = { err ->
                            triggerStateEvent(PlayBackOnLiveEvent.ShowLoading(false))
                            triggerStateEvent(
                                PlayBackOnLiveEvent.GetError(err)
                            )
                        },
                        onLoading = {
                            triggerStateEvent(PlayBackOnLiveEvent.ShowLoading())
                        }
                    )
                }
            }
        }
    }

    fun setCurrentIndex(indexCurrent: Int) {
        if (indexCurrent != -1) {
            _currentIndex.update {
                indexCurrent
            }
        }
    }

    fun shareOnProfile(liveStreamId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val bodyRequest = BodyProfileRequest(livestreamId = liveStreamId)
            livestreamUseCase.postShareOnProfile(bodyRequest).collectLatest { result ->
                _isLoading.value = false
            }
        }
    }

    fun deleteUserFollowing(followedUserId: Int){
        val userId = sharedPrefs.getUserInfoLocal().userId ?: 0
        val userIdFinal = listOf(userId)
        val followedUserIdList = listOf(followedUserId)
        val userFollow = UserFollowRequestInfo(userId = userIdFinal, followedUserId = followedUserIdList)
        val query = Gson().toJson(userFollow)
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            livestreamUseCase.deleteUserFollow(query).collectLatest {
                _unFollowSuccess.value = true
                _isLoading.value = false
            }
        }
    }


    fun updatePackageSelected(packageOptionInfo: PackageOptionInfo){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(PlayBackOnLiveEvent.UpdatePackageSelected(packageOptionInfo))
        }
    }

    fun clearPackageSelected(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(PlayBackOnLiveEvent.UpdatePackageSelected(null))
        }
    }

    fun updateWrapperPayment(wrapperUrlPayment: WrapperUrlPayment){
        triggerStateEvent(PlayBackOnLiveEvent.UpdateWrapperPayment(wrapperUrlPayment))
    }

    fun updateServiceBookingFromList(serviceBooking: ServiceBooking?){
        triggerStateEvent(PlayBackOnLiveEvent.UpdateServiceBookingSelectFromList(serviceBooking))
    }

    fun trackingViewLiveStream(trackingType: AnalyticsKey, liveInput: LiveSteamShortInfo?){
        viewModelScope.launch(Dispatchers.IO) {
            val userInfo = sharedPrefs.getUserInfoLocal()
            val trackingModel = TrackingModel(
                user_id = userInfo.userId,
                livestream_id = liveInput?.id,
                livestream_tile = liveInput?.title,
                seller_name = liveInput?.user?.name
            )
            when(trackingType){
                AnalyticsKey.ActionLiveStartFirebase -> {
                    AnalyticsManager.getInstance()?.trackingLiveStreamStartContent(trackingModel)
                }
                AnalyticsKey.ActionClickLikeFirebase -> {
                    AnalyticsManager.getInstance()?.trackingClickLiveStream(trackingModel)
                }
                AnalyticsKey.ActionClickFollowFirebase -> {
                    AnalyticsManager.getInstance()?.trackingClickFollowStream(trackingModel)
                }
                AnalyticsKey.ActionSendCommentFirebase -> {
                    AnalyticsManager.getInstance()?.trackingSendCommentStream(trackingModel)
                }
                AnalyticsKey.ActionSendReportFirebase -> {
                    AnalyticsManager.getInstance()?.trackingSendReportStream(trackingModel)
                }
                AnalyticsKey.ActionSendMessageFirebase -> {
                    AnalyticsManager.getInstance()?.trackingSendMessageSellerStream(trackingModel)
                }
                AnalyticsKey.ActionShareSocialFirebase -> {
                    AnalyticsManager.getInstance()?.trackingShareSocialStream(trackingModel)
                }
                AnalyticsKey.ActionClickViewFirebase -> {
                    AnalyticsManager.getInstance()?.trackingClickView(trackingModel)
                }
                AnalyticsKey.ActionClickCloseViewFirebase -> {
                    AnalyticsManager.getInstance()?.trackingClickClose(trackingModel)
                }
                AnalyticsKey.ActionShareProfileFirebase -> {
                    AnalyticsManager.getInstance()?.trackingShareProfileStream(trackingModel)
                }
                AnalyticsKey.ActionClickMiniSizeFirebase -> {
                    AnalyticsManager.getInstance()?.trackingClickMiniSize(trackingModel)
                }
                AnalyticsKey.ActionLiveStreamEndFirebase -> {
                    AnalyticsManager.getInstance()?.trackingLiveStreamEnd(trackingModel)
                }
                else -> {

                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
