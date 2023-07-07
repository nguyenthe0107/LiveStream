package olmo.wellness.android.ui.screen.calendar_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.himanshoe.kalendar.common.KalendarSelector
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.Kalendar
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.KalendarType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.data.model.schedule.FillDataSchedule
import olmo.wellness.android.ui.common.dashedBorder
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.common.picker.time.dialog.TimePickerBottomSheet
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.KalendarStyle
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarShape
import olmo.wellness.android.ui.livestream.schedule.component.CreateLiveSchedulingButton
import olmo.wellness.android.ui.livestream.schedule.component.HeaderPlaningItemCompose
import olmo.wellness.android.ui.livestream.schedule.component.PlaningItemCompose
import olmo.wellness.android.ui.livestream.schedule.dialog.CalendarDialog
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.ui.toDate
import olmo.wellness.android.ui.toLocalDate
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("SuspiciousIndentation", "NewApi", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CalendarScreen(
    navController: NavController,
    fillData: FillDataSchedule?,
    isBack: Boolean = true,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val showCalendarDialog = remember { mutableStateOf(false) }

    val dateSelected = viewModel.dateSelect.collectAsState()

    var showButtonBook by remember { mutableStateOf(true) }

    val listSchedulerCalendar = viewModel.listSchedulerFilter.collectAsState()

    val listEventCalendar = viewModel.listEventCalendar.collectAsState()

    val isNext = viewModel.isNext.collectAsState()

    val modalTimePicker = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = {
            false
        })

    CalendarDialog(
        dateSelect = dateSelected.value.toLocalDate(),
        showCalendarDialog.value,
        eventList = listEventCalendar.value,
        setShowDialog = {
            showCalendarDialog.value = it

        },
        onDateSelect = {
            viewModel.setDateSelect(it.toDate())
        })

    OnLifecycleEvent { owner, event ->
        // do stuff on event
        when (event) {
            Lifecycle.Event.ON_START -> {
                viewModel.fetchListScheduler()
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {

            }
            Lifecycle.Event.ON_DESTROY -> {
            }
            else -> {

            }
        }
    }

    Scaffold(bottomBar = {
        ButtonAddAvailability(
            fillData,
            scope,
            modalTimePicker,
            dateSelected.value,
            isNext.value,
            showButtonBook,
            navController
        )
    }) {
        Column(
            modifier = Modifier
                .background(Color_LiveStream_Main_Color)
                .fillMaxSize()
        ) {
            val style = KalendarStyle(
                kalendarBackgroundColor = Color_LiveStream_Main_Color,
                kalendarColor = Color_LiveStream_Main_Color,
                kalendarSelector = KalendarSelector.Rounded(
                    defaultColor = White,
                    selectedColor = Color_BLUE_7F4,
                    todayColor = White,
                    eventTextColor = Color_RED_65B2
                ),
                hasRadius = true,
                shape = KalendarShape.DefaultRectangle
            )
            Kalendar(kalendarType = KalendarType.Oceanic(),
                kalendarStyle = style, onCurrentDayClick = { localDate, kalendarEvent,kalendarMoney ->
                    viewModel.setDateSelect(localDate.toDate())
                },
                isBack = isBack,
                onBack = {
                    navController.popBackStack()
                }, onDrop = {
                    showCalendarDialog.value = true
                },
                selectedDay = dateSelected.value.toLocalDate(),
                kalendarEvents = listEventCalendar.value
            )
            Card(
                modifier = Modifier
                    .padding(top = marginStandard),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                backgroundColor = White
            ) {
                Column(
                    modifier = Modifier
                        .padding(marginDouble)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        item {
                            HeaderPlaningItemCompose()
                        }
                        items(listSchedulerCalendar.value.size) { index ->
                            val data = listSchedulerCalendar.value[index]
                            PlaningItemCompose(data, callbackFunc = {
                                navController.navigate(ScreenName.EventDetailScreen.route.plus("?${Constants.KEY_ID}=${data.id}"))
                            })
                        }
                        if (listSchedulerCalendar.value.isEmpty()){
                            item { EmptyData() }
                        }
                    }
                }
            }
        }
    }

    TimePickerBottomSheet(
        modalBottomSheetState = modalTimePicker,
        onSuccess = { selected, dateLong ->
            val hour = DateTimeHelper.getHour(dateLong)
            val minute = DateTimeHelper.getMinute(dateLong)
            viewModel.addLivestreamInfo(
                fillData, DateTimeHelper.convertToDateHourMinute(hour, minute, dateSelected.value)
            )
            scope.launch {
                modalTimePicker.hide()
            }
            showButtonBook = false
        },
        onFailed = {
            showButtonBook = true
            scope.launch {
                modalTimePicker.hide()
            }
        })
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("NewApi")
@Composable
fun ButtonAddAvailability(
    fillData: FillDataSchedule?,
    scope: CoroutineScope,
    modalBottomSheetValue: ModalBottomSheetState,
    dateSelected: Date,
    isNext: Boolean,
    showButtonBook: Boolean,
    navController: NavController
) {
    if (fillData != null) {
        if (!isNext) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (showButtonBook) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                modalBottomSheetValue.show()
                            }
                        }, modifier = Modifier
                            .dashedBorder(
                                width = 1.dp,
                                color = Color_BLUE_7F4,
                                shape = RoundedCornerShape(50.dp),
                                10.dp, 10.dp
                            )
                            .clip(RoundedCornerShape(40.dp))
                            .height(40.dp),
                        border = BorderStroke(0.dp, Color.Transparent),
                        shape = RoundedCornerShape(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_button_add_calendar),
                            contentDescription = null,
                            tint = Color_Purple_7F4,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .background(Transparent)
                        )
                        Text(
                            text = stringResource(R.string.btn_add_your_availability),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 14.sp, color = Color_BLUE_7F4
                            )
                        )
                    }
                }
            }
        } else {
            CreateLiveSchedulingButton(title = stringResource(id = R.string.next), onClick = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(Constants.BUNDLE_DATA, dateSelected.time)
                navController.popBackStack()
            }, modifier = Modifier)
        }
    }

}
