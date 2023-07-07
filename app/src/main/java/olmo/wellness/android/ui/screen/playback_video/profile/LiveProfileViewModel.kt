package olmo.wellness.android.ui.screen.playback_video.profile

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.core.enums.UploadFileType
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.profile.update.ProfileUpdateRequest
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.livestream.QueryLiveStreamModel
import olmo.wellness.android.domain.model.livestream.toLiveShortItem
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.profile.ProfileUpdateInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.model.user_follow.UserFollowInfo
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.livestream.stream.data.UpdateLivestreamRequest
import olmo.wellness.android.ui.livestream.stream.data.UpdateLivestreamWrapRequest
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.UploadFileIdServerViewModel
import javax.inject.Inject

@HiltViewModel
class LiveProfileViewModel @Inject constructor(
    application: Application,
    private val livestreamUseCase: LivestreamUseCase,
    private val getInternalApiUseCase: GetApiUseCase,
    private val getRoomChatUseCase: RoomChatUseCase,
    val getUploadUrlInfoUseCase: GetUploadIdServerUrlInfoUseCase,
    val uploadFileUseCase: UploadFileIdServerUseCase,
    uploadUrlInfoUseCase : GetUploadUrlInfoUseCase,
    private val setProfileInfoUseCase: SetProfileInfoWellnessUseCase,
) : UploadFileIdServerViewModel(application, getUploadUrlInfoUseCase,uploadUrlInfoUseCase ,uploadFileUseCase) {

    private val _userType = MutableStateFlow<UserTypeModel?>(null)
    val userType: StateFlow<UserTypeModel?> = _userType

    private val _isLoading = MutableStateFlow(false)
    val isNeedLoading: StateFlow<Boolean> = _isLoading

    private val _liveSteamShortInfo = MutableStateFlow<List<LiveSteamShortInfo?>?>(null)
    val liveSteamShortInfo: StateFlow<List<LiveSteamShortInfo?>?> = _liveSteamShortInfo

    private val _listVideoPin = MutableStateFlow<List<LiveSteamShortInfo?>?>(null)
    val listVideoPin: StateFlow<List<LiveSteamShortInfo?>?> = _listVideoPin

    private val _userFollowing = MutableStateFlow<List<UserFollowInfo>?>(null)
    val userFollowing: StateFlow<List<UserFollowInfo>?> = _userFollowing

    private val _userFollower = MutableStateFlow<List<UserFollowInfo>?>(null)
    val userFollower: StateFlow<List<UserFollowInfo>?> = _userFollower

    private val _profile = MutableStateFlow<ProfileInfo?>(null)
    val profile: StateFlow<ProfileInfo?> = _profile

    private val _nameUser = MutableStateFlow<String?>(null)
    val nameUser: StateFlow<String?> = _nameUser

    private val _totalUnseenNotification = MutableStateFlow(0)
    val totalUnseenNotification: StateFlow<Int> = _totalUnseenNotification

    private val _totalMessage = MutableStateFlow(0)
    val totalMessage: StateFlow<Int> = _totalMessage

    private val _currentPage = MutableStateFlow(0)
    private val currentPage: StateFlow<Int> = _currentPage

    private val _reloadProfile = MutableStateFlow(false)
    val reloadProfile: StateFlow<Boolean> = _reloadProfile

    init {
        getProfile()
        getProfileFromIdApi()
        getListPinVideo()
        getUserType()
        getUserFollowing()
        getUserFollower()
        getTotalCountMes()
    }

    fun reload() {
        getProfile()
        getLiveStreamVideo()
    }

    fun getLiveStreamVideo() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = sharedPrefs.getUserInfoLocal().userId
            val limit = 20
            livestreamUseCase.getListProfileLiveStream(userId = userId, page = 1, limit = limit)
                .collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {
                            _isLoading.value = true
                        }
                        is Result.Success -> {
                            _isLoading.value = false
                            if (result.data?.isNotEmpty() == true) {
                                _liveSteamShortInfo.value = result.data.map { liveInfo ->
                                    liveInfo
                                }
                            }
                        }
                        is Result.Error -> {
                            _isLoading.value = false
                        }
                    }
                }
        }
    }

    private fun getUserFollowing() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val userId = sharedPrefs.getUserInfoLocal().userId
            livestreamUseCase.getUserFollowing(userId).collectLatest { res ->
                res.onResultReceived(
                    onSuccess = {
                        _isLoading.value = false
                        _userFollowing.value = res.data
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
            val userId = sharedPrefs.getUserInfoLocal().userId
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

    private fun getProfileFromIdApi() {
        viewModelScope.launch(Dispatchers.IO) {
            val userLocal = sharedPrefs.getUserInfoLocal()
            val filed = "[\"id\",\"createdAt\",\"lastModified\",\"userName\",\"userType\",\"email\",\"phoneNumber\"]"
            val profileRequest = GetProfileRequest(userLocal.userId, filed)
            getInternalApiUseCase.getProfile(GetApiUseCase.Params(profileRequest)).collectLatest {
                when (it) {
                    is Result.Success -> {
                        if (it.data?.isNotEmpty() == true) {
                            val response = it.data.last()
                            _profile.value = response
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


    private fun getUserType() {
        viewModelScope.launch(Dispatchers.IO) {
            val userLocal = sharedPrefs.getUserInfoLocal()
            if (userLocal.userTypeModel != null) {
                _userType.value = userLocal.userTypeModel
            }
        }
    }

    private fun getListPinVideo() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = sharedPrefs.getUserInfoLocal().userId ?: 0
            livestreamUseCase.getPinStreamList(userId).collectLatest {
                when (it) {
                    is Result.Success -> {
                        if (it.data?.isNotEmpty() == true) {
                            val response = it.data
                            _listVideoPin.value = response.map { info ->
                                info.toLiveShortItem()
                            }
                        } else {
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

    fun updatePinVideo(statusPin: Boolean, videoId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val liveStreamRequest = QueryLiveStreamModel(id = listOf(videoId))
            val requestJson = Gson().toJson(liveStreamRequest)
            livestreamUseCase.updateVideoStream(requestJson, createUpdateLiveRequest(statusPin))
                .collectLatest {
                    when (it) {
                        is Result.Success -> {
                            getListPinVideo()
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

    private fun createUpdateLiveRequest(statusPin: Boolean): UpdateLivestreamWrapRequest {
        val request = UpdateLivestreamRequest(
            status = null,
            isRecord = null,
            userId = null,
            isPin = statusPin
        )
        return UpdateLivestreamWrapRequest(request)
    }

    @SuppressLint("SuspiciousIndentation")
    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val userType = sharedPrefs.getUserInfoLocal()
            getInternalApiUseCase.getUserInfo().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data.let { response ->
                            if (userType.userTypeModel == UserTypeModel.BUYER) {
                                _nameUser.value = response?.name
                            } else {
                                if(response?.store?.name != null){
                                    _nameUser.value = response.store?.name
                                }else{
                                    if (userType.userTypeModel != UserTypeModel.BUYER) {
                                        if (sharedPrefs.getProfile().storeName?.isNotEmpty() == true) {
                                            _nameUser.value = sharedPrefs.getProfile().storeName
                                        }
                                    }
                                }
                            }
                            if(response?.avatar?.isNotEmpty() == true){
                                var profile = ProfileInfo(
                                    id = response.id,
                                    avatar = response.avatar
                                )
                                if(response.bio?.isNotEmpty() == true){
                                    profile = profile.copy(bio = response.bio)
                                }
                                _profile.value = profile
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

    fun getNotificationUnseen() {
        viewModelScope.launch(Dispatchers.IO) {
            val total = sharedPrefs.getTotalUnSeenNotification()
            _totalUnseenNotification.value = total
        }
    }

    fun getListVideoForBuyer() {
        val userInfo = sharedPrefs.getUserInfoLocal()
        if (userInfo.userTypeModel == UserTypeModel.BUYER) {
            viewModelScope.launch(Dispatchers.IO) {
                livestreamUseCase.getListProfileLiveStream(
                    userId = userInfo.userId,
                    page = 1,
                    limit = 20
                ).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            if (result.data?.isNotEmpty() == true) {
                                _liveSteamShortInfo.value = result.data.map { liveInfo ->
                                    liveInfo
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
    }

    private fun getTotalCountMes() {
        viewModelScope.launch(Dispatchers.IO) {
            getRoomChatUseCase.getTotalNotificationNotSeen().collectLatest { response ->
                when (response) {
                    is Result.Success -> {
                        if (response.data != null) {
                            _totalMessage.value = response.data.total
                        }
                    }
                }
            }
        }
    }

    /* Handle Upload Avatar */
    fun handleUploadCapture(imageSelectedUri: Uri){
        viewModelScope.launch(Dispatchers.IO) {
            val imageUpload = async { uploadFile(imageSelectedUri, UploadFileType.PROFILE) }
            val resultImage = imageUpload.await()
            if (resultImage!= null){
                updateAvatar(resultImage)
            }
        }
    }

    fun updateAvatar(avatar: String){
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            _reloadProfile.value = false
            val userId = sharedPrefs.getUserInfoLocal().userId
            if(userId != null){
                val profileInfo = ProfileUpdateInfo().copy(id = listOf(userId))
                val temp = Gson().toJson(profileInfo)
                val childRequest = ProfileUpdateRequest().copy(
                    avatar = avatar
                )
                val bodyRequest = ProfileBodyRequest(childRequest)
                val param = SetProfileInfoWellnessUseCase.Params(bodyRequest)
                setProfileInfoUseCase.invoke(temp, true, param).collect{ it ->
                    when(it){
                        is Result.Success -> {
                            _isLoading.value = false
                            val profileApi = it.data?.first()
                            if(profileApi?.avatar != null){
                                _profile.value = profile.value?.copy(
                                    avatar = profileApi.avatar
                                )
                                saveLocal(profileApi.avatar)
                            }
                            _reloadProfile.update {
                                true
                            }
                        }
                        is Result.Error -> {
                            _isLoading.value = false
                            _profile.update { profileInfo ->
                                profileInfo?.copy(avatar = avatar)
                            }
                        }
                        is Result.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }
            }
        }
    }

    private fun saveLocal(avatarLink: String?=null){
        viewModelScope.launch(Dispatchers.IO) {
            var profileInfo = sharedPrefs.getProfile()
            if(avatarLink?.isNotEmpty() == true){
                profileInfo = profileInfo.copy(avatar = avatarLink)
                sharedPrefs.setProfile(profileInfo)
            }
        }
    }

}
