package olmo.wellness.android.ui.livestream.schedule.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.data.model.schedule.FillDataSchedule
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.extension.hideKeyboard
import olmo.wellness.android.ui.livestream.schedule.view.ActionBottomUI
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddDescriptionScheduleBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    fillDataSchedule: FillDataSchedule?,
    onSave: (String?) -> Unit
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val description = remember {
        mutableStateOf(fillDataSchedule?.description)
    }
    val enableConfirm = remember {
        mutableStateOf(false)
    }
    enableConfirm.value = description.value?.isNotBlank() == true

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            DescriptionUI(modalBottomSheetState,context, description.value, onDesChange = {
                description.value = it
            }, onDismiss = {
                hideKeyboard(context as Activity)
                scope.launch {
                    modalBottomSheetState.hide()
                }
                description.value = null
            }, onConfirm = {
                hideKeyboard(context as Activity)
                onSave.invoke(it)
                scope.launch {
                    modalBottomSheetState.hide()
                }
            }, enableConfirm = enableConfirm.value)
        }) {

    }
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("ResourceAsColor")
@Composable
private fun DescriptionUI(
    modalBottomSheetState: ModalBottomSheetState,
    context: Context,
    description: String?,
    onDesChange: (String?) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (String?) -> Unit,
    enableConfirm: Boolean
) {
    LazyColumn(modifier = Modifier) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = stringResource(R.string.lb_add_description),
                    style = MaterialTheme.typography.h1.copy(
                        fontSize = 16.sp, color = Color_Purple_FBC
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }

        item {
            TextField(
                value = description ?: "",
                onValueChange = {
                    onDesChange.invoke(it)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {})
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(color = Color_gray_3F9, shape = RoundedCornerShape(20.dp)),
                placeholder = {
                    Text(
                        text = stringResource(R.string.lb_your_awesome_description),
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Neutral_Gray_5, fontSize = 12.sp
                        ),
                    )
                },
                textStyle = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color_gray_3F9,
                    disabledLabelColor = White,
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent,
                    cursorColor = Color.Black
                )
            )
        }

        item {
            Text(
                text = stringResource(R.string.hint_description_livestream),
                style = MaterialTheme.typography.subtitle1.copy(
                    color = Neutral_Gray_5, fontSize = 10.sp
                ), modifier = Modifier.padding(end = 16.dp, start = 16.dp, top = 16.dp)
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(end = 16.dp, start = 16.dp,)) {
                ActionBottomUI(
                    context = context,
                    onDismiss = onDismiss,
                    enableConfirm = enableConfirm,
                    onConfirm = {
                        onConfirm.invoke(description)
                    },
                )
            }
        }
        item {
            Spacer(modifier = Modifier.padding(bottom = 20.dp))
        }
    }
}
