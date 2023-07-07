package olmo.wellness.android.ui.screen.following_profile

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collectLatest
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogConfirmWithIconLiveStream
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButtonWithIcon
import olmo.wellness.android.ui.common.live_stream.SingleLiveInfoItem
import olmo.wellness.android.ui.livestream.report.ReportDialog
import olmo.wellness.android.ui.livestream.stream.data.TypeTitleLivestream
import olmo.wellness.android.ui.livestream.utils.Effects
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.playback_video.home.buildListActions
import olmo.wellness.android.ui.screen.playback_video.profile.ProfileType
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_2.scrollEnabled
import olmo.wellness.android.ui.screen.search_home.SearchHomeBottomSheet
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*
private const val CELL_COUNT = 2

@SuppressLint(
    "RememberReturnType", "StateFlowValueCalledInComposition",
    "UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition"
)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@ExperimentalFoundationApi
@Composable
fun FollowingProfileScreen(
    navController: NavController,
    defaultLiveStream: LiveSteamShortInfo,
    viewModel: FollowingProfileViewModel = hiltViewModel()
){
    LaunchedEffect(key1 = "Init"){
        viewModel.bindData(defaultLiveStream)
    }
    val listState = androidx.compose.foundation.lazy.grid.rememberLazyGridState()
    val profileType = remember {
        mutableStateOf(ProfileType.MY_PROFILE)
    }

    val modalSearchBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )
    val scope = rememberCoroutineScope()

    val liveStreamInfo = viewModel.liveDefaultInfo.collectAsState()
    val listUserFollow = viewModel.userFollow.collectAsState()
    val listUserFollowerState = viewModel.userFollower.collectAsState()
    val totalUser = listUserFollow.value?.size ?: 0
    val listUserFollower = listUserFollowerState.value?.size ?: 0
    val profileModel = viewModel.profile.collectAsState()
    val name = viewModel.nameUser.collectAsState()
    val userType = viewModel.userType.collectAsState()
    val userTypeFinal = userType.value
    val lifecycleOwner = LocalLifecycleOwner.current
    val totalUnseenNotificationState = viewModel.totalUnseenNotification.collectAsState()
    val totalUnseenNotification = totalUnseenNotificationState.value
    val isLoading = viewModel.isLoading.collectAsState()
    val isFollowSuccess = viewModel.followSuccess.collectAsState()
    val showDialogRemovedLiveStream = remember {
        mutableStateOf(false)
    }
    Effects.Disposable(
        lifeCycleOwner = lifecycleOwner,
        onStart = {
            viewModel.getNotificationUnseen()
        },
        onStop = {}
    )
    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                true
            })
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        modifier = Modifier
            .fillMaxHeight(),
        sheetContent = {
            Box(modifier = Modifier.defaultMinSize(minHeight = 100.dp))
        }){
        Box(modifier = Modifier.fillMaxSize()) {
            val listLiveStreamState = viewModel.liveStreamList.collectAsState()
            val listLiveStreamPinState = viewModel.listVideoPin.collectAsState()
            val listLiveStream = listLiveStreamState.value
            val listLiveStreamPin = listLiveStreamPinState.value
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.reload()
                })
            {
                Box(modifier = Modifier
                    .background(color = White)
                    .fillMaxSize()){
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)) {
                        AsyncImage(
                            model = R.drawable.olmo_bg_profile,
                            contentDescription = "olmo_bg_profile",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxWidth(), alignment = Alignment.TopStart
                        )
                    }
                    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                        columns = GridCells.Fixed(CELL_COUNT),
                        state = listState,
                        modifier = Modifier,
                    ){
                        item(span = {
                            androidx.compose.foundation.lazy.grid.GridItemSpan(
                                currentLineSpan = CELL_COUNT
                            )
                        }){
                            Box {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 100.dp)
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 30.dp,
                                                topEnd = 30.dp
                                            )
                                        )){
                                    ConstraintLayout(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(color = Color.White)
                                            .wrapContentHeight()
                                    ){}
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(top = 70.dp, start = 16.dp)
                                        .fillMaxWidth()
                                        .wrapContentHeight()){
                                    if(profileModel.value != null){
                                        val rememberResource = rememberAsyncImagePainter(model = R.drawable.olmo_ic_group_default_place_holder)
                                        AsyncImage(modifier = Modifier
                                            .size(88.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, Color.White, CircleShape),
                                            contentScale = ContentScale.Crop,
                                            model = profileModel.value?.avatar.orEmpty(),
                                            contentDescription = "avatar_home",
                                            error = painterResource(id = R.drawable.olmo_ic_group_default_place_holder), placeholder = rememberResource)
                                    }else{
                                        AsyncImage(
                                            model = R.drawable.olmo_ic_group_default_place_holder,
                                            contentDescription = "avatar",
                                            modifier = Modifier
                                                .size(88.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop,
                                        )
                                    }
                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 40.dp, start = 26.dp, end = 26.dp)) {
                                        FollowingCompose(
                                            totalUser = totalUser,
                                            totalFollower = listUserFollower,
                                            userType = userTypeFinal,
                                            navController = navController)
                                    }
                                }
                            }
                        }
                        item(span = {
                            androidx.compose.foundation.lazy.grid.GridItemSpan(
                                currentLineSpan = CELL_COUNT
                            )
                        }){
                            Column(modifier = Modifier.background(color = White)){
                                if(profileModel.value == null && (name.value.isNullOrEmpty())){
                                    DefaultProfileItem(userTypeFinal)
                                }else{
                                    HeaderProfile(
                                        liveSteamShortInfo = liveStreamInfo.value,
                                        userType = userTypeFinal,
                                        name = name.value,
                                        bio = profileModel.value?.bio.orEmpty(),
                                        navController = navController,
                                        viewModel = viewModel
                                    )
                                    if(listLiveStreamPin?.isNotEmpty() == true){
                                        SpotLight(profileType.value, listLiveStreamPin)
                                    }
                                    LiveStream()
                                    if(listLiveStream.isNullOrEmpty()){
                                        DefaultLiveStreamItem(userTypeFinal)
                                    }
                                }
                            }
                        }

                        listLiveStream?.size?.let {
                            items(it, key = { liveStream -> liveStream }) { index ->
                                listLiveStream[index]?.let { it1 ->
                                    Column(modifier = Modifier.background(color = White)) {
                                        SingleVideoItem(navController,it1, viewModel)
                                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                                    }
                                }
                            }
                        }

                        item(span = {
                            androidx.compose.foundation.lazy.grid.GridItemSpan(
                                currentLineSpan = CELL_COUNT
                            )
                        }){
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .background(color = White)
                                .height(80.dp)){}
                        }
                    }
                }
            }
            LoadingScreen(isLoading = isLoading.value)
        }
        TopAppBar(
            backgroundColor = Transparent,
            elevation = 0.dp,
            contentPadding = PaddingValues(0.dp),
            content = {
                Box {
                    AsyncImage(
                        model = R.drawable.olmo_bg_profile,
                        contentDescription = "olmo_bg_profile",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(48.dp),
                        alignment = Alignment.TopStart
                    )
                    DetailTopBar(
                        navController = navController,
                        backgroundColor = Transparent,
                        title = "",
                        actions = buildListActions(navController, totalUnseenNotification, scope =scope, modalSearchBottomSheetState ),
                        elevation = 0.dp,
                        backIconDrawableTintColor = White,
                        onOpenDrawer = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        )
    }
    LaunchedEffect(isFollowSuccess.value){
        snapshotFlow { isFollowSuccess.value }.collectLatest { status ->
            if(status){
                showDialogRemovedLiveStream.value = true
            }
        }
    }
    if(showDialogRemovedLiveStream.value){
        DialogConfirmWithIconLiveStream(openDialogCustom = showDialogRemovedLiveStream, confirmCallback = {
            showDialogRemovedLiveStream.value = false
            viewModel.resetFollowStatus()
        }, content = stringResource(id = R.string.title_confirm_dialog_following_success))
    }
    SearchHomeBottomSheet(modalSearchBottomSheetState, navController, onCloseCallBack = { status ->
        /*if(status){
            scope.launch {
                modalSearchBottomSheetState.hide()
            }
        }*/
    })
}

@Composable
fun SingleVideoItem(navController : NavController,
                    singleLive: LiveSteamShortInfo,
                    viewModel: FollowingProfileViewModel) {
    Column(
        modifier = Modifier
            .background(White)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SingleLiveInfoItem(modifier = Modifier
            .background(White), liveStreamInfo = singleLive, sectionType = SectionType.LIVE_NOW, onItemClick = {
            val liveData = it.copy(fromProfile = true, typeTitle = TypeTitleLivestream.FinishedOfSeller)
            navController.navigate(ScreenName.ExploreLiveStreamScreen.route.plus("?defaultData=${liveData.toJson()}"))
        }, onItemLongClick = {
        }, isDisableLiveBadge = true,
//            contentScale = FixedScale(2f)
        )
    }
}

@Composable
fun LiveStream() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            text = "Livestream",
            style = MaterialTheme.typography.button.copy(
                color = Color.Black,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(
                start = 16.dp,
                bottom = 5.dp
            )
        )
    }
}

