package olmo.wellness.android.data

import okhttp3.RequestBody
import olmo.wellness.android.core.Constants.LIMIT_PER_PAGE
import olmo.wellness.android.data.model.*
import olmo.wellness.android.data.model.bank.BankDTO
import olmo.wellness.android.data.model.booking.*
import olmo.wellness.android.data.model.business.*
import olmo.wellness.android.data.model.business_type.BusinessTypeDTO
import olmo.wellness.android.data.model.chat.ConversationInfoDTOModel
import olmo.wellness.android.data.model.country.CountryDTO
import olmo.wellness.android.data.model.delete_account.DeleteAccountRequest
import olmo.wellness.android.data.model.id_api.RegisterIDApiRequest
import olmo.wellness.android.data.model.live_stream.*
import olmo.wellness.android.data.model.live_stream.category.LiveCategoryDTO
import olmo.wellness.android.data.model.notification.NotificationDTOModel
import olmo.wellness.android.data.model.order.OrderPaymentResponseDTO
import olmo.wellness.android.data.model.order.OrderRequestBody
import olmo.wellness.android.data.model.profile.BodyProfileRequest
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.profile.update.ProfileInfoUpdateResponse
import olmo.wellness.android.data.model.profile.ProfileInfoDTO
import olmo.wellness.android.data.model.report_livestream.ReportLiveStreamDTO
import olmo.wellness.android.data.model.report_livestream.ReportLiveStreamRequestDTO
import olmo.wellness.android.data.model.session.SessionRequest
import olmo.wellness.android.data.model.tips.CoinDTO
import olmo.wellness.android.data.model.tips.PackageOptionDTO
import olmo.wellness.android.data.model.tips.PricePackageDTO
import olmo.wellness.android.data.model.tips.TipsPackageOptionDTO
import olmo.wellness.android.data.model.upload.UploadResponse
import olmo.wellness.android.data.model.user_auth.UserAuthRequest
import olmo.wellness.android.data.model.user_follow.UserFollowDTO
import olmo.wellness.android.data.model.user_follow.UserFollowModifiedDTO
import olmo.wellness.android.data.model.user_message_shortcut.UserMessageShortcutDTO
import olmo.wellness.android.data.model.user_setting.UserSettingDTO
import olmo.wellness.android.data.model.user_setting.UserSettingModifiedResponseDTO
import olmo.wellness.android.data.model.user_setting.UserSettingRequestDTO
import olmo.wellness.android.data.model.verification_1.response.VerificationDTOResponse
import olmo.wellness.android.data.model.verification_1.step1.AddressDTO
import olmo.wellness.android.data.model.verification_1.step1.Verification1Step1Request
import olmo.wellness.android.data.model.verification_1.step1.Verification1Step1Response
import olmo.wellness.android.data.model.verification_1.step2.Verification1Step2Request
import olmo.wellness.android.data.model.verification_1.step4.Verification1Step4Request
import olmo.wellness.android.data.model.verification_1.step3.Verification1Step3Request
import olmo.wellness.android.data.model.verification_2.Verification2Step1Request
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
import olmo.wellness.android.data.model.voucher.VoucherModelDTO
import olmo.wellness.android.data.model.voucher.VoucherValidateModelDTO
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.user_follow.UserFollowRequest
import olmo.wellness.android.domain.model.verification1.step1.Address
import olmo.wellness.android.domain.model.verification1.step1.BusinessAddressRequest
import olmo.wellness.android.ui.livestream.stream.data.UpdateLivestreamWrapRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("v2/user/register")
    suspend fun register(@Body registerRequest: RegisterIDApiRequest): Response<Unit>

    @POST("v2/user/verify-code")
    suspend fun requestVerifyCode(@Body bodyRequest: VerifyCodeRequest): Response<Unit>

    @POST("v2/user/verify")
    suspend fun verifyConfirmCode(@Body bodyRequest: VerifyCodeRequest): Response<Unit>

    /* Forget password */
    @POST("v2/user/forget-password")
    suspend fun forgetPassword(@Body identify: VerifyCodeRequest): Response<Unit>

    @POST("v2/user/reset-password")
    suspend fun resetPassword(@Body identify: VerifyCodeRequest): Unit

    //-- list all livestream
    @GET("livestream/list")
    suspend fun listAllLivestreams(
        @Query("fields", encoded = true) fields: String,
        @Query("&projection") projection: String?,
    ): Response<BaseResponse<LiveStreamInfoDTO>>

    @DELETE("livestream")
    suspend fun deleteLivestream(
        @Query("fields", encoded = true) fields: String,
        @Query("&returning") returning: Boolean?,
    ): Response<LiveStreamDTO>

    //-- create a live stream
    @POST("livestream")
    suspend fun createLivestreamKey(@Body request: LiveStreamRequest): Response<LiveStreamDTO>

    @GET("livestream/list")
    suspend fun getAllLivestreamsPin(
        @Query("fields", encoded = true) fields: String,
    ): Response<BaseResponse<LiveStreamInfoDTO>>

    //-- update a live stream status
    @PUT("livestream")
    suspend fun updateLivestream(
        @Query("fields", encoded = true) id: String,
        @Body update: UpdateLivestreamWrapRequest,
    ): Response<Unit>

    //-- list all livestream OnLive
    @GET("livestream/filter")
    suspend fun getAllLiveStreams(
        @Query("typeTitle", encoded = false) type: String,
        @Query("userId") userId: Int?,
        @Query("limit") limit: Int?,
        @Query("page") page: Int?,
        @Query("startTime") startTime: Long?,
        @Query("endTime") endTime: Long?,
    ): Response<BaseResponse<LiveStreamShortInfoDTO>>

    //-- list all livestream
    @GET("livestream/sweeplist")
    suspend fun getSweepListStreams(
        @Query("type") fields: String,
    ): Response<BaseResponse<LiveStreamShortInfoDTO>>

    @GET("livestream/finishedOfSeller?page=1&limit=20")
    suspend fun getAllFinishedLiveStream(): Response<BaseResponse<LiveSteamShortInfo>>

    @GET("profilelivestream")
    suspend fun getListProfileLiveStream(
        @Query("userId") userId: String?,
        @Query("limit") limit: Int?,
        @Query("page") page: Int?,
    ): Response<BaseResponse<LiveSteamShortInfo>>

    /* get user follow */
    @GET("userfollow")
    suspend fun getUserFollow(
        @Query("type") fields: String? = null,
        @Query("&projection") projection: String?,
    ): Response<BaseResponse<UserFollowDTO>>

    @GET("userFollow/followings")
    suspend fun getUserFollowing(
        @Query("userId") userId: String?,
    ): Response<BaseResponse<UserFollowDTO>>

    @GET("userFollow/followers")
    suspend fun getUserFollower(@Query("userId") userId: Int?): Response<BaseResponse<UserFollowDTO>>

    /* post user follow */
    @POST("userfollow")
    suspend fun postUserFollow(
        @Body userId: UserFollowRequest,
    ): Response<Unit>

    /* post user follow */
    @DELETE("userfollow")
    suspend fun deleteUserFollow(
        @Query("fields", encoded = true) fields: String,
        @Query("&returning") returning: Boolean,
    ): Response<UserFollowModifiedDTO>

    /* Signup */
    @POST("v2/user/register")
    suspend fun sendToken(@Body registerRequest: RegisterRequest): Response<Unit>

    /* SignIn */
    @POST("v2/session")
    suspend fun login(@Body registerRequest: LoginRequest): Response<LoginResponse>

    /* get UserInfo */
    @GET("user/info")
    suspend fun getUserInfo(): Response<UserInfoResponseDTO>

    /* Api Get Verify Code */
    @POST("user/verify-code")
    suspend fun verifyCode(): Unit

    /* Api Verify User */
    @POST("user/verify")
    suspend fun verifyUser(@Body code: CodeRequest): LoginResponse

    @GET("productcategorytree?level=1&depth=2")
    suspend fun getProductCategories(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = LIMIT_PER_PAGE,
    ): Response<BaseResponse<LiveCategoryDTO>>

    @GET("country")
    suspend fun getCountryList(
        @Query("limit") limit: Int = Int.MAX_VALUE,
    ): Response<BaseResponse<CountryDTO>>

    @POST("session")
    suspend fun getSession(
        @Body sessionRequest: SessionRequest,
    ): Response<LoginResponse>

    @GET("businesstype")
    suspend fun getBusinessTypes(
        @Query("limit") limit: Int = Int.MAX_VALUE,
    ): Response<BaseResponse<BusinessTypeDTO>>

    @POST("seller/verification1/step1")
    suspend fun submitVerification1Step1(
        @Body verification1Step1Request: Verification1Step1Request,
    ): Response<Verification1Step1Response>

    @PUT("seller/verification1/step1")
    suspend fun submitUpdateVerification1Step1(
        @Query("businessId", encoded = true) extension: Int,
        @Body verification1Step1Request: Verification1Step1Request,
    ): Response<Unit>

    @POST("seller/verification1/step2")
    suspend fun submitVerification1Step2(
        @Query("businessId", encoded = true) extension: Int,
        @Body verification1Step2Request: Verification1Step2Request,
    ): Response<Unit>

    @PUT("seller/verification1/step2")
    suspend fun submitUpdateVerification1Step2(
        @Query("businessId", encoded = true) extension: Int,
        @Body verification1Step2Request: Verification1Step2Request,
    ): Response<Unit>

    @POST("seller/verification1/step3")
    suspend fun submitVerification1Step3(
        @Query("businessId", encoded = true) extension: Int,
        @Body verification1Step3Request: Verification1Step3Request,
    ): Response<Unit>

    @PUT("seller/verification1/step3")
    suspend fun submitUpdateVerification1Step3(
        @Query("businessId", encoded = true) extension: Int,
        @Body verification1Step3Request: Verification1Step3Request,
    ): Response<Unit>

    @POST("seller/verification1/step4")
    suspend fun submitVerification1Step4(
        @Query("businessId", encoded = true) extension: Int,
        @Body verification1Step4Request: Verification1Step4Request,
    ): Response<Unit>

    @PUT("seller/verification1/step4")
    suspend fun submitUpdateVerification1Step4(
        @Query("businessId", encoded = true) extension: Int,
        @Body verification1Step4Request: Verification1Step4Request,
    ): Response<Unit>

    @GET("upload-url")
    suspend fun getUploadUrlInfo(
        @Query("&extension", encoded = true) extension: String,
    ): Response<UploadResponse>

    @Headers(
        "isAuthorizable: false"
    )
    @PUT
    suspend fun uploadFile(
        @Header("Content-Type") contentType: String,
        @Url urlUpload: String,
        @Body file: RequestBody,
    ): Response<Unit>

    @GET("bank")
    suspend fun getBankListSIV(
        @Query("fields", encoded = true) fields: String,
    ): Response<BaseResponse<BankDTO>>

    @GET("store/name-validation")
    suspend fun checkStoreName(
        @Query("storeName", encoded = true) storeName: String,
    ): Response<Unit>

    @GET("business/owned")
    suspend fun getBusinessOwned(): Response<BusinessOwnResponse>

    @GET("store/main")
    suspend fun getStoreMain(): Response<StoreOwnerResponse?>

    @GET("store")
    suspend fun getStore(
        @Query(
            "fields",
            encoded = true
        ) fields: String,
    ): Response<StoreOwnerResponse>

    @PUT("store")
    suspend fun updateStoreBusinessOwned(
        @Query("fields", encoded = true) fields: String,
        @Query("&returning") returning: Boolean,
        @Body update: StoreBusinessRequest,
    ): Response<BusinessStoreResponse>

    @GET("business/verification")
    suspend fun getVerificationInfo(
        @Query("businessId", encoded = true) businessId: Int,
    ): Response<VerificationDTOResponse>

    @POST("seller/verification2/step1")
    suspend fun submitVerification2Step1(
        @Query("businessId", encoded = true) extension: Int,
        @Body verification2Step1Request: Verification2Step1Request,
    ): Response<Unit>

    /* profile */
    @GET("user/list")
    suspend fun getProfile(
        @Query(
            "fields",
            encoded = true
        ) fields: String,
        @Query("&projection") projections: String?,
    ): Response<BaseResponse<ProfileInfoDTO>>

    @PUT("user")
    suspend fun updateProfiles(
        @Query("fields", encoded = true) fields: String,
        @Query("&returning") returning: Boolean,
        @Body update: ProfileBodyRequest,
    ): Response<ProfileInfoUpdateResponse>

    @GET("businessaddress")
    suspend fun getBusinessAddress(
        @Query(
            "fields",
            encoded = true
        ) fields: String,
    ): Response<BaseResponse<AddressDTO>>

    @PUT("store")
    suspend fun updateBusinessAddress(
        @Query("fields", encoded = true) fields: String,
        @Query("&returning") returning: Boolean,
        @Body update: BusinessAddressRequest,
    ): Response<BusinessAddressUpdateResponse>

    @GET("usersetting")
    suspend fun getUserSetting(
        @Query("fields", encoded = true) fields: String,
        @Query("&projection") projections: String,
    ): Response<BaseResponse<UserSettingDTO>>

    @PUT("usersetting")
    suspend fun updateUserSetting(
        @Query("fields", encoded = true) fields: String,
        @Query("&returning") returning: Boolean,
        @Body update: UserSettingRequestDTO,
    ): Response<UserSettingModifiedResponseDTO>

    @GET("usermessageshortcut")
    suspend fun getUserMessageShortcut(
        @Query("fields", encoded = true) fields: String,
        @Query("&projection") projections: String,
    ): Response<BaseResponse<UserMessageShortcutDTO>>

    @POST("usermessageshortcut")
    suspend fun createUserMessageShortcut(
        @Body body: PostRequest<List<UserMessageShortcutDTO>>,
    ): Response<BaseResponse<UserMessageShortcutDTO>>

    @PUT("usermessageshortcut")
    suspend fun updateUserMessageShortcut(
        @Body body: UpdateRequest<UserMessageShortcutDTO>,
        @Query("fields", encoded = true) fields: String,
        @Query("&returning") returning: Boolean,
    ): Response<BaseResponse<UserMessageShortcutDTO>>

    @GET("notification")
    suspend fun getNotification(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("userId") userId: Int?,
    ): Response<BaseResponse<NotificationDTOModel>>

    @GET("channels")
    suspend fun getConversations(
        @Query("fields", encoded = true) fields: String,
    ): Response<BaseResponse<ConversationInfoDTOModel>>

    //LiveHome
    @GET("productcategorytree?level=1&depth=1")
    suspend fun getCategories(): Response<BaseResponse<LiveCategoryDTO>>

    @GET("livestream/home")
    suspend fun getSections(
        @Query("isMySelf") isMySelf: Int?,
        @Query("userId") userId: Int?,
    ): Response<BaseResponse<HomeLiveSectionDTO>>

    @GET("livestream/filter")
    suspend fun getLivestreamFilter(
        @Query("typeTitle") typeTitle: String?,
        @Query("startTime") startTime: Long?,
        @Query("endTime") endTime: Long?,
        @Query("title") title: String?,
        @Query("limit") limit: Int?,
        @Query("categoryId") categoryId: Int?,
        @Query("isMySelf") isMySelf: Int?,
        @Query("page") page: Int?,
    ): Response<BaseResponse<LiveStreamShortInfoDTO>>

    @GET("hashtag")
    suspend fun getTrendingHashtags(): Response<BaseResponse<HashTagDTO>>

    @GET("https://dev-chat-api.olmowellness.com/categories")
    suspend fun getLinkSharing(): Response<BaseResponse<LiveCategoryDTO>>

    @POST("https://dev-chat-api.olmowellness.com/categories")
    suspend fun postSharing(): Response<BaseResponse<LiveCategoryDTO>>

    @GET("useraddress")
    suspend fun getBuyerAddress(
        @Query(
            "fields",
            encoded = true
        ) fields: String,
    ): Response<BaseResponse<AddressDTO>>

    @PUT("useraddress")
    suspend fun updateBuyerAddress(
        @Query("fields", encoded = true) fields: String,
        @Query("returning") returning: Boolean,
        @Body update: BusinessAddressRequest,
    ): Response<BusinessAddressUpdateResponse>

    @POST("useraddress")
    suspend fun postBuyerAddress(
        @Query("fields", encoded = true) fields: String,
        @Query("returning") returning: Boolean,
        @Body update: PostRequest<List<Address>>,
    ): Response<PostRequest<List<AddressDTO>>>

    @POST("userfollowlivestream")
    suspend fun postUserFollowLiveStream(
        @Body body: PostRequest<List<UserFollowDTO>>,
    ): Response<PostRequest<List<UserFollowDTO>>>

    @DELETE("userfollowlivestream")
    suspend fun deleteUserFollowLiveStream(
        @Query("fields", encoded = true) fields: String,
    ): Response<ModifiedResponse<List<UserFollowDTO>>>

    @POST("livestream/share")
    suspend fun postShareOnProfile(@Body liveId: BodyProfileRequest): Response<Boolean>

    @GET("sharelivestreams")
    suspend fun getShareOnProfile(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<BaseResponse<LiveStreamShortInfoDTO>>

    /* Report */
    @GET("userreportlivestream")
    suspend fun getUserReport(@Query("fields") fields: String): Response<BaseResponse<ReportLiveStreamDTO>>

    @POST("userreportlivestream")
    suspend fun postUserReport(@Body bodyRequest: PostRequest<List<ReportLiveStreamRequestDTO>>): Response<BaseResponse<LiveStreamShortInfoDTO>>

    @PUT("userreportlivestream")
    suspend fun updateUserReport(
        @Query("fields") fields: String,
        @Body bodyRequest: UpdateRequest<ReportLiveStreamRequestDTO>,
    ): Response<BaseResponse<LiveStreamShortInfoDTO>>

    @GET("tippackage")
    suspend fun getTipsPackage(
        @Query("fields") fields: String?,
        @Query("projection") projection: String?,
        @Query("limit") limit: Int?,
        @Query("page") page: Int?,
    ): Response<BaseResponse<TipsPackageOptionDTO>>

    @GET("packageoption")
    suspend fun getPackageOptions(
        @Query("fields") fields: String?,
        @Query("projection") projection: String?,
        @Query("limit") limit: Int?,
        @Query("page") page: Int?,
    ): Response<BaseResponse<PackageOptionDTO>>

    @GET("total-coin")
    suspend fun getTotalCoin(): Response<CoinDTO>

    @GET("userbankaccount/list")
    suspend fun getUserBankAccounts(): Response<BaseResponse<BankDTO>>

    @GET("packageoption/get-price")
    suspend fun getPackageOptionPrice(@Query("id") id: Int): Response<PricePackageDTO>

    @POST("payment/create-request")
    suspend fun createRequestOrder(@Body requestBody: OrderRequestBody): Response<OrderPaymentResponseDTO>

    @GET("service")
    suspend fun getServices(
        @Query("fields") fields: String?,
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = LIMIT_PER_PAGE,
        @Query("page") page: Int?,
    ): Response<BaseResponse<ServiceDTO>>

    @GET("serviceLivestream/list")
    suspend fun getServicesLiveStream(@Query("livestreamId") liveStreamId: Int?): Response<BaseResponse<ServiceDTO>>

    @DELETE("serviceLivestream/delete")
    suspend fun deleteServiceLivestream(
        @Query("livestreamId") liveStreamId: Int?,
        @Query("userId") userId: Int?,
        @Query("serviceId") serviceId: Int?,
    ): Response<String>

    @GET("voucher/list")
    suspend fun getListVoucher(
        @Query("storeId") storeId: Int? = null,
        @Query("serviceId") serviceId: Int? = null,
    ): Response<BaseExceptionResponse<VoucherModelDTO>>

    @GET("voucher/validate")
    suspend fun getValidateVoucher(
        @Query("voucherCode") voucherCode: String? = null,
        @Query("sessionConfirmId") sessionConfirmId: Double? = null,
    ): Response<VoucherValidateModelDTO>

    @GET("booking/buyer")
    suspend fun getListBookingBuyer(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("bookingTitle") bookingTitle: String? = null,
        @Query("bookingId") bookingId: Int? = null,
    ): Response<BaseResponse<BookingHistoryDTO>>

    @GET("booking/buyer")
    suspend fun getListBookingSeller(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("bookingTitle") bookingTitle: String? = null,
        @Query("bookingId") bookingId: Int? = null,
    ): Response<BaseResponse<BookingHistoryDTO>>

    @POST("usercart")
    suspend fun addToCart(@Body rqt: PostRequest<List<RequestAddCart>>): Response<String>

    @DELETE("usercart")
    suspend fun deleteToCart(
        @Query("returning") returning: Boolean,
        @Query("fields", encoded = true
        ) fields: String?,
    ): Response<String>

    @GET("usercart")
    suspend fun getUserCart(
        @Query("fields", encoded = true
        ) fields: String?,
        @Query("page") page: Int, @Query("limit") limit: Int = LIMIT_PER_PAGE,
    ): Response<BaseResponse<CartBookingDTO>>

    @GET("booking/info")
    suspend fun getBookingDetail(
        @Query("id") id: Int?,
    ): Response<BaseResponse<BookingHistoryDTO>>

    @POST("booking")
    suspend fun createBookingId(
        @Body requestBody: BookingRequestModelDTO,
    ): Response<BookingIdResponseDTO>

    @GET("service/public/{id}")
    suspend fun getServicePublicById(
        @Path("id") id: Int?,
    ): Response<ServiceForCartDTO>

    @GET("servicelocation")
    suspend fun getServiceLocation(
        @Query("fields") fields: String,
    ): Response<BaseResponse<ServiceLocationDTO>>

    @GET("servicecalendar")
    suspend fun getServiceCalendar(
        @Query("fromDate") fromDate: Long,
        @Query("toDate") toDate: Long,
        @Query("serviceId") serviceId: Int,
    ): Response<DatePriceWrapDTO>

    @GET("servicesessionofdate")
    suspend fun getServiceSessionByDate(
        @Query("timestamp") fromDate: Long,
        @Query("serviceId") serviceId: Int,
        @Query("id") id: Double,
        @Query("typeSession") typeSession: String,
    ): Response<TimeBookingDTO>

    @POST("servicesessionconfirm")
    suspend fun postServiceSessionConfirm(
        @Body bodyRequest: ModifiedServiceResponse<List<ServiceSessionInfoDTO>>,
    ): Response<ServiceSessionConfirmDTO>

    @PUT("booking/request-cancel")
    suspend fun requestCancelBooking(
        @Body bodyRequest: RequestCancelBooking,
    ): Response<Unit>

    @PUT("booking/reschedule")
    suspend fun requestRescheduleBooking(
        @Body bodyRequest: RequestCancelBooking,
    ): Response<Unit>

    /* request auth */
    @POST("user/auth")
    suspend fun createRequestUserAuth(@Body requestBody: UserAuthRequest): Response<Unit>

    @POST("user/auth/verify")
    suspend fun verifyRequestUserAuth(@Body requestBody: UserAuthRequest): Response<Unit>

    @POST("user/request-to-delete")
    suspend fun requestToDelete(@Body rqt: DeleteAccountRequest): Response<Any?>
}