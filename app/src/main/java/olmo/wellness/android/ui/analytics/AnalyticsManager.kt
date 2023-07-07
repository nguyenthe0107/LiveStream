package olmo.wellness.android.ui.analytics

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.ui.analytics.TrackingConstants.VALUE_FAILED
import olmo.wellness.android.ui.analytics.TrackingConstants.VALUE_SUCCESS

class AnalyticsManager {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var mInstance: AnalyticsManager? = null
        fun getInstance(): AnalyticsManager? {
            if (mInstance == null) throw NullPointerException("Analytics controller is null!")
            return mInstance
        }

        @JvmStatic
        @Synchronized
        fun initialize(context: Context) {
            if (mInstance == null) {
                mInstance = AnalyticsManager().apply {
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                    mFirebaseAnalytics.let {
                        it?.setAnalyticsCollectionEnabled(true)
                        it?.setSessionTimeoutDuration(1000000)
                    }
                }
            }
        }
    }

    private fun sendEventFirebase(event: AnalyticsKey, bundleData: Bundle?) {
        mFirebaseAnalytics?.logEvent(event.key, bundleData)
    }

    private fun sendEventFirebaseWithoutParam(event: AnalyticsKey) {
        val bundleData = Bundle()
        mFirebaseAnalytics?.logEvent(event.key, bundleData)
    }

    fun trackingSignIn(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        if(trackingModel.isSuccess == true){
            bundle.putString(TrackingConstants.TRACKING_STATUS, VALUE_SUCCESS)
            bundle.putString(TrackingConstants.TRACKING_ACCOUNT_NAME, trackingModel.account_name)
        }else{
            bundle.putString(TrackingConstants.TRACKING_STATUS, VALUE_FAILED)
            bundle.putString(TrackingConstants.TRACKING_ERROR_MES,trackingModel.err_message)
        }
        sendEventFirebase(AnalyticsKey.ActionSignIn, bundle)
    }

    fun trackingSignInSuccess() {
        val bundle = Bundle()
        sendEventFirebase(AnalyticsKey.ActionSignInSuccessPage, bundle)
    }

    fun trackingSignUpByGoogle() {
        val bundle = Bundle()
        sendEventFirebase(AnalyticsKey.ActionClickSignUpGoogleFirebase, bundle)
    }

    fun trackingSignUpByPhone() {
        val bundle = Bundle()
        sendEventFirebase(AnalyticsKey.ActionClickSignUpPhoneFirebase, bundle)
    }

    fun trackingSignUpByEmail() {
        val bundle = Bundle()
        sendEventFirebase(AnalyticsKey.ActionClickSignUpEmailFirebase, bundle)
    }

    fun trackingSignUpSuccess() {
        val bundle = Bundle()
        sendEventFirebase(AnalyticsKey.ActionSignUpPage, bundle)
    }

    private fun getGeneralBundle(trackingModel: TrackingModel) : Bundle{
        val bundle = Bundle()
        val userId = trackingModel.user_id ?: 0
        bundle.putInt(TrackingConstants.TRACKING_USER_ID,userId)
        bundle.putString(TrackingConstants.TRACKING_OS_DEVICE, TrackingConstants.OS_DEVICE)
        bundle.putString(TrackingConstants.TRACKING_OS_VERSION, trackingModel.os_ver)
        if(trackingModel.isSuccess == true){
            bundle.putString(TrackingConstants.TRACKING_STATUS, VALUE_SUCCESS)
        }else{
            bundle.putString(TrackingConstants.TRACKING_STATUS, VALUE_FAILED)
            bundle.putString(TrackingConstants.TRACKING_ERROR_MES,trackingModel.err_message)
        }
        return bundle
    }

    fun trackingWhenClickSignUp(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putString(TrackingConstants.TRACKING_CATEGORY_ID, trackingModel.category_id.toString())
        sendEventFirebase(AnalyticsKey.ActionClickSignUpFirebase, bundle)
    }

    fun trackingRequestOTP(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        sendEventFirebase(AnalyticsKey.ActionRequestOTPFirebase, bundle)
    }

    fun trackingResendOTP(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        sendEventFirebase(AnalyticsKey.ActionResendOTPFirebase, bundle)
    }

    fun trackingConfirmedOTP(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        sendEventFirebase(AnalyticsKey.ActionSubmitOTPFirebase, bundle)
    }

    fun trackingClickForgetPassword() {
        sendEventFirebaseWithoutParam(AnalyticsKey.ActionClickForgerPassFirebase)
    }

    fun trackingClickSendForgetPassword(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        sendEventFirebase(AnalyticsKey.ActionClickSendForgotFirebase, bundle)
    }

    fun trackingClickSubmitNewPass(trackingModel: TrackingModel){
        val bundle = getGeneralBundle(trackingModel)
        sendEventFirebase(AnalyticsKey.ActionClickSubmitNewPassFirebase, bundle)
    }

    fun trackingHomeLoad() {
        sendEventFirebaseWithoutParam(AnalyticsKey.ActionHomeLoadFirebase)
    }

    fun trackingHomeLoadSuccess(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        if(trackingModel.isSuccess == true){
            bundle.putString(TrackingConstants.TRACKING_STATUS, VALUE_SUCCESS)
        }else{
            bundle.putString(TrackingConstants.TRACKING_STATUS, VALUE_FAILED)
            bundle.putString(TrackingConstants.TRACKING_ERROR_MES,trackingModel.err_message)
        }
        sendEventFirebaseWithoutParam(AnalyticsKey.ActionHomeLoadSuccessFirebase)
    }

    fun trackingCategory2(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE2_ID, trackingModel.cat2_id ?: 0)
        sendEventFirebase(AnalyticsKey.ActionClickCategories2Firebase, bundle)
    }

    fun trackingCategory3(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id ?: 0)
        sendEventFirebase(AnalyticsKey.ActionClickCategories3Firebase, bundle)
    }

    fun trackingContentCategory3(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putString(TrackingConstants.TRACKING_CONTENT_CATEGORY3, trackingModel.group_content)
        sendEventFirebase(AnalyticsKey.ActionClickCategoriesContentPageFirebase, bundle)
    }

    fun trackingClickSeeAllGroupContent(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putString(TrackingConstants.TRACKING_CONTENT_CATEGORY3, trackingModel.group_content)
        sendEventFirebase(AnalyticsKey.ActionClickSeeAllSectionFirebase, bundle)
    }

    fun trackingLoadGroupContent(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putString(TrackingConstants.TRACKING_CONTENT_CATEGORY3, trackingModel.group_content)
        sendEventFirebase(AnalyticsKey.ActionLoadGroupContentPageFirebase, bundle)
    }

    fun trackingLiveStreamStartContent(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putString(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.categories_livestream.toString())
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionLiveStartFirebase, bundle)
    }

    fun trackingClickLiveStream(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionClickLikeFirebase, bundle)
    }

    fun trackingClickFollowStream(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionClickFollowFirebase, bundle)
    }

    fun trackingSendCommentStream(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionSendCommentFirebase, bundle)
    }

    fun trackingSendReportStream(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionSendReportFirebase, bundle)
    }

    fun trackingSendMessageSellerStream(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionSendMessageFirebase, bundle)
    }

    fun trackingShareProfileStream(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionShareProfileFirebase, bundle)
    }

    fun trackingShareSocialStream(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionShareSocialFirebase, bundle)
    }

    fun trackingClickView(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionClickViewFirebase, bundle)
    }

    fun trackingClickClose(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionClickCloseViewFirebase, bundle)
    }

    fun trackingClickMiniSize(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionClickMiniSizeFirebase, bundle)
    }

    fun trackingSwipe(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionSwipeFirebase, bundle)
    }

    fun trackingLiveStreamEnd(trackingModel: TrackingModel) {
        val bundle = getGeneralBundle(trackingModel)
        bundle.putInt(TrackingConstants.TRACKING_CONTENT_CATE3_ID, trackingModel.cat3_id?:0)
        bundle.putInt(TrackingConstants.TRACKING_LIVESTREAM_ID, trackingModel.livestream_id?:0)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_TITLE, trackingModel.livestream_tile)
        bundle.putString(TrackingConstants.TRACKING_LIVESTREAM_SELLER_NAME, trackingModel.seller_name)
        sendEventFirebase(AnalyticsKey.ActionLiveStreamEndFirebase, bundle)
    }
}