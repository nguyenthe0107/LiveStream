package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiIDTypeRepository
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject
import javax.inject.Provider

class GetSessionUseCase @Inject constructor(
    private val repository: Provider<ApiIDTypeRepository>
) {

    @JvmInline
    value class Params(val refreshToken: String)

    suspend operator fun invoke(params: Params) = repository.get().getSession(params.refreshToken)
}