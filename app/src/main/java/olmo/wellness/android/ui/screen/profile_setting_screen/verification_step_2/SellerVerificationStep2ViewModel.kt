package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_2

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.core.enums.DocumentTypeUri
import olmo.wellness.android.domain.model.business_type.BusinessType
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.domain.model.verification2.V2Step1Request
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.domain.use_case.GetVerificationInfoUseCase.Params
import javax.inject.Inject

@HiltViewModel
class SellerVerificationStep2ViewModel @Inject constructor(
    application: Application,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val getBusinessTypeListUseCase: GetBusinessTypeListUseCase,
    private val getBusinessOwnedUseCase: GetBusinessOwnedUseCase,
    private val getVerificationInfoUseCase: GetVerificationInfoUseCase,
    private val submitVerification2Step1UseCase: SubmitVerification2Step1UseCase
) : BaseViewModel(application) {

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _businessTypeList = MutableStateFlow(listOf<BusinessType>())
    val businessTypeList: StateFlow<List<BusinessType>> = _businessTypeList

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _allImagesFromGallery: MutableStateFlow<List<Uri>> = MutableStateFlow(listOf())
    val allImagesFromGallery: StateFlow<List<Uri>> = _allImagesFromGallery

    private val _verificationInfo = MutableStateFlow<VerificationData?>(null)
    val verificationInfo: StateFlow<VerificationData?> = _verificationInfo

    private val _businessId = MutableStateFlow<Int?>(null)
    val businessId: StateFlow<Int?> = _businessId

    private val _documentUrl: MutableStateFlow<MutableMap<DocumentTypeUri, String>> =
        MutableStateFlow(mutableMapOf<DocumentTypeUri, String>())
    private val documentUrl: StateFlow<MutableMap<DocumentTypeUri, String>> = _documentUrl

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun setDocumentUrl(documentTypeUri: DocumentTypeUri?, url: String?) {
        if (documentTypeUri != null) {
            _documentUrl.value[documentTypeUri] = url ?: ""
        }
    }

    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep

    fun setCurrentStep(step: Int) {
        _currentStep.value = step
    }

    init {
        getBusinessTypeList()
        getBusinessOwnedList()
        getCountryList()
    }

    private fun getBusinessOwnedList() {
        _error.value = ""
        viewModelScope.launch {
            getBusinessOwnedUseCase().collect {
                when (it) {
                    is Result.Success -> {
                        _isLoading.value = false
                        val businessList = it.data.orEmpty()
                        val business = businessList.lastOrNull()
                        _businessId.value = business?.businessId
                        business?.let { businessOwn ->
                            getVerificationInfo(businessOwn.businessId)
                        }
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

    private fun getVerificationInfo(businessId: Int) {
        _error.value = ""
        viewModelScope.launch {
            getVerificationInfoUseCase(Params(businessId)).collectLatest {
                when (it) {
                    is Result.Success -> {
                        _isLoading.value = false
                        _verificationInfo.value = it.data
                        _currentStep.value = it.data?.currentSiv1Step?.plus(1) ?: 1
                    }
                    else -> {
                        _isLoading.value = false
                        it.message?.let { errorMessage ->
                            _error.value = errorMessage
                        }
                    }
                }
            }
        }
    }

    private fun getCountryList() {
        viewModelScope.launch {
            getCountryListUseCase().collectLatest {
                when (it) {
                    is Result.Success -> {
                        _error.value = ""
                        if (!it.data.isNullOrEmpty()) {
                            _countryList.value = emptyList()
                            _countryList.value = it.data.orEmpty()
                        }
                    }
                    else -> {
                        it.message?.let { errorMessage ->
                            _error.value = errorMessage
                        }
                    }
                }
            }
        }
    }

    private fun getBusinessTypeList() {
        viewModelScope.launch {
            getBusinessTypeListUseCase().collectLatest {
                when (it) {
                    is Result.Success -> {
                        _error.value = ""
                        if (!it.data.isNullOrEmpty()) {
                            _businessTypeList.value = emptyList()
                            _businessTypeList.value = it.data.orEmpty()
                        }
                    }
                    else -> {
                        it.message?.let { errorMessage ->
                            _error.value = errorMessage
                        }
                    }
                }
            }
        }
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

    @SuppressLint("NewApi")
    fun getRequest() : V2Step1Request{
        var stepRequest = V2Step1Request()
        documentUrl.value.forEach { (documentTypeUri, url) ->
            when (documentTypeUri) {
                DocumentTypeUri.HistorySellReport -> {
                    stepRequest = stepRequest.copy(sellReport = url )
                }
                DocumentTypeUri.BrandLicense -> {
                    stepRequest = stepRequest.copy(brandLicense = url)
                }
                DocumentTypeUri.ElectricityBill -> {
                    stepRequest = stepRequest.copy(electricBill = url)
                }
                DocumentTypeUri.BusinessLicense -> {
                    stepRequest = stepRequest.copy(businessLicense = url)
                }

            }
        }
        return stepRequest
    }

    @SuppressLint("NewApi")
    fun uploadDocumentFinal() {
        businessId.value?.let {
            _isLoading.value = true
            viewModelScope.launch {
                submitVerification2Step1UseCase(
                    SubmitVerification2Step1UseCase.Params(
                        it,
                        getRequest()
                    )
                ).collect {
                    when (it) {
                        is Result.Success -> {
                            _isSuccess.value = true
                            _isLoading.value = false
                            _error.value = ""
                        }
                        is Result.Error -> {
                            _isSuccess.value = false
                            _isLoading.value = false
                            it.message?.let { errorMessage ->
                                _error.value = errorMessage
                            }
                        }
                        is Result.Loading -> {
                            _error.value = ""
                            _isLoading.value = true
                        }
                    }
                }
            }
        }
    }
}