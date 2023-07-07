package olmo.wellness.android.domain.use_case

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.*
import olmo.wellness.android.data.model.id_api.RegisterIDApiRequest
import olmo.wellness.android.domain.model.login.LoginData
import olmo.wellness.android.domain.model.login.RegisterData
import olmo.wellness.android.domain.repository.ApiIDTypeRepository
import javax.inject.Inject

class GetApiIDTypeUseCase @Inject constructor(
    private val repository: ApiIDTypeRepository
) {

    fun login(loginRequest: LoginRequest): Flow<Result<LoginData>> {
        return repository.login(loginRequest)
    }

    fun register(res: RegisterIDApiRequest): Flow<Result<RegisterData>> {
        return repository.register(res)
    }

    suspend fun verifyCode(): Flow<Unit> {
        return repository.verifyCode()
    }

    suspend fun verifyUser(code: CodeRequest): Flow<Unit> {
        return repository.verifyUser(code = code)
    }

    suspend fun resetPassword(identityRequest: IdentityRequest): Flow<Unit> {
        return repository.resetPassword(identityRequest)
    }

    suspend fun resetPasswordWithVerifyCode(resetPasswordRequest: ResetPasswordRequest): Flow<ResetPasswordResponse> {
        return repository.resetPasswordWithVerifyCode(resetPasswordRequest)
    }
}