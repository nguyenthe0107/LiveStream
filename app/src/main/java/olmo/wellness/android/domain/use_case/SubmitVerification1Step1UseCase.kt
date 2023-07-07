package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.model.verification1.step1.Step1Request
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class SubmitVerification1Step1UseCase @Inject constructor(
    private val repository: ApiRepository
) {

    @JvmInline
    value class Params(
        val step1Request: Step1Request
    )

    operator fun invoke(params: Params) = repository.submitVerification1Step1(params.step1Request)

    fun invokeUpdate(businessId : Int,params: Params) = repository.submitUpdateVerification1Step1(businessId, params.step1Request)
}