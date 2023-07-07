package olmo.wellness.android.domain.use_case

import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class SetProfileInfoWellnessUseCase @Inject constructor(
    private val repository: ApiRepository
){
    @JvmInline
    value class Params(val userInfo: ProfileBodyRequest)

    operator fun invoke(queryString: String, isReturn: Boolean, params: Params) =
        repository.updateProfiles(queryString, isReturn, params.userInfo)
}