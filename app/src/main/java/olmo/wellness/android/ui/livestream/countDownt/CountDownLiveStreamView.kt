package olmo.wellness.android.ui.livestream.countDownt

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import olmo.wellness.android.ui.theme.Color_Purple_FBC
import olmo.wellness.android.ui.theme.Success_500

@Composable
fun CountDownLiveStreamView(
    time: Int,
    max: Int,
    modifier: Modifier = Modifier){
    Box(modifier.background(Color.Transparent, shape = CircleShape)) {
        CircularProgressBar(percentage = time.toFloat())
        Canvas(modifier = Modifier
            .size(50.dp)
            .align(Alignment.Center)
            , onDraw = {
            val size = 50.dp.toPx()
            drawCircle(
                color = Color_Purple_FBC,
                radius = size
            )
        })
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = "$time", style = MaterialTheme.typography.subtitle2.copy(
                color = Color.White,
                fontSize = 30.sp
            ),
        )
    }
}
