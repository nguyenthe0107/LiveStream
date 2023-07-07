package olmo.wellness.android.ui.screen.playback_video.on_board

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.chat.ConnectionState
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.use_case.GetApiUseCase
import olmo.wellness.android.domain.use_case.socket.connection.GetConnectionStateUseCase
import olmo.wellness.android.domain.use_case.socket.connection.GetPingPongUseCase
import olmo.wellness.android.domain.use_case.socket.connection.RequestSessionUseCase
import olmo.wellness.android.domain.use_case.socket.connection.SendPingPongUseCase
import olmo.wellness.android.domain.use_case.socket.connection.*
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.webrtc.rtc.RtcUtils
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(
    application: Application,
    private val getConnectionStateUseCase: GetConnectionStateUseCase,
    private val requestSessionUseCase: RequestSessionUseCase,
    private val getProfileUseCase: GetApiUseCase,
    private val getPingPongUseCase: GetPingPongUseCase,
    private val sendPingPongUseCase: SendPingPongUseCase,
    private val disconnectSessionUseCase: DisconnectSessionUseCase,
) : BaseViewModel(application) {

    private val _userType = MutableStateFlow<UserTypeModel?>(UserTypeModel.BUYER)
    val userType: StateFlow<UserTypeModel?> = _userType

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel: StateFlow<ProfileInfo> = _profileModel

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isStart = MutableStateFlow(true)

    private val _job = MutableStateFlow<Job?>(null)

    private val TIME_DELAY = 10000L
    private var _countSendPing = MutableStateFlow(0)

    private val _totalUnseenNotification = MutableStateFlow(0)
    val totalUnseenNotification: StateFlow<Int> = _totalUnseenNotification
    val token = sharedPrefs.getToken()

    init {
        fetchUserLocal()
        setupConnectionRtc()
        sendTrackingHomeLoad()
    }

    private fun fetchUserLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _userType.value = (sharedPrefs.getUserInfoLocal().userTypeModel)
            _isLoading.value = false
            sharedPrefs.setLoginSuccess(true)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setupConnectionRtc() {
        if (_isStart.value) {
            RtcUtils.getConnection(getConnectionStateUseCase, onSuccess = { connectionState ->
                when (connectionState) {
                    ConnectionState.CONNECTED -> {
                        if (_job.value == null)
                        {
                            _job.value = startRepeatingJob(TIME_DELAY)
                        }else{
                            _job.value?.start()
                        }
                    }
                    ConnectionState.DISCONNECTED -> {
                        if (_job.value!=null) {
                            _job.value?.cancel()
                            _isStart.value=true
                        }
                    }
                }
            })
            RtcUtils.getPingPong(getPingPongUseCase, onSuccess = {
                _countSendPing.value = 0
            })
            _isStart.value = false
            RtcUtils.connectionServer(token,requestSessionUseCase)
        }
    }

    private fun startRepeatingJob(timeInterval: Long): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            while (NonCancellable.isActive) {
                if (_countSendPing.value >= 3) {
                        RtcUtils.connectionServer(token,requestSessionUseCase)
                    _countSendPing.value = 0
                }
                delay(timeInterval)
                RtcUtils.sendPingPong(sendPingPongUseCase)
                _countSendPing.value++
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            getProfileUseCase.getUserInfo().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data.let { response ->
                            if(response?.avatar?.isNotEmpty() == true){
                                _profileModel.update {
                                    ProfileInfo(id = response.id, avatar = response.avatar)
                                }
                            }
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

    fun reloadProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val profileLocal = sharedPrefs.getProfile()
            _profileModel.update {
                profileLocal
            }
        }
    }

    private fun sendTrackingHomeLoad(){
        viewModelScope.launch(Dispatchers.IO) {
            val userLocal = sharedPrefs.getUserInfoLocal()
            val trackingModel = TrackingModel(
                user_id = userLocal.userId,
                method = userLocal.methodTracking,
                isSuccess = true,
            )
            AnalyticsManager.getInstance()?.trackingHomeLoadSuccess(trackingModel)
        }
    }

}
