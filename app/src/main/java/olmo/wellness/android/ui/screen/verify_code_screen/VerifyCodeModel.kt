package olmo.wellness.android.ui.screen.verify_code_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.Result
import olmo.wellness.android.core.enums.UserRole
import olmo.wellness.android.data.model.ErrorResponseDTO
import olmo.wellness.android.data.model.LoginRequest
import olmo.wellness.android.data.model.definition.AuthMethod
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.fcm.AppUserRequest
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
import olmo.wellness.android.di.wrapper.AccessTokenWrapper
import olmo.wellness.android.domain.model.UserInfo
import olmo.wellness.android.domain.model.login.LoginData
import olmo.wellness.android.domain.model.login.UserInfoResponse
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.model.user_info.UserInfoLocal
import olmo.wellness.android.domain.model.wrapper.VerifyCodeWrapper
import olmo.wellness.android.domain.use_case.GetApiUseCase
import olmo.wellness.android.domain.use_case.NotificationUseCase
import olmo.wellness.android.domain.use_case.SetUserInfoUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.helpers.getUniqueDeviceId
import retrofit2.HttpException
import javax.inject.Inject

@SuppressLint("NewApi")
@HiltViewModel
class VerifyCodeModel @Inject constructor(
    private val useCaseOlmoInternal: GetApiUseCase,
    private val accessTokenWrapper: AccessTokenWrapper,
    private val notificationUseCase: NotificationUseCase,
    private val setUserInfoUseCase: SetUserInfoUseCase,
) : ViewModel(){

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorBody = MutableSharedFlow<ErrorResponseDTO>()
    val errorBody = _errorBody.asSharedFlow()

    private val _isOverLimitOtp = MutableStateFlow<Boolean>(false)
    val isOverLimitOtp : StateFlow<Boolean> = _isOverLimitOtp

    private val _otpValue = MutableStateFlow("")
    val otpValue: StateFlow<String> = _otpValue

    private val _userType = MutableStateFlow(UserTypeModel.BUYER)
    val userType: StateFlow<UserTypeModel> = _userType
    fun setUserType(userType: UserTypeModel){
        _userType.value = userType
    }

    private val _isNeedLogin = MutableStateFlow<Boolean>(false)
    val isNeedLogin : StateFlow<Boolean> = _isNeedLogin

    private val _verifyCodeModel = MutableStateFlow<VerifyCodeWrapper?>(null)
    private val verifyCodeModel: StateFlow<VerifyCodeWrapper?> = _verifyCodeModel
    fun bindVerifyCodeModel(input : VerifyCodeWrapper){
        _verifyCodeModel.update {
            if(input.isPhone == true){
                if(input.identity?.trim()?.contains("+", ignoreCase = true) == true){
                    input.identity = input.identity.orEmpty().trim()
                }else{
                    if(input.identity?.trim()?.contains("+", ignoreCase = true) == false){
                        input.identity = "+" + (input.identity?.trim())?.trim()
                    }
                }
            }else{
                input.identity = input.identity
            }
            input
        }
    }

    fun verifyUser(otp: String) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val code = VerifyCodeRequest(otp = otp, identity = verifyCodeModel.value?.identity?.trim())
                useCaseOlmoInternal.verifyConfirmCode(code).collectLatest{ result ->
                    when(result){
                        is Result.Success -> {
                            _isLoading.value = true
                            if(verifyCodeModel.value?.isForget == null || verifyCodeModel.value?.isForget == false){
                                val loginRequest = LoginRequest(
                                    authMethod = AuthMethod.USER_PASS,
                                    authData = arrayListOf(
                                        verifyCodeModel.value?.identity.orEmpty(),
                                        verifyCodeModel.value?.password.orEmpty()
                                    )
                                )
                                useCaseOlmoInternal.login(loginRequest).collectLatest { loginResult ->
                                    if (loginResult.data != null) {
                                        accessTokenWrapper.invokeAccessToken(loginResult.data.accessToken?.token)
                                        setUserInfo(loginData = loginResult.data)
                                        useCaseOlmoInternal.getUserInfo()
                                            .collectLatest { userInfoResponse ->
                                                when (userInfoResponse) {
                                                    is Result.Success -> {
                                                        handleSaveUserLocal(
                                                            loginResult.data,
                                                            userInfoResponse.data
                                                        )
                                                        accessTokenWrapper.invokeAccessToken(
                                                            loginResult.data.accessToken?.token
                                                        )
                                                        sharedPrefs.setToken(loginResult.data.accessToken?.token.orEmpty())
                                                        val trackingModel =
                                                            getTrackingModel(statusInput = true)
                                                        AnalyticsManager.getInstance()
                                                            ?.trackingConfirmedOTP(
                                                                trackingModel
                                                            )
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            val userId =
                                                                loginResult.data.userId
                                                            val deviceId =
                                                                getUniqueDeviceId()
                                                            val appUserRequest = AppUserRequest(
                                                                userId = userId ?: 0,
                                                                deviceId = deviceId,
                                                                firebaseToken = verifyCodeModel.value?.tokenDevice.orEmpty()
                                                            )
                                                            notificationUseCase.sendAppUser(
                                                                appUserRequest
                                                            ).collectLatest {}
                                                        }
                                                        _isSuccess.emit(true)
                                                        _isLoading.value = false
                                                        AnalyticsManager.getInstance()?.trackingSignUpSuccess()
                                                    }
                                                    is Result.Error -> {
                                                        _isLoading.value = false
                                                        _isNeedLogin.value = true
                                                    }
                                                }
                                            }
                                    }
                                }
                            }else{
                                val trackingModel = getTrackingModel(statusInput = true)
                                AnalyticsManager.getInstance()?.trackingConfirmedOTP(trackingModel)
                                _isLoading.value = false
                                _isSuccess.emit(true)
                            }
                        }

                        is Result.Loading -> {
                            _isLoading.value = true
                        }

                        is Result.Error -> {
                            _isLoading.value = false
                            handleExceptionWithMes(result.message)
                        }
                    }
                }
            } catch (throwable: Exception) {
                handleException(throwable)
            }
        }
    }

    private suspend fun handleException(throwable: Exception) {
        if (throwable is HttpException) {
            val errorBody: String? = throwable.response()?.errorBody()?.string()
            val errorResponse: ErrorResponseDTO =
                Gson().fromJson(errorBody, ErrorResponseDTO::class.java)
            _errorBody.emit(errorResponse)
            _isLoading.value = false
            val trackingModel = getTrackingModel(errMes = errorResponse.message, statusInput = false)
            AnalyticsManager.getInstance()?.trackingConfirmedOTP(trackingModel)
        } else {
            _isLoading.value = false
        }
    }

    private suspend fun handleExceptionWithMes(contentErr: String?) {
        _errorBody.emit(ErrorResponseDTO(message = contentErr ?: Constants.ERROR_COMMON, name = contentErr?: Constants.ERROR_COMMON))
        _isLoading.value = false
        val trackingModel = getTrackingModel(errMes = contentErr, statusInput = false)
        AnalyticsManager.getInstance()?.trackingConfirmedOTP(trackingModel)
    }

    private fun getTrackingModel(errMes: String?=null, statusInput: Boolean?): TrackingModel {
        val userLocal = sharedPrefs.getUserInfoLocal()
        return TrackingModel(
            isSuccess = statusInput,
            user_id = userLocal.userId ?: 0,
            account_name = verifyCodeModel.value?.identity,
            method = userLocal.methodTracking,
            err_message = errMes
        )
    }

    suspend fun resendOTP() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            if(verifyCodeModel.value?.isForget == true){
                try {
                    val bodyRequest = VerifyCodeRequest(identity = verifyCodeModel.value?.identity)
                    useCaseOlmoInternal.forgetPassword(bodyRequest).collect {
                        _isLoading.value = false
                    }
                }catch (ex: Exception){
                }
            }else{
                val bodyRequest = VerifyCodeRequest(identity = verifyCodeModel.value?.identity)
                try {
                    useCaseOlmoInternal.requestVerifyCode(bodyRequest).collect {
                        _isLoading.value = false
                    }
                }catch (ex: Exception){
                }
            }
            val trackingModel = getTrackingModel(statusInput = true)
            AnalyticsManager.getInstance()?.trackingResendOTP(trackingModel)
        }
    }

    fun getOTP(otpInput: String?) {
        if (otpInput != null && otpInput.isNotEmpty()) {
            _otpValue.value = otpInput
        }
    }

    fun resetLocal(){
        sharedPrefs.clearUserRole()
    }

    fun resetState(){
        _isSuccess.value = false
        _otpValue.value = ""
        _isNeedLogin.value = false
    }

    private fun handleSaveUserLocal(
        loginData: LoginData?,
        userInfo: UserInfoResponse?){
        loginData?.let {
            viewModelScope.launch {
                val userType =
                    UserTypeModel(
                        loginData.userType ?: UserTypeModel.BUYER.value
                    )
                val userInfoLocal = UserInfoLocal(
                    userId = loginData.userId,
                    identity = verifyCodeModel.value?.identity,
                    userTypeModel = userType,
                    auMethod = verifyCodeModel.value?.authMethod?.name,
                    name = userInfo?.name,
                    email = userInfo?.email,
                    avatar = userInfo?.avatar,
                    verified = it.verified ?: false,
                    store = userInfo?.store
                )
                sharedPrefs.setUserInfoLocal(userInfoLocal)
                sharedPrefs.setLoginSuccess(true)
                sharedPrefs.setTotalUnSeenNotification(0)
            }
        }
    }

    private fun setUserInfo(loginData: LoginData?) {
        loginData?.let {
            accessTokenWrapper.invokeAccessToken(loginData.accessToken?.token)
            viewModelScope.launch {
                setUserInfoUseCase(
                    SetUserInfoUseCase.Params(
                        UserInfo(
                            token = loginData.accessToken?.token.orEmpty(),
                            refreshToken = loginData.accessToken?.refreshToken.orEmpty(),
                            expiration = loginData.accessToken?.expiration ?: 0,
                            userId = loginData.userId ?: 0,
                            userType = loginData.userType ?: UserRole.BUYER.role
                        )
                    )
                )
            }
        }
    }

    fun updateOTP(value : String){
        if(value.isNotEmpty()){
            _otpValue.value = value
        }
    }

}