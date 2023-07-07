package olmo.wellness.android.ui.screen.following_profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.model.user_follow.UserFollowInfo
import olmo.wellness.android.domain.model.user_follow.UserFollowRequest
import olmo.wellness.android.domain.model.user_follow.UserFollowRequestInfo
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class FollowingProfileViewModel @Inject constructor(
    application: Application,
    private val livestreamUseCase: LivestreamUseCase,
    private val getInternalApiUseCase: GetApiUseCase,
) : BaseViewModel(application) {

    private val _userType = MutableStateFlow<UserTypeModel?>(null)
    val userType: StateFlow<UserTypeModel?> = _userType

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _liveStreamList = MutableStateFlow<List<LiveSteamShortInfo?>?>(null)
    val liveStreamList: StateFlow<List<LiveSteamShortInfo?>?> = _liveStreamList

    private val _liveDefaultInfo = MutableStateFlow<LiveSteamShortInfo?>(null)
    val liveDefaultInfo: StateFlow<LiveSteamShortInfo?> = _liveDefaultInfo

    private val _listVideoPin = MutableStateFlow<List<LivestreamInfo?>?>(null)
    val listVideoPin: StateFlow<List<LivestreamInfo?>?> = _listVideoPin

    private val _userFollow = MutableStateFlow<List<UserFollowInfo>?>(null)
    val userFollow: StateFlow<List<UserFollowInfo>?> = _userFollow

    private val _userFollower = MutableStateFlow<List<UserFollowInfo>?>(null)
    val userFollower: StateFlow<List<UserFollowInfo>?> = _userFollower

    private val _profile = MutableStateFlow<ProfileInfo?>(null)
    val profile: StateFlow<ProfileInfo?> = _profile

    private val _nameUser = MutableStateFlow<String?>(null)
    val nameUser: StateFlow<String?> = _nameUser

    private val _totalUnseenNotification = MutableStateFlow(0)
    val totalUnseenNotification: StateFlow<Int> = _totalUnseenNotification

    private val _unFollowSuccess = MutableStateFlow(false)
    val unFollowSuccess : StateFlow<Boolean> = _unFollowSuccess
    fun resetStatus(){
        _unFollowSuccess.update {
            false
        }
    }

    private val _followSuccess = MutableStateFlow(false)
    val followSuccess : StateFlow<Boolean> = _followSuccess
    fun resetFollowStatus(){
        _followSuccess.update {
            false
        }
    }

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _errBody = MutableStateFlow("")
    val errBody : StateFlow<String> = _errBody

    fun reload() {
        getListVideoForOwner()
    }

    fun bindData(liveSteamShortInfo: LiveSteamShortInfo?){
        if(liveSteamShortInfo != null){
            _liveDefaultInfo.update {
                liveSteamShortInfo
            }
            getListVideoForOwner()
            getListPinVideo()
            getUserFollow()
            getUserFollower()
            getProfile()
        }
    }

    private fun getListVideoForOwner(){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = liveDefaultInfo.value?.userId
            livestreamUseCase.getListProfileLiveStream(userId = userId, page = 1, limit = 20).collectLatest { res ->
                when (res) {
                    is Result.Success -> {
                        if (res.data?.isNotEmpty() == true) {
                            _liveStreamList.value = res.data.map { liveInfo ->
                                liveInfo
                            }
                        }
                        _isLoading.value = false
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

    private fun getUserFollow() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = liveDefaultInfo.value?.userId
            livestreamUseCase.getUserFollowing(userId).collectLatest { res ->
                res.onResultReceived(
                    onSuccess = {
                        _isLoading.value = false
                        _userFollow.value = res.data
                    },
                    onError = {
                        _isLoading.value = false
                    },
                    onLoading = {
                        _isLoading.value = true
                    }
                )
            }
        }
    }

    private fun getUserFollower() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val userId = liveDefaultInfo.value?.userId
            livestreamUseCase.getUserFollower(userId).collectLatest { res ->
                res.onResultReceived(
                    onSuccess = {
                        _isLoading.value = false
                        _userFollower.value = res.data
                    },
                    onError = {
                        _isLoading.value = false
                    },
                    onLoading = {
                        _isLoading.value = true
                    }
                )
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            liveDefaultInfo.value?.userId.let { it ->
                val filed = "[\"id\",\"createdAt\",\"lastModified\",\"userName\",\"userType\",\"email\",\"phoneNumber\"]"
                val profileRequest = GetProfileRequest(it, filed)
                getInternalApiUseCase.getProfile(GetApiUseCase.Params(profileRequest)).collectLatest {
                    when (it) {
                        is Result.Success -> {
                            if (it.data?.isNotEmpty() == true) {
                                val response = it.data.last()
                                _profile.value = response
                                _nameUser.value = response.name.orEmpty()
                                if(response.userType != null){
                                    _userType.value = response.userType
                                    if(response.userType != UserTypeModel.BUYER){
                                        _nameUser.value = response.store?.name
                                    }
                                }
                            }
                            _isLoading.value = false
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
    }

    private fun getListPinVideo(){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = liveDefaultInfo.value?.userId
            if (userId != null) {
                livestreamUseCase.getPinStreamList(userId).collectLatest {
                    when (it) {
                        is Result.Success -> {
                            if (it.data?.isNotEmpty() == true) {
                                val response = it.data
                                _listVideoPin.value = response
                            }else{
                                _listVideoPin.value = emptyList()
                            }
                            _isLoading.value = false
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
    }

    fun getNotificationUnseen(){
        viewModelScope.launch(Dispatchers.IO) {
            val total = sharedPrefs.getTotalUnSeenNotification()
            _totalUnseenNotification.value = total
        }
    }

    fun postUserFollow(userId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            userId?.let {
                livestreamUseCase.postUserFollow(UserFollowRequest((it))).collectLatest { res ->
                    res.onResultReceived(
                        onSuccess = {
                            _isLoading.value = false
                            _followSuccess.update {
                                true
                            }
                            _liveDefaultInfo.value = liveDefaultInfo.value?.copy(isFollow = true)
                        },
                        onError = {
                            _isLoading.value = false
                            _followSuccess.update {
                                false
                            }
                            res.message.let {
                                _errBody.update {
                                    it
                                }
                            }
                        },
                        onLoading = {
                            _isLoading.value = true
                        }
                    )
                }
            }
        }
    }

    fun deleteUserFollowing(followedUserId: Int){
        val userId = sharedPrefs.getUserInfoLocal().userId ?: 0
        val userIdFinal = listOf(userId)
        val followedUserIdList = listOf(followedUserId)
        val userFollow = UserFollowRequestInfo(userId = userIdFinal, followedUserId = followedUserIdList)
        val query = Gson().toJson(userFollow)
        viewModelScope.launch(Dispatchers.IO) {
            livestreamUseCase.deleteUserFollow(query).collectLatest { res ->
                when(res){
                    is Result.Success -> {
                        _unFollowSuccess.value = true
                        _liveDefaultInfo.value = liveDefaultInfo.value?.copy(isFollow = false)
                        _isLoading.value = false
                    }
                    is Result.Error -> {
                        res.message.let {
                            _errBody.update {
                                it
                            }
                        }
                        _unFollowSuccess.value = false
                        _isLoading.value = false
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
                _isLoading.value = false
            }
        }
    }

}
