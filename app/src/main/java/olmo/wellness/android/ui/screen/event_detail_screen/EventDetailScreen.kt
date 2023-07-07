package olmo.wellness.android.ui.screen.event_detail_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.core.utils.getScreenWidth
import olmo.wellness.android.core.utils.pxToDp
import olmo.wellness.android.domain.model.category.Category
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.avatar.Avatar
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.livestream.utils.Effects
import olmo.wellness.android.ui.screen.category_screen.CategoryScreenViewModel
import olmo.wellness.android.ui.screen.category_screen.SectionHeaderCategory
import olmo.wellness.android.ui.screen.category_screen.cell.SectionCategorySignUp
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category.components.HorizontalGridCategories
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun EventDetailScreen(
    navController: NavHostController,
    id : String?,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    Effects.Disposable(
        lifeCycleOwner = lifecycleOwner,
        onStart = {
            id?.toInt()?.let { viewModel.fetchListOfStreams(it) }
        },
        onStop = {}
    )
    val context = LocalContext.current
    val isLoading = viewModel.isLoading.collectAsState()
    val livestreamInfo = viewModel.listSchedulerCalendar.collectAsState()
    val livestreamInfoFinal = livestreamInfo.value
    val errMes = viewModel.errorContent.collectAsState()
    EventDetail(navController = navController,livestreamInfoFinal, viewModel)
//    LoadingScreen(isLoading = isLoading.value)
    LoaderWithAnimation(isPlaying = isLoading.value)

    if(errMes.value.isNotEmpty()){
        showMessage(context, errMes.value)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EventDetail(navController: NavHostController, data : List<LivestreamInfo>, viewModel: EventDetailViewModel) {
    Scaffold(content = {
        EvenInfo(navController, data)
    }, bottomBar = {
        BottomAction(viewModel,data)
    })
}

@Composable
private fun BottomAction(viewModel: EventDetailViewModel, data : List<LivestreamInfo>) {
    val infoDetails = data.firstOrNull()
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color_Gradient_button,
                        Color_LiveStream_Accent_Color,
                    )
                ), shape = RoundedCornerShape(50.dp)
            )
            .noRippleClickable {
                if (infoDetails?.isFollow == false) {
                    viewModel.postUserFollow()
                } else {
                    viewModel.deleteUserFollow()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        if(infoDetails?.isFollow == false){
            Icon(
                painter = painterResource(id = R.drawable.ic_bell_plus),
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(16.dp)
            )
        }
        Text(
            text = stringResource(R.string.lb_add_to_your_calendar),
            style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 16.sp, color = White,
                textAlign = TextAlign.Center
            ), modifier = Modifier.height(18.dp)
        )
    }
}

@Composable
fun EvenInfo(navController: NavHostController, data : List<LivestreamInfo>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item{
            CoverDetail(navController, data)
            Spacer(modifier = Modifier.padding(5.dp))
            Info(data)
            if (data.firstOrNull() != null && data.firstOrNull()?.categories?.isNotEmpty() == true) {
                SectionCategory(category = data.first())
            }
            Text(text = "Description", style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 16.sp
            ), modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 25.dp))

            Text(
                modifier = Modifier
                    .padding(top = 10.dp, start = 15.dp,end = 15.dp),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp
                ),
                text = data.firstOrNull()?.description ?: ""
            )
            SpaceCompose(height = 100.dp)
        }
    }
}

