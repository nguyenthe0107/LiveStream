package olmo.wellness.android.domain.use_case

import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.domain.repository.ApiIDTypeRepository
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class SetProfileInfoUseCase @Inject constructor(
    private val repository: ApiIDTypeRepository
){
    @JvmInline
    value class Params(val userInfo: ProfileBodyRequest)

    operator fun invoke(queryString: String, isReturn: Boolean, params: Params) =
        repository.updateProfilesFromIdServer(queryString, isReturn, params.userInfo)
}