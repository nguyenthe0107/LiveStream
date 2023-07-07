package olmo.wellness.android.ui.livestream.schedule.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.ui.common.components.live_button.CoupleLiveButton
import olmo.wellness.android.ui.livestream.schedule.component.UpcomingItemCompose
import olmo.wellness.android.ui.theme.Black_037
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Color_gray_FF7
import olmo.wellness.android.ui.theme.White

@Composable
fun WarningDoubleScheduleBottomSheet(livestreamInfo: LivestreamInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color_gray_FF7,
                shape = RoundedCornerShape(
                    topEnd = 24.dp,
                    topStart = 24.dp
                )
            )
            .wrapContentHeight()

    ){
        Title()
        Subtitle()
        UpcomingItemCompose(
            livestreamInfo,
            modifier = Modifier
                .padding(
                    top = 24.dp
                )
                .padding(
                    horizontal = 16.dp
                )
        )
        CoupleLiveButton(
            modifier = Modifier
                .padding(
                    top = 40.dp
                ),
            confirmText = "Delete",
            cancelText = "Cancel",
            confirmCallback = {

            },
            cancelCallback = {

            }
        )
    }
}

@Composable
private fun Subtitle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
               horizontal = 16.dp
            )
            .padding(
                top = 4.dp
            )
    ){
        Text(
            text = "Do you want to delete?",
            style = MaterialTheme.typography.body1.copy(
                color = Black_037,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun Title() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                Color_gray_FF7,
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .padding(
                horizontal = 16.dp
            )
            .padding(
                top = 20.dp
            )
    ){
        Text(
            text = "Double Schedule",
            style = MaterialTheme.typography.body1.copy(
                color = Color_LiveStream_Main_Color,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}