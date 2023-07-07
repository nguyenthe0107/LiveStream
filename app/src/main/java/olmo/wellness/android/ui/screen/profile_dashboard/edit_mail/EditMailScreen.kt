package olmo.wellness.android.ui.screen.profile_dashboard.edit_mail

import android.annotation.SuppressLint
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.InputValueProfileCompose
import olmo.wellness.android.ui.screen.profile_dashboard.items.InvalidateCompose
import olmo.wellness.android.ui.screen.profile_dashboard.items.ValidateSuccessCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditMailScreen(
    modalBottomSheetState: ModalBottomSheetState,
    title: Int?,
    onSuccess: (Boolean, String) -> Unit,
    onFailed: (Boolean) -> Unit,
    viewModel: EditMailViewModel = hiltViewModel()){

    val focusManager = LocalFocusManager.current
    val profileModel = viewModel.profileModel.collectAsState()
    val maxHeight = 300.dp
    val isSuccess = viewModel.isSuccess.collectAsState()
    var textChange by remember {
        mutableStateOf(profileModel.value.email ?:"")
    }
    val scope = rememberCoroutineScope()
    val isError = viewModel.isErrorValidate.collectAsState()
    val errorMes = viewModel.error.collectAsState()
    if (isSuccess.value) {
        LaunchedEffect(key1 = true){
            onSuccess.invoke(true, textChange)
            viewModel.resetState()
            scope.launch {
                modalBottomSheetState.hide()
            }
        }
    }
    val context = LocalContext.current
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                (context as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
                (context as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
            Lifecycle.Event.ON_DESTROY -> {
            }
            else -> {
            }
        }
    }
    Column(
        modifier = Modifier
            .background(Color_gray_FF7)
            .requiredHeight(maxHeight),
        verticalArrangement = Arrangement.SpaceBetween){
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Top){
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
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = Color_Purple_FBC
                        ),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = marginDouble),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            InputValueProfileCompose(contentDefault = profileModel.value.email ?:"", focusManager, contentChange = {
                textChange = it ?: ""
                viewModel.updateContentEmail(it)
            })

            Column(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .background(Color_gray_FF7)
                    .wrapContentHeight()){

                if (!isError.value && textChange.trim().isNotEmpty() && errorMes.value.isEmpty()) {
                    ValidateSuccessCompose(content = stringResource(id = R.string.title_success_email),horizontalDp = marginDouble, verticalDp = marginStandard)
                } else {
                    if(textChange.trim().isEmpty()){
                        InvalidateCompose(
                            isError = isError.value && textChange.isNotEmpty(),
                            contentNormal = stringResource(id = R.string.des_profile_email),
                            contentError = stringResource(id = R.string.error_profile_email),
                            iconDefault = R.drawable.ic_error_verify_pw,
                            paddingStart = marginDouble,
                            paddingEnd = marginDouble
                        )
                    }
                    SpaceCompose(height = 8.dp)
                }
                if(errorMes.value.isNotEmpty()){
                    InvalidateCompose(
                        isError = errorMes.value.isNotEmpty() && textChange.isNotEmpty(),
                        contentError = errorMes.value,
                        iconDefault = R.drawable.ic_error_verify_pw,
                        paddingStart = marginDouble,
                        paddingEnd = marginDouble
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom){
            GroupButtonBottomCompose(confirmCallback = {
                if (textChange.isNotEmpty()) {
                    viewModel.updateEmail(textChange)
                }
            }, cancelCallback = {
                onFailed.invoke(true)
                scope.launch {
                    modalBottomSheetState.hide()
                }
            },
            enable = textChange.trim().isNotEmpty() && !isError.value)
        }
    }
}

