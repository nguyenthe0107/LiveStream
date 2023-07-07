package olmo.wellness.android.ui.screen.profile_dashboard.component_common.verification_code

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.user_auth.UserAuthRequest
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@SuppressLint("NewApi")
@HiltViewModel
class VerifyCodeProfileModel @Inject constructor(
    private val useCaseOlmoInternal: GetApiUseCase,
) : ViewModel() {

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorBody = MutableStateFlow<String>("")
    val errorBody : StateFlow<String> = _errorBody

    private val _otpValue = MutableStateFlow("")
    val otpValue: StateFlow<String> = _otpValue

    private val _userType = MutableStateFlow(UserTypeModel.BUYER)
    val userType: StateFlow<UserTypeModel> = _userType
    fun setUserType(userType: UserTypeModel){
        _userType.value = userType
    }

    private val _identity = MutableStateFlow("")
    private val identity : StateFlow<String> = _identity

    private val _isPhone = MutableStateFlow(false)
    private val isPhone : StateFlow<Boolean> = _isPhone

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel : StateFlow<ProfileInfo> = _profileModel

    init {
        getProfile()
    }

    fun verifyUser(otp: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            try {
                updateIdentity(identity.value, otp)
                _isLoading.value = false
            } catch (throwable: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun resendOTP() {
        viewModelScope.launch(Dispatchers.IO) {
        }
    }

    fun getOTP(otpInput: String?) {
        if (otpInput != null && otpInput.isNotEmpty()) {
            _otpValue.value = otpInput
        }
    }

    fun bindIdentity(isPhoneInput: Boolean?, identityInput: String?) {
        if (identityInput != null) {
            _identity.value = identityInput
        }
        if(isPhoneInput == true){
            _isPhone.value = isPhoneInput
        }else{
            _isPhone.value = false
        }
    }

    private fun updateIdentity(identity: String, otp: String){
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            val childRequest = if(isPhone.value){
                UserAuthRequest(
                    newIdentity = identity,
                    otp = otp
                )
            }else{
                UserAuthRequest(
                    newIdentity = identity,
                    otp = otp
                )
            }
            try {
                useCaseOlmoInternal.verifyUserAuth(childRequest).collectLatest{ result ->
                    when(result){
                        is Result.Loading -> {
                            _isLoading.value = true
                        }
                        is Result.Success -> {
                            _isLoading.value = false
                            _isSuccess.value = true
                            Log.e("WTF", " _isSuccess.verifyUserAuth ")
                            _profileModel.update { profile ->
                                if(isPhone.value){
                                    profile.copy(phoneNumber = identity)
                                }else{
                                    profile.copy(email = identity)
                                }
                            }
                            saveLocal(_profileModel.value)
                        }
                        is Result.Error -> {
                            _isLoading.value = false
                            _isSuccess.value = false
                            _profileModel.update { profile ->
                                if(isPhone.value){
                                    profile.copy(phoneNumber = identity)
                                }else{
                                    profile.copy(email = identity)
                                }
                            }
                            saveLocal(_profileModel.value)
                            result.message?.let { _errorBody.value = it }
                        }
                    }
                }
            }catch (ex: Exception){
            }
        }
    }

    private fun getProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            val profile = sharedPrefs.getProfile()
            if(profile.id != null){
                _profileModel.value = profile
            }
        }
    }

    private fun saveLocal(profileModel: ProfileInfo){
        viewModelScope.launch(Dispatchers.Default) {
            if(sharedPrefs.getUserInfoLocal().userId != null){
                val profileModelFinal = profileModel.copy(id=sharedPrefs.getUserInfoLocal().userId)
                sharedPrefs.setProfile(profileModelFinal)
            }
        }
    }

    fun resetValue() {
        _isSuccess.value = false
        _errorBody.update {
            ""
        }
    }
}