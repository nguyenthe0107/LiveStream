package olmo.wellness.android.ui.screen.account_setting.share_profile

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
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.data.model.profile.business_address.BusinessUpdateDTO
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.model.user_setting.UserSetting
import olmo.wellness.android.domain.model.user_setting.UserSettingRequest
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.extension.WTF
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class ShareProfileViewModel @Inject constructor(application: Application,
                                                private val getUserLocal : GetUserInfoUseCase,
                                                private val getProfileUseCase: GetProfileUseCase,
                                                private val userSettingsUseCase: UserSettingsUseCase
) : BaseViewModel(application){

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel : StateFlow<ProfileInfo> = _profileModel

    private val _userId = MutableStateFlow<Int>(0)

    private val _defaultShareProfileMode: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val defaultShareProfileMode : StateFlow<Boolean?> = _defaultShareProfileMode

    init {
        getProfile()
    }

    private fun getProfile(){
        viewModelScope.launch {
            val profile = sharedPrefs.getProfile()
            if(profile.id != null){
                _profileModel.value = profile
                getUserSetting(profile.id)
                _userId.value = profile.id
                return@launch
            }

            val filed = "[\"id\",\"name\",\"bio\",\"gender\",\"birthday\",\"avatar\",\"phoneNumber\",\"email\"]"
            val profileRequest = GetProfileRequest(sharedPrefs.getUserInfoLocal().userId, filed)
            getProfileUseCase.invoke(GetProfileUseCase.Params(profileRequest)).collect {
                when(it){
                    is Result.Success -> {
                        if(it.data?.isNotEmpty() == true){
                            val response = it.data.last()
                            _profileModel.value = response.copy(
                                username = response.name
                            )
                            _userId.value = response.id?:0

                            if (_userId.value != 0){
                                getUserSetting(_userId.value)
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

    private fun getUserSetting(userId: Int){
        viewModelScope.launch {
            if(userId != 0){
                val filed = "[\"id\",\"createdAt\",\"lastModified\",\"isPrivateActivity\",\"isAllowContactSyncing\",\"isAllowShareProfile\",\"language\",\"isAllowChatOnLivestream\",\"isAllowPrivateChatOnLivestream\",\"isAllowSendAutoReply\",\"autoReply\",\"isShowMessageShortcut\",\"serviceUpdate\",\"orderUpdate\",\"yourCircle\",\"promotions\",\"olmoFeed\",\"livestream\",\"walletUpdate\"]"
                userSettingsUseCase.invoke(userId,filed ).collectLatest {
                    when(it){
                        is Result.Success -> {
                            if(it.data?.isNotEmpty() == true){
                                val response = it.data.last()
                                _defaultShareProfileMode.value = response.isAllowShareProfile?:false
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

    fun updateUserSetting(isSharing: Boolean){
        viewModelScope.launch {
            _isLoading.value = true
            if(_userId.value != 0){
                val updateBody = UserSettingRequest(
                    UserSetting(isAllowChatOnLivestream = isSharing)
                )
                val listUserId = listOf(_userId.value)
                val requestString = Gson().toJson(
                    UserSetting(
                        userId = listUserId)
                )
                userSettingsUseCase.updateUserSetting(requestString, true, updateBody ).collectLatest {
                    when(it){
                        is Result.Success -> {
                            if(it.data?.isNotEmpty() == true){
                                val response = it.data.last()
                                _isSuccess.value = true
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


    fun getListUser() : List<User>{
        val user = User("https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            "abc@gmail.com", 1, false, false, false )
        val user1 = User("https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            "abc@gmail.com", 2, false, false, false, )
        val user2 = User("https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            "abc@gmail.com", 3, false, false, false, )
        val user3 = User("https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            "abc@gmail.com", 4, false, false, false, )
        return listOf(user, user1, user2, user3)
    }
}