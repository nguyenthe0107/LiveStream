package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.TokenRequest
import olmo.wellness.android.domain.model.authenticator.Authenticator
import olmo.wellness.android.domain.use_case.AuthenticatorUseCase
import javax.inject.Inject

@HiltViewModel
class AuthenticationCodeViewModel @Inject constructor(
    application: Application,
    private val authenticatorUseCase: AuthenticatorUseCase
) : BaseViewModel(application) {

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _authenticator = MutableStateFlow<Authenticator?>(null)
    val authenticator : StateFlow<Authenticator?> = _authenticator

    private fun generateCode(){
        viewModelScope.launch {
            _isLoading.value = true
            authenticatorUseCase.invoke().collect { it ->
                when (it) {
                    is Result.Success -> {
                        _authenticator.value = it.data
                        _isLoading.value = false
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        it.message?.let { errorMessage ->
                            _error.value = errorMessage
                        }
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }

    fun uploadCode(token: String){
        viewModelScope.launch {
            _isLoading.value = true
            val tokenRequest = TokenRequest(token)
            authenticatorUseCase.postAuthenticator(tokenRequest).collect {
                when (it) {
                    is Result.Success -> {
                        _isSuccess.value = true
                        _isLoading.value = false
                        _error.value = ""
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        _isSuccess.value = true
                        it.message?.let { errorMessage ->
                            _error.value = errorMessage
                        }
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
                if(it.message != null && it.message.isNotEmpty()){
                    _error.value = it.message
                }
            }
        }
    }

    init {
        generateCode()
    }
}
