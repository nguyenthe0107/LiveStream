package olmo.wellness.android.ui.screen.playback_video.common

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.live_stream.carousel.OlmoLiveStreamPlayer
import olmo.wellness.android.ui.common.live_stream.carousel.OlmoPlayer
import olmo.wellness.android.ui.livestream.chatlivestream.state.ChatLivestreamUiState
import olmo.wellness.android.ui.livestream.chatlivestream.view.ChatLivestreamContent
import olmo.wellness.android.ui.screen.playback_video.explore.OlboardHomeInteractionEvents

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalFoundationApi
@Composable
fun PagerItem(
    isLiveStream: Boolean? = null,
    startPlay: Boolean? = false,
    livestreamInfo: LiveSteamShortInfo,
    roomChat: ChatLivestreamUiState? = null,
    isComment: State<Boolean>? = null,
    isLike: Boolean?,
    isInputChat: MutableState<Boolean>? = null,
    isShowAddComment: MutableState<Boolean>? = null,
    msgSelect: MutableState<ChatMessage?>? = null,
    openDialogActionMessage: MutableState<Boolean>? = null,
    countHeart: Int? = null,
    countComment: Int? = null,
    countBookNow : Int?=null,
    selected: Boolean,
    interactionEvents: ((OlboardHomeInteractionEvents, LiveSteamShortInfo?) -> Unit)? = null,
    playbacks: LiveSteamShortInfo? = null,
    requestOpenPip: ((LiveSteamShortInfo?, Long) -> Unit)? = null,
    requestPauseAllVideo: (() -> Unit)? = null,
    requestPlayVideo: (() -> Unit)? = null,
    requestLoadMoreChat: (() -> Unit)? = null,
    onClosePlayer: ((Boolean) -> Unit)? = null,
    requestShowChat: (() -> Unit)? = null,
    serviceBookingShow : ServiceBooking?=null,
    numberServiceBookings: Int=0,
    onFinishedVideo: ((status: Boolean) -> Unit)? = null,
    transformFollowAction: Boolean? = false,
    showFollow: Boolean? = false,
    actionFollow: MutableState<Pair<Int, Boolean>>? = mutableStateOf(Pair(0, false)),
    showDescriptionVideo: Boolean? = false,
) {
    val durationVideo = remember {
        mutableStateOf(0L)
    }
    val context = LocalContext.current
    var isMute by remember {
        mutableStateOf(false)
    }

    val bookingShow = remember {
        mutableStateOf<ServiceBooking?>(null)
    }
    bookingShow.value= serviceBookingShow

    Box(modifier = Modifier.fillMaxSize()) {
        playbacks?.recordUrl?.let {
            if (isLiveStream == true) {
                OlmoLiveStreamPlayer(
                    context = context,
                    url = it,
                    selected = selected,
                    isMute = false,
                    idVideo = livestreamInfo.id,
                    onFinishedVideo = onFinishedVideo)
            } else {
                OlmoPlayer(
                    context,
                    it,
                    selected,
                    isLiveStream,
                    startPlay,
                    durationVideo,
                    isMute,
                    onFinishedVideo
                )
            }
        }
        HeaderOnLiveStream(
            modifier = Modifier.fillMaxWidth(),
            livestreamInfo = livestreamInfo,
            isLiveStream = isLiveStream ?: false,
            interactionEvents = {
                when (it) {
                    OlboardHomeInteractionEvents.CloseLiveStream -> {
                        onClosePlayer?.invoke(true)
                    }
                    OlboardHomeInteractionEvents.OpenComments -> {
                        requestShowChat?.invoke()
                    }
                    else -> {
                        interactionEvents?.invoke(it, livestreamInfo)
                    }
                }
            },
            isShowInfo = showDescriptionVideo ?: false,
            usersSeeWatch = roomChat?.getCountUser()?.collectAsState()?.value,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 56.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            VideoIconsSection(
                livestreamInfo = livestreamInfo,
                countHeart = countHeart,
                isLike = isLike,
                countComment = countComment,
                countBookNow= countBookNow,
                interactionEvents = interactionEvents,
                requestPauseAllVideo = requestPauseAllVideo,
                durationVideo = durationVideo,
                showFollow = showFollow,
                transformFollowAction = transformFollowAction,
                actionFollow = actionFollow,
                numberServiceBookings = numberServiceBookings
            )
        }
        if (isLiveStream == null || isLiveStream == false) {
            VideoOverLayUI(
                livestreamInfo,
                modifier = Modifier.padding(
                    bottom = 64.dp
                ),
                showDescriptionVideo = showDescriptionVideo
            )
            VoiceOverLayUI(
                livestreamInfo, { event ->
                    if (event == OlboardHomeInteractionEvents.MuteSound) {
                        isMute = !isMute
                    }
                },
                modifier = Modifier.padding(
                    bottom = 20.dp
                )
            )
        }

        if (isComment?.value == true) {
            Box(
                modifier = Modifier
                    .padding(bottom = 50.dp, start = 15.dp)
                    .fillMaxSize()

            ) {
                isShowAddComment?.value?.let {
                    ChatLivestreamContent(uiState = roomChat,
                        onLoadMore = {
                            if (roomChat?.messages?.isNotEmpty() == true) {
                                requestLoadMoreChat?.invoke()
                            }
                        },
                        onComment = {
                            isInputChat?.value = true
                        },
                        modifier = Modifier.fillMaxHeight(),
                        isShowAddComment = it,
                        onTouchMessage = { msg ->
                            if (sharedPrefs.getUserInfoLocal().userId != msg.userId) {
                                msgSelect?.value = msg
                                if (openDialogActionMessage?.value != null) {
                                    openDialogActionMessage.value = !openDialogActionMessage.value
                                }
                            }
                        }, isBuyer = false, onCloseBook = {
                            interactionEvents?.invoke(OlboardHomeInteractionEvents.CloseShowBookService,null)

                        }, serviceBooking = serviceBookingShow,
                        onBookingCallback = {
                            interactionEvents?.invoke(OlboardHomeInteractionEvents.OnBookingService(serviceBookingShow),null)
                        }
                    )
                }
            }
        }
    }
}