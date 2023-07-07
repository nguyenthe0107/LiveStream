package olmo.wellness.android.ui.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import olmo.wellness.android.ui.screen.signup_screen.utils.BottomBarCompose

@Composable
fun BottomBarNormal(){
    Box(modifier = Modifier){
        Row(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            BottomBarCompose()
        }
    }
}