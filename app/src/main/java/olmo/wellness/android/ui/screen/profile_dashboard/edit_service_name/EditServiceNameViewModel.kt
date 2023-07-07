package olmo.wellness.android.ui.screen.profile_dashboard.edit_service_name

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
import olmo.wellness.android.data.model.business.StoreBusinessRequest
import olmo.wellness.android.data.model.business.StoreDetailRequest
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.profile.ProfileUpdateInfo
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.validate.checkValidateServiceName
import javax.inject.Inject

@HiltViewModel
class EditServiceNameViewModel @Inject constructor(application: Application,
                                                   private val getApiUseCase: GetApiUseCase,
                                                   private val setServiceBusinessInfoUseCase: SetServiceBusinessInfoUseCase,
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

    private val _storeId = MutableStateFlow<Int?>(null)
    private val storeId : StateFlow<Int?> = _storeId

    init {
        getBusinessOwnedList()
    }

    private fun getBusinessOwnedList() {
        _error.value = ""
        val userLocal = sharedPrefs.getUserInfoLocal()
        if(userLocal.userTypeModel != UserTypeModel.BUYER){
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.value = true
                getApiUseCase.getUserInfo().collectLatest {
                    when (it) {
                        is Result.Success -> {
                            val store = it.data?.store
                            store?.let { internalStore ->
                                _storeId.update { internalStore.id }
                                if(internalStore.name != null){
                                    _profileModel.update { profile ->
                                        profile.copy(storeName = internalStore.name, name = internalStore.name)
                                    }
                                    saveLocal(internalStore.name)
                                }
                            }
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
    }

    private fun saveLocal(serviceName: String?=null){
        viewModelScope.launch(Dispatchers.Default) {
            if(sharedPrefs.getUserInfoLocal().userId != null){
                _profileModel.update {
                    it.copy(
                        id = sharedPrefs.getUserInfoLocal().userId,
                        storeId = storeId.value
                    )
                }
            }
            if(serviceName?.isNotEmpty() == true){
                _profileModel.update { it.copy(storeName = serviceName, name = serviceName) }
            }
            sharedPrefs.setProfile(profileModel.value)
        }
    }

    fun updateServiceName(name: String){
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            val userId = storeId.value
            val profileInfo = ProfileUpdateInfo().copy(id = listOf(userId))
            val queryString = Gson().toJson(profileInfo)
            val childRequest = StoreDetailRequest().copy(
                name = name
            )
            val bodyRequest = StoreBusinessRequest(childRequest)
            val param = SetServiceBusinessInfoUseCase.Params(bodyRequest)
            setServiceBusinessInfoUseCase.invoke(queryString, true, param).collect{
                when(it){
                    is Result.Success -> {
                        _isLoading.value = false
                        _isSuccess.value = true
                        if(it.data != null){
                            _profileModel.update { profile ->
                                profile.copy(storeName = name, name = name)
                            }
                            saveLocal(name)
                        }
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        _error.value = it.message ?: ERROR_COMMON
                        _profileModel.update { it.copy(storeName = name, name = name) }
                        saveLocal(name)
                        _isSuccess.value = true
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }

    fun validateServiceName(username: String){
        username.let {
            viewModelScope.launch(Dispatchers.Default) {
                val invalidateUserName = checkValidateServiceName(username)
                _isErrorValidate.update {
                    invalidateUserName
                }
            }
        }
    }
}