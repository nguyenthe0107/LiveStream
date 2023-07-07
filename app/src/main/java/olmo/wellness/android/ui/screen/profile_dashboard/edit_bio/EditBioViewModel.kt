package olmo.wellness.android.ui.screen.profile_dashboard.edit_bio

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
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.profile.update.ProfileUpdateRequest
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.profile.ProfileUpdateInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.use_case.GetProfileUseCase
import olmo.wellness.android.domain.use_case.SetProfileInfoUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class EditBioViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val setProfileUseCase: SetProfileInfoUseCase,
) : BaseViewModel<EditBioState, EditBioEvent>() {

    private var _updateBioState: MutableStateFlow<Result<List<ProfileInfo>>?> = MutableStateFlow(null)

    private var _getProfileData: MutableStateFlow<Result<ProfileInfo>?> = MutableStateFlow(null)

    private val _bioContent = MutableStateFlow("")
    val bioContent : StateFlow<String> = _bioContent

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel : StateFlow<ProfileInfo> = _profileModel

    override fun initState(): EditBioState {
        return EditBioState()
    }

    init {
        initObserver()
    }

    private fun initObserver() {
        viewModelScope.launch(Dispatchers.IO) {
            _getProfileData.collectLatest {  result ->
                result?.onResultReceived(
                    onSuccess = {
                        triggerStateEvent(
                            EditBioEvent.LoadDefaultValueSuccess(
                                profileInfo = it?:ProfileInfo()
                            )
                        )
                    },
                    onError = {
                        triggerStateEvent(EditBioEvent.ShowLoading(false))
                    },
                    onLoading = {
                        triggerStateEvent(EditBioEvent.ShowLoading(true))
                    }
                )
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _updateBioState.collectLatest { result ->
                result?.onResultReceived(
                    onLoading = {
                        triggerStateEvent(EditBioEvent.ShowLoading(true))
                    },
                    onSuccess = {
                        triggerStateEvent(EditBioEvent.UpdateBioSuccess(true))
                    },
                    onError = {
                        triggerStateEvent(EditBioEvent.ShowLoading(false))
                    }
                )
            }
        }
    }

    override fun onTriggeredEvent(event: EditBioEvent) {
        when(event){
            is EditBioEvent.Validate -> {
                setState(
                    uiState.value.copy(
                        errMessage = event.errMessage,
                        isValid = event.isValid,
                        message = event.message
                    )
                )
            }
            is EditBioEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        showLoading = event.isLoading
                    )
                )
            }
            is EditBioEvent.LoadDefaultValueSuccess -> {
                setState(
                    uiState.value.copy(
                        showLoading = false,
                        profileInfo = event.profileInfo,
                        message = event.profileInfo.bio
                    )
                )
            }
            is EditBioEvent.UpdateBioSuccess -> {
               setState(
                   uiState.value.copy(
                       showLoading = false,
                       isUpdateBioSuccess = event.isUpdateBioSuccess
                   )
               )
            }
        }
    }

    fun validateMessage(msg: String){
        when {
            msg.isEmpty() -> {
                triggerStateEvent(
                    EditBioEvent.Validate(
                        isValid = false,
                        errMessage = "Bio should not be empty",
                        message = msg
                    )
                )
            }
            msg.length > 500 -> {
                triggerStateEvent(
                    EditBioEvent.Validate(
                        isValid = false,
                        errMessage = "Maximum 500 characters",
                        message = msg
                    )
                )
            }
            else -> triggerStateEvent(
                EditBioEvent.Validate(
                    isValid = true,
                    message = msg
                )
            )
        }
    }

    fun updateBio(){
        val bio = uiState.value.message
        if (bio.isNullOrEmpty())
            return
        viewModelScope.launch(Dispatchers.IO) {
            _updateBioState.emit(Result.Loading(null))
            val userId = sharedPrefs.getUserInfoLocal().userId
            val profileInfo = ProfileUpdateInfo().copy(id = listOf(userId))
            val temp = Gson().toJson(profileInfo)
            val childRequest = ProfileUpdateRequest().copy(
                bio = bio
            )
            val bodyRequest = ProfileBodyRequest(childRequest)
            val params = SetProfileInfoUseCase.Params(bodyRequest)
            setProfileUseCase.invoke(
                temp,
                true,
                params
            ).collectLatest {
                _updateBioState.emit(it)
                triggerStateEvent(EditBioEvent.UpdateBioSuccess(true))
                _profileModel.update { profile ->
                        profile.copy(bio = bio)
                }
                saveLocal(_profileModel.value)
            }
        }
    }

    private fun saveLocal(profileInfo: ProfileInfo){
        viewModelScope.launch(Dispatchers.IO) {
            if(sharedPrefs.getUserInfoLocal().userId != null){
                val profileInfoFinal = profileInfo.copy(id = sharedPrefs.getUserInfoLocal().userId)
                sharedPrefs.setProfile(profileInfoFinal)
            }
        }
    }

    fun getProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            _getProfileData.emit(Result.Loading(null))
            val filed = "[\"name\",\"bio\",\"gender\",\"birthday\",\"avatar\",\"phoneNumber\",\"email\"]"
            val profileRequest = GetProfileRequest(sharedPrefs.getUserInfoLocal().userId, filed)
            getProfileUseCase.invoke(GetProfileUseCase.Params(profileRequest)).collectLatest {
                val profile = it.data?.last()
                if (profile != null) {
                    _getProfileData.emit(Result.Success(profile))
                    _bioContent.update {
                        profile.bio.orEmpty()
                    }
                    _profileModel.value = profile
                    saveLocal(profile)
                } else {
                    _getProfileData.emit(Result.Error("Profile null"))
                }
            }
        }
    }

    fun resetState(){
        triggerStateEvent(EditBioEvent.UpdateBioSuccess(false))
    }

}