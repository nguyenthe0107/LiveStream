package olmo.wellness.android.ui.common.components.upload

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog

@Composable
fun DialogUploadInstruction(
    openDialogCustom: MutableState<Boolean>,
    onRequestImage: ((Boolean) -> Unit)? = null,
    onRequestDocument: ((Boolean) -> Unit)? = null
) {
    if(openDialogCustom.value){
        Dialog(onDismissRequest = { openDialogCustom.value = false }) {
            UploadIInstructionUI(requestImage = onRequestImage, requestDocument = onRequestDocument)
        }
    }
}