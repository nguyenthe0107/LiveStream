package olmo.wellness.android.ui.screen.account_setting.chat_setting.message_shortcut.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.components.BaseEditTextInputProfile
import olmo.wellness.android.ui.screen.account_setting.chat_setting.ChatSettingViewModel
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailMessageShortcutScreen(
    navController: NavController,
    viewModel: ChatSettingViewModel = hiltViewModel()
){
        var textChange by remember {
            mutableStateOf("")
        }

        Scaffold(topBar = {
            DetailTopBar(
                title = stringResource(id = R.string.title_auto_reply),
                navController = navController
            )
        }) {
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
                        hint = "", contentDefault = "", onTextChanged = {
                        textChange = it
                    }, isMiniHeight = false)
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 5.dp, start = marginDouble, end = marginDouble,)) {
                    Text(text = "You can put greetings, shop pollicies, promotions or any other information you wish to let customer know in the message, limited to 500 characters.",
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Neutral_Gray_7))
                }
            }
        }
}

