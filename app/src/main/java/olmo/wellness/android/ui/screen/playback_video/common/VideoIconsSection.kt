package olmo.wellness.android.ui.screen.playback_video.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.playback_video.explore.OlboardHomeInteractionEvents
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.RED_F65
import olmo.wellness.android.ui.theme.White

@Composable
fun VideoIconsSection(
    livestreamInfo: LiveSteamShortInfo? = null,
    countHeart: Int?,
    isLike: Boolean?,
    countComment: Int?,
    countBookNow: Int?,
    interactionEvents: ((OlboardHomeInteractionEvents, LiveSteamShortInfo?) -> Unit)? = null,
    requestPauseAllVideo: (() -> Unit)? = null,
    numberServiceBookings: Int=0,
    durationVideo: MutableState<Long>,
    showFollow: Boolean? = false,
    transformFollowAction: Boolean? = false,
    actionFollow: MutableState<Pair<Int, Boolean>>? = mutableStateOf(Pair(0, false)),
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileImageWithFollow(
            idVideo = livestreamInfo?.id,
            modifier = Modifier.size(40.dp),
            showFollow = showFollow ?: false,
            transformFollowAction = transformFollowAction,
            actionFollow = actionFollow,
            imageId = R.drawable.olmo_ic_group_default_place_holder,
            avatar = livestreamInfo?.user?.avatar,
            followActionCallBack = {
                interactionEvents?.invoke(
                    OlboardHomeInteractionEvents.FollowAction,
                    livestreamInfo
                )
            },
            unFollowActionCallBack = {
                interactionEvents?.invoke(
                    OlboardHomeInteractionEvents.UnFollowAction,
                    livestreamInfo
                )
            },
            navigationToProfile = {
                interactionEvents?.invoke(
                    OlboardHomeInteractionEvents.NavigationToFollowProfile,
                    livestreamInfo
                )
            }
        )
        SpaceCompose(height = 16.dp)
        if (livestreamInfo?.isLiveStream == true) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = R.drawable.ic_gift_donate,
                    contentDescription = null,
                    modifier = Modifier
                        .noRippleClickable {
                            interactionEvents?.invoke(
                                OlboardHomeInteractionEvents.GiftDonate, livestreamInfo
                            )
                        }
                        .size(40.dp)
                )
                Text(
                    text = "0",
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 12.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            SpaceCompose(height = 16.dp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LikeIcon(id = livestreamInfo?.id ?: -1, onCallbackFunc = {
                interactionEvents?.invoke(
                    OlboardHomeInteractionEvents.ReactionHeart, livestreamInfo
                )
            }, isLike = isLike)
            Text(
                text = (countHeart ?: 0).toString(),
                style = MaterialTheme.typography.body2.copy(fontSize = 12.sp, color = Color.White),
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        SpaceCompose(height = 16.dp)

        if (livestreamInfo?.isLiveStream == false) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = R.drawable.olmo_ic_chat_live,
                    contentDescription = null,
                    modifier = Modifier
                        .noRippleClickable {
                            interactionEvents?.invoke(
                                OlboardHomeInteractionEvents.OpenComments,
                                livestreamInfo
                            )
                        }
                        .size(40.dp)
                )
                Text(
                    text = (countComment ?: 0).toString(),
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 12.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            SpaceCompose(height = 16.dp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = R.drawable.ic_mini_screen,
                contentDescription = "ic_mini_screen",
                modifier = Modifier
                    .noRippleClickable {
                        requestPauseAllVideo?.invoke()
                        interactionEvents?.invoke(
                            OlboardHomeInteractionEvents.OpenPipMode(
                                livestreamInfo,
                                durationVideo.value
                            ), livestreamInfo
                        )
                    }
                    .size(40.dp)
            )
        }
        SpaceCompose(height = 16.dp)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = R.drawable.olmo_ic_more_option,
                contentDescription = "option",
                modifier = Modifier
                    .noRippleClickable {
                        interactionEvents?.invoke(
                            OlboardHomeInteractionEvents.MoreOption,
                            livestreamInfo
                        )
                    }
                    .size(40.dp)
            )
        }
        //livestreamInfo?.isLiveStream == true &&
        if (numberServiceBookings>0) {
            SpaceCompose(height = 16.dp)
            Column(modifier = Modifier
                .noRippleClickable {
                    interactionEvents?.invoke(
                        OlboardHomeInteractionEvents.BookNowService, livestreamInfo
                    )
                },
                horizontalAlignment = Alignment.CenterHorizontally) {

                Box(modifier = Modifier.size(40.dp)) {
                    Image(painter = painterResource(id = R.drawable.ic_bag),
                        contentDescription = "bag")

                    Box(modifier = Modifier
                        .padding(4.dp)
                        .size(16.dp)
                        .align(Alignment.TopEnd)
                        .background(color = RED_F65, shape = CircleShape)) {

                        Text(text = numberServiceBookings.toString(),
                            modifier = Modifier
                                .align(Alignment.Center),
                            style = MaterialTheme.typography.subtitle2.copy(
                                color = White, fontSize = 8.sp, lineHeight = 10.sp
                            ))
                    }
                }

                androidx.compose.material3.Text(text = stringResource(id = R.string.lb_book_now),
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = White, fontSize = 8.sp, lineHeight = 16.sp
                    ))

            }
        }

    }
}
