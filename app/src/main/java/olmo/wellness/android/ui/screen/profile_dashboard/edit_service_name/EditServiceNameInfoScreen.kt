package olmo.wellness.android.ui.screen.profile_dashboard.edit_service_name

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.InputValueProfileCompose
import olmo.wellness.android.ui.screen.profile_dashboard.items.InvalidateCompose
import olmo.wellness.android.ui.screen.profile_dashboard.items.ValidateSuccessCompose
import olmo.wellness.android.ui.theme.*

/* Service Name */
@Composable
fun EditServiceNameInfoScreen(title: Int?, content: String = "",
                       onSuccess: (Boolean) -> Unit,
                       onFailed: (Boolean) -> Unit,
                       viewModel: EditServiceNameViewModel = hiltViewModel()) {

    var textChange by remember {
        mutableStateOf("")
    }
    val maxHeight = 300.dp
    val isSuccess = viewModel.isSuccess.collectAsState()
    val profileModel = viewModel.profileModel.collectAsState()
    val isError = viewModel.isErrorValidate.collectAsState()
    if(isSuccess.value){
        onSuccess.invoke(true)
        viewModel.resetState()
    }
    Column(
        modifier = Modifier
            .background(Color_gray_FF7)
            .requiredHeight(maxHeight),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Top
        ) {
            if (title != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color_gray_FF7)
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

            val focusManager = LocalFocusManager.current
            InputValueProfileCompose(
                contentDefault = profileModel.value.serviceName ?: "",
                focusManager = focusManager
            ) { contentChange ->
                textChange = contentChange ?: ""
                viewModel.validateServiceName(textChange)
            }

            Column(
                modifier = Modifier
                    .background(Color_gray_FF7)
                    .padding(top = marginDouble)
                    .wrapContentHeight()
            ) {
                if (!isError.value && textChange.isNotEmpty()) {
                    ValidateSuccessCompose(horizontalDp = marginDouble, verticalDp = marginStandard)
                } else {
                    InvalidateCompose(
                        contentNormal = stringResource(id = R.string.des_profile_service_name),
                        contentError = stringResource(id = R.string.des_profile_service_name),
                        iconDefault = R.drawable.ic_error_verify_pw,
                        paddingStart = marginDouble,
                        paddingEnd = marginDouble,
                        isError = isError.value && textChange.isNotEmpty()
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom){
            GroupButtonBottomCompose(confirmCallback = {
                if(textChange.isNotEmpty()){
                    viewModel.updateServiceName(textChange)
                }
            }, cancelCallback = {
                onFailed.invoke(true)
            }, enable = !isError.value)
        }
    }
}