package olmo.wellness.android.ui.screen.profile_dashboard.edit_mail

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Constants.ERROR_COMMON
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.ErrorResponseDTO
import olmo.wellness.android.data.model.IdentityRequest
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.profile.update.ProfileUpdateRequest
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.profile.ProfileUpdateInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.validate.emailValidator
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class EditMailViewModel @Inject constructor(application: Application,
                                            private val getProfileUseCase: GetProfileUseCase,
                                            private val setProfileInfoUseCase: SetProfileInfoUseCase,
                                            private val checkStoreNameUseCase: CheckStoreNameUseCase,
                                            private val useCaseID: GetApiIDTypeUseCase,
) : BaseViewModel(application){

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    fun resetState(){
        _isSuccess.value = false
        _isLoading.value = false
        _error.value = ""
    }

    private val _isErrorValidate = MutableStateFlow(false)
    val isErrorValidate: StateFlow<Boolean> = _isErrorValidate

    private val _isErrorDuplicateMail = MutableStateFlow(false)
    val isErrorDuplicateMail: StateFlow<Boolean> = _isErrorDuplicateMail

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel : StateFlow<ProfileInfo> = _profileModel

    private val _mail = MutableStateFlow("")
    fun updateContentEmail(value: String?){
        if(value != null){
            _mail.value = value
            isEmailValid(_mail.value)
            // checkMailExists(_mail.value)
        }
    }

    private fun isEmailValid(email: String) {
        if (email.isNotEmpty()) {
            _isErrorValidate.value = !emailValidator(email)
        } else {
            _isErrorValidate.value = false
        }
    }

    private fun checkMailExists(email : String){
        viewModelScope.launch {
            checkStoreNameUseCase(CheckStoreNameUseCase.Params(email)).collectLatest {
                when (it) {
                    is Result.Success -> {
                        _isErrorDuplicateMail.value = false
                    }
                    else -> {
                        _isErrorDuplicateMail.value = true
                    }
                }
            }
        }
    }

    init {
        getProfile()
    }

    private fun getProfile(){
        viewModelScope.launch {
            val profile = sharedPrefs.getProfile()
            if(profile.id != null){
                _profileModel.value = profile
            }
            val filed = "[\"name\",\"bio\",\"gender\",\"birthday\",\"avatar\",\"phoneNumber\",\"email\"]"
            val profileRequest = GetProfileRequest(sharedPrefs.getUserInfoLocal().userId, filed)
            getProfileUseCase.invoke(GetProfileUseCase.Params(profileRequest)).collect {
                when(it){
                    is Result.Success -> {
                        if(it.data?.isNotEmpty() == true){
                            val response = it.data.last()
                            _profileModel.value = response.copy(
                                username = response.name
                            )
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

    private fun saveLocal(){
        viewModelScope.launch(Dispatchers.Default) {
            if(sharedPrefs.getUserInfoLocal().userId != null){
                _profileModel.update {
                    it.copy(
                        id = sharedPrefs.getUserInfoLocal().userId
                    )
                }
            }
            sharedPrefs.setProfile(profileModel.value)
        }
    }

    fun updateEmail(email: String){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val userId = sharedPrefs.getUserInfoLocal().userId
            val profileInfo = ProfileUpdateInfo().copy(id = listOf(userId))
            val temp = Gson().toJson(profileInfo)
            val childRequest = ProfileUpdateRequest().copy(
                email = email,
                phoneNumber = null
            )
            val bodyRequest = ProfileBodyRequest(childRequest)
            val param = SetProfileInfoUseCase.Params(bodyRequest)
            setProfileInfoUseCase.invoke(temp, true, param).collect{
                when(it){
                    is Result.Success -> {
                        _isLoading.value = false
                        _isSuccess.value = true
                        _profileModel.update { profile ->
                            profile.copy(email = email)
                        }
                        saveLocal()
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        _profileModel.update { profile ->
                            profile.copy(email = email)
                        }
                        saveLocal()
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }

    suspend fun sendToGetVerifyCode(identity: String){
        viewModelScope.launch{
            var identityRequest: IdentityRequest?= null
            identityRequest =  IdentityRequest(identity)
            _isLoading.value = true
            try {
                useCaseID.resetPassword(identityRequest).collectLatest {

                }
            }catch (throwable: Exception){
                if (throwable is HttpException) {
                    val errorBody: String? = throwable.response()?.errorBody()?.string()
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(errorBody, ErrorResponseDTO::class.java)
                    _error.update {
                        errorResponse.message ?: ERROR_COMMON
                    }
                    _isLoading.value = false
                } else {
                    _isLoading.value = false
                }
            }
        }
    }
}