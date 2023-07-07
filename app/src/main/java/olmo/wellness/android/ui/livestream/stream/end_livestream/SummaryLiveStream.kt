package olmo.wellness.android.ui.livestream.stream.end_livestream

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*
import kotlin.math.abs

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SummaryLiveStream(
    navController: NavController,
    livestreamInfo: LivestreamInfo? = null,
    viewModel: SummaryLiveStreamViewModel = hiltViewModel()
) {

    LaunchedEffect("Init") {
        viewModel.loadDefaultInfo(livestreamInfo)
    }
    val isLoading = remember {
        viewModel.isLoading
    }
    var isBackHome by remember {
        mutableStateOf(false)
    }
    val liveStreamState = viewModel.liveStreamInfo.collectAsState()
    if (liveStreamState.value != null) {
        val liveStreamInfo = liveStreamState.value
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color_PURPLE_335)
        ) {
            val (topActionbar, content) = createRefs()
            Image(painter = painterResource(id = R.drawable.olmo_ic_close_white),
                contentDescription = "icon_close",
                modifier = Modifier
                    .constrainAs(topActionbar) {
                        end.linkTo(parent.end)
                    }
                    .padding(end = marginDouble, top = 20.dp)
                    .noRippleClickable {
                        isBackHome = true
                    }
            )
            LazyColumn(
                content = {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                AsyncImage(
                                    model = R.drawable.olmo_ic_group_summary_live_left,
                                    contentDescription = "left",
                                    modifier = Modifier.size(26.dp, 61.dp)
                                )
                                SpaceHorizontalCompose(width = 5.dp)
                                if (liveStreamInfo?.avatarProfile != null) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(liveStreamInfo.avatarProfile)
                                            .crossfade(true)
                                            .build(),
                                        error = painterResource(id = R.drawable.olmo_bg_success_live_stream),
                                        contentDescription = "image-avatar",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(72.dp)
                                            .clip(CircleShape)
                                    )
                                } else {
                                    AsyncImage(
                                        model = R.drawable.olmo_bg_success_live_stream,
                                        contentDescription = "profile",
                                        modifier = Modifier.size(128.dp)
                                    )
                                }
                                SpaceHorizontalCompose(width = 5.dp)
                                AsyncImage(
                                    model = R.drawable.olmo_ic_group_summary_live_right,
                                    contentDescription = "right",
                                    modifier = Modifier.size(26.dp, 61.dp)
                                )
                            }

                            Text(
                                text = liveStreamInfo?.title ?: "",
                                style = MaterialTheme.typography.subtitle2.copy(
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    lineHeight = 32.sp
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                    item {
                        SpaceCompose(height = 32.dp)
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(128.dp)
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .background(Color_Purple_7F4_20)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.olmo_ic_eye_filled),
                                        contentDescription = "reviewer"
                                    )
                                    val totalUser = livestreamInfo?.viewCount?.toString() ?: "1"
                                    Text(
                                        text = totalUser,
                                        style = MaterialTheme.typography.subtitle2.copy(
                                            color = Color.White,
                                            fontSize = 28.sp,
                                            lineHeight = 36.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            SpaceHorizontalCompose(width = 20.dp)
                            Box(
                                modifier = Modifier
                                    .size(128.dp)
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .background(Color_Purple_7F4_20)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.olmo_ic_chat_mes_white),
                                        contentDescription = "chatting",
                                    )
                                    val userReview = (livestreamInfo?.viewCount ?: 1).toString()
                                    Text(
                                        text = userReview,
                                        style = MaterialTheme.typography.subtitle2.copy(
                                            color = Color.White,
                                            fontSize = 28.sp,
                                            lineHeight = 36.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    item {
                        SpaceCompose(height = 20.dp)
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(128.dp)
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .background(Color_Purple_7F4_20)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.olmo_ic_heart_white),
                                        contentDescription = "heart",
                                    )
                                    val totalUser = livestreamInfo?.heartCount?.toString() ?: "1"
                                    Text(
                                        text = totalUser,
                                        style = MaterialTheme.typography.subtitle2.copy(
                                            color = Color.White,
                                            fontSize = 28.sp,
                                            lineHeight = 36.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            SpaceHorizontalCompose(width = 20.dp)
                            Box(
                                modifier = Modifier
                                    .size(128.dp)
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .background(Color_Purple_7F4_20)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.olmo_ic_sand_white),
                                        contentDescription = "profile"
                                    )
                                    var durationTime by remember {
                                        mutableStateOf(0L)
                                    }
                                    LaunchedEffect(true) {
                                        val createdTime = sharedPrefs.getTimeLiveCreated()
                                        val currentTime = System.currentTimeMillis()
                                        val diff = abs(currentTime - createdTime)
                                        durationTime = diff
                                    }
                                    val default = "00:10:10"
                                    var timeFormat = default
                                    if (durationTime != 0L) {
                                        timeFormat =
                                            DateTimeHelper.hmsTimeFormatter(durationTime) ?: default
                                    }
                                    Text(
                                        text = timeFormat,
                                        style = MaterialTheme.typography.subtitle2.copy(
                                            color = Color.White,
                                            fontSize = 27.sp,
                                            lineHeight = 36.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        SpaceCompose(height = 32.dp)
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .padding(start = 18.dp, end = 18.dp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            SecondLiveButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 6.dp),
                                stringResource(R.string.action_discard_video),
                                onClickFunc = {
                                    viewModel.setLoading(true)
                                    isBackHome = true
                                    viewModel.discardVideo()
                                },
                                backgroundColor = White
                            )
                            PrimaryLiveButton(
                                enable = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 6.dp),
                                text = stringResource(R.string.action_save_video),
                                onClickFunc = {
                                    viewModel.setLoading(true)
                                    viewModel.saveVideo()
                                    isBackHome = true
                                }
                            )
                        }
                    }

                }, modifier = Modifier
                    .constrainAs(content) { top.linkTo(parent.top) }
                    .padding(top = 50.dp))
        }
    }

    LoaderWithAnimation(isPlaying = isLoading.value)

    if (isBackHome) {
        LaunchedEffect(true) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(
                    Constants.BUNDLE_DATA,
                    Constants.BUNDLE_DATA_CLOSE_LIVE_STREAM
                )
            navController.popBackStack()
        }
    }
}