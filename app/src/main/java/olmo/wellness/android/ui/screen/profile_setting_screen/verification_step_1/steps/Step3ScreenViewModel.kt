package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.steps

import android.app.Application
import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.defination.TypeURI
import olmo.wellness.android.domain.model.defination.WrapURI
import olmo.wellness.android.domain.model.defination.re_submit.ErrorResubmitType
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.domain.model.verification1.step3.Step3Request
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.domain.use_case.SubmitVerification1Step3UseCase.Params
import olmo.wellness.android.extension.sizeInMb
import java.io.File
import javax.inject.Inject

@HiltViewModel
class Step3ScreenViewModel @Inject constructor(
    application: Application,
    private val submitVerification1Step3UseCase: SubmitVerification1Step3UseCase,
    private val checkStoreNameUseCase: CheckStoreNameUseCase,
    private val getUploadUrlInfoUseCase: GetUploadUrlInfoUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
    private val getVerificationInfoUseCase: GetVerificationInfoUseCase
) : BaseViewModel(application) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _listSubcategoryDefaultSelected: MutableList<Int>? = null
    val listSubcategoryDefaultSelected: List<Int> get() = _listSubcategoryDefaultSelected?: listOf()

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    fun setIsSuccess(status: Boolean){
        _isSuccess.value = status
    }

    private val _isValidate = MutableStateFlow(false)
    val isValidate: StateFlow<Boolean> = _isValidate

    private val businessId = MutableStateFlow<Int?>(null)
    fun setBusinessId(value: Int?) {
        businessId.value = value
    }

    private val verificationData = MutableStateFlow<VerificationData?>(null)
    fun setVerificationData(data : VerificationData?) {
        if (verificationData.value == null) {
            verificationData.value = data
            setListSubCategoriesSelected(data?.step13?.subCategoryIds)
            if (verificationData.value != null) {
                checkIssue()
            }
        }
    }

    private fun setListSubCategoriesSelected(subCategoryIds: List<Int>?) {
        if (_listSubcategoryDefaultSelected == null) {
            _listSubcategoryDefaultSelected = mutableListOf<Int>().apply {
                addAll(
                    subCategoryIds?: listOf()
                )
            }
        }
    }

    fun removeIndexMapped(subCategoryId: Int){
        if (_listSubcategoryDefaultSelected?.contains(subCategoryId) == true) {
            _listSubcategoryDefaultSelected?.remove(subCategoryId)
        }
    }

    private val storeName = MutableStateFlow<String?>(null)
    fun setStoreName(name: String) {
        storeName.value = name
        checkStoreName()
        _isValidate.value = isValidateData()
    }

    private val _isStoreNameAvailable = MutableStateFlow<Boolean?>(null)
    val isStoreNameAvailable: StateFlow<Boolean?> = _isStoreNameAvailable

    private val hasBrandLicense = MutableStateFlow<Boolean?>(null)
    val getBrandLicense : StateFlow<Boolean?> = hasBrandLicense
    fun setHasBrandLicense(value: Boolean) {
        hasBrandLicense.value = value
    }

    private val subCategoryIds = MutableStateFlow<List<Int>?>(null)
    fun setSubCategoryIds(value: List<Int>) {
        subCategoryIds.value = value
        _isValidate.value = isValidateData()
    }

    private val serviceLicenses = MutableStateFlow<List<String>>(emptyList())
    fun setServiceLicenses(value: List<String>) {
        serviceLicenses.value = value
    }

    private val _identifyList = MutableStateFlow<List<WrapURI>>(mutableListOf())
    val identifyList : StateFlow<List<WrapURI>> = _identifyList

    private val listAll : MutableList<WrapURI> = ArrayList(emptyList())
    fun setIdentifyList(value: List<WrapURI>) {
        val distinct = value.distinctBy { it.uri?.scheme }
        listAll.addAll(distinct)
        val set: Set<WrapURI> = HashSet<WrapURI>(listAll)
        val mergeListWithoutDuplicates: ArrayList<WrapURI> = ArrayList<WrapURI>()
        mergeListWithoutDuplicates.addAll(set)
        _identifyList.value = mergeListWithoutDuplicates
    }

    /* Mapping Error with filedName Or FiledIssue */
    /* Define Issue */
    private val _hasErrServiceName = MutableStateFlow(false)
    val hasErrServiceName : StateFlow<Boolean> = _hasErrServiceName

    private val _hasErrServiceLicense = MutableStateFlow(false)
    val hasErrServiceLicense : StateFlow<Boolean> = _hasErrServiceLicense

    private val _hasErrCategory = MutableStateFlow(false)
    val hasErrTypeDocument : StateFlow<Boolean> = _hasErrCategory

    private fun isValidateData(): Boolean {
        if(isResubmit()){
            return true
        }
        return isValidateInput()
    }

    private fun isValidateInput() : Boolean{
        if (storeName.value.isNullOrEmpty())
            return false

        if (_isStoreNameAvailable.value == null || _isStoreNameAvailable.value == false)
            return false

        if (subCategoryIds.value == null || subCategoryIds.value?.isEmpty() == true)
            return false
        return true
    }

    private fun getRequest() =
        Step3Request(
            storeName = storeName.value,
            isHaveServiceLicense = hasBrandLicense.value,
            subCategoryIds = subCategoryIds.value,
            serviceLicenses = if(hasBrandLicense.value == true){
                serviceLicenses.value
            }else{
                null
            }
        )

    fun submitStep3() {
        /* Step1
        * 1. upload Uri to ger url
        * 2. submitStep3
        * */
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.update { true }
            _error.value = ""
            if(hasBrandLicense.value == true){
                if(_identifyList.value.distinctBy { it.uri?.scheme }.isNotEmpty()){
                    val listUrl = ArrayList<String>()
                    _identifyList.value.forEach { documentUriSelected ->
                        val file = documentUriSelected.uri?.path?.let { path -> File(path) }
                        file?.let {
                            if (file.sizeInMb > 5) {
                                _error.value = context.getString(R.string.error_file_size)
                                _isLoading.update { false }
                                return@launch
                            } else
                                documentUriSelected.uri?.let {
                                    val documentUpload = async { uploadFile(it) }
                                    val resultDocumentUpload = documentUpload.await()
                                    if (resultDocumentUpload != null) {
                                        listUrl.add(resultDocumentUpload)
                                    }
                                }
                        }
                    }
                    if(listUrl.isNotEmpty()){
                        serviceLicenses.value = listUrl.distinct()
                        if(serviceLicenses.value.size == _identifyList.value.size){
                            handleSubmitStep3()
                        }
                    }
                }
            }else{
                handleSubmitStep3()
            }
        }
    }

