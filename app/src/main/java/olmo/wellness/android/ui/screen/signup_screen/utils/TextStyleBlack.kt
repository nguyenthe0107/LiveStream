package olmo.wellness.android.ui.screen.signup_screen.utils

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import olmo.wellness.android.ui.theme.MontserratFont
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import olmo.wellness.android.ui.theme.heightText_20

@Composable
fun TextStyleBlack(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.subtitle1,
    colors: Color = Neutral_Gray_9,
    content: String = "",
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = content, style = style.copy(
            color = colors,
            fontFamily = MontserratFont,
            lineHeight = heightText_20,
            fontWeight = fontWeight,
            textAlign = textAlign
        )
    )
}