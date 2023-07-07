package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.business_type.BusinessType
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.defination.StepType
import olmo.wellness.android.domain.model.defination.re_submit.ErrorResubmitType
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.domain.use_case.GetBusinessOwnedUseCase
import olmo.wellness.android.domain.use_case.GetBusinessTypeListUseCase
import olmo.wellness.android.domain.use_case.GetCountryListUseCase
import olmo.wellness.android.domain.use_case.GetVerificationInfoUseCase
import olmo.wellness.android.domain.use_case.GetVerificationInfoUseCase.Params
import javax.inject.Inject

@HiltViewModel
class SellerVerificationStep1ViewModel @Inject constructor(
    application: Application,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val getBusinessTypeListUseCase: GetBusinessTypeListUseCase,
    private val getBusinessOwnedUseCase: GetBusinessOwnedUseCase,
    private val getVerificationInfoUseCase: GetVerificationInfoUseCase
) : BaseViewModel(application) {

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _businessTypeList = MutableStateFlow(listOf<BusinessType>())
    val businessTypeList: StateFlow<List<BusinessType>> = _businessTypeList

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _errorResubmit = MutableStateFlow<List<StepType>>(emptyList())
    val errorResubmit: StateFlow<List<StepType>> = _errorResubmit

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _allImagesFromGallery: MutableStateFlow<List<Uri>> = MutableStateFlow(listOf())
    val allImagesFromGallery: StateFlow<List<Uri>> = _allImagesFromGallery

    private val _verificationInfo = MutableStateFlow<VerificationData?>(null)
    val verificationInfo: StateFlow<VerificationData?> = _verificationInfo

    private val _businessId = MutableStateFlow<Int?>(null)
    val businessId: StateFlow<Int?> = _businessId

    fun setBusinessId(id: Int) {
        if(id != 0){
            _businessId.value = id
        }
    }

    private val _identityInput = MutableStateFlow<String?>(null)
    val identityInput : StateFlow<String?> = _identityInput
    fun setIdentityInput(inputData: String?){
        _identityInput.value = inputData
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

    fun onRefresh() {
        _countryList.value = emptyList()
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
                        business?.let { businessOwn ->
                            _businessId.value = businessOwn.businessId
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
                        if(it.data != null){
                            _verificationInfo.value = it.data
                            _currentStep.value = it.data.currentSiv1Step?.plus(1) ?: 1
                            checkIssue(it.data)
                        }
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
                            _countryList.value = it.data.sortedBy { it.name }
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

    private fun checkIssue(validation: VerificationData?) {
        viewModelScope.launch(Dispatchers.Default) {
            val listError : MutableList<StepType> = arrayListOf()
            validation?.verificationDetail?.map {
                if(it.fieldIssue != null){
                    when(it.fieldIssue){
                        ErrorResubmitType.STEP1_CONTACT_PHONE.err,
                        ErrorResubmitType.STEP1_CONTACT_EMAIL.err,
                        ErrorResubmitType.STEP1_BUSINESS_TYPE.err,
                        ErrorResubmitType.STEP1_BUSINESS_NAME.err,
                        ErrorResubmitType.STEP1_BUSINESS_ADDRESS.err,
                        ErrorResubmitType.STEP1_BUSINESS_LOCATION.err -> {
                            listError.add(StepType.STEP1)
                        }

                        ErrorResubmitType.STEP2_COUNTRY_CITIZEN.err,
                        ErrorResubmitType.STEP2_BIRTHDAY.err,
                        ErrorResubmitType.STEP2_ID_TYPE.err,
                        ErrorResubmitType.STEP2_ID_NUMBER.err,
                        ErrorResubmitType.STEP2_ID_EXPIRATION.err,
                        ErrorResubmitType.STEP2_ID_COUNTRY.err -> {
                            listError.add(StepType.STEP2)
                        }

                        ErrorResubmitType.STEP3_SERVICE_NAME.err,
                        ErrorResubmitType.STEP3_SERVICE_LICENSE.err,
                        ErrorResubmitType.STEP3_CATEGORIES.err -> {
                            listError.add(StepType.STEP3)
                        }

                        ErrorResubmitType.STEP4_BANK_NAME.err,
                        ErrorResubmitType.STEP4_BANK_COUNTRY.err,
                        ErrorResubmitType.STEP4_HOLDER_NAME.err,
                        ErrorResubmitType.STEP4_ACCOUNT_NUMBER.err,
                        ErrorResubmitType.STEP4_BANK_BRANCH.err -> {
                            listError.add(StepType.STEP4)
                        }
                    }
                }
                _errorResubmit.value = listError
            }
        }
    }
}