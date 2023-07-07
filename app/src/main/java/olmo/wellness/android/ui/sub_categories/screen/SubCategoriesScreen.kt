package olmo.wellness.android.ui.sub_categories.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.ui.broadcast_receiver.SystemBroadcastReceiver
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.screen.for_you.cell.CustomScrollableTabRow
import olmo.wellness.android.ui.screen.for_you.cell.TabRowStyle
import olmo.wellness.android.ui.screen.for_you.tab.EventTab
import olmo.wellness.android.ui.screen.for_you.tab.LiveNowTab
import olmo.wellness.android.ui.screen.for_you.tab.UpcomingTab
import olmo.wellness.android.ui.screen.for_you.viewmodel.ForYouViewModel
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.video_small.ACTION_PIP_CONTROL
import olmo.wellness.android.ui.screen.video_small.onIntentReceivedForPipController
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SubCategoriesScreen(
    navigation: NavController,
    title : String,
    categoryId : Int?,
    viewModel: ForYouViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    val listTab = remember(uiState.value.listTab) {
        mutableStateOf(uiState.value.listTab)
    }

    LaunchedEffect(Unit){
        viewModel.sendTrackingContentCategory(title)
    }

    val styleTab = TabRowStyle(
        backgroundColor = GRAY_8F8,
        colorSelect = Color_LiveStream_Main_Color,
        colorUnSelect = Neutral_Gray_9
    )

    SystemBroadcastReceiver(systemAction = ACTION_PIP_CONTROL) { intent ->
        intent.onIntentReceivedForPipController(
            onClosePip = {
            },
            onRequestOpenContent = {
                if (it == null)
                    return@onIntentReceivedForPipController
                if (it.isLiveStream == true) {
                    navigation.navigate(
                        ScreenName.PlayBackOnLiveStreamScreen.route
                            .plus("?defaultData=${it.toJson()}")
                    )
                } else {
                    navigation.navigate(
                        ScreenName.ExploreLiveStreamScreen.route
                            .plus("?defaultData=${it.toJson()}")
                    )
                }
            }
        )
    }

    Scaffold(
        topBar = {
            ToolbarSchedule(
                title = title,
                backIconDrawable = R.drawable.ic_back_calendar,
                navController = navigation,
                backgroundColor = Transparent
            )
        }, modifier = Modifier.fillMaxSize(),
        backgroundColor = Color_LiveStream_Main_Color
    ) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = GRAY_8F8)
        ) {

            listTab.value?.let { it1 ->
                CustomScrollableTabRow(
                    tabs = it1.map { it?.title },
                    selectedTabIndex = pagerState.currentPage,
                    onTabClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    }, style = styleTab,
                    customColorText = Neutral_Gray_9
                )
            }
            listTab.value?.size?.let { it1 ->
                HorizontalPager(count = it1, state = pagerState, key = {
                    listTab.value?.get(it)?.name ?: ""
                }) { index ->
                    when (val type = listTab.value?.get(index)?.name) {
                        SectionType.LIVE_NOW.value -> {
                            LiveNowTab(
                                navigation,
                                type,
                                categoryId,
                                viewModel,
                                null
                            )
                        }
                        SectionType.UPCOMING.value -> {
                            UpcomingTab(navigation, type, categoryId,viewModel)
                        }
                        else -> {
                            EventTab(navigation, type, categoryId,viewModel)
                        }
                    }
                }
            }

        }
    }

}