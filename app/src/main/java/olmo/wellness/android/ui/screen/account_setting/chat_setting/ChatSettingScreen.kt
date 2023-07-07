package olmo.wellness.android.ui.screen.account_setting.chat_setting

import android.annotation.SuppressLint
import android.view.WindowManager
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.hideSystemUI
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.ItemSwitch
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.account_setting.chat_setting.dialog.MessageShortcutsBottomSheet
import olmo.wellness.android.ui.screen.account_setting.chat_setting.dialog.SetupAutomaticBottomSheet
import olmo.wellness.android.ui.screen.business_hours.AvatarMascot
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatSettingScreen(
    navController: NavController,
    viewModel: ChatSettingViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    (context as MainActivity).hideSystemUI()

    val isLoading = viewModel.isLoading.collectAsState()
    val defaultPrivateChatMode = viewModel.defaultPrivateChatMode.collectAsState()
    val defaultLiveChatMode = viewModel.defaultLiveChatMode.collectAsState()
    val defaultAutoReplyMode = viewModel.defaultAutoReplyMode.collectAsState()

    val messageShortcutModal =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )

    val setupAutomaticModal = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            false
        }
    )

    var textChange by remember {
        mutableStateOf("")
    }


    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                context.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
                context.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
            Lifecycle.Event.ON_DESTROY -> {
            }
            else -> {

            }
        }
    }


    Scaffold(topBar = {
        ToolbarSchedule(
            title = stringResource(R.string.title_chat_setting),
            backIconDrawable = R.drawable.ic_back_calendar,
            navController = navController,
            backgroundColor = Color_LiveStream_Main_Color
        )
    }, bottomBar = {
        AutomaticReplyMessage(defaultAutoReplyMode, setupAutomaticModal,viewModel)
    }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color_LiveStream_Main_Color)
        ) {
            val (options, imageCompose, endCompose) = createRefs()
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 32.dp,
                            topEnd = 32.dp
                        )
                    )
                    .background(color = Color_gray_FF7)
                    .fillMaxSize()
                    .fillMaxHeight()
                    .constrainAs(options) {
                        start.linkTo(parent.start)
                        top.linkTo(imageCompose.top, 36.dp)
                    }
                    .padding(top = 50.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color_gray_FF7)
                        .padding(
                            vertical = marginStandard
                        )
                ) {

                    Spacer(modifier = Modifier.padding(vertical = 20.dp))

                    ItemSwitch(
                        title = stringResource(R.string.lb_live_chat_during_livestream),
                        switchDefault = defaultLiveChatMode.value ?: false,
                        onSwitch = {
                            viewModel.updateUserSetting(liveChatMode = it)
                        })


                    ItemSwitch(
                        title = stringResource(R.string.lb_private_chat_during_livestream),
                        switchDefault = defaultPrivateChatMode.value ?: false,
                        onSwitch = {
                            viewModel.updateUserSetting(privateChatMode = it)
                        })

                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .noRippleClickable {
                                scope.launch {
                                    messageShortcutModal.show()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = stringResource(id = R.string.title_message_shortcut),
                            style = MaterialTheme.typography.subtitle1.copy(
                                color = Color_Purple_FBC,
                                fontSize = 14.sp
                            ), modifier = Modifier.weight(1f)
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_next),
                            contentDescription = "next"
                        )
                    }


                    Text(
                        text = "Create, edit, and delete shortcuts for frequently used messages in chat. You can create up to 20 message shortcuts.",
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Neutral_Gray_6,
                            fontSize = 12.sp
                        ), modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                    )

                }
            }

            AvatarMascot(modifier = Modifier.constrainAs(imageCompose) {
                top.linkTo(parent.top, 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, uri = null, callbackFun = {
            }, src = R.drawable.ic_chat_setting)

        }
    }


    MessageShortcutsBottomSheet(modalBottomSheetState = messageShortcutModal)

    SetupAutomaticBottomSheet(modalBottomSheetState = setupAutomaticModal, onConfirm = {

    })