@Composable
fun SpotLight(profileType: ProfileType, listData : List<LivestreamInfo?>?) {
    if(listData?.isNotEmpty() == true){
        Column(
            modifier = Modifier
                .padding(
                    top = 26.dp,
                )
                .background(color = White)
        ) {
            Text(
                text = "Spotlight",
                style = MaterialTheme.typography.button.copy(
                    color = Color.Black,
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(
                    start = 16.dp
                )
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .scrollEnabled(true)
                    .padding(vertical = 16.dp)
            ) {
                items(listData.size) { index ->
                    SpotLightItem(listData[index])
                }
            }
        }
    }else{
        Column(
            modifier = Modifier
                .padding(
                    top = 26.dp,
                )
                .background(color = White)){
        }
    }
}

@Composable
fun SpotLightItem(livestreamInfo: LivestreamInfo?) {
    val rememberResource = rememberAsyncImagePainter(model = R.drawable.olmo_bg_thumnail_livestream)
    AsyncImage(
        model = livestreamInfo?.avatar ?: "",
        contentDescription = null,
        error = rememberResource,
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 8.dp
            )
            .size(72.dp)
            .clip(RoundedCornerShape(72.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun HeaderProfile(
    liveSteamShortInfo: LiveSteamShortInfo?,
    userType: UserTypeModel?, bio: String,
    name: String?,
    navController: NavController,
    viewModel: FollowingProfileViewModel
){
    val context = LocalContext.current
    val closeDialog = remember{
        mutableStateOf(false)
    }
    val closeDialogReport = remember{
        mutableStateOf(false)
    }
    val isShowReportDialog = remember{
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .background(color = White)
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp)
        .wrapContentHeight()) {
        Row(modifier = Modifier.padding(top = 16.dp)) {
            var nameStore = if(userType == UserTypeModel.BUYER){
                stringResource(id = R.string.title_my_profile_your_name)
            }else{
                stringResource(id = R.string.value_default_store_name)
            }
            if(name?.isNotEmpty() == true){
                nameStore = name.orEmpty()
            }
            if(nameStore.isNotEmpty()){
                nameStore = if(nameStore.length > 16){
                    nameStore.substring(0,14) + "..."
                }else{
                    nameStore
                }
            }
            Text(
                nameStore, style = MaterialTheme.typography.subtitle2.copy(
                    color = Neutral_Gray_9,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 28.sp)
            )
        }
        ExpandingText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .wrapContentHeight(),
            text = bio
        )
        val isVisibleFollow = liveSteamShortInfo?.isFollow ?: false
        var textFollow = if(isVisibleFollow){
            R.string.action_following
        }else{
            R.string.action_follow
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White)
                .padding(
                    bottom = 32.dp,
                    top = 16.dp
                )
        ){
            SecondLiveButtonWithIcon(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
                    .noRippleClickable {
                    },
                stringResource(id = textFollow),
                onClickFunc = {
                    if(isVisibleFollow){
                        (context as MainActivity).showAsBottomSheet(closeDialog) {
                            FollowOptionBottomSheet(onCancelCallBack = {
                                closeDialog.value = true
                            }, onConfirmCallBack = { option ->
                                closeDialog.value = true
                                if(option == OptionFollowType.REPORT_OPTION){
                                    isShowReportDialog.value = true
                                }
                                if(option == OptionFollowType.UNFOLLOW_OPTION){
                                    liveSteamShortInfo?.user?.id?.let { it1 ->
                                        viewModel.deleteUserFollowing(
                                            it1
                                        )
                                    }
                                }
                            })
                        }
                    }else{
                        viewModel.postUserFollow(liveSteamShortInfo?.userId)
                    }
                },
                iconRight = R.drawable.olmo_ic_arrow_down_purple,
                isVisibleIcon = isVisibleFollow
            )
            PrimaryLiveButton(
                enable = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                text = stringResource(id = R.string.action_message),
                onClickFunc = {
                    navController.navigate(
                        ScreenName.ConversationDetailScreen.route.plus(
                            "?${Constants.KEY_ID}=${
                                liveSteamShortInfo?.userId
                            }"
                        )
                    )
                }
            )
        }
    }
    LaunchedEffect(isShowReportDialog.value){
        snapshotFlow { isShowReportDialog.value }.collectLatest { status ->
            if(status){
                (context as MainActivity).showAsBottomSheet(closeDialogReport) {
                    ReportDialog(liveStreamId = liveSteamShortInfo?.id, onConfirm = {
                        closeDialogReport.value = true
                        isShowReportDialog.value = false
                    })
                }
                closeDialogReport.value = false
                isShowReportDialog.value = false
            }
        }
    }
}

