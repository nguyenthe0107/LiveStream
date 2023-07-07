package olmo.wellness.android.ui.screen.verification_success

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import olmo.wellness.android.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.components.ButtonConfirmCompose
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.theme.*

@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun VerificationSummarySuccess(navController: NavController) {
    Scaffold(topBar = {
        DetailTopBar(
            "",
            navController,
            Neutral_Gray,
            zeroDimen
        )
    }) {
        val paddingParent = marginDouble
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(start = 45.dp, end = 45.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 135.dp)) {
                Text(
                    text = "Continue Registration Seller Identity Verification",
                    style = MaterialTheme.typography.h6.copy(
                        color = Neutral_Gray_9,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = padding_20),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(end = 14.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_group_sivi_green),
                            contentDescription = ""
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(start = 14.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_group_sivi_gray),
                            contentDescription = ""
                        )
                    }

                }
                Row(modifier = Modifier.padding(top = marginPent)) {
                    ButtonConfirmCompose(stringResource(R.string.next), onSubmit = {
                        navController.navigate(ScreenName.SellerVerificationStep1Screen.route)
                    }, colorEnable = Color_Green_Main)
                }
            }
        }
    }
}