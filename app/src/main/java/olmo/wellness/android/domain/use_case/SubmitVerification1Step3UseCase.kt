package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.model.verification1.step3.Step3Request
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class SubmitVerification1Step3UseCase @Inject constructor(
    private val repository: ApiRepository
) {

    data class Params(
        val businessId: Int,
        val step3Request: Step3Request
    )

    operator fun invoke(params: Params) =
        repository.submitVerification1Step3(params.businessId, params.step3Request)

    fun invokeUpdate(params: Params) =
        repository.submitUpdateVerification1Step3(params.businessId, params.step3Request)
}