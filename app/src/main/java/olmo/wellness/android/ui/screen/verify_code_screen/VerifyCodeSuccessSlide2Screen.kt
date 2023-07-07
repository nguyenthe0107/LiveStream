package olmo.wellness.android.ui.screen.verify_code_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@SuppressLint("NewApi", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun VerifyCodeSuccessSlide2Screen(
    navController: NavController,
    identity: String? = "",
    username: String? = "",
    userType: String? = UserTypeModel.BUYER.value){
    Scaffold{
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(start = 25.dp, end = 25.dp)
            .noRippleClickable { navController.navigate(ScreenName.SellerHubScreen.route) }) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                val image = painterResource(id = R.drawable.img_slide_2)
                Image(painter = image, contentDescription = "")
                SpaceCompose(28.dp)
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Neutral_Gray_9, fontWeight = FontWeight.Normal )) {
                                append("Before starting your business, please continue to set up your")
                            }
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Neutral_Gray_9)) {
                                append(" Seller Identity Verification")
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