@Composable
fun FollowingCompose(totalUser : Int,
                     totalFollower: Int,
                     userType: UserTypeModel?,
                     navController: NavController){
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()){
        val baselineShift = 0.5f
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Neutral_Gray_9,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        baselineShift = BaselineShift(baselineShift)
                    )
                ) {
                    append(""+totalUser)
                    append("\n")
                }
                withStyle(
                    style = SpanStyle(
                        color = Neutral_Gray_7,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                ) {
                    append("Following")
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(28.dp)
                .background(color = Neutral_Gray_4)
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Neutral_Gray_9, fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        baselineShift = BaselineShift(baselineShift)
                    )
                ){
                    append(""+totalFollower)
                    append("\n")
                }
                withStyle(
                    style = SpanStyle(
                        color = Neutral_Gray_7, fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                ) {
                    append("Follower")
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable
fun ExpandingText(modifier: Modifier = Modifier, text: String) {
    val MAX_LINES = 3
    var isExpanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    var isClickable by remember { mutableStateOf(false) }
    var finalText by remember { mutableStateOf(text) }
    var moreText by remember {
        mutableStateOf("")
    }

    val textLayoutResult = textLayoutResultState.value
    LaunchedEffect(textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect
        when {
            isExpanded -> {
                finalText = "$text "
                if(moreText.trim() == "... See More"){
                    moreText = "Show Less"
                }
            }
            !isExpanded && textLayoutResult.hasVisualOverflow -> {
                val lastCharIndex = textLayoutResult.getLineEnd(MAX_LINES - 1)
                val showMoreString = "... See More"
                moreText = showMoreString
                val adjustedText = text
                    .substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(showMoreString.length)
                    .dropLastWhile { it == ' ' || it == '.' }
                finalText = adjustedText
                isClickable = true
            }
        }
    }

    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Neutral_Gray_9, fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                )
            ) {
                append(finalText)
            }
            withStyle(
                style = SpanStyle(
                    color = Color_BLUE_OF8, fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            ) {
                append(moreText)
            }
        },
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.caption.copy(
            lineHeight = 16.sp
        ),
        maxLines = if (isExpanded) Int.MAX_VALUE else MAX_LINES,
        onTextLayout = { textLayoutResultState.value = it },
        modifier = modifier
            .noRippleClickable {
                isExpanded = !isExpanded
            }
            .animateContentSize(),
    )
}

