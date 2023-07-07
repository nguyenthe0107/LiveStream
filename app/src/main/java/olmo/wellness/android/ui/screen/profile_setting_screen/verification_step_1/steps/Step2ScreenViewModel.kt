package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.steps

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.core.enums.IdentityType
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.defination.WrapAddress
import olmo.wellness.android.domain.model.defination.re_submit.ErrorResubmitType
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.domain.model.verification1.step1.ContactPhone
import olmo.wellness.android.domain.model.verification1.step2.AddressDetail
import olmo.wellness.android.domain.model.verification1.step2.Identity
import olmo.wellness.android.domain.model.verification1.step2.SellerInfo
import olmo.wellness.android.domain.model.verification1.step2.Step2Request
import olmo.wellness.android.domain.use_case.GetVerificationInfoUseCase
import olmo.wellness.android.domain.use_case.SubmitVerification1Step2UseCase
import olmo.wellness.android.domain.use_case.SubmitVerification1Step2UseCase.Params
import olmo.wellness.android.ui.common.validate.phoneFormatWithoutSpecialCharacter
import javax.inject.Inject

@HiltViewModel
class Step2ScreenViewModel @Inject constructor(
    private val submitVerification1Step2UseCase: SubmitVerification1Step2UseCase,
    private val getVerificationInfoUseCase: GetVerificationInfoUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    fun setIsSuccess(status: Boolean){
        _isSuccess.value = status
    }

    private val verificationData = MutableStateFlow<VerificationData?>(null)
    fun setVerificationData(data : VerificationData?) {
        if(data?.step12 != null){
            verificationData.value = data
            if(verificationData.value != null){
                checkIssue()
            }
        }
    }

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

    private val businessId = MutableStateFlow<Int?>(null)
    fun setBusinessId(value: Int?) {
        businessId.value = value
    }

    private val countryList = MutableStateFlow<List<Country>>(emptyList())
    fun setCountryList(list: List<Country>) {
        countryList.value = list
    }

    private val countryOfCitizen = MutableStateFlow("")
    fun setCountryOfCitizen(name: String) {
        countryOfCitizen.value = name
        _isValidate.value = isValidateData()
    }

    private val dateOfBirth = MutableStateFlow("")
    fun setDateOfBirth(date: String) {
        dateOfBirth.value = date
        _isValidate.value = isValidateData()
    }

    private val identityNumber = MutableStateFlow("")
    fun setIdentityNumber(number: String) {
        identityNumber.value = number
        _isValidate.value = isValidateData()
    }

    private val identityCountryOfIssue = MutableStateFlow("")
    fun setIdentityCountryOfIssue(name: String) {
        identityCountryOfIssue.value = name
        _isValidate.value = isValidateData()
    }

    private val identityDateOfExpiration = MutableStateFlow("")
    fun setIdentityDateOfExpiration(date: String) {
        identityDateOfExpiration.value = date
        _isValidate.value = isValidateData()
    }

    private val _identityDocumentUris = MutableStateFlow<List<String>>(emptyList())
    val identityDocumentUris: StateFlow<List<String>> = _identityDocumentUris
    fun setIdentityDocumentUris(list: List<String>) {
        _identityDocumentUris.value = list
        _isValidate.value = isValidateData()
    }

    private val addressList = MutableStateFlow<List<AddressDetail>>(emptyList())
    fun setAddress(address: AddressDetail) {
        val oldList = ArrayList(addressList.value)
        oldList.add(address)
        addressList.value = oldList
        _isValidate.value = isValidateData()
    }

    private val phoneList = MutableStateFlow<List<ContactPhone>>(emptyList())
    fun setPhone(phone: ContactPhone) {
        val oldList = ArrayList(phoneList.value)
        oldList.add(phone)
        oldList.map { contactPhone ->
            contactPhone.copy(countryId = contactPhone.countryId,
                phoneNumber = phoneFormatWithoutSpecialCharacter(contactPhone.phoneNumber)
            )
        }
        phoneList.value = oldList
        _isValidate.value = isValidateData()
    }

    private val validateAddress = MutableStateFlow(false)
    fun setValidateAddress(value: Boolean) {
        validateAddress.value = value
    }

    private val validatePhone = MutableStateFlow(false)
    fun setValidatePhone(value: Boolean) {
        validatePhone.value = value
    }

    /* Mapping Error with filedName Or FiledIssue */
    /* Define Issue */
    private val _hasErrCountryCitizen = MutableStateFlow(false)
    val hasErrCountryCitizen : StateFlow<Boolean> = _hasErrCountryCitizen

    private val _hasErrBirthday = MutableStateFlow(false)
    val hasErrBirthday : StateFlow<Boolean> = _hasErrBirthday

    private val _hasErrTypeDocument = MutableStateFlow(false)
    val hasErrTypeDocument : StateFlow<Boolean> = _hasErrTypeDocument

    private val _hasErrNumberOfId = MutableStateFlow(false)
    val hasErrNumberOfId : StateFlow<Boolean> = _hasErrNumberOfId

    private val _hasErrDateExpire = MutableStateFlow(false)
    val hasErrDateExpire : StateFlow<Boolean> = _hasErrDateExpire

    private val _hasErrCountryOfIssue = MutableStateFlow(false)
    val hasErrCountryOfIssue : StateFlow<Boolean> = _hasErrCountryOfIssue

    private fun isValidateData(): Boolean {

        if(isResubmit()){
            return true
        }

        if (countryOfCitizen.value.isEmpty())
            return false

        if (dateOfBirth.value.isEmpty())
            return false

        if (_typeIdentity.value.isEmpty())
            return false

        if (identityNumber.value.isEmpty())
            return false

        if (identityCountryOfIssue.value.isEmpty())
            return false

        if (identityDateOfExpiration.value.isEmpty())
            return false

        if (_identityDocumentUris.value.isEmpty())
            return false

        return true
    }

    private fun clearFiled(){
        _identityDocumentUris.value = emptyList()
        isResubmit()
    }

    private fun getRequest() =
        Step2Request(
            info = if(countryOfCitizen.value.isEmpty() && dateOfBirth.value.isEmpty()){
                null
            }else{
                if(getIdentity() != null){
                    val sellerInfo = SellerInfo(
                        countryOfCitizen = if(countryList.value.isNotEmpty()) {
                            if(getSellerInfo()?.countryOfCitizen == countryList.value.firstOrNull(){ it.name == countryOfCitizen.value }?.id){
                                null
                            }else{
                                countryList.value.firstOrNull(){ it.name == countryOfCitizen.value }?.id
                            }
                        }else{ null },
                        dateOfBirth = if(getDateOfBirth().isNotEmpty()){
                            if(getDateOfBirth().isNotEmpty() && getDateOfBirth().trim() == dateOfBirth.value.trim()){
                                null
                            }else{
                                dateOfBirth.value.trim()
                            }
                        }else{
                            dateOfBirth.value.ifEmpty { null }
                        }
                    )
                    if(sellerInfo.countryOfCitizen == null && sellerInfo.dateOfBirth == null){
                        null
                    }else{
                        sellerInfo
                    }
                }else{
                    SellerInfo(
                        countryOfCitizen = if(countryList.value.isNotEmpty()) {
                            countryList.value.firstOrNull(){ it.name == countryOfCitizen.value }?.id
                        }else{ null },
                        dateOfBirth = dateOfBirth.value.ifEmpty { null }
                    )
                }},
            identity = if (_typeIdentity.value.isEmpty() && identityNumber.value.isEmpty() &&
                identityCountryOfIssue.value.isEmpty() && identityDateOfExpiration.value.isEmpty() &&
                _identityDocumentUris.value.isNullOrEmpty()){
                null
            } else {
                if(getIdentity() != null){
                    val identity = Identity(
                        type = if (getIdentity()?.type?.trim() == _typeIdentity.value) {
                            null
                        } else {
                            _typeIdentity.value.ifEmpty { null }
                        },
                        identityNumber = if(getIdentity()?.identityNumber?.trim() == identityNumber.value.trim()){
                            null
                        }else{
                            identityNumber.value.ifEmpty { null }
                        },
                        countryOfIssue = if (getIdentity()?.countryOfIssue ==
                            countryList.value.firstOrNull(){ it.name == identityCountryOfIssue.value }?.id) {
                            null
                        } else {
                            countryList.value.firstOrNull() { it.name == identityCountryOfIssue.value }?.id
                        },
                        dateOfExpiration = if(getIdentity()?.dateOfExpiration == identityDateOfExpiration.value.ifEmpty { null }){
                            null
                        }else{
                            identityDateOfExpiration.value.ifEmpty { null }
                        },
                        documentUris = _identityDocumentUris.value.ifEmpty { null }
                    )
                    if(identity.identityNumber == null &&
                        identity.type == null && identity.countryOfIssue == null &&
                            identity.dateOfExpiration == null &&
                            identity.documentUris == null){
                        null
                    }else{
                        identity
                    }
                }else{
                    Identity(
                        type = _typeIdentity.value.ifEmpty { null },
                        identityNumber = identityNumber.value.ifEmpty { null },
                        countryOfIssue = countryList.value.firstOrNull() { it.name == identityCountryOfIssue.value }?.id,
                        dateOfExpiration = identityDateOfExpiration.value.ifEmpty { null },
                        documentUris = _identityDocumentUris.value.ifEmpty { null }
                    )
                }
            },
            address = addressList.value.ifEmpty { emptyList() },
            phone = phoneList.value.ifEmpty { emptyList() }
        )

    fun submitStep2() {
        viewModelScope.launch(Dispatchers.Default){
            if(verificationData.value?.step12 != null){
                businessId.value?.let {
                    submitVerification1Step2UseCase.invokeUpdate(Params(it, getRequest())).collect {
                        handleResultApi(it)
                    }
                }
            }else{
                businessId.value?.let { businessId ->
                    submitVerification1Step2UseCase(Params(businessId, getRequest())).collect {
                        handleResultApiPost(it)
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

    private fun handleResultApi(it: Result<Boolean>) {
        when (it) {
            is Result.Success -> {
                _isLoading.value = false
                _error.value = ""
                _isSuccess.value = true
            }
            is Result.Error -> {
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

    private fun handleResultApiPost(it: Result<Boolean>) {
        when (it) {
            is Result.Success -> {
                _isLoading.value = false
                _error.value = ""
                _isSuccess.value = true
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
                _isLoading.value = true
            }
        }
    }

    /* Pre Data */
    fun getCountryOfCitizenShip() : String{
        var value = ""
        if(verificationData.value?.step12 == null || verificationData.value?.step12?.info == null || countryList.value.isEmpty()){
            return value
        }
        value = countryList.value.firstOrNull(){ it.id == verificationData.value?.step12?.info?.countryOfCitizen }?.name ?: value
        return value
    }

    fun getDateOfBirth() : String{
        var value = ""
        if(verificationData.value?.step12 == null || verificationData.value?.step12?.info == null){
            return value
        }
        value = verificationData.value?.step12?.info?.dateOfBirth ?: value
        return value
    }

    fun getIdentity() : Identity?{
        if(verificationData.value?.step12 == null || verificationData.value?.step12?.identity == null){
            return null
        }
        return verificationData.value?.step12?.identity
    }

    private fun getSellerInfo(): SellerInfo?{
        if(verificationData.value?.step12 == null || verificationData.value?.step12?.info == null){
            return null
        }
        return verificationData.value?.step12?.info
    }

    fun getIdentityURI() : List<Uri>?{
        if(verificationData.value?.step12 == null || verificationData.value?.step12?.identity == null){
            return null
        }
        return try {
            verificationData.value?.step12?.identity?.documentUris?.map { valueDraw ->
                Uri.parse(valueDraw)
            }
        }catch (ex: Exception){
            emptyList()
        }
    }

    fun getIdentityObject() : IdentityType?{
        val identityList =
            listOf(IdentityType.Passport, IdentityType.IdCard, IdentityType.DriverLicense)
        var value : IdentityType? = null
        if(getIdentity() == null)
            return null
        identityList.forEach { typeInput ->
            if(typeInput.type == getIdentity()?.type){
                value = typeInput
            }
        }
        return value
    }

    fun getDateOfExpiry() : String{
        val value = ""
        if(verificationData.value?.step12 == null || verificationData.value?.step12?.identity == null){
            return value
        }
        return verificationData.value?.step12?.identity?.dateOfExpiration ?: value
    }

    fun getCountryOfIssue() : String?{
        val value = ""
        if(verificationData.value?.step12 == null || verificationData.value?.step12?.identity == null || countryList.value.isEmpty()){
            return value
        }
        return countryList.value.firstOrNull(){ it.id == verificationData.value?.step12?.identity?.countryOfIssue }?.name ?: value
    }

    fun getResidentialAddress() : List<WrapAddress?>{
        if(verificationData.value?.step12 == null || verificationData.value?.step12?.address == null){
            return emptyList()
        }
        var list : List<WrapAddress> = mutableListOf()
        verificationData.value?.step12?.address?.map {
            list = listOf(WrapAddress().copy(addressInfo = it.addressInfo, title = it.addressType))
        }

        return list
    }

    fun getMobileNumber() : List<WrapAddress?>{
        if(verificationData.value?.step12 == null || verificationData.value?.step12?.phone == null){
            return emptyList()
        }
        var list : List<WrapAddress> = mutableListOf()
        verificationData.value?.step12?.phone?.map {
            list = listOf(WrapAddress().copy(addressInfo = it.phoneNumber))
        }
        return list
    }

    private fun checkIssue(){
        viewModelScope.launch(Dispatchers.Default) {
            if(verificationData.value?.step12 != null){
                verificationData.value?.verificationDetail?.map {
                    if(it.fieldIssue != null){
                        when(it.fieldIssue){
                            ErrorResubmitType.STEP2_COUNTRY_CITIZEN.err -> {
                                _hasErrCountryCitizen.value = true
                            }
                            ErrorResubmitType.STEP2_BIRTHDAY.err -> {
                                _hasErrBirthday.value = true
                            }
                            ErrorResubmitType.STEP2_ID_COUNTRY.err -> {
                                _hasErrCountryOfIssue.value = true
                            }
                            ErrorResubmitType.STEP2_ID_EXPIRATION.err -> {
                                _hasErrDateExpire.value = true
                            }
                            ErrorResubmitType.STEP2_ID_NUMBER.err -> {
                                _hasErrNumberOfId.value = true
                            }
                            ErrorResubmitType.STEP2_ID_TYPE.err -> {
                                _hasErrTypeDocument.value = true
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
                            clearFiled()
                        }
                    }
                    else -> {
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    private fun<T> isEqual(first: List<T>, second: List<T>): Boolean {
        if (first.size != second.size) {
            return false
        }
        return first.zip(second).all { (x, y) -> x.toString().trim() == y.toString().trim() }
    }
}