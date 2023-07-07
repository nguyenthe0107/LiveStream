package olmo.wellness.android.ui.screen.playback_video.explore

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.chat.ConnectionState
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.profile.BodyProfileRequest
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.WrapperUrlPayment
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.livestream.QueryLiveStreamWrap
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.model.user_follow.UserFollowRequest
import olmo.wellness.android.domain.model.user_follow.UserFollowRequestInfo
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.domain.use_case.LivestreamUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsKey
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.livestream.stream.data.LivestreamStatus
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ExplorePlayBackViewModel @Inject constructor(
    private val livestreamUseCase: LivestreamUseCase,
) : BaseViewModel<PlayBackState, PlayBackEvent>() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun setLoading(status: Boolean) {
        _isLoading.value = status
    }

    private var roomId: String? = null
    private var hostRoomId: Int? = null
    private var heartCountServer: Int = 0
    private val _unFollowSuccess = MutableStateFlow(false)
    val unFollowSuccess : StateFlow<Boolean> = _unFollowSuccess
    fun resetStatus(){
        _unFollowSuccess.update {
            false
        }
    }

    override fun initState(): PlayBackState {
        return PlayBackState()
    }

    private fun fetchPlayBacks(liveDataInput: LiveSteamShortInfo) {
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            try {
                val query = QueryLiveStreamWrap(liveDataInput.typeTitle)
                livestreamUseCase.getAllLiveStream(query).collectLatest { res ->
                    res.onResultReceived(
                        onSuccess = {
                            if (res.data?.isNotEmpty() == true) {
                                var listData = res.data
                                val videoById = listData.find { it.id == liveDataInput.id }
                                //Log.e("WTF", " videoById $videoById")
                                //Log.e("WTF", " liveDataInput $liveDataInput")
                                listData = if (videoById != null) {
                                    listData.toMutableList().apply {
                                        if (liveDataInput.status?.trim() == LivestreamStatus.ON_LIVE.name) {
                                            add(
                                                0,
                                                videoById.copy(
                                                    title = liveDataInput.title,
                                                    recordUrl = videoById.playbackUrl,
                                                    typeTitle = liveDataInput.typeTitle
                                                )
                                            )
                                        } else {
                                            add(
                                                0,
                                                videoById.copy(
                                                    title = liveDataInput.title,
                                                    recordUrl = if(liveDataInput.fromSearch){
                                                        liveDataInput.recordUrl
                                                    }else{
                                                        videoById.recordUrl
                                                    },
                                                    typeTitle = liveDataInput.typeTitle
                                                )
                                            )
                                        }
                                    }
                                } else {
                                    listData.toMutableList().apply {
                                        if (liveDataInput.status?.trim() == LivestreamStatus.ON_LIVE.name) {
                                            add(
                                                0,
                                                liveDataInput.copy(
                                                    title = liveDataInput.title,
                                                    recordUrl = liveDataInput.playbackUrl,
                                                    typeTitle = liveDataInput.typeTitle
                                                )
                                            )
                                        } else {
                                            add(
                                                0,
                                                liveDataInput.copy(
                                                    title = liveDataInput.title,
                                                    recordUrl = liveDataInput.recordUrl,
                                                    typeTitle = liveDataInput.typeTitle
                                                )
                                            )
                                        }
                                    }
                                }
                                triggerStateEvent(
                                    PlayBackEvent.LoadDefaultValueSuccess(
                                        livestreamList = listData
                                    )
                                )
                            }
                            _isLoading.value = false
                        },
                        onError = { err ->
                            triggerStateEvent(PlayBackEvent.ShowLoading(false))
                            triggerStateEvent(PlayBackEvent.GetError(err))
                        },
                        onLoading = {
                            triggerStateEvent(PlayBackEvent.ShowLoading(true))
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

    private fun getListVideoForOwner(liveDataInput: LiveSteamShortInfo){
        viewModelScope.launch(Dispatchers.IO) {
            val userInfo = sharedPrefs.getUserInfoLocal()
            val userId = userInfo.userId
            livestreamUseCase.getListProfileLiveStream(userId = userId, page = 1, limit = 20).collectLatest { res ->
                when (res) {
                    is Result.Success -> {
                        if (res.data?.isNotEmpty() == true) {
                            var listData = res.data
                            val videoById = listData.find { it.id == liveDataInput.id }
                            listData = if (videoById != null) {
                                listData.toMutableList().apply {
                                    if (liveDataInput.status?.trim() == LivestreamStatus.ON_LIVE.name) {
                                        add(
                                            0,
                                            videoById.copy(
                                                title = liveDataInput.title,
                                                recordUrl = videoById.playbackUrl,
                                                typeTitle = liveDataInput.typeTitle,
                                                fromProfile = liveDataInput.fromProfile
                                            )
                                        )
                                    } else {
                                        add(
                                            0,
                                            videoById.copy(
                                                title = liveDataInput.title,
                                                recordUrl = videoById.recordUrl,
                                                typeTitle = liveDataInput.typeTitle,
                                                fromProfile = liveDataInput.fromProfile
                                            )
                                        )
                                    }
                                }
                            } else {
                                listData.toMutableList().apply {
                                    if (liveDataInput.status?.trim() == LivestreamStatus.ON_LIVE.name) {
                                        add(
                                            0,
                                            liveDataInput.copy(
                                                title = liveDataInput.title,
                                                recordUrl = liveDataInput.playbackUrl,
                                                typeTitle = liveDataInput.typeTitle,
                                                fromProfile = liveDataInput.fromProfile
                                            )
                                        )
                                    } else {
                                        add(
                                            0,
                                            liveDataInput.copy(
                                                title = liveDataInput.title,
                                                recordUrl = liveDataInput.recordUrl,
                                                typeTitle = liveDataInput.typeTitle,
                                                fromProfile = liveDataInput.fromProfile
                                            )
                                        )
                                    }
                                }
                            }
                            triggerStateEvent(
                                PlayBackEvent.LoadDefaultValueSuccess(
                                    livestreamList = listData
                                )
                            )
                        }
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }

    override fun onTriggeredEvent(event: PlayBackEvent) {
        when (event) {
            is PlayBackEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        showLoading = event.isLoading
                    )
                )
            }

            is PlayBackEvent.LoadDefaultValueSuccess -> {
                setState(
                    uiState.value.copy(
                        showLoading = false,
                        livestreamList = uiState.value.livestreamList?.toMutableList()?.apply {
                            addAll(0, event.livestreamList ?: emptyList())
                        }?.toList() ?: event.livestreamList
                    )
                )
            }

            is PlayBackEvent.AddFirstDefaultLiveInfo -> {
                if (event.livestream.title?.isNotEmpty() == true) {
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
            is PlayBackEvent.UpdateConnectionState -> {
                when (event.connectionState) {
                    ConnectionState.CONNECTING -> {

                    }
                    ConnectionState.CONNECTED -> {
                    }
                    ConnectionState.DISCONNECTED -> {
                    }
                }
            }

            is PlayBackEvent.UpdatePackageSelected -> {
                setState(
                    uiState.value.copy(
                        packageOptionSelected = event.packageOptionInfo
                    )
                )
            }

            is PlayBackEvent.UpdateWrapperPayment -> {
                setState(
                    uiState.value.copy(
                        wrapperUrlPayment = event.wrapperUrlPayment
                    )
                )
            }

            is PlayBackEvent.UpdateServiceBookingSelectFromList -> {
                setState(
                    uiState.value.copy(
                        bookingServiceFromSelection = event.serviceBooking
                    )
                )
            }

        }
    }

    fun pushTopDefaultLiveInfo(liveData: LiveSteamShortInfo) {
        viewModelScope.launch(Dispatchers.Default) {
            val userInfo = sharedPrefs.getUserInfoLocal()
            if(userInfo.userTypeModel != null){
                when(userInfo.userTypeModel){
                    UserTypeModel.BUYER -> {
                        when {
                            liveData.fromProfile -> {
                                getListVideoForOwner(liveData)
                            }
                            else -> {
                                fetchPlayBacks(liveData)
                            }
                        }
                    }
                    else -> {
                        if(liveData.fromProfile){
                            getListVideoForOwner(liveData)
                        }else{
                            fetchPlayBacks(liveData)
                        }
                    }
                }
            }else{
                fetchPlayBacks(liveData)
            }
        }
    }

    fun getUserFollow() {
        viewModelScope.launch(Dispatchers.Default) {
            triggerStateEvent(PlayBackEvent.ShowLoading(true))
            val projection = "[\"id\",\"createdAt\",\"lastModified\",\"userId\",\"businessId\"]"
            livestreamUseCase.getUserFollow(projection = projection).collectLatest { res ->
                res.onResultReceived(
                    onSuccess = {
                        triggerStateEvent(PlayBackEvent.ShowLoading(false))
                        triggerStateEvent(
                            PlayBackEvent.GetUserFollow(it?.size)
                        )
                    },
                    onError = { err ->
                        triggerStateEvent(PlayBackEvent.ShowLoading(false))
                        triggerStateEvent(
                            PlayBackEvent.GetError(err)
                        )
                    },
                    onLoading = {
                        triggerStateEvent(PlayBackEvent.ShowLoading())
                    }
                )
            }
        }
    }

    fun postUserFollow(userId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            userId?.let {
                livestreamUseCase.postUserFollow(UserFollowRequest((it))).collectLatest { res ->
                    res.onResultReceived(
                        onSuccess = {
                            triggerStateEvent(PlayBackEvent.ShowLoading(false))
                        },
                        onError = { err ->
                            triggerStateEvent(PlayBackEvent.ShowLoading(false))
                            triggerStateEvent(
                                PlayBackEvent.GetError(err)
                            )
                        },
                        onLoading = {
                            triggerStateEvent(PlayBackEvent.ShowLoading(true))
                        }
                    )
                }
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


    fun updateWrapperPayment(wrapperUrlPayment: WrapperUrlPayment){
        triggerStateEvent(PlayBackEvent.UpdateWrapperPayment(wrapperUrlPayment))
    }

    fun updateServiceBookingFromList(serviceBooking: ServiceBooking?){
        triggerStateEvent(PlayBackEvent.UpdateServiceBookingSelectFromList(serviceBooking))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
