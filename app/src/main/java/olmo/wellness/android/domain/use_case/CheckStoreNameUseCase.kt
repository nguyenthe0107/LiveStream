package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class CheckStoreNameUseCase @Inject constructor(
    private val repository: ApiRepository
) {

    @JvmInline
    value class Params(val storeName: String)

    operator fun invoke(params: Params) =
        repository.checkStoreName(params.storeName)
}