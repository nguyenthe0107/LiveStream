@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package olmo.wellness.android.ui.screen.signin_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
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
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.defination.SignUpBottomSheetType
import olmo.wellness.android.domain.model.defination.UserExistsType
import olmo.wellness.android.domain.model.wrapper.VerifyCodeWrapper
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.TrackingConstants
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.components.BasePasswordInput
import olmo.wellness.android.ui.common.components.BaseTextInput
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.common.extensions.showMessageResource
import olmo.wellness.android.ui.common.BackPressHandler
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.forget_password.ResetPasswordScreen
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signin_screen.common.TopSignInCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.*
import olmo.wellness.android.ui.screen.user_registed.UserRegisteredScreen
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignInEmailScreen(
    navController: NavController, isDeepLink: Boolean? = false,
    viewModel: SignInViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoading = viewModel.isLoading.collectAsState()
    val isAccountNotExists = viewModel.isErrorAccountNotExist.collectAsState()
    val mFirebaseAuth = FirebaseAuth.getInstance()
    if (mFirebaseAuth.currentUser != null) {
        mFirebaseAuth.signOut()
    }
    var typeBottomSheetSelected by remember {
        mutableStateOf(SignUpBottomSheetType.UN_KNOW)
    }
    val isNavigationForgetPassword = viewModel.navigationToForgetPassword.collectAsState()
    BackPressHandler {
        (context as MainActivity).finish()
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
                    handleSignInResult(
                        task,
                        context,
                        viewModel,
                        coroutineScope
                    )
                }
            }
        }

    val modalImageBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
        )

    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxHeight(),
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp)){
                when(typeBottomSheetSelected){
                    SignUpBottomSheetType.RESET_PASSWORD -> {
                        ResetPasswordScreen(modalImageBottomSheetState = modalImageBottomSheetState, navController = navController, isPhone = false)
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
                    else -> {
                    }
                }
            }
        },
        sheetState = modalImageBottomSheetState,
    ){
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
                ){
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
                    CardViewCompose(
                        onFacebookCLick = {},
                        onGoogleClick = {
                            startForResult.launch(signInClient.signInIntent)
                        },
                        navController = navController,
                        viewModel = viewModel,
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
                if (userType.value == UserTypeModel.BUYER.value) {
                    navController.navigate(ScreenName.OnboardLiveScreen.route)
                } else {
                    if (isDeepLink == true) {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("data", "true")
                        navController.popBackStack()
                    } else {
                        navController.navigate(ScreenName.OnboardLiveScreen.route)
                    }
                }
            }
        }
    }

    val errorBody = viewModel.errorBody.collectAsState()
    if (errorBody.value.isNotEmpty()) {
        if (errorBody.value != SpecialError.AUTHENTICATION_ERROR.name) {
            showMessage(
                context,
                errorBody.value
            )
        }
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

@OptIn(ExperimentalAnimationApi::class)
private fun firebaseAuthWithGoogle(
    acct: GoogleSignInAccount,
    context: Context, viewModel: SignInViewModel,
    coroutineScope: CoroutineScope
) {
    val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
    if (acct.email != null) {
        val userLocal = sharedPrefs.getUserInfoLocal().copy(identity = acct.email)
        sharedPrefs.setUserInfoLocal(userLocal)
    }
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
        .addOnFailureListener(context) { exception ->
            showMessage(
                context,
                exception.message?.toString().orEmpty()
            )
        }
}

@Composable
fun CardViewCompose(
    onGoogleClick: () -> Unit, onFacebookCLick: () -> Unit,
    navController: NavController?,
    viewModel: SignInViewModel,
    onResetPassword: ((Boolean) -> Unit) ?= null
) {
    val paddingModifier = Modifier.padding(dimensionResource(R.dimen.dimen_10dp))
    val paddingModifierText = Modifier.padding(top = dimensionResource(R.dimen.dimen_10dp))
    val navigationToOTPVerify = viewModel.navigationToOTPVerify.collectAsState()
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val isErrorEmail = viewModel.isErrorEmail.collectAsState()
    val isErrorPassword = viewModel.errorBody.collectAsState()
    val enableButton = !isErrorEmail.value && password.isNotEmpty()
    var isNextStep by remember {
        mutableStateOf(false)
    }
    val userType = viewModel.userType.collectAsState()
    val hasErrorPassword = viewModel.hasOnErrorPassword.collectAsState()
    Column(
        modifier = paddingModifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)){
        Text(
            text = stringResource(id = R.string.tv_sign_in_by_email), style =
            MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFont
            ), modifier = paddingModifierText
        )
        SpaceCompose(height = 5.dp)
        BaseTextInput(
            hint = stringResource(id = R.string.hint_email_input),
            onTextChanged = { text ->
                email = text.trim()
                isNextStep = false
                viewModel.resetState()
                viewModel.isEmailValid(email)
            },
            onKeyboardDropFunc = {
                viewModel.isEmailValid(email)
            },
            hasError = isErrorEmail.value,
            isSuccess = !isErrorEmail.value && email.isNotEmpty()
        )
        SpaceCompose(height = 16.dp)
        BasePasswordInput(
            Modifier,
            stringResource(id = R.string.hint_password_input),
            stringResource(id = R.string.hint_password_input),
            R.drawable.ic_eye_slash,
            hasError = isErrorPassword.value.isNotEmpty() &&
                    isErrorPassword.value == SpecialError.INVALID_CREDENTIALS.nameError,
            onTextChanged = { text ->
                password = text
                viewModel.resetState()
                viewModel.isValidatePassword(password)
            },
            lastInput = true,
            isNextStep = isNextStep,
            contentError = null,
            isSuccess = !hasErrorPassword.value && password.isNotEmpty()
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .noRippleClickable {
                    onResetPassword?.invoke(true)
                    viewModel.trackingClickForgerPassword()
                },
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                stringResource(id = R.string.tv_forgot_password),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontFamily = MontserratFont,
                    color = Color_LiveStream_Main_Color,
                    fontWeight = SemiBold
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryLiveButton(
            modifier = Modifier.fillMaxWidth(),
            stringResource(id = R.string.tv_login),
            enable = true, //enableButton
            onClickFunc = {
                viewModel.loginWithUserPass(
                    /*email,
                    password,*/
                    /*"seddahadegri-4563@yopmail.com",*/
                    /*"recoxebrevu-4516@yopmail.com",
                    "Aa12345@",*/
                    /*"kepler_store@yopmail.com",
                    "12345@Ab",*/
                    "bautoidem942@gmail.com",
                            "Aa12345@",
                    TrackingConstants.TRACKING_VALUE_SIGNIN_EMAIL
                )
            }
        )
        SpaceCompose(height = 16.dp)
        SecondLiveButton(text = stringResource(id = R.string.tv_login_by_phone), onClickFunc = {
            navController?.navigate(ScreenName.SignInByPhoneScreen.route)
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
                fontWeight = Normal,
            ))
        }
        SpaceCompose(height = 20.dp)
    }

    if(navigationToOTPVerify.value){
        LaunchedEffect(true){
            val identity = email
            val userName = ""
            val verifyCodeWrapper = VerifyCodeWrapper(identity = identity, userName = userName, userType = userType.value)
            navController?.navigate(
                ScreenName.VerifyCodeScreen.route
                    .plus("?defaultData=${verifyCodeWrapper.toJson()}")
            )
            viewModel.resetNavigation()
        }
    }
}



