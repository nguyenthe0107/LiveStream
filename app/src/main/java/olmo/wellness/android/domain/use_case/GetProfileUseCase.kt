package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.repository.ApiIDTypeRepository
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ApiIDTypeRepository){

    @JvmInline
    value class Params(val profileRequest: GetProfileRequest)

    operator fun invoke(params: Params? = null) = repository.getProfileFromIdServer(params?.profileRequest?.userId, params?.profileRequest?.fields.orEmpty())
}