package olmo.wellness.android.ui.screen.profile_dashboard.edit_birthday

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
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.profile.update.ProfileUpdateRequest
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.profile.ProfileUpdateInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class EditBirthdayViewModel @Inject constructor(application: Application,
                                                private val getUserLocal : GetUserInfoUseCase,
                                                private val getProfileUseCase: GetProfileUseCase,
                                                private val setProfileInfoUseCase: SetProfileInfoUseCase,
                                                private val getCountryListUseCase: GetCountryListUseCase
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

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel : StateFlow<ProfileInfo> = _profileModel

    private val _avatar = MutableStateFlow("")
    val avatar : StateFlow<String> = _avatar

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

    private fun saveLocal(profileInfo: ProfileInfo){
        viewModelScope.launch(Dispatchers.Default) {
            if(sharedPrefs.getUserInfoLocal().userId != null){
                val profileInfoFinal = profileInfo.copy(id = sharedPrefs.getUserInfoLocal().userId)
                sharedPrefs.setProfile(profileInfoFinal)
            }
        }
    }

    fun updateBirthday(birthday: String){
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            val userId = sharedPrefs.getUserInfoLocal().userId
            val profileInfo = ProfileUpdateInfo().copy(id = listOf(userId))
            val temp = Gson().toJson(profileInfo)
            val childRequest = ProfileUpdateRequest().copy(
                birthday = birthday
            )
            val bodyRequest = ProfileBodyRequest(childRequest)
            val param = SetProfileInfoUseCase.Params(bodyRequest)
            setProfileInfoUseCase.invoke(temp, true, param).collect{
                when(it){
                    is Result.Success -> {
                        _isLoading.value = false
                        _isSuccess.value = true
                        if(it.data?.isNotEmpty() == true){
                            _profileModel.update { profile ->
                                profile.copy(birthday = birthday)
                            }
                            saveLocal(_profileModel.value)
                        }
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        _profileModel.update { profile ->
                            profile.copy(birthday = birthday)
                        }
                        saveLocal(_profileModel.value)
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }
}