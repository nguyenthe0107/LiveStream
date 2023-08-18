package olmo.wellness.android.ui.livestream.view.streamer

import android.annotation.SuppressLint
import android.text.TextUtils
import android.widget.RelativeLayout
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.*
import olmo.wellness.android.core.Constants.BUNDLE_DATA
import olmo.wellness.android.core.Constants.BUNDLE_DATA_CLOSE_LIVE_STREAM
import olmo.wellness.android.core.Constants.BUNDLE_DATA_RELOAD_HOME_TAB
import olmo.wellness.android.core.Constants.KEY_SUMMARY_LIVE_DATA
import olmo.wellness.android.core.utils.shareSocialMedia
import olmo.wellness.android.core.utils.shareVideoTikTok
import olmo.wellness.android.core.utils.shareWithEmail
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.data.model.live_stream.FillDataLiveStream
import olmo.wellness.android.data.model.live_stream.LiveStreamRequest
import olmo.wellness.android.domain.model.SocialNetwork
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.booking.list_service.view.ListOfServiceBottomSheet
import olmo.wellness.android.ui.booking.viewmodel.BookingPlayBackViewModel
import olmo.wellness.android.ui.chat.conversation_detail.ConversationDetailBottomSheet
import olmo.wellness.android.ui.chat.private_chat.viewmodel.PrivateChatViewModel
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogConfirmed
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogWarningLiveStream
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.livestream.chatlivestream.view.*
import olmo.wellness.android.ui.livestream.chatlivestream.viewmodel.ChatLivestreamViewModel
import olmo.wellness.android.ui.livestream.countDownt.CountDownLiveStreamView
import olmo.wellness.android.ui.livestream.info_livestream.ActionMessageDialog
import olmo.wellness.android.ui.livestream.info_livestream.AddDescriptionBottomSheet
import olmo.wellness.android.ui.livestream.info_livestream.InfoLiveStreamBottomSheet
import olmo.wellness.android.ui.livestream.utils.Heart
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.booking_service.add_booking.AddBookingServiceBottomSheet
import olmo.wellness.android.ui.screen.booking_service.add_booking.AddBookingServiceViewModel
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.NotifyFollowersBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.broadcast_audience.AudienceType
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.broadcast_audience.BroadCastAudienceBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.list_user.ListUserChatBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category.SelectCategoryBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.share.ShareBottomSheet
import olmo.wellness.android.ui.screen.playback_video.common.LiveStreamIconsSectionBottom
import olmo.wellness.android.ui.screen.playback_video.common.LiveStreamIconsSectionTop
import olmo.wellness.android.ui.screen.playback_video.explore.OlboardHomeInteractionEvents
import olmo.wellness.android.ui.screen.playback_video.onlive.getHeartColor
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*

