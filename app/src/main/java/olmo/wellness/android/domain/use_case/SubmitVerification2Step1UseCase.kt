package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.model.verification2.V2Step1Request
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class SubmitVerification2Step1UseCase @Inject constructor(
    private val repository: ApiRepository
) {

    data class Params(
        val businessId: Int,
        val step1Request: V2Step1Request
    )

    operator fun invoke(params: Params) =
        repository.submitVerification2Step1(params.businessId, params.step1Request)
}