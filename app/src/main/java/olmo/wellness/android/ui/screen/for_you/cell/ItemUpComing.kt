package olmo.wellness.android.ui.screen.for_you.cell

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.common.avatar.Avatar
import olmo.wellness.android.ui.common.live_stream.LiveNotification
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.theme.*

@Composable
fun ItemUpComing(
    modifier: Modifier = Modifier,
    data: LiveSteamShortInfo,
    onItemClick: (LiveSteamShortInfo) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                onItemClick.invoke(data)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(95.dp)) {
            RoundedAsyncImage(
                imageUrl = data.thumbnailUrl ?: "", cornerRadius = 10.dp,
                size = 95.dp,
                modifier = Modifier
                    .size(95.dp)

            )
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Black_466.copy(
                            alpha = 0.2f
                        ), shape = RoundedCornerShape(10.dp)
                    )
            )

            Text(
                text = DateTimeHelper.convertToStringHour(data.createdAt).uppercase(),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 16.sp, color = White
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(75.dp)
                .background(
                    color = Neutral_Gray_14,
                    shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 15.dp,
                        vertical = 10.dp
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    data.title?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(end = 3.dp),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 14.sp
                            )
                        )
                    }
                    TagEvent()
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(
                        imageUrl = data.user?.avatar,
                        name = getNameUserChat(data.user),
                        modifier = Modifier.size(28.dp)
                    )

                    Text(
                        text = data.user?.name ?: "",
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 10.sp, color = Black_466
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        LiveNotification(
                            modifier = Modifier.align((Alignment.CenterEnd)),
                            onClick = {

                            })
                    }
                }
            }

        }
    }
}

@Composable
fun TagEvent() {
    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .background(color = ERROR_BACKGROUND_RESUBMIT, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp)

    ) {
        Text(
            text = stringResource(id = R.string.lb_event),
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 10.sp, color = RED_F65
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}