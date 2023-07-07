package olmo.wellness.android.ui.screen.search_home.cell

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.core.formatToK
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.common.avatar.Avatar
import olmo.wellness.android.ui.common.validate.provideThumbnailUrl
import olmo.wellness.android.ui.theme.*

@Composable
fun ItemSearch(data: LiveSteamShortInfo, onClickCallback: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickCallback?.invoke()
            }, verticalAlignment = Alignment.CenterVertically
    ) {

        RoundedAsyncImage(
            imageUrl = data.thumbnailUrl ?: "", cornerRadius = 10.dp,
            modifier = Modifier
                .size(95.dp),
            size = 95.dp,
        )
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
                    HeaderItem(modifier = Modifier, liveStreamInfo = data)
                    Text(
                        text = data.title.orEmpty(),
                        modifier = Modifier.padding(horizontal = 3.dp),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 14.sp
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {

                    TextIcon(text = data.viewCount?.formatToK() ?: "0", R.drawable.ic_eye)

                    TextIcon(text = data.heartCount?.formatToK() ?: "0", R.drawable.ic_heart_search)
                    TextIcon(
                        text = data.roomChat?.totalMessage?.formatToK() ?: "0",
                        R.drawable.ic_comments_search
                    )

                    if (data.isShowIconReminder == true) {
                        Box(modifier = Modifier.weight(1f)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_notification_search),
                                tint = RED_F65,
                                contentDescription = "Notification",
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderItem(
    modifier: Modifier,
    liveStreamInfo: LiveSteamShortInfo? = null
) {
    Box(
        modifier = modifier
            .background(
                color = Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .wrapContentSize()
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                imageUrl = liveStreamInfo?.user?.avatar,
                name = getNameUserChat(liveStreamInfo?.user),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun TextIcon(text: String, icon: Int) {
    Column(
        modifier = Modifier.padding(end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,

            modifier = Modifier.size(10.dp)
        )
        Text(
            text = text, style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 10.sp
            )
        )
    }
}