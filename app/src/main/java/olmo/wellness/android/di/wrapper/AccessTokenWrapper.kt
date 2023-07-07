package olmo.wellness.android.di.wrapper

import kotlinx.coroutines.*
import olmo.wellness.android.core.enums.UserRole
import olmo.wellness.android.domain.model.UserInfo
import olmo.wellness.android.domain.model.login.LoginData
import olmo.wellness.android.domain.use_case.GetSessionUseCase
import olmo.wellness.android.domain.use_case.GetUserInfoUseCase
import olmo.wellness.android.domain.use_case.SetUserInfoUseCase
import javax.inject.Inject


class AccessTokenWrapper @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val setUserInfoUseCase: SetUserInfoUseCase,
    private val getSessionUseCase: GetSessionUseCase
) {
    private var accessToken: String? = null

    fun getAccessToken(): String? {
        if (accessToken == null) {
            runBlocking {
                val userInfo =
                    CoroutineScope(Dispatchers.IO).async { getUserInfoUseCase() }
                accessToken = userInfo.await()?.token
            }
        }
        return accessToken
    }

    fun invokeAccessToken(token: String?) {
        accessToken = token
    }

    fun getNewAccessToken(): String? {
        if (accessToken != null) {
            runBlocking {
                CoroutineScope(Dispatchers.IO).launch {
                    getUserInfoUseCase(
                        GetUserInfoUseCase.Params(null)
                    )?.let { userInfo ->
                        val tokenResult =
                            CoroutineScope(Dispatchers.IO).async {
                                getTokenSession(
                                    userInfo.refreshToken
                                )
                            }
                        accessToken = tokenResult.await()
                    }
                }
            }
        }
        return accessToken
    }

    private suspend fun invokeRefreshToken(refreshToken: String) =
        getSessionUseCase(GetSessionUseCase.Params(refreshToken))

    private suspend fun getTokenSession(refreshToken: String): String {
        val result = CoroutineScope(Dispatchers.IO).async {
            invokeRefreshToken(refreshToken)
        }
        val loginData = result.await()
        saveAccessToken(loginData)
        return loginData?.accessToken?.token.orEmpty()
    }

    private fun saveAccessToken(loginData: LoginData?) {
        loginData?.let {
            CoroutineScope(Dispatchers.IO).launch {
                setUserInfoUseCase(
                    SetUserInfoUseCase.Params(
                        userInfo = UserInfo(
                            token = loginData.accessToken?.token.orEmpty(),
                            refreshToken = loginData.accessToken?.refreshToken.orEmpty(),
                            expiration = loginData.accessToken?.expiration ?: 0,
                            userId = loginData.userId ?: 0,
                            userType = loginData.userType ?: UserRole.BUYER.role
                        )
                    )
                )
            }
        }
    }
}
