package olmo.wellness.android.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.extension.WTF

private const val SubscriberBufferSize = 64
abstract class BaseViewModel<STATE, EVENT>: ViewModel() {

    private var _uiState = MutableStateFlow(this.initState())
    val uiState : StateFlow<STATE>
        get() = _uiState

    private val _events =
        MutableSharedFlow<Array<out () -> CallBackEvent>>(extraBufferCapacity = SubscriberBufferSize)
    val events: SharedFlow<Array<out () -> CallBackEvent>>
        get() = _events

    fun setState(state: STATE){
        viewModelScope.launch {
            _uiState.update { state }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun triggerStateEvent(event: EVENT){
        GlobalScope.launch(Dispatchers.Main) {
            onTriggeredEvent(event)
        }
    }

    protected fun postEvent(vararg block: () -> CallBackEvent) {
        viewModelScope.launch(Dispatchers.Main) {
            _events.emit(block)
        }
    }

    /**
     * MUST CALL THIS FUNC FOR RELEASING ALL OF DATA
     */
    fun clearState(){
        viewModelScope.launch {
            _uiState.emit(initState())
        }
    }

    abstract fun initState(): STATE
    abstract fun onTriggeredEvent(event: EVENT)
}