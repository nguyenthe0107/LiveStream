package olmo.wellness.android.ui.screen.playback_video.onlive

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.maybeShowPipActivity
import olmo.wellness.android.core.toJson
import olmo.wellness.android.core.utils.shareLinkVideo
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.ChatObject
import olmo.wellness.android.data.model.chat.CountType
import olmo.wellness.android.domain.model.booking.BookingWrapperInfo
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.WrapperUrlPayment
import olmo.wellness.android.domain.model.booking.toServiceBooking
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.payment.PaymentRequireWrapper
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.ui.analytics.AnalyticsKey
import olmo.wellness.android.ui.booking.book_now.view.BookNowBottomSheet
import olmo.wellness.android.ui.booking.book_now_livestream.view.BookNowBuyerBottomSheet
import olmo.wellness.android.ui.booking.book_now_livestream.view.CartBookingBottomSheet
import olmo.wellness.android.ui.booking.detail.view.ServiceBookingDetailBottomSheet
import olmo.wellness.android.ui.booking.viewmodel.BookingPlayBackViewModel
import olmo.wellness.android.ui.chat.conversation_detail.ConversationDetailBottomSheet
import olmo.wellness.android.ui.chat.private_chat.viewmodel.PrivateChatViewModel
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogFinishedLiveStream
import olmo.wellness.android.ui.common.live_stream.carousel.Pager
import olmo.wellness.android.ui.common.live_stream.carousel.PagerState
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.livestream.chatlivestream.view.InputChatLiveStream
import olmo.wellness.android.ui.livestream.chatlivestream.view.dismissActionMessage
import olmo.wellness.android.ui.livestream.info_livestream.ActionFollowDialog
import olmo.wellness.android.ui.livestream.chatlivestream.viewmodel.ChatLivestreamViewModel
import olmo.wellness.android.ui.livestream.info_livestream.ActionMessageDialog
import olmo.wellness.android.ui.livestream.info_livestream.MoreOptionLiveStreamDialog
import olmo.wellness.android.ui.livestream.report.ReportDialog
import olmo.wellness.android.ui.livestream.utils.Gift
import olmo.wellness.android.ui.livestream.utils.Heart
import olmo.wellness.android.ui.screen.playback_video.MoreOptionWrapper
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.list_user.ListUserChatBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.voucher_service.VoucherServiceBottomSheet
import olmo.wellness.android.ui.screen.playback_video.common.PagerItem
import olmo.wellness.android.ui.screen.playback_video.contact_information.ContactInformationServiceBottomSheet
import olmo.wellness.android.ui.screen.playback_video.donate.data.TipsType
import olmo.wellness.android.ui.screen.playback_video.donate.package_options.PackageOptionsBottomSheet
import olmo.wellness.android.ui.screen.playback_video.donate.payment_methods.PaymentMethodsBottomSheet
import olmo.wellness.android.ui.screen.playback_video.donate.payment_methods.PurchaseSuccessBottomSheet
import olmo.wellness.android.ui.screen.playback_video.donate.ui_main.TipsAndDonateBottomSheet
import olmo.wellness.android.ui.screen.playback_video.explore.OlboardHomeInteractionEvents
import olmo.wellness.android.ui.screen.playback_video.explore.dismissDialogMoreOption
import olmo.wellness.android.ui.screen.playback_video.items.EmptyOnLiveBody
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.screen.video_small.VideoActivity
import olmo.wellness.android.ui.theme.*

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterialApi::class)
@SuppressLint(
    "CoroutineCreationDuringComposition", "MutableCollectionMutableState",
    "StateFlowValueCalledInComposition"
)
@Composable
fun PlayBackOnLiveStreamScreen(
    navController: NavController,
    playBackViewModel: PlayBackOnLiveViewModel = hiltViewModel(),
    chatLiveStreamViewModel: ChatLivestreamViewModel = hiltViewModel(),
    chatPrivateViewModel: PrivateChatViewModel = hiltViewModel(),
    bookingPlayBackViewModel: BookingPlayBackViewModel = hiltViewModel(),
    defaultLivestreamInfo: LiveSteamShortInfo? = null,
) {
    val mainScope = rememberCoroutineScope()
    val context = LocalContext.current as MainActivity
    val bottomSheetCommentScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp

    val uiStatePlayBack = playBackViewModel.uiState.collectAsState().value
    val uiStateChatLiveStream = chatLiveStreamViewModel.uiState.collectAsState().value
    val uiStateChatPrivate = chatPrivateViewModel.uiState.collectAsState().value
    val uiStateBooking = bookingPlayBackViewModel.uiState.collectAsState().value

    val isLoading = uiStatePlayBack.showLoading

    val isComment = remember {
        mutableStateOf(false)
    }
    isComment.value = uiStateChatLiveStream.isComment

    val isLikeRoom = uiStateChatLiveStream.roomStream?.getIsLikeRoom()?.collectAsState()

    val countComment = uiStateChatLiveStream.roomStream?.getCountComment()?.collectAsState()

    val countHeart = uiStateChatLiveStream.roomStream?.getHeartReaction()?.collectAsState()

    val heartCountAnimation = remember {
        mutableStateOf(0)
    }

    val countDonation = uiStateChatLiveStream.roomStream?.getCountDonation()?.collectAsState()

    val donation = uiStateChatLiveStream.roomStream?.getDonation()?.collectAsState()

    if (countHeart?.value?.countType == CountType.NORMAL) {
        heartCountAnimation.value = countHeart.value.countReaction
    }

    val openDialogFinished = remember {
        mutableStateOf(false)
    }

    val packageOptionSelected = uiStatePlayBack.packageOptionSelected
    val wrapperUrlPayment = uiStatePlayBack.wrapperUrlPayment

    val focusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val isInputChat = remember {
        mutableStateOf(false)
    }

    val isShowAddComment = remember {
        mutableStateOf(true)
    }

    val isShowReportDialog = remember {
        mutableStateOf(Pair<Boolean, LiveSteamShortInfo?>(false, null))
    }

    val msgSelect = remember {
        mutableStateOf<ChatMessage?>(null)
    }

    val openDialogActionMessage = remember {
        mutableStateOf(false)
    }

    val openDialogActionUnFollow = remember {
        mutableStateOf(false)
    }

    val actionFollowing = remember {
        mutableStateOf<Pair<Int, Boolean>>(Pair(0, false))
    }

    val bookingServiceFromSelection = uiStatePlayBack.bookingServiceFromSelection

    val paymentRequireWrapper = remember {
        mutableStateOf<PaymentRequireWrapper?>(null)
    }

    val modalListUserBottomSheetSate =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalTipsAndDonateBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalPaymentMethodsBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalPackageOptionBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalConversationDetailBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalContactInformationBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalVoucherBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalPaymentSuccessBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalBookNowSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalCartSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                false
            }
        )

    val modalBookingDetailSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val bookingDetail = remember {
        mutableStateOf<ServiceBooking?>(null)
    }

    val modalBookNowOneByOneSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    var bookingWrapperInfo by remember {
        mutableStateOf<BookingWrapperInfo?>(null)
    }

    val resultActivity = remember {
        mutableStateOf<String?>(null)
    }

    val textMessageValueState = remember {
        mutableStateOf(
            TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        )
    }

    val pauseAll = remember {
        mutableStateOf(false)
    }

    val requestPauseAllVideo: (() -> Unit) = {
        pauseAll.value = true
    }

    val requestPlayVideo: (() -> Unit) = {
        pauseAll.value = false
    }

    var requestMoreOption by remember {
        mutableStateOf<MoreOptionWrapper?>(null)
    }

    val openDescriptionVideo = remember {
        mutableStateOf(false)
    }

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
            }
            Lifecycle.Event.ON_DESTROY -> {
                chatLiveStreamViewModel.clearRoomLS()
                chatLiveStreamViewModel.clearState()
            }
            else -> {

            }
        }
    }

    ActionMessageDialog(msgSelect.value, showDialog = openDialogActionMessage.value, onCancel = {
        dismissActionMessage(msgSelect, openDialogActionMessage, true)
    }, onLikeComment = { user, idMessage ->
        chatLiveStreamViewModel.sendReactionMessageRoomLS(idMessage)
        dismissActionMessage(msgSelect, openDialogActionMessage, true)
    }, onReply = {
        val newText = "@" + getNameUserChat(it) + " "
        textMessageValueState.value = textMessageValueState.value.copy(
            text = newText,
            selection = TextRange(newText.length)
        )
        dismissActionMessage(msgSelect, openDialogActionMessage, false)
        isInputChat.value = true
    })

    val liveInfo = remember {
        mutableStateOf<LiveSteamShortInfo?>(null)
    }

    val liveInfoUnFollow = remember {
        mutableStateOf<LiveSteamShortInfo?>(null)
    }

    ActionFollowDialog(
        user = liveInfo.value?.user,
        showDialog = openDialogActionUnFollow.value,
        onCancel = {
            dismissActionMessage(msgSelect, openDialogActionUnFollow, true)
            openDialogActionUnFollow.value = false
        },
        onConfirm = { user ->
            user?.id?.let { playBackViewModel.deleteUserFollowing(it) }
            val liveInfoFinal = liveInfoUnFollow.value
            liveInfoUnFollow.value = liveInfoFinal?.copy(transformFollow = false, isFollow = false)
            actionFollowing.value = Pair(liveInfoFinal?.id ?: 0, false)
            openDialogActionUnFollow.value = false
        })

    LaunchedEffect(key1 = "onLifeDefaultValue") {
        defaultLivestreamInfo?.let { playBackViewModel.pushTopDefaultLiveInfo(it) }
    }

    if (uiStatePlayBack.livestreamList?.isNotEmpty() == true) {
        val livestreamInfo = uiStatePlayBack.livestreamList.distinctBy { it.id }
        val pagerState: PagerState = run {
            remember {
                PagerState(0, 0, livestreamInfo.size - 1)
            }
        }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collectLatest { page ->
                liveInfo.value = livestreamInfo[page]
                chatLiveStreamViewModel.sendJoinRoomLS(
                    _roomId = liveInfo.value?.roomChatId,
                    _hostRoomId = liveInfo.value?.userId,
                    _isShowChild = false,
                    _heartCountServer = liveInfo.value?.heartCount
                )
                chatPrivateViewModel.getRoomChatSingle(liveInfo.value?.userId)
                bookingPlayBackViewModel.getServiceLivestream(livestreamId = liveInfo.value?.id,
                    roomId = liveInfo.value?.roomChatId)
                playBackViewModel.setCurrentIndex(page)
                playBackViewModel.trackingViewLiveStream(
                    AnalyticsKey.ActionSwipeFirebase,
                    livestreamInfo[page]
                )
            }
        }

        BottomSheetScaffold(
            scaffoldState = bottomSheetCommentScaffoldState,
            sheetContent = {
                Box(Modifier.defaultMinSize(minHeight = 1.dp)) {}
            },
            sheetPeekHeight = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                Pager(
                    state = pagerState,
                    orientation = Orientation.Vertical,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val liveInfoInternal = remember(livestreamInfo[commingPage]) {
                        livestreamInfo[commingPage]
                    }
                    val isSelected = pagerState.currentPage == commingPage
                    val playOnStarting = !pauseAll.value && !VideoActivity.isActive
                    if (liveInfoInternal.id != null) {
                        liveInfoInternal.let { info ->
                            if (info.id == liveInfoUnFollow.value?.id) {
                                liveInfoInternal.isFollow = liveInfoUnFollow.value?.isFollow
                            }
                            PagerItem(
                                isLiveStream = true,
                                startPlay = playOnStarting,
                                livestreamInfo = info,
                                requestOpenPip = { _, _ ->
                                    requestPauseAllVideo()
                                    context.maybeShowPipActivity(VideoActivity(context, info))
                                },
                                selected = isSelected,
                                showDescriptionVideo = openDescriptionVideo.value,
                                interactionEvents = { event, livestreamInfo ->
                                    when (event) {
                                        OlboardHomeInteractionEvents.OpenComments -> {
                                            mainScope.launch {
                                                bottomSheetCommentScaffoldState.bottomSheetState.expand()
                                            }
                                        }
                                        OlboardHomeInteractionEvents.ShowListUser -> {
                                            scope.launch {
                                                modalListUserBottomSheetSate.show()
                                            }
                                        }

                                        OlboardHomeInteractionEvents.FollowAction -> {
                                            playBackViewModel.postUserFollow(livestreamInfo?.userId)
                                            liveInfoInternal.isFollow = true
                                            liveInfoInternal.transformFollow = true
                                            actionFollowing.value = Pair(info?.id ?: 0, true)
                                            playBackViewModel.trackingViewLiveStream(
                                                AnalyticsKey.ActionClickFollowFirebase,
                                                info
                                            )
                                        }
                                        OlboardHomeInteractionEvents.UnFollowAction -> {
                                            openDialogActionUnFollow.value = true
                                            liveInfoUnFollow.value = info
                                        }
                                        OlboardHomeInteractionEvents.NavigationToFollowProfile -> {
                                            navController.navigate(
                                                ScreenName.FollowingProfileScreen.route
                                                    .plus("?defaultData=${info.toJson()}")
                                            )
                                        }
                                        OlboardHomeInteractionEvents.CloseLiveStream -> {
                                            chatLiveStreamViewModel.clearRoomLS()
                                            chatLiveStreamViewModel.clearRoomLS()
                                            chatLiveStreamViewModel.clearState()
                                            chatPrivateViewModel.clearState()
                                            playBackViewModel.clearState()
                                            navController.popBackStack()
                                            playBackViewModel.trackingViewLiveStream(
                                                AnalyticsKey.ActionClickCloseViewFirebase,
                                                info
                                            )
                                        }
                                        is OlboardHomeInteractionEvents.ReactionHeart -> {
                                            chatLiveStreamViewModel.sendReactionRoom()
                                            if (isLikeRoom?.value == false) {
                                                uiStateChatLiveStream.roomStream.setLikeRoom(true)
                                            }
                                            playBackViewModel.trackingViewLiveStream(
                                                AnalyticsKey.ActionClickLikeFirebase,
                                                info
                                            )
                                        }
                                        is OlboardHomeInteractionEvents.ShareVideo -> {
                                            context.let {
                                                livestreamInfo?.recordUrl?.let { it1 ->
                                                    shareLinkVideo(
                                                        context,
                                                        it1
                                                    )
                                                }
                                            }
                                            playBackViewModel.trackingViewLiveStream(
                                                AnalyticsKey.ActionShareSocialFirebase,
                                                info
                                            )
                                        }
                                        is OlboardHomeInteractionEvents.OpenPipMode -> {
                                            requestPauseAllVideo()
                                            context.maybeShowPipActivity(
                                                VideoActivity(
                                                    context,
                                                    info
                                                )
                                            )
                                            chatLiveStreamViewModel.clearRoomLS()
                                            playBackViewModel.trackingViewLiveStream(
                                                AnalyticsKey.ActionClickMiniSizeFirebase,
                                                info
                                            )
                                            playBackViewModel.clearState()
                                            navController.popBackStack()
                                        }

                                        is OlboardHomeInteractionEvents.MoreOption -> {
                                            val moreOptionWrapper = MoreOptionWrapper(true, info)
                                            requestMoreOption = moreOptionWrapper
                                            /*// TODO Want to test booking ,
                                            scope.launch {
                                                modalBookNowSheetState.show()
                                            }*/
                                        }
                                        is OlboardHomeInteractionEvents.GiftDonate -> {
                                            scope.launch {
                                                modalTipsAndDonateBottomSheetState.show()
                                            }
                                        }
                                        is OlboardHomeInteractionEvents.ShowInfoStream -> {
                                            openDescriptionVideo.value = !openDescriptionVideo.value
                                        }
                                        is OlboardHomeInteractionEvents.BookNowService -> {
                                            scope.launch {
                                                modalBookNowSheetState.show()
                                            }
                                        }
                                        is OlboardHomeInteractionEvents.OnBookingService -> {
                                            bookingDetail.value =
                                                uiStateBooking.serviceBookingShow?.toServiceBooking()
                                            scope.launch {
                                                modalBookingDetailSheetState.show()
                                            }
                                        }
                                        is OlboardHomeInteractionEvents.CloseShowBookService -> {
                                            bookingPlayBackViewModel.closeShowBookingService()
                                        }
                                        else -> {
                                        }
                                    }
                                },
                                playbacks = info,
                                isLike = isLikeRoom?.value,
                                requestPlayVideo = requestPlayVideo,
                                requestPauseAllVideo = requestPauseAllVideo,
                                roomChat = uiStateChatLiveStream.roomStream,
                                isComment = isComment,
                                countHeart = countHeart?.value?.countReaction,
                                countComment = countComment?.value,
                                isInputChat = isInputChat,
                                isShowAddComment = isShowAddComment,
                                msgSelect = msgSelect,
                                openDialogActionMessage = openDialogActionMessage,
                                requestLoadMoreChat = {
                                    chatLiveStreamViewModel.loadMoreLS()
                                },
                                onClosePlayer = { status ->
                                    if (status) {
                                        playBackViewModel.clearState()
                                        navController.popBackStack()
                                    }
                                },
                                onFinishedVideo = { status ->
                                    if (status) {
                                        if (livestreamInfo.distinctBy { it.id }.size == 1) {
                                            openDialogFinished.value = true
                                        } else {
                                            pagerState.currentPage++
                                            openDialogFinished.value = false
                                        }
                                        playBackViewModel.trackingViewLiveStream(
                                            AnalyticsKey.ActionLiveStreamEndFirebase,
                                            liveInfoInternal
                                        )
                                    }
                                },
                                showFollow = liveInfoInternal.isFollow ?: false,
                                transformFollowAction = liveInfoUnFollow.value?.transformFollow,
                                actionFollow = actionFollowing,
                                serviceBookingShow = uiStateBooking.serviceBookingShow?.toServiceBooking(),
                                numberServiceBookings = uiStateBooking.serviceBookings?.size ?: 0
                            )
                        }
                        playBackViewModel.trackingViewLiveStream(
                            AnalyticsKey.ActionLiveStartFirebase,
                            liveInfoInternal
                        )
                    }
                }
                if (isInputChat.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                    ) {
                        InputChatLiveStream(
                            isShowAddComment,
                            focusRequester,
                            focusManager,
                            isInputChat,
                            scope,
                            textMessageValueState,
                            msgSelect.value?.id,
                            sendMessage = { text, replyID ->
                                chatLiveStreamViewModel.sendMessageLS(text, replyID)
                                playBackViewModel.trackingViewLiveStream(
                                    AnalyticsKey.ActionSendCommentFirebase,
                                    liveInfo.value
                                )
                            }
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    countHeart?.value?.let {
                        if (countHeart.value.countType == CountType.UP) {
                            repeat(it.countReaction - heartCountAnimation.value) { number ->
                                Heart(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.BottomCenter),
                                    horizontalPadding = 0,
                                    bottomMargin = 50,
                                    color = getHeartColor(number),
                                    number = number,
                                    offsetX = width - 45,
                                    offsetY = height - (if ( uiStateBooking.serviceBookings?.size!=null && uiStateBooking.serviceBookings?.size!!>0) 276 else 216)
                                )
                            }
                        }
                    }
                }
                // Temp Remove In Phase
                Box(modifier = Modifier.fillMaxSize()) {
                    countDonation?.value?.let {
                        repeat(it) {
                            Gift(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.BottomCenter),
                                horizontalPadding = 0,
                                bottomMargin = 50,
                                offsetX = width - 75,
                                offsetY = height - (if ( uiStateBooking.serviceBookings?.size!=null && uiStateBooking.serviceBookings?.size!!>0) 286 else 226),
                                type = getTypeDonation(donation?.value)
                            )
                        }
                    }
                }
            }

            MoreOptionLiveStreamDialog(showDialog = requestMoreOption?.isSelected == true,
                msg = requestMoreOption?.user,
                livestreamInfo = requestMoreOption?.livestreamInfo,
                onCancel = {
                    val moreOptionWrapper = MoreOptionWrapper(false, null, null)
                    requestMoreOption = moreOptionWrapper
                },
                onSendMessageToHost = {
                    val moreOptionWrapper = MoreOptionWrapper(false, null, null)
                    requestMoreOption = moreOptionWrapper
                    scope.launch {
                        modalConversationDetailBottomSheetState.show()
                    }
                    playBackViewModel.trackingViewLiveStream(
                        AnalyticsKey.ActionSendMessageFirebase,
                        it
                    )
                },
                onShareOnSocialMedia = { info ->
                    context.let {
                        requestMoreOption?.livestreamInfo?.recordUrl?.let { it1 ->
                            shareLinkVideo(
                                context,
                                it1
                            )
                        }
                    }
                    playBackViewModel.trackingViewLiveStream(
                        AnalyticsKey.ActionShareSocialFirebase,
                        info
                    )
                },
                onShareOnProfile = {
                    it?.id?.let { it1 ->
                        playBackViewModel.shareOnProfile(it1)
                        requestMoreOption = dismissDialogMoreOption()
                        playBackViewModel.trackingViewLiveStream(
                            AnalyticsKey.ActionShareProfileFirebase,
                            it
                        )
                    }
                },
                onReportAdmin = {
                    isShowReportDialog.value = true to it
                    val moreOptionWrapper = MoreOptionWrapper(false, null, null)
                    requestMoreOption = moreOptionWrapper
                    playBackViewModel.trackingViewLiveStream(
                        AnalyticsKey.ActionSendReportFirebase,
                        it
                    )
                }
            )
            val closePopup = remember {
                mutableStateOf(false)
            }
            if (isShowReportDialog.value.first) {
                context.showAsBottomSheet(closePopup) {
                    ReportDialog(liveStreamId = isShowReportDialog.value.second?.id, onConfirm = {
                        closePopup.value = true
                    })
                    isShowReportDialog.value = Pair(false, null)
                }
            }
            val unFollowSuccess = playBackViewModel.unFollowSuccess.collectAsState()
            LaunchedEffect(unFollowSuccess.value) {
                snapshotFlow { unFollowSuccess.value }.collectLatest { status ->
                    if (status) {
                        val comingPage = pagerState.currentPage
                        livestreamInfo[comingPage].isFollow = false
                        playBackViewModel.resetStatus()
                    }
                }
            }
        }
    } else {
        EmptyOnLiveBody()
    }
    LoaderWithAnimation(isLoading)
    if (openDialogFinished.value) {
        DialogFinishedLiveStream(openDialogCustom = openDialogFinished, confirmCallback = {
            playBackViewModel.clearState()
            navController.popBackStack()
        })
    }

    if (!isInputChat.value && !openDialogActionMessage.value) {
        msgSelect.value = null
    }

    TipsAndDonateBottomSheet(
        modalBottomSheetState = modalTipsAndDonateBottomSheetState,
        onRechargeNow = { status ->
            if (status) {
                scope.launch {
                    modalTipsAndDonateBottomSheetState.hide()
                    modalPaymentMethodsBottomSheetState.show()
                }
            }
        },
        onNavigationPackageOption = { status ->
            if (status) {
                scope.launch {
                    modalTipsAndDonateBottomSheetState.hide()
                    modalPackageOptionBottomSheetState.show()
                }
            }
        },
        onSendTipSuccess = { status, giftID ->
            if (status) {
                scope.launch {
                    modalTipsAndDonateBottomSheetState.hide()
                }
                chatLiveStreamViewModel.sendTipPackage(giftID)
            }
        }
    )
    PackageOptionsBottomSheet(
        modalBottomSheetState = modalPackageOptionBottomSheetState,
        onRechargeWithItem = { packageInfoCallBack ->
            if (packageInfoCallBack != null) {
                playBackViewModel.updatePackageSelected(packageInfoCallBack)
            }
            scope.launch {
                modalPackageOptionBottomSheetState.hide()
                modalPaymentMethodsBottomSheetState.show()
            }
        },
        onInfo = {
            navController.navigate(ScreenName.InfoPackageScreen.route)
        })

    val navigationWebView = remember {
        mutableStateOf<WrapperUrlPayment?>(null)
    }

    PaymentMethodsBottomSheet(modalBottomSheetState = modalPaymentMethodsBottomSheetState,
        packageOptionInfo = packageOptionSelected, navigationWebView = { status, linkPayment ->
            if (status) {
                navigationWebView.value = linkPayment
                playBackViewModel.updateWrapperPayment(linkPayment)
            }
        }, paymentRequireWrapper = paymentRequireWrapper.value)

    LaunchedEffect(navigationWebView.value) {
        snapshotFlow { navigationWebView.value }.take(1).collectLatest { wrapperPayment ->
            if (wrapperPayment?.url?.isNotEmpty() == true) {
                navController.navigate(
                    ScreenName.PaymentProcessScreen.route.plus(
                        "?defaultData=${wrapperPayment.toJson()}"
                    )
                )
                navigationWebView.value = null
            }
            scope.launch {
                modalPaymentMethodsBottomSheetState.hide()
            }
        }
    }

    ConversationDetailBottomSheet(
        roomChatState = uiStateChatPrivate.roomStream,
        modalBottomSheetState = modalConversationDetailBottomSheetState,
        onSendMessage = {
            chatPrivateViewModel.sendTextMessagePC(it, null)
        },
        onLoadMore = {
            chatLiveStreamViewModel.loadMoreLS()
        }, onImageSent = {
            chatPrivateViewModel.sendImageMessagePC(it, null)
        }
    )

    ListUserChatBottomSheet(listData = uiStateChatLiveStream.roomStream?.userMap,
        modalBottomSheetState = modalListUserBottomSheetSate, onUserSelected = { user ->
            chatPrivateViewModel.getRoomChatSingle(user.id)
            scope.launch {
                modalListUserBottomSheetSate.hide()
                modalConversationDetailBottomSheetState.show()
            }
        })

    BookNowBuyerBottomSheet(modalBottomSheetState = modalBookNowSheetState,
        serviceBookings = uiStateBooking.serviceBookings?.map {
            it.toServiceBooking()
        }, bookNowCallback = { serviceBooking ->
            playBackViewModel.updateServiceBookingFromList(serviceBooking)
            scope.launch {
                modalBookNowOneByOneSheetState.show()
            }
        }, openCart = {
            scope.launch {
                modalCartSheetState.show()
                modalBookNowOneByOneSheetState.hide()
            }
        }, onDetailService = {
            bookingDetail.value = it
            scope.launch {
                modalBookingDetailSheetState.show()
                modalBookNowSheetState.hide()
            }
        }, onAddCart = {
            bookingPlayBackViewModel.addToCart(it)
        })

    ServiceBookingDetailBottomSheet(booking = bookingDetail.value,
        modalBottomSheetState = modalBookingDetailSheetState,
        onBookNow = {
            playBackViewModel.updateServiceBookingFromList(it)
            scope.launch {
                modalBookNowOneByOneSheetState.show()
                modalBookingDetailSheetState.hide()
            }
        },
        onAddToCart = {
            bookingPlayBackViewModel.addToCart(it)
        })

    CartBookingBottomSheet(modalBottomSheetState = modalCartSheetState,
        bookingPlayBackViewModel = bookingPlayBackViewModel,
        confirmCallback = { bookingWrapper ->
            scope.launch {
                bookingWrapperInfo = bookingWrapper
                modalCartSheetState.hide()
                modalBookNowSheetState.hide()
                modalContactInformationBottomSheetState.show()
            }
        },
        cancelCallback = {
            scope.launch {
                modalCartSheetState.hide()
                modalBookNowOneByOneSheetState.show()
            }
        })

    /* Screen for book one detail Service */
    if (bookingServiceFromSelection != null) {
        BookNowBottomSheet(booking = bookingServiceFromSelection.copy(tempStamp = System.currentTimeMillis()),
            modalBottomSheetState = modalBookNowOneByOneSheetState,
            onOpenContactInformation = { bookingInfo ->
                scope.launch {
                    bookingWrapperInfo = bookingInfo
                    modalContactInformationBottomSheetState.show()
                }
            })
    }

    ContactInformationServiceBottomSheet(
        modalBottomSheetState = modalContactInformationBottomSheetState, cancelCallback = {
            if (it) {
                scope.launch {
                    modalContactInformationBottomSheetState.hide()
                    modalBookNowOneByOneSheetState.show()
                }
            }
        }, confirmCallback = { paymentInfoWrapper ->
            scope.launch {
                paymentRequireWrapper.value = paymentInfoWrapper
                playBackViewModel.clearPackageSelected()
                modalPaymentMethodsBottomSheetState.show()
                modalContactInformationBottomSheetState.hide()
                modalContactInformationBottomSheetState.hide()
                modalPaymentSuccessBottomSheetState.hide()
                modalBookNowOneByOneSheetState.hide()
            }
        }, bookingWrapperInfo = bookingWrapperInfo)

    VoucherServiceBottomSheet(modalBottomSheetState = modalVoucherBottomSheetState)

    navController.currentBackStackEntry
        ?.savedStateHandle?.getLiveData<String>(Constants.BUNDLE_DATA_PAYMENT_SUCCESS)
        ?.observe(LocalLifecycleOwner.current) { result ->
            if (result != null && result.isNotEmpty()) {
                resultActivity.value = result
            }
        }

    LaunchedEffect(resultActivity) {
        snapshotFlow { resultActivity }.take(1).collectLatest { result ->
            if (result.value?.isNotEmpty() == true) {
                if (result.value == Constants.BUNDLE_DATA_PAYMENT_BOOKING_SUCCESS) {
                    scope.launch {
                        modalContactInformationBottomSheetState.hide()
                        modalPaymentSuccessBottomSheetState.show()
                        paymentRequireWrapper.value = null
                        resultActivity.value = null
                    }
                }

                if (result.value == Constants.BUNDLE_DATA_PAYMENT_DONATE_SUCCESS) {
                    scope.launch {
                        modalContactInformationBottomSheetState.hide()
                        playBackViewModel.clearPackageSelected()
                        resultActivity.value = null
                    }
                }
            }
        }
    }

    PurchaseSuccessBottomSheet(modalBottomSheetState = modalPaymentSuccessBottomSheetState,
        wrapperUrlPayment,
        onNavigationDetail = {
            scope.launch {
                modalPaymentSuccessBottomSheetState.hide()
            }
            navController.popBackStack()
            navController.navigate(ScreenName.MyDashBoardBookingScreen.route)
        },
        onCancelCallback = {
            scope.launch {
                modalPaymentSuccessBottomSheetState.hide()
            }
        })
}

fun dismissDialogMoreOption(): MoreOptionWrapper {
    return MoreOptionWrapper(false, null, null)
}

fun getHeartColor(number: Int): Color {
    return when (number % 3) {
        0 -> Color_Background_Heart_1
        1 -> Color_Background_Heart_2
        else -> Color_Background_Heart_3
    }
}

fun getTypeDonation(donation: ChatObject?) : TipsType {
  return  when (donation?.id) {
        TipsType.ThumbUp.id -> {
            TipsType.ThumbUp
        }
        TipsType.SendingLove.id -> {
            TipsType.SendingLove
        }
        TipsType.LuckyCrystal.id -> {
            TipsType.LuckyCrystal
        }
        TipsType.PurpleCrystal.id -> {
            TipsType.PurpleCrystal
        }
        TipsType.HealthCrystal.id -> {
            TipsType.HealthCrystal
        }
        TipsType.HealingCrystal.id -> {
            TipsType.HealingCrystal
        }
        TipsType.GoldenBox.id -> {
            TipsType.GoldenBox
        }
        TipsType.DiamondBox.id -> {
            TipsType.DiamondBox
        }else->{
          TipsType.ThumbUp
        }
  }
}


