package olmo.wellness.android.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import olmo.wellness.android.R
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.ApiService
import olmo.wellness.android.data.model.*
import olmo.wellness.android.data.model.bank.BankFieldsRequest
import olmo.wellness.android.data.model.bank.toBankDomain
import olmo.wellness.android.data.model.booking.*
import olmo.wellness.android.data.model.business.*
import olmo.wellness.android.data.model.business_type.toBusinessType
import olmo.wellness.android.data.model.chat.toConversationInfoInfoDomain
import olmo.wellness.android.data.model.country.toCountryDomain
import olmo.wellness.android.data.model.delete_account.DeleteAccountRequest
import olmo.wellness.android.data.model.live_stream.*
import olmo.wellness.android.data.model.live_stream.category.toDomain
import olmo.wellness.android.data.model.notification.toNotificationInfoDomain
import olmo.wellness.android.data.model.order.OrderRequestBody
import olmo.wellness.android.data.model.order.toOrderPaymentResponseDomain
import olmo.wellness.android.data.model.profile.BodyProfileRequest
import olmo.wellness.android.data.model.profile.ProfileRequest
import olmo.wellness.android.data.model.profile.business_address.BusinessUpdateDTO
import olmo.wellness.android.data.model.profile.toProfileInfoDomain
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.report_livestream.ReportLiveStreamRequestDTO
import olmo.wellness.android.data.model.report_livestream.toReportLiveInfo
import olmo.wellness.android.data.model.session.SessionRequest
import olmo.wellness.android.data.model.tips.*
import olmo.wellness.android.data.model.upload.toUploadUrl
import olmo.wellness.android.data.model.user_auth.UserAuthRequest
import olmo.wellness.android.data.model.user_follow.UserFollowDTO
import olmo.wellness.android.data.model.user_follow.toUserFollowDomain
import olmo.wellness.android.data.model.user_message_shortcut.UserMessageShortcutDTO
import olmo.wellness.android.data.model.user_message_shortcut.toUserMessageShortcutDomain
import olmo.wellness.android.data.model.user_setting.UserSettingDTO
import olmo.wellness.android.data.model.user_setting.toUserSettingDomain
import olmo.wellness.android.data.model.verification_1.response.toVerificationData
import olmo.wellness.android.data.model.verification_1.step1.toAddressDomain
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
import olmo.wellness.android.data.model.voucher.toVoucherDomain
import olmo.wellness.android.domain.model.bank.BankInfo
import olmo.wellness.android.domain.model.booking.*
import olmo.wellness.android.domain.model.business.BusinessOwn
import olmo.wellness.android.domain.model.business.StoreOwnerResponseDM
import olmo.wellness.android.domain.model.business_type.BusinessType
import olmo.wellness.android.domain.model.chat.ConversationInfo
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.livestream.*
import olmo.wellness.android.domain.model.login.LoginData
import olmo.wellness.android.domain.model.login.UserInfoResponse
import olmo.wellness.android.domain.model.notification.NotificationInfo
import olmo.wellness.android.domain.model.order.OrderPaymentResponse
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.report_livestream.ReportLiveStreamInfo
import olmo.wellness.android.domain.model.upload.UploadUrlInfo
import olmo.wellness.android.domain.model.user_follow.UserFollowInfo
import olmo.wellness.android.domain.model.user_follow.UserFollowRequest
import olmo.wellness.android.domain.model.user_message.UserMessageShortcut
import olmo.wellness.android.domain.model.user_message.toDTO
import olmo.wellness.android.domain.model.user_setting.UserSetting
import olmo.wellness.android.domain.model.user_setting.UserSettingRequest
import olmo.wellness.android.domain.model.user_setting.toUserSettingRequestDTO
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.domain.model.verification1.step1.Address
import olmo.wellness.android.domain.model.verification1.step1.BusinessAddressRequest
import olmo.wellness.android.domain.model.verification1.step1.Step1Request
import olmo.wellness.android.domain.model.verification1.step1.toStep1RequestDTO
import olmo.wellness.android.domain.model.verification1.step2.Step2Request
import olmo.wellness.android.domain.model.verification1.step2.toStep2RequestDTO
import olmo.wellness.android.domain.model.verification1.step3.Step3Request
import olmo.wellness.android.domain.model.verification1.step3.toStep3RequestDTO
import olmo.wellness.android.domain.model.verification1.step4.Step4Request
import olmo.wellness.android.domain.model.verification1.step4.toStep4RequestDTO
import olmo.wellness.android.domain.model.verification2.V2Step1Request
import olmo.wellness.android.domain.model.verification2.toStep1RequestDTO
import olmo.wellness.android.domain.model.voucher.VoucherValidateInfo
import olmo.wellness.android.domain.repository.ApiRepository
import olmo.wellness.android.domain.tips.CoinInfo
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.domain.tips.PricePackageInfo
import olmo.wellness.android.domain.tips.TipsPackageOptionInfo
import olmo.wellness.android.extension.readAsRequestBody
import olmo.wellness.android.ui.livestream.stream.data.LivestreamKeyResponse
import olmo.wellness.android.ui.livestream.stream.data.UpdateLivestreamWrapRequest
import java.lang.reflect.Type
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: ApiService,
) : ApiRepository {

    override suspend fun register(registerRequest: RegisterRequest): Flow<Result<Boolean>> = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.sendToken(registerRequest) }
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

    override suspend fun login(loginRequest: LoginRequest) = flow {
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

    override suspend fun getUserInfo() = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getUserInfo() }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { userTypeResponse ->
                        emit(Result.Success(userTypeResponse.toUserTypeData()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    try {
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<UserInfoResponse>(errorResponse.message))
                    } catch (ex: Exception) {
                        emit(Result.Error<UserInfoResponse>(ex.message.orEmpty()))
                    }
                }
            }
            .onFailure {
                emit(
                    Result.Error<UserInfoResponse>(
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

    /* V2 */
    override suspend fun requestVerifyCode(bodyRequest: VerifyCodeRequest): Flow<Unit> {
        return flowOf(apiService.requestVerifyCode(bodyRequest)).map {
            it
        }
    }

    override suspend fun verifyConfirmCode(bodyRequest: VerifyCodeRequest): Flow<Result<Boolean>> =
        flow {
            emit(Result.Loading())
            kotlin.runCatching { apiService.verifyConfirmCode(bodyRequest) }.onSuccess {
                if (it.isSuccessful) {
                    emit(Result.Success(true))
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    try {
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<Boolean>(errorResponse.message))
                    } catch (ex: Exception) {
                        emit(Result.Error<Boolean>(ex.message.orEmpty()))
                    }
                }
            }.onFailure {
                emit(
                    Result.Error<Boolean>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
        }

    override suspend fun forgetPassword(bodyRequest: VerifyCodeRequest): Flow<Unit> {
        return flowOf(apiService.forgetPassword(bodyRequest)).map {
            it
        }
    }

    override suspend fun resetPassword(bodyRequest: VerifyCodeRequest): Flow<Unit> {
        return flowOf(apiService.resetPassword(bodyRequest)).map {
            it
        }
    }

    override fun getProductCategories(page: Int): Flow<Result<List<LiveCategory>>> = flow {
        emit(Result.Loading())

        kotlin.runCatching { apiService.getProductCategories(page = page) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { category -> category.toDomain() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<LiveCategory>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<LiveCategory>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getCountryList() = flow {
        emit(Result.Loading())

        kotlin.runCatching { apiService.getCountryList() }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { countryResponse ->
                        emit(Result.Success(countryResponse.records.map { countryDTO -> countryDTO.toCountryDomain() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    try {
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<List<Country>>(errorResponse.message))
                    } catch (ex: Exception) {
                        emit(Result.Error<List<Country>>("error"))
                    }
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<Country>>(
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

    override fun getBusinessType() = flow {
        emit(Result.Loading())

        kotlin.runCatching { apiService.getBusinessTypes() }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { businessType -> businessType.toBusinessType() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<BusinessType>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<BusinessType>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    /* Step1 */

    override fun submitVerification1Step1(step1Request: Step1Request) =
        flow {
            emit(Result.Loading())

            kotlin.runCatching { apiService.submitVerification1Step1(step1Request.toStep1RequestDTO()) }
                .onSuccess {
                    if (it.isSuccessful) {
                        it.body()?.let { response ->
                            emit(Result.Success(response.businessId))
                        }
                    } else {
                        val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<Int>(errorResponse.message))
                    }
                }
                .onFailure {
                    emit(
                        Result.Error<Int>(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun submitUpdateVerification1Step1(businessId: Int, step1Request: Step1Request) =
        flow {
            emit(Result.Loading())

            kotlin.runCatching {
                apiService.submitUpdateVerification1Step1(
                    businessId,
                    step1Request.toStep1RequestDTO()
                )
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

    /* End Step1 */

    /* Step2 */
    override fun submitVerification1Step2(businessId: Int, step2Request: Step2Request) =
        flow {
            emit(Result.Loading())

            kotlin.runCatching {
                apiService.submitVerification1Step2(
                    businessId,
                    step2Request.toStep2RequestDTO()
                )
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

    override fun submitUpdateVerification1Step2(
        businessId: Int,
        step2Request: Step2Request,
    ) = flow {
        emit(Result.Loading())

        kotlin.runCatching {
            apiService.submitUpdateVerification1Step2(
                businessId,
                step2Request.toStep2RequestDTO()
            )
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
                    emit(Result.Error<Boolean>(errorResponse?.message ?: ""))
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

    /* End-Step2 */

    /* Step3 */
    override fun submitVerification1Step3(businessId: Int, step3Request: Step3Request) =
        flow {
            emit(Result.Loading())

            kotlin.runCatching {
                apiService.submitVerification1Step3(
                    businessId,
                    step3Request.toStep3RequestDTO()
                )
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
                        emit(Result.Error<Boolean>(errorResponse?.message ?: ""))
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

    override fun submitUpdateVerification1Step3(
        businessId: Int,
        step3Request: Step3Request,
    ) = flow {
        emit(Result.Loading())

        kotlin.runCatching {
            apiService.submitUpdateVerification1Step3(
                businessId,
                step3Request.toStep3RequestDTO()
            )
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
                    emit(Result.Error<Boolean>(errorResponse?.message ?: ""))
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

    /* End Step3 */

    /* Step4 */
    override fun submitVerification1Step4(
        businessId: Int,
        step4Request: Step4Request,
    ) = flow {
        emit(Result.Loading())

        kotlin.runCatching {
            apiService.submitVerification1Step4(
                businessId,
                step4Request.toStep4RequestDTO()
            )
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

    override fun submitUpdateVerification1Step4(
        businessId: Int,
        step4Request: Step4Request,
    ) = flow {
        emit(Result.Loading())

        kotlin.runCatching {
            apiService.submitUpdateVerification1Step4(
                businessId,
                step4Request.toStep4RequestDTO()
            )
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
    /* End-Step4 */

    override fun getUploadUrlInfo(mimeType: String) =
        flow {
            val format = "\"$mimeType\""
            kotlin.runCatching { apiService.getUploadUrlInfo(format) }
                .onSuccess {
                    if (it.isSuccessful) {
                        it.body()?.let { response ->
                            emit(Result.Success(response.toUploadUrl()))
                        }
                    } else {
                        val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<UploadUrlInfo>(errorResponse.message))
                    }
                }
                .onFailure {
                    emit(
                        Result.Error<UploadUrlInfo>(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun uploadFile(url: String, fileUri: Uri) =
        flow {
            kotlin.runCatching {
                context.contentResolver.getType(fileUri)?.let { mimeType ->
                    val requestBody = context.contentResolver.readAsRequestBody(fileUri)
                    apiService.uploadFile(mimeType, url, requestBody)
                }
            }
                .onSuccess {
                    if (it?.isSuccessful == true) {
                        it.body()?.let {
                            emit(Result.Success(true))
                        }
                    } else {
                        emit(Result.Error<Boolean>(context.getString(R.string.upload_fail)))
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

    override fun getBankList(countryIds: List<Int>) = flow {
        val bankFieldsRequest = BankFieldsRequest(countryId = countryIds)
        kotlin.runCatching { apiService.getBankListSIV(Gson().toJson(bankFieldsRequest)) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { bankDTO -> bankDTO.toBankDomain() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<BankInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<BankInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun checkStoreName(storeName: String) = flow {
        kotlin.runCatching { apiService.checkStoreName("%22$storeName%22") }
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

    override fun getBusinessOwned() = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getBusinessOwned() }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.businesses.map { obj ->
                            obj.toBusinessOwnDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<BusinessOwn>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<BusinessOwn>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getStoreMain() = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getStoreMain() }
            .onSuccess {
                if (it.isSuccessful) {
                    if (it.body()?.store != null) {
                        it.body()?.let { response ->
                            emit(Result.Success(response.toStoreMainDomain()))
                        }
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<StoreOwnerResponseDM>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<StoreOwnerResponseDM>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getStore(query: String) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getStore(query) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.toStoreMainDomain()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<StoreOwnerResponseDM>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<StoreOwnerResponseDM>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun updateStoreBusinessOwned(
        fields: String,
        isReturn: Boolean,
        update: StoreBusinessRequest,
    ) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.updateStoreBusinessOwned(fields, isReturn, update) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.modified.map { obj ->
                            obj.toBusinessOwnDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<BusinessOwn>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<BusinessOwn>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }


    override fun getVerificationInfo(businessId: Int) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getVerificationInfo(businessId) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.toVerificationData()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<VerificationData>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<VerificationData>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun submitVerification2Step1(
        businessId: Int,
        step1Request: V2Step1Request,
    ) = flow {
        emit(Result.Loading())

        kotlin.runCatching {
            apiService.submitVerification2Step1(
                businessId,
                step1Request.toStep1RequestDTO()
            )
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

    override suspend fun getLivestreamKey(request: LiveStreamRequest) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.createLivestreamKey(request) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(
                            Result.Success(
                                response.toLiveStreamDomain()
                            )
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<LivestreamKeyResponse>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<LivestreamKeyResponse>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }

    }

    override suspend fun listLivestream(query: String, projection: String?) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.listAllLivestreams(query, projection) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toLiveStreamInfoDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<LivestreamInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<LivestreamInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun getLivestreamPin(query: String) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getAllLivestreamsPin(query) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toLiveStreamInfoDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<LivestreamInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<LivestreamInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun deleteLivestream(
        query: String,
        returning: Boolean?,
    ): Flow<Result<Boolean>> = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.deleteLivestream(query, returning) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(
                            Result.Success(true)
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error(it.message ?: context.getString(R.string.unknown_error))
                )
            }
    }

    override suspend fun updateStatusLivestream(
        query: String,
        update: UpdateLivestreamWrapRequest,
    ) = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.updateLivestream(
                query,
                update
            )
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

    override suspend fun getAllLiveStreams(
        typeTitle: String,
        userId: Int?,
        limit: Int?,
        page: Int?,
        startTime: Long?,
        endTime: Long?,
    ) = flow {
        emit(Result.Loading())
        val format = typeTitle
        kotlin.runCatching {
            apiService.getAllLiveStreams(
                format,
                userId,
                limit,
                page,
                startTime,
                endTime
            )
        }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toLiveSteamShortInfo()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<LiveSteamShortInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<LiveSteamShortInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun getSweepListStreams(
        query: String,
    ) = flow {
        emit(Result.Loading())
        val format = "\"$query\""
        kotlin.runCatching { apiService.getSweepListStreams(format) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toLiveSteamShortInfo()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<LiveSteamShortInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<LiveSteamShortInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun getAllSellerFinishedStreams() = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getAllFinishedLiveStream() }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<LiveSteamShortInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<LiveSteamShortInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun getListProfileLiveStream(userId: Int?, limit: Int?, page: Int?) = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            val formatUserId = "\"$userId\""
            apiService.getListProfileLiveStream(
                userId = formatUserId,
                limit = limit,
                page = page
            )
        }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<LiveSteamShortInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<LiveSteamShortInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun getUserFollow(query: String?, projection: String?) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getUserFollow(query, projection) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toUserFollowDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<UserFollowInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<UserFollowInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun getUserFollowing(userId: Int?) = flow {
        emit(Result.Loading())
        val formatUserId = "\"$userId\""
        kotlin.runCatching { apiService.getUserFollowing(formatUserId) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toUserFollowDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<UserFollowInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<UserFollowInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun getUserFollower(userId: Int?) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getUserFollower(userId) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toUserFollowDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<UserFollowInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<UserFollowInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override suspend fun postUserFollow(query: UserFollowRequest) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.postUserFollow(query) }
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

    override suspend fun deleteUserFollow(query: String) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.deleteUserFollow(query, true) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.list?.map { obj ->
                            obj.toUserFollowDomain()
                        } ?: emptyList()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<UserFollowInfo>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<UserFollowInfo>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getProfile(userId: Int?, projection: String?) = flow {
        emit(Result.Loading())
        val listUserId = listOf(userId ?: 0)
        val profileRequest = ProfileRequest(id = listUserId)
        val requestJson = Gson().toJson(profileRequest)
        kotlin.runCatching { apiService.getProfile(requestJson, projection) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toProfileInfoDomain()
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

    override fun updateProfiles(
        listQuery: String,
        returnResult: Boolean,
        updateBody: ProfileBodyRequest,
    ) = flow {
        kotlin.runCatching { apiService.updateProfiles(listQuery, returnResult, updateBody) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.modified.map { obj ->
                            obj.toProfileInfoDomain()
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

    override fun getBusinessAddress(userId: Int) = flow {
        emit(Result.Loading())
        val listUserId = listOf(userId)
        val requestString = Gson().toJson(BusinessUpdateDTO(businessId = listUserId))
        kotlin.runCatching { apiService.getBusinessAddress(requestString) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { addressDTO -> addressDTO.toAddressDomain() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<Address>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<Address>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun updateBusinessAddress(
        queries: String,
        isReturn: Boolean,
        updateBody: BusinessAddressRequest,
    ) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.updateBusinessAddress(queries, isReturn, updateBody) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.modified.map { addressDTO -> addressDTO.toAddressDomain() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<Address>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<Address>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getBuyerAddress(userId: Int) = flow {
        emit(Result.Loading())
        val listUserId = listOf(userId)
        val requestString = Gson().toJson(BusinessUpdateDTO(id = null, userId = listUserId))
        kotlin.runCatching { apiService.getBuyerAddress(requestString) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { addressDTO -> addressDTO.toAddressDomain() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<Address>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<Address>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun updateBuyerAddress(
        queries: String,
        isReturn: Boolean,
        updateBody: BusinessAddressRequest,
    ) = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.updateBuyerAddress(queries, isReturn, updateBody) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.modified.map { addressDTO -> addressDTO.toAddressDomain() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<Address>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<Address>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun postBuyerAddress(
        queries: String,
        isReturn: Boolean,
        updateBody: Address,
    ) = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.postBuyerAddress(queries,
                isReturn,
                PostRequest(listOf(updateBody)))
        }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { addressDTO -> addressDTO.toAddressDomain() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<Address>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<Address>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    /* userSetting */
    override fun getUserSetting(userId: Int, protection: String) = flow {
        emit(Result.Loading())
        val listUserId = listOf(userId)
        val requestString = Gson().toJson(UserSettingDTO(userId = listUserId))
        kotlin.runCatching { apiService.getUserSetting(requestString, protection) }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { addressDTO -> addressDTO.toUserSettingDomain() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<UserSetting>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<UserSetting>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun updateUserSetting(
        queries: String,
        isReturn: Boolean,
        updateBody: UserSettingRequest,
    ) = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.updateUserSetting(
                queries,
                isReturn,
                updateBody.toUserSettingRequestDTO()
            )
        }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.modified.map { addressDTO -> addressDTO.toUserSettingDomain() }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<UserSetting>>(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error<List<UserSetting>>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getUserMessageShortcut(
        userId: Int,
        queries: String,
    ): Flow<Result<List<UserMessageShortcut>>> = flow<Result<List<UserMessageShortcut>>> {
        emit(Result.Loading(null))
        val listUserId = listOf(userId)
        val requestString = Gson().toJson(UserMessageShortcutDTO(userId = listUserId))

        kotlin.runCatching {
            apiService.getUserMessageShortcut(requestString, queries)
        }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(
                            Result.Success(
                                response.records.map { userMessageShortcutDTO ->
                                    userMessageShortcutDTO.toUserMessageShortcutDomain()
                                }
                            )
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun createUserMessageShortcut(messageShortcut: String): Flow<Result<List<UserMessageShortcut>>> =
        flow<Result<List<UserMessageShortcut>>> {
            emit(Result.Loading(null))
            val body = listOf(
                UserMessageShortcutDTO(
                    messageShortcut = messageShortcut
                )
            )

            kotlin.runCatching {
                apiService.createUserMessageShortcut(body = PostRequest(body))
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(
                            Result.Success(
                                response.records.map { userMessageShortcutDTO ->
                                    userMessageShortcutDTO.toUserMessageShortcutDomain()
                                }
                            )
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun updateUserMessageShortcut(
        messageShortcut: UserMessageShortcut,
        fields: String,
        returning: Boolean,
    ): Flow<Result<List<UserMessageShortcut>>> = flow<Result<List<UserMessageShortcut>>> {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.updateUserMessageShortcut(
                body = UpdateRequest(messageShortcut.toDTO()),
                fields = fields,
                returning = returning
            )
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(
                        Result.Success(
                            response.records.map { userMessageShortcutDTO ->
                                userMessageShortcutDTO.toUserMessageShortcutDomain()
                            }
                        )
                    )
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getNotification(
        page: Int,
        limit: Int,
        userId: Int?,
    ): Flow<Result<List<NotificationInfo>>> =
        flow<Result<List<NotificationInfo>>> {
            emit(Result.Loading())
            kotlin.runCatching {
                apiService.getNotification(
                    page, limit, userId
                )
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(
                            Result.Success(
                                response.records.map { notificationDTO ->
                                    notificationDTO.toNotificationInfoDomain()
                                }
                            )
                        )
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun getConversations(queries: String): Flow<Result<List<ConversationInfo>>> = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.getConversations(fields = queries)
        }.onSuccess {
            (if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { channelDTO ->
                        channelDTO.toConversationInfoInfoDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }) as Unit
        }.onFailure {
            emit(
                Result.Error<List<ConversationInfo>>(
                    it.message ?: context.getString(R.string.unknown_error)
                )
            )
        }
    }

    override fun getCategories(): Flow<Result<List<LiveCategory>>> = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.getCategories()
        }.onSuccess {
            (if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { categoryDTO ->
                        categoryDTO.toDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }) as Unit
        }.onFailure {
            emit(
                Result.Error<List<LiveCategory>>(
                    it.message ?: context.getString(R.string.unknown_error)
                )
            )
        }
    }

    override fun getSections(
        isMySelf: Int?,
        userId: Int?,
    ): Flow<Result<List<HomeLiveSectionData>>> = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.getSections(isMySelf, userId)
        }.onSuccess {
            (if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { sectionDTO ->
                        sectionDTO.toDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }) as Unit
        }.onFailure {
            emit(
                Result.Error<List<HomeLiveSectionData>>(
                    it.message ?: context.getString(R.string.unknown_error)
                )
            )
        }
    }

    override fun getLivestreamFilter(
        typeTitle: String?, startTime: Long?, endTime: Long?, categoryId: Int?,
        title: String?, page: Int?, limit: Int?, userId: Int?, isMySelf: Boolean?,
    ): Flow<Result<List<LiveSteamShortInfo>>> = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.getLivestreamFilter(
                typeTitle = typeTitle,
                startTime = startTime,
                categoryId = categoryId,
                endTime = endTime,
                title = title,
                limit = limit,
                isMySelf = (if (isMySelf == true) 1 else null),
                page = page
            )
        }.onSuccess {
            (if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { sectionDTO ->
                        sectionDTO.toLiveSteamShortInfo()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }) as Unit
        }.onFailure {
            emit(
                Result.Error<List<LiveSteamShortInfo>>(
                    it.message ?: context.getString(R.string.unknown_error)
                )
            )
        }
    }

    override fun getTrendingHashtags(): Flow<Result<List<HashTag>>> = flow {
        emit(Result.Loading())
        kotlin.runCatching {
            apiService.getTrendingHashtags()
        }.onSuccess {
            (if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { hashTagDTO ->
                        hashTagDTO.toDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }) as Unit
        }.onFailure {
            emit(
                Result.Error<List<HashTag>>(
                    it.message ?: context.getString(R.string.unknown_error)
                )
            )
        }
    }

    override fun postUserFollowLiveStream(bodyRequest: UserFollowInfo): Flow<Result<List<UserFollowInfo>>> =
        flow {
            emit(Result.Loading(null))
            val body = listOf(
                UserFollowDTO(
                    userId = bodyRequest.userId,
                    livestreamId = bodyRequest.livestreamId
                )
            )

            kotlin.runCatching {
                apiService.postUserFollowLiveStream(body = PostRequest(body))
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { hashTagDTO ->
                            hashTagDTO.toUserFollowDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun deleteUserFollowLiveStream(query: String): Flow<Result<List<UserFollowInfo>>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.deleteUserFollowLiveStream(query)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.modified.map { hashTagDTO ->
                            hashTagDTO.toUserFollowDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun postShareOnProfile(bodyRequest: BodyProfileRequest): Flow<Result<Boolean>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.postShareOnProfile(bodyRequest)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(true))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun getShareOnProfile(page: Int, limit: Int): Flow<Result<List<LiveSteamShortInfo>>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.getShareOnProfile(page = page, limit = limit)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toLiveSteamShortInfo()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun postUserReport(bodyRequest: PostRequest<List<ReportLiveStreamRequestDTO>>): Flow<Result<Boolean>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.postUserReport(bodyRequest)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(true))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun getUserReport(query: String): Flow<Result<List<ReportLiveStreamInfo>>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.getUserReport(query)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { obj ->
                            obj.toReportLiveInfo()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun getTipPackagesOptions(
        query: String?,
        projection: String?,
        page: Int?,
        limit: Int?,
    ): Flow<Result<List<TipsPackageOptionInfo>>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getTipsPackage(
                fields = query,
                projection = projection,
                page = page,
                limit = limit
            )
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { obj ->
                        obj.toTipPackageDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getPackagesOptions(
        query: String?,
        projection: String?,
        page: Int?,
        limit: Int?,
    ): Flow<Result<List<PackageOptionInfo>>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getPackageOptions(
                fields = query,
                projection = projection,
                page = page,
                limit = limit
            )
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { obj ->
                        obj.toPackageOptionDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getTotalCoin(): Flow<Result<CoinInfo>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getTotalCoin()
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.toCoinDomain()))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getPricePackage(idPackage: Int): Flow<Result<PricePackageInfo>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getPackageOptionPrice(idPackage)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.toPricePackageInfo()))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun createOrder(bodyRequest: OrderRequestBody): Flow<Result<OrderPaymentResponse>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.createRequestOrder(bodyRequest)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.toOrderPaymentResponseDomain()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun getUserBankAccount(): Flow<Result<List<BankInfo>>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getUserBankAccounts()
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { ob ->
                        ob.toBankDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getService(
        fields: String?,
        search: String?,
        page: Int?,
    ): Flow<Result<List<ServiceBooking>>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getServices(fields = fields, search = search, page = page)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { ob ->
                        ob.toServiceBooking()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getServiceLivestream(livestreamId: Int?): Flow<Result<List<ServiceBooking>>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.getServicesLiveStream(liveStreamId = livestreamId)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { ob ->
                            ob.toServiceBooking()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
                .onFailure {
                    emit(
                        Result.Error(
                            it.message ?: context.getString(R.string.unknown_error)
                        )
                    )
                }
        }

    override fun deleteServiceLivestream(
        userId: Int?,
        livestreamId: Int?,
        serviceId: Int?,
    ): Flow<Result<String>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.deleteServiceLivestream(liveStreamId = livestreamId,
                userId = userId,
                serviceId = serviceId)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

    override fun getListVoucher(serviceId: Int?): Flow<Result<List<olmo.wellness.android.domain.model.voucher.VoucherInfo>>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.getListVoucher(serviceId = serviceId)
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.vouchers?.map { ob ->
                            ob?.toVoucherDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
        }

    override fun getValidateVoucher(
        voucherCode: String?,
        sessionConfirmId: Double?,
    ): Flow<Result<VoucherValidateInfo>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getValidateVoucher(voucherCode = voucherCode,
                sessionConfirmId = sessionConfirmId)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.toVoucherDomain()))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
    }

    override fun getListBookingBuyer(
        title: String?,
        bookingId: Int?,
        page: Int?,
        limit: Int?,
    ): Flow<Result<List<BookingHistoryInfo>>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getListBookingBuyer(bookingTitle = title,
                bookingId = bookingId,
                page = page,
                limit = limit)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { ob ->
                        ob.toBookingHistoryDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
    }

    override fun getListBookingSeller(
        title: String?,
        bookingId: Int?,
        page: Int?,
        limit: Int?,
    ): Flow<Result<List<BookingHistoryInfo>>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getListBookingSeller(bookingTitle = title,
                bookingId = bookingId,
                page = page,
                limit = limit)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { ob ->
                        ob.toBookingHistoryDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
    }

    override fun getBookingDetail(id: Int?): Flow<Result<List<BookingHistoryInfo>>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getBookingDetail(id = id)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { ob ->
                        ob.toBookingHistoryDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
    }

    override fun getServicePublicById(id: Int?): Flow<Result<ServiceBookingForCart>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getServicePublicById(id = id)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.toServiceBooking()))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }.onFailure {
        }
    }

    override fun getServiceLocation(query: String?): Flow<Result<List<ServiceLocationInfo>>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.getServiceLocation(fields = query.orEmpty())
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.records.map { ob ->
                            ob.toServiceLocationDomain()
                        }))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
        }

    override fun getServiceCalendar(
        fromDate: Long,
        toDate: Long,
        serviceId: Int,
    ): Flow<Result<List<DatePriceInfo>>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getServiceCalendar(fromDate, toDate, serviceId)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.datePrices.map { ob ->
                        ob.toDatePriceDomain()
                    }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
    }

    override fun getServiceSessionByDate(
        fromDate: Long, serviceId: Int,
        id: Double, typeSession: String,
    ): Flow<Result<TimeBookingInfo>> = flow {
        emit(Result.Loading(null))
        kotlin.runCatching {
            apiService.getServiceSessionByDate(fromDate = fromDate,
                serviceId = serviceId,
                id = id,
                typeSession = typeSession)
        }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.toTimeBookingDomain()))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                val errorResponse: ErrorResponseDTO =
                    Gson().fromJson(it.errorBody()?.charStream(), type)
                emit(Result.Error(errorResponse.message))
            }
        }
    }

    override fun postServiceSessionConfirm(bodyRequest: List<ServiceSessionInfo>): Flow<Result<ServiceSessionConfirmInfo>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.postServiceSessionConfirm(ModifiedServiceResponse(
                    bodyRequest.map {
                        it.toServiceSessionDTO()
                    }
                ))
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.toServiceSessionConfirmDomain()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
        }

    override fun createBookingId(body: BookingRequestInfo): Flow<Result<BookingIdResponseInfo>> =
        flow {
            emit(Result.Loading(null))
            kotlin.runCatching {
                apiService.createBookingId(body.toBookingDTO())
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        emit(Result.Success(response.toBookingIdDomain()))
                    }
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
        }

    override fun requestDeleteServiceSessionConfirm(bodyRequest: RequestCancelBooking): Flow<Result<Boolean>> =
        flow {
            emit(Result.Loading())
            kotlin.runCatching { apiService.requestCancelBooking(bodyRequest) }.onSuccess {
                if (it.isSuccessful) {
                    emit(Result.Success(true))
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    try {
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<Boolean>(errorResponse.message))
                    } catch (ex: Exception) {
                        emit(Result.Error<Boolean>(ex.message.orEmpty()))
                    }
                }
            }.onFailure {
                emit(
                    Result.Error<Boolean>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
        }

    override fun requestRescheduleServiceSessionConfirm(bodyRequest: RequestCancelBooking): Flow<Result<Boolean>> =
        flow {
            emit(Result.Loading())
            kotlin.runCatching { apiService.requestRescheduleBooking(bodyRequest) }.onSuccess {
                if (it.isSuccessful) {
                    emit(Result.Success(true))
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    try {
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<Boolean>(errorResponse.message))
                    } catch (ex: Exception) {
                        emit(Result.Error<Boolean>(ex.message.orEmpty()))
                    }
                }
            }.onFailure {
                emit(
                    Result.Error<Boolean>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
        }

    override fun addToCart(rqt: PostRequest<List<RequestAddCart>>): Flow<Result<String>> = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.addToCart(rqt) }.onSuccess {
            if (it.isSuccessful) {
                emit(Result.Success(""))
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                try {
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<String>(errorResponse.message))
                } catch (ex: Exception) {
                    emit(Result.Error<String>(ex.message.orEmpty()))
                }
            }
        }.onFailure {
            emit(
                Result.Error<String>(
                    it.message ?: context.getString(R.string.unknown_error)
                )
            )
        }
    }

    override fun deleteToCart(returning: Boolean, fields: String?): Flow<Result<String>> = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.deleteToCart(fields = fields, returning = returning) }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                try {
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<String>(errorResponse.message))
                } catch (ex: Exception) {
                    emit(Result.Error<String>(ex.message.orEmpty()))
                }
            }
        }.onFailure {
            emit(
                Result.Error<String>(
                    it.message ?: context.getString(R.string.unknown_error)
                )
            )
        }
    }

    override fun getUserCart(fields: String?, page: Int): Flow<Result<List<CartBooking>>> = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.getUserCart(fields = fields, page = page) }.onSuccess {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    emit(Result.Success(response.records.map { it.toCartBooking() }))
                }
            } else {
                val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                try {
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error<List<CartBooking>>(errorResponse.message))
                } catch (ex: Exception) {
                    emit(Result.Error<List<CartBooking>>(ex.message.orEmpty()))
                }
            }
        }.onFailure {
            emit(
                Result.Error<List<CartBooking>>(
                    it.message ?: context.getString(R.string.unknown_error)
                )
            )
        }
    }

    override suspend fun createRequestUserAuth(bodyRequest: UserAuthRequest): Flow<Result<Boolean>> =
        flow {
            emit(Result.Loading())
            kotlin.runCatching { apiService.createRequestUserAuth(bodyRequest) }.onSuccess {
                if (it.isSuccessful) {
                    emit(Result.Success(true))
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    try {
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<Boolean>(errorResponse.message))
                    } catch (ex: Exception) {
                        emit(Result.Error<Boolean>(ex.message.orEmpty()))
                    }
                }
            }.onFailure {
                emit(
                    Result.Error<Boolean>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
        }

    override suspend fun verifyRequestUserAuth(bodyRequest: UserAuthRequest): Flow<Result<Boolean>> =
        flow {
            emit(Result.Loading())
            kotlin.runCatching { apiService.verifyRequestUserAuth(bodyRequest) }.onSuccess {
                if (it.isSuccessful) {
                    emit(Result.Success(true))
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    try {
                        val errorResponse: ErrorResponseDTO =
                            Gson().fromJson(it.errorBody()?.charStream(), type)
                        emit(Result.Error<Boolean>(errorResponse.message))
                    } catch (ex: Exception) {
                        emit(Result.Error<Boolean>(ex.message.orEmpty()))
                    }
                }
            }.onFailure {
                emit(
                    Result.Error<Boolean>(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
        }

    override fun requestToDelete(rqt: DeleteAccountRequest): Flow<Result<String>> = flow {
        emit(Result.Loading())
        kotlin.runCatching { apiService.requestToDelete(rqt) }
            .onSuccess {
                if (it.isSuccessful) {
                    emit(Result.Success(""))
                } else {
                    val type: Type = object : TypeToken<ErrorResponseDTO?>() {}.type
                    val errorResponse: ErrorResponseDTO =
                        Gson().fromJson(it.errorBody()?.charStream(), type)
                    emit(Result.Error(errorResponse.message))
                }
            }
            .onFailure {
                emit(
                    Result.Error(
                        it.message ?: context.getString(R.string.unknown_error)
                    )
                )
            }
    }

}