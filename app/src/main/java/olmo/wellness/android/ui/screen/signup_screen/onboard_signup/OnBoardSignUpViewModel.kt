package olmo.wellness.android.ui.screen.signup_screen.onboard_signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.core.enums.UserRole
import olmo.wellness.android.data.model.LoginRequest
import olmo.wellness.android.data.model.RegisterRequest
import olmo.wellness.android.data.model.definition.AuthMethod
import olmo.wellness.android.data.model.definition.AuthType
import olmo.wellness.android.data.model.definition.SpecialError
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.di.wrapper.AccessTokenWrapper
import olmo.wellness.android.domain.model.UserInfo
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.login.LoginData
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.analytics.TrackingConstants
import javax.inject.Inject

@HiltViewModel
class OnBoardSignUpViewModel @Inject constructor(
    private val getInternalApiUseCase: GetApiUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val setUserInfoUseCase: SetUserInfoUseCase,
    private val getProductCategoriesUseCase: GetProductCategoriesFromLocalUseCase,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val accessTokenWrapper: AccessTokenWrapper,
) : ViewModel() {

    private val _navigationToHome = MutableStateFlow(false)
    val navigationToHome : StateFlow<Boolean> = _navigationToHome

    private val _navigationToOTPVerify = MutableStateFlow(false)
    val navigationToOTPVerify: StateFlow<Boolean> = _navigationToOTPVerify

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _identityValue = MutableStateFlow("")

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _countryCodeSelected = MutableStateFlow("+84")
    val countryCodeSelected: StateFlow<String> = _countryCodeSelected

    private val _errorBody = MutableStateFlow("")
    val errorBody: StateFlow<String> = _errorBody

    private val _isErrorAccountNotExist = MutableStateFlow(false)
    val isErrorAccountNotExist: StateFlow<Boolean> = _isErrorAccountNotExist

    /* Flow Google Or Ins or Tiktok  */
    private val _navigationSelectUserType = MutableStateFlow(false)
    val navigationSelectUserType: StateFlow<Boolean> = _navigationSelectUserType

    private val _tokenResponse = MutableStateFlow<String>("")
    private val tokenResponse : StateFlow<String> = _tokenResponse

    private val _authDataFirebase = MutableStateFlow<ArrayList<String>>(arrayListOf())
    val authDataFirebase : StateFlow<ArrayList<String>> = _authDataFirebase

    private val _identify = MutableStateFlow<String>("")
    val identify : StateFlow<String> = _identify

    fun sendToken(token: String) {
        registerUserByGoogle(token)
    }

    private fun registerUserByGoogle(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _isErrorAccountNotExist.value = false
            val loginRequest = LoginRequest(
                authMethod = AuthMethod.PROVIDER,
                authData = arrayListOf(AuthType.FIREBASE.name, token)
            )
            _authDataFirebase.value = arrayListOf(AuthType.FIREBASE.name, token)
            getInternalApiUseCase.login(loginRequest).collectLatest { resultLogin ->
                when(resultLogin){
                    is Result.Success -> {
                        _isErrorAccountNotExist.value = true
                        _errorBody.value = SpecialError.USER_REGISTERED.nameError
                        _isLoading.value = false
                    }
                    is Result.Error -> {
                        _tokenResponse.value = token
                        sharedPrefs.clearToken()
                        _navigationSelectUserType.value = true
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun registerUserByGoogleFinalStep() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            sharedPrefs.clearToken()
            val subCategoryIds: List<Int> = getProductCategoriesUseCase.invoke()
            val userType =
                UserTypeModel(
                    getUserInfoUseCase()?.userType ?: UserTypeModel.BUYER.value
                )
            val reqInternal = RegisterRequest(
                authMethod = AuthMethod.PROVIDER,
                authData = arrayListOf(AuthType.FIREBASE.name, tokenResponse.value),
                userType = userType,
                subCategoryIds = subCategoryIds
            )
            getInternalApiUseCase.register(reqInternal).collect { registerDataIDResponse ->
                when (registerDataIDResponse) {
                    is Result.Success -> {
                        val loginRequest = LoginRequest(
                            authMethod = AuthMethod.PROVIDER,
                            authData = arrayListOf(AuthType.FIREBASE.name, tokenResponse.value)
                        )
                        getInternalApiUseCase.login(loginRequest).collectLatest { resultLogin ->
                            handleSaveUserLocal(resultLogin.data, userType)
                            sharedPrefs.setToken(resultLogin.data?.accessToken?.token.orEmpty())
                            setUserInfo(resultLogin.data)
                            _errorBody.value = ""
                            _isLoading.value = false
                            _navigationToHome.value = true
                            val trackingModel = TrackingModel(
                                isSuccess = true,
                                user_id = resultLogin.data?.userId?:0,
                                method = TrackingConstants.TRACKING_VALUE_GMAIL,
                                category_id = subCategoryIds,
                                account_name = resultLogin.data?.userName)
                            AnalyticsManager.getInstance()?.trackingWhenClickSignUp(trackingModel)
                        }
                    }

                    is Result.Error -> {
                        _isLoading.value = false
                        registerDataIDResponse.message?.let { errorMessage ->
                            when(errorMessage.trim()){
                                SpecialError.USER_REGISTERED.nameError -> {
                                    _isErrorAccountNotExist.value = true
                                }
                                else -> {
                                    _errorBody.value = errorMessage
                                }
                            }
                        }
                        _navigationSelectUserType.value = false
                    }

                    is Result.Loading -> {
                        _errorBody.value = ""
                        _isLoading.value = true
                    }
                }
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

    private fun handleSaveUserLocal(
        loginData: LoginData?,
        userTypeInput: UserTypeModel){
        loginData?.let {
            viewModelScope.launch {
                val userType = loginData.userType?.let { it1 ->
                    UserTypeModel(
                        it1
                    )
                }
                val userLocal = sharedPrefs.getUserInfoLocal().copy(
                    userId = loginData.userId,
                    identity = _identityValue.value,
                    userTypeModel = userType ?: userTypeInput,
                    verified = it.verified ?: false
                )
                sharedPrefs.setUserInfoLocal(userLocal)
            }
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
                    else -> {}
                }
            }
        }
    }

    init {
        getCountryList()
    }

    fun resetStatus(){
        _navigationToHome.value = false
        _errorBody.value = ""
        _navigationSelectUserType.value = false
    }

    fun resetUserTypeLocal(){
        viewModelScope.launch {
            sharedPrefs.clearUserRole()
        }
    }

    fun sendTrackingGmail(){
        viewModelScope.launch(Dispatchers.IO) {
            AnalyticsManager.getInstance()?.trackingSignUpByGoogle()
        }
    }

}