package olmo.wellness.android.ui.screen.playback_video.common

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.live_stream.LiveBadge
import olmo.wellness.android.ui.screen.playback_video.explore.OlboardHomeInteractionEvents
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.Transparent
import olmo.wellness.android.ui.theme.White
import java.util.concurrent.TimeUnit

@ExperimentalFoundationApi
@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun HeaderOnLiveStream(
    modifier: Modifier,
    livestreamInfo: LiveSteamShortInfo?,
    callbackClose: (() -> Unit)? = null,
    callbackCollapsed: (() -> Unit)? = null,
    isLiveStream: Boolean,
    isShowInfo: Boolean,
    usersSeeWatch: Int?,
    interactionEvents: ((OlboardHomeInteractionEvents) -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .background(color = Transparent)
            .padding(start = 16.dp, end = 16.dp, top = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Row(modifier = Modifier.weight(1f)
            .clickable {
                interactionEvents?.invoke(OlboardHomeInteractionEvents.ShowInfoStream)
            }, verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLiveStream) {
                LiveBadge(modifier = Modifier, hideIcon = true)
                SpaceHorizontalCompose(width = 8.dp)
            }
            val contentTitle = if(livestreamInfo?.title.orEmpty().length > 21){
                livestreamInfo?.title?.substring(0,20).plus("...")
            }else{
                livestreamInfo?.title
            }
            Text(
                text = contentTitle.orEmpty(),
                style = MaterialTheme.typography.subtitle2.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    lineHeight = 26.sp
                ),
                maxLines = 2,
                modifier = Modifier.wrapContentWidth()
            )
            SpaceHorizontalCompose(width = 4.dp)
            if(livestreamInfo?.description?.isNotEmpty() == true){
                val rotate = (if (isShowInfo) 0f  else 180f)
                AsyncImage(
                    model = R.drawable.olmo_ic_nav_down_white,
                    contentDescription = "",
                    modifier = Modifier.rotate(rotate).size(16.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .defaultMinSize(minHeight = 23.dp)
                .noRippleClickable {}) {
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

