package olmo.wellness.android.ui.common.picker.time.dialog

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.picker.time.component.ActionBottomUI
import olmo.wellness.android.ui.common.picker.time.component.HeaderBottom
import olmo.wellness.android.ui.common.picker.time.type.PickerSelector
import olmo.wellness.android.ui.common.picker.time.ui.AMPMHours
import olmo.wellness.android.ui.common.picker.time.ui.Hours
import olmo.wellness.android.ui.common.picker.time.ui.HoursNumberPicker
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.theme.Color_BLUE_7F4
import olmo.wellness.android.ui.theme.White
import java.util.Calendar

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimePickerBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    onSuccess: (dateSelected: String, dateLong: Long) -> Unit,
    onFailed: (status: Boolean) -> Unit
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var state by remember {
        mutableStateOf<Hours>(
            AMPMHours(
                DateTimeHelper.getHourCurrent(),
                DateTimeHelper.getMinuteCurrent(),
                DateTimeHelper.getAmPmTimePicker()
            )
        )
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {

            HeaderBottom(title = stringResource(R.string.lb_set_your_availability))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color_BLUE_7F4)
            ) {
            }

            HoursNumberPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 20.dp), leadingZero = true,

                pickerSelector = PickerSelector.PickerDefault(),
                value = state,
                onValueChange = {
                    state = it
                },
                textStyle = MaterialTheme.typography.h1.copy(
                    fontSize = 18.sp
                )
            )

            ActionBottomUI(context, onConfirm = {
                val currentTime = Calendar.getInstance()
                currentTime.apply {
                        set(Calendar.HOUR, state.hours)
                        set(Calendar.MINUTE, state.minutes)
                    set(Calendar.AM_PM, (if ((state as AMPMHours).dayTime == AMPMHours.DayTime.AM) 0 else 1))
                }
                val timeString = DateTimeHelper.convertDataToString(
                    currentTime.timeInMillis,
                    DateTimeHelper.FORMAT_TIME_PICKER
                )
                onSuccess.invoke(timeString, currentTime.timeInMillis)
            }, onDismiss = {
                onFailed.invoke(false)
            }, enableConfirm = true)
        }) {
    }
}
