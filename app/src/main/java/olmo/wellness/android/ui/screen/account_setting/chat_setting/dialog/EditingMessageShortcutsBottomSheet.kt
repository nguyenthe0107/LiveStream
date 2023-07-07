package olmo.wellness.android.ui.screen.account_setting.chat_setting.dialog

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogAction
import olmo.wellness.android.ui.livestream.schedule.view.ActionBottomUI
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditingMessageShortcutsBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {

    val scope = rememberCoroutineScope()

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
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.title_message_shortcut),
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Color_Purple_FBC, fontSize = 18.sp
                        ), modifier = Modifier.padding(bottom = 10.dp)
                    )

                    TextField(
                        value = description ?: "",
                        onValueChange = {
                            description = it
                        },
                        modifier = Modifier
                            .padding(bottom = 50.dp)
                            .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {})
                            .height(200.dp)
                            .fillMaxWidth()
                            .background(color = Color_gray_3F9, shape = RoundedCornerShape(16.dp)),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.title_message_shortcut),
                                style = MaterialTheme.typography.subtitle1.copy(
                                    color = Neutral_Gray_5, fontSize = 14.sp
                                ),
                            )
                        },
                        textStyle = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 14.sp
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color_gray_3F9,
                            disabledLabelColor = White,
                            focusedIndicatorColor = Transparent,
                            unfocusedIndicatorColor = Transparent,
                            cursorColor = Color.Black
                        )
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
                            onDismiss()
                            description = null
                        },
                        enableConfirm = enableConfirm,
                        onConfirm = {
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
            onDismiss()
            description = null
        },
        btnConfirmCallback = {
            description?.let { onConfirm.invoke(it) }
            onDismiss()
            description = null
        })


}