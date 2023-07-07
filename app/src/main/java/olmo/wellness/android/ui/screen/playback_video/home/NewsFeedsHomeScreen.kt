package olmo.wellness.android.ui.screen.playback_video.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.domain.model.fcm.NotificationModel
import olmo.wellness.android.domain.model.livestream.HashTag
import olmo.wellness.android.domain.model.livestream.HomeLiveSectionData
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.common.ShimmerBox
import olmo.wellness.android.ui.common.live_stream.SingleLiveInfoItem
import olmo.wellness.android.ui.helpers.DateTimeHelper.getSessionDay
import olmo.wellness.android.ui.livestream.stream.data.TypeTitleLivestream
import olmo.wellness.android.ui.livestream.utils.Effects
import olmo.wellness.android.ui.screen.for_you.category.SubCategoryBottomSheet
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.voucher_service.VoucherServiceBottomSheet
import olmo.wellness.android.ui.screen.search_home.SearchHomeBottomSheet
import olmo.wellness.android.ui.screen.voucher.DialogReceivedVoucher
import olmo.wellness.android.ui.services.RemoteMessageObserver
import olmo.wellness.android.ui.theme.*

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun NewsFeedsHomeScreen(
    navController: NavController,
    openBottomSheet: MutableState<Boolean>? = null,
    viewModel: NewsFeedsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val modalSearchBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    Effects.Disposable(
        lifeCycleOwner = lifecycleOwner,
        onStart = {
            viewModel.getProfileLocal()
            viewModel.getNotificationUnseen()
        },
        onStop = {}
    )
    val uiState = viewModel.uiState.collectAsState()

    val listCategories = remember(uiState.value.listCategories) {
        uiState.value.listCategories
    }

    val listSubCategories = remember(uiState.value.listSubCategories) {
        uiState.value.listSubCategories
    }

    val titleCategory = remember(uiState.value.title) {
        uiState.value.title
    }

    /*val listHashTags = remember(uiState.value.listHashTags) {
        uiState.value.listHashTags
    }*/

    val listBodySection = remember(uiState.value.listBodySection) {
        uiState.value.listBodySection
    }
    val totalUnseenNotification = remember(uiState.value.totalUnseenNotification) {
        uiState.value.totalUnseenNotification
    }
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    openBottomSheet?.value = modalBottomSheetState.isVisible
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        modifier = Modifier
            .fillMaxHeight(),
        sheetContent = {
            Box(modifier = Modifier.defaultMinSize(minHeight = 100.dp))
        }) {
        val brush = Brush.horizontalGradient(
            colors = listOf(
                Color_LiveStream_Main_Color,
                Color_LiveStream_Main_Color,
                Color_Purple_Gradient,
                Color_Purple_Gradient
            )
        )
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
        Box(
            modifier = Modifier.background(
                brush
            )
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.reload()
                },
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(brush)
                        .padding(top = 56.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxSize()
                    ) {
                        GreetingSection(uiState.value.profile)
                        Column(
                            modifier = Modifier
                                .background(White)
                                .fillMaxSize()
                        ){
                            CategoriesSection(listCategories = listCategories, onViewAllClick = {
                                viewModel.getSubCategories(it)
                                scope.launch {
                                    modalBottomSheetState.show()
                                }
                                viewModel.sendTrackingCategory2(it.id)
                            })
                            /*Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .background(Neutral_Gray)
                            )
                            TrendingHashTags(listHashTags, navController)
                            */
                            ListBodySections(navController, listBodySection, viewModel)
                            Box(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
        TopAppBar(
            backgroundColor = Transparent,
            elevation = 0.dp,
            contentPadding = PaddingValues(0.dp),
            content = {
                Box(modifier = Modifier.background(Transparent)) {
                    DetailTopBar(
                        navController = navController,
                        backgroundColor = Transparent,
                        actions = buildListActions(
                            navController,
                            totalUnseenNotification,
                            scope,
                            modalSearchBottomSheetState
                        ),
                        title = "",
                        elevation = 0.dp,
                        backIconDrawable = R.drawable.ic_hambuger,
                        backIconDrawableTintColor = White,
                        onOpenDrawer = {
                            navController.navigate(ScreenName.AccountSwitcherScreen.route)
                        }
                    )
                }
            })
    }

    SubCategoryBottomSheet(
        modalBottomSheetState = modalBottomSheetState,
        titleCategory = titleCategory,
        listCategories = listSubCategories,
        onClick = {
            scope.launch {
                modalBottomSheetState.hide()
                navController.navigate(ScreenName.SubCategoriesScreen.route.plus("?title=${it.nameLocale?.en}").plus("&id=${it.id}"))
            }
            viewModel.sendTrackingCategory3(it.id)
        }
    )

    SearchHomeBottomSheet(modalSearchBottomSheetState, navController, onCloseCallBack = { status ->
        if(status){
            scope.launch {
                modalSearchBottomSheetState.hide()
            }
        }
    })

    val remoteMessageObserver: RemoteMessageObserver by lazy { RemoteMessageObserver.getInstance() }
    val isObserverResultFromFirebase = remember {
        mutableStateOf("")
    }
    scope.launch {
        remoteMessageObserver.result.collectLatest { message ->
            if (message?.data != null && message.data.isNotEmpty()) {
                isObserverResultFromFirebase.value = message.data.toString()
            }
        }
    }
    LaunchedEffect(isObserverResultFromFirebase) {
        snapshotFlow { isObserverResultFromFirebase.value }.collectLatest { message ->
            try {
                val payLoad: NotificationModel =
                    Gson().fromJson(message, NotificationModel::class.java)
                if (payLoad.type != null) {
                    viewModel.reload()
                }
            } catch (ex: Exception) {
            }
        }
    }
}

@Composable
fun ListBodySections(
    navController: NavController,
    list: List<HomeLiveSectionData>?,
    viewModel: NewsFeedsViewModel
){
    val rememberList = remember(list) {
        list
    }
    if (list.isNullOrEmpty()) {
        ShimmerBox(
            height = 48.dp,
            radius = 16.dp,
            modifier = Modifier.padding(
                top = 10.dp,
                start = 16.dp,
                end = 16.dp
            )
        )
    } else {
        rememberList?.forEach { section ->
            BodySection(
                section = section, navController = navController, viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BodySection(
    section: HomeLiveSectionData,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: NewsFeedsViewModel?
) {
    val sectionData = remember(section.data) {
        section.data
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                bottom = 16.dp
            )
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Neutral_Gray)
        )
        SectionHeader(
            sectionName = section.title ?: "",
            modifier = Modifier.padding(
                top = 16.dp
            )
        ){
            viewModel?.sendTrackingSeeAllSection(section.title)
            val sectionType = section.type
            val sectionTitle = section.title
            if (sectionType == SectionType.UPCOMING.value) {
                navController.navigate(ScreenName.SeeAllUpComingScreen.route)
            } else {
                navController.navigate(
                    ScreenName.SeeAllFilterLiveStreamScreen.route.plus("?section_type=${sectionType}")
                        .plus("&section_title=${sectionTitle}")
                )
            }
        }
        val listState = rememberLazyListState()
        LazyRow(
            modifier = Modifier
                .padding(
                    top = 24.dp
                ), state = listState
        ) {
            items(sectionData?.size ?: 0, key = { it }) { index ->
                section.data?.get(index)?.apply {
                    SingleLiveInfoItem(
                        modifier = Modifier
                            .padding(
                                start = 16.dp
                            ),
                        liveStreamInfo = this,
//                        contentScale = FixedScale(2f),
                        sectionType = section.getSectionType(),
                        onItemClick = {
                            when (section.type) {
                                SectionType.UPCOMING.value -> {
                                    navController.navigate(ScreenName.EventDetailScreen.route.plus("?${Constants.KEY_ID}=${it.id}"))
                                }
                                SectionType.LIVE_NOW.value -> {
                                    val liveData = it.copy(recordUrl = it.playbackUrl)
                                    navController.navigate(
                                        ScreenName.PlayBackOnLiveStreamScreen.route.plus(
                                            "?defaultData=${liveData.toJson()}"
                                        )
                                    )
                                    viewModel?.trackingViewLiveStream(it)
                                }
                                SectionType.EVENT.value -> {
                                    val liveData = it.copy(
                                        recordUrl = it.playbackUrl,
                                        typeTitle = TypeTitleLivestream.Event
                                    )
                                    navController.navigate(
                                        ScreenName.ExploreLiveStreamScreen.route.plus(
                                            "?defaultData=${liveData.toJson()}"
                                        )
                                    )
                                    viewModel?.trackingViewLiveStream(it)
                                }
                                SectionType.TOP_TRENDING.value -> {
                                    val liveData = it.copy(
                                        recordUrl = it.playbackUrl,
                                        typeTitle = TypeTitleLivestream.TopTrending
                                    )
                                    navController.navigate(
                                        ScreenName.ExploreLiveStreamScreen.route.plus(
                                            "?defaultData=${liveData.toJson()}"
                                        )
                                    )
                                    viewModel?.trackingViewLiveStream(it)
                                }
                                SectionType.RECOMMENDED.value -> {
                                    val liveData = it.copy(
                                        recordUrl = it.playbackUrl,
                                        typeTitle = TypeTitleLivestream.Recommended
                                    )
                                    navController.navigate(
                                        ScreenName.ExploreLiveStreamScreen.route.plus(
                                            "?defaultData=${liveData.toJson()}"
                                        )
                                    )
                                    /*val liveData = it.copy(recordUrl = it.playbackUrl, typeTitle = TypeTitleLivestream.LiveNow)
                                    navController.navigate(
                                        ScreenName.PlayBackOnLiveStreamScreen.route.plus(
                                            "?defaultData=${liveData.toJson()}"
                                        )
                                    )*/
                                    viewModel?.trackingViewLiveStream(it)
                                }
                            }
                        })
                }
            }
        }
    }
}


@Composable
fun TrendingHashTags(
    listHashTag: List<HashTag>?,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = 24.dp,
                bottom = 16.dp
            )
    ) {
        SectionHeader(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            "Trending Hashtag",
        ) {
            //View All Hash Tag
        }

        if (listHashTag.isNullOrEmpty()) {
            ShimmerBox(
                height = 48.dp,
                radius = 16.dp,
                modifier = Modifier.padding(
                    top = 10.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp)
            ) {
                items(listHashTag.size) { index ->
                    val hashTag = listHashTag.get(index)
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(
                                start = 16.dp
                            )
                            .background(
                                Color_LiveStream_Main_Color,
                                shape = RoundedCornerShape(32.dp)
                            )
                    ) {
                        TrendingHashtagSingleItem(
                            hashTag ?: return@Box,
                            modifier = Modifier
                                .wrapContentSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    sectionName: String,
    onViewAllClick: (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
            )
    ) {
        Text(
            text = sectionName,
            style = MaterialTheme.typography.body1.copy(
                color = Black,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = stringResource(R.string.lb_see_all),
            style = MaterialTheme.typography.subtitle2.copy(
                color = Color_BLUE_OF8,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.clickable {
                onViewAllClick?.invoke()
            }
        )
    }
}

@Composable
fun TrendingHashtagSingleItem(
    hashTag: HashTag,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            )
            .wrapContentHeight()
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RoundedAsyncImage(
            imageUrl = "",
            cornerRadius = 18.dp,
            size = 18.dp,
        )
        Box(modifier = Modifier.width(8.dp))
        Text(
            text = "#${hashTag.name}",
            style = MaterialTheme.typography.body2.copy(
                color = White
            )
        )
        Box(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_eye_home),
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(12.dp)
        )
        Box(modifier = Modifier.width(4.dp))
        Text(
            text = if ((hashTag.maxViewCount
                    ?: 0) <= 1000
            ) "${hashTag?.maxViewCount ?: 1000}" else "${(hashTag?.maxViewCount ?: 0) / 1000}K",
            style = MaterialTheme.typography.body2.copy(
                color = White,
                fontSize = 10.sp
            )
        )
    }
}

@Composable
fun GreetingSection(
    profile: ProfileInfo?
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Transparent)
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
    ) {
        val (title, image) = createRefs()
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = 100.dp)
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                buildAnnotatedString {
                    if (profile?.name?.isNotEmpty() == true) {
                        withStyle(
                            SpanStyle(
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = White,
                            )
                        ) {
                            append(getSessionDay() + "\n")
                        }
                    } else {
                        withStyle(
                            SpanStyle(
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 25.sp,
                                color = White,
                            )
                        ) {
                            append(getSessionDay() + "\n")
                        }
                    }
                    if (profile?.name?.isNotEmpty() == true) {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 25.sp,
                                color = White,
                            )
                        ) {
                            append(profile.name + "\n")
                        }
                    }
                }
            )
        }
        Row(modifier = Modifier.constrainAs(image) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        }, horizontalArrangement = Arrangement.End) {
            AsyncImage(
                model = R.drawable.olmo_img_onboard_right,
                contentDescription = "img_right",
                modifier = Modifier.size(94.dp, 112.dp),
            )
        }
    }
}

