package olmo.wellness.android.domain.use_case

import olmo.wellness.android.data.model.TokenRequest
import olmo.wellness.android.domain.repository.ApiIDTypeRepository
import javax.inject.Inject

class AuthenticatorUseCase @Inject constructor(
    private val repository: ApiIDTypeRepository
) {

    operator fun invoke() = repository.generateAuthenticator()

    fun postAuthenticator(tokenRequest: TokenRequest) = repository.postAuthenticator(tokenRequest)
}