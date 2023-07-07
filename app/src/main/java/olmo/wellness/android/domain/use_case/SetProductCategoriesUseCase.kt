package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.repository.LocalRepository
import javax.inject.Inject

class SetProductCategoriesUseCase @Inject constructor(
    private val repository: LocalRepository
){
    @JvmInline
    value class Params(val subCategories: MutableList<LiveCategory>)

    suspend operator fun invoke(params: Params) = repository.saveSubCategories(params.subCategories)
}