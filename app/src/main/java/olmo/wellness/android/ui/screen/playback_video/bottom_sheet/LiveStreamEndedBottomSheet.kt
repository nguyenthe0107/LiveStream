package olmo.wellness.android.ui.screen.playback_video.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.SecondaryButton
import olmo.wellness.android.ui.common.components.PrimaryButton
import olmo.wellness.android.ui.theme.*

@ExperimentalMaterialApi
@Composable
fun LiveStreamEndedBottomSheet(
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
    ) {
        Text(
            text = "Live Video Ended",
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1
        )

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray_EF3)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    start = 16.dp,
                    top = 16.dp
                )
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_simple_chart),
                contentDescription = "",
                tint = Neutral_Gray_9
            )
            Text(
                text = "View Insight",
                modifier = Modifier.padding(
                    start = 10.dp
                ),
                style = MaterialTheme.typography.body2.copy(
                    color = Neutral_Gray_9
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 48.dp
            )
                .fillMaxWidth()
                .wrapContentHeight()
        ){
            IndexItem(
                500,
                "View"
            )
            IndexItem(
                100,
                "Like"
            )
            IndexItem(
                5000,
                "Comment"
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 48.dp
            )
                .fillMaxWidth()
                .wrapContentHeight()
        ){
            Text(
                text = "Duration",
                style = MaterialTheme.typography.body2.copy(
                    color = Neutral_Gray_9
                )
            )
            Text(
                text = "01:10:10",
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color_Green_Main
                ),
                textAlign = TextAlign.Center
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray_EF3)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(
                   horizontal = 16.dp,
                    vertical = 22.dp
                )
                .fillMaxWidth()
                .wrapContentHeight()

        ) {
            SecondaryButton(
                text = "Discard video",
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        end = 4.dp
                    ),
                color = RED_000
            )

            PrimaryButton(
                text = "Save video",
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 4.dp
                    )
            )
        }
    }
}

@Composable
private fun IndexItem(
    count: Int,
    title: String
){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Bold,
                color = Color_Green_Main
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = title, style = MaterialTheme.typography.caption.copy(
                color = Neutral_Gray_7
            ),
            textAlign = TextAlign.Center
        )
    }
}