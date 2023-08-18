package olmo.wellness.android.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import olmo.wellness.android.R
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.ApiIDTypeService
import olmo.wellness.android.data.model.*
import olmo.wellness.android.data.model.authenticator.toAuthenticatorDomain
import olmo.wellness.android.data.model.delete_account.DeleteAccountRequest
import olmo.wellness.android.data.model.id_api.RegisterIDApiRequest
import olmo.wellness.android.data.model.profile.ProfileRequest
import olmo.wellness.android.data.model.profile.toProfileInfoDomain
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.session.SessionRequest
import olmo.wellness.android.data.model.user_info.toUserInfoDomain
import olmo.wellness.android.domain.model.authenticator.Authenticator
import olmo.wellness.android.domain.model.login.LoginData
import olmo.wellness.android.domain.model.login.RegisterData
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.user_info.UserInfoDomain
import olmo.wellness.android.domain.repository.ApiIDTypeRepository
import java.lang.reflect.Type
import javax.inject.Inject

class ApiIDTypeRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: ApiIDTypeService
) : ApiIDTypeRepository {

    override fun login(loginRequest: LoginRequest) = flow {
        emit(Result.Loading())

        kotlin.runCatching { apiService.login(loginRequest) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { loginResponse ->
                        emit(Result.Success(loginResponse.toLoginData()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<LoginData>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<LoginData>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun register(registerRequest: RegisterIDApiRequest) = flow {
        emit(Result.Loading())

        kotlin.runCatching { apiService.sendToken(registerRequest) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { registerResponse ->
                        emit(Result.Success(registerResponse.toRegisterData()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<RegisterData>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<RegisterData>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun verifyCode(): Flow<Unit> {
        return flowOf(apiService.verifyCode()).map {
            it
        }
    }

    override suspend fun verifyUser(code: CodeRequest): Flow<Unit> {
        return flowOf(apiService.verifyUser(code = code)).map {
            it
        }
    }


    override suspend fun resetPasswordWithVerifyCode(resetPasswordRequest: ResetPasswordRequest): Flow<ResetPasswordResponse> {
        return flowOf(apiService.resetPasswordWithVerifyCode(resetPasswordRequest)).map {
            it
        }
    }

    override fun generateAuthenticator() = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.generateAuthenticator()
        }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { authenticator ->
                        emit(Result.Success(authenticator.toAuthenticatorDomain()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<Authenticator>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<Authenticator>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun postAuthenticator(token: TokenRequest) =
        flow {
            emit(Result.Loading())
            kotlin.runCatching {
                apiService.postVerifyAuthenticator(token)
            }
                .onSuccess {
                    if (it.isSuccessful) {
                        it.body()?.let {
                            emit(Result.Success(true))
                        }
                    } else {
                        val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<Boolean>(errorResponse.message))
                    }
                }
                .onFailure {
                    emit(
                        Result.Error<Boolean>(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override suspend fun getSession(refreshToken: String): LoginData? {
        var loginData: LoginData? = null
        kotlin.runCatching { apiService.getSession(SessionRequest(authData = listOf(refreshToken))) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { loginResponse ->
                        loginData = loginResponse.toLoginData()
                    }
                }
            }
        return loginData
    }

    override fun getProfileFromIdServer(userId: Int?, fieds: String) = flow {
        emit(Result.Loading())
        val listUserId = listOf(userId ?: 0)
        val profileRequest = ProfileRequest(id = listUserId)
        val requestJson = Gson().toJson(profileRequest)
        kotlin.runCatching { apiService.getProfile(requestJson, fieds) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toProfileInfoDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    try {
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<List<ProfileInfo>>(errorResponse.message))
                    }catch (_: Exception){
                    }
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<ProfileInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun updateProfilesFromIdServer(listQuery: String, isReturn: Boolean, updateBody: ProfileBodyRequest) = flow {
        kotlin.runCatching { apiService.updateProfiles(listQuery, isReturn, updateBody) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.modified.map {
                                obj -> obj.toProfileInfoDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<ProfileInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<ProfileInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }
    override fun getUserInfoFromIdServer() = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getUserInfo() }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.toUserInfoDomain()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<UserInfoDomain>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<UserInfoDomain>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun resetPassword(identify: IdentityRequest): Flow<Unit> {
        return flowOf(apiService.resetPassword(identify)).map {
            it
        }
    }
}