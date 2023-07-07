package olmo.wellness.android.ui.screen.playback_video.donate.payment_process

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class PaymentProcessViewModel @Inject constructor(application: Application) :
    olmo.wellness.android.core.BaseViewModel(application) {

    private val _urlPayment: MutableStateFlow<String> = MutableStateFlow<String>("")
    val urlPayment: StateFlow<String> = _urlPayment

    init {
        getUrl()
    }

    private fun getUrl(){
        if(sharedPrefs.getUrlPaymentMethod().isNotEmpty()){
            _urlPayment.value = sharedPrefs.getUrlPaymentMethod()
        }
    }

}