@Composable
fun DefaultProfileItem(userType: UserTypeModel?){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 42.dp, end = 42.dp)
        .wrapContentHeight(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        if(userType != UserTypeModel.BUYER){
            SpaceCompose(height = 42.dp)
        }else{
            SpaceCompose(height = 124.dp)
        }
        Text(text = "Welcome to Kepler", style = MaterialTheme.typography.subtitle1.copy(
            color = Color_LiveStream_Main_Color,
            lineHeight = 16.sp,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        ))
        SpaceCompose(height = 38.dp)
        AsyncImage(model = R.drawable.olmo_img_place_holder_setting_profile, contentDescription = "setting", modifier = Modifier.size(120.dp))
        SpaceCompose(height = 30.dp)
        Text(text = "Please set up your information", style = MaterialTheme.typography.subtitle1.copy(
            color = Color.Black,
            lineHeight = 16.sp,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        ))
        if(userType != UserTypeModel.BUYER){
            SpaceCompose(height = 68.dp)
            AsyncImage(model = R.drawable.olmo_img_welcome_signup_png, contentDescription = "setting_profile", modifier = Modifier.size(100.dp,120.dp))
            SpaceCompose(height = 30.dp)
            Text(text = "And create your first livestream", style = MaterialTheme.typography.subtitle1.copy(
                color = Color.Black,
                lineHeight = 16.sp,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            ))
        }
    }
}

@Composable
fun DefaultLiveStreamItem(userType: UserTypeModel?){
    val content = when(userType){
        UserTypeModel.BUYER -> {
            "Your shared video will appear here"
        }
        else -> {
            "Your video livestream will appear here"
        }
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 42.dp, end = 42.dp, top = 68.dp)
        .wrapContentHeight(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(model = R.drawable.olmo_ic_group_default_welcome_live_stream, contentDescription = "setting_profile", modifier = Modifier.size(134.dp,160.dp))
        SpaceCompose(height = 30.dp)
        Text(text = content, style = MaterialTheme.typography.subtitle1.copy(
            color = Color.Black,
            lineHeight = 16.sp,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        ))
    }
}

