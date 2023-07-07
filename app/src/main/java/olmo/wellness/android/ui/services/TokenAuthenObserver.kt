package olmo.wellness.android.ui.services

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TokenAuthenObserver private constructor() {

    companion object : SingletonProvider<TokenAuthenObserver>({ TokenAuthenObserver() })

    private val _result = MutableStateFlow<Boolean?>(null)

    val result: StateFlow<Boolean?>
        get() = _result.asStateFlow()

    fun observe(message: Boolean?) {
        _result.update { message }
    }
}