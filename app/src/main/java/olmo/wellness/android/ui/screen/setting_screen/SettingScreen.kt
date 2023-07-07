package olmo.wellness.android.ui.screen.setting_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun SettingScreen(
    navController: NavController
) {
    Scaffold(
        topBar = { DetailTopBar(stringResource(R.string.settings), navController, White) },
        bottomBar = { LogoutButton() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Neutral_Gray)
                .padding(
                    vertical = marginStandard
                )
        ) {
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
                            navController.navigate(ScreenName.ProfileSettingScreen.route)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.profile_settings),
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
                        .height(defaultHeightButton)
                        .noRippleClickable { navController.navigate(ScreenName.MyProfileDashboardScreen.route) }
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.user_management),
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

@Composable
fun LogoutButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(defaultHeightBottomBar)
            .background(White),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { },
            modifier = Modifier
                .width(defaultWidthButton)
                .height(defaultHeightButton)
                .padding(top = marginStandard),
            shape = RoundedCornerShape(
                topStart = marginStandard,
                topEnd = marginStandard,
                bottomStart = marginStandard,
                bottomEnd = marginStandard
            ),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = White
            ),
            border = BorderStroke(defaultBorderWidth, Tiffany_Blue_500)
        ) {
            Text(
                text = stringResource(R.string.log_out),
                style = MaterialTheme.typography.button,
                overflow = TextOverflow.Ellipsis,
                color = Tiffany_Blue_500
            )
        }
    }
}