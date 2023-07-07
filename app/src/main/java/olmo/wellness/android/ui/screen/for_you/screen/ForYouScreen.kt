package olmo.wellness.android.ui.screen.for_you.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager as HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.screen.for_you.cell.CustomScrollableTabRow
import olmo.wellness.android.ui.screen.for_you.cell.TabRowStyle
import olmo.wellness.android.ui.screen.for_you.tab.EventTab
import olmo.wellness.android.ui.screen.for_you.tab.LiveNowTab
import olmo.wellness.android.ui.screen.for_you.tab.UpcomingTab
import olmo.wellness.android.ui.screen.for_you.viewmodel.ForYouViewModel
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ForYouScreen(
    navigation: NavController,
    openBottomSheet: MutableState<Boolean>,
    viewModel: ForYouViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    val isLoading = uiState.value.isLoading

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    val listTab = remember(uiState.value.listTab) {
        mutableStateOf(uiState.value.listTab)
    }

//    val listType = remember(uiState.value.listTab) {
//        mutableStateOf(uiState.value.listType)
//    }

    val styleTab = TabRowStyle(
        backgroundColor = Color_LiveStream_Main_Color,
        colorSelect = White,
        colorUnSelect = Neutral_Bare_Gray
    )

    Scaffold(modifier = Modifier.fillMaxWidth(), content = {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GRAY_8F8)
        ) {

            listTab.value?.let { it1 ->
                CustomScrollableTabRow(
                    tabs = it1.map { it?.name },
                    selectedTabIndex = pagerState.currentPage,
                    onTabClick = {
                        scope.launch {
                            pagerState.scrollToPage(it)
                        }
                    }, style = styleTab
                )
            }

            listTab.value?.size?.let { it1 ->
                HorizontalPager(count = it1, state = pagerState, key = {
                    listTab.value?.get(it)?.name ?: ""
                }) { index ->
                    when (val type = listTab.value?.get(index)?.name) {
                        SectionType.LIVE_NOW.value -> {
                            LiveNowTab(
                                navigation= navigation,
                                type = type,
                                categoryId = null,
                                viewModel = viewModel,
                                openBottomSheet = openBottomSheet
                            )
                        }
                        SectionType.UPCOMING.value -> {
                            UpcomingTab(navigation=navigation, type = type,categoryId = null,viewModel= viewModel)
                        }
                        else -> {
                            EventTab(navigation=navigation, type=type,categoryId = null,viewModel= viewModel)
                        }
                    }
                }
            }

            LoaderWithAnimation(isPlaying = true)

            if (listTab.value==null || listTab.value?.isEmpty()==true){
                EmptyData()
            }

        }
    })
}

