package olmo.wellness.android.ui.common.empty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.Transparent

@Composable
fun EmptyData(resource: Int = R.string.lb_no_data, retry: (() -> Unit)? = null) {

    val content = stringResource(id = resource)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 42.dp, end = 42.dp, top = 150.dp)
//                .wrapContentHeight()
        ,
            verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = R.drawable.olmo_ic_group_default_welcome_live_stream,
            contentDescription = "setting_profile",
            modifier = Modifier
                .size(134.dp, 160.dp)
                .noRippleClickable {
                    retry?.invoke()
                }
        )
        SpaceCompose(height = 16.dp)
        Text(
            text = content, style = MaterialTheme.typography.subtitle1.copy(
                lineHeight = 16.sp,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            ), modifier = Modifier.noRippleClickable {
                retry?.invoke()
            }
        )
    }
}

@Composable
fun EmptyBottomSheet(){
    Spacer(modifier = Modifier.height(1.dp).background(Transparent))
}