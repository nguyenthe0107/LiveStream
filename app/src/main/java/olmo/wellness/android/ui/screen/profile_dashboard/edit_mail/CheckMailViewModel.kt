package olmo.wellness.android.ui.screen.profile_dashboard.edit_mail

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Constants.ERROR_COMMON
import olmo.wellness.android.data.model.ErrorResponseDTO
import olmo.wellness.android.data.model.IdentityRequest
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
import olmo.wellness.android.domain.use_case.*
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CheckMailViewModel @Inject constructor(application: Application,
                                             private val getApiUseCase: GetApiUseCase,
                                             private val useCaseID: GetApiIDTypeUseCase) : BaseViewModel(application){

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    fun resetState(){
        _isSuccess.value = false
    }

    private val _email = MutableStateFlow("")
    private val email : StateFlow<String> = _email

    fun bindEmail(email: String){
        _email.update {
            email
        }
    }

    fun sendToGetVerifyCode(){
        viewModelScope.launch(Dispatchers.IO){
            val email = email.value
            val identityRequest =  IdentityRequest(email)
            val bodyRequest = VerifyCodeRequest(identity = email)
            _isLoading.value = true
            try {
                _isLoading.value = false
                _isSuccess.value = true
            }catch (throwable: Exception){
                if (throwable is HttpException) {
                    val errorBody: String? = throwable.response()?.errorBody()?.string()
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(errorBody, ErrorResponseDTO::class.java)
                    _error.update {
                        errorResponse.message ?: ERROR_COMMON
                    }
                    _isLoading.value = false
                    _isSuccess.value = true
                } else {
                    _isLoading.value = false
                    _isSuccess.value = true
                }
            }
        }
    }
}