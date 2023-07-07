package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.model.verification1.step2.Step2Request
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class SubmitVerification1Step2UseCase @Inject constructor(
    private val repository: ApiRepository
) {

    data class Params(
        val businessId: Int,
        val step2Request: Step2Request
    )

    operator fun invoke(params: Params) =
        repository.submitVerification1Step2(params.businessId, params.step2Request)

    fun invokeUpdate(params: Params) = repository.submitUpdateVerification1Step2(params.businessId, params.step2Request)
}