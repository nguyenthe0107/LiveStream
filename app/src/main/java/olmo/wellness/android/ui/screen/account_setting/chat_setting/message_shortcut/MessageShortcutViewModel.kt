package olmo.wellness.android.ui.screen.account_setting.chat_setting.message_shortcut

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.core.fieldsOf
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.model.user_message.UserMessageShortcut
import olmo.wellness.android.domain.model.user_setting.UserSetting
import olmo.wellness.android.domain.model.user_setting.UserSettingRequest
import olmo.wellness.android.domain.use_case.GetProfileUseCase
import olmo.wellness.android.domain.use_case.GetUserInfoUseCase
import olmo.wellness.android.domain.use_case.UserMessageShortcutUseCase
import olmo.wellness.android.domain.use_case.UserSettingsUseCase
import olmo.wellness.android.extension.WTF
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class MessageShortcutViewModel@Inject constructor(
    application: Application,
    private val getUserLocal : GetUserInfoUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val userSettingsUseCase: UserSettingsUseCase,
    private val userMessageShortcut: UserMessageShortcutUseCase,
) : BaseViewModel(application)  {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel : StateFlow<ProfileInfo> = _profileModel

    private val _userId = MutableStateFlow<Int>(0)

    private val _defaultShowShortcutMode: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val defaultShowShortcutMode : StateFlow<Boolean?> = _defaultShowShortcutMode

    private val _listShortcuts: MutableStateFlow<List<UserMessageShortcut>?> = MutableStateFlow(null)
    val listShortcuts: StateFlow<List<UserMessageShortcut>?> get() = _listShortcuts

    init {
        getProfile()
        getUserMessageShortcut()
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

//            val filed = "[\"id\",\"name\",\"bio\",\"gender\",\"birthday\",\"avatar\",\"phoneNumber\",\"email\"]"
            val filed = fieldsOf(
                "id",
                "name",
                "bio",
                "gender",
                "birthday",
                "avatar",
                "phoneNumber",
                "email"
            )
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
                val filed = fieldsOf(
                    "id",
                    "createdAt",
                    "lastModified",
                    "isPrivateActivity",
                    "isAllowContactSyncing",
                    "isAllowShareProfile",
                    "language",
                    "isAllowChatOnLivestream",
                    "isAllowPrivateChatOnLivestream",
                    "isAllowSendAutoReply",
                    "autoReply",
                    "isShowMessageShortcut",
                    "serviceUpdate",
                    "orderUpdate",
                    "yourCircle",
                    "promotions",
                    "olmoFeed",
                    "livestream",
                    "walletUpdate",
                )
                userSettingsUseCase.invoke(userId,filed ).collectLatest {
                    when(it){
                        is Result.Success -> {
                            if(it.data?.isNotEmpty() == true){
                                val response = it.data.last()
                                _defaultShowShortcutMode.value = response.isShowMessageShortcut?:false
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

    private fun getUserMessageShortcut(){
        viewModelScope.launch {
            val profile = sharedPrefs.getProfile()
            if(profile.id == null)
                return@launch

            val userId = profile.id
            val fields = fieldsOf(
                "id",
                "messageShortcut"
            )
            userMessageShortcut.getMessageShortcuts(userId, fields).collectLatest {
                    _listShortcuts.value = listOf(
                        UserMessageShortcut(
                            messageShortcut = "shortcut đasada2đ",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "short",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "shortcut đasada2đ",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "shortcut 3 dsadsadasdsad asdsa dsa đá ádsa",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "shortcut 3 dsadsadasdsad t 3 dsadsadasdsad t 3 dsadsadasdsad t 3 dsadsadasdsad t 3 dsadsadasdsad asdsa dsa đá ádsa",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "shortcut 1",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "shortcut đasada2đ",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "short",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "shortcut đasada2đ",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "shortcut 3 dsadsadasdsad asdsa dsa đá ádsa",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "shortcut 3 dsadsadasdsad t 3 dsadsadasdsad t 3 dsadsadasdsad t 3 dsadsadasdsad t 3 dsadsadasdsad asdsa dsa đá ádsa",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "shortcut 1",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "shortcut đasada2đ",
                        ),
                        UserMessageShortcut(
                            messageShortcut = "short",
                        ),
                    )
                    when(it){
                        is Result.Success -> {
                            if(it.data?.isNotEmpty() == true){
                                val response = it.data.last()
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



    fun updateUserSetting(isShowShortCut: Boolean){
        viewModelScope.launch {
            _isLoading.value = true
            if(_userId.value != 0){
                val updateBody = UserSettingRequest(
                    UserSetting(isShowMessageShortcut = isShowShortCut)
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
}