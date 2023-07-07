package olmo.wellness.android.ui.services

import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RemoteMessageObserver private constructor() {

    companion object : SingletonProvider<RemoteMessageObserver>({ RemoteMessageObserver() })

    private val _result = MutableStateFlow<RemoteMessage?>(null)

    val result: StateFlow<RemoteMessage?>
        get() = _result.asStateFlow()

    fun observe(message: RemoteMessage?) {
        _result.update { message }
    }
}