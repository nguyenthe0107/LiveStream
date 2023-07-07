package olmo.wellness.android.ui.common.components.dialog_confirm

import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import olmo.wellness.android.R

@Composable
fun DialogConfirmed(
    openDialogCustom: MutableState<Boolean>,
    confirmCallback: ((Boolean) -> Unit)? = null,
    cancelCallback: ((Boolean) -> Unit)? = null
) {
    if (openDialogCustom.value) {
        Dialog(onDismissRequest = { openDialogCustom.value = false }) {
            ConfirmedUI(cancelCallback = {
                cancelCallback?.invoke(it)
            }, confirmCallback = {
                confirmCallback?.invoke(it)
            })
        }
    }
}


@Composable
fun DialogAction(
    openDialogCustom: MutableState<Boolean>,
    title: String,
    description: String,
    titleBtnCancel: String = stringResource(id = R.string.cancel),
    titleBtnConfirm: String = stringResource(id = R.string.save),
    btnCancelCallback: ((Boolean) -> Unit)? = null,
    btnConfirmCallback: ((Boolean) -> Unit)? = null
) {
    if (openDialogCustom.value) {
        Dialog(onDismissRequest = { openDialogCustom.value = false }) {
            ActionDialogUI(
                title = title,
                description = description,
                titleBtn1 = titleBtnCancel,
                titleBtn2 = titleBtnConfirm,
                btn1Callback = {
                    openDialogCustom.value = false
                    btnCancelCallback?.invoke(true)
                },
                btn2Callback = {
                    openDialogCustom.value = false
                    btnConfirmCallback?.invoke(true)
                })
        }
    }
}