package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiUploadRepository
import javax.inject.Inject

class GetUploadIdServerUrlInfoUseCase @Inject constructor(
    private val repository: ApiUploadRepository
) {
    @JvmInline
    value class Params(val mimType: String)

    operator fun invoke(params: Params) = repository.getUploadUrlInfo(params.mimType)
}