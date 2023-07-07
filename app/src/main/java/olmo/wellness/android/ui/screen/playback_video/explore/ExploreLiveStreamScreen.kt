package olmo.wellness.android.ui.screen.playback_video.explore

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.maybeShowPipActivity
import olmo.wellness.android.core.toJson
import olmo.wellness.android.core.utils.shareLinkVideo
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.CountType
import olmo.wellness.android.domain.model.booking.BookingWrapperInfo
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.WrapperUrlPayment
import olmo.wellness.android.domain.model.booking.toServiceBooking
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.payment.PaymentRequireWrapper
import olmo.wellness.android.ui.analytics.AnalyticsKey
import olmo.wellness.android.ui.booking.book_now.view.BookNowBottomSheet
import olmo.wellness.android.ui.booking.book_now_livestream.view.BookNowBuyerBottomSheet
import olmo.wellness.android.ui.booking.book_now_livestream.view.CartBookingBottomSheet
import olmo.wellness.android.ui.booking.detail.view.ServiceBookingDetailBottomSheet
import olmo.wellness.android.ui.booking.viewmodel.BookingPlayBackViewModel
import olmo.wellness.android.ui.chat.conversation_detail.ConversationDetailBottomSheet
import olmo.wellness.android.ui.chat.private_chat.viewmodel.PrivateChatViewModel
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.common.live_stream.carousel.Pager
import olmo.wellness.android.ui.common.live_stream.carousel.PagerState
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.livestream.chatlivestream.view.InputChatLiveStream
import olmo.wellness.android.ui.livestream.chatlivestream.view.convertTextReply
import olmo.wellness.android.ui.livestream.chatlivestream.viewmodel.ChatLivestreamViewModel
import olmo.wellness.android.ui.livestream.info_livestream.ActionFollowDialog
import olmo.wellness.android.ui.livestream.info_livestream.MoreOptionLiveStreamDialog
import olmo.wellness.android.ui.livestream.report.ReportDialog
import olmo.wellness.android.ui.livestream.utils.Heart
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.playback_video.MoreOptionWrapper
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.list_user.ListUserChatBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.voucher_service.VoucherServiceBottomSheet
import olmo.wellness.android.ui.screen.playback_video.common.PagerItem
import olmo.wellness.android.ui.screen.playback_video.contact_information.ContactInformationServiceBottomSheet
import olmo.wellness.android.ui.screen.playback_video.donate.payment_methods.PaymentMethodsBottomSheet
import olmo.wellness.android.ui.screen.playback_video.donate.payment_methods.PurchaseSuccessBottomSheet
import olmo.wellness.android.ui.screen.playback_video.explore.comment.CommentVideoBottomSheet
import olmo.wellness.android.ui.screen.playback_video.items.EmptyOnLiveBody
import olmo.wellness.android.ui.screen.playback_video.onlive.getHeartColor
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.screen.video_small.VideoActivity

