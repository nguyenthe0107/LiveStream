package olmo.wellness.android.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.Color_Green_Main
import olmo.wellness.android.ui.theme.White

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color_Green_Main,
                shape = RoundedCornerShape(8.dp)
            )
    ){
        Text(
            text = text,
            style = MaterialTheme.typography.button.copy(
                color = White
            ),
            modifier = Modifier
                .wrapContentSize()
                .padding(
                    vertical = 10.dp
                )
        )
    }
}