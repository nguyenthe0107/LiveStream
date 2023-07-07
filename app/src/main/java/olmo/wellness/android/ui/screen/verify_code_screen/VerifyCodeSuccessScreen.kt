package olmo.wellness.android.ui.screen.verify_code_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import olmo.wellness.android.R
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.ButtonConfirmCompose
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.theme.Color_Green_Main

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun VerifyCodeSuccessScreen(
    navController: NavController,
    identity: String? = "",
    userName: String? = "",
    userType: String? = UserTypeModel.BUYER.value){
    Scaffold(
        topBar = {}){
        val state = rememberPagerState()
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(start = 25.dp, end = 25.dp)
            .noRippleClickable {}
        ) {
            val sizePage = 2
            Column(modifier = Modifier.align(Alignment.TopCenter)){
                SliderView(navController,sizePage, state, identity ?: "")
            }
            // If want auto Slide
            Column(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)) {
                DotsIndicator(
                    totalDots = sizePage,
                    selectedIndex = state.currentPage
                )
                Spacer(modifier = Modifier.padding(4.dp))
                ButtonConfirmCompose(buttonText = stringResource(id = R.string.next), colorEnable = Color_Green_Main) {
                    navController.navigate(ScreenName.SellerHubScreen.route)
                    if (userType == UserTypeModel.BUYER.value)
                        navController.navigate(ScreenName.OnboardLiveScreen.route)
                    else {
                        navController.navigate(ScreenName.SellerVerificationStep1Screen.route.plus("?identity=${identity}"))
                    }
                }
            }
            LaunchedEffect(key1 = state.currentPage) {
                delay(5000)
                var newPosition = state.currentPage + 1
                if (newPosition > 1) newPosition = 0
                // scrolling to the new position.
                state.animateScrollToPage(newPosition)
            }
        }
    }
}