@SuppressLint(
    "CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter",
    "StateFlowValueCalledInComposition", "SuspiciousIndentation", "NewApi"
)
@OptIn(ExperimentalMaterialApi::class, FlowPreview::class)
@ExperimentalFoundationApi
@Composable
fun ExploreLiveStreamScreen(
    navController: NavController,
    playBackViewModel: ExplorePlayBackViewModel = hiltViewModel(),
    chatLiveStreamViewModel: ChatLivestreamViewModel = hiltViewModel(),
    chatPrivateViewModel: PrivateChatViewModel = hiltViewModel(),
    defaultLivestreamInfo: LiveSteamShortInfo? = null,
    bookingPlayBackViewModel: BookingPlayBackViewModel = hiltViewModel(),
    olMoInteractionEvents: ((OlboardHomeInteractionEvents, LiveSteamShortInfo?) -> Unit)? = null,
){

    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp

    val uiStatePlayBack = playBackViewModel.uiState.collectAsState().value
    val uiStateChatLiveStream = chatLiveStreamViewModel.uiState.collectAsState()
    val uiStateChatPrivate = chatPrivateViewModel.uiState.collectAsState()
    val uiStateBooking = bookingPlayBackViewModel.uiState.collectAsState().value

    val isInputChat = remember {
        mutableStateOf(false)
    }

    val isShowAddComment = remember { mutableStateOf(true) }

    val liveInfo = remember { mutableStateOf<LiveSteamShortInfo?>(null) }

    val countHeart = uiStateChatLiveStream.value.roomStream?.getHeartReaction()?.collectAsState()

    val countComment = uiStateChatLiveStream.value.roomStream?.getCountComment()?.collectAsState()

    val isLikeRoom = uiStateChatLiveStream.value.roomStream?.getIsLikeRoom()?.collectAsState()

    val heartCountAnimation = remember {
        mutableStateOf(0)
    }

    if (countHeart?.value?.countType == CountType.NORMAL) {
        heartCountAnimation.value = countHeart.value.countReaction
    }

    val focusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    val textMessageValueState = remember {
        mutableStateOf(
            TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        )
    }

    val messageReply = remember {
        mutableStateOf<ChatMessage?>(null)
    }

    val modalConversationDetailBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    if (messageReply.value != null) {
        textMessageValueState.value = convertTextReply(messageReply.value?.user)
    }

    val isLoading = uiStatePlayBack.showLoading
    val pauseAll = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val modalBottomSheetStateCommentVideo =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )

    val modalListUserBottomSheetSate =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val requestPauseAllVideo: (() -> Unit) = {
        pauseAll.value = true
    }

    var requestMoreOption by remember {
        mutableStateOf<MoreOptionWrapper?>(null)
    }

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
                true
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

    var bookingWrapperInfo by remember{
        mutableStateOf<BookingWrapperInfo?>(null)
    }

    val bookingServiceFromSelection = uiStatePlayBack.bookingServiceFromSelection

    val paymentRequireWrapper = remember{
        mutableStateOf<PaymentRequireWrapper?>(null)
    }

    val modalPaymentMethodsBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    LaunchedEffect(key1 = "setupDefault") {
        defaultLivestreamInfo?.apply {
            playBackViewModel.pushTopDefaultLiveInfo(this)
        }
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
//                chatLiveStreamViewModel.clearRoomLS()
            }
            Lifecycle.Event.ON_DESTROY -> {
                chatLiveStreamViewModel.clearRoomLS()
                chatLiveStreamViewModel.clearState()
            }
            else -> {
            }
        }
    }

    val requestPlayVideo: (() -> Unit) = {
        pauseAll.value = false
    }

    val isShowReportDialog = remember {
        mutableStateOf(Pair<Boolean, LiveSteamShortInfo?>(false, null))
    }

    val openDialogActionUnFollow = remember {
        mutableStateOf(false)
    }

    val liveInfoUnFollow = remember {
        mutableStateOf<LiveSteamShortInfo?>(null)
    }

    val openDescriptionVideo = remember {
        mutableStateOf(false)
    }

    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            })
    val scope = rememberCoroutineScope()
    val actionFollowing = remember {
        mutableStateOf<Pair<Int, Boolean>>(Pair(0, false))
    }
    val resultActivity = remember{
        mutableStateOf<String?>(null)
    }
    if (uiStatePlayBack.livestreamList?.isNotEmpty() == true) {
        val livestreamInfo = uiStatePlayBack.livestreamList
        val pagerState: PagerState = run {
            remember {
                PagerState(0, 0, livestreamInfo.size - 1)
            }
        }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                liveInfo.value = livestreamInfo[page]
                chatLiveStreamViewModel.sendJoinRoomLS(
                    _roomId = liveInfo.value?.roomChatId,
                    _hostRoomId = liveInfo.value?.userId,
                    _isShowChild = true,
                    _heartCountServer = liveInfo.value?.heartCount
                )
                chatPrivateViewModel.getRoomChatSingle(liveInfo.value?.userId)
                bookingPlayBackViewModel.getServiceLivestream(livestreamId = liveInfo.value?.id,
                    roomId = liveInfo.value?.roomChatId)
                playBackViewModel.trackingViewLiveStream(
                    AnalyticsKey.ActionSwipeFirebase,
                    livestreamInfo[page]
                )
            }
        }
        ModalBottomSheetLayout(
            sheetState = modalBottomSheetState,
            sheetContent = {
                Column(modifier = Modifier.height(1.dp)) {}
            }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)){
                Pager(
                    state = pagerState,
                    orientation = Orientation.Vertical,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val liveInfoInternal = livestreamInfo[commingPage]
                    val isSelected = pagerState.currentPage == commingPage
                    val playOnStarting = !pauseAll.value && !VideoActivity.isActive
                    liveInfoInternal.let {
                        if (it.id == liveInfoUnFollow.value?.id) {
                            liveInfoInternal.isFollow = liveInfoUnFollow.value?.isFollow
                        }
                        it.let { infoVideo ->
                            PagerItem(
                                isLiveStream = false,
                                startPlay = playOnStarting,
                                livestreamInfo = infoVideo,
                                selected = isSelected,
                                showDescriptionVideo = openDescriptionVideo.value,
                                interactionEvents = { event, info ->
                                    when (event) {
                                        OlboardHomeInteractionEvents.FollowAction -> {
                                            playBackViewModel.postUserFollow(info?.userId)
                                            livestreamInfo[commingPage].isFollow = true
                                            playBackViewModel.trackingViewLiveStream(
                                                AnalyticsKey.ActionClickFollowFirebase,
                                                info
                                            )
                                            liveInfoInternal.isFollow = true
                                            actionFollowing.value = Pair(info?.id ?: 0 , true)
                                            playBackViewModel.trackingViewLiveStream(AnalyticsKey.ActionClickFollowFirebase, info)
                                        }

                                        OlboardHomeInteractionEvents.UnFollowAction -> {
                                            openDialogActionUnFollow.value = true
                                            liveInfoUnFollow.value = info
                                        }

                                        OlboardHomeInteractionEvents.NavigationToFollowProfile -> {
                                            navController.navigate(
                                                ScreenName.FollowingProfileScreen.route
                                                    .plus("?defaultData=${info?.toJson()}")
                                            )
                                        }
                                        is OlboardHomeInteractionEvents.ShareVideo -> {
                                            context.let {
                                                info?.recordUrl?.let { it1 ->
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
                                        is OlboardHomeInteractionEvents.OpenComments -> {
                                            scope.launch {
                                                modalBottomSheetStateCommentVideo.show()
                                            }
                                        }
                                        is OlboardHomeInteractionEvents.ShowListUser -> {
                                            scope.launch {
                                                modalListUserBottomSheetSate.show()
                                            }
                                        }
                                        is OlboardHomeInteractionEvents.OpenPipMode -> {
                                            requestPauseAllVideo()
                                            context.maybeShowPipActivity(
                                                VideoActivity(
                                                    context,
                                                    infoVideo
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
                                            val moreOptionWrapper =
                                                MoreOptionWrapper(true, infoVideo)
                                            requestMoreOption = moreOptionWrapper
                                            /*/ TODO Want to test booking ,
                                            scope.launch {
                                                modalBookNowSheetState.show()
                                            }*/
                                        }
                                        is OlboardHomeInteractionEvents.ReactionHeart -> {
                                            chatLiveStreamViewModel.sendReactionRoom()
                                            if (isLikeRoom?.value == false) {
                                                uiStateChatLiveStream.value.roomStream?.setLikeRoom(
                                                    true
                                                )
                                            }
                                            playBackViewModel.trackingViewLiveStream(
                                                AnalyticsKey.ActionClickLikeFirebase,
                                                info
                                            )
                                        }
                                        is OlboardHomeInteractionEvents.ShowInfoStream -> {
                                            openDescriptionVideo.value = !openDescriptionVideo.value
                                        }
                                        is OlboardHomeInteractionEvents.BookNowService -> {
                                            scope.launch {
                                                modalBookNowSheetState.show()
                                            }
                                        }
                                        else -> {
                                            olMoInteractionEvents?.invoke(event, infoVideo)
                                        }
                                    }

                                },
                                countHeart = countHeart?.value?.countReaction,
                                isLike = isLikeRoom?.value,
                                countComment = countComment?.value,
                                playbacks = infoVideo,
                                roomChat = uiStateChatLiveStream.value.roomStream,
                                requestPauseAllVideo = requestPauseAllVideo,
                                requestPlayVideo = requestPlayVideo,
                                onClosePlayer = { status ->
                                    if (status) {
                                        chatLiveStreamViewModel.clearRoomLS()
                                        playBackViewModel.clearState()
                                        playBackViewModel.trackingViewLiveStream(
                                            AnalyticsKey.ActionClickCloseViewFirebase,
                                            liveInfoInternal
                                        )
                                        chatLiveStreamViewModel.clearState()
                                        chatPrivateViewModel.clearState()
                                        navController.popBackStack()
                                    }
                                },
                                showFollow = (liveInfoInternal.isFollow ?: false),
                                transformFollowAction = liveInfoUnFollow.value?.transformFollow,
                                actionFollow = actionFollowing,
                                onFinishedVideo = { status ->
                                    if (status) {
                                        playBackViewModel.trackingViewLiveStream(
                                            AnalyticsKey.ActionLiveStreamEndFirebase,
                                            liveInfoInternal
                                        )
                                    }
                                },
                                serviceBookingShow = uiStateBooking.serviceBookingShow?.toServiceBooking(),
                                numberServiceBookings = uiStateBooking.serviceBookings?.size ?: 0
                            )
                        }
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
                                    offsetY = height - 285
                                )
                            }
                        }
                    }
                }

                CommentVideoBottomSheet(
                    modalBottomSheetState = modalBottomSheetStateCommentVideo,
                    isShowAddComment = isShowAddComment,
                    isInputChat = isInputChat,
                    messageReply = messageReply,
                    roomChatState = uiStateChatLiveStream.value.roomStream, onReaction = {
                        chatLiveStreamViewModel.sendReactionMessageRoomLS(it?.id)
                    })
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
                            messageReply.value?.id,
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
            }
        }
        MoreOptionLiveStreamDialog(showDialog = requestMoreOption?.isSelected == true,
            msg = requestMoreOption?.user,
            livestreamInfo = requestMoreOption?.livestreamInfo,
            onCancel = {
                val moreOptionWrapper = MoreOptionWrapper(false, null, null)
                requestMoreOption = moreOptionWrapper
            },
            onShareOnSocialMedia = { info ->
                context.let {
                    requestMoreOption?.livestreamInfo?.recordUrl?.let { it1 ->
                        shareLinkVideo(
                            context,
                            it1
                        )
                    }
                    playBackViewModel.trackingViewLiveStream(
                        AnalyticsKey.ActionShareSocialFirebase,
                        info
                    )
                }
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
            onSendMessageToHost = {
                val moreOptionWrapper = MoreOptionWrapper(false, null, null)
                requestMoreOption = moreOptionWrapper
                scope.launch {
                    modalConversationDetailBottomSheetState.show()
                }
                playBackViewModel.trackingViewLiveStream(AnalyticsKey.ActionSendMessageFirebase, it)
            },
            onReportAdmin = {
                isShowReportDialog.value = true to it
                val moreOptionWrapper = MoreOptionWrapper(false, null, null)
                requestMoreOption = moreOptionWrapper
                playBackViewModel.trackingViewLiveStream(AnalyticsKey.ActionSendReportFirebase, it)
            }
        )
        val closePopup = remember {
            mutableStateOf(false)
        }
        if (isShowReportDialog.value.first) {
            (context as MainActivity).showAsBottomSheet(closePopup) {
                ReportDialog(liveStreamId = isShowReportDialog.value.second?.id, onConfirm = {
                    closePopup.value = true
                })
                isShowReportDialog.value = Pair(false, null)
            }
        }

        ActionFollowDialog(
            user = liveInfo.value?.user,
            showDialog = openDialogActionUnFollow.value,
            onCancel = {
                openDialogActionUnFollow.value = false
            },
            onConfirm = { user ->
                user?.id?.let { playBackViewModel.deleteUserFollowing(it) }
                val liveInfoFinal = liveInfoUnFollow.value
                liveInfoUnFollow.value =
                    liveInfoFinal?.copy(transformFollow = false, isFollow = false)
                liveInfoUnFollow.value = liveInfoFinal?.copy(transformFollow = false, isFollow = false)
                actionFollowing.value = Pair(liveInfoFinal?.id ?: 0, false)
                openDialogActionUnFollow.value = false
            })
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
        LoaderWithAnimation(isLoading)
    } else {
        EmptyOnLiveBody()
    }

    ConversationDetailBottomSheet(
        roomChatState = uiStateChatPrivate.value.roomStream,
        modalBottomSheetState = modalConversationDetailBottomSheetState,
        onSendMessage = {
            chatPrivateViewModel.sendTextMessagePC(it, null)
        },
        onLoadMore = {
            chatLiveStreamViewModel.loadMoreLS()
        }, onImageSent= {
            chatPrivateViewModel.sendImageMessagePC(it,null)
        }
    )

    if (!isInputChat.value) {
        messageReply.value = null
    }

    ListUserChatBottomSheet(listData = uiStateChatLiveStream.value.roomStream?.userMap,
        modalBottomSheetState = modalListUserBottomSheetSate, onUserSelected = { user ->
            chatPrivateViewModel.getRoomChatSingle(user.id)
            scope.launch {
                modalListUserBottomSheetSate.hide()
                modalConversationDetailBottomSheetState.show()
            }
        })

    val navigationWebView = remember {
        mutableStateOf<WrapperUrlPayment?>(null)
    }

    val packageOptionSelected = uiStatePlayBack.packageOptionSelected
    val wrapperUrlPayment = uiStatePlayBack.wrapperUrlPayment

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
                navigationWebView.value =  null
            }
            scope.launch {
                modalPaymentMethodsBottomSheetState.hide()
            }
        }
    }

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
    if(bookingServiceFromSelection != null){
        BookNowBottomSheet(booking = bookingServiceFromSelection.copy(tempStamp = System.currentTimeMillis()) ,
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
                modalPaymentMethodsBottomSheetState.show()
                modalContactInformationBottomSheetState.hide()
                modalContactInformationBottomSheetState.hide()
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

    LaunchedEffect(resultActivity){
        snapshotFlow{resultActivity}.take(1).collectLatest { result ->
            if(result.value?.isNotEmpty() == true){
                if(result.value == Constants.BUNDLE_DATA_PAYMENT_BOOKING_SUCCESS){
                    scope.launch {
                        modalContactInformationBottomSheetState.hide()
                        modalPaymentSuccessBottomSheetState.show()
                        paymentRequireWrapper.value = null
                        resultActivity.value = null
                    }
                }
            }
        }
    }

    PurchaseSuccessBottomSheet(modalBottomSheetState = modalPaymentSuccessBottomSheetState, wrapperUrlPayment, onNavigationDetail = {
        navController.popBackStack()
        navController.navigate(ScreenName.MyDashBoardBookingScreen.route)
    })
}

fun dismissDialogMoreOption(): MoreOptionWrapper {
    return MoreOptionWrapper(false, null, null)
}