@Composable
fun CategoriesSection(listCategories: List<LiveCategory>?,
                      onViewAllClick: (LiveCategory) -> Unit) {
    if (listCategories.isNullOrEmpty()) {
        ShimmerBox(
            height = 120.dp,
            radius = 16.dp,
            modifier = Modifier.padding(
                top = 10.dp,
                start = 16.dp,
                end = 16.dp
            )
        )
    } else {
        val listState = rememberLazyListState()
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 24.dp,
                    bottom = 16.dp
                ),
            state = listState
        ) {
            items(listCategories.size) { index ->
                val category = listCategories[index]
                ViewItemCategory(onViewAllClick, category)
            }
        }
    }
}

@Composable
fun ViewItemCategory(
    onViewAllClick: (LiveCategory) -> Unit,
    category: LiveCategory,
    modifier: Modifier = Modifier,
    circleSize: Dp = 50.dp
) {
    Column(
        modifier = modifier
            .noRippleClickable {
                onViewAllClick.invoke(category)
            }
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(RoundedCornerShape(circleSize))
                .border(
                    width = (1.5).dp,
                    color = Color_BLUE_7F4,
                    shape = RoundedCornerShape(circleSize)
                )
        ) {
            AsyncImage(
                category.icon,
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Inside,
                error = painterResource(R.drawable.img_talk)
            )
        }

        Text(
            text = category.nameLocale?.en ?: "",
            modifier = Modifier
                .padding(
                    top = 10.dp
                )
                .width(70.dp),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1.copy(
                color = Black_037,
                fontSize = 12.sp
            )
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun buildListActions(
    navController: NavController,
    totalUnseenNotification: Int? = null,
    scope: CoroutineScope,
    modalSearchBottomSheetState: ModalBottomSheetState
): @Composable() (RowScope.() -> Unit) {
    return {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier.noRippleClickable {
                scope.launch {
                    modalSearchBottomSheetState.show()
                }
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        if (totalUnseenNotification != 0) {
            Icon(
                painter = painterResource(id = R.drawable.olmo_ic_has_noti),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier.noRippleClickable {
                    navController.navigate(ScreenName.NotificationHomeScreen.route)
                }
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.noRippleClickable {
                    navController.navigate(ScreenName.NotificationHomeScreen.route)
                }
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}


