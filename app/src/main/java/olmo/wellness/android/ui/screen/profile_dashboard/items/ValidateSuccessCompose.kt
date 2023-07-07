package olmo.wellness.android.ui.screen.profile_dashboard.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.*

@Composable
fun ValidateSuccessCompose(content: String? = "",horizontalDp: Dp = zeroDimen, verticalDp: Dp = zeroDimen){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = horizontalDp, vertical = verticalDp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.olmo_ic_check_circle_blue_filled),
            contentDescription = "check",
            modifier = Modifier
                .size(sizeIcon_16)
                .padding(end = marginMinimum)
        )
        val contentFinal = if(content.isNullOrEmpty()){
            stringResource(id = R.string.available)
        }else{
            content
        }
        Text(
            text = contentFinal,
            color = Color_BLUE_OF8,
            style = MaterialTheme.typography.overline,
            overflow = TextOverflow.Ellipsis
        )
    }
}