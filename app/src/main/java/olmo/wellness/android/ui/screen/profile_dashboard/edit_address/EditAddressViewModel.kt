package olmo.wellness.android.ui.screen.profile_dashboard.edit_address

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.profile.AddressProfileRequest
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.verification1.step1.Address
import olmo.wellness.android.domain.model.verification1.step1.BusinessAddressRequest
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class EditAddressViewModel @Inject constructor(application: Application,
                                               private val getCountryListUseCase: GetCountryListUseCase,
                                               private val businessAddressUseCase: BusinessAddressUseCase,
                                               private val getBusinessOwnedUseCase: GetBusinessOwnedUseCase,
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

    private val _isValidate = MutableStateFlow(false)
    val isValidate: StateFlow<Boolean> = _isValidate

    private val _countryList = MutableStateFlow(listOf<Country>())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _businessAddress = MutableStateFlow(Address())
    val businessAddress : StateFlow<Address> = _businessAddress

    private val _businessId = MutableStateFlow<Int?>(null)
    val businessId: StateFlow<Int?> = _businessId

    init {
        reloadApi()
    }

    fun reloadApi(){
        getAddressLocal()
        getCountryList()
        getAddressInfoFromApi()
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

    private fun getAddressLocal(){
        viewModelScope.launch(Dispatchers.Default) {
            if(sharedPrefs.getAddressDetail().id != null){
                _businessAddress.value = sharedPrefs.getAddressDetail()
            }
        }
    }

    /* get Business Address */
    private fun getAddressInfoFromApi() {
        _error.value = ""
        viewModelScope.launch {
            val userLocal = sharedPrefs.getUserInfoLocal()
            val addressDetail = sharedPrefs.getAddressDetail()
            if(userLocal.userTypeModel == UserTypeModel.BUYER) {
                _businessId.value = userLocal.userId
                userLocal.userId?.let {
                    businessAddressUseCase.getBuyerAddress(it).collectLatest {
                        when (it) {
                            is Result.Success -> {
                                _isLoading.value = false
                                if(it.data?.isNotEmpty() == true){
                                    val lastInfo = it.data.last()
                                    _businessAddress.value = lastInfo.copy(
                                        city = lastInfo.city?.ifEmpty { addressDetail.city }
                                    )
                                    saveLocal(lastInfo.copy(timeStamp = System.currentTimeMillis()))
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
            }else{
                getBusinessOwnedUseCase.getStoreMain().collectLatest {
                    when (it) {
                        is Result.Success -> {
                            _isLoading.value = false
                            val business = it.data?.store
                            business?.let { businessOwn ->
                                _businessId.value = businessOwn.id
                                val addressLocal = Address().copy(
                                    id = businessOwn.id,
                                    address = businessOwn.address,
                                    cityId = businessOwn.cityId,
                                    countryId = businessOwn.countryId,
                                    zipCode = businessOwn.zipCode)
                                getBusinessAddress(addressLocal)
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
    }

    private fun getBusinessAddress(address: Address){
        viewModelScope.launch(Dispatchers.Default) {
            val userLocal = sharedPrefs.getUserInfoLocal()
            if(userLocal.userTypeModel == UserTypeModel.BUYER) {
                _businessId.value = userLocal.userId
                return@launch
            }
            val addressDetail = sharedPrefs.getAddressDetail()
            _businessAddress.value = address.copy(
                city = addressDetail.city
            )
            saveLocal(address.copy(timeStamp = System.currentTimeMillis(), city = addressDetail.city))
        }
    }

    private fun saveLocal(address: Address){
        viewModelScope.launch(Dispatchers.IO) {
            sharedPrefs.setAddressDetail(address)
        }
    }

    private val _businessAddressCountry = MutableStateFlow(192)
    private val businessAddressCountry : StateFlow<Int?> = _businessAddressCountry
    fun setBusinessAddressCountry(value: Int?) {
        if (value != null) {
            _businessAddressCountry.value = value
        }
    }

    private val businessAddressPostalCode = MutableStateFlow("")
    fun setBusinessAddressPostalCode(value: String) {
        businessAddressPostalCode.value = value
    }

    private val _businessAddressCity = MutableStateFlow("")
    private val businessAddressCity : StateFlow<String> = _businessAddressCity
    fun setBusinessAddressCity(value: String) {
        if(value.isNotEmpty()){
            _businessAddressCity.value = value
        }else{
            if(sharedPrefs.getAddressDetail().address != null){
                if(sharedPrefs.getAddressDetail().city?.isNotEmpty() == true){
                    _businessAddressCity.value = sharedPrefs.getAddressDetail().city ?: ""
                }
            }
        }
    }

    private val _businessAddressLine = MutableStateFlow("")
    private val businessAddressLine : StateFlow<String> = _businessAddressLine
    fun setBusinessAddressLine1(value: String) {
        if(value.isNotEmpty()){
            _businessAddressLine.value = value
        }else{
            if(sharedPrefs.getAddressDetail().address != null){
                _businessAddressLine.value = sharedPrefs.getAddressDetail().address ?: ""
            }
        }
    }

    fun getCountryName() : String{
        var value = ""
        if(countryList.value.isNotEmpty()){
            value = countryList.value.firstOrNull{ it.id == businessAddress.value.countryId }?.name ?: value
        }
        return value
    }

    private fun getCountryId() : Int {
        return businessAddressCountry.value ?: 192
    }

    fun updateAddress(){
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            val userLocal = sharedPrefs.getUserInfoLocal()
            val addressLocal = sharedPrefs.getAddressDetail()
            if(userLocal.userTypeModel == UserTypeModel.BUYER) {
                val userId = userLocal.userId
                val listUserId = listOf(userId)
                val queryUpdate = Gson().toJson(AddressProfileRequest(userId = listUserId, id = null))
                val address = Address().copy(
                    businessId = null,
                    countryId = getCountryId(),
                    zipCode = businessAddressPostalCode.value.ifEmpty { null },
                    address = businessAddressLine.value,
                    districtId = getCountryId(),
                    isDefaultAddress = true,
                    isPickupAddress = false,
                    isReturnAddress = true,
                    label = "HOME",
                    phoneCountryId = getCountryId(),
                    fullName = userLocal.name
                )
                val updateBody = BusinessAddressRequest(update = address)
                if(address.countryId != null && userId != null){
                    businessAddressUseCase.getBuyerAddress(userId).collectLatest { result ->
                        when(result){
                            is Result.Success -> {
                                if(result.data?.isNullOrEmpty() == true){
                                    businessAddressUseCase.postBuyerAddress(queryUpdate, true, address).collectLatest {
                                        when (it) {
                                            is Result.Loading -> {
                                                _isLoading.value = true
                                            }
                                            is Result.Success -> {
                                                _isLoading.value = false
                                                _isSuccess.value = true
                                                if(businessAddressCity.value.isNotEmpty() || businessAddressLine.value.isNotEmpty()){
                                                    saveLocal(address.copy(
                                                        id=userId,
                                                        city = businessAddressCity.value.ifEmpty { addressLocal.city },
                                                        address = businessAddressLine.value.ifEmpty { addressLocal.address },
                                                        timeStamp = System.currentTimeMillis()))
                                                }
                                            }
                                            is Result.Error -> {
                                                _isLoading.value = false
                                                it.message?.let { errorMessage ->
                                                    _error.value = errorMessage
                                                }
                                                _isSuccess.value = true
                                                if(businessAddressCity.value.isNotEmpty() || businessAddressLine.value.isNotEmpty()){
                                                    saveLocal(address.copy(id=userId,
                                                        city = businessAddressCity.value.ifEmpty { addressLocal.city },
                                                        address = businessAddressLine.value.ifEmpty { addressLocal.address },
                                                        timeStamp = System.currentTimeMillis()))
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    businessAddressUseCase.updateBuyerAddress(queryUpdate, true, updateBody).collectLatest {
                                        when (it) {
                                            is Result.Loading -> {
                                                _isLoading.value = true
                                            }
                                            is Result.Success -> {
                                                _isLoading.value = false
                                                _isSuccess.value = true
                                                if(businessAddressCity.value.isNotEmpty() || businessAddressLine.value.isNotEmpty()){
                                                    saveLocal(address.copy(
                                                        id=userId,
                                                        city = businessAddressCity.value.ifEmpty { addressLocal.city },
                                                        address = businessAddressLine.value.ifEmpty { addressLocal.address },
                                                        timeStamp = System.currentTimeMillis()))
                                                }
                                            }
                                            is Result.Error -> {
                                                _isLoading.value = false
                                                it.message?.let { errorMessage ->
                                                    _error.value = errorMessage
                                                }
                                                _isSuccess.value = true
                                                if(businessAddressCity.value.isNotEmpty() || businessAddressLine.value.isNotEmpty()){
                                                    saveLocal(address.copy(
                                                        id=userId,
                                                        city = businessAddressCity.value.ifEmpty { addressLocal.city },
                                                        address = businessAddressLine.value.ifEmpty { addressLocal.address },
                                                        timeStamp = System.currentTimeMillis()))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else -> {
                                businessAddressUseCase.updateBuyerAddress(queryUpdate, true, updateBody).collectLatest {
                                    when (it) {
                                        is Result.Loading -> {
                                            _isLoading.value = true
                                        }
                                        is Result.Success -> {
                                            _isLoading.value = false
                                            _isSuccess.value = true
                                            if(businessAddressCity.value.isNotEmpty() || businessAddressLine.value.isNotEmpty()){
                                                saveLocal(address.copy(
                                                    id = userId,
                                                    city = businessAddressCity.value.ifEmpty{addressLocal.city},
                                                    address = businessAddressLine.value.ifEmpty { addressLocal.address },
                                                    timeStamp = System.currentTimeMillis()))
                                            }
                                        }
                                        is Result.Error -> {
                                            _isLoading.value = false
                                            it.message?.let { errorMessage ->
                                                _error.value = errorMessage
                                            }
                                            _isSuccess.value = true
                                            if(businessAddressCity.value.isNotEmpty() || businessAddressLine.value.isNotEmpty()){
                                                saveLocal(address.copy(
                                                    id=userId,
                                                    city = businessAddressCity.value.ifEmpty { addressLocal.city },
                                                    address = businessAddressLine.value.ifEmpty { addressLocal.address },
                                                    timeStamp = System.currentTimeMillis()))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                val businessId = businessId.value
                val listUserId = listOf(businessId)
                val query = "{\"id\":$listUserId}"
                val address = Address().copy(
                    businessId = null,
                    countryId = getCountryId(),
                    zipCode = businessAddressPostalCode.value.ifEmpty { null },
                    address = businessAddressLine.value
                )
                val updateBody = BusinessAddressRequest(update = address)
                if(address.countryId != null){
                    businessAddressUseCase.updateBusinessAddress(query, true, updateBody).collectLatest {
                        when (it) {
                            is Result.Loading -> {
                                _isLoading.value = true
                            }
                            is Result.Success -> {
                                _isLoading.value = false
                                _isSuccess.value = true
                                if(businessAddressCity.value.isNotEmpty() || businessAddressLine.value.isNotEmpty()){
                                    saveLocal(address.copy(id=businessId,
                                        district = businessAddressCity.value,
                                        address = businessAddressLine.value.ifEmpty { addressLocal.address },
                                        city = businessAddressCity.value.ifEmpty { addressLocal.city },
                                        timeStamp = System.currentTimeMillis()))
                                }
                            }
                            is Result.Error -> {
                                _isLoading.value = false
                                it.message?.let { errorMessage ->
                                    _error.value = errorMessage
                                }
                                _isSuccess.value = true
                                if(businessAddressCity.value.isNotEmpty() || businessAddressLine.value.isNotEmpty()){
                                    saveLocal(address.copy(id=businessId,
                                        district = businessAddressCity.value,
                                        address = businessAddressLine.value.ifEmpty { addressLocal.address },
                                        city = businessAddressCity.value.ifEmpty { addressLocal.city },
                                        timeStamp = System.currentTimeMillis()))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}