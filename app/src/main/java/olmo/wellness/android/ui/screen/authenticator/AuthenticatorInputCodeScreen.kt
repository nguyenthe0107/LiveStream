package olmo.wellness.android.ui.screen.authenticator
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.AuthenticationCodeViewModel
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.theme.Neutral_Gray
import olmo.wellness.android.ui.theme.zeroDimen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AuthenticatorInputCodeScreen(navController: NavController,
    viewModel: AuthenticationCodeViewModel = hiltViewModel()) {

    val isLoading = viewModel.isLoading.collectAsState()
    val isSuccess = viewModel.isSuccess.collectAsState()

    Scaffold(topBar = {
        DetailTopBar(
            "Verification SIV1 successful",
            navController,
            Neutral_Gray,
            zeroDimen, onBackStackFunc = {
                navController.popBackStack()
            }
        )
    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            AuthenticationCodeCompose(
                onSubmitClick = { code, _ ->
                    if(code?.isNotEmpty() == true){
                        viewModel.uploadCode(code)
                    }
                },
                onCancelClick = {},
            )
        }
    }
    LaunchedEffect(true){
        viewModel.isSuccess.collect {
            if(it){
                navController.navigate(ScreenName.AccountSwitcherScreen.route)
            }
        }
    }

    LoadingScreen(isLoading = isLoading.value)
}