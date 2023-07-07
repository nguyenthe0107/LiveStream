package olmo.wellness.android.domain.repository

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.data.model.*
import olmo.wellness.android.data.model.id_api.RegisterIDApiRequest
import olmo.wellness.android.domain.model.login.LoginData
import olmo.wellness.android.domain.model.login.RegisterData
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.delete_account.DeleteAccountRequest
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.domain.model.authenticator.Authenticator
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.user_info.UserInfoDomain

interface ApiIDTypeRepository {
    fun login(registerRequest: LoginRequest): Flow<Result<LoginData>>
    fun register(registerRequest: RegisterIDApiRequest): Flow<Result<RegisterData>>
    suspend fun verifyCode(): Flow<Unit>
    suspend fun verifyUser(code: CodeRequest): Flow<Unit>
    suspend fun resetPassword(identify: IdentityRequest): Flow<Unit>
    suspend fun resetPasswordWithVerifyCode(resetPasswordRequest: ResetPasswordRequest): Flow<ResetPasswordResponse>
    fun generateAuthenticator(): Flow<Result<Authenticator>>
    fun postAuthenticator(token: TokenRequest): Flow<Result<Boolean>>
    suspend fun getSession(refreshToken: String): LoginData?
    fun getProfileFromIdServer(userId: Int?, fields: String): Flow<Result<List<ProfileInfo>>>
    fun updateProfilesFromIdServer(listQuery: String,isReturn: Boolean, updateBody: ProfileBodyRequest): Flow<Result<List<ProfileInfo>>>
    fun getUserInfoFromIdServer(): Flow<Result<UserInfoDomain>>
}