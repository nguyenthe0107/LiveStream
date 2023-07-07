package olmo.wellness.android.ui.screen.account_setting.block_contacts.list_blocked_contacts

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.data.model.chat.room.User

class ListBlockContactsViewModel(application: Application) : BaseViewModel(application) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = MutableStateFlow(false)

    private val listDemo = listOf(
        User.TEST1,
        User.TEST2
    )

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList :StateFlow<List<User>> = _userList

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

    fun updateUserList(userId: Int){
        viewModelScope.launch {
            _isLoading.update {
                true
            }
            val user = User(id = userId,
                avatar = "https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
                fullName = "user them ")
            _userList.update {
                listDemo.plus(user)
            }
            _isLoading.update {
                false
            }
        }
    }

}
