package olmo.wellness.android.ui.screen.profile_dashboard.edit_single_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.components.BaseEditTextInputProfile
import olmo.wellness.android.ui.common.components.SectionButtonDialog
import olmo.wellness.android.ui.screen.profile_dashboard.MyProfileDashBoardViewModel
import olmo.wellness.android.ui.screen.profile_dashboard.items.InvalidateCompose
import olmo.wellness.android.ui.screen.profile_dashboard.items.ValidateSuccessCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun EditBasicInfoScreen(title: Int?, content: String = "",
                       onSuccess: (Boolean) -> Unit,
                       onFailed: (Boolean) -> Unit,
                       viewModel: MyProfileDashBoardViewModel = hiltViewModel()) {

    var textChange by remember {
        mutableStateOf("")
    }

    var isValidate by remember {
        mutableStateOf(false)
    }
    val maxHeight = 300.dp
    val isSuccess = viewModel.isSuccess.collectAsState()
    if(isSuccess.value){
        onSuccess.invoke(true)
        viewModel.resetState()
    }
    Column(
        modifier = Modifier
            .background(Neutral_Gray_2)
            .requiredHeight(maxHeight),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Top
        ) {
            if (title != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White)
                        .padding(marginDouble),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(title),
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        color = Neutral_Gray_9,
                        modifier = Modifier.padding(horizontal = marginDouble),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Neutral_Gray_2)
                    .height(marginStandard)
            )

            BaseEditTextInputProfile(hint = "", contentDefault = content, onTextChanged = {
                textChange = it
            })

            Column(modifier = Modifier
                .background(Neutral_Gray_2)
                .wrapContentHeight()) {
                when(title){
                    R.string.title_my_profile_username -> {
                        if (isValidate) {
                            ValidateSuccessCompose(horizontalDp = marginDouble, verticalDp = marginStandard)
                        } else {
                            InvalidateCompose(contentError = stringResource(id = R.string.error_profile_username),
                                iconDefault = R.drawable.ic_error_verify_pw, paddingStart = marginDouble,paddingEnd = marginDouble)
                        }
                    }
                    R.string.title_my_profile_email -> {
                        if (isValidate) {
                            ValidateSuccessCompose(horizontalDp = marginDouble, verticalDp = marginStandard)
                        } else {
                            InvalidateCompose(contentError = stringResource(id = R.string.error_profile_email),
                                iconDefault = R.drawable.ic_error_verify_pw, paddingStart = marginDouble,paddingEnd = marginDouble)
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom){
            SectionButtonDialog(
                titleSuccess = stringResource(id = R.string.save_value),
                onSubmitClick = {
                    if(textChange.isNotEmpty()){
                    }
                },
                onCancelClick = {
                })
        }
    }
}