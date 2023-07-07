package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import olmo.wellness.android.ui.theme.*

@Composable
fun ButtonNextStep(buttonText: String, isEnable: Boolean = true, onSubmit: () -> Unit) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .background(Neutral_Gray)
            .height(space_20)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = marginDouble, vertical = padding_10)
        ) {
            Button(
                onClick = {
                    if (isEnable)
                        onSubmit()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(defaultHeightButton),
                shape = RoundedCornerShape(marginStandard),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isEnable) Color_Green_Main else Neutral_Bare_Gray
                )
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.button,
                    overflow = TextOverflow.Ellipsis,
                    color = White
                )
            }
        }
    }
}