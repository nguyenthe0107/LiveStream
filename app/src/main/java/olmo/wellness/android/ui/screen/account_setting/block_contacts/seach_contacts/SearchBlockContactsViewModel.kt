package olmo.wellness.android.ui.screen.account_setting.block_contacts.seach_contacts

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.data.model.chat.room.User

class SearchBlockContactsViewModel(application: Application) : BaseViewModel(application) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = MutableStateFlow(false)

    private val listDemo = listOf(
        User.TEST,
        User.TEST,
        User.TEST,
        User.TEST,
        User.TEST,
        User.TEST,
        User.TEST,
        User.TEST,
        User.TEST,
        User.TEST
    )

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList :StateFlow<List<User>> = _userList

    private val _userSelected = MutableStateFlow<User?>(null)
    val userSelected :StateFlow<User?> = _userSelected

    init {
        getUserList()
    }

    private fun getUserList(){
        viewModelScope.launch {
            _isLoading.update {
                true
            }
            _userList.update {
                listDemo
            }
            _isLoading.update {
                false
            }
        }
    }

    fun blockUser(user: User){
        viewModelScope.launch(Dispatchers.Main) {
            _userSelected.update {
                user
            }
        }
    }

}
