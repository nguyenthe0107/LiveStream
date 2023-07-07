package olmo.wellness.android.ui.common

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.ui.theme.MontserratFont
import olmo.wellness.android.ui.theme.Tiffany_Blue_500

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CopyTextCompose(valueDefault: String? = "") {
    Box {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(62.dp)
                .border(
                    width = 1.dp, color = Color(0xFFDDDFE3),
                    shape = RoundedCornerShape(12.dp)
                )
                .align(Alignment.Center)
        ) {
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)) {
                Text(
                    text = valueDefault ?: "",
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = MontserratFont,
                        color = Color(0xFF303037),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(48.dp)
                    .width(105.dp)
                    .padding(2.dp)
            ) {
                val clipboardManager = LocalClipboardManager.current
                Button(
                    onClick = { clipboardManager.setText(AnnotatedString((valueDefault.toString()))) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Tiffany_Blue_500,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxHeight()
                        .padding(end = 2.dp)
                ) {
                    Text(
                        text = "Copy",
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontFamily = MontserratFont,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp
                        )
                    )
                }
            }
        }
    }
}