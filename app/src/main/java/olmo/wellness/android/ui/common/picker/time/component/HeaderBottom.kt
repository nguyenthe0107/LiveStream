package olmo.wellness.android.ui.common.picker.time.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.ui.theme.Color_Purple_FBC

@Composable
fun HeaderBottom(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 16.sp, color = Color_Purple_FBC
            ),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}