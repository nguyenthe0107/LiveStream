package olmo.wellness.android.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.*
import olmo.wellness.android.ui.livestream.stream.data.LivestreamKeyResponse
import olmo.wellness.android.data.model.RegisterRequest
import olmo.wellness.android.data.model.booking.RequestAddCart
import olmo.wellness.android.data.model.booking.RequestCancelBooking
import olmo.wellness.android.data.model.business.StoreBusinessRequest
import olmo.wellness.android.data.model.delete_account.DeleteAccountRequest
import olmo.wellness.android.data.model.live_stream.LiveStreamRequest
import olmo.wellness.android.data.model.order.OrderRequestBody
import olmo.wellness.android.data.model.profile.BodyProfileRequest
import olmo.wellness.android.data.model.profile.update.ProfileBodyRequest
import olmo.wellness.android.data.model.report_livestream.ReportLiveStreamRequestDTO
import olmo.wellness.android.data.model.user_auth.UserAuthRequest
import olmo.wellness.android.data.model.verify_code.VerifyCodeRequest
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
import olmo.wellness.android.domain.model.user_setting.UserSetting
import olmo.wellness.android.domain.model.user_setting.UserSettingRequest
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.domain.model.verification1.step1.Address
import olmo.wellness.android.domain.model.verification1.step1.BusinessAddressRequest
import olmo.wellness.android.domain.model.verification1.step1.Step1Request
import olmo.wellness.android.domain.model.verification1.step2.Step2Request
import olmo.wellness.android.domain.model.verification1.step4.Step4Request
import olmo.wellness.android.domain.model.verification1.step3.Step3Request
import olmo.wellness.android.domain.model.verification2.V2Step1Request
import olmo.wellness.android.domain.model.voucher.VoucherInfo
import olmo.wellness.android.domain.model.voucher.VoucherValidateInfo
import olmo.wellness.android.domain.tips.CoinInfo
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.domain.tips.PricePackageInfo
import olmo.wellness.android.domain.tips.TipsPackageOptionInfo
import olmo.wellness.android.ui.livestream.stream.data.UpdateLivestreamWrapRequest

interface ApiRepository {
    suspend fun register(registerRequest: RegisterRequest): Flow<Result<Boolean>>
    suspend fun login(loginRequest: LoginRequest): Flow<Result<LoginData>>
    suspend fun verifyCode(): Flow<Unit>
    suspend fun verifyUser(code: CodeRequest): Flow<Unit>
    suspend fun getUserInfo(): Flow<Result<UserInfoResponse>>

    /* V2 */
    suspend fun requestVerifyCode(bodyRequest: VerifyCodeRequest): Flow<Unit>
    suspend fun verifyConfirmCode(bodyRequest: VerifyCodeRequest): Flow<Result<Boolean>>
    suspend fun forgetPassword(bodyRequest: VerifyCodeRequest): Flow<Unit>
    suspend fun resetPassword(bodyRequest: VerifyCodeRequest): Flow<Unit>

    /* LiveStream */
    suspend fun getLivestreamKey(request : LiveStreamRequest): Flow<Result<LivestreamKeyResponse>>
    suspend fun listLivestream(query: String, projection : String?): Flow<Result<List<LivestreamInfo>>>
    suspend fun deleteLivestream(query: String, returning: Boolean?): Flow<Result<Boolean>>
    suspend fun updateStatusLivestream(query: String,update: UpdateLivestreamWrapRequest): Flow<Result<Boolean>>
    suspend fun getAllLiveStreams(typeTitle: String, userId: Int?,
                                  limit : Int?=null, page: Int?=null,
                                  startTime : Long?=null,endTime : Long?=null) : Flow<Result<List<LiveSteamShortInfo>>>
    suspend fun getSweepListStreams(query: String) : Flow<Result<List<LiveSteamShortInfo>>>
    suspend fun getUserFollow(query: String?=null,projection: String?) : Flow<Result<List<UserFollowInfo>>>
    suspend fun postUserFollow(query: UserFollowRequest) : Flow<Result<Boolean>>
    suspend fun deleteUserFollow(query: String) : Flow<Result<List<UserFollowInfo>>>
    suspend fun getAllSellerFinishedStreams() : Flow<Result<List<LiveSteamShortInfo>>>
    suspend fun getListProfileLiveStream(userId: Int?,limit : Int?=null, page: Int?=null) : Flow<Result<List<LiveSteamShortInfo>>>
    suspend fun getUserFollowing(userId: Int?) : Flow<Result<List<UserFollowInfo>>>
    suspend fun getUserFollower(userId: Int?) : Flow<Result<List<UserFollowInfo>>>
    suspend fun getLivestreamPin(query: String) : Flow<Result<List<LivestreamInfo>>>

