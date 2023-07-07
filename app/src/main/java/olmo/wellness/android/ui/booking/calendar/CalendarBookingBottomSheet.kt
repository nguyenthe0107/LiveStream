package olmo.wellness.android.ui.booking.calendar

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.himanshoe.kalendar.common.KalendarSelector
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.KalendarStyle
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarShape
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.Kalendar
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.KalendarType
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.ui.toDate
import java.time.LocalDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarBookingBottomSheet(
    serviceId: Int,
    onCallbackCalendarBooking : (KalendarMoney) -> Unit,
    onCancelCallback: (() -> Unit) ?= null,
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: CalendarBookingViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    viewModel.onBindServiceId(serviceId)
    val uiState = viewModel.uiState.collectAsState()
    val listDataCalendar = uiState.value.listCalendarMoney
    var calendarSelected by remember(uiState.value.kalendarMoneyDefault) {
        mutableStateOf(uiState.value.kalendarMoneyDefault)
    }
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = White,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            Scaffold(topBar = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Chọn ngày & giờ",
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 18.sp, lineHeight = 26.sp
                            ),
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Icon(painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "close",
                            tint = Black_037,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(12.dp)
                                .noRippleClickable {
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                })

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .align(Alignment.BottomCenter)
                                .background(Gray_EF3)
                        )
                    }
            }, bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .padding(top = 20.dp, end = 20.dp, start = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SecondLiveButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 6.dp),
                            stringResource(R.string.lb_back),
                            onClickFunc = {
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                                onCancelCallback?.invoke()
                            }
                        )
                        PrimaryLiveButton(
                            enable = calendarSelected != null,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 6.dp),
                            text = stringResource(R.string.btn_action_next),
                            onClickFunc = {
                                calendarSelected?.let { onCallbackCalendarBooking?.invoke(it) }
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                            }
                        )
                    }
                }
            }, modifier = Modifier.fillMaxHeight(0.9f)) {
                Column(modifier = Modifier) {
                    val style = KalendarStyle(
                        kalendarBackgroundColor = White,
                        kalendarColor = White,
                        kalendarSelector = KalendarSelector.RoundedCorner(
                            defaultColor = White,
                            selectedColor = Color_BLUE_7F4, todayColor = White,
                            textTitle = Neutral_Gray_9
                        ),
                        hasRadius = false,
                        shape = KalendarShape.DefaultRectangle
                    )
                    Kalendar(
                        kalendarType = KalendarType.Firey(),
                        kalendarStyle = style,
                        onCurrentDayClick = { localDate, kalendarEvent, kalendarMoney ->
                            calendarSelected = kalendarMoney
                        },
                        isBack = false,
                        isDrop = false,
                        onBack = {
                        },
                        onDrop = {
                        },
                        onBackMonth ={ startMonth, endMonth ->
                            viewModel.fetchDataByRangeTime(startTime = startMonth.toDate().time,
                                endTime = endMonth.toDate().time, serviceId = serviceId)
                        },
                        kalendarMoneys = listDataCalendar ?: defaultListCalendarMoney(),
                    )
                }
            }

        }) {
    }
}

@SuppressLint("NewApi")
fun defaultListCalendarMoney(): List<KalendarMoney> {
    return mutableListOf<KalendarMoney>().apply {
        add(KalendarMoney(LocalDate.now(), 0F))
    }
}