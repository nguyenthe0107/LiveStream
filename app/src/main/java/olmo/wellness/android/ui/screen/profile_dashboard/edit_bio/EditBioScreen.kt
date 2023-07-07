package olmo.wellness.android.ui.screen.profile_dashboard.edit_bio

import android.annotation.SuppressLint
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.AreaInputValueProfileCompose
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.profile_dashboard.items.InvalidateCompose
import olmo.wellness.android.ui.screen.profile_dashboard.items.ValidateSuccessCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditBioScreen(
    modalBottomSheetState: ModalBottomSheetState,
    onSuccess: (Boolean) -> Unit,
    onFailed: (Boolean) -> Unit,
    viewModel: EditBioViewModel = hiltViewModel()){

    val uiState = viewModel.uiState.collectAsState().value
    val scope = rememberCoroutineScope()
    LaunchedEffect("Init"){
        viewModel.getProfile()
    }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    context as MainActivity
    val isSuccess = uiState.isUpdateBioSuccess
    if (isSuccess){
        scope.launch {
            modalBottomSheetState.hide()
        }
        onSuccess.invoke(true)
        viewModel.resetState()
    }
    val isValid = uiState.isValid
    val bioContent = viewModel.bioContent.collectAsState()
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
    Column(
        modifier = Modifier
            .background(Color_gray_FF7)
            .requiredHeight(350.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween){
        Column(
            modifier = Modifier
                .padding(top = marginStandard),
            verticalArrangement = Arrangement.Top
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color_gray_FF7)
                    .padding(marginDouble),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.title_my_profile_bio),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 16.sp,
                        color = Color_Purple_FBC
                    ),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = marginDouble),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            var bioInput by remember {
                mutableStateOf(bioContent.value)
            }
            AreaInputValueProfileCompose(
                contentDefault = bioContent.value,
                focusManager
            ,contentChange = { text ->
                    bioInput = text?:"".trim()
                    viewModel.validateMessage(bioInput)
            })
            Column(
                modifier = Modifier
                    .background(Color_gray_FF7)
                    .padding(top = marginDouble)
                    .wrapContentHeight()){
                if (isValid == true && uiState.message?.isNotEmpty() == true) {
                    ValidateSuccessCompose(content = stringResource(id = R.string.title_success_bio),horizontalDp = marginDouble, verticalDp = marginStandard)
                } else {
                    InvalidateCompose(
                        contentNormal = stringResource(id = R.string.des_profile_service_bio),
                        contentError = stringResource(id = R.string.err_profile_service_bio),
                        iconDefault = R.drawable.ic_error_verify_pw,
                        paddingStart = marginDouble,
                        paddingEnd = marginDouble,
                        isError = isValid == false && uiState.message?.isNotEmpty() == true
                    )
                }
            }
        }

        GroupButtonBottomCompose(confirmCallback = {
            viewModel.updateBio()
        }, cancelCallback = {
            scope.launch {
                modalBottomSheetState.hide()
            }
            onFailed.invoke(true)
        }, enable = isValid ?: false)
    }
}