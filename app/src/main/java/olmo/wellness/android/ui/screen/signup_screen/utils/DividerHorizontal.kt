package olmo.wellness.android.ui.screen.signup_screen.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.Neutral_Gray_4

@Composable
fun DividerHorizontal(thickness: Dp = 1.dp, color: Color = Neutral_Gray_4) {
    Divider(
        color = color,
        modifier = Modifier
            .fillMaxWidth(),
        thickness = thickness
    )
}