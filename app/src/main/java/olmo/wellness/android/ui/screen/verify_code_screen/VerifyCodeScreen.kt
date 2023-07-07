package olmo.wellness.android.ui.screen.verify_code_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.utils.callHotline
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.wrapper.VerifyCodeWrapper
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.common.extensions.showMessageResource
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@SuppressLint("NewApi", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun VerifyCodeScreen(
    navController: NavController,
    verifyCode : VerifyCodeWrapper,
    viewModel: VerifyCodeModel = hiltViewModel()
) {
    viewModel.setUserType(UserTypeModel(verifyCode.userType ?: UserTypeModel.BUYER.value))
    viewModel.bindVerifyCodeModel(verifyCode)
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val isForgetPassword by remember {
        mutableStateOf(verifyCode.isForget)
    }
    val context = LocalContext.current
    val isLoading = viewModel.isLoading.collectAsState()
    var otpValue by remember { mutableStateOf("") }
    val otpFromAPIGoogle = viewModel.otpValue.collectAsState()
    if (otpFromAPIGoogle.value.isNotEmpty()) {
        otpValue = otpFromAPIGoogle.value
    }
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
            else -> {}
        }
    }

    SmsRetrieverUserConsentBroadcast { _, code ->
        if (code.isNotEmpty()) {
            viewModel.getOTP(code)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.isSuccess.collect {
            if (it) {
                if (isForgetPassword == true) {
                    navController.navigate(
                        ScreenName.CreatePasswordScreen.route
                            .plus("?code=$otpValue")
                            .plus("&identity=${verifyCode.identity?.trim()}")
                            .plus("&isPhone=${verifyCode.isPhone}")
                    )
                    viewModel.resetState()
                } else {
                    if(verifyCode.caseNotVerified == true){
                        val isDeepLink = false
                        navController.navigate(ScreenName.SignInEmailScreen.route + "/$isDeepLink")
                    }else{
                        navController.navigate(ScreenName.OnboardLiveScreen.route)
                    }
                    viewModel.resetLocal()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.isNeedLogin.collect {
            if (it) {
                val isDeepLink = false
                navController.navigate(ScreenName.SignInEmailScreen.route + "/$isDeepLink")
                viewModel.resetState()
            }
        }
    }

    Scaffold(bottomBar = {
        Column(
            modifier = Modifier.padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            val isEnable = otpValue.length == 6
            PrimaryLiveButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 38.dp, end = 38.dp),
                stringResource(id = R.string.button_action_explore),
                onClickFunc = {
                    if (isForgetPassword == true) {
                        navController.navigate(
                            ScreenName.CreatePasswordScreen.route
                                .plus("?code=$otpValue").plus("&identity=${verifyCode.identity}")
                                .plus("&isPhone=${verifyCode.isPhone}")
                        )
                    } else {
                        focusManager.clearFocus()
                        viewModel.verifyUser(otpValue)
                    }
                },
                enable = isEnable
            )
        }
    }) {
        Column(
            modifier = Modifier
                .background(White)
                .fillMaxSize()
                .padding(bottom = 95.dp)
                .verticalScroll(rememberScrollState())){
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
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
                        .padding(top = 200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = stringResource(id = R.string.tv_title_header), style =
                            MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 30.sp
                            ),
                            fontFamily = MontserratFont,
                            color = Neutral_Gray_9,
                            textAlign = TextAlign.Center
                        )
                    }
                    SpaceCompose(height = 16.dp)
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (verifyCode.isPhone == true) {
                                stringResource(id = R.string.tv_title_input_otp_phone)
                            } else {
                                stringResource(id = R.string.tv_title_input_otp_email)
                            }, style =
                            MaterialTheme.typography.caption.copy(
                                fontWeight = FontWeight.Normal
                            ),
                            fontFamily = MontserratFont,
                            color = Neutral_Gray_9,
                            textAlign = TextAlign.Center
                        )
                    }
                    SpaceCompose(height = 16.dp)
                    Text(
                        text = verifyCode.identity.orEmpty(), style =
                        MaterialTheme.typography.caption.copy(
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        fontFamily = MontserratFont,
                        color = Neutral_Gray_9,
                        textAlign = TextAlign.Center
                    )
                    SpaceCompose(16.dp)
                    var countDown by remember { mutableStateOf(60) }
                    LaunchedEffect(countDown) {
                        while (countDown > 0) {
                            delay(1.seconds)
                            countDown--
                        }
                    }
                    if (countDown == 0) {
                        Row(horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                coroutineScope.launch {
                                    countDown = 60
                                    viewModel.resendOTP()
                                }
                            }) {
                            Text(
                                text = stringResource(id = R.string.tv_des_did_not_resend_otp), style =
                                MaterialTheme.typography.caption.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                fontFamily = MontserratFont,
                                color = Neutral_Gray_9,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.tv_action_resend_otp), style =
                                MaterialTheme.typography.caption.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                fontFamily = MontserratFont,
                                color = Color_LiveStream_Main_Color
                            )
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val style1 = MaterialTheme.typography.caption.copy(
                                color = Color_LiveStream_Main_Color,
                                lineHeight = 14.sp,
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.Medium
                            )
                            val style2 = MaterialTheme.typography.caption.copy(
                                color = Neutral_Gray_9,
                                lineHeight = 14.sp,
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = stringResource(id = R.string.tv_content_first_resend_otp) + " ",
                                style = style1,
                                modifier = Modifier.padding(end = 3.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.tv_content_second_resend_otp) + " ",
                                style = style2,
                                modifier = Modifier.padding(end = 3.dp)
                            )
                            Text(
                                text = "$countDown",
                                style = style1.copy(color = Color_LiveStream_Main_Color),
                                modifier = Modifier.padding(end = 3.dp)
                            )

                            Text(
                                text = " " + stringResource(id = R.string.tv_content_third_resend_otp),
                                style = style2
                            )

                        }
                    }
                    SpaceCompose(height = 50.dp)
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 38.dp, end = 38.dp)
                    ){
                        OtpView(
                            otpText = otpValue,
                            onOtpTextChange = {
                                otpValue = it
                                viewModel.updateOTP(otpValue)
                            },
                            type = OTP_VIEW_TYPE_UNDERLINE,
                            password = false,
                            otpCount = 6,
                            containerSize = 52.dp,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            charColor = Neutral_Gray_9,
                            charSize = 24.sp
                        )
                    }
                    SpaceCompose(40.dp)
                }
            }
        }
        LoadingScreen(isLoading = isLoading.value)
    }
    LaunchedEffect(true) {
        viewModel.errorBody.collect { res ->
            if (res.message.isNotEmpty()) {
                res.message.let { showMessage(context, it) }
            } else {
                showMessageResource(context, R.string.tv_signup_failed)
            }
        }
    }
}

