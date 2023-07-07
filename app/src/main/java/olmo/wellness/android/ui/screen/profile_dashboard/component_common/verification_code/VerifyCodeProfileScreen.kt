package olmo.wellness.android.ui.screen.profile_dashboard.component_common.verification_code

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.utils.getFormattedNumber
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.screen.verify_code_screen.OTP_VIEW_TYPE_UNDERLINE
import olmo.wellness.android.ui.screen.verify_code_screen.OtpView
import olmo.wellness.android.ui.theme.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, androidx.compose.material.ExperimentalMaterialApi::class)
@SuppressLint("NewApi", "CoroutineCreationDuringComposition")
@Composable
fun VerifyCodeProfileScreen(
    modalBottomSheetState: ModalBottomSheetState,
    identity: String? = "",
    isPhone: Boolean? = false,
    userType: String? = UserTypeModel.BUYER.value,
    onSuccess: (status: Boolean) -> Unit,
    onFailed: (status: Boolean) -> Unit,
    viewModel: VerifyCodeProfileModel = hiltViewModel()){
    val context = LocalContext.current
    LaunchedEffect("Init"){
        viewModel.bindIdentity(isPhone,identity)
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
            Lifecycle.Event.ON_DESTROY -> {
            }
            else -> {
            }
        }
    }
    viewModel.setUserType(UserTypeModel(userType ?: UserTypeModel.BUYER.value))
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val isLoading = viewModel.isLoading.collectAsState()
    var otpValue by remember { mutableStateOf("") }
    var isWaitingOTP by remember {
        mutableStateOf(true)
    }
    val otpFromAPIGoogle = viewModel.otpValue.collectAsState()
    if (otpFromAPIGoogle.value.isNotEmpty()) {
        otpValue = otpFromAPIGoogle.value
    }
    SmsRetrieverUserConsentBroadcast { _, code ->
        if (code.isNotEmpty()) {
            viewModel.getOTP(code)
            if (isPhone == true) {
                isWaitingOTP = false
            }
        }
    }
    val isSuccess = viewModel.isSuccess.collectAsState()
    if (isSuccess.value) {
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }
        onSuccess.invoke(true)
        viewModel.resetValue()
    }
    val maxHeight = 350.dp
    Box(
        modifier = Modifier
            .background(Color_gray_FF7)
            .requiredHeight(maxHeight)){
        Column(
            modifier = Modifier
                .background(Color_gray_FF7)
                .requiredHeight(maxHeight),
            verticalArrangement = Arrangement.SpaceBetween){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.Top){
                Column {
                    SpaceCompose(10.dp)
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color_Purple_FBC, fontWeight = FontWeight.Bold,
                                    fontFamily = MontserratFont, fontSize = 18.sp )) {
                                    append(stringResource(id = R.string.tv_title_header))
                                }
                            },
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.caption,
                        )
                    }
                    SpaceCompose(5.dp)
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color_Purple_FBC, fontFamily = MontserratFont,fontSize = 16.sp)) {
                                    append(stringResource(id = R.string.tv_title_input_otp))
                                }
                            },
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.caption,
                        )
                    }
                    SpaceCompose(5.dp)
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                        var identityFinal = identity
                        if(isPhone == true){
                            identityFinal = identity?.let { getFormattedNumber(it) }
                        }
                        Text(
                            text = identityFinal ?: "", style =
                            MaterialTheme.typography.h6,
                            fontFamily = MontserratFont,
                            color = Color_Purple_FBC,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    }
                    SpaceCompose(30.dp)
                    Row(horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()){
                        OtpView(
                            otpText = otpValue,
                            onOtpTextChange = {
                                otpValue = it
                            },
                            type = OTP_VIEW_TYPE_UNDERLINE,
                            password = false,
                            otpCount = 6,
                            containerSize = 52.dp,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            charColor = Color_Purple_FBC,
                            charSize = 24.sp
                        )
                    }
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    coroutineScope.launch {
                                        countDown = 60
                                        viewModel.resendOTP()
                                    }
                                }){
                            Text(
                                text = "Did not receive the code?", style =
                                MaterialTheme.typography.subtitle1,
                                fontFamily = MontserratFont,
                                color = Color_Purple_FBC,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(
                                        fontFamily = MontserratFont,
                                        color = Color_Purple_FBC, fontSize = 14.sp)) {
                                        append(stringResource(id = R.string.title_action_click_to) + " ")
                                    }
                                    withStyle(style = SpanStyle(
                                        fontFamily = MontserratFont,
                                        color = Color_Purple_FBC,
                                        fontSize = 14.sp,
                                        textDecoration = TextDecoration.Underline)) {
                                        append(stringResource(id = R.string.title_action_resend))
                                    }
                                }
                            )
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()){
                            val style = MaterialTheme.typography.subtitle2.copy(
                                color = Color_Purple_FBC,
                                lineHeight = 22.sp,
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.Normal
                            )
                            Text(
                                text = "Please wait ", style = style,
                                modifier = Modifier.padding(end = 3.dp)
                            )
                            Text(
                                text = "$countDown", style = style.copy(color = Color_Purple_FBC),
                                modifier = Modifier.padding(end = 3.dp)
                            )

                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(
                                        fontFamily = MontserratFont,
                                        fontWeight = FontWeight.Normal,
                                        color = Color_Purple_FBC, fontSize = 14.sp)) {
                                        append("seconds to " + " ")
                                    }
                                    withStyle(style = SpanStyle(
                                        fontFamily = MontserratFont,
                                        color = Color_Purple_FBC,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        textDecoration = TextDecoration.Underline)) {
                                        append(stringResource(id = R.string.title_action_resend))
                                    }
                                }
                            )

                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom){
                GroupButtonBottomCompose(cancelCallback = {
                    onFailed.invoke(true)
                    viewModel.resetValue()
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                }, confirmCallback = {
                    coroutineScope.launch {
                        focusManager.clearFocus()
                        viewModel.verifyUser(otpValue)
                    }
                })
            }
        }
        LoaderWithAnimation(isPlaying = isLoading.value)
    }

    LaunchedEffect(viewModel.errorBody) {
        viewModel.errorBody.collect { res ->
            if (res.isNotEmpty()) {
                showMessage(context, res)
                viewModel.resetValue()
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
