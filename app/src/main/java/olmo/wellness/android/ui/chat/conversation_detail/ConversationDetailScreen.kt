package olmo.wellness.android.ui.chat.conversation_detail

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.extension.getUserChat
import olmo.wellness.android.ui.chat.private_chat.viewmodel.PrivateChatViewModel
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.livestream.chatlivestream.cell.UserInput
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.capture_screen.CaptureScreen
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Transparent
import olmo.wellness.android.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationDetailScreen(
    navController: NavHostController,
    userId: String?,
    chatPrivateViewModel: PrivateChatViewModel = hiltViewModel(),
) {


    val uiStateChatPrivate = chatPrivateViewModel.uiState.collectAsState()
    val activity = LocalContext.current as MainActivity

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                chatPrivateViewModel.getRoomChatSingle(userId = userId?.toInt())
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
            }
            Lifecycle.Event.ON_DESTROY -> {
                activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                chatPrivateViewModel.clearState()
            }
        }
    }

    ConversationDetailHome(listMessage = uiStateChatPrivate.value.roomStream?.messages,
        chatPrivateViewModel = chatPrivateViewModel,
        user = getUserChat(uiStateChatPrivate.value.roomStream?.userMap),
        navController = navController,
        roomId = uiStateChatPrivate.value.roomStream?.getRoomId())
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ConversationDetailHome(
    navController: NavHostController,
    user: User?,
    roomId: String?,
    chatPrivateViewModel: PrivateChatViewModel,
    listMessage: List<ChatMessage?>?,
) {

    var isCaptureDisplay by remember { mutableStateOf(false) }

    val imgTakePhoto = remember {
        mutableStateOf<Uri?>(null)
    }


    androidx.compose.material.Scaffold(
        topBar = {
            ToolbarSchedule(
                title = getNameUserChat(user),
                backIconDrawable = R.drawable.ic_back_calendar,
                navController = navController,
                backgroundColor = Transparent
            )
        }, bottomBar = {
            UserInput(onMessageSent = {
                chatPrivateViewModel.sendTextMessagePC(it, null)
            }, roomId = roomId, onImageSent = {
                chatPrivateViewModel.sendImageMessagePC(it, null)
            }, isPrivateChat = true, isOpenCaptureImage = {
                isCaptureDisplay = true
            }, imgTakePhoto = imgTakePhoto)
        }, modifier = Modifier.fillMaxSize(),
        backgroundColor = Color_LiveStream_Main_Color
    ) {
        Column(modifier = Modifier
            .padding(top = 20.dp, bottom = 50.dp)
            .fillMaxHeight()
            .clip(
                RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp
                )
            )
            .background(color = White)
            .padding(12.dp), content = {
//            SearchConversation(onTextChange = {
//
//            })
            ConversationContent(listMessage = listMessage, onLoadMore = {
                chatPrivateViewModel.loadMorePC()
            })
        })
    }


    CaptureScreen(isDisplay = isCaptureDisplay, callbackUri = { uriSelected ->
        uriSelected?.let {
            imgTakePhoto.value = it
        }
        isCaptureDisplay = false
    })


}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ConversationContent(listMessage: List<ChatMessage?>?, onLoadMore: () -> Unit) {

    MessageItem(
        messages = listMessage,
        modifier = Modifier.fillMaxWidth(),
        onLoadMore = onLoadMore
    )
}