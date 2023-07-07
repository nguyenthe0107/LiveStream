package olmo.wellness.android.ui.livestream.statistics.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import olmo.wellness.android.ui.theme.cornerRadius
import olmo.wellness.android.ui.theme.marginDouble
import olmo.wellness.android.ui.theme.marginStandard

@Composable
fun StatisticsLivestreamItem(data : String) {
    Card(modifier = Modifier.padding(horizontal = marginStandard, vertical = marginStandard).fillMaxWidth(),
    elevation =cornerRadius, backgroundColor = Color.White,
    shape = RoundedCornerShape(corner = CornerSize(cornerRadius))
    ) {
        Row(modifier = Modifier.clickable {

        }
        ) {
            Column(modifier = Modifier
                .padding(marginDouble)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)) {
                Text(text = data, style = MaterialTheme.typography.h6)
                Text(text = "View detail", style = MaterialTheme.typography.caption)
            }
        }
    }

}