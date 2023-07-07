package olmo.wellness.android.ui.booking.book_now.time_booking

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.formatToVND
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.ui.toDate
import olmo.wellness.android.ui.toLocalDate
import javax.security.auth.callback.Callback

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "NewApi")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimeBookingBottomSheet(
    serviceId: Int,
    kalendarMoney: KalendarMoney,
    onCallbackTimeBooking: ((KalendarMoney) -> Unit)? = null,
    onCancelCallback: (() -> Unit) ?= null,
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: TimeBookingViewModel = hiltViewModel(),
){

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        viewModel.onBindServiceId(kalendarMoney, serviceId)
    }
    val ui = viewModel.uiState.collectAsState()
    val listTimeSelection = ui.value.timeBookingInfo?.timestamps
    val isSelected = remember {
        mutableStateOf(false)
    }

    val dateSelected = remember {
        mutableStateOf(kalendarMoney.date.toDate().time)
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
                ){
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
                            enable = dateSelected.value != 0L && isSelected.value,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 6.dp),
                            text = stringResource(R.string.lb_confirm),
                            onClickFunc = {
                                onCallbackTimeBooking?.invoke(kalendarMoney.copy(
                                    date = DateTimeHelper.convertLongToDate(dateSelected.value).toLocalDate(),
                                    timeStamp = dateSelected.value
                                ))
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                            }
                        )
                    }
                }
            }, modifier = Modifier.fillMaxHeight(0.8f)) {
                LazyColumn() {
                    listTimeSelection?.size?.let { it1 ->
                        items(count = it1, key = {
                            it
                        }) { index ->
                            val item = listTimeSelection[index]
                            ItemTimeBooking(data = item, kalendarMoney = kalendarMoney,
                                callbackDate= { selected ->
                                    dateSelected.value = selected
                                    isSelected.value = true
                                }, dataSelected = dateSelected.value,
                            isSelected = isSelected)
                        }
                    }
                    item { 
                        SpaceCompose(height = 80.dp)
                    }
                }
            }

        }) {
    }
}


@Composable
private fun ItemTimeBooking(data: Long,
                            dataSelected: Long,
                            kalendarMoney: KalendarMoney,
                            isSelected: MutableState<Boolean>,
                            callbackDate: ((Long) -> Unit) ?= null) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(
            color = if (data == dataSelected && isSelected.value) {
                Color_BLUE_7F4.copy(alpha = 0.3f)
            } else {
                Color.Transparent
            }
        )
        .noRippleClickable {
            callbackDate?.invoke(data)
        }){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp), horizontalArrangement = Arrangement.Center) {
            Text(
                text = DateTimeHelper.convertToStringHour(data),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp, lineHeight = 24.sp
                ),
                modifier = Modifier,
                textAlign = TextAlign.Center
            )

            if(kalendarMoney.money != null){
                Text(
                    text = " - "+ formatToVND(kalendarMoney.money),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 16.sp, lineHeight = 24.sp
                    ),
                    modifier = Modifier,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray_EF3.copy(alpha = 0.3f))
        )

    }
}