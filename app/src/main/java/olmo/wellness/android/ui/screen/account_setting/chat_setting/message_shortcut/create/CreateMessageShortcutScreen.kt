package olmo.wellness.android.ui.screen.account_setting.chat_setting.message_shortcut.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.user_message.UserMessageShortcut
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.components.BaseEditTextInputProfile
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun CreateMessageShortcutScreen(
    navController: NavController,
    messageDefault: UserMessageShortcut? = null,
    viewModel: CreateMessageShortcutViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = "initial"){
        viewModel.setDefaultMessage(messageDefault)
    }

    val uiState = viewModel.uiState.collectAsState().value

    if (uiState.savedSuccess){
        //DO ON SUCCESS
        //viewModel.clearState()
    }

    Scaffold(
        topBar = {
            DetailTopBar(
                title = stringResource(id = R.string.title_create_message_shortcut),
                navController = navController,
                actions = {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = if (uiState.isValid == true) Color_Green_Main else Gray_FE3
                        ),
                        modifier = Modifier
                            .padding(
                                end = 16.dp
                            )
                            .clickable {
                                if (uiState.isValid == true) {
                                    viewModel.saveMessageShortcut(uiState.message?:"")
                                }
                            },
                    )
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .background(Neutral_Gray_2)
                .padding(top = marginStandard)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
            ){
                SpaceCompose(height = 10.dp)
                BaseEditTextInputProfile(
                    hint = "",
                    contentDefault = messageDefault?.messageShortcut?:"",
                    onTextChanged = {
                        viewModel.validateMessage(it)
                    },
                    isMiniHeight = false
                )
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp, start = marginDouble, end = marginDouble,)) {
                Text(text = "You can put greetings, shop pollicies, promotions or any other information you wish to let customer know in the message, limited to 500 characters.",
                    style = MaterialTheme.typography.overline.copy(
                        color = Neutral_Gray_7
                    )
                )
            }
        }
    }
    LoadingScreen(isLoading = uiState.showLoading)
}