@SuppressLint(
    "StateFlowValueCalledInComposition",
    "CoroutineCreationDuringComposition",
    "MutableCollectionMutableState"
)
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun LiveStreamerScreen(
    navController: NavController,
    liveStreamerViewModel: LiveStreamerViewModel = hiltViewModel(),
    chatLiveStreamViewModel: ChatLivestreamViewModel = hiltViewModel(),
    chatPrivateViewModel: PrivateChatViewModel = hiltViewModel(),
//    bookingServiceViewModel: AddBookingServiceViewModel = hiltViewModel(),
//    bookingPlayBackViewModel: BookingPlayBackViewModel = hiltViewModel(),
) {

    val viewAWS = liveStreamerViewModel.previewView.collectAsState()

    val uiStateChatLiveStream = chatLiveStreamViewModel.uiState.collectAsState()
    val uiStateChatPrivate = chatPrivateViewModel.uiState.collectAsState().value
//    val uiStateBooking = bookingPlayBackViewModel.uiState.collectAsState().value

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    val sendJoinRoomLS = remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    (context as MainActivity).hideSystemUI()

    val scope = rememberCoroutineScope()
    val isLoading = liveStreamerViewModel.isLoading.collectAsState()

    val isComment = remember {
        mutableStateOf(false)
    }
    isComment.value = uiStateChatLiveStream.value.isComment

    val roomLSID = liveStreamerViewModel.roomLSID.collectAsState()

    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp

//    val listFilters = remember {
//        mutableStateOf(getMasksFilters)
//    }

    val isStartFilter = remember {
        mutableStateOf(false)
    }

//    val pagerStateCarousel: PagerStateCarousel = run {
//        remember { PagerStateCarousel(0, 0, listFilters.value.size - 1) }
//    }

//    val selectedPageCarousel = remember { mutableStateOf(0) }

    /* Handle InfoLive Stream */


    val fillDataLiveStream = remember {
        mutableStateOf(FillDataLiveStream().copy())
    }
    val categoryListSelected by remember {
        mutableStateOf<MutableList<LiveCategory>>(mutableListOf())
    }

    val countHeart = uiStateChatLiveStream.value.roomStream?.getHeartReaction()?.collectAsState()

    val numberUser = uiStateChatLiveStream.value.roomStream?.getCountUser()?.collectAsState()

    val heartCount = remember {
        mutableStateOf(countHeart?.value?.countReaction)
    }

    heartCount.value = countHeart?.value?.countReaction

    if (roomLSID.value?.isBlank() == false && sendJoinRoomLS.value) {
        sendJoinRoomLS.value = false
        chatLiveStreamViewModel.sendJoinRoomLS(
            _roomId = roomLSID.value,
            _hostRoomId = sharedPrefs.getUserInfoLocal().userId
        )
    }

    //-- check permissions
    var isStart by remember {
        mutableStateOf(false)
    }
    var isCloseStream by remember {
        mutableStateOf(false)
    }

    var isShowCountDown by remember {
        mutableStateOf(false)
    }

    val time = liveStreamerViewModel.time.collectAsState()
    val openBottomSheetInfoLivestream = remember {
        mutableStateOf(false)
    }

    var liveStreaming by remember {
        mutableStateOf(false)
    }

    var buttonLive by remember {
        mutableStateOf(true)
    }

    buttonLive = (fillDataLiveStream.value.title?.isBlank() == false)

    val openDialogCustom = remember {
        mutableStateOf(false)
    }

    val openDialogWarningLiveStream = remember {
        mutableStateOf(false)
    }

    val closeDialogBroadcast = remember {
        mutableStateOf(false)
    }

    val closeDialogShare = remember {
        mutableStateOf(false)
    }

    val openDialogActionMessage = remember {
        mutableStateOf(false)
    }

    val isShowInfoStream = remember {
        mutableStateOf(false)
    }

    val modalListUserBottomSheetSate =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalInfoLiveStreamBottomSheetSate =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                false
            })

    val modalNotifyBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                false
            })

    val modalAudienceBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                false
            })

    val modalDescriptionBottomSheetSate =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                false
            })


    val modalSelectCategoryBottomSheetSate =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                false
            })

    val modalConversationDetailBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalAddBookingServiceBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalListOfServiceBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val isStartStream = remember {
        mutableStateOf(false)
    }

    isStartStream.value = liveStreamerViewModel.isStartStream.value

    val isInputChat = remember {
        mutableStateOf(false)
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

    val isShowAddComment = remember {
        mutableStateOf(true)
    }

    val msgSelect = remember {
        mutableStateOf<ChatMessage?>(null)
    }

    var linkShare by remember {
        mutableStateOf("https://itviec.com/nha-tuyen-dung/olmo-technology")
    }

    val profileModel = liveStreamerViewModel.profile.collectAsState()

    /* Booking service */
    val servicesBookingSelectedFromListService = liveStreamerViewModel.bookingServiceSelectedFromListService.collectAsState()
    val liveStreamId = liveStreamerViewModel.livestreamId
    val roomId = liveStreamerViewModel.roomLSID

    if (isStartStream.value) {
        val request = LiveStreamRequest().also {
            val fillData = fillDataLiveStream.value
            if (!TextUtils.isEmpty(fillData.title)) {
                it.title = fillData.title.toString()
            }
            if (!TextUtils.isEmpty(fillData.hashtag)) {
                val tag = arrayListOf<String>(fillData.hashtag!!)
                it.hashtag = tag
            }
            if (fillData.listCategory?.isNotEmpty() == true) {
                it.categoryIds = fillData.listCategory?.map { category ->
                    category.id ?: 0
                }
            }
            if (fillData.serviceIds?.isNotEmpty() == true) {
                it.serviceIds = fillData.serviceIds?.map { service ->
                    service ?: 0
                }
            }
            /*it.isPrivate = viewModel.personSee.value?.id != 1*/
        }
        isShowCountDown = false
        liveStreaming = true
        liveStreamerViewModel.startLive(request, startSuccess ={
//            if (request.categoryIds?.isNotEmpty()==true){
//                bookingPlayBackViewModel.getServiceLivestream(liveStreamerViewModel.livestreamId)
//            }
        })
    }
    DialogConfirmed(openDialogCustom, cancelCallback = {
        openDialogCustom.value = false
        liveStreamerViewModel.setLoading(false)
    }, confirmCallback = {
        liveStreamerViewModel.setLoading(true)
        isCloseStream = true
        liveStreamerViewModel.stopLive()
        chatLiveStreamViewModel.clearState()
        chatPrivateViewModel.clearState()
        if (isCloseStream) {
            navController.popBackStack()
            val livestreamInfo = LivestreamInfo(
                title = fillDataLiveStream.value.title,
                totalUser = uiStateChatLiveStream.value.roomStream?.userMap?.size ?: 0,
                avatarProfile = profileModel.value?.avatar,
                heartCount = heartCount.value,
                viewCount = uiStateChatLiveStream.value.roomStream?.userMap?.size ?: 0
            ).toJson()
            navController.navigate(ScreenName.SummaryLiveStreamScreen.route.plus("?$KEY_SUMMARY_LIVE_DATA=${livestreamInfo}"))
            isCloseStream = false
        }
    })

    DialogWarningLiveStream(openDialogCustom = openDialogWarningLiveStream, confirmCallback = {
        openBottomSheetInfoLivestream.value = true
        openDialogWarningLiveStream.value = false
    }, cancelCallback = {
        openDialogWarningLiveStream.value = false
    })

    ActionMessageDialog(msgSelect.value, showDialog = openDialogActionMessage.value, onCancel = {
        dismissActionMessage(msgSelect, openDialogActionMessage, true)
    }, onLikeComment = { user, idMessage ->
//        viewModel.updateReactionMessage(idMessage)
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

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                liveStreamerViewModel.fetchListOfStreams()
                if (!isStart) {
                    isStart = true
                    liveStreamerViewModel.initializeDeepAR(context, lifecycleOwner.value)
                }
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
            }
            Lifecycle.Event.ON_DESTROY -> {
                liveStreamerViewModel.stopLive()
                chatPrivateViewModel.clearState()
                chatLiveStreamViewModel.clearState()
            }
            else -> {
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                factory = { ctx ->
                    RelativeLayout(ctx).apply {
                    }
                },
                update = {
                    println("update preview view")
                    if (viewAWS.value == null) {
                        it.removeAllViews()
                    } else {
                        if (viewAWS.value!!.parent == null) {
                            it.addView(viewAWS.value)
                        }
                    }
                }
            )
            if (isShowCountDown) {
                CountDownLiveStreamView(
                    time = time.value,
                    max = liveStreamerViewModel.maxProcess,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 56.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                val clipboardManager = LocalClipboardManager.current
                LiveStreamIconsSectionBottom(
                    avatar = profileModel.value?.avatar,
                    isLiveStream = liveStreaming,
                    interactionEvents = {
                        when (it) {
                            is OlboardHomeInteractionEvents.CloseLiveStream -> {
                                openDialogCustom.value = true
                            }
                            is OlboardHomeInteractionEvents.SwitchCamera -> {
                                liveStreamerViewModel.setLoading(true)
                                liveStreamerViewModel.setSwitchCamera(context)
                            }

                            is OlboardHomeInteractionEvents.AddBookService -> {
                                scope.launch {
                                    modalAddBookingServiceBottomSheetState.show()
                                }
                            }

                            is OlboardHomeInteractionEvents.SetInformationLiveStream -> {
                                scope.launch {
                                    modalInfoLiveStreamBottomSheetSate.show()
                                }
                                hideFilter(isStartFilter)
                            }
                            is OlboardHomeInteractionEvents.ReactionHeart -> {
                                chatLiveStreamViewModel.sendReactionRoom()
                                hideFilter(isStartFilter)
                            }

                            is OlboardHomeInteractionEvents.OpenModeBroadcast -> {
                                scope.launch {
                                    modalAudienceBottomSheetState.show()
                                }
                                hideFilter(isStartFilter)
                            }
                            is OlboardHomeInteractionEvents.FilterLiveStream -> {
                                isStartFilter.value = !isStartFilter.value
                            }
                        }
                        if (it is OlboardHomeInteractionEvents.ShareVideo) {
                            context.let { main ->
                                val listUser = uiStateChatLiveStream.value.roomStream?.userMap
                                    ?: emptyList<User>()
                                main.showAsBottomSheet(closeDialogShare) {
                                    ShareBottomSheet(
                                        userList = listUser.toList(),
                                        showMore = {
                                            closeDialogShare.value = true
                                            if (listUser.isNotEmpty()) {
                                                showListShareUser(listUser.toList(), context)
                                            }
                                        },
                                        onSocialNetworkShareRequest = { socialNetwork ->
                                            when (socialNetwork) {
                                                SocialNetwork.EMAIL -> {
                                                    shareWithEmail(context, linkShare)
                                                }
                                                SocialNetwork.TIKTOK -> {
                                                    shareVideoTikTok(context, linkShare)
                                                }
                                                else -> {
                                                    shareSocialMedia(
                                                        context,
                                                        socialNetwork,
                                                        linkShare
                                                    )
                                                }
                                            }
                                        },
                                        onUserShareRequest = { user ->
                                            // Call Api to share
                                        },
                                        onCopyLinkRequest = { link ->
                                            linkShare = link
                                            clipboardManager.setText(AnnotatedString((link)))
                                        }
                                    )
                                }
                            }
                        }
                    },
                    isComment = isComment.value,
                    countHeart = countHeart?.value?.countReaction,
                    countServiceBooking = servicesBookingSelectedFromListService.value.size
                )
            }


            if (isShowInfoStream.value) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(WidthView)
                        .background(color = Transparent)
                ) {
                    Text(
                        text = fillDataLiveStream.value.description ?: "",
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = White,
                            fontSize = 14.sp
                        ), modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 50.dp, start = 15.dp)

                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LiveStreamIconsSectionTop(
                Modifier.fillMaxWidth(),
                isStartStream.value,
                interactionEvents = {
                    when (it) {
                        OlboardHomeInteractionEvents.CloseLiveStream -> {
                            if (isStartStream.value) {
                                openDialogCustom.value = true
                            } else {
                                isCloseStream = true
                                if (isCloseStream) {
                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set(BUNDLE_DATA, BUNDLE_DATA_RELOAD_HOME_TAB)
                                    navController.popBackStack()
                                    isCloseStream = false
                                }
                            }
                        }
                        OlboardHomeInteractionEvents.ShowListUser -> {
                            scope.launch {
                                modalListUserBottomSheetSate.show()
                            }
                        }
                        OlboardHomeInteractionEvents.ShowInfoStream -> {
                            isShowInfoStream.value = !isShowInfoStream.value
                            chatLiveStreamViewModel.setIsComment(!isComment.value)
                        }
                        else -> {}
                    }
                },
                title = fillDataLiveStream.value.title ?: "",
                usersSeeWatch = numberUser?.value ?: 0,
                isShowInfo = isShowInfoStream.value
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 30.dp),
        ) {
            if (isComment.value) {
                Box(
                    modifier = Modifier
                        .padding(start = 15.dp)
                ) {
                    ChatLivestreamContent(uiState = uiStateChatLiveStream.value.roomStream,
                        onLoadMore = {
                            if (uiStateChatLiveStream.value.roomStream?.messages?.isNotEmpty() == true) {
                                chatLiveStreamViewModel.loadMoreLS()
                            }
                        }, onComment = {
                            isInputChat.value = true
                        }, isShowAddComment = isShowAddComment.value, onTouchMessage = { msg ->
                            if (sharedPrefs.getUserInfoLocal().userId != msg.userId) {
                                msgSelect.value = msg
                                openDialogActionMessage.value = !openDialogActionMessage.value
                            }
                        }, isBuyer = true,
                        serviceBooking = liveStreamerViewModel.bookingServiceShowing.collectAsState().value
                    )
                }
            }

            if (!isStartStream.value) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = 15.dp)
                            .size(space_64)
                            .clickable(enabled = true) {
                                if (!buttonLive) {
                                    openDialogWarningLiveStream.value = true
                                    return@clickable
                                }
                                scope.launch {
                                    modalNotifyBottomSheetState.show()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        val resourceDefault = remember {
                            mutableStateOf((R.drawable.olmo_ic_live_stream))
                        }
                        val resourceActivated = remember {
                            mutableStateOf((R.drawable.olmo_ic_live_stream_actived))
                        }
                        AsyncImage(
                            model = if (buttonLive) {
                                resourceActivated.value
                            } else {
                                resourceDefault.value
                            },
                            contentDescription = "Circle Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(space_64)
                                .clip(CircleShape)
                        )
                    }
                }

            }