//    Scaffold(topBar = {
//        DetailTopBar(
//            title = stringResource(id = R.string.title_select_reason_delete_account),
//            navController = navController
//        )
//    }) {
//        Column(
//            modifier = Modifier
//                .background(Neutral_Gray_2)
//                .padding(top = marginStandard)
//                .fillMaxSize()
//        ) {
//
//            if (defaultLiveChatMode.value != null){
//                SwitchViewEx(
//                    text = "Live Chat During Livestream",
//                    defaultMode = defaultLiveChatMode.value?:false,
//                ) { liveMode ->
//                    viewModel.updateUserSetting(liveChatMode = liveMode)
//                }
//            }
//
//            if(defaultPrivateChatMode.value != null){
//                SwitchViewEx(
//                    text = "Private Chat During Livestream",
//                    defaultMode = defaultPrivateChatMode.value?:false,
//                ) { privateMode ->
//                    viewModel.updateUserSetting(privateChatMode = privateMode)
//                }
//            }
//
//            SpaceCompose(height = marginDouble)
//            Column(modifier = Modifier.background(color = White)) {
//                if (defaultAutoReplyMode.value != null){
//                    SwitchViewEx(
//                        text = "Send Auto-Reply in chat",
//                        defaultMode = defaultAutoReplyMode.value?:false,
//                    ) { autoReply ->
//                        viewModel.updateUserSetting(autoReplyMode = autoReply)
//                    }
//                }
//
//                DividerView(
//                    paddingStart = marginDouble,
//                    paddingEnd = marginDouble
//                )
//
//                Row(
//                    modifier = Modifier
//                        .padding(start = marginDouble, end = marginDouble, bottom = 10.dp)
//                        .fillMaxWidth()
//                        .wrapContentHeight(),
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    Button(
//                        onClick = {
//                           navController.navigate(ScreenName.AutoReplyScreen.route)
//                        },
//                        modifier = Modifier
//                            .height(36.dp)
//                            .padding(end = marginStandard),
//                        shape = RoundedCornerShape(20.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            backgroundColor = Neutral_Gray_3
//                        ),
//                        border = BorderStroke(defaultBorderWidth, Color.Transparent)
//                    ) {
//                        Text(
//                            text = "Hello! How can I help you?",
//                            style = MaterialTheme.typography.subtitle2.copy(
//                                color = Neutral_Gray_9,
//                                fontWeight = FontWeight.Normal,
//                                fontFamily = MontserratFont
//                            )
//                        )
//                    }
//                }
//            }
//
//            Row(
//                modifier = Modifier
//                    .padding(
//                        start = marginDouble,
//                        end = marginDouble,
//                        bottom = padding_14,
//                        top = padding_14
//                    )
//                    .background(color = Neutral_Gray_2)
//            ) {
//                Text(
//                    text = "Enable to send self-defined reply message to buyers when they chat with you",
//                    style = MaterialTheme.typography.subtitle2.copy(
//                        color = Neutral_Gray_6,
//                        fontFamily = MontserratFont,
//                        fontWeight = FontWeight.Normal
//                    )
//                )
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(color = White)
//                    .padding(
//                        start = marginDouble,
//                        end = marginDouble,
//                        top = marginStandard,
//                        bottom = marginStandard
//                    )
//                    .alpha(0.5f)
//                    .noRippleClickable {
//                        navController.navigate(ScreenName.MessageShortcutScreen.route)
//                    }
//                    .height(defaultHeightButton),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically) {
//
//                Text(
//                    text = "Message Shortcuts",
//                    style = MaterialTheme.typography.body2.copy(
//                        color = Black_037
//                    ),
//                    overflow = TextOverflow.Ellipsis,
//                )
//                Image(
//                    painter = painterResource(R.drawable.ic_baseline_arrow_right_24),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(defaultSizeImage)
//                )
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(
//                        start = marginDouble,
//                        end = marginDouble,
//                        top = marginStandard,
//                        bottom = marginStandard
//                    )){
//                Text(
//                    text = "Create, edit, and delete shortcuts for frequently used messages in chat",
//                    style = MaterialTheme.typography.body2,
//                    overflow = TextOverflow.Ellipsis,
//                    color = Neutral_Gray_6
//                )
//            }
//        }
//    }
    LoadingScreen(isLoading = isLoading.value)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AutomaticReplyMessage(
    defaultAutoReplyMode: State<Boolean?>,
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: ChatSettingViewModel
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(width = 1.dp, color = Color_Purple_FBC, shape = RoundedCornerShape(20.dp))
    ) {
        ItemSwitch(
            title = "Automatically Reply in Chat",
            switchDefault = defaultAutoReplyMode.value ?: false,
            onSwitch = {
                if (it) {
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                }
                viewModel.updateUserSetting(autoReplyMode = it)

            })

        Text(
            text = "Allow Kepler to send pre-defined reply message to buyers when you are not available online.",
            style = MaterialTheme.typography.subtitle1.copy(
                color = Neutral_Gray_6,
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 14.dp)
        )
    }
}

