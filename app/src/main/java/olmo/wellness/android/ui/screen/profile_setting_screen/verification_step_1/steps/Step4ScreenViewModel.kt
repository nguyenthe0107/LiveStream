package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.steps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Constants.UNDERFINE_NUMBER
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.bank.BankInfo
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.defination.re_submit.ErrorResubmitType
import olmo.wellness.android.domain.model.verification1.response.Ver1Step4Data
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.domain.model.verification1.step4.Step4Request
import olmo.wellness.android.domain.use_case.GetBankListUseCase
import olmo.wellness.android.domain.use_case.GetVerificationInfoUseCase
import olmo.wellness.android.domain.use_case.SubmitVerification1Step4UseCase
import olmo.wellness.android.domain.use_case.SubmitVerification1Step4UseCase.Params
import javax.inject.Inject

@HiltViewModel
class Step4ScreenViewModel @Inject constructor(
    private val submitVerification1Step4UseCase: SubmitVerification1Step4UseCase,
    private val getBankListUseCase: GetBankListUseCase,
    private val getVerificationInfoUseCase: GetVerificationInfoUseCase
) : ViewModel() {

    private var step4Data: Step4Request? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    fun setIsSuccess(status: Boolean){
        _isSuccess.value = status
    }

    private val _isValidate = MutableStateFlow(false)
    val isValidate: StateFlow<Boolean> = _isValidate

    private var lastCountryIdRequestListBank = -1

    private val verificationData = MutableStateFlow<VerificationData?>(null)
    fun setVerificationData(data : VerificationData?) {
        if (verificationData.value == null){
            verificationData.value = data
            if(verificationData.value != null){
                checkIssue()
                getBankList(verificationData.value?.step14?.countryId?: UNDERFINE_NUMBER)
                getBankName()
            }
        }
    }

    private val businessId = MutableStateFlow<Int?>(null)
    fun setBusinessId(value: Int?) {
        businessId.value = value
    }

    private val _bankInfoList = MutableStateFlow<List<BankInfo>>(emptyList())
    val bankInfoList: StateFlow<List<BankInfo>> = _bankInfoList

    private val countryList = MutableStateFlow<List<Country>>(emptyList())
    fun setCountryList(list: List<Country>) {
        countryList.value = list
    }

    private val countryBank = MutableStateFlow("")
    fun setCountryBank(name: String) {
        countryBank.value = name
        _isValidate.value = isValidateData()
        val countryId = countryList.value.firstOrNull() { it.name == countryBank.value }?.id ?: UNDERFINE_NUMBER
        getBankList(countryId)
    }

    private val bankName = MutableStateFlow("")
    fun setBankName(name: String) {
        bankName.value = name
        _bankNameTemp.value = name
        _isValidate.value = isValidateData()
    }

    private val holderName = MutableStateFlow("")
    fun setHolderName(name: String) {
        holderName.value = name
        _isValidate.value = isValidateData()
    }

    private val accountNumber = MutableStateFlow("")
    fun setAccountNumber(number: String) {
        accountNumber.value = number
        _isValidate.value = isValidateData()
    }

    private val bankBranch = MutableStateFlow("")
    fun setBankBranch(name: String) {
        bankBranch.value = name
        _isValidate.value = isValidateData()
    }

    private val _bankNameTemp = MutableStateFlow("")
    val bankNameTemp : StateFlow<String> = _bankNameTemp

    private val papalAccessToken = MutableStateFlow("")
    fun setPapalAccessToken(name: String) {
        papalAccessToken.value = name
        //_isValidate.value = isValidateData()
    }

    /* Mapping Error with filedName Or FiledIssue */
    /* Define Issue */
    private val _hasErrBankName = MutableStateFlow(false)
    val hasErrBankName : StateFlow<Boolean> = _hasErrBankName

    private val _hasErrBankCountry = MutableStateFlow(false)
    val hasErrBankCountry : StateFlow<Boolean> = _hasErrBankCountry

    private val _hasErrHolderName = MutableStateFlow(false)
    val hasErrHolderName : StateFlow<Boolean> = _hasErrHolderName

    private val _hasErrAccountNumber = MutableStateFlow(false)
    val hasErrAccountNumber : StateFlow<Boolean> = _hasErrAccountNumber

    private val _hasErrBankBranch = MutableStateFlow(false)
    val hasErrBankBranch : StateFlow<Boolean> = _hasErrBankBranch

    private fun isValidateData(): Boolean {

        if(isResubmit()){
            return true
        }

        if (countryBank.value.isEmpty())
            return false

        if (bankName.value.isEmpty())
            return false

        if (holderName.value.isEmpty())
            return false

        if (accountNumber.value.isEmpty())
            return false

        if (bankBranch.value.isEmpty())
            return false

        return true
    }

    private fun getBankList(countryId : Int) {
        if (countryId == UNDERFINE_NUMBER)
            return

        if (lastCountryIdRequestListBank == countryId && _bankInfoList.value.isNotEmpty())
            return

        viewModelScope.launch {
            getBankListUseCase(GetBankListUseCase.Params(listOf(countryId))).collectLatest {
                when (it) {
                    is Result.Success -> {
                        it.data?.let { bankList ->
                            // TODO
                            //_bankInfoList.value = emptyList()
                            //_bankInfoList.value = bankList
                            lastCountryIdRequestListBank = countryId
                        }
                        _error.value = ""
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

    private fun getRequest() =
        Step4Request(
            bankId = if(bankInfoList.value.isNullOrEmpty()){
                null
            }else{
                bankInfoList.value.firstOrNull(){ it.bankName == bankName.value }?.id
            },
            countryId = if(countryList.value.isNullOrEmpty()){
                null
            }else{
                countryList.value.firstOrNull() { it.name == countryBank.value }?.id
            },
            holderName = holderName.value.ifEmpty { null },
            accountNumber = accountNumber.value.ifEmpty { null },
            bankBranch = bankBranch.value.ifEmpty { null },
            paypalAccessToken = papalAccessToken.value.ifEmpty { null }
        ).apply {
            step4Data = this
        }

    fun submitStep4() {
        viewModelScope.launch {
            if((verificationData.value?.step14 != null) || (_isSuccess.value)){
                businessId.value?.let {
                    submitVerification1Step4UseCase.invokeUpdate(Params(it, getRequest())).collectLatest {
                        handleResultApi(it)
                    }
                }
            }else{
                businessId.value?.let {
                    submitVerification1Step4UseCase(Params(it, getRequest())).collectLatest {
                        handleResultForPosting(it)
                    }
                }
            }
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

    fun isResubmit() : Boolean{
        return if((_isSuccess.value) || (verificationData.value?.step12 != null)){
            _isValidate.value = true
            true
        }else{
            _isValidate.value = false
            false
        }
    }

    /* Get Validate Information */

    private fun isStep13Null() : Boolean{
        if(verificationData.value == null || verificationData.value?.step14 == null){
            return true
        }
        return false
    }

    private fun getVerifyCationData() : Ver1Step4Data? {
        return verificationData.value?.step14
    }

    fun getCountry(): String?{
        val value = ""
        if(isStep13Null() || countryList.value.isEmpty()){
            return value
        }
        return countryList.value.firstOrNull() { it.id == getVerifyCationData()?.countryId }?.name ?: value
    }

    private fun getBankName() {
        if(isStep13Null() || getVerifyCationData()?.countryId == null){
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(getVerifyCationData()?.countryId != null){
                val deferred = async { getBankListUseCase(GetBankListUseCase.Params(listOf(getVerifyCationData()?.countryId!!))) }
                val listBank = deferred.await()
                listBank.collectLatest {
                    when (it) {
                        is Result.Success -> {
                            it.data?.let { bankList ->
                                if(bankList?.isNotEmpty() == true){
                                    val bankName = bankList.firstOrNull{ it.id == getVerifyCationData()?.bankId }?.bankName
                                    if(bankName?.isNotEmpty() == true){
                                        _bankNameTemp.value = bankName
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getHolderName(): String?{
        return getVerifyCationData()?.holderName
    }

    fun getBankAccountNumber(): String?{
        return getVerifyCationData()?.accountNumber
    }

    fun getBankBranch(): String?{
        return getVerifyCationData()?.bankBranch
    }

    private fun checkIssue(){
        viewModelScope.launch(Dispatchers.Default) {
            if(verificationData.value?.step14 != null){
                verificationData.value?.verificationDetail?.map {
                    if(it.fieldIssue != null){
                        when(it.fieldIssue){
                            ErrorResubmitType.STEP4_ACCOUNT_NUMBER.err -> {
                                _hasErrAccountNumber.value = true
                            }
                            ErrorResubmitType.STEP4_BANK_BRANCH.err -> {
                                _hasErrBankBranch.value = true
                            }
                            ErrorResubmitType.STEP4_BANK_NAME.err -> {
                                _hasErrBankName.value = true
                            }
                            ErrorResubmitType.STEP4_HOLDER_NAME.err -> {
                                _hasErrHolderName.value = true
                            }
                            ErrorResubmitType.STEP4_BANK_COUNTRY.err -> {
                                _hasErrBankCountry.value = true
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
                        if(it.data != null){
                            verificationData.value = it.data
                            clearFiled()
                        }
                        _isSuccess.value = true
                        _isLoading.value = false
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

    private fun clearFiled() {
        isResubmit()
    }

}