//            if (isStartFilter.value) {
//                Box(modifier = Modifier) {
//                    PreparePager(pagerStateCarousel, listFilters.value, selectedPageCarousel)
//                }
//                liveStreamerViewModel.gotoNext(listFilters.value[selectedPageCarousel.value])
//            }

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
                    }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            heartCount.value?.let {
                repeat(it) { number ->
                    Heart(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.BottomCenter),
                        horizontalPadding = 0,
                        bottomMargin = 50,
                        color = getHeartColor(number),
                        number = number,
                        offsetX = width - 45,
                        offsetY = height - 225
                    )
                }
            }
        }
    }

    LoaderWithAnimation(isPlaying = isLoading.value)

    if (openBottomSheetInfoLivestream.value) {
        scope.launch {
            modalInfoLiveStreamBottomSheetSate.show()
            openBottomSheetInfoLivestream.value = false
        }
    }

    InfoLiveStreamBottomSheet(
        modalBottomSheetState = modalInfoLiveStreamBottomSheetSate,
        fillData = fillDataLiveStream,
        onDismiss = {
            scope.launch {
                modalInfoLiveStreamBottomSheetSate.hide()
            }
        },
        openDescription = {
            scope.launch {
                modalInfoLiveStreamBottomSheetSate.hide()
                modalDescriptionBottomSheetSate.show()
            }
        },
        openCategories = {
            scope.launch {
                modalInfoLiveStreamBottomSheetSate.hide()
                modalSelectCategoryBottomSheetSate.show()
            }

        },
        onConfirm = {
            if (it != null) {
                fillDataLiveStream.value = it
            }
            scope.launch {
                modalInfoLiveStreamBottomSheetSate.hide()
            }
        }
    )

    BroadCastAudienceBottomSheet(
        modalBottomSheetState = modalAudienceBottomSheetState,
        onSelected = { type ->
            liveStreamerViewModel.setPersonSee(type)
            scope.launch {
                modalAudienceBottomSheetState.hide()
            }
        },
        dismissDialog = {
            scope.launch {
                modalAudienceBottomSheetState.hide()
            }
        },
        defaultType = AudienceType(liveStreamerViewModel.personSee.value?.name.orEmpty())
    )

    SelectCategoryBottomSheet(
        modalBottomSheetState = modalSelectCategoryBottomSheetSate,
        onCategorySelected = { categorySelected ->
            if (!categorySelected.isNullOrEmpty()) {
                categoryListSelected.addAll(categorySelected)
                fillDataLiveStream.value = fillDataLiveStream.value.copy(
                    listCategory = categoryListSelected.distinctBy { it.id }
                )
            }
            closeDialogBroadcast.value = true
            scope.launch {
                modalInfoLiveStreamBottomSheetSate.show()
            }
        },
        onCancelBottomSheet = {
            scope.launch {
                modalInfoLiveStreamBottomSheetSate.show()
            }
        },
    )

    AddDescriptionBottomSheet(
        modalBottomSheetState = modalDescriptionBottomSheetSate,
        fillData = fillDataLiveStream.value,
//        focusRequest = focusRequester,
        onSave = {
//            focusManager.clearFocus()
            fillDataLiveStream.value = fillDataLiveStream.value.copy(
                description = it
            )
            scope.launch {
                modalDescriptionBottomSheetSate.hide()
                modalInfoLiveStreamBottomSheetSate.show()
            }
        })

    NotifyFollowersBottomSheet(
        modalBottomSheetState = modalNotifyBottomSheetState,
        confirmCallback = {
            liveStreamerViewModel.timer.start()
            isShowCountDown = true
            scope.launch {
                modalNotifyBottomSheetState.hide()
            }
        },
        cancelCallback = {
            scope.launch {
                modalNotifyBottomSheetState.hide()
            }
        })

    ListUserChatBottomSheet(listData = uiStateChatLiveStream.value.roomStream?.userMap,
        modalBottomSheetState = modalListUserBottomSheetSate, onUserSelected = { user ->
            chatPrivateViewModel.getRoomChatSingle(user.id)
            scope.launch {
                modalListUserBottomSheetSate.hide()
                modalConversationDetailBottomSheetState.show()
            }
        })

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

    /* Booking Service */
    AddBookingServiceBottomSheet(
        isLiveStream = liveStreaming,
        liveStreamId = liveStreamId,
        roomId =roomId.value ,
        servicesSelected = servicesBookingSelectedFromListService.value,
        modalBottomSheetState = modalAddBookingServiceBottomSheetState,
        confirmCallback = { listSelected ->
            scope.launch {
                    fillDataLiveStream.value = fillDataLiveStream.value.copy(
                        listServiceIds = listSelected?.map { it.id }
                    )
                modalAddBookingServiceBottomSheetState.hide()
            }
        },
        cancelCallback = { status ->
            if (status) {
                scope.launch {
                    modalAddBookingServiceBottomSheetState.hide()
                }
            }
        },
        openListOfService = { status ->
            if (status) {
                scope.launch {
                    modalListOfServiceBottomSheetState.show()
                }
            }
        },
        onSelectedItemToShowCallback = {
            liveStreamerViewModel.updateStatusSelectedServiceBooking(it)
        },
        notifyChangeListService = { newList ->
            if (newList?.isNotEmpty() == true) {
                liveStreamerViewModel.bindSelectedServiceBooking(newList)
            }
        })

    if (servicesBookingSelectedFromListService.value.isNotEmpty()) {
        val listSelected = servicesBookingSelectedFromListService.value.distinctBy { it.id }
        val listService = mutableListOf<Int>()
        if (listSelected.isNotEmpty()) {
            listSelected.map { info ->
                listService.add(info.id ?: 0)
            }
            fillDataLiveStream.value = fillDataLiveStream.value.copy(
                serviceIds = listService
            )
        }
    }

    ListOfServiceBottomSheet(modalBottomSheetState = modalListOfServiceBottomSheetState, onDone = {
        if (it?.isNotEmpty() == true) {
            liveStreamerViewModel.bindSelectedServiceBooking(it)
            if (isStart){
                liveStreamerViewModel.sendAddService(roomId = uiStateChatLiveStream.value.roomStream?.getRoomId(), liveStreamId =liveStreamId, serviceList = it )
            }
            scope.launch {
                modalListOfServiceBottomSheetState.hide()
            }
        }
    })

    /* OnBack-Handle */
    BackHandler {
        if (isInputChat.value) {
            clearInputChat(
                focusManager,
                isInputChat,
                isShowAddComment,
                textMessageValueState,
            )
        }
        isCloseStream = true
        liveStreamerViewModel.stopLive()
        chatLiveStreamViewModel.clearRoomLS()
        if (isCloseStream) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(BUNDLE_DATA, BUNDLE_DATA_CLOSE_LIVE_STREAM)
            navController.popBackStack()
            isCloseStream = false
        }

    }

    if (isInputChat.value) {
        hideFilter(isStartFilter)
    } else {
        if (!openDialogActionMessage.value) {
            msgSelect.value = null
        }
    }
}

private fun hideFilter(isStartFilter: MutableState<Boolean>) {
    isStartFilter.value = false
}


