@file:Suppress("PreviewAnnotationInFunctionWithParameters")
package olmo.wellness.android.ui.screen.signup_screen.mail
import android.annotation.SuppressLint
import android.util.Log
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.definition.AuthMethod
import olmo.wellness.android.data.model.definition.SpecialError
import olmo.wellness.android.domain.model.wrapper.VerifyCodeWrapper
import olmo.wellness.android.ui.analytics.TrackingConstants
import olmo.wellness.android.ui.common.components.*
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.common.validate.getFormatPhone
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.SignUpViewModel
import olmo.wellness.android.ui.screen.signup_screen.utils.*
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalMaterialApi
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter",
    "StateFlowValueCalledInComposition"
)
@Composable
fun SignUpScreen(
    modalBottomSheetState: ModalBottomSheetState,
    navController: NavController,
    phoneMode: Boolean ?= false,
    onNavigationSelectUserType : ((status: Boolean) -> Unit)?=null,
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigationUserRegister : ((status: Boolean) -> Unit)?=null,
){
    val isLoading = viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    context as MainActivity
    var email by remember {
        mutableStateOf(viewModel.email.value)
    }
    var phoneNumber by remember {
        mutableStateOf(viewModel.phoneNumber.value)
    }
    var password by remember {
        mutableStateOf(viewModel.password.value)
    }
    val hasErrorPassword = viewModel.hasOnErrorPassword.collectAsState()
    val isErrorEmail = viewModel.isErrorEmail.collectAsState()
    var isNotFoundAcc by remember {
        mutableStateOf(false)
    }
    val isErrorPhone = viewModel.isErrorPhone.collectAsState()
    var isInvalidAccount by remember {
        mutableStateOf(false)
    }
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
    LaunchedEffect(Unit) {
        viewModel.errorBody.collect { res ->
            if (res.isNotEmpty()) {
                isInvalidAccount = res == SpecialError.PRINCIPAL_NOT_FOUND.nameError
            }
        }
    }
    val errMes = viewModel.errorBody.collectAsState()
    if (errMes.value.isNotEmpty()) {
        isNotFoundAcc = errMes.value == SpecialError.USER_NOT_FOUND.nameError
    }
    val paddingModifier = Modifier.padding(dimensionResource(R.dimen.dimen_10dp))
    val coroutineScope = rememberCoroutineScope()
    val userType = viewModel.userType.collectAsState()
    val identity = viewModel.identityValue.collectAsState()
    val userName = viewModel.username.collectAsState()
    val authData = viewModel.authData.collectAsState()
    val tokenDevice = viewModel.tokenDevice.collectAsState()
    val enable =
        if (phoneMode == true) {
            !isErrorPhone.value && phoneNumber.isNotEmpty()
        } else {
            !isErrorEmail.value && email.isNotEmpty()
        }
        && (!hasErrorPassword.value && password.isNotEmpty()) && userType.value.isNotEmpty()
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                viewModel.getUserTypeFromLocal()
            }
            else->{}
        }
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.6f)
        .background(Color.White)) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween){
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ){
                Column(
                    modifier = paddingModifier
                        .background(color = White)
                        .padding(horizontal = 32.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 23.dp)
                    ){
                        if(phoneMode == true){
                            PhoneNumberInput(
                                hint = stringResource(id = R.string.hint_phone_input),
                                onTextChanged = { text ->
                                    phoneNumber = text.trim()
                                    viewModel.isPhoneValid(phoneNumber)
                                },
                                listCountry = viewModel.countryList.collectAsState().value,
                                countryCodeValue = {
                                    viewModel.updateCountrySelected(it)
                                },
                                onClickSelectFlag = {
                                },
                                hasError = isErrorPhone.value || isInvalidAccount,
                                defaultValue = phoneNumber
                            )
                        }else{
                            BaseTextInput(
                                hint = stringResource(id = R.string.hint_email_input_signup),
                                title = stringResource(id = R.string.hint_email_input),
                                onTextChanged = { text ->
                                    email = text.trim()
                                    viewModel.isEmailValid(email)
                                }, onKeyboardDropFunc = {
                                    viewModel.isEmailValid(email)
                                },
                                hasError = isErrorEmail.value || isNotFoundAcc,
                                defaultValue = email,
                                isSuccess = !isErrorEmail.value && email.isNotEmpty()
                            )
                        }
                        SpaceCompose(height = 16.dp)
                        BasePasswordInput(Modifier,
                            stringResource(id = R.string.hint_password_input),
                            stringResource(id = R.string.hint_password_input),
                            R.drawable.ic_eye_slash,
                            onTextChanged = { text ->
                                password = text
                                viewModel.isValidatePassword(password)
                            },
                            onKeyboardDropFunc = {
                                viewModel.isValidatePassword(password)
                            },
                            defaultValue = password,
                            isSuccess = !hasErrorPassword.value && password.isNotEmpty()
                        )
                        AnimatedVisibility(visible = password.isNotEmpty()) {
                            DescriptionPasswordCompose(
                                paddingTop = 16.dp,
                                normalStateIcon = R.drawable.ic_normal_state_pw,
                                activeStateIcon = R.drawable.ic_active_pw_purple,
                                isChecking = true,
                                content = password,
                                defaultValueValidate = enable
                            )
                        }
                        SpaceCompose(height = 16.dp)
                        ItemUserTypeMenuSelected(
                            titleText = stringResource(id = R.string.text_title_user_type_select),
                            hint = stringResource(id = R.string.text_hilt_user_type_select),
                            valueContent = userType.value, onChange = {
                                onNavigationSelectUserType?.invoke(true)
                                coroutineScope.launch {
                                    modalBottomSheetState.hide()
                                }
                            }
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 35.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp)
                ){
                    PrimaryLiveButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp),
                        stringResource(id = R.string.action_sign_up),
                        enable = enable,
                        onClickFunc = {
                            if(phoneMode == true){
                                if(phoneNumber.isNotEmpty() && password.isNotEmpty()){
                                    coroutineScope.launch {
                                        val phoneFormat = getFormatPhone(phoneNumber)
                                        val finalPhoneNumber = viewModel.countryCodeSelected.value.plus(phoneFormat)
                                        viewModel.registerWithUserPass(finalPhoneNumber, password, TrackingConstants.TRACKING_VALUE_SIGNIN_PHONE_NUMBER)
                                    }
                                    viewModel.sendTracking(true)
                                }
                            }else{
                                if (email.isNotEmpty() && password.isNotEmpty()) {
                                    coroutineScope.launch {
                                        viewModel.registerWithUserPass(email, password, TrackingConstants.TRACKING_VALUE_SIGNIN_EMAIL)
                                    }
                                    viewModel.sendTracking(false)
                                }
                            }
                        }
                    )
                }
                ProxyViewCompose(openPrivacy = {
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                    val linkWebView = Constants.LINK_PRIVACY
                    navController.navigate(
                        ScreenName.PrivacyScreen.route.plus(
                            "?defaultData=$linkWebView"
                        )
                    )
                })
            }
        }
        LoadingScreen(isLoading = isLoading.value)
    }
    val error = viewModel.errorBody.collectAsState()
    if (error.value.isNotEmpty()) {
        if (error.value == SpecialError.USER_REGISTERED.nameError) {
            viewModel.resetState()
            coroutineScope.launch {
                modalBottomSheetState.hide()
            }
            onNavigationUserRegister?.invoke(true)
        } else {
            showMessage(context, error.value)
        }
    }
    LaunchedEffect(Unit){
        viewModel.navigationToHome.collect {
            if (it){
                viewModel.resetUserTypeLocal()
                navController.navigate(ScreenName.OnboardLiveScreen.route)
            }
        }
    }
    val isRegisterPhone = viewModel.isPhone.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.navigationToOTPVerify.collect {
            if (it) {
                val verifyCodeWrapper = VerifyCodeWrapper(identity = identity.value,
                    userName = userName.value, userType = userType.value,
                    authData = authData.value, authMethod = AuthMethod.USER_PASS,
                    tokenDevice = tokenDevice.value, password = password, isPhone = isRegisterPhone.value)
                navController.navigate(
                    ScreenName.VerifyCodeScreen.route
                        .plus("?defaultData=${verifyCodeWrapper.toJson()}")
                )
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                }
                viewModel.resetState()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.toOTPVerifyForNotVerified.collectLatest {
            if (it) {
                val verifyCodeWrapper = VerifyCodeWrapper(identity = identity.value,
                    userName = userName.value, userType = userType.value,
                    authData = authData.value, authMethod = AuthMethod.USER_PASS, caseNotVerified = true)
                navController.navigate(
                    ScreenName.VerifyCodeScreen.route
                        .plus("?defaultData=${verifyCodeWrapper.toJson()}")
                )
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                }
                viewModel.resetState()
            }
        }
    }

}


