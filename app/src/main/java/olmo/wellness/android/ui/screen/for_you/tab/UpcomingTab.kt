package olmo.wellness.android.ui.screen.for_you.tab

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.himanshoe.kalendar.common.KalendarSelector
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.Kalendar
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.KalendarType
import olmo.wellness.android.ui.common.gridItems
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.KalendarStyle
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarShape
import olmo.wellness.android.ui.screen.for_you.cell.ItemUpComing
import olmo.wellness.android.ui.screen.for_you.viewmodel.ForYouViewModel
import olmo.wellness.android.ui.screen.notification.OnBottomReached
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.ui.toDate
import olmo.wellness.android.ui.toLocalDate
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation", "NewApi")
@Composable
fun UpcomingTab(navigation: NavController, type: String?,categoryId : Int? ,viewModel: ForYouViewModel) {


    val uiState = viewModel.uiState.collectAsState()

    val pageCurrent = remember {
        mutableStateOf(1)
    }

    val scrollState = rememberLazyListState()

    val dateSelect = remember(uiState.value.dateSelect) {
        mutableStateOf(uiState.value.dateSelect)
    }

    LaunchedEffect(Unit){
        viewModel.getData(type,pageCurrent.value, dateSelect.value,categoryId)
    }

    val listSchedule = remember(uiState.value.listFilterItem) {
        mutableStateOf(uiState.value.listFilterItem)
    }


    val isLoading = remember {
        mutableStateOf(true)
    }

    isLoading.value = uiState.value.isLoading

    scrollState.OnBottomReached() {
        if (listSchedule.value?.isNotEmpty() == true) {
            pageCurrent.value = pageCurrent.value + 1
            viewModel.getData(type, pageCurrent.value,dateSelect.value,categoryId)
        }
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
        modifier = Modifier
            .fillMaxSize()
            .background(GRAY_8F8)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(GRAY_8F8),
            state = scrollState,
            ) {
            item {
                Kalendar(
                    kalendarType = KalendarType.Oceanic(),
                    kalendarStyle = style,
                    onCurrentDayClick = { localDate, kalendarEvent, kalendarMoney ->
                        pageCurrent.value=1
                        viewModel.setDateSelect(type,pageCurrent.value,localDate.toDate(),categoryId)
                    },
                    isBack = false,
                    onBack = {
                    },
                    onDrop = {
                    },
                    selectedDay = dateSelect.value?.toLocalDate()?:Date().toLocalDate(),
                    isDrop = false,
                kalendarEvents = uiState.value.listEventCalendar?: emptyList()
                )
            }

            listSchedule.value?.let {
                gridItems(it.size, 1) { index ->
                    listSchedule.value?.get(index)?.let { it1 ->
                        ItemUpComing(
                            modifier = Modifier.padding(start = 15.dp, bottom = 20.dp, end = 15.dp),
                            data = it1,
                            onItemClick = {
                            })
                    }
                }
            }

            if (listSchedule.value==null || listSchedule.value?.isEmpty() == true) {
                item {
                    EmptyData()
                }
            }

            item {
                LoaderWithAnimation(isPlaying = isLoading.value)
            }

            item {
                Spacer(modifier = Modifier.padding(vertical = 30.dp))
            }
        }
    }

}