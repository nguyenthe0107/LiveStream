package olmo.wellness.android.ui.common.components.upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun UploadIInstructionUI(requestImage: ((Boolean) -> Unit)?= null,
                         requestDocument: ((Boolean) -> Unit)?= null){
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.uploading_instructions),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = marginDouble, vertical = padding_10)
        )
        Text(
            text = stringResource(id = R.string.uploading_instructions_update_decs),
            color = Neutral_Gray_7,
            style = MaterialTheme.typography.body2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = marginDouble, vertical = padding_10)
        )
        Divider(
            color = Neutral_Gray_3,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = marginDouble,
                    end = marginDouble,
                    top = marginStandard,
                    bottom = marginStandard
                )
                .height(defaultHeightButton)
                .noRippleClickable {
                    requestImage?.invoke(true)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    painter = painterResource(R.drawable.ic_photo),
                    contentDescription = "",
                    modifier = Modifier
                        .size(defaultSizeImage)
                )
                SpaceHorizontalCompose(width = 5.dp)
                Text(
                    text = "Photos",
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = Neutral_Gray_9
                )
            }
            Row{
                Image(
                    painter = painterResource(R.drawable.ic_baseline_arrow_right_24),
                    contentDescription = "",
                    modifier = Modifier
                        .size(defaultSizeImage)
                )
            }
        }

        Divider(
            color = Neutral_Gray_3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp)
                .height(1.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = marginDouble,
                    end = marginDouble,
                    top = marginStandard,
                    bottom = marginStandard
                )
                .height(defaultHeightButton)
                .noRippleClickable {
                    requestDocument?.invoke(true)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    painter = painterResource(R.drawable.ic_document),
                    contentDescription = "",
                    modifier = Modifier
                        .size(defaultSizeImage)
                )
                SpaceHorizontalCompose(width = 5.dp)
                Text(
                    text = "Files",
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = Neutral_Gray_9
                )
            }
            Row{
                Image(
                    painter = painterResource(R.drawable.ic_baseline_arrow_right_24),
                    contentDescription = "",
                    modifier = Modifier
                        .size(defaultSizeImage)
                )
            }
        }
    }
}