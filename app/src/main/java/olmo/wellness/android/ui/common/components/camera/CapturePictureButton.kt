package olmo.wellness.android.ui.common.components.camera

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.*

@Composable
fun CapturePictureButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color = if (isPressed) Tiffany_Blue_500 else Color.White
    val contentPadding = PaddingValues(if (isPressed) marginStandard else padding_12)
    OutlinedButton(
        modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(borderWidth_2dp, Color.White),
        contentPadding = contentPadding,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
        onClick = { /* GNDN */ },
        enabled = false
    ) {
        Button(
            modifier = Modifier
                .fillMaxSize(),
            shape = CircleShape,
            border = BorderStroke(defaultBorderWidth, Color.Black),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = color
            ),
            interactionSource = interactionSource,
            onClick = onClick
        ) {
            // No content
        }
    }
}

@Preview
@Composable
fun PreviewCapturePictureButton() {
    Scaffold(
        modifier = Modifier
            .size(125.dp)
            .wrapContentSize()
    ) { innerPadding ->
        CapturePictureButton(
            modifier = Modifier
                .padding(innerPadding)
                .size(100.dp)
        )
    }
}
