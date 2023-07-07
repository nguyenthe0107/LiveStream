package olmo.wellness.android.ui.livestream.stream.end_livestream

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.domain.use_case.LivestreamUseCase
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class SummaryLiveStreamViewModel @Inject constructor(
    application: Application,
    private val livestreamUseCase: LivestreamUseCase,
) : BaseViewModel(application) {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun setLoading(status: Boolean) {
        _isLoading.value = status
    }

    private val _liveStreamInfo : MutableStateFlow<LivestreamInfo?> = MutableStateFlow(null)
    val liveStreamInfo : StateFlow<LivestreamInfo?> = _liveStreamInfo

    fun loadDefaultInfo(livestreamInfoInput: LivestreamInfo?){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _liveStreamInfo.update {
                livestreamInfoInput
            }
            _isLoading.value = false
        }
    }

    fun discardVideo(){
        viewModelScope.launch(Dispatchers.IO) {
            sharedPrefs.clearTimeCreatedLive()
        }
    }

    fun saveVideo(){
        viewModelScope.launch(Dispatchers.IO) {
            sharedPrefs.clearTimeCreatedLive()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
