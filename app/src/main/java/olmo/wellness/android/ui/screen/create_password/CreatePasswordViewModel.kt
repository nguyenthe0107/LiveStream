package olmo.wellness.android.ui.screen.create_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.data.model.ErrorResponseDTO
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.use_case.GetApiUseCase
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.analytics.TrackingConstants
import olmo.wellness.android.ui.common.validate.validatePassword
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CreatePasswordViewModel @Inject constructor(
    private val useCaseID: GetApiUseCase,
): ViewModel(){

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorBody = MutableSharedFlow<ErrorResponseDTO>()
    val errorBody = _errorBody.asSharedFlow()

    private val _onSuccessResponse = MutableSharedFlow<Boolean>()
    val isResetSuccess = _onSuccessResponse.asSharedFlow()

    private val _isEnableButton = MutableStateFlow(false)
    val isEnableButton : StateFlow<Boolean> = _isEnableButton

    private val _hasOnErrorPassword = MutableStateFlow(false)
    val hasOnErrorPassword : StateFlow<Boolean> = _hasOnErrorPassword

    private val _password = MutableStateFlow("")
    private val password : StateFlow<String> = _password
    private val _reInputPassword = MutableStateFlow("")
    private val reInputPassword : StateFlow<String> = _reInputPassword

    suspend fun sendToGetVerifyCode(identity: String, code: String, newPassword: String, isPhoneNumber: Boolean){
        viewModelScope.launch{
            _isLoading.value = true
            if(!validatePassword(newPassword)){
                _hasOnErrorPassword.update {
                    true
                }
                _isLoading.value = false
                return@launch
            }
            try {
                val resetPasswordRequest = if(isPhoneNumber){
                    VerifyCodeRequest(identity = "+".plus(identity.trim()), otp = code, newPassword = newPassword)
                }else{
                    VerifyCodeRequest(identity = identity.trim(), otp = code, newPassword = newPassword)
                }
                useCaseID.resetPassword(resetPasswordRequest).collect {
                    _onSuccessResponse.emit(true)
                }
                _hasOnErrorPassword.update {
                    false
                }
            }catch (throwable: Exception){
                if (throwable is HttpException) {
                    val errorBody: String? = throwable.response()?.errorBody()?.string()
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(errorBody, ErrorResponseDTO::class.java)
                    _errorBody.emit(errorResponse)
                }
                _isLoading.value = false
                _hasOnErrorPassword.update {
                    false
                }
            }
        }
    }

    fun isValidatePassword(password: String) : Boolean{
        if(password.isNotEmpty()){
            _hasOnErrorPassword.value = !validatePassword(password)
            return _hasOnErrorPassword.value
        }else{
            _hasOnErrorPassword.value = false
        }
        return false
    }

    fun sendTrackingSubmitNewPass(identity: String, isPhoneNumber: Boolean){
        val method = TrackingConstants.TRACKING_VALUE_SIGNIN_EMAIL
        if(isPhoneNumber){
            TrackingConstants.TRACKING_VALUE_SIGNIN_PHONE_NUMBER
        }else{
            TrackingConstants.TRACKING_VALUE_SIGNIN_EMAIL
        }
        val tracking = TrackingModel(
            account_name = identity,
            method = method
        )
        AnalyticsManager.getInstance()?.trackingClickSubmitNewPass(tracking)
    }

}