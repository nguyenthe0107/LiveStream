package olmo.wellness.android.ui.screen.authenticator

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import olmo.wellness.android.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.core.utils.generateMyQRCode
import olmo.wellness.android.ui.common.CopyTextCompose
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.components.ButtonConfirmCompose
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.AuthenticationCodeViewModel
import olmo.wellness.android.ui.screen.signup_screen.utils.*
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AuthenticatorScreen(
    navController: NavController,
    viewModel: AuthenticationCodeViewModel = hiltViewModel()
) {
    val authenticator = viewModel.authenticator.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isSuccess = viewModel.isSuccess.collectAsState()
    val context = LocalContext.current
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
        val paddingParent = marginDouble
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingParent)
                    .verticalScroll(rememberScrollState())
            ) {
                /* Top */
                Text(
                    text = "Download App", style = MaterialTheme.typography.h6.copy(
                        color = Neutral_Gray_9,
                        fontFamily = MontserratFont,
                        lineHeight = heightText_28,
                        fontWeight = FontWeight.Bold
                    )
                )
                SpaceCompose(height = marginStandard)
                Text(
                    text = "Step 1",
                    style = MaterialTheme.typography.h6.copy(
                        color = Neutral_Gray_9,
                        fontFamily = MontserratFont,
                        lineHeight = heightText_24,
                        fontWeight = FontWeight.Bold
                    )
                )
                SpaceCompose(height = marginMinimum)
                Text(
                    text = "Download and install Google Authenticator",
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Neutral_Gray_7,
                        fontFamily = MontserratFont,
                        lineHeight = heightText_22,
                        fontWeight = FontWeight.Normal
                    )
                )
                SpaceCompose(height = marginDouble)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_apple_store),
                        contentDescription = ""
                    )
                    SpaceHorizontalCompose(width = space_10)
                    Image(
                        painter = painterResource(id = R.mipmap.ic_chplay),
                        contentDescription = ""
                    )
                }
                SpaceCompose(height = 20.dp)
                DividerHorizontal(thickness = 1.dp, color = Neutral)
                SpaceCompose(height = 11.dp)
                TextStyleGray(
                    content = "Scan QR Code", style = MaterialTheme.typography.h6.copy(
                        fontFamily = MontserratFont,
                        lineHeight = heightText_28,
                        fontWeight = FontWeight.Bold
                    ), colors = Neutral_Gray_9
                )
                SpaceCompose(height = 8.dp)
                Text(
                    text = "Step 2",
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Neutral_Gray_7,
                        fontFamily = MontserratFont,
                        lineHeight = heightText_24,
                        fontWeight = FontWeight.Bold
                    )
                )
                SpaceCompose(height = 4.dp)
                TextStyleGray(
                    content = "Using Authenticator to scan your personalized\n" +
                            "QR code below", style = MaterialTheme.typography.subtitle2.copy(
                        color = Neutral_Gray_9,
                        fontFamily = MontserratFont,
                        lineHeight = heightText_22,
                        fontWeight = FontWeight.Normal
                    )
                )

                SpaceCompose(height = 10.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(authenticator.value != null){
                        val bitmap = generateMyQRCode(authenticator.value!!.qrBase64Img)
                        bitmap?.asImageBitmap()?.let { it1 ->
                            Image(
                                bitmap = it1,
                                contentDescription = "", Modifier.size(200.dp, 200.dp)
                            )
                        }
                    }
                }
                SpaceCompose(height = 10.dp)
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextStyleGray(
                        content = "If you can’t scan the QR code, enter the following code into the Authenticator app",
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Neutral_Gray_9,
                            fontFamily = MontserratFont,
                            lineHeight = heightText_22,
                            fontWeight = FontWeight.Normal
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                SpaceCompose(height = 16.dp)
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    val code = authenticator.value?.secret.orEmpty()
                    CopyTextCompose(valueDefault = code)
                }
                Box(Modifier.padding(top = 20.dp)) {
                    Row(
                        Modifier
                            .align(BottomCenter)
                            .fillMaxWidth()
                    ) {
                        ButtonConfirmCompose(
                            "Next", true){
                            navController.navigate(ScreenName.AuthenticatorInputCodeScreen.route)
                        }
                    }
                }
            }
        }
    }
    LoadingScreen(isLoading = isLoading.value)
}