@Composable
fun CoverDetail(navController: NavHostController, data : List<LivestreamInfo>) {
    val brush = Brush.linearGradient(
        colors = listOf(
            Color_LiveStream_Main_Color,
            Color_LiveStream_Main_Color,
            Color_LiveStream_Light_Color,
        )
    )
    var isError by remember {
        mutableStateOf(false)
    }
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (box) = createRefs()
        val guidelineBottom = createGuidelineFromBottom(0f)
        Box(modifier = Modifier
            .background(brush = brush)
            .constrainAs(box) {
                linkTo(start = parent.start, end = parent.end)
                linkTo(parent.top, guidelineBottom)
                width = Dimension.ratio("16:9")
                height = Dimension.fillToConstraints
            }
            .height(200.dp)) {
            val infoDetails = data.firstOrNull()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .fillMaxHeight()) {
                if(infoDetails?.thumbnailUrl.isNullOrEmpty() || isError){
                    AsyncImage(
                        model = R.drawable.olmo_img_onboard_right,
                        contentDescription = "avatar_big",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(134.dp, 161.dp)
                            .align(Alignment.BottomCenter),
                    )
                }else{
                    AsyncImage(
                        model = infoDetails?.thumbnailUrl,
                        error = painterResource(id = R.drawable.olmo_img_onboard_right),
                        contentDescription = "avatar_big",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        onError = {
                            isError = true
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(15.dp)
                    .background(color = White, shape = CircleShape)
                    .clip(CircleShape)
                    .size(30.dp)
                    .clickable {
                        navController.popBackStack()
                    }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_violet),
                    tint = Color_LiveStream_Main_Color,
                    contentDescription = "Back"
                )
            }
        }
    }
}

@Composable
fun Info(data : List<LivestreamInfo>) {
    val infoDetails = data.firstOrNull()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        val (title, date, hour, avatar, labDescription, description) = createRefs()
        Text(text = infoDetails?.title ?: "", style = MaterialTheme.typography.subtitle2.copy(
            fontSize = 16.sp
        ), modifier = Modifier.constrainAs(title) {})

        Avatar(
            imageUrl = infoDetails?.thumbnailUrl ?: "https://picsum.photos/200", name = "Demo", modifier =
            Modifier
                .size(60.dp)
                .constrainAs(avatar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        )

        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .constrainAs(date) {
                    start.linkTo(title.start)
                    top.linkTo(title.bottom)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = "calendar",
                tint = Neutral_Gray_7,
                modifier = Modifier.size(15.dp)
            )
            val timeCreated = DateTimeHelper.convertToStringDate(infoDetails?.createdAt,DateTimeHelper.FORMAT_SCHEDULE_LINE).replace(",", "")
            if(timeCreated.isNotEmpty()){
                Text(
                    text = timeCreated, style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .height(15.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(10.dp)
                .constrainAs(hour) {
                    start.linkTo(date.end)
                    top.linkTo(title.bottom)
                },
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = "clock",
                tint = Neutral_Gray_7,
                modifier = Modifier.size(15.dp)
            )
            val time = DateTimeHelper.convertToStringHour(infoDetails?.startTime)
            if(time.isNotEmpty()){
                Text(
                    text = DateTimeHelper.convertToStringHour(infoDetails?.startTime), style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .height(15.dp)
                )
            }
        }
    }
}

@Composable
fun SectionCategory(
    category: LivestreamInfo,
    modifier: Modifier = Modifier,
){
    val sectionData = remember(category) {
        category
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                bottom = 16.dp
            )
    ){
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        SectionHeaderCategory(
            sectionName = "Categories",
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 10.dp
            ),
            color = Neutral_Gray_9,
            fontSize = 12
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(1),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .heightIn(
                    80.dp,
                    125.dp
                )
                .padding(
                    top = 8.dp
                )
        ){
            items(sectionData.categories?.size?:0, key = { it }){ index ->
                sectionData.categories?.get(index)?.apply {
                    CategoryItem(sectionData.categories[index])
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .size(width = (getScreenWidth() / 4).pxToDp(context).dp, height = 125.dp)
            .wrapContentSize()
            .noRippleClickable {
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textColor = Neutral_Gray_9
        val shadowColor = Color.Gray
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = shadowColor,
                    ambientColor = shadowColor
                ),
            color = White
        ) {
            Box(
                Modifier
                    .size(80.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = category.icon ?: "",
                    contentDescription = "",
                    error = painterResource(R.drawable.olmo_ic_group_default_place_holder),
                    modifier = Modifier
                        .size(80.dp)
                        .padding(20.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Inside
                )
            }
        }
        SpaceCompose(height = 16.dp)
        Text(
            text = category.name?:"",
            maxLines = 2,
            overflow = TextOverflow.Clip,
            style = MaterialTheme.typography.body2.copy(
                color = textColor,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}