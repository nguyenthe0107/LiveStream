package olmo.wellness.android.ui.screen.user_type_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import olmo.wellness.android.core.enums.UserRole
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.UserInfo
import olmo.wellness.android.domain.use_case.GetUserInfoUseCase
import olmo.wellness.android.domain.use_case.SetUserInfoUseCase
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class UserTypeScreenViewModel @Inject constructor(
    private val setUserInfoUseCase: SetUserInfoUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {

    private val _userRole : MutableStateFlow<UserRole> = MutableStateFlow(UserRole.BUYER)
    val userRole : StateFlow<UserRole> = _userRole

    fun onUserRoleClick(userRole: UserRole) {
        viewModelScope.launch {
            setUserInfoUseCase(SetUserInfoUseCase.Params(UserInfo(userType = userRole.role)))
            sharedPrefs.setUserRole(userRole.role)
        }
    }

    fun updateRole(userRole: UserRole) {
        _userRole.value = userRole
    }

    fun getUserType(){
        viewModelScope.launch {
            if(sharedPrefs.getUserRoleLocal()?.isNotEmpty() == true){
                val userRole = UserTypeModel(sharedPrefs.getUserRoleLocal() ?: UserTypeModel.BUYER.value)
                _userRole.value = UserRole.valueOf(userRole.name)
            }
        }
    }

}