package olmo.wellness.android.ui.chat.conversation_list.view

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.extension.getUserChat
import olmo.wellness.android.ui.chat.conversation_list.cell.MessageListItem
import olmo.wellness.android.ui.chat.conversation_list.cell.SearchConversation
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.notification.OnBottomReached
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Transparent
import olmo.wellness.android.ui.theme.White

@Composable
fun ConversationListContentScreen(
    navController: NavHostController,
    viewModel: ConversationListModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    val pageCurrent = remember {
        mutableStateOf(1)
    }

    val search = remember {
        mutableStateOf<String?>(null)
    }

    val listState = rememberLazyListState()

//    val isStart = remember {
//        mutableStateOf(true)
//    }

    LaunchedEffect(Unit) {
        if (uiState.value.listRoomConversation == null || uiState.value.listRoomConversation?.isEmpty() == true) {
            viewModel.getListRoom(page = pageCurrent.value, search = search.value)
        }
    }

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
            }
            Lifecycle.Event.ON_DESTROY -> {
//                viewModel.clearState()
            }
        }
    }

    ConversationListContentHome(
        navController = navController,
        listData = uiState.value.listRoomConversation,
        isLoading = uiState.value.showLoading,
        listState = listState,
        search = {
            search.value = it
            pageCurrent.value = 1
            viewModel.getListRoom(page = pageCurrent.value, search = search.value)
        }
    )


    listState.OnBottomReached(buffer = Constants.PAGE_SIZE, onLoadMore = {
        pageCurrent.value = pageCurrent.value + 1
        viewModel.getListRoom(page = pageCurrent.value, search = search.value)
    })
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ConversationListContentHome(
    navController: NavHostController,
    listData: List<DetailRoom>?,
    search: (String) -> Unit,
    isLoading: Boolean,
    listState: LazyListState
) {

    Scaffold(
        topBar = {
            navController.let {
                ToolbarSchedule(
                    title = stringResource(R.string.lb_messages),
                    backIconDrawable = R.drawable.ic_back_calendar,
                    navController = it,
                    backgroundColor = Transparent
                )
            }
        }, modifier = Modifier.fillMaxSize(),
        backgroundColor = Color_LiveStream_Main_Color
    ) {
        Column(modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxSize()
            .clip(
                RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp
                )
            )
            .background(color = White)
            .padding(12.dp), content = {
            SearchConversation(
                hint = stringResource(R.string.lb_search_message),
                onTextChange = {
                    search.invoke(it)
                })

            LoaderWithAnimation(isPlaying = isLoading)

            ConversationListContent(listData = listData, navController, listState = listState)
        })
    }
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(
    ExperimentalAnimationApi::class,
    androidx.compose.foundation.ExperimentalFoundationApi::class
)
@Composable
fun ConversationListContent(
    listData: List<DetailRoom>?,
    navController: NavHostController,
    listState: LazyListState,
) {

    Box {
        if (listData?.isEmpty() == true) {
            EmptyData()
        }
        LazyColumn(state = listState) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            listData?.let { list ->
                items(list.size, key = {
                    it.toString()
                }) { index ->
                    val item = list[index]
                    Box(modifier = Modifier.background(color = Color.Transparent)) {
                        MessageListItem(item) {
                            // navController.navigate(ScreenName.ConversationDetailScreen.route)
                            navController.navigate(
                                ScreenName.ConversationDetailScreen.route.plus(
                                    "?${Constants.KEY_ID}=${
                                        getUserChat(
                                            it.users
                                        )?.id
                                    }"
                                )
                            )
                        }
                    }
                }
            }

        }
    }
}

