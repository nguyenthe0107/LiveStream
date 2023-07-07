package olmo.wellness.android.ui.screen.account_setting

import android.app.Application
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.use_case.GetApiUseCase
import olmo.wellness.android.domain.use_case.GetProfileUseCase
import olmo.wellness.android.domain.use_case.NotificationUseCase
import olmo.wellness.android.domain.use_case.socket.connection.DisconnectSessionUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.helpers.getUniqueDeviceId
import olmo.wellness.android.ui.screen.account_setting.common.logoutAccount
import olmo.wellness.android.webrtc.rtc.RtcUtils
import javax.inject.Inject

@HiltViewModel
class AccountSwitcherViewModel @Inject constructor(
    application: Application,
    private val getApiUseCase: GetApiUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val disconnectSessionUseCase: DisconnectSessionUseCase,
) : BaseViewModel(application) {
    private val _isLoading = MutableStateFlow(false)
    val isNeedLoading: StateFlow<Boolean> = _isLoading

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel: StateFlow<ProfileInfo> = _profileModel

    private val _nameUser = MutableStateFlow<String?>(null)
    val nameUser: StateFlow<String?> = _nameUser

    private val _isLogoutSuccess = MutableStateFlow<Boolean>(false)
    val isLogoutSuccess: StateFlow<Boolean> = _isLogoutSuccess

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            var profile = sharedPrefs.getProfile()
            val userLocal = sharedPrefs.getUserInfoLocal()
            getApiUseCase.getUserInfo().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data.let { response ->
                            if (userLocal.userTypeModel != UserTypeModel.BUYER) {
                                _nameUser.value = response?.store?.name.orEmpty()
                                profile = profile.copy(
                                    name = response?.store?.name.orEmpty(),
                                    storeName = response?.store?.name.orEmpty(),
                                    avatar = response?.avatar,
                                    store = response?.store
                                )
                            } else {
                                _nameUser.value = response?.name
                                profile = profile.copy(name = response?.name, avatar = response?.avatar)
                            }
                            _profileModel.update {
                                profile
                            }
                            saveLocal(profile)
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

    private fun saveLocal(responseApi: ProfileInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPrefs.setProfile(responseApi)
        }
    }

    fun handleLogout() {
        logoutAccount(scope = viewModelScope,
            notificationUseCase = notificationUseCase,
            disconnectSessionUseCase = disconnectSessionUseCase,
            isLoading = _isLoading,
            isLogoutSuccess = _isLogoutSuccess)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}