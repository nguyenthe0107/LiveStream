package olmo.wellness.android.ui.screen.forget_password

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.toJson
import olmo.wellness.android.domain.model.wrapper.VerifyCodeWrapper
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.components.BaseTextInput
import olmo.wellness.android.ui.common.components.PhoneNumberInput
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.common.extensions.showMessageResource
import olmo.wellness.android.ui.common.validate.emailValidator
import olmo.wellness.android.ui.common.validate.getFormatPhone
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.ButtonCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.White

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun ForgetPasswordScreen(navController: NavController, isPhone: Boolean, viewModel: ForgetPasswordViewModel = hiltViewModel()){
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
    Scaffold(
        topBar = {
            DetailTopBar(
                stringResource(id = R.string.title_forgot_password),
                navController,
                White,
                2.dp)
        }) {
        val canNextStep =
            if(isPhone){
                phoneInput.isNotEmpty()
            }else{
                emailValidator(emailInput)
            }
        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 38.dp, end = 38.dp)) {
            SpaceCompose(32.dp)
            if(isPhone){
                PhoneNumberInput(
                    hint = stringResource(id = R.string.hint_phone_input),
                    onTextChanged = { text ->
                        phoneInput = text.trim()
                    },
                    listCountry = viewModel.countryList.collectAsState().value,
                    countryCodeValue = {
                        viewModel.updateCountrySelected(it)
                    }
                )
            }else{
                BaseTextInput(
                    hint = stringResource(id = R.string.hint_email_input),
                    onTextChanged = { text ->
                        emailInput = text.trim()
                    })
            }
            ButtonCompose(
                12.dp,
                16.dp,
                R.string.next, onClick = {
                coroutineScope.launch {
                    val identity = if(isPhone){
                        getFormatPhone(phoneInput)
                    }else{
                        emailInput
                    }
                    viewModel.sendToGetVerifyCode(isPhone,identity)
                }
            }, enable = canNextStep)
        }
    }
    LoadingScreen(isLoading = isLoading.value)
    LaunchedEffect(true) {
        viewModel.isResetSuccess.collectLatest {
            val identity = if(isPhone){
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
                showMessage(
                    context,
                    res.message
                )
            } else {
                showMessageResource(context, R.string.tv_signup_failed)
            }
        }
    }
}