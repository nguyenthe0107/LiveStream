package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class GetBusinessTypeListUseCase @Inject constructor(
    private val repository: ApiRepository
) {

    operator fun invoke() = repository.getBusinessType()
}