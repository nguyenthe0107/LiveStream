package olmo.wellness.android.ui.screen.playback_video.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.live_stream.LiveBadge
import olmo.wellness.android.ui.screen.playback_video.explore.OlboardHomeInteractionEvents
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.Transparent

@Composable
fun LiveStreamIconsSectionTop(
    modifier: Modifier,
    isStreaming: Boolean = false,
    title: String,
    isShowInfo: Boolean,
    usersSeeWatch: Int?,
    livestreamInfo: LivestreamInfo? = null,
    interactionEvents: ((OlboardHomeInteractionEvents) -> Unit)? = null
) {


    Row(
        modifier = modifier
            .background(color = Transparent)
            .padding(start = 25.dp, end = 25.dp, top = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        if (isStreaming) {
            Row(modifier = Modifier
                .weight(1f)
                .clickable {
                    interactionEvents?.invoke(OlboardHomeInteractionEvents.ShowInfoStream)
                }, verticalAlignment = Alignment.CenterVertically) {
                LiveBadge(modifier = Modifier, hideIcon = true)
                SpaceHorizontalCompose(width = 8.dp)
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        lineHeight = 26.sp
                    ),
                    maxLines = 2
                )
                SpaceHorizontalCompose(width = 4.dp)
                val rotate = (if (isShowInfo)  180f else 0f)
                Image(
                    painter = painterResource(id = R.drawable.olmo_ic_nav_down_white),
                    contentDescription = "",
                    modifier = Modifier.rotate(rotate)
                )
            }
        }else{
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .defaultMinSize(minHeight = 23.dp)) {
            if (isStreaming) {
                Box(
                    modifier = Modifier
                        .background(
                            Color.Black.copy(
                                alpha = 0.2f
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .noRippleClickable {
                            interactionEvents?.invoke(OlboardHomeInteractionEvents.ShowListUser)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.olmo_ic_user_filled),
                            contentDescription = ""
                        )
                        SpaceHorizontalCompose(width = 4.dp)
                        Text(
                            text = usersSeeWatch?.toString() ?: "0",
                            style = MaterialTheme.typography.subtitle2.copy(
                                color = Color.White
                            )
                        )
                    }
                }
            }
            SpaceHorizontalCompose(width = 18.dp)
            Icon(
                painter = painterResource(id = R.drawable.olmo_ic_close_white),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .noRippleClickable {
                        interactionEvents?.invoke(OlboardHomeInteractionEvents.CloseLiveStream)
                    }
            )
        }
    }
}
