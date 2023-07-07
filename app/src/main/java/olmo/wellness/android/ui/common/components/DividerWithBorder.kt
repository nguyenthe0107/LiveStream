package olmo.wellness.android.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.Color_BLUE_OF8

@Composable
fun DividerWithBorder(modifier: Modifier){
    Column(modifier = modifier
        .width(8.dp)
        .background(Color_BLUE_OF8, shape = RoundedCornerShape(5.dp))
        .height(72.dp)){
    }

}