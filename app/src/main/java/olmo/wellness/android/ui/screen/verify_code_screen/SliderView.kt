package olmo.wellness.android.ui.screen.verify_code_screen
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SliderView(navController: NavController,
               pageSize: Int = 2,
               state: PagerState, userName: String) {
    HorizontalPager(
        state = state,
        count = pageSize, modifier = Modifier.fillMaxSize()
    ) { page ->
        if(page == 0){
            VerifyCodeSuccessSlide1Screen(navController, username = userName)
        }else{
            VerifyCodeSuccessSlide2Screen(navController, username = userName)
        }
    }
}