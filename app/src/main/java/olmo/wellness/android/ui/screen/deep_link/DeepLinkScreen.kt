package olmo.wellness.android.ui.screen.deep_link

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import olmo.wellness.android.R
import olmo.wellness.android.core.enums.VeificationStatus
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.theme.*

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class
)
@Composable
fun DeepLinkScreen(
    businessIdInput: Int? = 0,
    status: String? = null,
    navController: NavController,
    viewModel: DeepLinkViewModel = hiltViewModel()){
    val idToken = sharedPrefs?.getToken()
    val isLogin = sharedPrefs?.getStatusLogin() ?: false
    if (idToken?.isNotEmpty() == true && isLogin) {
        viewModel.setBusinessId(businessIdInput)
        val isSuccess by viewModel.isSuccess.collectAsState()
        val businessOwn = viewModel.businessOwn.collectAsState()
        val loadingValue = viewModel.isLoading.collectAsState()
        val isLoading = loadingValue.value
        Scaffold(
            topBar = {
                DetailTopBar(
                    stringResource(R.string.seller_identify_verification_title),
                    navController,
                    White,
                    onBackStackFunc = {}
                )
            }) {
            Column(modifier = Modifier.background(color = Color.White)) {
                LaunchedEffect(true){
                    if(status == VeificationStatus.Success.status){
                        navController.navigate(ScreenName.AuthenticatorScreen.route)
                    }else{
                        val empty = ""
                        navController.navigate(
                            ScreenName.SellerVerificationStep1Screen.route
                                .plus("?identity=$empty")
                                .plus("&businessId=${businessIdInput}")
                        )
                    }
                }
            }
            LoadingScreen(isLoading = isLoading)
        }
    } else {
        var statusResult by remember {
            mutableStateOf("")
        }
        if(statusResult.isEmpty()){
            LaunchedEffect(true){
                val isDeepLink = true
                navController.navigate(ScreenName.SignInEmailScreen.route + "/$isDeepLink")
            }
        }
        navController.currentBackStackEntry
            ?.savedStateHandle?.getLiveData<String>("data")
            ?.observe(LocalLifecycleOwner.current) { result ->
                // Do something with the result.
                statusResult = result
                if(result != null){
                    if(status == VeificationStatus.Success.status){
                        navController.navigate(ScreenName.AuthenticatorScreen.route)
                    }else{
                        navController.navigate(ScreenName.SellerVerificationStep1Screen.route)
                    }
                }
            }
    }
}