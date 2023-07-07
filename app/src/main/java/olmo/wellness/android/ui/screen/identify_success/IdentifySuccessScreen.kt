package olmo.wellness.android.ui.screen.identify_success

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.components.ButtonConfirmCompose
import olmo.wellness.android.ui.common.components.DividerWithElevation
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun IdentifySuccessScreen(navController: NavController) {
    Box(modifier = Modifier
        .fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(sizeIcon_40)
                    .padding(top = padding_12),
                painter = painterResource(id = R.drawable.ic_check_green),
                contentDescription = "verification step 1 success"
            )
            Text(
                text = stringResource(id = R.string.thank_for_request),
                color = Success_500,
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = MontserratFont,
                    lineHeight = heightText_26
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = padding_20)
            )
            Text(
                text = stringResource(id = R.string.verification_1_desc),
                color = Neutral_Gray_9,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    start = padding_20,
                    end = padding_20,
                    bottom = padding_20
                ),
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)){
            DividerWithElevation()
            SpaceCompose(height = 10.dp)
            Row(modifier = Modifier.padding(bottom = marginDouble)) {
                ButtonConfirmCompose(stringResource(R.string.next), onSubmit = {
                    navController.navigate(ScreenName.ProfileSettingScreen.route)
                })
            }
        }
    }
}
