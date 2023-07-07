package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject
import javax.inject.Provider

class GetBusinessOwnedUseCase @Inject constructor(
    private val repository: Provider<ApiRepository>
) {
    operator fun invoke() = repository.get().getBusinessOwned()

    fun getStoreMain() = repository.get().getStoreMain()

    fun getStore(query: String) = repository.get().getStore(query)
}