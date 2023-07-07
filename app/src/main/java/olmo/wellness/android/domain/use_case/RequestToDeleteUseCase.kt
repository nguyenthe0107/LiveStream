package olmo.wellness.android.domain.use_case

import olmo.wellness.android.data.model.delete_account.DeleteAccountRequest
import olmo.wellness.android.domain.repository.ApiIDTypeRepository
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject
import javax.inject.Provider

class RequestToDeleteUseCase @Inject constructor(
    private val repository: Provider<ApiRepository>
) {

    @JvmInline
    value class Params(val rqt: DeleteAccountRequest)

    suspend operator fun invoke(params: Params) = repository.get().requestToDelete(params.rqt)
}