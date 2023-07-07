package olmo.wellness.android.ui.livestream.schedule.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import olmo.wellness.android.R
import olmo.wellness.android.core.utils.shareSocialMedia
import olmo.wellness.android.core.utils.shareVideoTikTok
import olmo.wellness.android.core.utils.shareWithEmail
import olmo.wellness.android.domain.model.SocialNetwork
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.domain.model.wrapper.LiveStreamInfoWrapper
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.livestream.schedule.component.CreateLiveSchedulingButton
import olmo.wellness.android.ui.livestream.schedule.component.EmptyLiveScheduling
import olmo.wellness.android.ui.livestream.schedule.component.SwipeCompose
import olmo.wellness.android.ui.livestream.schedule.component.UpcomingItemCompose
import olmo.wellness.android.ui.livestream.schedule.dialog.LiveAlertDialog
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.calendar_screen.CalendarViewModel
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.share.ShareBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.share.ShareScreenType
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Color_gray_FF7
import olmo.wellness.android.ui.theme.Transparent
import olmo.wellness.android.ui.theme.White

@SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LiveSchedulingScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val showDialogDelete = remember {
        mutableStateOf<LiveStreamInfoWrapper?>(null)
    }

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                viewModel.fetchListScheduler()
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
            }
            Lifecycle.Event.ON_DESTROY -> {
            }
            else -> {
            }
        }
    }
    val isLoading = viewModel.isLoading.collectAsState()
    LoaderWithAnimation(isPlaying = isLoading.value)
    val listSchedules = viewModel.listSchedulerCalendar.collectAsState()
    val linkShare by remember {
        mutableStateOf("https://itviec.com/nha-tuyen-dung/olmo-technology")
    }
    Scaffold(
        topBar = {
            ToolbarSchedule(
                title = stringResource(id = R.string.lb_live_scheduling),
                backIconDrawable = R.drawable.ic_back_calendar,
                navController = navController,
                backgroundColor = Color_LiveStream_Main_Color
            )
        }, bottomBar = {
            CreateLiveSchedulingButton(
                Modifier
                    .wrapContentSize()
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = {
                    navController.navigate(ScreenName.CreateScheduleLiveStreamScreen.route)
                }
            )
        }, modifier = Modifier.fillMaxSize(),
        backgroundColor = Color_LiveStream_Main_Color
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = 20.dp
                )
                .fillMaxSize()
                .background(
                    color = Color_gray_FF7,
                    shape = RoundedCornerShape(
                        topEnd = 30.dp,
                        topStart = 30.dp
                    )
                )
                .clip(
                    shape = RoundedCornerShape(
                        topEnd = 30.dp,
                        topStart = 30.dp
                    )
                )
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 70.dp
                )
        ) {
            if (listSchedules.value.isEmpty()) {
                EmptyLiveScheduling(
                    Modifier
                        .wrapContentSize()
                )
            } else {
                val stateLazy = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier
                        .background(Color_gray_FF7),
                    content = {
                        item {
                            Text(
                                text = stringResource(id = R.string.title_upcoming),
                                style = MaterialTheme.typography.subtitle2.copy(
                                    color = Color_LiveStream_Main_Color,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp
                                )
                            )
                        }

                        items(count = listSchedules.value.size, key = {
                            it
                        }) { index ->
                            val info = listSchedules.value[index]
                            SwipeCompose(color = Transparent,
                                isShowDeleteItemDefault = true,
                                isShowOptionDefault = false,
                                disableSwipeLeftToRight = true,
                                content = {
                                    UpcomingItemCompose(info)
                                }, callbackEditItemUpcoming = {
                                    navController.navigate(ScreenName.CreateScheduleLiveStreamScreen.route)
                                }, callbackDeleteItemUpcoming = {
                                    showDialogDelete.value = LiveStreamInfoWrapper(true, info)
                                }, callbackSharingItemUpcoming = {
                                    val listUser = viewModel.getListUser()
                                    context.let {
                                        (context as MainActivity).showAsBottomSheet {
                                            ShareBottomSheet(
                                                type = ShareScreenType.LIVE_SCHEDULING,
                                                userList = listUser,
                                                showMore = {},
                                                onCopyLinkRequest = {},
                                                onSocialNetworkShareRequest = { socialNetwork ->
                                                    when (socialNetwork) {
                                                        SocialNetwork.EMAIL,
                                                        SocialNetwork.EMAIL_SCHEDULING -> {
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
                                                onUserShareRequest = {}
                                            )
                                        }
                                    }
                                })
                        }
                    }, state = stateLazy)
            }
            LoaderWithAnimation(isPlaying = isLoading.value)
        }
        showDialogDelete.value?.let {
            LiveAlertDialog(
                it.isSelected ?: false,
                "Delete Event?",
                "Are you sure you want to delete event?",
                negativeText = "Cancel",
                positiveText = "Continue",
                onNegativeClick = {
                    showDialogDelete.value = it.copy(isSelected = false)

                },
                onPositiveClick = {
                    if (it.livestreamInfo.id != null) {
                        viewModel.deleteLivestream(it.livestreamInfo)
                    }
                    showDialogDelete.value = it.copy(isSelected = false)
                },
                onClickOutSide = {
                    showDialogDelete.value = it.copy(isSelected = false)
                }
            )
        }
    }
}

