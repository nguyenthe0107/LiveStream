package olmo.wellness.android.ui.common.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.MontserratFont
import olmo.wellness.android.ui.theme.zeroDimen

@Composable
fun ErrorCompose(modifier: Modifier = Modifier,
                 content: String?,
                 iconErrorResource: Int?,
                 paddingStart: Dp = zeroDimen,
                 paddingEnd: Dp = zeroDimen
                 ){
    Column(modifier = Modifier
        .padding(start = 14.dp, end = 14.dp, top = paddingStart, bottom = paddingEnd)
        .fillMaxWidth()
        .background(color = Color(0xFFFFF2EC))
        .wrapContentHeight()){
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = Color(0xFFFFF2EC),
            modifier = modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            border = BorderStroke(1.dp, color = Color.Transparent)
            ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painter = painterResource(id = iconErrorResource ?: R.drawable.ic_error_validate)
                Image(painter = painter, modifier = Modifier.padding(end = 10.dp),
                    contentDescription = "", contentScale = ContentScale.Crop)
                Text(
                    text = content.orEmpty(),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Medium,
                        fontFamily = MontserratFont,
                        color = Color(0xFF303037)
                    )
                )
            }
        }

    }
}