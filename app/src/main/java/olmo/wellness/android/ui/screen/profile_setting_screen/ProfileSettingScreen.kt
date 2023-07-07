package olmo.wellness.android.ui.screen.profile_setting_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.theme.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileSettingScreen(
    navController: NavController
) {
    Scaffold(
        topBar = { DetailTopBar(stringResource(R.string.profile_settings), navController, White) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Neutral_Gray)
                .padding(
                    vertical = marginStandard
                )
        ) {
            Text(
                text = stringResource(R.string.seller_identify_verification),
                style = MaterialTheme.typography.button,
                overflow = TextOverflow.Ellipsis,
                color = Neutral_Gray_7,
                modifier = Modifier.padding(
                    top = marginMinimum,
                    bottom = marginStandard,
                    start = marginDouble,
                    end = marginDouble
                )
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(White),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = marginDouble,
                            end = marginDouble,
                            top = marginStandard,
                            bottom = marginMinimum
                        )
                        .height(defaultHeightButton)
                        .noRippleClickable {
                            navController.navigate(ScreenName.SellerVerificationStep1Screen.route)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.seller_identify_verification_step1),
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        color = Neutral_Gray_9
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_baseline_arrow_right_24),
                        contentDescription = "",
                        modifier = Modifier
                            .size(defaultSizeImage)
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = marginDouble, vertical = marginMinimum)
                        .height(defaultHeightLine)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = marginDouble,
                            end = marginDouble,
                            top = marginStandard,
                            bottom = marginStandard
                        )
                        .alpha(0.5f)
                        .noRippleClickable {
                            navController.navigate(ScreenName.SellerVerificationStep2Screen.route)
                        }
                        .height(defaultHeightButton),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.seller_identify_verification_step2),
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        color = Neutral_Gray_9
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_baseline_arrow_right_24),
                        contentDescription = "",
                        modifier = Modifier
                            .size(defaultSizeImage)
                    )
                }
            }
        }
    }
}