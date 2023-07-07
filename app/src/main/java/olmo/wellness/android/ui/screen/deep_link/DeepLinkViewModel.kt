package olmo.wellness.android.ui.screen.deep_link

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.business.StoreOwner
import olmo.wellness.android.domain.use_case.GetBusinessOwnedUseCase
import javax.inject.Inject

@HiltViewModel
class DeepLinkViewModel @Inject constructor(
    application: Application,
    private val getBusinessOwnedUseCase: GetBusinessOwnedUseCase
) : BaseViewModel(application) {

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun setLoading(inputData: Boolean){
        _isLoading.value = inputData
    }

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _businessOwn = MutableStateFlow<StoreOwner?>(null)
    val businessOwn: StateFlow<StoreOwner?> = _businessOwn

    private val _businessId = MutableStateFlow<Int?>(null)
    val businessId: StateFlow<Int?> = _businessId
    fun setBusinessId(id: Int?) {
        if(id != null){
            _businessId.value = id
        }
    }

    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep

    init {
        getBusinessOwnedList()
    }

    private fun getBusinessOwnedList() {
        viewModelScope.launch(Dispatchers.Default) {
            getBusinessOwnedUseCase.getStoreMain().collect{
                when (it) {
                    is Result.Success -> {
                        if(_businessId.value != null){
                            _businessOwn.value = it.data?.store
                            _isSuccess.value = true
                            _isLoading.value = false
                        }else{
                            _error.value = true
                            _isLoading.value = false
                        }
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        it.message?.let { errorMessage ->
                            _error.value = true
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