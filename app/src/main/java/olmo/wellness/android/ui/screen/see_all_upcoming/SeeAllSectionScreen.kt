package olmo.wellness.android.ui.screen.see_all_upcoming

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.himanshoe.kalendar.common.KalendarSelector
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.Kalendar
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.KalendarType
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.common.gridItems
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.KalendarStyle
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarShape
import olmo.wellness.android.ui.screen.for_you.cell.ItemUpComing
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.ui.toDate
import olmo.wellness.android.ui.toLocalDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "NewApi")
@Composable
fun SeeAllUpComingScreen(
    navigation: NavController,
    viewModel: SeeAllUpComingViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val isLoading = uiState.value.isLoading
    val dateSelect = remember(uiState.value.dateSelect) {
        mutableStateOf(uiState.value.dateSelect)
    }

    val listUpComing = remember(uiState.value.listUpComing) {
        mutableStateOf(uiState.value.listUpComing)
    }
    val style = KalendarStyle(
        kalendarBackgroundColor = GRAY_8F8,
        kalendarColor = Color_LiveStream_Main_Color,
        kalendarSelector = KalendarSelector.Rounded(
            defaultColor = White,
            selectedColor = Color_BLUE_7F4,
            todayColor = White,
            eventTextColor = Color_RED_65B2,
            textTitle = Neutral_Gray_9
        ),
        hasRadius = true,
        shape = KalendarShape.DefaultRectangle
    )

    Scaffold(
        topBar = {
            ToolbarSchedule(
                title = "Upcoming",
                backIconDrawable = R.drawable.ic_back_violet,
                navController = navigation,
                titleColor = Color_LiveStream_Main_Color,
                backgroundColor = White
            )
        },
        modifier = Modifier.fillMaxSize(),
        backgroundColor = GRAY_8F8
    ) {
        LazyColumn(
            modifier = Modifier
//                .fillMaxSize()
        ) {

            item {
                Kalendar(
                    kalendarType = KalendarType.Oceanic(),
                    kalendarStyle = style,
                    onCurrentDayClick = { localDate, kalendarEvent, kalendarMoney ->
//                    viewModel.setDateSelect(localDate.toDate())
                        viewModel.setDateSelect(localDate.toDate())
                    },
                    isBack = false,
                    isDrop = false,
                    onBack = {
//                    navController.popBackStack()
                    },
                    onDrop = {
//                    showCalendarDialog.value = true
                    },
                    selectedDay = dateSelect.value.toLocalDate(),
//                kalendarEvents = uiState.value.listEventCalendar?: emptyList()
                )
            }

            item {
                LoaderWithAnimation(isPlaying = isLoading)
            }

            listUpComing.value?.let {
                gridItems(it.size, 1) { index ->
                    listUpComing.value?.get(index)?.let { it1 ->
                        ItemUpComing(
                            modifier = Modifier.padding(start = 15.dp, bottom = 20.dp, end = 15.dp),
                            data = it1,
                            onItemClick = {
                                navigation.navigate(ScreenName.EventDetailScreen.route.plus("?${Constants.KEY_ID}=${it.id}"))
                            })
                    }
                }
            }

            if (listUpComing.value == null || listUpComing.value?.isEmpty() == true) {
                item { EmptyData() }
            }

        }
    }

}