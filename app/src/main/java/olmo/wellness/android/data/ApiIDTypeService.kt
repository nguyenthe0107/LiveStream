package olmo.wellness.android.data

import olmo.wellness.android.data.model.*
import olmo.wellness.android.data.model.authenticator.AuthenticatorDTO
import olmo.wellness.android.data.model.delete_account.DeleteAccountRequest
import olmo.wellness.android.data.model.id_api.RegisterIDApiRequest
import olmo.wellness.android.data.model.profile.ProfileInfoDTO
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.profile.update.ProfileInfoUpdateResponse
import olmo.wellness.android.data.model.session.SessionRequest
import olmo.wellness.android.data.model.user_info.UserInfoDTO
import retrofit2.Response
import retrofit2.http.*

interface ApiIDTypeService {

    /* SignIn */
    @POST("session")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @POST("session")
    suspend fun getSession(
        @Body sessionRequest: SessionRequest
    ): Response<LoginResponse>

    @POST("user/register")
    suspend fun sendToken(@Body registerRequest: RegisterIDApiRequest) : Response<RegisterResponse>

    /* Api Get Verify Code */
    @POST("user/verify-code")
    suspend fun verifyCode() : Unit

    /* Api Verify User */
    @POST("user/verify")
    suspend fun verifyUser(@Body code: CodeRequest) : Unit

    /* Forget password */
    @POST("user/reset-password")
    suspend fun resetPassword(@Body identify: IdentityRequest) : Unit

    @POST("user/reset")
    suspend fun resetPasswordWithVerifyCode(@Body resetPasswordRequest: ResetPasswordRequest): ResetPasswordResponse


    @POST("2fa/generate")
    suspend fun generateAuthenticator(): Response<AuthenticatorDTO>

    @POST("2fa/verify")
    suspend fun postVerifyAuthenticator(@Body tokenRequest: TokenRequest): Response<Unit>

    /* profile */
    @GET("user")
    suspend fun getProfile(@Query("fields", encoded = true) fields: String,
        @Query("&projection") projections: String?): Response<BaseResponse<ProfileInfoDTO>>

    @PUT("user")
    suspend fun updateProfiles(
        @Query("fields", encoded = true) fields: String,
        @Query("&returning") returning: Boolean,
        @Body update: ProfileBodyRequest
    ): Response<ProfileInfoUpdateResponse>

    /* get UserInfo */
    @GET("user/info")
    suspend fun getUserInfo(): Response<UserInfoDTO>
}