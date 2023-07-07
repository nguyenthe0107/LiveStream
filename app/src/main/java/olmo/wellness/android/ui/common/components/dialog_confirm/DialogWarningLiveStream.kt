package olmo.wellness.android.ui.common.components.dialog_confirm

import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog

@Composable
fun DialogWarningLiveStream(
    openDialogCustom: MutableState<Boolean>,
    confirmCallback: ((Boolean) -> Unit)? = null,
    cancelCallback: ((Boolean) -> Unit)? = null
) {
    if (openDialogCustom.value) {
        Dialog(onDismissRequest = { openDialogCustom.value = false }) {
            WarningUI(cancelCallback = {
                cancelCallback?.invoke(it)
            }, confirmCallback = {
                confirmCallback?.invoke(it)
            })
        }
    }
}