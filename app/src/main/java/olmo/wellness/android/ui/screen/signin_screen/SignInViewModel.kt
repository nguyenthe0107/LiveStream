package olmo.wellness.android.ui.screen.signin_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.Result
import olmo.wellness.android.core.enums.UserRole
import olmo.wellness.android.data.model.LoginRequest
import olmo.wellness.android.data.model.definition.AuthMethod
import olmo.wellness.android.data.model.definition.AuthType
import olmo.wellness.android.data.model.definition.SpecialError
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.fcm.AppUserRequest
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
import olmo.wellness.android.di.wrapper.AccessTokenWrapper
import olmo.wellness.android.domain.model.UserInfo
import olmo.wellness.android.domain.model.business.StoreOwner
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.login.LoginData
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.model.user_info.UserInfoLocal
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.analytics.TrackingConstants
import olmo.wellness.android.ui.common.validate.emailValidator
import olmo.wellness.android.ui.common.validate.validatePassword
import olmo.wellness.android.ui.common.validate.validateRegexPhone
import olmo.wellness.android.ui.helpers.getUniqueDeviceId
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val getInternalApiUseCase: GetApiUseCase,
    private val setUserInfoUseCase: SetUserInfoUseCase,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val accessTokenWrapper: AccessTokenWrapper,
    private val notificationUseCase: NotificationUseCase
) : ViewModel() {

    private val _isSuccess = MutableStateFlow<Boolean>(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorBody = MutableStateFlow("")
    val errorBody: StateFlow<String> = _errorBody

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _countryCodeSelected = MutableStateFlow("+84")
    val countryCodeSelected: StateFlow<String> = _countryCodeSelected

    private val _isErrorEmail = MutableStateFlow(false)
    val isErrorEmail: StateFlow<Boolean> = _isErrorEmail

    private val _isErrorPhone = MutableStateFlow(false)
    val isErrorPhone: StateFlow<Boolean> = _isErrorPhone

    private val _hasOnErrorPassword = MutableStateFlow(false)
    val hasOnErrorPassword: StateFlow<Boolean> = _hasOnErrorPassword

    private val _isErrorAccountNotExist = MutableStateFlow(false)
    val isErrorAccountNotExist: StateFlow<Boolean> = _isErrorAccountNotExist

    private val _isErrorAccountNotVerified = MutableStateFlow(false)
    val isErrorAccountNotVerified: StateFlow<Boolean> = _isErrorAccountNotVerified

    private val _userType = MutableStateFlow("")
    val userType: StateFlow<String> = _userType

    private val _authMethod = MutableStateFlow("")
    val authMethod: StateFlow<String> = _authMethod

    private val _navigationToOTPVerify = MutableStateFlow(false)
    val navigationToOTPVerify: StateFlow<Boolean> = _navigationToOTPVerify

    private val _identityValue = MutableStateFlow("")
    private val identityValue: StateFlow<String> = _identityValue

    private val _tokenDevice = MutableStateFlow("")
    private val tokenDevice : StateFlow<String> = _tokenDevice

    private val _numberInputPassword = MutableStateFlow(0)
    private val numberInputPassword : StateFlow<Int> = _numberInputPassword

    private val _navigationToForgetPassword = MutableStateFlow(false)
    val navigationToForgetPassword: StateFlow<Boolean> = _navigationToForgetPassword

    fun loginWithFirebase(token: String) {
        _isLoading.value = true
        loginApi(LoginRequest(AuthMethod.PROVIDER, arrayListOf(AuthType.FIREBASE.name, token)),
            isFirebase = true, method = TrackingConstants.TRACKING_VALUE_GMAIL)
        _authMethod.value = AuthMethod.PROVIDER.name
    }

    fun loginWithUserPass(identity: String, password: String,method: String) {
        _isLoading.value = true
        _identityValue.value = identity
        loginApi(LoginRequest(AuthMethod.USER_PASS, arrayListOf(identity, password)),
            identity = identity, method = method)
        _authMethod.value = AuthMethod.USER_PASS.name
    }

    private fun loginApi(req: LoginRequest, identity: String?=null,
                         isFirebase: Boolean?=false, method: String) {
        viewModelScope.launch {
            sharedPrefs.clearToken()
            getInternalApiUseCase.login(req).collect { loginIDResponse ->
                when (loginIDResponse) {
                    is Result.Success -> {
                        _isLoading.value = true
                        _errorBody.value = ""
                        if(loginIDResponse.data?.verified == true && loginIDResponse.data.accessToken?.token?.isNotEmpty() == true){
                            setUserInfo(loginIDResponse.data)
                            loginIDResponse.data.let { loginResponse ->
                                getInternalApiUseCase.getUserInfo().collect{ userTypeResponse ->
                                    when (userTypeResponse) {
                                        is Result.Success -> {
                                            setUserInfo(loginResponse.copy(userType = userTypeResponse.data?.userType))
                                            _isLoading.value = false
                                            _errorBody.value = ""
                                            _isSuccess.value = true
                                            sharedPrefs.setToken(loginResponse.accessToken?.token.orEmpty())
                                            setUserInfoLocal(
                                                userId = loginResponse.userId ?: 0,
                                                identity = identityValue.value,
                                                userTypeModel = UserTypeModel(userTypeResponse.data?.userType.orEmpty()),
                                                authMethod = authMethod.value,
                                                name = userTypeResponse.data?.name,
                                                email = userTypeResponse.data?.email,
                                                avatar = userTypeResponse.data?.avatar,
                                                methodTracking = method,
                                                store = userTypeResponse.data?.store
                                            )
                                            viewModelScope.launch(Dispatchers.IO) {
                                                val userId = sharedPrefs.getUserInfoLocal().userId
                                                val deviceId = getUniqueDeviceId()
                                                val appUserRequest = AppUserRequest(userId = userId ?: 0, deviceId = deviceId, firebaseToken = tokenDevice.value)
                                                notificationUseCase.sendAppUser(appUserRequest).collectLatest {}
                                                var accountName = identity
                                                if(isFirebase == true){
                                                    accountName = userTypeResponse.data?.name
                                                }
                                                val trackingModel = TrackingModel(isSuccess = true,
                                                    user_id = userId?:0, account_name = accountName, method = method)
                                                AnalyticsManager.getInstance()?.trackingSignIn(trackingModel)
                                            }
                                        }
                                        is Result.Error -> {
                                            _isLoading.value = false
                                            loginIDResponse.message?.let { errorMessage ->
                                                _errorBody.value = errorMessage
                                                when(errorMessage.trim()){
                                                    SpecialError.USER_NOT_FOUND.nameError,
                                                    SpecialError.PRINCIPAL_NOT_FOUND.nameError -> {
                                                        _isErrorAccountNotExist.value = true
                                                    }
                                                    SpecialError.USER_NOT_VERIFIED.nameError -> {
                                                        sendRequestOTP()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else{
                            /* Case wrong password */
                            if(loginIDResponse.data?.verified == true && loginIDResponse.data.accessToken?.token.isNullOrEmpty()){
                                _isLoading.value = false
                                _errorBody.value = SpecialError.INVALID_CREDENTIALS.nameError
                                _numberInputPassword.value += 1
                                if(numberInputPassword.value % 5 == 0){
                                    _navigationToForgetPassword.value = true
                                }
                            }else{
                                /* Case account not yet verified */
                                sharedPrefs.setToken(loginIDResponse.data?.accessToken?.token)
                                setUserInfoLocal(
                                    userId = loginIDResponse.data?.userId ?: 0,
                                    identity = identityValue.value,
                                    userTypeModel = UserTypeModel(loginIDResponse.data?.userType.orEmpty()),
                                    authMethod = authMethod.value,
                                    name = loginIDResponse.data?.userName,
                                    methodTracking = method,
                                )
                                val bodyRequest = VerifyCodeRequest(identity = identityValue.value)
                                getInternalApiUseCase.requestVerifyCode(bodyRequest).collectLatest {
                                    _isLoading.value = false
                                    _navigationToOTPVerify.value = true
                                    sendTrackingOTP(
                                        userId = loginIDResponse.data?.userId ?: 0,
                                        identity = identityValue.value,
                                        statusInput = true,
                                        method = method
                                    )
                                }
                            }
                        }
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        loginIDResponse.message?.let { errorMessage ->
                            _errorBody.value = errorMessage
                            when(errorMessage.trim()){
                                SpecialError.USER_NOT_FOUND_V2.nameError,
                                SpecialError.USER_NOT_FOUND.nameError,
                                SpecialError.PRINCIPAL_NOT_FOUND.nameError -> {
                                    _isErrorAccountNotExist.value = true
                                }
                                SpecialError.USER_NOT_VERIFIED.nameError -> {
                                    sendRequestOTP()
                                }
                                SpecialError.INVALID_CREDENTIALS.nameError -> {
                                    _numberInputPassword.value += 1
                                    if(numberInputPassword.value % 5 == 0){
                                        _navigationToForgetPassword.value = true
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                        _errorBody.value = ""
                    }
                }
            }
        }
    }

    private fun setUserInfo(loginData: LoginData?) {
        _userType.value = loginData?.userType ?: UserRole.BUYER.role
        loginData?.let {
            accessTokenWrapper.invokeAccessToken(loginData.accessToken?.token)
            viewModelScope.launch {
                val userInfo = UserInfo(
                    token = loginData.accessToken?.token.orEmpty(),
                    refreshToken = loginData.accessToken?.refreshToken.orEmpty(),
                    expiration = loginData.accessToken?.expiration ?: 0,
                    userId = loginData.userId ?: 0,
                    userType = loginData.userType ?: UserRole.BUYER.role,
                )
                setUserInfoUseCase(
                    SetUserInfoUseCase.Params(
                        userInfo = userInfo
                    )
                )
            }
        }
    }

    private fun setUserInfoLocal(userId: Int, identity: String,
                                 userTypeModel: UserTypeModel,
                                 authMethod: String,name :String?,
                                 email: String?=null,avatar : String?=null,
                                 methodTracking: String?=null,
                                 store: StoreOwner?=null) {
        viewModelScope.launch(Dispatchers.IO) {
            val userInfo = UserInfoLocal(
                userId = userId,
                identity = identity,
                userTypeModel = userTypeModel,
                auMethod = authMethod,
                name = name,
                email = email,
                avatar = avatar,
                store = store,
                methodTracking = methodTracking
            )
            sharedPrefs.setUserInfoLocal(userInfo)
            sharedPrefs.setLoginSuccess(true)
            sharedPrefs.setTotalUnSeenNotification(0)
        }
    }

    fun isEmailValid(email: String) {
        if (email.isNotEmpty()) {
            _isErrorEmail.value = !emailValidator(email)
        } else {
            _isErrorEmail.value = false
        }
    }

    fun isPhoneValid(phone: String) {
        if (phone.isNotEmpty()) {
            _isErrorPhone.value = !validateRegexPhone(phone)
        } else {
            _isErrorPhone.value = false
        }
    }

    fun isValidatePassword(password: String) {
        if (password.isNotEmpty()) {
            _hasOnErrorPassword.value = !validatePassword(password)
        } else {
            _hasOnErrorPassword.value = false
        }
    }

    fun resetState() {
        _errorBody.value = ""
    }

    fun resetStateNumberInputPassword() {
        _navigationToForgetPassword.value = false
    }

    fun resetNavigation(){
        _navigationToOTPVerify.value = false
    }

    fun updateCountrySelected(country: String) {
        if (country.isNotEmpty()) {
            _countryCodeSelected.value = country
        }
    }

    private fun getCountryList() {
        viewModelScope.launch(Dispatchers.IO) {
            getCountryListUseCase().collectLatest {
                when (it) {
                    is Result.Success -> {
                        if (!it.data.isNullOrEmpty()) {
                            _countryList.value = it.data.orEmpty()
                        }
                    }
                }
            }
        }
    }

    init {
        getCountryList()
        getToken()
    }

    private fun sendRequestOTP(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val bodyRequest = VerifyCodeRequest(identity = identityValue.value)
                getInternalApiUseCase.requestVerifyCode(bodyRequest).collect {
                    _navigationToOTPVerify.value = true
                    _isLoading.value = false
                }
            } catch (throwable: Exception) {
                _errorBody.value = throwable?.message.toString() ?: Constants.ERROR_COMMON
                _isLoading.value = false
            }
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _tokenDevice.update { task.result.toString() }
                    viewModelScope.launch(Dispatchers.IO) {
                        val userLocal = sharedPrefs.getUserInfoLocal().copy(firebaseToken = task.result.toString())
                        sharedPrefs.setUserInfoLocal(userLocal)
                    }
                }
            }
    }

    fun trackingClickForgerPassword(){
        viewModelScope.launch(Dispatchers.IO) {
            AnalyticsManager.getInstance()?.trackingClickForgetPassword()
        }
    }

    private fun sendTrackingOTP(userId: Int,identity: String,
                                method: String, errMes: String?=null,
                                statusInput: Boolean?) {
        val tracking = TrackingModel(
            isSuccess = statusInput,
            user_id = userId,
            account_name = identity,
            method = method,
            err_message = errMes
        )
        AnalyticsManager.getInstance()?.trackingRequestOTP(tracking)
    }
}