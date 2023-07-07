package olmo.wellness.android.domain.use_case

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.CodeRequest
import olmo.wellness.android.data.model.LoginRequest
import olmo.wellness.android.data.model.RegisterRequest
import olmo.wellness.android.data.model.user_auth.UserAuthRequest
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
import olmo.wellness.android.domain.model.login.LoginData
import olmo.wellness.android.domain.model.login.UserInfoResponse
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class GetApiUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend fun register(res: RegisterRequest): Flow<Result<Boolean>> {
        return repository.register(res)
    }

    suspend fun login(res: LoginRequest): Flow<Result<LoginData>> {
        return repository.login(res)
    }

    suspend fun verifyCode(): Flow<Unit> {
        return repository.verifyCode()
    }

    suspend fun verifyUser(code: CodeRequest): Flow<Unit> {
        return repository.verifyUser(code = code)
    }

    suspend fun getUserInfo() : Flow<Result<UserInfoResponse>>{
        return repository.getUserInfo()
    }

    suspend fun requestVerifyCode(bodyRequest: VerifyCodeRequest): Flow<Unit>{
        return repository.requestVerifyCode(bodyRequest)
    }

    suspend fun verifyConfirmCode(bodyRequest: VerifyCodeRequest): Flow<Result<Boolean>>{
        return repository.verifyConfirmCode(bodyRequest)
    }

    suspend fun forgetPassword(bodyRequest: VerifyCodeRequest): Flow<Unit>{
        return repository.forgetPassword(bodyRequest)
    }

    suspend fun resetPassword(bodyRequest: VerifyCodeRequest): Flow<Unit>{
        return repository.resetPassword(bodyRequest)
    }

    suspend fun requestUserAuth(bodyRequest: UserAuthRequest) : Flow<Result<Boolean>>{
        return repository.createRequestUserAuth(bodyRequest)
    }

    suspend fun verifyUserAuth(bodyRequest: UserAuthRequest) : Flow<Result<Boolean>>{
        return repository.verifyRequestUserAuth(bodyRequest)
    }

    @JvmInline
    value class Params(val profileRequest: GetProfileRequest)
    suspend fun getProfile(params: Params? = null) = repository.getProfile(params?.profileRequest?.userId, params?.profileRequest?.fields.orEmpty())
}