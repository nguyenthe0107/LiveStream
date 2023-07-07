package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.LocalRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: LocalRepository
) {

    @JvmInline
    value class Params(val userId: Int?)

    suspend operator fun invoke(params: Params? = null) = repository.getUserInfo(params?.userId)
}