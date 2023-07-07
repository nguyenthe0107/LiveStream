package olmo.wellness.android.ui.screen.profile_dashboard.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.*

@Composable
fun InvalidateCompose(isError: Boolean = false,
                      contentNormal: String?=null,
                      contentError: String?=null,
                      iconDefault: Int?,
                    paddingStart : Dp = zeroDimen, paddingEnd: Dp = zeroDimen){
    Row(modifier = Modifier.fillMaxWidth()
        .padding(start = paddingStart, end = paddingEnd),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start){
        if(isError){
            Image(
                painter = painterResource(id = iconDefault ?: R.drawable.ic_error_verify_pw),
                contentDescription = "check",
                modifier = Modifier
                    .size(sizeIcon_16)
                    .padding(end = 2.dp)
            )
            if(contentError != null){
                Text(
                    text = contentError,
                    color = Error_500,
                    style = MaterialTheme.typography.overline,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }else{
            if(contentNormal != null){
                Text(
                    text = contentNormal,
                    color = Neutral_Gray_7,
                    style = MaterialTheme.typography.overline,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
}