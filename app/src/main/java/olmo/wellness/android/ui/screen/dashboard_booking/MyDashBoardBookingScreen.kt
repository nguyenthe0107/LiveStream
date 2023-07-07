package olmo.wellness.android.ui.screen.dashboard_booking

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.screen.dashboard_booking.compose.MenuStateDashBoardBookings
import olmo.wellness.android.ui.screen.dashboard_booking.tabs.HistoryBookingTab
import olmo.wellness.android.ui.screen.for_you.cell.CustomScrollableTabRow
import olmo.wellness.android.ui.screen.for_you.cell.TabRowStyle
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyDashBoardBookingScreen(navController: NavHostController, viewModel: MyDashBoardBookingViewModel = hiltViewModel()){

    val scope = rememberCoroutineScope()
    val openSettingMenu = remember {
        mutableStateOf(false)
    }
    val pagerState = rememberPagerState(
        initialPage = 0,
    )
    val uiState = viewModel.uiState.collectAsState()

    val listTab = remember(uiState.value.listTabBookingServices) {
        mutableStateOf(uiState.value.listTabBookingServices)
    }

    val styleTab = TabRowStyle(
        backgroundColor = Color_LiveStream_Main_Color,
        colorSelect = White,
        colorUnSelect = Neutral_Bare_Gray
    )

    Scaffold(topBar = {
        ToolbarSchedule(
            title = stringResource(id = R.string.title_my_booking),
            backIconDrawable = R.drawable.ic_back_calendar,
            optionIconRightDrawable = R.drawable.ic_olmo_setting_booking_dashboard,
            navController = navController,
            backgroundColor = Color_LiveStream_Main_Color,
            onOpenOptionSettingFunc = {
                openSettingMenu.value = !openSettingMenu.value
            }
        )
    },modifier = Modifier.fillMaxSize()){
        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = height_toolbar)
                .background(GRAY_8F8)) {
                listTab.value?.let { it1 ->
                    CustomScrollableTabRow(
                        tabs = it1.map { stringResource(id = it.name) },
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
                        listTab.value?.get(it)?.type ?: ""
                    }){ index ->
                        val type = listTab.value?.get(index)?.type
                        HistoryBookingTab(navController = navController, type= type,categoryId = null,viewModel= viewModel)
                    }
                }
            }

            if (listTab.value == null || listTab.value?.isEmpty() == true){
                EmptyData()
            }

            /* DropDown Menu */
            uiState.value.listTabBookingServices?.let { it1 -> MenuStateDashBoardBookings(listMenu = it1,openSettingMenu.value, onSelectMenu = { typeSelected ->
                openSettingMenu.value = !openSettingMenu.value
                var index = 0
                listTab.value?.mapIndexed { indexInternal, stateDashBoardModel ->
                    if(stateDashBoardModel.type == typeSelected){
                        index = indexInternal
                    }
                }
                scope.launch {
                    pagerState.scrollToPage(index)
                }
            }) }

            LoaderWithAnimation(isPlaying = uiState.value.isLoading)
        }
    }
}


