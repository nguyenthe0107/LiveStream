package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.LocalRepository
import javax.inject.Inject

class GetProductCategoriesFromLocalUseCase @Inject constructor(
    private val repository: LocalRepository
) {

    suspend operator fun invoke() = repository.getSubCategories()
}