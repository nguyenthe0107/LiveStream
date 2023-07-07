package olmo.wellness.android.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.Shapes

@Composable
fun DividerWithElevation(){
    Surface(
        elevation = 1.dp,
        color = MaterialTheme.colors.surface,
        shape = Shapes.medium){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            elevation = 2.dp,
            shape = Shapes.medium
        ) {}
    }
}