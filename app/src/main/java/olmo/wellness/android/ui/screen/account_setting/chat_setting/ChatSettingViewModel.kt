package olmo.wellness.android.ui.screen.account_setting.chat_setting

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.model.user_setting.UserSetting
import olmo.wellness.android.domain.model.user_setting.UserSettingRequest
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class ChatSettingViewModel @Inject constructor(
    application: Application,
    private val getBusinessTypeListUseCase: GetBusinessTypeListUseCase,
    private val getUserLocal: GetUserInfoUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val userSettingsUseCase: UserSettingsUseCase
) : BaseViewModel(application) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _isValidate = MutableStateFlow(false)
    val isValidate: StateFlow<Boolean> = _isValidate

    private val _typeIdentity = MutableStateFlow("")
    val typeIdentity: StateFlow<String> = _typeIdentity

    fun setTypeIdentity(type: String) {
        _typeIdentity.value = type
        _isValidate.value = isValidateData()
    }

    private val _isOpenUploadDialog = MutableStateFlow(false)
    val isOpenUploadDialog: StateFlow<Boolean> = _isOpenUploadDialog

    fun setOpenUploadDialog(open: Boolean) {
        _isOpenUploadDialog.value = open
    }

    private val _identityDocumentUri = MutableStateFlow<String>("")
    val identityDocumentUri: StateFlow<String> = _identityDocumentUri
    fun setIdentityDocumentUris(imageSelected: String) {
        _identityDocumentUri.value = imageSelected
        _isValidate.value = isValidateData()
    }

    private val _allImagesFromGallery: MutableStateFlow<List<Uri>> = MutableStateFlow(listOf())
    val allImagesFromGallery: StateFlow<List<Uri>> = _allImagesFromGallery

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _defaultPrivateChatMode: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val defaultPrivateChatMode: StateFlow<Boolean?> = _defaultPrivateChatMode

    private val _defaultLiveChatMode: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val defaultLiveChatMode: StateFlow<Boolean?> = _defaultLiveChatMode

    private val _defaultAutoReplyMode: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val defaultAutoReplyMode: StateFlow<Boolean?> = _defaultAutoReplyMode

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel: StateFlow<ProfileInfo> = _profileModel

    private val _userId = MutableStateFlow<Int>(0)

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

    private fun isValidateData(): Boolean {
        return true
    }

    init {
        getProfile()
        getCountryList()
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

    private fun getProfile() {
        viewModelScope.launch {
            val profile = sharedPrefs.getProfile()
            if (profile.id != null) {
                _profileModel.value = profile
                getUserSetting(profile.id)
                _userId.value = profile.id
                return@launch
            }

            val filed =
                "[\"id\",\"name\",\"bio\",\"gender\",\"birthday\",\"avatar\",\"phoneNumber\",\"email\"]"
            val profileRequest = GetProfileRequest(sharedPrefs.getUserInfoLocal().userId, filed)
            getProfileUseCase.invoke(GetProfileUseCase.Params(profileRequest)).collect {
                when (it) {
                    is Result.Success -> {
                        if (it.data?.isNotEmpty() == true) {
                            val response = it.data.last()
                            _profileModel.value = response.copy(
                                username = response.name
                            )
                            _userId.value = response.id ?: 0

                            if (_userId.value != 0) {
                                getUserSetting(_userId.value)
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

    private fun getUserSetting(userId: Int) {
        viewModelScope.launch {
            if (userId != 0) {
                val filed =
                    "[\"id\",\"createdAt\",\"lastModified\",\"isPrivateActivity\",\"isAllowContactSyncing\",\"isAllowShareProfile\",\"language\",\"isAllowChatOnLivestream\",\"isAllowPrivateChatOnLivestream\",\"isAllowSendAutoReply\",\"autoReply\",\"isShowMessageShortcut\",\"serviceUpdate\",\"orderUpdate\",\"yourCircle\",\"promotions\",\"olmoFeed\",\"livestream\",\"walletUpdate\"]"
                userSettingsUseCase.invoke(userId, filed).collectLatest {
                    when (it) {
                        is Result.Success -> {
                            if (it.data?.isNotEmpty() == true) {
                                val response = it.data.last()
                                _defaultAutoReplyMode.value = response.isAllowSendAutoReply ?: false
                                _defaultLiveChatMode.value =
                                    response.isAllowChatOnLivestream ?: false
                                _defaultPrivateChatMode.value =
                                    response.isAllowPrivateChatOnLivestream ?: false
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
    }


    fun updateUserSetting(
        privateChatMode: Boolean? = null,
        liveChatMode: Boolean? = null,
        autoReplyMode: Boolean? = null,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            if (_userId.value != 0) {
                val updateBody = UserSettingRequest(
                    UserSetting(
                        isAllowPrivateChatOnLivestream = privateChatMode,
                        isAllowChatOnLivestream = liveChatMode,
                        isAllowSendAutoReply = autoReplyMode,
                    )
                )
                val listUserId = listOf(_userId.value)
                val requestString = Gson().toJson(
                    UserSetting(
                        userId = listUserId
                    )
                )
                userSettingsUseCase.updateUserSetting(requestString, true, updateBody)
                    .collectLatest {
                        when (it) {
                            is Result.Success -> {
                                if (it.data?.isNotEmpty() == true) {
                                    _isSuccess.value = true
                                    if (privateChatMode != null) {
                                        _defaultPrivateChatMode.value=privateChatMode
                                    }
                                    if (liveChatMode != null) {
                                        _defaultLiveChatMode.value=liveChatMode
                                    }
                                    if (autoReplyMode != null) {
                                        _defaultAutoReplyMode.value=autoReplyMode
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
    }
}