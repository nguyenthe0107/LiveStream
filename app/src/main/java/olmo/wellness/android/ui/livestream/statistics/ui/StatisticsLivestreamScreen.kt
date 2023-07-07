package olmo.wellness.android.ui.livestream.statistics.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.theme.*

@SuppressLint("SuspiciousIndentation", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StatisticsLivestreamScreen(navController: NavController?) {
    val scope = rememberCoroutineScope()
        Scaffold(topBar = {
            navController?.let {
                DetailTopBar(
                    title = "Live Scheduling",
                    navController = it,
                    backgroundColor = White
                )
            }
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Neutral_Gray)
                    .padding(marginStandard)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_empty_live_scheduling),
                        contentDescription = "Empty live scheduling",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                    Text(
                        text = "Schedule a live video",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(vertical = marginStandard)
                    )
                    Text(
                        text = "People will be able to view details about your video and get reminders before you go live.",
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Neutral_Gray_7,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(
                            horizontal = marginStandard,
                            vertical = marginStandard
                        ),
                        textAlign = TextAlign.Center
                    )
                    Box {
                        Row(modifier = Modifier
                            .align(Alignment.Center)
                            .clickable {
//                            navController?.navigate(ScreenName.ScheduleLiveStreamScreen.route)
                            }) {
                            Text(
                                text = "Get Started",
                                style = MaterialTheme.typography.subtitle2.copy(
                                    color = Color_Green_Main,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                }
            }
        }
    }

@Preview
@Composable
fun build() {
    StatisticsLivestreamScreen(navController = null)
}