package olmo.wellness.android.ui.screen.follower_following_list

import android.app.Application
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.domain.model.user_follow.UserFollowInfo
import javax.inject.Inject

@HiltViewModel
class ListFollowerViewModel @Inject constructor(application: Application) : BaseViewModel(application = application){

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _listUserData = MutableStateFlow<List<UserFollowInfo>>(emptyList())
    val listUserData : StateFlow<List<UserFollowInfo>> = _listUserData

    fun bindData(listData: List<UserFollowInfo>){
        if(listData.isNotEmpty()){
            _listUserData.update {
                listData
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}

