package olmo.wellness.android.domain.use_case

import olmo.wellness.android.data.model.business.StoreBusinessRequest
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class SetServiceBusinessInfoUseCase @Inject constructor(
    private val repository: ApiRepository
){
    @JvmInline
    value class Params(val userInfo:StoreBusinessRequest)

    suspend operator fun invoke(queryString: String,isReturn: Boolean,params: Params) = repository.updateStoreBusinessOwned(queryString, isReturn,params.userInfo)
}