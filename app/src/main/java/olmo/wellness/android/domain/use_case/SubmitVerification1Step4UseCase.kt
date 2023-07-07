package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.model.verification1.step4.Step4Request
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class SubmitVerification1Step4UseCase @Inject constructor(
    private val repository: ApiRepository
) {

    data class Params(
        val businessId: Int,
        val step4Request: Step4Request
    )

    operator fun invoke(params: Params) =
        repository.submitVerification1Step4(params.businessId, params.step4Request)

    fun invokeUpdate(params: Params) =
        repository.submitUpdateVerification1Step4(params.businessId, params.step4Request)
}