//    private fun getRequest() =
//        Step3Request(
//            storeName = storeName.value,
//            isHaveServiceLicense = hasBrandLicense.value,
//            subCategoryIds = subCategoryIds.value,
//            serviceLicenses = if(hasBrandLicense.value){
//                serviceLicenses.value
//            }else{
//                emptyList()
//            }
//        )
    private fun handleSubmitStep3() {
        viewModelScope.launch(Dispatchers.Default) {
            if((verificationData.value?.step13 != null) || (_isSuccess.value)){
                businessId.value?.let {
                    submitVerification1Step3UseCase.invokeUpdate(
                        Params(it, getRequest())
                    ).collectLatest {
                        handleResult(it)
                    }
                }
            }else{
                businessId.value?.let {
                    submitVerification1Step3UseCase(
                        Params(it, getRequest())
                    ).collectLatest {
                        handleResultForPosting(it)
                    }
                }
            }
        }
    }

    fun isResubmit() : Boolean{
        return if((_isSuccess.value) || (verificationData.value?.step12 != null)){
            _isValidate.value = true
            true
        }else{
            _isValidate.value = false
            false
        }
    }

    private fun handleResult(it: Result<Boolean>) {
        when (it) {
            is Result.Success -> {
                _isLoading.value = false
                _error.value = ""
                _isSuccess.value = true
            }
            is Result.Error -> {
                _isLoading.value = false
                _isSuccess.value = false
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

    private fun handleResultForPosting(it: Result<Boolean>) {
        when (it) {
            is Result.Success -> {
                _isLoading.value = true
                _error.value = ""
                if(businessId.value != null){
                    getVerificationInfo(businessId.value!!)
                }
            }
            is Result.Error -> {
                _isLoading.value = false
                _isSuccess.value = false
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

    private fun getVerificationInfo(businessId: Int) {
        _error.value = ""
        viewModelScope.launch {
            getVerificationInfoUseCase(GetVerificationInfoUseCase.Params(businessId)).collectLatest {
                when (it) {
                    is Result.Success -> {
                        if(it.data != null){
                            verificationData.value = it.data
                            clearFiled()
                        }
                        _isSuccess.value = true
                        _isLoading.value = false
                    }
                    else -> {
                        _isLoading.value = false
                        _isSuccess.value = false
                        it.message?.let { errorMessage ->
                            _error.value = errorMessage
                        }
                    }
                }
            }
        }
    }

    private fun clearFiled(){
        _identifyList.value = emptyList()
        isResubmit()
    }

    private fun checkStoreName() {
        if(isValidateInput())
            return
        viewModelScope.launch {
            checkStoreNameUseCase(CheckStoreNameUseCase.Params(storeName.value?:"")).collectLatest {
                when (it) {
                    is Result.Success -> {
                        _isStoreNameAvailable.value = true
                        isValidateData()
                    }
                    else -> {
                        _isStoreNameAvailable.value = false
                        isValidateData()
                    }
                }
            }
        }
    }

    /* Util */
    private suspend fun uploadFile(fileUri: Uri): String? {
        var uploadSuccess: String? = null
        try {
            _error.value = ""
            val mimeType = context.contentResolver.getType(fileUri) ?: Constants.MIME_IMAGE

            val uploadInfo = viewModelScope.async {
                getUploadUrlInfoUseCase(
                    GetUploadUrlInfoUseCase.Params(
                        mimeType.substring(mimeType.indexOf("/") + 1).ifEmpty { "" })
                ).last()
            }
            uploadInfo.await().data?.let {
                val uploadStatus = viewModelScope.async {
                    requestUploadFile(
                        it.putPresignedUrl.orEmpty(),
                        fileUri
                    ).last()
                }
                val result = uploadStatus.await().data ?: false
                if (result) {
                    uploadSuccess = it.objectKey
                } else _error.value = context.getString(R.string.upload_fail)
            }
        } catch (e: Exception) {
            _error.value = context.getString(R.string.upload_fail)
        }
        return uploadSuccess
    }

    private fun requestUploadFile(presignedUrl: String, fileUri: Uri) =
        uploadFileUseCase(UploadFileUseCase.Params(presignedUrl, fileUri))

    /* Get Optional when Resubmit */
    fun getStoreName(): String?{
        return verificationData.value?.step13?.storeName
    }

    private fun isStep13Null() : Boolean{
        if(verificationData.value == null || verificationData.value?.step13 == null){
            return true
        }
        return false
    }

    fun getOptionBranchLicense(): Boolean?{
        return verificationData.value?.step13?.isHaveServiceLicense
    }

    fun getListCategory(): List<Int>?{
        if(subCategoryIds.value != null){
            return return subCategoryIds.value
        }
        return verificationData.value?.step13?.subCategoryIds
    }

    fun getIdentity(): List<WrapURI?>?{
        return try{
            verificationData.value?.step13?.serviceLicenses?.map { value ->
                WrapURI(typeURI = TypeURI.IMAGE_TYPE, uri = Uri.parse(value))
            }
        }catch (ex: Exception){
            emptyList()
        }
    }

    private fun checkIssue(){
        viewModelScope.launch(Dispatchers.Default) {
            if(verificationData.value?.step13 != null){
                verificationData.value?.verificationDetail?.map {
                    if(it.fieldIssue != null){
                        when(it.fieldIssue){
                            ErrorResubmitType.STEP3_CATEGORIES.err -> {
                                _hasErrCategory.value = true
                            }
                            ErrorResubmitType.STEP3_SERVICE_LICENSE.err -> {
                                _hasErrServiceLicense.value = true
                            }
                            ErrorResubmitType.STEP3_SERVICE_NAME.err -> {
                                _hasErrServiceName.value = true
                            }
                        }
                    }
                }
            }
        }
    }
}