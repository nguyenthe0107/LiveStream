package olmo.wellness.android.domain.use_case

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.user_setting.UserSetting
import olmo.wellness.android.domain.model.user_setting.UserSettingModifiedResponse
import olmo.wellness.android.domain.model.user_setting.UserSettingRequest
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject
import javax.inject.Provider

class UserSettingsUseCase @Inject constructor(
    private val repository: Provider<ApiRepository>){

    operator fun invoke(userId: Int, queryString: String) : Flow<Result<List<UserSetting>>> {
        return repository.get().getUserSetting(userId, queryString)
    }

    fun updateUserSetting(queries: String,isReturn: Boolean, updateBody: UserSettingRequest) : Flow<Result<List<UserSetting>>>{
        return repository.get().updateUserSetting(queries,isReturn, updateBody)
    }
}