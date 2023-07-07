package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class GetBankListUseCase @Inject constructor(
    private val repository: ApiRepository
) {

    @JvmInline
    value class Params(val countryId: List<Int>)

    operator fun invoke(params: Params) = repository.getBankList(params.countryId)
}