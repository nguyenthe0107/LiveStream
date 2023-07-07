package olmo.wellness.android.ui.screen.business_hours

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.SwitchDay
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogAction
import olmo.wellness.android.ui.common.picker.time.type.PickerSelector
import olmo.wellness.android.ui.common.picker.time.type.PickerShape
import olmo.wellness.android.ui.common.picker.time.ui.AMPMHours
import olmo.wellness.android.ui.common.picker.time.ui.HoursNumberPicker
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BusinessHoursScreen(navController: NavController) {
    val listHours = createHourWeeks

    val openDialogCustom = remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        ToolbarSchedule(
            title = stringResource(id = R.string.title_item_business_hour),
            backIconDrawable = R.drawable.ic_back_calendar,
            navController = navController,
            backgroundColor = Color_LiveStream_Main_Color,
            onBackStackFunc = {
                openDialogCustom.value=true
            }
        )
    }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color_LiveStream_Main_Color)
        ) {
            val (options, imageCompose, endCompose) = createRefs()
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 32.dp,
                            topEnd = 32.dp
                        )
                    )
                    .background(color = Color_gray_FF7)
                    .fillMaxSize()
                    .fillMaxHeight()
                    .noRippleClickable { hideAllPicker(listHours) }
                    .constrainAs(options) {
                        start.linkTo(parent.start)
                        top.linkTo(imageCompose.top, 36.dp)
                    }
                    .padding(top = 50.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color_gray_FF7)
                        .padding(
                            vertical = marginStandard
                        )
                        .verticalScroll(rememberScrollState())
                        .noRippleClickable { hideAllPicker(listHours) }
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp, horizontal = 20.dp)
                    ) {

                        Text(
                            text = stringResource(R.string.lb_set_your_own_store_schedule),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 16.sp, color = Color_Purple_FBC
                            ), modifier = Modifier.padding(bottom = 15.dp)
                        )

                        listHours.forEach { hourWeek ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                                SwitchDay(
                                    text = hourWeek.dayName,
                                    defaultMode = hourWeek.enable,
                                    modifier = Modifier.weight(1f),
                                    changeSwitch = {
                                        hideAllPicker(listHours)
                                    }
                                )

                                Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                                Row(
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .wrapContentHeight(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OffDayUI(hourWeek)
                                    StartTimeUI(hourWeek,listHours)
                                }


                                Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                                Row(
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .wrapContentHeight(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OffDayUI(hourWeek)
                                    EndTimeUI(hourWeek,listHours)
                                }
                            }
                        }
                    }

                }
            }

            AvatarMascot(modifier = Modifier.constrainAs(imageCompose) {
                top.linkTo(parent.top, 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, uri = null, callbackFun = {
            }, src = R.drawable.ic_launcher_foreground)

        }
    }

    DialogAction(openDialogCustom = openDialogCustom,title ="Hours Shorten?", description = "It looks like your hours are not sufficient, save anyway?",
        btnCancelCallback= {
            openDialogCustom.value = false
        }, btnConfirmCallback = {
            openDialogCustom.value = false
            navController.popBackStack()
        })
}

private fun hideAllPicker(data : List<HourWeek>){
    data.forEach {
        it.hourStartEnable.value=false
        it.hourEndEnable.value=false
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun StartTimeUI(hourWeek: HourWeek, listHours: List<HourWeek>) {
    if (hourWeek.enable.value) {
        if (hourWeek.hourStart == null) {
            hourWeek.hourStart = mutableStateOf(
                AMPMHours(
                    hours = DateTimeHelper.getHourCurrent(),
                    minutes = DateTimeHelper.getMinuteCurrent(),
                    dayTime = DateTimeHelper.getAmPmTimePicker()
                )
            )
        }
        if (!hourWeek.hourStartEnable.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color_3E5, shape = RoundedCornerShape(22.dp))
                    .padding(vertical = 2.dp)
                    .wrapContentHeight()
                    .noRippleClickable {
                        hideAllPicker(listHours)
                        hourWeek.hourStartEnable.value = !hourWeek.hourStartEnable.value
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val hours = hourWeek.hourStart?.value as? AMPMHours
                Text(
                    text = "${hours?.hours}:${hours?.minutes}     ${hours?.dayTime}",
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = White, fontSize = 16.sp
                    )
                )
            }
        } else {
            HoursNumberPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(), leadingZero = true,

                pickerSelector = PickerSelector.PickerDefault(backgroundColor = Color_3E5),
                pickerShape = PickerShape(padding = 0.dp),
                value = hourWeek.hourStart?.value!!,
                onValueChange = {
                    hourWeek.hourStart!!.value = it
                },
                textStyle = MaterialTheme.typography.h1.copy(
                    fontSize = 18.sp
                )
            )
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun EndTimeUI(hourWeek: HourWeek,  listHours: List<HourWeek>) {
    if (hourWeek.enable.value) {
        if (hourWeek.hourEnd == null) {
            hourWeek.hourEnd = mutableStateOf(
                AMPMHours(
                    hours = DateTimeHelper.getHourCurrent() + 1,
                    minutes = DateTimeHelper.getMinuteCurrent(),
                    dayTime = DateTimeHelper.getAmPmTimePicker()
                )
            )
        }
        if (!hourWeek.hourEndEnable.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color_3E5, shape = RoundedCornerShape(22.dp))
                    .padding(vertical = 2.dp)
                    .wrapContentHeight().noRippleClickable {
                        hideAllPicker(listHours)
                        hourWeek.hourEndEnable.value=!hourWeek.hourEndEnable.value
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val hours = hourWeek.hourEnd?.value as? AMPMHours
                Text(
                    text = "${hours?.hours}:${hours?.minutes}     ${hours?.dayTime}",
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = White, fontSize = 16.sp
                    )
                )
            }
        }else{
            HoursNumberPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(), leadingZero = true,

                pickerSelector = PickerSelector.PickerDefault(backgroundColor = Color_3E5),
                pickerShape = PickerShape(padding = 0.dp),
                value = hourWeek.hourEnd?.value!!,
                onValueChange = {
                    hourWeek.hourEnd!!.value = it
                },
                textStyle = MaterialTheme.typography.h1.copy(
                    fontSize = 18.sp
                )
            )
        }
    }
}


@Composable
private fun OffDayUI(hourWeek: HourWeek) {
    if (!hourWeek.enable.value)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color_Purple_FBC,
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(vertical = 2.dp)
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.lb_off),
                style = MaterialTheme.typography.button.copy(
                    color = Color_Purple_FBC
                )
            )
        }
}