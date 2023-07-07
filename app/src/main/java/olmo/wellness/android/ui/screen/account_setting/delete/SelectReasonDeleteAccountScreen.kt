package olmo.wellness.android.ui.screen.account_setting.delete

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.components.BaseEditTextInputProfile
import olmo.wellness.android.ui.screen.profile_dashboard.MyProfileDashBoardViewModel
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.ButtonNextStep
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.GroupRadioButtonSelected
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SelectReasonDeleteAccountScreen(
    navController: NavController,
    viewModel: MyProfileDashBoardViewModel = hiltViewModel()){

    var textChange by remember {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        DetailTopBar(
            title = stringResource(id = R.string.title_select_reason_delete_account),
            navController = navController
        )
    }) {
        Column(
            modifier = Modifier
                .background(Neutral_Gray_2)
                .padding(top = marginStandard)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Top
            ) {

                val reasons = listOf(
                    stringResource(id = R.string.content_group_select_reason_delete_account_1),
                    stringResource(id = R.string.content_group_select_reason_delete_account_2),
                    stringResource(id = R.string.content_group_select_reason_delete_account_3),
                    stringResource(id = R.string.content_group_select_reason_delete_account_4),
                    stringResource(id = R.string.content_group_select_reason_delete_account_5)
                )

                GroupRadioButtonSelected(
                    modifier = Modifier,
                    titleText = stringResource(id = R.string.title_group_select_reason_delete_account),
                    selectionList = reasons,
                    swapPosition = false,
                    hideTitle = false,
                    colorBackground = Color.White,
                    showDivider = true
                ) {
                    //
                }
                SpaceCompose(height = 10.dp)
                BaseEditTextInputProfile(
                    modifier = Modifier.fillMaxWidth(),
                    hint = "Write Something...",
                    isShowTitle = true,
                    contentDefault = "",
                    onTextChanged = {
                        textChange = it
                    },
                    isMiniHeight = false,
                    title = "Comment",
                    colorBackground = Color.White
                )
                SpaceCompose(height = 10.dp)
                Row(modifier = Modifier.fillMaxWidth()
                    .background(color = Color.White).padding(start = marginDouble,
                    end = marginDouble, top = marginStandard, bottom = marginStandard)
                    .defaultMinSize(minHeight = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                    ) {
                    Text(text = "Email", style = MaterialTheme.typography.subtitle2.copy(
                        color = Neutral_Gray_9,
                        fontFamily = MontserratFont
                    ))
                    Text(text = "abc@gmail.com", style = MaterialTheme.typography.subtitle2.copy(
                        color = Neutral_Gray_5,
                        fontFamily = MontserratFont
                    ))
                }
            }

            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                ButtonNextStep(buttonText = stringResource(id = R.string.submit)) {

                }
            }
        }
    }
}