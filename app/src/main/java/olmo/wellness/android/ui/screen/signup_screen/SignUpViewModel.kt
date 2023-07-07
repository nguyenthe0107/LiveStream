package olmo.wellness.android.ui.screen.signup_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.core.enums.UserRole
import olmo.wellness.android.data.model.LoginRequest
import olmo.wellness.android.data.model.RegisterRequest
import olmo.wellness.android.data.model.definition.AuthMethod
import olmo.wellness.android.data.model.definition.SpecialError
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
import olmo.wellness.android.di.wrapper.AccessTokenWrapper
import olmo.wellness.android.domain.model.UserInfo
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.login.LoginData
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.analytics.TrackingConstants
import olmo.wellness.android.ui.common.validate.emailValidator
import olmo.wellness.android.ui.common.validate.validatePassword
import olmo.wellness.android.ui.common.validate.validateRegexPhone
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val getInternalApiUseCase: GetApiUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val setUserInfoUseCase: SetUserInfoUseCase,
    private val getProductCategoriesUseCase: GetProductCategoriesFromLocalUseCase,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val accessTokenWrapper: AccessTokenWrapper,
    private val notificationUseCase: NotificationUseCase
) : ViewModel() {

    private val _navigationToHome = MutableStateFlow(false)
    val navigationToHome: StateFlow<Boolean> = _navigationToHome

    private val _navigationToOTPVerify = MutableStateFlow(false)
    val navigationToOTPVerify: StateFlow<Boolean> = _navigationToOTPVerify

    private val _toOTPVerifyForNotVerified = MutableStateFlow(false)
    val toOTPVerifyForNotVerified: StateFlow<Boolean> = _toOTPVerifyForNotVerified

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _identityValue = MutableStateFlow("")
    val identityValue: StateFlow<String> = _identityValue

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _countryCodeSelected = MutableStateFlow("+84")
    val countryCodeSelected: StateFlow<String> = _countryCodeSelected

    private val _errorBody = MutableStateFlow("")
    val errorBody: StateFlow<String> = _errorBody

    private val _hasOnErrorPassword = MutableStateFlow(false)
    val hasOnErrorPassword: StateFlow<Boolean> = _hasOnErrorPassword

    private val _isErrorEmail = MutableStateFlow(false)
    val isErrorEmail: StateFlow<Boolean> = _isErrorEmail

    private val _isErrorPhone = MutableStateFlow(false)
    val isErrorPhone: StateFlow<Boolean> = _isErrorPhone

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _userType = MutableStateFlow("")
    val userType: StateFlow<String> = _userType

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _tokenDevice = MutableStateFlow("")
    val tokenDevice : StateFlow<String> = _tokenDevice

    private val _authData = MutableStateFlow<ArrayList<String>>(arrayListOf())
    val authData : StateFlow<ArrayList<String>> = _authData

    private val _isPhone = MutableStateFlow(false)
    val isPhone : StateFlow<Boolean> = _isPhone

    init {
        getCountryList()
        getToken()
    }

    fun registerWithUserPass(identity: String, password: String, method: String) {
        _identityValue.value = identity
        _authData.update {
            arrayListOf(identity, password)
        }
        _isPhone.value = method == TrackingConstants.TRACKING_VALUE_SIGNIN_PHONE_NUMBER
        registerUser(identity, password, method)
    }

    fun sendTracking(isPhone: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            if(isPhone){
                AnalyticsManager.getInstance()?.trackingSignUpByPhone()
            }else{
                AnalyticsManager.getInstance()?.trackingSignUpByEmail()
            }
        }
    }

    private fun registerUser(identity: String, password: String, method: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            sharedPrefs.clearToken()
            val userType =
                UserTypeModel(
                    getUserInfoUseCase()?.userType ?: UserTypeModel.BUYER.value
                )
            val subCategoryIds: List<Int> = getProductCategoriesUseCase.invoke() ?: emptyList()
            val reqInternal = RegisterRequest(
                authMethod = AuthMethod.USER_PASS,
                authData = arrayListOf(identity, password),
                userType = userType,
                subCategoryIds = subCategoryIds
            )
            getInternalApiUseCase.register(reqInternal)
                .collectLatest { resultRegister ->
                    when (resultRegister) {
                        is Result.Success -> {
                            val loginRequest = LoginRequest(
                                authMethod = AuthMethod.USER_PASS,
                                authData = arrayListOf(identity, password)
                            )
                            getInternalApiUseCase.login(loginRequest).collectLatest { loginResult ->
                                when (loginResult) {
                                    is Result.Success -> {
                                        handleSaveUserLocal(loginResult.data, userType, method)
                                        _userType.value = userType.value
                                        setUserInfo(loginResult.data)
                                        if (loginResult.data?.verified == true) {
                                            accessTokenWrapper.invokeAccessToken(loginResult.data.accessToken?.token)
                                            _errorBody.value = ""
                                            _isLoading.value = false
                                            _navigationToHome.value = true
                                            sendTrackingClickSignUp(
                                                loginResult.data,
                                                identity,
                                                method,subCategoryIds
                                            )
                                        } else {
                                            setUserInfo(loginResult.data)
                                            _navigationToOTPVerify.value = true
                                            _isLoading.value = false
                                            sendTrackingOTP(
                                                userId = loginResult.data?.userId?:0,
                                                identity = identity, method = method,
                                                statusInput = true
                                            )
                                        }
                                    }
                                    is Result.Error -> {
                                        _isLoading.value = false
                                        loginResult.message?.let { errorMessage ->
                                            _errorBody.value = errorMessage
                                        }
                                    }
                                    is Result.Loading -> {
                                        _errorBody.value = ""
                                        _isLoading.value = true
                                    }
                                }
                            }
                        }
                        is Result.Error -> {
                            _isLoading.value = false
                            resultRegister.message?.let { errorMessage ->
                                _errorBody.value = errorMessage
                                if(_errorBody.value.contentEquals(SpecialError.USER_REGISTERED_NOT_VERIFIED.nameError, true)){
                                    _isLoading.value = true
                                    val bodyRequest = VerifyCodeRequest(identity = identityValue.value)
                                    getInternalApiUseCase.requestVerifyCode(bodyRequest).collectLatest {
                                        _isLoading.value = false
                                        _toOTPVerifyForNotVerified.value = true
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun setUserInfo(loginData: LoginData?) {
        loginData?.let {
            viewModelScope.launch {
                setUserInfoUseCase(
                    SetUserInfoUseCase.Params(
                        UserInfo(
                            userId = loginData.userId ?: 0,
                            userType = loginData.userType ?: UserRole.BUYER.role
                        )
                    )
                )
            }
        }
    }

    private fun handleSaveUserLocal(
        loginData: LoginData?,
        userType: UserTypeModel,
        method: String){
        loginData?.let {
            viewModelScope.launch {
                val userLocal = sharedPrefs.getUserInfoLocal().copy(
                    userId = loginData.userId,
                    identity = identityValue.value,
                    userTypeModel = userType,
                    verified = it.verified ?: false,
                    methodTracking = method
                )
                sharedPrefs.setUserInfoLocal(userLocal)
            }
        }
    }

    fun isValidatePassword(password: String) {
        if (password.isNotEmpty()) {
            _hasOnErrorPassword.value = !validatePassword(password)
        } else {
            _hasOnErrorPassword.value = false
        }
        _password.update {
            password
        }
    }

    fun isEmailValid(email: String) {
        if (email.isNotEmpty()) {
            _isErrorEmail.value = !emailValidator(email)
        } else {
            _isErrorEmail.value = false
        }
        _email.update {
            email
        }
    }

    fun isPhoneValid(phone: String) {
        if (phone.isNotEmpty()) {
            _isErrorPhone.value = !validateRegexPhone(phone)
        } else {
            _isErrorPhone.value = false
        }
        _phoneNumber.update {
            phone
        }
    }

    fun updateCountrySelected(country: String) {
        if (country.isNotEmpty()) {
            _countryCodeSelected.value = country
        }
    }

    fun resetState() {
        _errorBody.value = ""
        _navigationToHome.value = false
        _navigationToOTPVerify.value = false
        _toOTPVerifyForNotVerified.value = false
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
                    else -> {}
                }
            }
        }
    }

    fun getUserTypeFromLocal(){
        viewModelScope.launch {
            if(sharedPrefs.getUserRoleLocal()?.isNotEmpty() == true){
                val userRole = UserTypeModel(sharedPrefs.getUserRoleLocal() ?: UserTypeModel.BUYER.value)
                _userType.update {
                    userRole.name
                }
            }
        }
    }

    fun resetUserTypeLocal(){
        viewModelScope.launch {
            sharedPrefs.clearUserRole()
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _tokenDevice.update { task.result.toString() }
                }
            }
    }

    private fun sendTrackingClickSignUp(
        data: LoginData,
        identity: String,
        method: String,
        subCategoryIds: List<Int>?){
        AnalyticsManager.getInstance()?.trackingSignUpSuccess()
        val trackingModel = TrackingModel(
            isSuccess = true,
            user_id = data.userId ?: 0,
            account_name = identity,
            method = method,
            category_id = subCategoryIds
        )
        AnalyticsManager.getInstance()?.trackingWhenClickSignUp(trackingModel)
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