    fun getProductCategories(page: Int): Flow<Result<List<LiveCategory>>>
    fun getCountryList(): Flow<Result<List<Country>>>
    suspend fun getSession(refreshToken: String): LoginData?
    fun getBusinessType(): Flow<Result<List<BusinessType>>>
    fun submitVerification1Step1(step1Request: Step1Request): Flow<Result<Int>>
    fun submitUpdateVerification1Step1(businessId: Int,step1Request: Step1Request): Flow<Result<Boolean>>
    fun submitVerification1Step2(businessId: Int, step2Request: Step2Request): Flow<Result<Boolean>>
    fun submitUpdateVerification1Step2(businessId: Int, step2Request: Step2Request): Flow<Result<Boolean>>
    fun submitVerification1Step3(businessId: Int, step3Request: Step3Request): Flow<Result<Boolean>>
    fun submitUpdateVerification1Step3(businessId: Int, step3Request: Step3Request): Flow<Result<Boolean>>
    fun submitVerification1Step4(businessId: Int, step4Request: Step4Request): Flow<Result<Boolean>>
    fun submitUpdateVerification1Step4(businessId: Int, step4Request: Step4Request): Flow<Result<Boolean>>
    fun getUploadUrlInfo(mimeType: String): Flow<Result<UploadUrlInfo>>
    fun uploadFile(url: String, fileUri: Uri): Flow<Result<Boolean>>
    fun getBankList(countryIds: List<Int>): Flow<Result<List<BankInfo>>>
    fun checkStoreName(storeName: String): Flow<Result<Boolean>>
    fun getBusinessOwned(): Flow<Result<List<BusinessOwn>>>
    fun getStoreMain() : Flow<Result<StoreOwnerResponseDM>>
    fun getStore(query: String) : Flow<Result<StoreOwnerResponseDM>>
    fun updateStoreBusinessOwned(fields: String,isReturn: Boolean, update: StoreBusinessRequest) : Flow<Result<List<BusinessOwn>>>
    fun getVerificationInfo(businessId: Int): Flow<Result<VerificationData>>
    fun submitVerification2Step1(businessId: Int, step1Request: V2Step1Request): Flow<Result<Boolean>>
    /* Current ignore 2 api below */
    fun getProfile(userId: Int?, projection: String?): Flow<Result<List<ProfileInfo>>>
    fun updateProfiles(listQuery: String, isReturn: Boolean, updateBody: ProfileBodyRequest): Flow<Result<List<ProfileInfo>>>

    /* Seller */
    fun getBusinessAddress(userId: Int): Flow<Result<List<Address>>>
    fun updateBusinessAddress(queries: String,isReturn: Boolean, updateBody: BusinessAddressRequest): Flow<Result<List<Address>>>

    /* Buyer */
    fun getBuyerAddress(userId: Int): Flow<Result<List<Address>>>
    fun updateBuyerAddress(queries: String,isReturn: Boolean, updateBody: BusinessAddressRequest): Flow<Result<List<Address>>>
    fun postBuyerAddress(queries: String,isReturn: Boolean, updateBody: Address): Flow<Result<List<Address>>>

    fun getUserSetting(userId: Int,queries: String): Flow<Result<List<UserSetting>>>
    fun updateUserSetting(queries: String,isReturn: Boolean, updateBody: UserSettingRequest): Flow<Result<List<UserSetting>>>
    fun getUserMessageShortcut(userId: Int, queries: String): Flow<Result<List<UserMessageShortcut>>>
    fun createUserMessageShortcut(messageShortcut: String): Flow<Result<List<UserMessageShortcut>>>
    fun updateUserMessageShortcut(
        messageShortcut: UserMessageShortcut,
        fields: String,
        returning: Boolean
    ): Flow<Result<List<UserMessageShortcut>>>
    fun getNotification(page : Int,limit : Int, userId: Int?) : Flow<Result<List<NotificationInfo>>>
    fun getConversations(queries: String): Flow<Result<List<ConversationInfo>>>

    //LiveHome
    fun getCategories(): Flow<Result<List<LiveCategory>>>
    fun getSections(isMySelf: Int?, userId: Int?): Flow<Result<List<HomeLiveSectionData>>>
    fun getLivestreamFilter(typeTitle : String?,startTime : Long?,endTime : Long?,categoryId : Int?,
                            title: String?,page : Int?,limit : Int?, userId: Int?, isMySelf:Boolean?) : Flow<Result<List<LiveSteamShortInfo>>>
    fun getTrendingHashtags(): Flow<Result<List<HashTag>>>

