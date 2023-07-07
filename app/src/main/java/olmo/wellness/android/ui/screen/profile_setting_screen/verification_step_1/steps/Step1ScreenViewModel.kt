package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.steps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.business_type.BusinessType
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.defination.re_submit.ErrorResubmitType
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.domain.model.verification1.step1.Address
import olmo.wellness.android.domain.model.verification1.step1.ContactPhone
import olmo.wellness.android.domain.model.verification1.step1.Step1Request
import olmo.wellness.android.domain.use_case.GetVerificationInfoUseCase
import olmo.wellness.android.domain.use_case.SubmitVerification1Step1UseCase
import olmo.wellness.android.domain.use_case.SubmitVerification1Step1UseCase.Params
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class Step1ScreenViewModel @Inject constructor(
    private val submitVerification1Step1UseCase: SubmitVerification1Step1UseCase,
    private val getVerificationInfoUseCase: GetVerificationInfoUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    fun setIsSuccess(status: Boolean?){
        if (status != null) {
            _isSuccess.value = status
        }
    }

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _businessId = MutableStateFlow<Int?>(null)
    val businessId: StateFlow<Int?> = _businessId

    private val _businessIdResubmit = MutableStateFlow<Int?>(null)
    fun setBusinessResubmitId(id: Int?){
        _businessIdResubmit.value = id
    }

    /*  _identityInput is email or phone from signup*/
    private val _identityInput = MutableStateFlow<String?>(null)
    fun setIdentityInput(dataInput: String?){
        _identityInput.value = dataInput
    }

    val verificationData = MutableStateFlow<VerificationData?>(null)
    fun setVerificationData(data : VerificationData?) {
        if(data?.step11 != null){
            verificationData.value = data
            if(verificationData.value != null){
                checkIssue()
            }
        }
    }

    private val _isValidate = MutableStateFlow(false)
    val isValidate: StateFlow<Boolean> = _isValidate

    private val countryList = MutableStateFlow<List<Country>>(emptyList())
    fun setCountryList(list: List<Country>) {
        countryList.value = list
        setBusinessContactNationId(list.firstOrNull()?.id ?: 0)
    }

    private val businessTypeList = MutableStateFlow<List<BusinessType>>(emptyList())
    fun setBusinessTypeList(list: List<BusinessType>) {
        businessTypeList.value = list
    }

    private val businessLocation = MutableStateFlow("")
    fun setBusinessLocation(value: String) {
        businessLocation.value = value
        _isValidate.value = isValidateData()
    }

    private val businessType = MutableStateFlow("")
    fun setBusinessType(value: String) {
        businessType.value = value
        _isValidate.value = isValidateData()
    }

    private val businessName = MutableStateFlow("")
    fun setBusinessName(value: String) {
        businessName.value = value
        _isValidate.value = isValidateData()
    }

    private val businessAddressCountry = MutableStateFlow("")
    fun setBusinessAddressCountry(value: String) {
        businessAddressCountry.value = value
    }

    private val businessAddressPostalCode = MutableStateFlow("")
    fun setBusinessAddressPostalCode(value: String) {
        businessAddressPostalCode.value = value
    }

    private val businessAddressCity = MutableStateFlow("")
    fun setBusinessAddressCity(value: String) {
        businessAddressCity.value = value
    }

    private val businessAddressDistrict = MutableStateFlow("")
    fun setBusinessAddressDistrict(value: String) {
        businessAddressDistrict.value = value
    }

    private val businessAddressLine1 = MutableStateFlow("")
    fun setBusinessAddressLine1(value: String) {
        businessAddressLine1.value = value
    }

    private val businessAddressLine2 = MutableStateFlow<String?>(null)
    fun setBusinessAddressLine2(value: String?) {
        businessAddressLine2.value = value
    }

    private val businessContact = MutableStateFlow("")
    fun setBusinessContact(value: String) {
        businessContact.value = value
        _isValidate.value = isValidateData()
    }

    private val businessContactNationId = MutableStateFlow(0)
    fun setBusinessContactNationId(code: Int) {
        businessContactNationId.value = code
        _isValidate.value = isValidateData()
    }

    private fun isValidateData(): Boolean {

        if(isSubmitted()){
            return true
        }

        if (businessLocation.value.isEmpty())
            return false

        if (businessType.value.isEmpty())
            return false

        if (businessName.value.isEmpty())
            return false

        if (businessContact.value.isEmpty())
            return false

        if (businessContactNationId.value == 0)
            return false

        return true
    }

    /* Mapping Error with filedName Or FiledIssue */
    /* Define Issue */
    private val _hasErrBusinessLocation = MutableStateFlow(false)
    val hasErrBusinessLocation : StateFlow<Boolean> = _hasErrBusinessLocation

    private val _hasErrBusinessType = MutableStateFlow(false)
    val hasErrBusinessType : StateFlow<Boolean> = _hasErrBusinessType

    private val _hasErrBusinessName = MutableStateFlow(false)
    val hasErrBusinessName : StateFlow<Boolean> = _hasErrBusinessName

    private val _hasErrBusinessAddress = MutableStateFlow(false)
    val hasErrBusinessAddress : StateFlow<Boolean> = _hasErrBusinessAddress

    private val _hasErrEmail = MutableStateFlow(false)
    val hasErrContact : StateFlow<Boolean> = _hasErrEmail

    private val _hasErrPhone = MutableStateFlow(false)
    val hasErrPhone : StateFlow<Boolean> = _hasErrPhone

    private fun getRequest() =
        Step1Request(
            businessLocationId = countryList.value.firstOrNull() { it.name == businessLocation.value }?.id,
            businessTypeId = businessTypeList.value.firstOrNull { it.name == businessType.value }?.id,
            businessName = businessName.value.ifEmpty { null },
            // Address is Option Object
            address = if(businessAddressCountry.value.isEmpty() &&
                businessAddressPostalCode.value.isEmpty() &&
                businessAddressCity.value.isEmpty() &&
                businessAddressDistrict.value.isEmpty() &&
                businessAddressLine1.value.isEmpty() &&
                businessAddressLine2.value.isNullOrEmpty()) {
                null
            }else{
                Address(
                    countryId = if(businessAddressCountry.value.isNotEmpty()){
                        countryList.value.first { it.name == businessAddressCountry.value }.id
                    }else{ null },
                    zipCode = businessAddressPostalCode.value.ifEmpty { null },
                    city = businessAddressCity.value.ifEmpty{ null },
                    district = businessAddressDistrict.value.ifEmpty { null },
                    addressLine1 = businessAddressLine1.value.ifEmpty { null },
                    addressLine2 = businessAddressLine2.value?.ifEmpty{ null }
                )
            },
            contactPhone = if(businessContactNationId.value != 0 && businessContact.value.isNotEmpty()) {
                ContactPhone(businessContactNationId.value, businessContact.value)
            }else{
                null
            },
            contactEmail = if(_identityInput.value != null){
                if(_identityInput.value?.isNotEmpty() == true){
                    _identityInput.value
                }else{
                    sharedPrefs.getUserInfoLocal().identity
                }
            }else{
                if(!isSubmitted()){
                   verificationData.value?.step11?.contactEmail?.ifEmpty { "" }
                }else{
                    null
                }
            }
        )

    fun submitStep1() {
        viewModelScope.launch {
            if(isSubmitted()){
                _businessIdResubmit.value?.let {
                    submitVerification1Step1UseCase.invokeUpdate(it,Params(getRequest())).collect {
                        handleResult(it)
                    }
                }
            }else{
                submitVerification1Step1UseCase(Params(getRequest())).collect {
                    handleResultApi(it)
                }
            }
        }
    }

    private fun isSubmitted() : Boolean{
        return (verificationData.value?.step11 != null && (_businessIdResubmit.value != null))
    }

    private fun isFirstValidate() : Boolean{
        if(isSubmitted())
            return true
        return false
    }

    private fun handleResultApi(it: Result<Int>) {
        when (it) {
            is Result.Success -> {
                _isLoading.value = false
                _error.value = ""
                _isSuccess.value = true
                _businessId.value = it.data
                if(_businessId.value != null){
                    getVerificationInfo(_businessId.value!!)
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
                _isLoading.value = true
            }
        }
    }

    private fun handleResult(it: Result<Boolean>) {
        when (it) {
            is Result.Success -> {
                _isLoading.value = false
                _error.value = ""
                _businessId.value = _businessIdResubmit.value
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
                _isLoading.value = true
            }
        }
    }

    fun getCountryName(isBusinessLocation : Boolean) : String{
        var value = ""
        if(verificationData.value?.step11 != null){
            if(countryList.value.isNotEmpty()){
                value = if(isBusinessLocation){
                    countryList.value.firstOrNull { it.id == verificationData.value?.step11?.businessLocationId}?.name ?: value
                }else{
                    countryList.value.firstOrNull{ it.id == verificationData.value?.step11?.address?.countryId}?.name ?: value
                }
            }
        }
        return value
    }

    fun getBusinessType() : String{
        var value = ""
        if(verificationData.value?.step11 != null){
            if(businessTypeList.value.isNotEmpty()){
                value = businessTypeList.value.firstOrNull { it.id == verificationData.value?.step11?.businessTypeId }?.name ?: value
            }
        }
        return value
    }

    fun getBusinessName() : String{
        return if(verificationData.value?.step11?.businessName != null){
            verificationData.value?.step11?.businessName ?: ""
        }else{
            ""
        }
    }

    fun getAddressInfo() : Address?{
        return verificationData.value?.step11?.address
    }

    fun getContactInfo() : ContactPhone?{
        _isValidate.value = isFirstValidate()
        return verificationData.value?.step11?.contactPhone
    }

    private fun getEmailInfo() : String?{
        if(verificationData.value == null)
            return ""
        return if(!isSubmitted()){
            verificationData.value?.step11?.contactEmail?.ifEmpty { "" }
        }else{
            ""
        }
    }

    private fun checkIssue(){
        viewModelScope.launch(Dispatchers.Default) {
            if(verificationData.value?.step11 != null){
                verificationData.value?.verificationDetail?.map {
                    if(it.fieldIssue != null){
                        when(it.fieldIssue){
                            ErrorResubmitType.STEP1_BUSINESS_LOCATION.err -> {
                                _hasErrBusinessLocation.value = true
                            }
                            ErrorResubmitType.STEP1_BUSINESS_ADDRESS.err -> {
                                _hasErrBusinessAddress.value = true
                            }
                            ErrorResubmitType.STEP1_BUSINESS_NAME.err -> {
                                _hasErrBusinessName.value = true
                            }
                            ErrorResubmitType.STEP1_BUSINESS_TYPE.err -> {
                                _hasErrBusinessType.value = true
                            }
                            ErrorResubmitType.STEP1_CONTACT_EMAIL.err -> {
                                _hasErrEmail.value = true
                            }
                            ErrorResubmitType.STEP1_CONTACT_PHONE.err -> {
                                _hasErrPhone.value = true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getVerificationInfo(businessId: Int) {
        _error.value = ""
        viewModelScope.launch {
            getVerificationInfoUseCase(GetVerificationInfoUseCase.Params(businessId)).collectLatest {
                when (it) {
                    is Result.Success -> {
                        _isLoading.value = false
                        if(it.data != null){
                            verificationData.value = it.data
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

}