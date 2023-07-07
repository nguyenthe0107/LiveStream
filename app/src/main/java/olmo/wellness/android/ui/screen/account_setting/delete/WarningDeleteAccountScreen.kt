package olmo.wellness.android.ui.screen.account_setting.delete

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.screen.profile_dashboard.MyProfileDashBoardViewModel
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.ButtonNextStep
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WarningDeleteAccountScreen(
    navController: NavController,
    viewModel: MyProfileDashBoardViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        DetailTopBar(
            title = stringResource(id = R.string.title_delete_account_warning),
            navController = navController
        )
    }) {
        Column(
            modifier = Modifier
                .background(Neutral_Gray_2)
                .padding(top = marginStandard)
                .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween){
            Column(
                modifier = Modifier
                    .background(White)
                    .padding(start = marginDouble, end = marginDouble, top = marginDouble, bottom = marginDouble)
                    .wrapContentHeight(),
                    verticalArrangement = Arrangement.Top){
                Text(
                    text = stringResource(id = R.string.header_account_warning),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Normal,
                        color = Error_500,
                        textAlign = TextAlign.Start
                    )
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Neutral_Gray_9,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append(stringResource(id = R.string.content_warning_account_warning_1))
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Neutral_Gray_9,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append(stringResource(id = R.string.content_warning_account_warning_2))
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Neutral_Gray_9
                            )
                        ) {
                            append(stringResource(id = R.string.content_warning_account_warning_3))
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Neutral_Gray_9
                            )
                        ) {
                            append(stringResource(id = R.string.content_warning_account_warning_4))
                        }
                    },
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(start = marginStandard, end = marginStandard)
                )
                /*Text(
                    text = stringResource(id = R.string.content_warning_account_warning_1),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        textAlign = TextAlign.Start
                    )
                )

                Text(
                    text = stringResource(id = R.string.content_warning_account_warning_1),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        textAlign = TextAlign.Start
                    )
                )

                Text(
                    text = stringResource(id = R.string.content_warning_account_warning_1),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        textAlign = TextAlign.Start
                    )
                )

                Text(
                    text = stringResource(id = R.string.content_warning_account_warning_1),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        textAlign = TextAlign.Start
                    )
                )*/
            }

            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                ButtonNextStep(buttonText = stringResource(id = R.string.process)) {

                }
            }
        }
    }
}