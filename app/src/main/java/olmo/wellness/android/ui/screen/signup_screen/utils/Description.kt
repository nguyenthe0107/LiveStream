package olmo.wellness.android.ui.screen.signup_screen.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun Description(content : String, onError: Boolean ?= null){
    val modifier = Modifier.padding(top = 0.dp)
    Text(text = content,
        modifier = modifier,
        style = TextStyle(
            color = if(onError != null){
                if(onError == true){
                    Color(0xFFFF3236)
                }else{
                    Color(0xFF5E5E60)
                }
            }else{
                Color(0xFF5E5E60)
            },
            fontStyle = FontStyle.Normal,
            lineHeight = 16.sp,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.01.em,
            fontWeight = FontWeight(500),
            fontSize = 10.sp,
        )
    )
}