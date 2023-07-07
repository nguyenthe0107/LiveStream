package olmo.wellness.android.ui.screen.profile_dashboard.edit_bio

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.profile.update.ProfileUpdateRequest
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.profile.ProfileUpdateInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class BioViewModel @Inject constructor(application: Application,
                                       private val getUserLocal : GetUserInfoUseCase,
                                       private val getProfileUseCase: GetProfileUseCase,
                                       private val setProfileInfoUseCase: SetProfileInfoUseCase
) : BaseViewModel(application){

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    fun resetState(){
        _isSuccess.value = false
    }

    private val _isErrorValidate = MutableStateFlow(false)
    val isErrorValidate: StateFlow<Boolean> = _isErrorValidate

    private val _bioContent = MutableStateFlow("")
    fun setBioContent(value: String?){
        if(value != null){
            _bioContent.value = value
            _isErrorValidate.value = _bioContent.value.isNotEmpty() && _bioContent.value.length <= 500
        }
    }

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel : StateFlow<ProfileInfo> = _profileModel

    fun isValidateData(): Boolean {
        if(_bioContent.value.isNotEmpty() && _bioContent.value.length <= 500){
            return true
        }
        return false
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
                            _profileModel.value = response
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

    fun updateBio(bioContent: String){
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            val userId = sharedPrefs.getUserInfoLocal().userId
            val profileInfo = ProfileUpdateInfo().copy(id = listOf(userId))
            val temp = Gson().toJson(profileInfo)
            val childRequest = ProfileUpdateRequest().copy(
                bio = bioContent
            )
            val bodyRequest = ProfileBodyRequest(childRequest)
            val param = SetProfileInfoUseCase.Params(bodyRequest)
            setProfileInfoUseCase.invoke(temp, true, param).collect{
                when(it){
                    is Result.Success -> {
                        _isLoading.value = false
                        _isSuccess.value = true
                        _profileModel.update { profile ->
                            profile.copy(bio = bioContent)
                        }
                        saveLocal()
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        _profileModel.update { profile ->
                            profile.copy(bio = bioContent)
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
}