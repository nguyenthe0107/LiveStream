package olmo.wellness.android.ui.screen.forget_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.ErrorResponseDTO
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.use_case.GetApiUseCase
import olmo.wellness.android.domain.use_case.GetCountryListUseCase
import olmo.wellness.android.ui.analytics.AnalyticsManager
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val apiUseCase: GetApiUseCase,
    private val getCountryListUseCase: GetCountryListUseCase
): ViewModel(){

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorBody = MutableSharedFlow<ErrorResponseDTO>()
    val errorBody = _errorBody.asSharedFlow()

    private val _isResetSuccess = MutableSharedFlow<Boolean>()
    val isResetSuccess = _isResetSuccess.asSharedFlow()

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _countryCodeSelected = MutableStateFlow("+84")
    val countryCodeSelected: StateFlow<String> = _countryCodeSelected

    suspend fun sendToGetVerifyCode(isPhone: Boolean, identity: String){
        viewModelScope.launch(Dispatchers.Default){
            val identityRequest: VerifyCodeRequest = if(isPhone){
                VerifyCodeRequest(countryCodeSelected.value.plus(identity))
            }else{
                VerifyCodeRequest(identity)
            }
            _isLoading.value = true
            try {
                apiUseCase.forgetPassword(identityRequest).collect {
                    _isResetSuccess.emit(true)
                    _isLoading.value = false
                }
            }catch (throwable: Exception){
                if (throwable is HttpException) {
                    val errorBody: String? = throwable.response()?.errorBody()?.string()
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(errorBody, ErrorResponseDTO::class.java)
                    _errorBody.emit(errorResponse)
                    _isLoading.value = false
                } else {
                    _isLoading.value = false
                }
            }
        }
    }

    fun trackingClickSendForgotPassword(isPhone: Boolean, identity: String){
        viewModelScope.launch(Dispatchers.IO) {
            val identityRequest: String = if(isPhone){
                countryCodeSelected.value.plus(identity)
            }else{
                identity
            }
            val trackingModel = TrackingModel(
                account_name = identityRequest
            )
            AnalyticsManager.getInstance()?.trackingClickSendForgetPassword(trackingModel)
        }
    }

    suspend fun resetPassword(isPhone: Boolean, identity: String){
        viewModelScope.launch(Dispatchers.Default){
            var identityRequest: VerifyCodeRequest ?= null
            identityRequest = if(isPhone){
                VerifyCodeRequest(countryCodeSelected.value.plus(identity))
            }else{
                VerifyCodeRequest(identity)
            }
            _isLoading.value = true
            try {
                apiUseCase.resetPassword(identityRequest).collect {
                    _isResetSuccess.emit(true)
                    _isLoading.value = false
                }
            }catch (throwable: Exception){
                if (throwable is HttpException) {
                    val errorBody: String? = throwable.response()?.errorBody()?.string()
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(errorBody, ErrorResponseDTO::class.java)
                    _errorBody.emit(errorResponse)
                    _isLoading.value = false
                } else {
                    _isLoading.value = false
                }
            }
        }
    }

    fun updateCountrySelected(country: String){
        if(country.isNotEmpty()){
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
    }

}