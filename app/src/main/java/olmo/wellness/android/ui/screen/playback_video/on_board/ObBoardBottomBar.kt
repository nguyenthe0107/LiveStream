package olmo.wellness.android.ui.screen.playback_video.on_board

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import olmo.wellness.android.R
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.ui.screen.playback_video.common.HomeBottomNavigation

var temp = 0

@ExperimentalPagerApi
@Composable
fun OnboardBottomBar(
    modifier: Modifier,
    tabs: List<BottomBarOnboardTabData>,
    pagerState: PagerState,
    backgroundColor: Color,
    userRole: UserTypeModel = UserTypeModel.BUYER,
    onSizeChanged: ((IntSize) -> Unit)? = null,
    onTabSelected: ((BottomBarOnboardTabData, userRole: UserTypeModel) -> Unit)? = null,
    avatarLink: String
){
    UIBottomBar(
        modifier,
        tabs,
        userRole,
        pagerState,
        backgroundColor,
        onSizeChanged,
        onTabSelected,
        avatarLink
    )
}

@ExperimentalPagerApi
@Composable
fun UIBottomBar(
    modifier: Modifier,
    tabs: List<BottomBarOnboardTabData>,
    role: UserTypeModel,
    pagerState: PagerState,
    backgroundColor: Color,
    onSizeChanged: ((IntSize) -> Unit)? = null,
    onTabSelected: ((BottomBarOnboardTabData, userRole: UserTypeModel) -> Unit)? = null,
    avatarLink: String
) {
    HomeBottomNavigation(modifier = modifier,pagerState = pagerState,tabs = tabs, onTabClick = {
        onTabSelected?.invoke(it, role)
    }, avatarLink = avatarLink)
}
