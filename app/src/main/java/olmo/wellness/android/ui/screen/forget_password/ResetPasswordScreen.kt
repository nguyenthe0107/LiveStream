package olmo.wellness.android.ui.screen.forget_password

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.toJson
import olmo.wellness.android.domain.model.wrapper.VerifyCodeWrapper
import olmo.wellness.android.ui.common.components.BaseTextInput
import olmo.wellness.android.ui.common.components.PhoneNumberInput
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.common.extensions.showMessageResource
import olmo.wellness.android.ui.common.validate.emailValidator
import olmo.wellness.android.ui.common.validate.getFormatPhone
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.MontserratFont
import olmo.wellness.android.ui.theme.Neutral_Gray_3
import olmo.wellness.android.ui.theme.Neutral_Gray_9

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun ResetPasswordScreen(
    modalImageBottomSheetState: ModalBottomSheetState?,
    navController: NavController,
    isPhone: Boolean,
    viewModel: ForgetPasswordViewModel = hiltViewModel()
){
    val isLoading = viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    var emailInput by remember {
        mutableStateOf("")
    }
    var phoneInput by remember {
        mutableStateOf("")
    }
    val countryCode = viewModel.countryCodeSelected.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val canNextStep =
        if (isPhone) {
            phoneInput.isNotEmpty()
        } else {
            emailValidator(emailInput)
        }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    ){
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                SpaceCompose(height = 16.dp)
                Text(
                    text = "Reset Password", style = MaterialTheme.typography.subtitle2.copy(
                        color = Neutral_Gray_9,
                        fontFamily = MontserratFont,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 26.sp,
                        textAlign = TextAlign.Center
                    )
                )
                SpaceCompose(height = 7.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Neutral_Gray_3)
                        .height(1.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 34.dp, start = 32.dp, end = 32.dp)
                ) {
                    if (isPhone) {
                        PhoneNumberInput(
                            hint = stringResource(id = R.string.hint_phone_input),
                            onTextChanged = { text ->
                                phoneInput = text.trim()
                            },
                            listCountry = viewModel.countryList.collectAsState().value,
                            countryCodeValue = {
                                viewModel.updateCountrySelected(it)
                            },
                            lastInput = true,
                            onKeyboardDropFunc = {
                            }
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.tv_sign_in_by_email), style =
                            MaterialTheme.typography.subtitle2.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = MontserratFont
                            )
                        )
                        SpaceCompose(height = 5.dp)
                        BaseTextInput(
                            hint = stringResource(id = R.string.hint_email_input),
                            onTextChanged = { text ->
                                emailInput = text.trim()
                            },
                            lastInput = true,
                            onKeyboardDropFunc = {
                            })
                    }
                    SpaceCompose(height = 30.dp)
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Bottom
            ){
                PrimaryLiveButton(
                    modifier = Modifier.fillMaxWidth(),
                    stringResource(id = R.string.send), onClickFunc = {
                        coroutineScope.launch {
                            val identity = if (isPhone) {
                                getFormatPhone(phoneInput)
                            } else {
                                emailInput
                            }
                            viewModel.sendToGetVerifyCode(isPhone, identity)
                            viewModel.trackingClickSendForgotPassword(isPhone, identity)
                            modalImageBottomSheetState?.hide()
                        }
                    }, enable = canNextStep
                )
                SpaceCompose(height = 16.dp)
                Text(
                    text = stringResource(id = R.string.des_reset_password), style =
                    MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = MontserratFont,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Start,
                        color = Neutral_Gray_9
                    )
                )
            }
        }
        LoadingScreen(isLoading = isLoading.value)
    }
    LaunchedEffect(true) {
        viewModel.isResetSuccess.collectLatest {
            val identity = if (isPhone) {
                countryCode.value.plus(getFormatPhone(phoneInput))
            } else {
                emailInput
            }
            val isForget = true
            val verifyCodeWrapper = VerifyCodeWrapper(identity = identity, isForget = isForget, isPhone = isPhone)
            navController.navigate(
                ScreenName.VerifyCodeScreen.route
                    .plus("?defaultData=${verifyCodeWrapper.toJson()}")
            )
        }
    }

    LaunchedEffect(true) {
        viewModel.errorBody.collect { res ->
            if (res.message.isNotEmpty()) {
                res.message.let {
                    showMessage(
                        context,
                        it
                    )
                }
            } else {
                showMessageResource(context, R.string.tv_signup_failed)
            }
        }
    }
}