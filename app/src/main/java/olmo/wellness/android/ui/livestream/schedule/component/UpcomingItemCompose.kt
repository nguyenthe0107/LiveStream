package olmo.wellness.android.ui.livestream.schedule.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.Color_Green_Main
import olmo.wellness.android.ui.theme.Color_gray_FF7
import olmo.wellness.android.ui.theme.White

@Composable
fun UpcomingItemCompose(
    livestreamInfo: LivestreamInfo,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color_gray_FF7,
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
//                .defaultMinSize(minHeight = 92.dp)
//                .background(Color_gray_FF7)
                .padding(16.dp)
//                .background(White)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedAsyncImage(
                imageUrl = livestreamInfo.thumbnailUrl ?: "",
                cornerRadius = 16.dp,
                size = 60.dp,
                modifier = Modifier
                    .size(60.dp)
            )

            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f)
            ) {
                Text(
                    text = livestreamInfo.title ?: "How to sleep well",
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 14.sp, lineHeight = 22.sp,
                        color = Color_Green_Main
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.olmo_ic_calendar_filled),
                        "olmo_ic_calendar_filled"
                    )
                    SpaceHorizontalCompose(width = 5.dp)
                    val timeCreated = DateTimeHelper.convertToStringDate(
                        livestreamInfo.createdAt,
                        DateTimeHelper.ddMMMyy
                    ).replace(",", "")
                    Text(
                        text = timeCreated,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 12.sp, lineHeight = 18.sp
                        )
                    )
                    SpaceHorizontalCompose(width = 15.dp)
                    Image(
                        painter = painterResource(id = R.drawable.olmo_ic_clock_filled),
                        "olmo_ic_calendar_filled"
                    )
                    SpaceHorizontalCompose(width = 5.dp)
                    Text(
                        text = DateTimeHelper.convertToStringHour(livestreamInfo.startTime),
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 12.sp, lineHeight = 18.sp
                        )
                    )
                }
            }
        }


    }
}
