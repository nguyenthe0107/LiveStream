package olmo.wellness.android.ui.screen.drawer_layout

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import javax.inject.Inject

@HiltViewModel
class ScheduleSlideViewModel @Inject constructor(application: Application) : BaseViewModel(application) {

    private val _livestreamInfo : MutableStateFlow<LivestreamInfo?> = MutableStateFlow<LivestreamInfo?>(null)
    val livestreamInfo : StateFlow<LivestreamInfo?> = _livestreamInfo

    private val _isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    init {

    }

}