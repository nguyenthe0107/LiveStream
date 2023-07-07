package olmo.wellness.android.ui.screen.profile_dashboard.edit_phone

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
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.ErrorResponseDTO
import olmo.wellness.android.data.model.IdentityRequest
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.profile.update.ProfileUpdateRequest
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.profile.ProfileUpdateInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.validate.validateRegexPhone
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class EditPhoneViewModel @Inject constructor(application: Application,
                                             private val getProfileUseCase: GetProfileUseCase,
                                             private val setProfileInfoUseCase: SetProfileInfoUseCase,
                                             private val getCountryListUseCase: GetCountryListUseCase,
                                             private val getApiUseCase: GetApiUseCase,
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
    }

    private val _isErrorValidate = MutableStateFlow(false)
    val isErrorValidate: StateFlow<Boolean> = _isErrorValidate

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel : StateFlow<ProfileInfo> = _profileModel

    private val _countryCodeSelected = MutableStateFlow("+84")
    val countryCodeSelected : StateFlow<String> = _countryCodeSelected

    init {
        getProfile()
        getCountryList()
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
                            _profileModel.value = it.data.last()
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

    fun updateCountrySelected(country: String){
        if(country.isNotEmpty()){
            _countryCodeSelected.value = country
        }
    }

    fun updatePhone(phoneNumber: String){
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            val userId = sharedPrefs.getUserInfoLocal().userId
            val profileInfo = ProfileUpdateInfo().copy(id = listOf(userId))
            val temp = Gson().toJson(profileInfo)
            val childRequest = ProfileUpdateRequest().copy(
                phoneNumber = _countryCodeSelected.value.plus(phoneNumber)
            )
            val bodyRequest = ProfileBodyRequest(childRequest)
            val param = SetProfileInfoUseCase.Params(bodyRequest)
            setProfileInfoUseCase.invoke(temp, true, param).collect{
                when(it){
                    is Result.Success -> {
                        _isLoading.value = false
                        _isSuccess.value = true
                        _profileModel.update { profile ->
                            profile.copy(phoneNumber = _countryCodeSelected.value.plus(phoneNumber))
                        }
                        //sendToGetVerifyCode(_countryCodeSelected.value.plus(phoneNumber))
                        saveLocal()
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        _profileModel.update { profile ->
                            profile.copy(phoneNumber = _countryCodeSelected.value.plus(phoneNumber))
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

    fun isPhoneValid(phone: String) {
        if (phone.isNotEmpty()) {
            _isErrorValidate.value = !validateRegexPhone(phone)
            _error.value = ""
        } else {
            _isErrorValidate.value = false
        }
    }

}