package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class GetProductCategoriesFromServiceUseCase @Inject constructor(
    private val repository: ApiRepository
) {

    @JvmInline
    value class Params(val page: Int)

    operator fun invoke(params: Params) = repository.getProductCategories(params.page)
}