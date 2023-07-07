@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package olmo.wellness.android.ui.screen.signin_screen.phone

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.definition.SpecialError
import olmo.wellness.android.domain.model.defination.SignUpBottomSheetType
import olmo.wellness.android.domain.model.defination.UserExistsType
import olmo.wellness.android.domain.model.wrapper.VerifyCodeWrapper
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.analytics.TrackingConstants
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.components.*
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.common.extensions.showMessageResource
import olmo.wellness.android.ui.common.validate.getFormatPhone
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.forget_password.ResetPasswordScreen
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signin_screen.SignInViewModel
import olmo.wellness.android.ui.screen.signin_screen.common.TopSignInCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.*
import olmo.wellness.android.ui.screen.user_registed.UserRegisteredScreen
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun SignInByPhoneScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoading = viewModel.isLoading.collectAsState()
    val isAccountNotExists = viewModel.isErrorAccountNotExist.collectAsState()
    val mFirebaseAuth = FirebaseAuth.getInstance()
    if (mFirebaseAuth.currentUser != null) {
        mFirebaseAuth.signOut()
    }
    val signInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(stringResource(id = R.string.gcp_id))
            .requestId()
            .build()
    )
    signInClient.signOut()
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    handleSignInResult(task, context, viewModel, coroutineScope)
                }
            }
        }
    var typeBottomSheetSelected by remember {
        mutableStateOf(SignUpBottomSheetType.UN_KNOW)
    }
    val isNavigationForgetPassword = viewModel.navigationToForgetPassword.collectAsState()
    val modalImageBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )
    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxHeight(),
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp)){
                when(typeBottomSheetSelected){

                    SignUpBottomSheetType.RESET_PASSWORD -> {
                        ResetPasswordScreen(modalImageBottomSheetState = modalImageBottomSheetState, navController = navController, isPhone = true)
                        viewModel.resetStateNumberInputPassword()
                    }

                    SignUpBottomSheetType.USER_REGISTERED -> {
                        UserRegisteredScreen(onCallBackFunc = { status ->
                            if(status){
                                coroutineScope.launch {
                                    modalImageBottomSheetState.hide()
                                }
                            }
                        }, userType = UserExistsType.USER_NOT_REGISTER)
                    }
                }
            }
        },
        sheetState = modalImageBottomSheetState
    ) {
        Scaffold(
            topBar = {
                navController.let {
                    ToolbarSchedule(
                        title = stringResource(id = R.string.tv_header_sign_in),
                        backIconDrawable = R.drawable.ic_back_calendar,
                        navController = it,
                        backgroundColor = Transparent,
                        onBackStackFunc = {
                            navController.popBackStack()
                        }
                    )
                }
            }, modifier = Modifier.fillMaxSize(),
            backgroundColor = Color_Purple_FBC
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 32.dp,
                            topEnd = 32.dp
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(White)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isAccountNotExists.value) {
                        typeBottomSheetSelected = SignUpBottomSheetType.USER_REGISTERED
                        coroutineScope.launch {
                            modalImageBottomSheetState.show()
                        }
                    }
                    SpaceCompose(height = 24.dp)
                    TopSignInCompose(onNavigationSignUp = {
                        navController.navigate(ScreenName.OnBoardSignUpScreen.route)
                    })
                    SpaceCompose(height = 30.dp)
                    BodyCompose(onFacebookCLick = {}, onGoogleClick = {
                        startForResult.launch(signInClient.signInIntent)
                    }, navController = navController, viewModel = viewModel,
                        coroutineScope = coroutineScope,
                        mFirebaseAuth = mFirebaseAuth, context = context,
                        onResetPassword = { status ->
                            if(status){
                                typeBottomSheetSelected = SignUpBottomSheetType.RESET_PASSWORD
                                coroutineScope.launch {
                                    modalImageBottomSheetState.show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
    LoadingScreen(isLoading = isLoading.value)
    val userType = viewModel.userType.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.isSuccess.collect {
            if (it) {
                navController.navigate(ScreenName.OnboardLiveScreen.route)
            }
        }
    }

    val errorBody = viewModel.errorBody.collectAsState()
    if (errorBody.value.isNotEmpty()) {
        showMessage(
            context,
            errorBody.value
        )
    }
    LaunchedEffect(key1 = isNavigationForgetPassword){
        snapshotFlow{isNavigationForgetPassword.value}.collectLatest { status ->
            if(status){
                typeBottomSheetSelected = SignUpBottomSheetType.RESET_PASSWORD
                coroutineScope.launch {
                    modalImageBottomSheetState.show()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
fun handleSignInResult(
    completedTask: Task<GoogleSignInAccount>,
    context: Context, viewModel: SignInViewModel, coroutineScope: CoroutineScope
) {
    val account = try {
        completedTask.getResult(ApiException::class.java)
    } catch (e: ApiException) {
        return
    }
    firebaseAuthWithGoogle(account, context, viewModel, coroutineScope)
}

@ExperimentalMaterialApi
@OptIn(ExperimentalAnimationApi::class)
private fun firebaseAuthWithGoogle(
    acct: GoogleSignInAccount,
    context: Context, viewModel: SignInViewModel,
    coroutineScope: CoroutineScope
) {
    val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnSuccessListener(context as MainActivity) { authResult ->
            val mUser = FirebaseAuth.getInstance().currentUser
            mUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result.token
                        // Send token to your backend via HTTPS
                        coroutineScope.launch {
                            viewModel.loginWithFirebase(idToken.orEmpty())
                        }
                    }
                }
        }
        .addOnFailureListener(context) { e ->
            showMessageResource(context, R.string.tv_login_failed)
        }
}

@Composable
fun BodyCompose(
    onGoogleClick: () -> Unit, onFacebookCLick: () -> Unit,
    navController: NavController?, isLoading: Boolean? = null,
    viewModel: SignInViewModel, coroutineScope: CoroutineScope,
    mFirebaseAuth: FirebaseAuth? = null, context: Context? = null,
    onResetPassword: ((Boolean) -> Unit) ?= null
){
    val paddingModifier = Modifier.padding(dimensionResource(R.dimen.dimen_10dp))
    val paddingModifierText = Modifier.padding(top = dimensionResource(R.dimen.dimen_10dp))
    var phone by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isNextStep by remember {
        mutableStateOf(false)
    }
    val isErrorPhone = viewModel.isErrorPhone.collectAsState()
    val isErrorPassword = viewModel.errorBody.collectAsState()
    val enableButton = !isErrorPhone.value && password.isNotEmpty() && phone.isNotEmpty()
    val hasErrorPassword = viewModel.hasOnErrorPassword.collectAsState()
    val userType = viewModel.userType.collectAsState()
    Column(
        modifier = paddingModifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        Text(
            text = stringResource(id = R.string.tv_sign_in_by_email), style =
            MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFont
            ), modifier = paddingModifierText
        )
        SpaceCompose(height = 5.dp)
        PhoneNumberInput(
            hint = stringResource(id = R.string.hint_phone_input),
            onTextChanged = { text ->
                phone = text.trim()
                isNextStep = false
                viewModel.resetState()
                viewModel.isPhoneValid(phone)
            },
            onKeyboardDropFunc = {
                viewModel.isPhoneValid(phone)
            },
            hasError = isErrorPhone.value,
            listCountry = viewModel.countryList.collectAsState()?.value,
            countryCodeValue = { selectedValue ->
                viewModel.updateCountrySelected(selectedValue)
            },
            isSuccess = !isErrorPhone.value && phone.isNotEmpty()
        )
        SpaceCompose(height = 16.dp)
        BasePasswordInput(
            Modifier,
            stringResource(id = R.string.hint_password_input),
            stringResource(id = R.string.hint_password_input),
            R.drawable.ic_eye_slash,
            hasError = isErrorPassword.value.isNotEmpty() && isErrorPassword.value == SpecialError.INVALID_CREDENTIALS.nameError,
            onTextChanged = { text ->
                password = text
                isNextStep = false
                viewModel.resetState()
                viewModel.isValidatePassword(password)
            },
            lastInput = true,
            isNextStep = isNextStep,
            contentError = null,
            onKeyboardDropFunc = {
            },
            isSuccess = !hasErrorPassword.value && password.isNotEmpty()
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .noRippleClickable {
                    onResetPassword?.invoke(true)
                    viewModel.trackingClickForgerPassword()
                }, horizontalArrangement = Arrangement.Start
        ) {
            Text(
                stringResource(id = R.string.tv_forgot_password),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontFamily = MontserratFont,
                    color = Color_LiveStream_Main_Color,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryLiveButton(
            modifier = Modifier.fillMaxWidth(),
            stringResource(id = R.string.tv_login),
            enable = enableButton,
            onClickFunc = {
                coroutineScope.launch {
                    val phoneFormat = getFormatPhone(phone)
                    val finalPhone = viewModel.countryCodeSelected.value.plus(phoneFormat).trim()
                    viewModel.loginWithUserPass(finalPhone, password, TrackingConstants.TRACKING_VALUE_SIGNIN_PHONE_NUMBER)
                }
                isNextStep = true
            }
        )
        SpaceCompose(height = 16.dp)
        SecondLiveButton(text = stringResource(id = R.string.tv_login_by_mail), onClickFunc = {
            navController?.popBackStack()
        })
        SpaceCompose(height = 20.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Text(
                text = " Or ", style = TextStyle(
                    color = Neutral_Gray_9,
                    fontFamily = MontserratFont
                )
            )
        }
        SpaceCompose(height = 20.dp)
        SecondLiveButton(
            text = stringResource(id = R.string.text_action_signin_by_google),
            iconLeft = R.drawable.olmo_ic_google_no_border_purple,
            onClickFunc = {
                onGoogleClick.invoke()
            }
        )
        SpaceCompose(height = 20.dp)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = provideAppVersion(), style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.Normal,
            ))
        }
    }
    val navigationToOTPVerify = viewModel.navigationToOTPVerify.collectAsState()
    if(navigationToOTPVerify.value){
        LaunchedEffect(true){
            val identity = phone
            val userName = ""
            val phoneFormat = getFormatPhone(identity)
            val finalPhoneNumber = viewModel.countryCodeSelected.value.plus(phoneFormat)
            val verifyCodeWrapper = VerifyCodeWrapper(identity = finalPhoneNumber, userName = userName, userType = userType.value, isPhone = true)
            navController?.navigate(
                ScreenName.VerifyCodeScreen.route
                    .plus("?defaultData=${verifyCodeWrapper.toJson()}")
            )
            viewModel.resetNavigation()
        }
    }
}

@Preview
@Composable
fun TextFiledBase(hint: String?, onTextChanged: (text: String) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        border = BorderStroke(1.dp, Color(0xFFDDDFE3)),
        shape = RoundedCornerShape(10.dp)
    ) {
        val focusManager = LocalFocusManager.current
        var text by remember {
            mutableStateOf(TextFieldValue(""))
        }
        TextField(
            value = text,
            onValueChange = {
                text = it
                onTextChanged(text.text)
            },
            placeholder = {
                Text(
                    text = hint.orEmpty(),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Medium,
                        fontFamily = MontserratFont,
                        color = Color(0xFFAEB1B7)
                    )
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                textColor = Color(0xFF303037),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
        )
    }
}


