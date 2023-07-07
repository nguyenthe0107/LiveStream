package olmo.wellness.android.ui.common.picker.date.dialog

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
import olmo.wellness.android.ui.common.picker.date.ui.Dates
import olmo.wellness.android.ui.common.picker.date.ui.DatesNumberPicker
import olmo.wellness.android.ui.common.picker.time.component.ActionBottomUI
import olmo.wellness.android.ui.common.picker.time.component.HeaderBottom
import olmo.wellness.android.ui.common.picker.time.type.PickerSelector
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.theme.Color_BLUE_7F4
import olmo.wellness.android.ui.theme.White
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatePickerBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    title : String,
    onSuccess: (dateSelected: String, dateLong: Long) -> Unit,
    onFailed: (status: Boolean) -> Unit
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var state = remember {
        mutableStateOf(
            Dates(
                DateTimeHelper.currentDay,
                DateTimeHelper.currentMonth,
                DateTimeHelper.currentYear
            )
        )
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            HeaderBottom(title =title?:"")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color_BLUE_7F4)
            ) {
            }

            DatesNumberPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 20.dp),
                pickerSelector = PickerSelector.PickerDefault(),
                value = state.value,
                textStyle = MaterialTheme.typography.h1.copy(
                    fontSize = 18.sp
                ), onValueChange = {
                    state.value=it
                }
            )

            ActionBottomUI(context, onConfirm = {
                val currentTime = Calendar.getInstance()
                currentTime.apply {
                    set(Calendar.YEAR, state.value.year)
                    set(Calendar.MONTH, state.value.month-1)
                    set(Calendar.DAY_OF_MONTH,state.value.day)
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