@Composable
fun SmsRetrieverUserConsentBroadcast(
    smsCodeLength: Int = 6,
    onSmsReceived: (message: String, code: String) -> Unit
) {

    val context = LocalContext.current
    var shouldRegisterReceiver by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        SmsRetriever.getClient(context)
            .startSmsUserConsent(null)
            .addOnSuccessListener {
                shouldRegisterReceiver = true
            }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it?.resultCode == Activity.RESULT_OK && it.data != null) {
                val message: String? = it.data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                message?.let {
                    val verificationCode = getVerificationCodeFromSms(message, smsCodeLength)
                    onSmsReceived(message, verificationCode)
                }
                shouldRegisterReceiver = false
            } else {
                // TODO Handle Exception
            }
        }

    if (shouldRegisterReceiver) {
        SystemBroadcastReceiver(
            systemAction = SmsRetriever.SMS_RETRIEVED_ACTION,
            broadCastPermission = SmsRetriever.SEND_PERMISSION,
        ) { intent ->
            if (intent != null && SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get consent intent
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            launcher.launch(consentIntent)
                        } catch (e: ActivityNotFoundException) {
                        }
                    }

                    CommonStatusCodes.TIMEOUT -> {
                    }
                }
            }
        }
    }
}

@Composable
fun SystemBroadcastReceiver(
    systemAction: String,
    broadCastPermission: String,
    onSystemEvent: (intent: Intent?) -> Unit
) {
    // Grab the current context in this part of the UI tree
    val context = LocalContext.current

    // Safely use the latest onSystemEvent lambda passed to the function
    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

    // If either context or systemAction changes, unregister and register again
    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onSystemEvent(intent)
            }
        }
        context.registerReceiver(broadcast, intentFilter)
        // When the effect leaves the Composition, remove the callback
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }

    DisposableEffect(context, broadCastPermission) {
        val intentFilter = IntentFilter(broadCastPermission)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onSystemEvent(intent)
            }
        }
        context.registerReceiver(broadcast, intentFilter)
        // When the effect leaves the Composition, remove the callback
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}

internal fun getVerificationCodeFromSms(sms: String, smsCodeLength: Int): String =
    sms.filter { it.isDigit() }
        .substring(0 until smsCodeLength)
