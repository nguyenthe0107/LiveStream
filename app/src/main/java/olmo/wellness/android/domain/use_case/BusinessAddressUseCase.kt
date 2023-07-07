package olmo.wellness.android.domain.use_case

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.verification1.step1.Address
import olmo.wellness.android.domain.model.verification1.step1.BusinessAddressRequest
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject
import javax.inject.Provider

class BusinessAddressUseCase @Inject constructor(
    private val repository: Provider<ApiRepository>){

    operator fun invoke(businessId: Int) : Flow<Result<List<Address>>> {
        return repository.get().getBusinessAddress(businessId)
    }

    fun updateBusinessAddress(queries: String,isReturn: Boolean, updateBody: BusinessAddressRequest) : Flow<Result<List<Address>>>{
        return repository.get().updateBusinessAddress(queries,isReturn, updateBody)
    }

    fun getBuyerAddress(userId: Int) : Flow<Result<List<Address>>>{
        return repository.get().getBuyerAddress(userId)
    }

    fun updateBuyerAddress(queries: String,isReturn: Boolean, updateBody: BusinessAddressRequest) : Flow<Result<List<Address>>>{
        return repository.get().updateBuyerAddress(queries,isReturn, updateBody)
    }

    fun postBuyerAddress(queries: String,isReturn: Boolean, updateBody: Address) : Flow<Result<List<Address>>>{
        return repository.get().postBuyerAddress(queries,isReturn, updateBody)
    }
}