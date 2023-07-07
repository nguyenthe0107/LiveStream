package olmo.wellness.android.ui.screen.home_screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.MontserratFont

@Composable
fun HomeScreen(
    navController: NavController
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Transparent)){
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Home", style = MaterialTheme.typography.body1,
                fontFamily = MontserratFont,
                color = Color.Black)

            Button(onClick = {
                navController.navigate(ScreenName.LivestreamPOCScreen.route)
            }){
                Text("Goto Livestream")
            }
            Box{
                Row(modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        navController.navigate(ScreenName.ProfileSettingScreen.route)
                    }) {
                    Text(text = "ProfileSettingScreen", style = MaterialTheme.typography.body1,
                        fontFamily = MontserratFont,
                        color = Color.Black)
                }
            }
            SpaceCompose(height = 20.dp)
            Box{
                Row(modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        val isDeepLink = false
                        navController.navigate(ScreenName.SignInEmailScreen.route + "/$isDeepLink")
                    }) {
                    Text(text = "SignIn-Screen", style = MaterialTheme.typography.body1,
                        fontFamily = MontserratFont,
                        color = Color.Black)
                }
            }
            SpaceCompose(height = 20.dp)
            Box{
                Row(modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
//                        navController.navigate(ScreenName.TestComposeScreen.route)
                        navController.navigate(ScreenName.StatisticsLiveStreamScreen.route)
                    }) {
                    Text(text = "Test-Screen", style = MaterialTheme.typography.body1,
                        fontFamily = MontserratFont,
                        color = Color.Black)
                }
            }
            SpaceCompose(height = 20.dp)
            Box{
                Row(modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        navController.navigate(ScreenName.AccountSwitcherScreen.route)
                    }) {
                    Text(text = "AccountSetting", style = MaterialTheme.typography.body1,
                        fontFamily = MontserratFont,
                        color = Color.Black)
                }
            }

            SpaceCompose(height = 20.dp)
            Box{
                Row(modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        sharedPrefs.logOut()
                        val isDeepLink = false
                        navController.navigate(ScreenName.SignInEmailScreen.route + "/$isDeepLink")
                    }) {
                    Text(text = "Logout", style = MaterialTheme.typography.body1,
                        fontFamily = MontserratFont,
                        color = Color.Black)
                }
            }
        }
    }
}