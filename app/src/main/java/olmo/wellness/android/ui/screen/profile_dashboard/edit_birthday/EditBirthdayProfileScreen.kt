package olmo.wellness.android.ui.screen.profile_dashboard.edit_birthday

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.picker.date.ui.Dates
import olmo.wellness.android.ui.common.picker.date.ui.DatesNumberPicker
import olmo.wellness.android.ui.common.picker.time.component.ActionBottomUI
import olmo.wellness.android.ui.common.picker.time.component.HeaderBottom
import olmo.wellness.android.ui.common.picker.time.type.PickerSelector
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.Color_gray_FF7
import java.util.*

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditBirthdayProfileScreen(
    onSuccess: ((Boolean) -> Unit) ?= null,
    onFailed: ((status: Boolean) -> Unit) ?= null,
    viewModel: EditBirthdayViewModel = hiltViewModel()){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val maxHeight = 350.dp
    val state = remember {
        mutableStateOf(
            Dates(DateTimeHelper.currentDay, DateTimeHelper.currentMonth, DateTimeHelper.currentYear)
        )
    }
    val isSuccess = viewModel.isSuccess.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    if (isSuccess.value) {
        LaunchedEffect(true){
            onSuccess?.invoke(true)
            viewModel.resetState()
        }
    }
    Box(modifier = Modifier
        .background(Color_gray_FF7)
        .requiredHeight(maxHeight)){

        Column(
            modifier = Modifier
                .background(Color_gray_FF7)
                .requiredHeight(maxHeight)){
            HeaderBottom(title = stringResource(id = R.string.title_my_profile_birthday))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color_gray_FF7)
            ) {}
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
                    DateTimeHelper.FORMAT_TIME_BIRTHDAY
                )
                viewModel.updateBirthday(timeString)
            }, onDismiss = {
                onFailed?.invoke(false)
            }, enableConfirm = true)
        }

        LoaderWithAnimation(isPlaying = isLoading.value)

    }
}
