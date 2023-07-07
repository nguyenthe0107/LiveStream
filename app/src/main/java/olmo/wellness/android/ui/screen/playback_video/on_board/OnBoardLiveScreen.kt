package olmo.wellness.android.ui.screen.playback_video.on_board

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.hideSystemUI
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.ui.broadcast_receiver.SystemBroadcastReceiver
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.calendar_screen.CalendarScreen
import olmo.wellness.android.ui.screen.for_you.screen.ForYouScreen
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.notification.NotificationHome
import olmo.wellness.android.ui.screen.playback_video.home.NewsFeedsHomeScreen
import olmo.wellness.android.ui.screen.playback_video.home.buildListActions
import olmo.wellness.android.ui.screen.playback_video.live_home.LiveHomeBuyerScreen
import olmo.wellness.android.ui.screen.playback_video.live_home.LiveHomeScreen
import olmo.wellness.android.ui.screen.playback_video.profile.LiveProfileScreen
import olmo.wellness.android.ui.screen.search_home.SearchHomeBottomSheet
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.screen.video_small.ACTION_PIP_CONTROL
import olmo.wellness.android.ui.screen.video_small.onIntentReceivedForPipController
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Color_Purple_Gradient
import olmo.wellness.android.ui.theme.White

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter")
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@OptIn(ExperimentalPagerApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@Composable
fun OnBoardLiveScreen(
    navController: NavController,
    viewModel: OnboardViewModel = hiltViewModel()
) {
    val userModel = viewModel.userType.collectAsState()
    val profileModel = viewModel.profileModel.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val userRole = if (userModel.value != null) {
        userModel.value
    } else {
        UserTypeModel.KOL
    }
    val context = LocalContext.current as MainActivity
    val openBottomSheet = remember {
        mutableStateOf(false)
    }
    val reloadProfile = remember {
        mutableStateOf(false)
    }
    if(reloadProfile.value){
        viewModel.reloadProfile()
        reloadProfile.value = false
    }
    val modalSearchBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )
    val scope = rememberCoroutineScope()
    val totalUnseenNotificationState = viewModel.totalUnseenNotification.collectAsState()
    val totalUnseenNotification = totalUnseenNotificationState.value
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                viewModel.getProfile()
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
            }
            Lifecycle.Event.ON_DESTROY -> {
//                viewModel.disconnectRtc()
            }
            else -> {
            }
        }
    }

    if (userRole != null) {
        val tabData = generateTabs(userRole)
        val pagerState = rememberPagerState(
            initialPage = 0
        )

        val tabIndex = pagerState.currentPage
        val isSelected = remember {
            mutableStateOf(false)
        }

        isSelected.value = tabIndex == 1
        var isLoading by remember {
            mutableStateOf(true)
        }
        val isExpandedFullScreen = remember { mutableStateOf(false) }

        val bottomSize = remember {
            mutableStateOf(
                IntSize(0, 0)
            )
        }
        val brush = Brush.horizontalGradient(
            colors = listOf(
                Color_LiveStream_Main_Color,
                Color_LiveStream_Main_Color,
                Color_Purple_Gradient,
                Color_Purple_Gradient
            )
        )
        val bottomBarBackgroundColor = animateColorAsState(
            targetValue = if (isExpandedFullScreen.value)
                Color_LiveStream_Main_Color.copy(
                    alpha = 0f
                )
            else
                Color_LiveStream_Main_Color.copy(
                    alpha = 0.5f
                )
        )

        SystemBroadcastReceiver(systemAction = ACTION_PIP_CONTROL) { intent ->
            intent.onIntentReceivedForPipController(
                onClosePip = {
                },
                onRequestOpenContent = {
                    if (it == null)
                        return@onIntentReceivedForPipController
                    if (it.isLiveStream == true) {
                        navController.navigate(
                            ScreenName.PlayBackOnLiveStreamScreen.route
                                .plus("?defaultData=${it.toJson()}")
                        )
                    } else {
                        navController.navigate(
                            ScreenName.ExploreLiveStreamScreen.route
                                .plus("?defaultData=${it.toJson()}")
                        )
                    }
                }
            )
        }

        Scaffold(
            modifier = Modifier.background(brush),
            topBar = {
                when (tabIndex) {
                    1 -> {
                        Column(
                            modifier = Modifier.background(Color_LiveStream_Main_Color)
                        ) {
                            DetailTopBar(
                                navController = navController,
                                backgroundColor = Color_LiveStream_Main_Color,
                                title = "",
                                actions = buildListActions(
                                    navController,
                                    totalUnseenNotification,
                                    scope,
                                    modalSearchBottomSheetState
                                ),
                                elevation = 0.dp,
                                backIconDrawable = R.drawable.ic_hambuger,
                                backIconDrawableTintColor = White,
                                onOpenDrawer = {
                                    navController.navigate(ScreenName.AccountSwitcherScreen.route)
                                }
                            )
                        }
                    }
                }
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = !openBottomSheet.value,
                    enter = expandVertically(expandFrom = Alignment.Top) { 0 },
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 300)) { fullHeight ->
                        0
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OnboardBottomBar(
                        modifier = Modifier,
                        pagerState = pagerState,
                        tabs = tabData,
                        backgroundColor = bottomBarBackgroundColor.value,
                        onSizeChanged = { size ->
                            bottomSize.value = size
                        },
                        userRole = userRole,
                        onTabSelected = { tab, userRole ->
                            onBottomTabSelected(navController, tab, userRole, context)
                        },
                        avatarLink = profileModel.value.avatar.orEmpty()
                    )
                }
            }
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .background(Color_LiveStream_Main_Color)
                    .fillMaxSize()
            ) {
                val (body) = createRefs()
                HorizontalPager(
                    userScrollEnabled = false,
                    state = pagerState,
                    count = tabData.size,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(body) {
                            bottom.linkTo(parent.bottom)
                            top.linkTo(parent.top)
                            height = Dimension.fillToConstraints
                        }
                ) { index ->
                    when (tabData[index].type) {
                        BottomBarOnboardTabData.TabType.HOME -> {
                            NewsFeedsHomeScreen(navController, openBottomSheet)
                        }
                        BottomBarOnboardTabData.TabType.FOR_YOU -> {
                            ForYouScreen(navController, openBottomSheet)
                        }
                        BottomBarOnboardTabData.TabType.LIVE -> {
                            LiveHomeScreen()
                        }
                        BottomBarOnboardTabData.TabType.NOTIFICATION -> {
                            NotificationHome(navController)
                        }
                        BottomBarOnboardTabData.TabType.CALENDAR -> {
                            CalendarScreen(
                                navController = navController,
                                fillData = null,
                                isBack = false
                            )
                        }
                        BottomBarOnboardTabData.TabType.PROFILE -> {
                            LiveProfileScreen(navController, openBottomSheet, reloadProfile)
                        }
                        BottomBarOnboardTabData.TabType.ACTIVITIES -> {
                            LiveHomeBuyerScreen(navController)
                        }
                    }
                }
            }
        }
        coroutineScope.launch {
            delay(200)
            isLoading = false
        }
        LoaderWithAnimation(isLoading && pagerState.currentPage != 0)
        /* Observer Result from Another Screen */
        var isObserverResultFromLive by remember {
            mutableStateOf<Boolean?>(null)
        }
        var dataResult by remember {
            mutableStateOf("")
        }
        navController.currentBackStackEntry
            ?.savedStateHandle?.getLiveData<String>(Constants.BUNDLE_DATA)
            ?.observe(LocalLifecycleOwner.current) { result ->
                if (result != null) {
                    dataResult = result
                    isObserverResultFromLive = when (result) {
                        Constants.BUNDLE_DATA_CLOSE_LIVE_STREAM -> {
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            }
        if (isObserverResultFromLive != null) {
            LaunchedEffect(true) {
                if (dataResult.isNotEmpty()) {
                    when (dataResult) {
                        Constants.BUNDLE_DATA_CLOSE_LIVE_STREAM -> {
                            isObserverResultFromLive = null
                            coroutineScope.launch {
                                pagerState.scrollToPage(4)
                            }
                        }
                        else -> {
                            isObserverResultFromLive = null
                            coroutineScope.launch {
                                pagerState.scrollToPage(0)
                            }
                        }
                    }
                }
            }
        }
    }
    SearchHomeBottomSheet(modalSearchBottomSheetState, navController, onCloseCallBack = { status ->
    })
}

@OptIn(ExperimentalPagerApi::class)
private fun onBottomTabSelected(
    navController: NavController,
    tab: BottomBarOnboardTabData,
    userRole: UserTypeModel,
    context: Context
) {
    when (tab.type) {
        BottomBarOnboardTabData.TabType.HOME -> {
        }
        BottomBarOnboardTabData.TabType.FOR_YOU -> {
        }
        BottomBarOnboardTabData.TabType.LIVE -> {
            navController.navigate(ScreenName.PermissionLivestreamStreamerScreen.route)
        }
        BottomBarOnboardTabData.TabType.ACTIVITIES -> {
        }
        BottomBarOnboardTabData.TabType.NOTIFICATION -> {

        }
        BottomBarOnboardTabData.TabType.CALENDAR -> {
        }
        BottomBarOnboardTabData.TabType.PROFILE -> {
        }
    }
}

private fun generateTabs(userRole: UserTypeModel?): List<BottomBarOnboardTabData> {
    return when (userRole) {
        UserTypeModel.BUYER -> listOf(
            BottomBarOnboardTabData(
                icon = R.drawable.ic_home,
                iconSelected = R.drawable.ic_home,
                BottomBarOnboardTabData.TabType.HOME
            ),
            BottomBarOnboardTabData(
                icon = R.drawable.ic_for_you,
                iconSelected = R.drawable.ic_for_you,
                BottomBarOnboardTabData.TabType.FOR_YOU
            ),
            /* BottomBarOnboardTabData(
                 icon = R.drawable.ic_history,
                 iconSelected = R.drawable.ic_history,
                 BottomBarOnboardTabData.TabType.ACTIVITIES
             ),*/
            /*BottomBarOnboardTabData(
                icon = R.drawable.olmo_ic_notification_buyer,
                iconSelected = R.drawable.olmo_ic_notification_buyer,
                BottomBarOnboardTabData.TabType.NOTIFICATION
            ),*/
            BottomBarOnboardTabData(
                icon = R.drawable.ic_calendar_home,
                iconSelected = R.drawable.ic_calendar_home,
                BottomBarOnboardTabData.TabType.CALENDAR
            ),
            BottomBarOnboardTabData(
                icon = R.drawable.olmo_ic_group_default_place_holder,
                iconSelected = R.drawable.olmo_ic_group_default_place_holder,
                BottomBarOnboardTabData.TabType.PROFILE
            ),
        )

        UserTypeModel.SELLER,
        UserTypeModel.KOL -> listOf(
            BottomBarOnboardTabData(
                icon = R.drawable.ic_home,
                iconSelected = R.drawable.ic_home,
                BottomBarOnboardTabData.TabType.HOME
            ),
            BottomBarOnboardTabData(
                icon = R.drawable.ic_for_you,
                iconSelected = R.drawable.ic_for_you,
                BottomBarOnboardTabData.TabType.FOR_YOU
            ),
            BottomBarOnboardTabData(
                icon = R.drawable.ic_live,
                iconSelected = R.drawable.ic_live,
                BottomBarOnboardTabData.TabType.LIVE
            ),
            BottomBarOnboardTabData(
                icon = R.drawable.ic_calendar_home,
                iconSelected = R.drawable.ic_calendar_home,
                BottomBarOnboardTabData.TabType.CALENDAR
            ),
            BottomBarOnboardTabData(
                icon = R.drawable.olmo_ic_group_default_place_holder,
                iconSelected = R.drawable.olmo_ic_group_default_place_holder,
                BottomBarOnboardTabData.TabType.PROFILE
            ),
        )
        else -> emptyList()
    }
}



