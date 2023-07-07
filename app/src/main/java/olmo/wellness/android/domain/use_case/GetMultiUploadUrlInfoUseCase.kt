package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiUploadRepository
import javax.inject.Inject

class GetMultiUploadUrlInfoUseCase  @Inject constructor(
    private val repository: ApiUploadRepository
) {
    @JvmInline
    value class Params(val mimType: List<String>)

    operator fun invoke(params: Params) = repository.getMultiUploadUrlInfo(params.mimType)
}