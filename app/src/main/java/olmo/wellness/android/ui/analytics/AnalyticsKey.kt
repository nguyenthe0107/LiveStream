package olmo.wellness.android.ui.analytics

enum class AnalyticsKey(val key: String) {
     ActionSignInSuccessPage("Signin_Page")
    ,ActionSignIn("Signin")
    ,ActionSignUpPage("Signup_Page")
    ,ActionClickSignUpGoogleFirebase("Click_signup_google")
    ,ActionClickSignUpPhoneFirebase("Click_signup_phone")
    ,ActionClickSignUpEmailFirebase("Click_signup_email")
    ,ActionClickSignUpFirebase("Click_signup")
    ,ActionRequestOTPFirebase("Request_OTP")
    ,ActionResendOTPFirebase("Resend_OTP")
    ,ActionSubmitOTPFirebase("Submit_OTP")
    ,ActionHomeLoadFirebase("Home_load")
    ,ActionHomeLoadSuccessFirebase("Home_load_success")
    ,ActionClickForgerPassFirebase("Click_forget_pass")
    ,ActionClickSendForgotFirebase("Click_send")
    ,ActionClickCategories2Firebase("Click_cat2")
    ,ActionClickCategories3Firebase("Click_cat3")
    ,ActionClickCategoriesContentPageFirebase("Cat3_content_page")
    ,ActionClickSeeAllSectionFirebase("Click_seeall_group_content")
    ,ActionClickSubmitNewPassFirebase("Click_submit_new_pass")
    ,ActionLoadGroupContentPageFirebase("Group_content_page")
    ,ActionLiveStartFirebase("livestream_start")
    ,ActionClickLikeFirebase("click_like")
    ,ActionClickFollowFirebase("click_follow")
    ,ActionSendCommentFirebase("send_comment")
    ,ActionSendReportFirebase("send_report")
    ,ActionSendMessageFirebase("send_message")
    ,ActionShareProfileFirebase("share_profile")
    ,ActionShareSocialFirebase("share_social")
    ,ActionClickViewFirebase("click_views")
    ,ActionClickCloseViewFirebase("click_close")
    ,ActionClickMiniSizeFirebase("click_minimize")
    ,ActionSwipeFirebase("swipe")
    ,ActionLiveStreamEndFirebase("livestream_end")
    ;

    companion object {
        fun getByValue(key: String) = values().find { it.key == key }
    }

}