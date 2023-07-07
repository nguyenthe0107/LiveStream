package olmo.wellness.android.ui.screen.playback_video.common

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.playback_video.on_board.BottomBarOnboardTabData
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Purple500
import olmo.wellness.android.ui.theme.White

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeBottomNavigation(
    modifier: Modifier,
    pagerState: PagerState,
    tabs: List<BottomBarOnboardTabData>,
    onTabClick: (BottomBarOnboardTabData) -> Unit,
    avatarLink: String
) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(
            topEnd = 20.dp,
            topStart = 20.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        ), elevation =  10.dp
    ) {
        TabRow(
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .customTabIndicatorOffset(
                            currentTabPosition = tabPositions[pagerState.currentPage],
                            tabWidth = 30.dp
                        )
                        .background(
                            color = Color_LiveStream_Main_Color,
                            shape = RoundedCornerShape(100.dp)
                        )
                        .clip(RoundedCornerShape(100.dp))
                        .padding(horizontal = 10.dp),
                    color = Color_LiveStream_Main_Color,
                    height = (2).dp
                )
            },
            modifier = modifier
                .background(
                    color = White, shape = RoundedCornerShape(
                        topEnd = 20.dp,
                        topStart = 20.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .clip(
                    RoundedCornerShape(
                        topEnd = 20.dp,
                        topStart = 20.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .height(60.dp),
            selectedTabIndex = pagerState.currentPage, tabs = {
                tabs.forEachIndexed { tabIndex, tab ->
                    Tab(
                        modifier = Modifier.padding(horizontal = (if (tabIndex == 0 || tabIndex == tabs.size - 1) 10.dp else 0.dp)),
                        selected = pagerState.currentPage == tabIndex,
                        onClick = {
                            scope.launch {
                                pagerState.scrollToPage(tabIndex)
                            }
                            onTabClick.invoke(tab)
                        },
                        content = {
                            val icon = tab.iconSelected
                            val colorTint = Color_LiveStream_Main_Color
                            if (tab.type == BottomBarOnboardTabData.TabType.PROFILE) {
                                val rememberResource = rememberAsyncImagePainter(model = icon)
                                AsyncImage(modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape),
                                    contentScale = ContentScale.Crop, model = avatarLink,
                                    contentDescription = "avatar_home",
                                    error = painterResource(id = icon), placeholder = rememberResource)
                            } else {
                                Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = "",
                                    tint = colorTint,
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            }

                            Text(
                                text = tab.type.tabName,
                                style = MaterialTheme.typography.subtitle2.copy(
                                    color = colorTint,
                                    fontSize = 10.sp,
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                            )
                        })
                }
            })
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .clip(RoundedCornerShape(100.dp))
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset, y = (-57).dp)
        .width(currentTabWidth)
}