    /* userFollow */
    fun postUserFollowLiveStream(bodyRequest: UserFollowInfo) : Flow<Result<List<UserFollowInfo>>>
    fun deleteUserFollowLiveStream(query: String) : Flow<Result<List<UserFollowInfo>>>

    /* Share On Profile */
    fun postShareOnProfile(bodyRequest: BodyProfileRequest) : Flow<Result<Boolean>>
    fun getShareOnProfile(page: Int, limit: Int) : Flow<Result<List<LiveSteamShortInfo>>>

    /* user report */
    fun postUserReport(bodyRequest : PostRequest<List<ReportLiveStreamRequestDTO>>): Flow<Result<Boolean>>
    fun getUserReport(query : String): Flow<Result<List<ReportLiveStreamInfo>>>

    /* Tips and Donate */
    fun getTipPackagesOptions(query: String?, projection: String?, page: Int?, limit: Int?): Flow<Result<List<TipsPackageOptionInfo>>>
    fun getPackagesOptions(query: String?, projection: String?, page: Int?, limit: Int?): Flow<Result<List<PackageOptionInfo>>>
    /* Payment */
    fun getTotalCoin(): Flow<Result<CoinInfo>>
    fun getPricePackage(idPackage: Int): Flow<Result<PricePackageInfo>>
    fun createOrder(bodyRequest: OrderRequestBody): Flow<Result<OrderPaymentResponse>>
    fun getUserBankAccount() : Flow<Result<List<BankInfo>>>

    /*Booking */
    fun getService(fields : String?,search : String?,page: Int?): Flow<Result<List<ServiceBooking>>>
    fun getServiceLivestream(livestreamId : Int?): Flow<Result<List<ServiceBooking>>>
    fun getListVoucher(livestreamId : Int?): Flow<Result<List<VoucherInfo>>>
    fun getValidateVoucher(voucherCode : String?, sessionConfirmId: Double?): Flow<Result<VoucherValidateInfo>>
    fun deleteServiceLivestream(userId : Int?, livestreamId: Int?,serviceId: Int?) : Flow<Result<String>>
    fun getListBookingBuyer(bookingTitle: String?=null, bookingId: Int ?=null,  page: Int?, limit: Int?): Flow<Result<List<BookingHistoryInfo>>>
    fun getListBookingSeller(bookingTitle: String?=null, bookingId: Int ?=null, page: Int?, limit: Int?): Flow<Result<List<BookingHistoryInfo>>>
    fun getBookingDetail(id: Int?): Flow<Result<List<BookingHistoryInfo>>>
    fun getServicePublicById(id: Int?) : Flow<Result<ServiceBookingForCart>>
    fun getServiceLocation(query: String?) : Flow<Result<List<ServiceLocationInfo>>>
    fun getServiceCalendar(fromDate: Long, toDate: Long, serviceId: Int) : Flow<Result<List<DatePriceInfo>>>
    fun getServiceSessionByDate(fromDate: Long, serviceId: Int,id: Double, typeSession: String) : Flow<Result<TimeBookingInfo>>
    fun postServiceSessionConfirm(bodyRequest: List<ServiceSessionInfo>) : Flow<Result<ServiceSessionConfirmInfo>>
    fun createBookingId(body: BookingRequestInfo) : Flow<Result<BookingIdResponseInfo>>
    fun requestDeleteServiceSessionConfirm(bodyRequest: RequestCancelBooking) : Flow<Result<Boolean>>
    fun requestRescheduleServiceSessionConfirm(bodyRequest: RequestCancelBooking) : Flow<Result<Boolean>>

    //Cart
    fun addToCart(rqt : PostRequest<List<RequestAddCart>>) : Flow<Result<String>>
    fun deleteToCart(returning: Boolean,fields: String?) : Flow<Result<String>>
    fun getUserCart(fields: String?, page: Int) : Flow<Result<List<CartBooking>>>

    /* create user auth */
    suspend fun createRequestUserAuth(bodyRequest: UserAuthRequest): Flow<Result<Boolean>>
    suspend fun verifyRequestUserAuth(bodyRequest: UserAuthRequest): Flow<Result<Boolean>>

    fun requestToDelete(rqt : DeleteAccountRequest) : Flow<Result<String>>
}