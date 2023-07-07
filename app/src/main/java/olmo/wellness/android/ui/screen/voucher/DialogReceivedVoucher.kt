package olmo.wellness.android.ui.screen.voucher

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import olmo.wellness.android.R

@Composable
fun DialogReceivedVoucher(
    openDialogCustom: MutableState<Boolean>,
    confirmCallback: ((Boolean) -> Unit)? = null
) {
    if (openDialogCustom.value) {
        Dialog(onDismissRequest = { openDialogCustom.value = false }) {
            UIReceivedVoucher()
        }
    }
}

@Composable
fun UIReceivedVoucher(){
    Box(modifier = Modifier.fillMaxSize()){
        AsyncImage(model = R.drawable.bg_received_coupon, contentDescription = "", modifier = Modifier.align(
            Alignment.Center))
    }
}