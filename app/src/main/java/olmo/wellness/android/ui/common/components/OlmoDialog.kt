package olmo.wellness.android.ui.common.components
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun OlMoAlertDialog(
    show: Boolean,
    onDismiss: (() -> Unit) ?=null,
    onConfirm: (() -> Unit) ?= null,
    content: String = ""
) {
    if (show) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {},
            dismissButton = {
                if (onDismiss != null) {
                    TextButton(onClick = onDismiss)
                    { Text(text = "Cancel") }
                }
            },
            title = { Text(text = "Dialog Error") },
            text = { Text(text = content ) }
        )
    }
}