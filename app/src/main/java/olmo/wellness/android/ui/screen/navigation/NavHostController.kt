package olmo.wellness.android.ui.screen.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import olmo.wellness.android.core.Constants.ID
import olmo.wellness.android.core.Constants.KEY_ID
import olmo.wellness.android.core.Constants.KEY_SUMMARY_LIVE_DATA
import olmo.wellness.android.core.Constants.SUMMARY_LIVE_DATA
import olmo.wellness.android.core.fromJson
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.schedule.FillDataSchedule
import olmo.wellness.android.domain.model.booking.BookingHistoryInfo
import olmo.wellness.android.domain.model.booking.WrapperUrlPayment
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.domain.model.user_message.UserMessageShortcut
import olmo.wellness.android.domain.model.wrapper.VerifyCodeWrapper
import olmo.wellness.android.ui.livestream.schedule.view.CreateScheduleLivestreamScreen
import olmo.wellness.android.ui.livestream.statistics.ui.StatisticsLivestreamScreen
import olmo.wellness.android.ui.screen.account_setting.AccountSetting
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.chat.conversation_detail.ConversationDetailScreen
import olmo.wellness.android.ui.screen.account_setting.AccountSwitcher
import olmo.wellness.android.ui.screen.create_password.CreatePasswordScreen
import olmo.wellness.android.ui.screen.forget_password.ForgetPasswordScreen
import olmo.wellness.android.ui.screen.home_screen.HomeScreen
import olmo.wellness.android.ui.screen.account_setting.chat_setting.ChatSettingScreen
import olmo.wellness.android.ui.screen.account_setting.delete.SelectReasonDeleteAccountScreen
import olmo.wellness.android.ui.screen.account_setting.delete.WarningDeleteAccountScreen
import olmo.wellness.android.ui.livestream.view.streamer.LiveStreamerScreen
import olmo.wellness.android.ui.screen.authenticator.AuthenticatorInputCodeScreen
import olmo.wellness.android.ui.screen.account_setting.block_contacts.list_blocked_contacts.BlockContacts
import olmo.wellness.android.ui.screen.account_setting.block_contacts.seach_contacts.BlockContactsSearch
import olmo.wellness.android.ui.screen.account_setting.chat_setting.AutoReplyScreen
import olmo.wellness.android.ui.screen.account_setting.chat_setting.message_shortcut.create.CreateMessageShortcutScreen
import olmo.wellness.android.ui.screen.account_setting.chat_setting.message_shortcut.MessageShortcutScreen
import olmo.wellness.android.ui.screen.account_setting.chat_setting.message_shortcut.detail.DetailMessageShortcutScreen
import olmo.wellness.android.ui.screen.account_setting.choose_language.ChooseLanguage
import olmo.wellness.android.ui.screen.account_setting.contact_sync.ContactSync
import olmo.wellness.android.ui.screen.account_setting.notification_settings.NotificationSetting
import olmo.wellness.android.ui.screen.account_setting.privacy_settings.PrivacySettings
import olmo.wellness.android.ui.screen.account_setting.share_profile.ShareProfile
import olmo.wellness.android.ui.screen.authenticator.AuthenticatorScreen
import olmo.wellness.android.ui.screen.deep_link.DeepLinkScreen
import olmo.wellness.android.ui.screen.identify_success.IdentifySuccessScreen
import olmo.wellness.android.ui.chat.conversation_list.view.ConversationListContentScreen
import olmo.wellness.android.ui.livestream.schedule.view.LiveSchedulingScreen
import olmo.wellness.android.ui.livestream.stream.end_livestream.SummaryLiveStream
import olmo.wellness.android.ui.livestream.view.streamer.PermissionLiveScreen
import olmo.wellness.android.ui.screen.business_hours.BusinessHoursScreen
import olmo.wellness.android.ui.screen.calendar_screen.CalendarScreen
import olmo.wellness.android.ui.screen.event_detail_screen.EventDetailScreen
import olmo.wellness.android.ui.screen.notification.NotificationHome
import olmo.wellness.android.ui.screen.playback_video.on_board.OnBoardLiveScreen
import olmo.wellness.android.ui.screen.playback_video.explore.ExploreLiveStreamScreen
import olmo.wellness.android.ui.screen.playback_video.home.NewsFeedsHomeScreen
import olmo.wellness.android.ui.screen.playback_video.onlive.PlayBackOnLiveStreamScreen
import olmo.wellness.android.ui.screen.profile_dashboard.MyProfileDashBoard
import olmo.wellness.android.ui.screen.profile_dashboard.social_media.SocialMediaScreen
import olmo.wellness.android.ui.screen.profile_setting_screen.ProfileSettingScreen
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.SellerVerificationStep1Screen
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_2.SellerVerificationStep2Screen
import olmo.wellness.android.ui.screen.all_section_home.SeeAllFilterLiveStreamScreen
import olmo.wellness.android.ui.screen.dashboard_booking.MyDashBoardBookingScreen
import olmo.wellness.android.ui.screen.dashboard_booking.details.MyServiceBookingDetails
import olmo.wellness.android.ui.screen.playback_video.donate.ui_main.InfoPackageScreen
import olmo.wellness.android.ui.screen.following_profile.FollowingProfileScreen
import olmo.wellness.android.ui.screen.playback_video.donate.payment_process.PaymentProcessScreen
import olmo.wellness.android.ui.screen.playback_video.donate.view.KeplerBalanceBuyerScreen
import olmo.wellness.android.ui.screen.playback_video.donate.view.KeplerBalanceSellerScreen
import olmo.wellness.android.ui.screen.profile_dashboard.social_media.WebViewSocialMediaScreen
import olmo.wellness.android.ui.screen.see_all_upcoming.SeeAllUpComingScreen
import olmo.wellness.android.ui.screen.seller.SellerHubScreen
import olmo.wellness.android.ui.screen.setting_screen.SettingScreen
import olmo.wellness.android.ui.screen.signin_screen.SignInEmailScreen
import olmo.wellness.android.ui.screen.signin_screen.phone.SignInByPhoneScreen
import olmo.wellness.android.ui.screen.signup_screen.onboard_signup.OnBoardSignupScreen
import olmo.wellness.android.ui.screen.test_compose_screen.TestComposeScreen
import olmo.wellness.android.ui.screen.user_registed.UserRegisteredScreen
import olmo.wellness.android.ui.screen.verification_success.VerificationStep2Success
import olmo.wellness.android.ui.screen.verification_success.VerificationSummarySuccess
import olmo.wellness.android.ui.screen.verify_code_screen.VerifyCodeScreen
import olmo.wellness.android.ui.screen.verify_code_screen.VerifyCodeSuccessScreen
import olmo.wellness.android.ui.sub_categories.screen.SubCategoriesScreen
import java.lang.Exception
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@ExperimentalFoundationApi
@OptIn(
    ExperimentalMaterialApi::class, ExperimentalAnimationApi::class,
    com.google.accompanist.permissions.ExperimentalPermissionsApi::class,
    coil.annotation.ExperimentalCoilApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class
)
@Composable
fun NavHostController(
    haveIntent: Boolean? = false,
    businessIdInput: Int? = 0,
    intentDataStatus: String? = "",
    haveIntentFromNotification: Boolean? = false,
    defaultRoute: String? = null,
    isTokenNotValid: Boolean? = null){
    val navController = rememberNavController()
    val idToken = sharedPrefs.getToken()
    val isLoginSuccess = sharedPrefs.getStatusLogin()
    NavHost(
        navController = navController,
        startDestination = defaultRoute?:
        if (idToken?.isNotEmpty() == true && isLoginSuccess && (isTokenNotValid == null || isTokenNotValid == false) ) {
            when {
                haveIntent == true -> {
                    ScreenName.DeepLinkScreen.route
                }
                haveIntentFromNotification == true -> {
                    ScreenName.NotificationHomeScreen.route
                }
                else -> {
                    ScreenName.OnboardLiveScreen.route
                }
            }
        } else {
            if (haveIntent == true) {
                ScreenName.DeepLinkScreen.route
            } else {
                ScreenName.OnBoardSignUpScreen.route
            }
        }
    ) {
        composable(
            route = ScreenName.DeepLinkScreen.route
        ) {
            DeepLinkScreen(businessIdInput, intentDataStatus, navController)
        }
        composable(
            route = ScreenName.SignInEmailScreen.route + "/{isDeepLink}",
            arguments = listOf(
                navArgument(name = "isDeepLink") {
                    NavType.BoolType
                    defaultValue = false
                }
            ),
        ) { from ->
            val isDeepLink = from.arguments?.getBoolean("isDeepLink") ?: false
            // navigation to sign in screen
            SignInEmailScreen(navController, isDeepLink)
        }
        composable(
            route = ScreenName.SignInByPhoneScreen.route
        ) {
            // navigation to sign in screenNavHostController
            SignInByPhoneScreen(navController)
        }
        composable(route = ScreenName.OnBoardSignUpScreen.route){
            OnBoardSignupScreen(navController)
        }
        composable(route = ScreenName.StatisticsLiveStreamScreen.route) {
            StatisticsLivestreamScreen(navController)
        }
        composable(route = ScreenName.CreateScheduleLiveStreamScreen.route) {
            CreateScheduleLivestreamScreen(navController)
        }
        composable(
            route = ScreenName.VerifyCodeScreen.route.plus("?defaultData={defaultData}")
        ) { from ->
            val verifyInfoJson = (from.arguments?.getString("defaultData") ?: "").trim()
            if(verifyInfoJson.isNotEmpty()){
                val verifyObject = Gson().fromJson(verifyInfoJson, VerifyCodeWrapper::class.java)
                VerifyCodeScreen(navController, verifyObject)
            }
        }
        composable(
            route = ScreenName.VerifyCodeSuccessScreen.route
                .plus("?identity={identity}")
                .plus("&userName={userName}")
                .plus("&userType={userType}"),
            arguments = listOf(
                navArgument(name = "identity") {
                    NavType.StringType
                    defaultValue = ""
                },
                navArgument(name = "userName") {
                    NavType.StringType
                    defaultValue = ""
                },
                navArgument(name = "userType") {
                    NavType.StringType
                    defaultValue = UserTypeModel.BUYER.value
                }
            )
        ) { from ->
            val identity = from.arguments?.getString("identity") ?: ""
            val userName = from.arguments?.getString("userName") ?: ""
            val userType = from.arguments?.getString("userType") ?: UserTypeModel.BUYER.value
            VerifyCodeSuccessScreen(navController, identity, userName, userType)
        }
        composable(
            route = ScreenName.HomeScreen.route
        ) {
            // navigation to home screen
            HomeScreen(navController)
        }
        composable(
            route = ScreenName.ForgetPasswordScreen.route + "?isPhone={isPhone}",
            arguments = listOf(
                navArgument("isPhone") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { from ->
            val isPhone = from.arguments?.getBoolean("isPhone") ?: false
            ForgetPasswordScreen(navController, isPhone)
        }
        composable(
            route = ScreenName.CreatePasswordScreen.route
                .plus("?code={code}&identity={identity}&isPhone={isPhone}"),
            arguments = listOf(
                navArgument("code") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("identity") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("isPhone") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { from ->
            val code = from.arguments?.getString("code") ?: ""
            val identity = from.arguments?.getString("identity") ?: ""
            val isPhone = from.arguments?.getBoolean("isPhone") ?: false
            CreatePasswordScreen(navController, identity, code, isPhone)
        }
        composable(
            route = ScreenName.SellerHubScreen.route
        ) {
            // navigation to choose category screen
            SellerHubScreen(navController)
        }
        composable(
            route = ScreenName.SettingsScreen.route
        ) {
            // navigation to choose category screen
            SettingScreen(navController)
        }
        composable(
            route = ScreenName.ProfileSettingScreen.route
        ) {
            // navigation to choose category screen
            ProfileSettingScreen(navController)
        }
        composable(
            route = ScreenName.SellerVerificationStep1Screen.route
                .plus("?identity={identity}".plus("&businessId={businessId}"))
        ) { from ->
            // Need To ReSearch
            BackHandler(true) {
            }
            val identity = from.arguments?.getString("identity") ?: ""
            val businessId = from.arguments?.getString("businessId")
            // navigation to choose category screen
            SellerVerificationStep1Screen(navController, identity, businessId?.toInt() ?: 0)
        }
        composable(
            route = ScreenName.UserRegistered.route
        ) {
            // navigation to choose category screen
            UserRegisteredScreen()
        }
        composable(
            route = ScreenName.TestComposeScreen.route
        ) {
            // navigation to choose category screen
            TestComposeScreen(navController)
        }
        composable(
            route = ScreenName.AccountSettingScreen.route
        ) {
            AccountSetting(navController)
        }
        composable(
            route = ScreenName.VerificationStep2SuccessScreen.route
        ) {
            VerificationStep2Success(navController)
        }
        composable(
            route = ScreenName.VerificationSummarySuccess.route
        ) {
            VerificationSummarySuccess(navController)
        }
        composable(
            route = ScreenName.SellerVerificationStep2Screen.route
        ) {
            SellerVerificationStep2Screen(navController)
        }

        composable(
            route = ScreenName.IdentifySuccessScreen.route
        ) {
            IdentifySuccessScreen(navController)
        }
        composable(
            route = ScreenName.LivestreamStreamerScreen.route
        ) {
            LiveStreamerScreen(navController)
        }
        composable(
            route = ScreenName.PermissionLivestreamStreamerScreen.route
        ) {
            PermissionLiveScreen(navController)
        }
        /*  MyProfile */
        composable(
            route = ScreenName.MyProfileDashboardScreen.route
        ) {
            MyProfileDashBoard(navController)
        }
        composable(
            route = ScreenName.AccountSwitcherScreen.route
        ) {
            AccountSwitcher(navController)
        }
        composable(
            route = ScreenName.SocialMediaScreen.route
        ) {
            SocialMediaScreen(navController)
        }

        composable(
            route = ScreenName.AuthenticatorScreen.route
        ) {
            AuthenticatorScreen(navController)
        }
        composable(
            route = ScreenName.AuthenticatorInputCodeScreen.route
        ) {
            AuthenticatorInputCodeScreen(navController)
        }
        composable(
            route = ScreenName.SubCategoriesScreen.route.plus("?title={title}").plus("&id={id}")
        ){navBackEntry->
            val title = navBackEntry.arguments?.getString("title") ?: ""
            val id = (navBackEntry.arguments?.getString("id") ?:"0" ).toInt()
            SubCategoriesScreen(navController,title,id)
        }
        composable(
            route = ScreenName.WarningDeleteAccountScreen.route
        ) {
            WarningDeleteAccountScreen(navController)
        }
        composable(route = ScreenName.SelectReasonDeleteAccountScreen.route) {
            SelectReasonDeleteAccountScreen(navController)
        }
        composable(route = ScreenName.ShareProfileScreen.route) {
            ShareProfile(navController)
        }
        composable(route = ScreenName.ChatSettingScreen.route) {
            ChatSettingScreen(navController = navController)
        }
        composable(route = ScreenName.AutoReplyScreen.route) {
            AutoReplyScreen(navController = navController)
        }
        composable(route = ScreenName.MessageShortcutScreen.route) {
            MessageShortcutScreen(navController = navController)
        }
        composable(route = ScreenName.DetailMessageShortcutScreen.route) {
            DetailMessageShortcutScreen(navController = navController)
        }
        composable(
            route = ScreenName.ContactSyncingScreen.route
        ) {
            ContactSync(navController)
        }
        composable(
            route = ScreenName.NotificationSettingScreen.route
        ) {
            NotificationSetting(navController)
        }
        composable(
            route = ScreenName.PrivacySettingsScreen.route
        ) {
            PrivacySettings(navController)
        }
        composable(
            route = ScreenName.BlockContacts.route
        ) {
            BlockContacts(navController)
        }
        composable(
            route = ScreenName.BlockContactsSearch.route
        ) {
            BlockContactsSearch(navController)
        }
        composable(
            route = ScreenName.ChooseLanguageScreen.route
        ) {
            ChooseLanguage(navController)
        }
        composable(
            route = ScreenName.CreateMessageShortcutScreen.route
                .plus("?messageShortcut={messageShortcut}")
        ) { navBackEntry ->
            val msgDefault = navBackEntry.arguments?.getString("messageShortcut") ?: ""
            val messageShortcut: UserMessageShortcut? = try {
                fromJson(msgDefault)
            } catch (e: Exception) {
                null
            }

            CreateMessageShortcutScreen(
                navController,
                messageDefault = messageShortcut
            )
        }
        composable(
            route = ScreenName.OnboardLiveScreen.route
        ) {
            OnBoardLiveScreen(
                navController
            )
        }
        composable(
            route = ScreenName.NewsFeedsHomeScreen.route
        ) {
            NewsFeedsHomeScreen(
                navController
            )
        }
        composable(
            route = ScreenName.ExploreLiveStreamScreen.route.plus("?defaultData={defaultData}")
        ) { navBackEntry ->
            val livestreamInfoJson = navBackEntry.arguments?.getString("defaultData") ?: ""
            val defaultLiveStreamInfo: LiveSteamShortInfo? = try {
                fromJson(livestreamInfoJson)
            } catch (e: Exception) {
                null
            }
            ExploreLiveStreamScreen(navController, defaultLivestreamInfo = defaultLiveStreamInfo)
        }
        composable(
            route = ScreenName.PlayBackOnLiveStreamScreen.route.plus("?defaultData={defaultData}"),
        ){ navBackEntry ->
            val livestreamInfoJson = navBackEntry.arguments?.getString("defaultData") ?: ""
            val defaultLiveStreamInfo: LiveSteamShortInfo? = try {
                fromJson(livestreamInfoJson)
            } catch (e: Exception) {
                null
            }
            PlayBackOnLiveStreamScreen(navController, defaultLivestreamInfo = defaultLiveStreamInfo)
        }
        composable(
            route = ScreenName.ConversationListContentScreen.route
        ){
            ConversationListContentScreen(navController)
        }
        composable(
            route= ScreenName.ConversationDetailScreen.route.plus(ID)
        ){navBackEntry->
            val id = navBackEntry.arguments?.getString(KEY_ID)
            ConversationDetailScreen(navController = navController, userId = id)
        }
        composable(
            route= ScreenName.EventDetailScreen.route.plus(ID)
        ){navBackEntry->
            val id =navBackEntry.arguments?.getString(KEY_ID)
           EventDetailScreen(navController=navController, id = id)
        }
        composable(
            route= ScreenName.NotificationHomeScreen.route
        ){
            NotificationHome(navController)
        }
        composable(
            route = ScreenName.SummaryLiveStreamScreen.route.plus(SUMMARY_LIVE_DATA)
        ){ navBackEntry ->
            val livestreamInfoJson = navBackEntry.arguments?.getString(KEY_SUMMARY_LIVE_DATA) ?: ""
            val defaultLiveStreamInfo: LivestreamInfo? = try {
                fromJson(livestreamInfoJson)
            } catch (e: Exception) {
                null
            }
            SummaryLiveStream(navController, defaultLiveStreamInfo)
        }
        composable(
            route = ScreenName.LiveSchedulingScreen.route
        ){
            LiveSchedulingScreen(navController)
        }
        composable(
            route = ScreenName.CalendarScreen.route.plus("?fillData={fillData}"),
            arguments = listOf(
                navArgument(name = "fillData"){
                    NavType.StringType
                    defaultValue=""
                }
            )
        ){from->
            val fillData = from.arguments?.getString("fillData") ?:""
            if (fillData.isNotBlank()){
                val ob = Gson().fromJson(fillData,FillDataSchedule::class.java)
                CalendarScreen(navController,ob)
            }else{
                CalendarScreen(navController,null)
            }
        }
        composable( route = ScreenName.BusinessHoursScreen.route){
            BusinessHoursScreen(navController = navController)
        }
        composable(route = ScreenName.SeeAllUpComingScreen.route){
            SeeAllUpComingScreen(navigation = navController)
        }
        composable(route = ScreenName.SeeAllFilterLiveStreamScreen.route.plus("?section_type={section_type}&section_title={section_title}"),
            arguments = listOf(
                navArgument("section_type") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("section_title") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )){ from ->
            val sectionType = from.arguments?.getString("section_type") ?:""
            val sectionTitle = from.arguments?.getString("section_title") ?:""
            if (sectionType.isNotBlank()){
                SeeAllFilterLiveStreamScreen(navigation = navController, sectionType = sectionType, sectionTitle = sectionTitle)
            }
        }
        composable(
            route = ScreenName.FollowingProfileScreen.route.plus("?defaultData={defaultData}")
        ){ from ->
            val liveInfoJson = from.arguments?.getString("defaultData") ?: ""
            if(liveInfoJson.isNotEmpty()){
                val livestreamInfo = Gson().fromJson(liveInfoJson, LiveSteamShortInfo::class.java)
                FollowingProfileScreen(navController, livestreamInfo)
            }
        }
        composable(route = ScreenName.InfoPackageScreen.route){
            InfoPackageScreen(navController = navController)
        }
        composable(route = ScreenName.KeplerBalanceSellerScreen.route) {
            KeplerBalanceSellerScreen(navController = navController)
        }
        composable(route = ScreenName.KeplerBalanceBuyerScreen.route){
                KeplerBalanceBuyerScreen(navController = navController)
        }
        composable(route = ScreenName.PaymentProcessScreen.route.plus("?defaultData={defaultData}")) { from ->
            val urlPayment = from.arguments?.getString("defaultData")?:""
            if(urlPayment.isNotEmpty()){
                val paymentWrapper = Gson().fromJson(urlPayment, WrapperUrlPayment::class.java)
                PaymentProcessScreen(navController = navController, wrapperUrlPayment = paymentWrapper)
            }
        }
        composable(route = ScreenName.PrivacyScreen.route.plus("?defaultData={defaultData}")) { from ->
            val urlPayment = from.arguments?.getString("defaultData")?:""
            if(urlPayment.isNotEmpty()){
                WebViewSocialMediaScreen(navController = navController, urlWebView = urlPayment)
            }
        }
        composable(route = ScreenName.MyDashBoardBookingScreen.route) { from ->
            MyDashBoardBookingScreen(navController = navController)
        }
        composable(route = ScreenName.MyServiceBookingDetails.route.plus("?defaultData={defaultData}")) { from ->
            val bookingInfo = from.arguments?.getString("defaultData")?:""
            if(bookingInfo.isNotEmpty()){
                val bookingObject = Gson().fromJson(bookingInfo, BookingHistoryInfo::class.java)
                MyServiceBookingDetails(navController = navController, bookingInfo = bookingObject)
            }
        }
    }
}