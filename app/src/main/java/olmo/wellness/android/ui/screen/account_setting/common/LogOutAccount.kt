package olmo.wellness.android.ui.screen.account_setting.common

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.data.model.fcm.AppUserRequest
import olmo.wellness.android.domain.use_case.NotificationUseCase
import olmo.wellness.android.domain.use_case.socket.connection.DisconnectSessionUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.helpers.getUniqueDeviceId
import olmo.wellness.android.webrtc.rtc.RtcUtils

fun logoutAccount(
    scope: CoroutineScope,
    notificationUseCase: NotificationUseCase,
    disconnectSessionUseCase: DisconnectSessionUseCase,
    isLoading: MutableStateFlow<Boolean>,
    isLogoutSuccess: MutableStateFlow<Boolean>,
) {
    scope.launch(Dispatchers.IO) {
        isLoading.value = true
        val userInfo = sharedPrefs.getUserInfoLocal()
        val deviceId = getUniqueDeviceId()
        val appUserRequest = AppUserRequest(userId = userInfo.userId ?: 0,
            deviceId = deviceId,
            firebaseToken = userInfo.firebaseToken.orEmpty())
        notificationUseCase.deleteAppUser(appUserRequest).collectLatest {
            isLoading.value = false
            isLogoutSuccess.value = true
            RtcUtils.disconnectServer(disconnectSessionUseCase)
            sharedPrefs.logOut()
        }
    }
}
