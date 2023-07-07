package olmo.wellness.android.ui.screen.create_password

import android.annotation.SuppressLint
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.ui.common.components.BasePasswordInputNewStyle
import olmo.wellness.android.ui.common.components.DescriptionPasswordCompose
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreatePasswordScreen(
    navController: NavController,
    identity: String,
    code: String,
    isPhone: Boolean,
    viewModel: CreatePasswordViewModel = hiltViewModel()
) {
    val isLoading = viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    var password by remember {
        mutableStateOf("")
    }
    val codeOTP by remember {
        mutableStateOf(code ?: "")
    }
    val isPhoneNumber by remember {
        mutableStateOf(isPhone)
    }
    var isValidAllFiledCallBack by remember {
        mutableStateOf(false)
    }
    val isEnable = password.isNotEmpty()
    val coroutineScope = rememberCoroutineScope()

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                (context as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
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
    Scaffold(bottomBar = {
        Column(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            PrimaryLiveButton(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp),
                text = stringResource(id = R.string.button_action_explore), onClickFunc = {
                    coroutineScope.launch {
                        viewModel.sendToGetVerifyCode(identity, codeOTP, password, isPhoneNumber)
                    }
                    viewModel.sendTrackingSubmitNewPass(identity, isPhoneNumber)
                }, enable = isEnable
            )
        }
    }){
        Column(
            modifier = Modifier
                .background(White)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 95.dp)){
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .wrapContentHeight()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                model = R.drawable.olmo_img_welcome_signup_png,
                                contentDescription = "",
                                contentScale = ContentScale.Inside
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(), horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_ellipse),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 180.dp),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(start = 32.dp, end = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        SpaceCompose(10.dp)
                        Text(
                            text = stringResource(id = R.string.title_create_new_password),
                            style = MaterialTheme.typography.subtitle1.copy(
                                color = Neutral_Gray_9,
                                lineHeight = 30.sp,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        SpaceCompose(height = 35.dp)
                        BasePasswordInputNewStyle(Modifier.padding(top = 0.dp),
                            stringResource(id = R.string.hint_new_password_input),
                            R.drawable.ic_eye_slash,
                            hasError = !isValidAllFiledCallBack && password.isNotEmpty(),
                            onTextChanged = { text ->
                                password = text.trim()
                                viewModel.isValidatePassword(password)
                            },
                            onKeyboardDropFunc = {
                                viewModel.isValidatePassword(password)
                            },
                            isSuccess = isValidAllFiledCallBack && password.isNotEmpty())
                        SpaceCompose(13.dp)
                        DescriptionPasswordCompose(
                            normalStateIcon = R.drawable.ic_normal_state_pw,
                            activeStateIcon = R.drawable.ic_active_pw_purple,
                            errorStateIcon = R.drawable.ic_error_verify_pw,
                            isChecking = true,
                            content = password,
                            isValidAllFiledCallBack = {
                                isValidAllFiledCallBack = it
                            }
                        )
                    }
                }
            }
        }
        LoadingScreen(isLoading = isLoading.value)
    }
    LaunchedEffect(Unit) {
        viewModel.isResetSuccess.collectLatest {
            navController.navigate(ScreenName.OnBoardSignUpScreen.route)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.errorBody.collectLatest { res ->
            if (res.message.isNotEmpty()) {
                res.message.let {
                    showMessage(
                        context,
                        it
                    )
                }
            } else {
                showMessage(context, Constants.ERROR_COMMON)
            }
        }
    }
}