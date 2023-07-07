package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class GetVerificationInfoUseCase @Inject constructor(
    private val repository: ApiRepository
) {

    @JvmInline
    value class Params(
        val businessId: Int
    )

    operator fun invoke(params: Params) = repository.getVerificationInfo(params.businessId)
}