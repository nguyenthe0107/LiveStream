package olmo.wellness.android.ui.screen.account_setting.chat_setting.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogAction
import olmo.wellness.android.ui.livestream.schedule.view.ActionBottomUI
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SetupAutomaticBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    onConfirm: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current


    val dialogConfirm = remember {
        mutableStateOf(false)
    }
    var description: String? by remember { mutableStateOf(null) }
    var enableConfirm by remember { mutableStateOf(false) }

    enableConfirm = description?.isNotBlank() == true
    val context = LocalContext.current

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {

            Box(modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.lb_setup_automatic_response),
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Color_Purple_FBC, fontSize = 18.sp
                        ), modifier = Modifier.padding(bottom = 10.dp)
                    )

                    TextField(
                        value = description ?: "", onValueChange = {
                            description = it
                        }, modifier = Modifier
                            .padding(bottom = 10.dp)
                            .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {})
                            .height(200.dp)
                            .fillMaxWidth()
                            .background(color = Color_gray_3F9, shape = RoundedCornerShape(16.dp)), placeholder = {
                            Text(
                                text = stringResource(R.string.title_message_shortcut),
                                style = MaterialTheme.typography.subtitle1.copy(
                                    color = Neutral_Gray_5, fontSize = 12.sp
                                ),
                            )
                        }, textStyle = MaterialTheme.typography.subtitle1.copy(
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

                    Text(
                        text = "Greetings, shop polices, promotions or any other information can be setup here as you wish to let customer know in the message. 500 charactes only.",
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 10.sp, color = Color_gray_F92
                        ),
                    )

                    Spacer(modifier = Modifier.padding(bottom = 50.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                ) {
                    ActionBottomUI(
                        context = context,
                        onDismiss = {
                            focusManager.clearFocus()
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                            description = null
                        },
                        enableConfirm = enableConfirm,
                        onConfirm = {
                            focusManager.clearFocus()
                            dialogConfirm.value = true
                        }
                    )

                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                }
            }

        }
    ) {

    }

    DialogAction(
        openDialogCustom = dialogConfirm,
        title = "Changes are not saved.",
        description = "Do you want to cancel the changes?",
        btnCancelCallback = {
            scope.launch {
                modalBottomSheetState.hide()
            }
            description = null
        },
        btnConfirmCallback = {
            description?.let { onConfirm.invoke(it) }
            scope.launch {
                modalBottomSheetState.hide()
            }
            description = null
        })

}