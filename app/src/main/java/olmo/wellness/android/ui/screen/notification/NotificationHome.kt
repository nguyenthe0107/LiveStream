package olmo.wellness.android.ui.screen.notification

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.core.toJson
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogConfirmWithIconLiveStream
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.livestream.schedule.component.SwipeCompose
import olmo.wellness.android.ui.livestream.stream.data.LivestreamStatus
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun NotificationHome(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
){
    NotiHome(navController = navController, viewModel)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotiHome(navController: NavController, viewModel: NotificationViewModel) {
    Scaffold(topBar = {
        ToolbarSchedule(
            title = "Notifications",
            navController = navController,
            backgroundColor = Color_LiveStream_Main_Color,
            onBackStackFunc = {
                val idToken = sharedPrefs.getToken()
                if (idToken.isNullOrEmpty()) {
                    navController.navigate(ScreenName.OnBoardSignUpScreen.route)
                } else {
                    var flag = false
                    try {
                        if(navController.backQueue.isNotEmpty()){
                            navController.backQueue.forEach {
                                if (it.destination.route == ScreenName.OnboardLiveScreen.route) {
                                    flag = true
                                    if(navController.currentDestination != null){
                                        navController.popBackStack()
                                    }else{
                                        flag = false
                                    }
                                    return@forEach
                                }
                            }
                        }
                    }catch (ex: Exception){
                    }
                    if (!flag) {
                        navController.navigate(ScreenName.OnboardLiveScreen.route)
                    }
                }
            }
        )
    }, content = {
        val isLoading = viewModel.isLoading.collectAsState()
        val showDialogRemovedLiveStream = remember {
            mutableStateOf(false)
        }
        val showDialogFinal = viewModel.isShowDialogRemoved.collectAsState().value
        showDialogRemovedLiveStream.value = showDialogFinal
        val allowNavigationOnLive = viewModel.allowNavigationLiveNow.collectAsState()
        val allowNavigationOnPlayBack = viewModel.allowNavigationPlayBack.collectAsState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color_LiveStream_Main_Color)
        ) {
            NotificationList(viewModel = viewModel, navController = navController)
            LoadingScreen(isLoading = isLoading.value)
            DialogConfirmWithIconLiveStream(
                openDialogCustom = showDialogRemovedLiveStream,
                confirmCallback = {
                    showDialogRemovedLiveStream.value = false
                    viewModel.updateStateDialogRemoved()
                },
                content = stringResource(id = R.string.title_confirm_dialog_removed_livestream)
            )
            val liveDataSelected = viewModel.liveStreamSelected.collectAsState()
            if (liveDataSelected.value != null && allowNavigationOnLive.value) {
                LaunchedEffect(true) {
                    val liveData = liveDataSelected.value
                    val liveDataFinal = liveData?.copy(status = LivestreamStatus.ON_LIVE.name)
                    navController.navigate(ScreenName.PlayBackOnLiveStreamScreen.route.plus("?defaultData=${liveDataFinal?.toJson()}"))
                    viewModel.resetStateAllowNavigation()
                }
            }
            if (allowNavigationOnPlayBack.value && liveDataSelected.value != null) {
                LaunchedEffect(true) {
                    val liveData = liveDataSelected.value
                    val liveDataFinal = liveData?.copy(status = LivestreamStatus.FINISHED.name, recordUrl = liveData.recordUrl.orEmpty(), fromSearch = true)
                    navController.navigate(ScreenName.ExploreLiveStreamScreen.route.plus("?defaultData=${liveDataFinal?.toJson()}"))
                    viewModel.resetStateAllowNavigation()
                }
            }
        }
    })
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun NotificationList(
    viewModel: NotificationViewModel,
    navController: NavController
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val recently = viewModel.recently.collectAsState()
    val recentlyFinal = recently.value
    val older = viewModel.older.collectAsState()
    val olderFinal = older.value
    LazyColumn(
        modifier = Modifier
            .padding(
                top = 10.dp
            )
            .background(
                Neutral_Gray_2,
                shape = RoundedCornerShape(
                    topEnd = 30.dp,
                    topStart = 30.dp
                )
            )
            .fillMaxSize(),
        state = listState
    ) {
        if (recentlyFinal.isNullOrEmpty() && olderFinal.isNullOrEmpty()) {
            item {
                Column {
                    EmptyData(resource = R.string.title_notification_empty)
                }
            }
            return@LazyColumn
        }
        if (recentlyFinal.isNotEmpty()) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    HeaderNotification(stringResource(id = R.string.title_notification_section_recently))
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            items(items = recentlyFinal, key = {
                it?.id ?: ""
            }) { notiData ->
                Row(
                    modifier = Modifier
                        .background(color = White)
                        .fillMaxWidth()
                ) {
                    SwipeCompose(
                        color = White,
                        isShowDeleteItemDefault = true,
                        isShowOptionDefault = false,
                        content = {
                            notiData?.let {
                                NotificationListItem(
                                    item = it,
                                    context = context,
                                    clickListener = { notificationInfo ->
                                        if (it.isSeen == false) {
                                            it.id?.let { it1 -> viewModel.seenNotification(it1) }
                                        }
                                        val liveData = it.dataNotification?.payload?.livestream
                                        viewModel.fetchPlayBacks(liveData)
                                    },
                                    isShowDivider = true
                                )
                            }
                        }, callbackDeleteItemUpcoming = {
                            notiData?.let {
                                it.id?.let { it1 ->
                                    viewModel.deleteNotification(
                                        isRecently = true,
                                        notiId = it1
                                    )
                                }
                            }
                        }, disableSwipeLeftToRight = true
                    )
                }
            }
        }

        if (olderFinal.isNotEmpty()) {
            item {
                Column() {
                    Spacer(modifier = Modifier.height(16.dp))
                    HeaderNotification(stringResource(id = R.string.title_notification_section_older))
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            items(items = olderFinal, key = {
                it?.id ?: ""
            }) { notiData ->
                SwipeCompose(
                    color = White,
                    isShowDeleteItemDefault = true,
                    isShowOptionDefault = false,
                    content = {
                        notiData?.let {
                            NotificationListItem(
                                item = it,
                                context = context,
                                clickListener = { notificationInfo ->
                                    if (it.isSeen == false) {
                                        it.id?.let { it1 -> viewModel.seenNotification(it1) }
                                    }
                                    it.dataNotification?.payload?.livestream?.id?.let { liveStreamId ->
                                        viewModel.handleNavigation(
                                            liveStreamId
                                        )
                                    }
                                })
                        }
                    }, callbackDeleteItemUpcoming = {
                        notiData?.let {
                            it.id?.let { it1 ->
                                viewModel.deleteNotification(
                                    notiId = it1,
                                    isOlder = true
                                )
                            }
                        }
                    }, disableSwipeLeftToRight = true
                )
            }
        }
    }
    // This will start loading more when reaches at total - 5 items
    listState.OnBottomReached(buffer = 5) {
        viewModel.loadMoreNotification()
    }
}

@Composable
fun HeaderNotification(titleHeader: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Transparent)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titleHeader,
            style = MaterialTheme.typography.subtitle2.copy(
                color = Neutral_Gray_9,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        )
    }
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color_LiveStream_Main_Color)
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        /*Text(
            text = message,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6,
            color = Color.Red
        )
        onClickRetry?.let {
            OutlinedButton(onClick = it) {
                Text(text = "Try again")
            }
        }*/
    }
}

@Composable
fun LazyListState.OnBottomReached(
    // tells how many items before we reach the bottom of the list
    // to call onLoadMore function
    buffer: Int = 0,
    onLoadMore: () -> Unit
) {
    // Buffer must be positive.
    // Or our list will never reach the bottom.
    require(buffer >= 0) { "buffer cannot be negative, but was $buffer" }
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true
            // subtract buffer from the total items
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer
        }
    }
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect { if (it) onLoadMore() }
    }
}
