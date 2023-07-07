package olmo.wellness.android.ui.screen.verification_success

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import olmo.wellness.android.ui.screen.authenticator.AuthenticationCodeCompose

@SuppressLint("RememberReturnType")
@Composable
fun VerificationStep2Success(navController: NavController) {
    Scaffold() {
        Box(modifier = Modifier.fillMaxSize()) {
            AuthenticationCodeCompose(
                onSubmitClick = { addressTitle, addressInfo -> },
                onCancelClick = {},
            )
        }
    }
}