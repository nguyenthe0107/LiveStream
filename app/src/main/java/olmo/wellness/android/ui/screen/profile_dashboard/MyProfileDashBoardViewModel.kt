package olmo.wellness.android.ui.screen.profile_dashboard

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.core.enums.UploadFileType
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.delete_account.DeleteAccountRequest
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.profile.update.ProfileUpdateRequest
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.profile.ProfileUpdateInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.model.user_info.UserInfoLocal
import olmo.wellness.android.domain.model.verification1.step1.Address
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.domain.use_case.socket.connection.DisconnectSessionUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.screen.account_setting.common.logoutAccount
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.UploadFileIdServerViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileDashBoardViewModel @Inject constructor(
    application: Application,
    private val getProfileUseCase: GetProfileUseCase,
    private val setProfileInfoUseCase: SetProfileInfoWellnessUseCase,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val getApiUseCase: GetApiUseCase,
    private val requestToDeleteUseCase: RequestToDeleteUseCase,
    val getUploadUrlInfoUseCase: GetUploadIdServerUrlInfoUseCase,
    val uploadFileUseCase: UploadFileIdServerUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val disconnectSessionUseCase: DisconnectSessionUseCase,
    uploadUrlInfoUseCase: GetUploadUrlInfoUseCase,
) : UploadFileIdServerViewModel(application, getUploadUrlInfoUseCase, uploadUrlInfoUseCase, uploadFileUseCase) {

    private val _isLoading = MutableStateFlow(false)
    val isNeedLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val errorResult: StateFlow<String> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    fun resetState() {
        _isSuccess.value = false
    }

    private val _isValidate = MutableStateFlow(false)
    val isValidate: StateFlow<Boolean> = _isValidate

    private val _allImagesFromGallery: MutableStateFlow<List<Uri>> = MutableStateFlow(listOf())

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel: StateFlow<ProfileInfo> = _profileModel

    private val _avatar = MutableStateFlow("")
    val avatar: StateFlow<String> = _avatar

    private val _authMethod = MutableStateFlow("")
    val authMethod: StateFlow<String> = _authMethod

    private val _userLocal = MutableStateFlow(UserInfoLocal())
    val userLocal: StateFlow<UserInfoLocal> = _userLocal

    private val _addressLocal = MutableStateFlow(Address())
    private val addressLocal: StateFlow<Address> = _addressLocal

    private val _storeID = MutableStateFlow(-1)
    val storeID: StateFlow<Int> = _storeID

    private val _storeName = MutableStateFlow("")
    val storeName: StateFlow<String> = _storeName

    private val _isLogoutSuccess = MutableStateFlow<Boolean>(false)
    val isLogoutSuccess: StateFlow<Boolean> = _isLogoutSuccess

    fun reCallProfileInfo() {
        if (sharedPrefs.getProfile().id != null) {
            var profileLocal = sharedPrefs.getProfile()
            _profileModel.value = profileLocal
            if (_profileModel.value.storeName?.isNotEmpty() == true) {
                _storeName.value = _profileModel.value.storeName.orEmpty()
            }
        }
    }

    fun reCallAddressDetailInfo() {
        _addressLocal.value = sharedPrefs.getAddressDetail()
    }

    private fun getAllImages() {
        val allImages = mutableListOf<Uri>()

        val imageProjection = arrayOf(
            MediaStore.Images.Media._ID
        )
        val imageSortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val cursor = Contexts.getApplication(context).contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            imageSortOrder
        )
        cursor.use {
            if (cursor != null) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    allImages.add(
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            cursor.getLong(idColumn)
                        )
                    )
                }
            }
        }
        _allImagesFromGallery.value = allImages
    }

    fun loadAllImages() {
        viewModelScope.launch {
            getAllImages()
        }
    }

    init {
        getProfile()
        getAvatarWellnessApi()
        getUserLocal()
        getCountryList()
        getBusinessOwnerInfo()
    }

    private fun getUserLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            val userLocal = sharedPrefs.getUserInfoLocal()
            _userLocal.value = userLocal
            val address = sharedPrefs.getAddressDetail()
            _addressLocal.value = address
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            var profile = sharedPrefs.getProfile()
            val userLocal = sharedPrefs.getUserInfoLocal()
            if (userLocal.userId != null) {
                _authMethod.value = userLocal.auMethod.orEmpty()
                profile = profile.copy(id = userLocal.userId)
            }
            if (profile.id != null) {
                _profileModel.value = profile
            }
            val filed = "[\"name\",\"bio\",\"gender\",\"birthday\",\"avatar\",\"phoneNumber\",\"email\"]"
            val profileRequest = GetProfileRequest(userLocal.userId, filed)
            getProfileUseCase.invoke(GetProfileUseCase.Params(profileRequest)).collectLatest {
                when (it) {
                    is Result.Success -> {
                        if (it.data?.isNotEmpty() == true) {
                            val response = it.data.last()
                            _profileModel.update {
                                response
                            }
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

    @SuppressLint("SuspiciousIndentation")
    private fun getAvatarWellnessApi() {
        viewModelScope.launch(Dispatchers.IO) {
            getApiUseCase.getUserInfo().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data.let { response ->
                            if(response?.avatar?.isNotEmpty() == true){
                                _avatar.value = response.avatar.orEmpty()
                            }
                        }
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

    private fun saveLocal(avatarLink: String? = null,
                          bio: String ?= null,
                          birthday: String ?= null,
                          email: String ?= null,
                          phone: String ?= null)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = sharedPrefs.getUserInfoLocal().userId
            if (userId != null) {
                _profileModel.update {
                    it.copy(
                        id = userId
                    )
                }
            }
            var profileInfo = sharedPrefs.getProfile()
            if (avatarLink?.isNotEmpty() == true) {
                profileInfo = profileInfo.copy(id = userId,avatar = avatarLink)
            }
            if (bio?.isNotEmpty() == true) {
                profileInfo = profileInfo.copy(id = userId,bio = bio)
            }
            if (birthday?.isNotEmpty() == true) {
                profileInfo = profileInfo.copy(id = userId,birthday = birthday)
            }
            if (email?.isNotEmpty() == true) {
                profileInfo = profileInfo.copy(id = userId,email = email)
            }
            if (phone?.isNotEmpty() == true) {
                profileInfo = profileInfo.copy(id = userId,phoneNumber = phone)
            }
            if (storeID.value != -1) {
                profileInfo = profileInfo.copy(id = userId, storeId = storeID.value)
            }
            if (storeName.value.isNotEmpty()) {
                profileInfo = profileInfo.copy(id = userId, storeName = storeName.value)
            }
            sharedPrefs.setProfile(profileInfo)
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

    fun deleteAccount(reason: String?) {
        _isLoading.value = true
        val rqt = DeleteAccountRequest(reason)
        val param = RequestToDeleteUseCase.Params(rqt)
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            requestToDeleteUseCase.invoke(param).collect {
                when (it) {
                    is Result.Success -> {
                        _isLoading.value = false
                        logoutAccount(scope = viewModelScope, notificationUseCase = notificationUseCase, disconnectSessionUseCase = disconnectSessionUseCase,_isLoading,_isLogoutSuccess)
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        logoutAccount(scope = viewModelScope, notificationUseCase = notificationUseCase, disconnectSessionUseCase = disconnectSessionUseCase,_isLoading,_isLogoutSuccess)
                    }
                    is Result.Loading -> {
                        _isLoading.value = true

                    }
                }
            }
        }
    }

    fun updateAvatar(avatar: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            if (userLocal.value.userId != null) {
                val profileInfo = ProfileUpdateInfo().copy(id = listOf(userLocal.value.userId))
                val temp = Gson().toJson(profileInfo)
                val childRequest = ProfileUpdateRequest().copy(
                    avatar = avatar
                )
                val bodyRequest = ProfileBodyRequest(childRequest)
                val param = SetProfileInfoWellnessUseCase.Params(bodyRequest)
                setProfileInfoUseCase.invoke(temp, true, param).collect {
                    when (it) {
                        is Result.Success -> {
                            _isLoading.value = false
                            _isSuccess.value = true
                            val profile = it.data?.first()
                            if (profile?.avatar != null) {
                                _avatar.value = profile.avatar.orEmpty()
                                _profileModel.update { profileInternal ->
                                    profileInternal.copy(avatar = profile.avatar.orEmpty())
                                }
                                saveLocal(avatarLink = profile.avatar)
                            }
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
    }

    fun getAddressDetail(): String {
        var address = ""
        if (addressLocal.value.city != null || addressLocal.value.address != null) {
            if (addressLocal.value.address?.isNotEmpty() == true) {
                address = addressLocal.value.address.plus(",")
            }
            if (addressLocal.value.city?.isNotEmpty() == true) {
                address += addressLocal.value.city
            }
            if (address.length > 20) {
                address.take(20).plus("...")
            }
        }
        return address
    }

    private fun getBusinessOwnerInfo() {
        val userLocal = sharedPrefs.getUserInfoLocal()
        if (userLocal.userTypeModel != UserTypeModel.BUYER) {
            viewModelScope.launch(Dispatchers.IO) {
                getApiUseCase.getUserInfo().collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            _isLoading.value = false
                            _isLoading.value = false
                            result.data.let { response ->
                                _storeID.update {
                                    response?.store?.id ?: -1
                                }
                                _profileModel.update { profile ->
                                    profile.copy(
                                        storeId = response?.store?.id,
                                        storeName = response?.store?.name,
                                        id = userLocal.userId)
                                }
                                _storeName.update {
                                    response?.store?.name.orEmpty()
                                }
                                saveLocal(
                                    bio = response?.bio,
                                    birthday = response?.birthday,
                                    email = response?.email,
                                    phone = response?.phoneNumber
                                )
                            }
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
    }

    fun handleUploadCapture(imageSelectedUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageUpload = async { uploadFile(imageSelectedUri, UploadFileType.PROFILE) }
            val resultImage = imageUpload.await()
            if (resultImage != null) {
                updateAvatar(resultImage)
                reCallProfileInfo()
            }
        }
    }

}