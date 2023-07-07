package olmo.wellness.android.ui.screen.verify_code_screen

import android.annotation.SuppressLint
import android.content.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.Color_Green_Main
import olmo.wellness.android.ui.theme.MontserratFont
import olmo.wellness.android.ui.theme.Neutral_Gray_7
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@SuppressLint("NewApi", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun VerifyCodeSuccessSlide1Screen(
    navController: NavController,
    identity: String? = "",
    username: String? = ""){
    Scaffold(
        topBar = {}){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, end = 25.dp)
            .noRippleClickable { navController.navigate(ScreenName.SellerHubScreen.route) }) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Let's Get\nStarted", style =
                    MaterialTheme.typography.h5,
                    fontFamily = MontserratFont,
                    fontSize = 34.sp,
                    lineHeight = 43.sp,
                    color = Color_Green_Main,
                    textAlign = TextAlign.Center
                )
                SpaceCompose(height = 11.dp)
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Neutral_Gray_9, fontWeight = FontWeight.Normal )) {
                                append("Hello, ")
                            }
                            withStyle(style = SpanStyle(color = Neutral_Gray_9, fontWeight = FontWeight.Bold )) {
                                append("$identity")
                            }
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Neutral_Gray_9)) {
                                append(" welcome to Olmo Seller Hub.")
                            }
                        },
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption,
                    )
                }

            }
        }
    }
}


