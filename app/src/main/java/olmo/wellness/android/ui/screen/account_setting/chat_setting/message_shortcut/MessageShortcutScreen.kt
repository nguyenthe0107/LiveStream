package olmo.wellness.android.ui.screen.account_setting.chat_setting.message_shortcut

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import olmo.wellness.android.R
import olmo.wellness.android.core.toJson
import olmo.wellness.android.domain.model.user_message.UserMessageShortcut
import olmo.wellness.android.extension.WTF
import olmo.wellness.android.ui.common.ChipText
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.SwitchViewEx
import olmo.wellness.android.ui.common.components.PrimaryButton
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MessageShortcutScreen(
    navController: NavController,
    viewModel: MessageShortcutViewModel = hiltViewModel()
) {
    val isSuccess = viewModel.isSuccess.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val defaultValue = viewModel.defaultShowShortcutMode.collectAsState()
    val listShortcuts = viewModel.listShortcuts.collectAsState()
    var textChange by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            DetailTopBar(
                title = stringResource(id = R.string.title_message_shortcut),
                navController = navController,
                backgroundColor = White
            )
        },
        backgroundColor = Gray_8FB
    ) {

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (header, newMsgButton, listMsg) = createRefs()

            MessageShortcutHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(header) {
                        top.linkTo(parent.top)
                    },
                defaultValue.value,
                viewModel
            )
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 8.dp
                    )
                    .constrainAs(listMsg) {
                        top.linkTo(header.bottom)
                        bottom.linkTo(newMsgButton.top)
                        height = Dimension.fillToConstraints
                    }
            ){
                if (listShortcuts.value != null){
                    ListMessageShortcuts(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        navController = navController,
                        listShortcuts.value?: listOf()
                    )
                }
            }
            ButtonNewMessage(
                Modifier
                    .constrainAs(newMsgButton) {
                        bottom.linkTo(parent.bottom)
                    },
                navController
            )
        }
    }
    LoadingScreen(isLoading = isLoading.value)
}

@Composable
fun ButtonNewMessage(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                White
            )
            .padding(top = marginStandard)
    ){
        PrimaryButton(
            text = "New Message Shortcut",
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
                .padding(
                    bottom = 24.dp
                )
                .clickable {
                    openCreateShortcutsMessageScreen(navController = navController)
                }
        )
    }
}

@Composable
fun ListMessageShortcuts(
    modifier: Modifier = Modifier,
    navController: NavController,
    listMessageShortcut: List<UserMessageShortcut>
) {
    FlowRow(
        modifier = modifier
    ) {
        listMessageShortcut.forEach {
            ItemShortcut(it){ messageShortcut ->
                openCreateShortcutsMessageScreen(
                    messageShortcut,
                    navController
                )
            }
        }
    }
}

@Composable
private fun ItemShortcut(
    itemData: UserMessageShortcut,
    onItemClick: (UserMessageShortcut) -> Unit
){
    ChipText(
        text = itemData.messageShortcut?:"",
        modifier = Modifier
            .padding(
                top = 8.dp,
                end = 8.dp
            )
    ){
        onItemClick.invoke(itemData)
    }
}

private fun openCreateShortcutsMessageScreen(
    msg: UserMessageShortcut? = null,
    navController: NavController
){
    navController.navigate(
        ScreenName.CreateMessageShortcutScreen.route
            .plus("?messageShortcut=${msg?.copy(
                id = 999 //For testing, remove when integrating api
            )?.toJson()}")
    )
}

@Composable
fun MessageShortcutHeader(
    modifier: Modifier = Modifier,
    defaultValue: Boolean? = null,
    viewModel: MessageShortcutViewModel
) {
    Column(
        modifier = modifier
            .padding(top = marginStandard),
        verticalArrangement = Arrangement.Top
    ) {
        Column(modifier = Modifier.background(color = White)) {
            if (defaultValue != null) {
                SwitchViewEx(
                    text = "Allow Kepler to access contact list",
                    defaultMode = defaultValue
                ) { isShow ->
                    viewModel.updateUserSetting(isShow)
                }
            }
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(GRAY_8F8)
        ){
            Text(
                text = "Turn on to show the message shortcuts on chat screens. You can create up to 20 mesage shortcuts.",
                style = MaterialTheme.typography.subtitle2.copy(
                    color = Neutral_Gray_6,
                    fontWeight = FontWeight.Normal,
                    fontFamily = MontserratFont
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
            )
        }
    }
}

