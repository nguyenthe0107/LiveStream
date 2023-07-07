package olmo.wellness.android.ui.screen.signup_screen.onboard_signup

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.insets.ui.Scaffold
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
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.Constants.LINK_PRIVACY
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.definition.AuthMethod
import olmo.wellness.android.data.model.definition.SpecialError
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.defination.SignUpBottomSheetType
import olmo.wellness.android.domain.model.defination.UserExistsType
import olmo.wellness.android.domain.model.wrapper.VerifyCodeWrapper
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.ProxyViewCompose
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.extensions.showMessageResource
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.category_screen.CategoryScreen
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.mail.SignUpScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.provideAppVersion
import olmo.wellness.android.ui.screen.user_registed.UserRegisteredScreen
import olmo.wellness.android.ui.screen.user_type_screen.UserTypeScreen
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun OnBoardSignupScreen(
    navController: NavController,
    viewModel: OnBoardSignUpViewModel = hiltViewModel()
) {
    val isLoading = viewModel.isLoading.collectAsState()
    val typeBottomSheetSelected = remember {
        mutableStateOf(SignUpBottomSheetType.UN_KNOW)
    }
    val userType by remember {
        mutableStateOf<UserTypeModel?>(null)
    }
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                viewModel.resetUserTypeLocal()
            }
            else -> {}
        }
    }
    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )
    var phoneMode by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val navigationToSelectToUserType = viewModel.navigationSelectUserType.collectAsState()
    if (navigationToSelectToUserType.value) {
        LaunchedEffect(Unit) {
            typeBottomSheetSelected.value = SignUpBottomSheetType.USER_TYPE
            scope.launch {
                modalBottomSheetState.show()
            }
        }
    }

    val isAccountNotExists = viewModel.isErrorAccountNotExist.collectAsState()
    if (isAccountNotExists.value) {
        LaunchedEffect(Unit) {
            typeBottomSheetSelected.value = SignUpBottomSheetType.USER_REGISTERED
            scope.launch {
                modalBottomSheetState.show()
            }
        }
    }

    val error = viewModel.errorBody.collectAsState()
    val context = LocalContext.current
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        modifier = Modifier.fillMaxSize(),
        sheetShape = RoundedCornerShape(topStart = space_30, topEnd = space_30),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp)
            ) {
                when (typeBottomSheetSelected.value) {
                    SignUpBottomSheetType.MAIN_SIGN_UP -> {
                        SignUpScreen(
                            modalBottomSheetState,
                            navController,
                            phoneMode,
                            onNavigationSelectUserType = { status ->
                                if (status) {
                                    typeBottomSheetSelected.value = SignUpBottomSheetType.USER_TYPE
                                    scope.launch {
                                        modalBottomSheetState.show()
                                    }
                                }
                            }, onNavigationUserRegister = { status ->
                                if (status) {
                                    typeBottomSheetSelected.value =
                                        SignUpBottomSheetType.USER_REGISTERED
                                    scope.launch {
                                        modalBottomSheetState.show()
                                    }
                                }
                            })
                    }

                    SignUpBottomSheetType.USER_TYPE -> {
                        UserTypeScreen(
                            modalBottomSheetState,
                            navController,
                            onNavigationSelectCategory = { status, userTypeCallBack ->
                                if (status) {
                                    typeBottomSheetSelected.value =
                                        SignUpBottomSheetType.SELECT_CATEGORY
                                    userType?.value = userTypeCallBack.value
                                    scope.launch {
                                        modalBottomSheetState.show()
                                    }
                                }
                            })
                    }

                    SignUpBottomSheetType.SELECT_CATEGORY -> {
                        CategoryScreen(
                            modalBottomSheetState,
                            onSelectedCategory = { status ->
                                if (status) {
                                    if (navigationToSelectToUserType.value) {
                                        viewModel.registerUserByGoogleFinalStep()
                                    } else {
                                        typeBottomSheetSelected.value =
                                            SignUpBottomSheetType.MAIN_SIGN_UP
                                        scope.launch {
                                            modalBottomSheetState.show()
                                        }
                                    }
                                }
                            })
                    }

                    SignUpBottomSheetType.USER_REGISTERED -> {
                        UserRegisteredScreen(onCallBackFunc = { status ->
                            if (status) {
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                                navController.popBackStack()
                                val isDeepLink = false
                                navController.navigate(ScreenName.SignInEmailScreen.route + "/$isDeepLink")
                            }
                        }, userType = UserExistsType.USER_REGISTERED)
                    }

                }
            }
        }
    ) {
        Scaffold {
            Column(modifier = Modifier.fillMaxSize()) {
                val coroutineScope = rememberCoroutineScope()
                val mFirebaseAuth = FirebaseAuth.getInstance()
                mFirebaseAuth.signOut()
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(), horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_ellipse),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(top = 80.dp)
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(), horizontalArrangement = Arrangement.Center
                            ) {
                                AsyncImage(
                                    model = R.drawable.olmo_img_welcome_signup_png,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = marginDouble, end = marginDouble),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.text_welcome_signup),
                                style = MaterialTheme.typography.h6.copy(
                                    color = Neutral_Gray_9,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 30.sp
                                )
                            )
                            SpaceCompose(height = 17.dp)
                            Text(
                                text = stringResource(id = R.string.text_des_welcome_signup),
                                style = MaterialTheme.typography.caption.copy(
                                    color = Neutral_Gray_9,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                            SpaceCompose(height = 25.dp)
                            SecondLiveButton(
                                text = stringResource(id = R.string.text_action_login_by_email),
                                iconLeft = R.drawable.olmo_ic_email_no_border_purple,
                                onClickFunc = {
                                    phoneMode = false
                                    typeBottomSheetSelected.value =
                                        SignUpBottomSheetType.MAIN_SIGN_UP
                                    scope.launch {
                                        modalBottomSheetState.show()
                                    }
                                })
                            SpaceCompose(height = 20.dp)

                            SecondLiveButton(
                                text = stringResource(id = R.string.text_action_login_by_phone),
                                iconLeft = R.drawable.olmo_ic_phone_purple,
                                onClickFunc = {
                                    typeBottomSheetSelected.value =
                                        SignUpBottomSheetType.MAIN_SIGN_UP
                                    phoneMode = true
                                    scope.launch {
                                        modalBottomSheetState.show()
                                    }
                                }
                            )
                            Text(
                                text = stringResource(id = R.string.text_or_general),
                                modifier = Modifier.padding(top = 11.dp, bottom = 11.dp),
                                textAlign = TextAlign.Center,
                                color = Neutral_Gray_9,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                fontFamily = MontserratFont
                            )

                            SecondLiveButton(
                                text = stringResource(id = R.string.text_action_login_by_google),
                                iconLeft = R.drawable.olmo_ic_google_no_border_purple,
                                onClickFunc = {
                                    startForResult.launch(signInClient.signInIntent)
                                    viewModel.sendTrackingGmail()
                                }
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 34.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.tv_des_have_account),
                                style = MaterialTheme.typography.body2.copy(
                                    fontFamily = MontserratFont,
                                    color = Neutral_Gray_9,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp
                                )
                            )
                            Spacer(modifier = Modifier.padding(end = 5.dp))
                            Text(
                                text = stringResource(id = R.string.text_action_login),
                                style = MaterialTheme.typography.body2.copy(
                                    fontFamily = MontserratFont,
                                    color = Color_LiveStream_Main_Color,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                ),
                                modifier = Modifier.noRippleClickable {
                                    val isDeepLink = false
                                    navController.navigate(ScreenName.SignInEmailScreen.route + "/$isDeepLink")
                                }
                            )
                        }
                        Spacer(modifier = Modifier.padding(top = 16.dp))
                        ProxyViewCompose(openPrivacy = {
                            val linkWebView = Constants.LINK_PRIVACY
                            navController.navigate(
                                ScreenName.PrivacyScreen.route.plus(
                                    "?defaultData=$linkWebView"
                                )
                            )
                        })
                        Spacer(modifier = Modifier.padding(top = 16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            androidx.compose.material.Text(
                                text = provideAppVersion(),
                                style = MaterialTheme.typography.subtitle2.copy(
                                    fontWeight = FontWeight.Normal,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
    LoadingScreen(isLoading = isLoading.value)
    if (error.value.isNotEmpty()) {
        if (error.value == SpecialError.USER_REGISTERED.nameError) {
            //showMessage(context, error.value)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.navigationToHome.collectLatest {
            if (it) {
                navController.navigate(ScreenName.OnboardLiveScreen.route)
                viewModel.resetStatus()
                viewModel.resetUserTypeLocal()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationToOTPVerify.collectLatest {
            if (it) {
                val verifyCodeWrapper = VerifyCodeWrapper(
                    identity = viewModel.identify.value,
                    userType = userType?.value,
                    authData = viewModel.authDataFirebase.value,
                    authMethod = AuthMethod.PROVIDER)
                navController.navigate(
                    ScreenName.VerifyCodeScreen.route
                        .plus("?defaultData=${verifyCodeWrapper.toJson()}")
                )
                viewModel.resetStatus()
            }
        }
    }

}

@ExperimentalMaterialApi
fun handleSignInResult(
    completedTask: Task<GoogleSignInAccount>,
    context: Context, viewModel: OnBoardSignUpViewModel, coroutineScope: CoroutineScope
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
    context: Context, viewModel: OnBoardSignUpViewModel, coroutineScope: CoroutineScope
){
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
                            viewModel.sendToken(idToken.orEmpty())
                        }
                    } else {
                        // Handle error -> task.getException()
                        showMessageResource(context, R.string.tv_login_failed)
                    }
                }
        }
        .addOnFailureListener(context) { e ->
            showMessageResource(context, R.string.tv_login_failed)
        }
}
