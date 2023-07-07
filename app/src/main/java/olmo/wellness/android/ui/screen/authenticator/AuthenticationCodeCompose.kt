package olmo.wellness.android.ui.screen.authenticator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.TextFieldWithError
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun AuthenticationCodeCompose(
    onSubmitClick: (String, String) -> Unit,
    onCancelClick: () -> Unit){
    AddAuthenticationCodeUI(onSubmitClick, onCancelClick)
}

@Composable
private fun AddAuthenticationCodeUI(
    onSubmitClick: (String, String) -> Unit,
    onCancelClick: () -> Unit
) {
    val validateAddressInfo by remember { mutableStateOf(false) }
    var addressTitle by remember { mutableStateOf("") }
    val addressInfo by remember { mutableStateOf("") }
    Card(
        shape = RoundedCornerShape(marginStandard),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent),
        elevation = defaultShadow
    ) {
        Column(
            Modifier
                .padding(horizontal = marginDouble)
                .fillMaxWidth()
                .background(White)
        ) {
            Text(
                text = stringResource(id = R.string.text_enable_verification),
                color = Neutral_Gray_9,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(
                        start = marginDouble,
                        end = marginDouble,
                        top = marginDouble
                    )
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(text = stringResource(id = R.string.text_action_enter_code_from_google_authenticator),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontWeight = FontWeight.Normal,
                    fontFamily = MontserratFont,
                    color = Neutral_Gray_7,
                    textAlign = TextAlign.Center
                ))

            SpaceCompose(height = 10.dp)

            TextFieldWithError(
                isValidateError = false,
                hint = "Code"
            ) {
                addressTitle = it
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = marginDouble, vertical = marginDouble)
                    .fillMaxWidth()
                    .height(height_60),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        onCancelClick()
                    },
                    modifier = Modifier
                        .height(height_44)
                        .weight(1f)
                        .padding(end = marginStandard),
                    shape = RoundedCornerShape(marginStandard),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = White
                    ),
                    border = BorderStroke(defaultBorderWidth, Tiffany_Blue_500)
                ) {
                    Text(
                        text = stringResource(R.string.skip),
                        style = MaterialTheme.typography.button,
                        overflow = TextOverflow.Ellipsis,
                        color = Tiffany_Blue_500
                    )
                }

                Button(
                    onClick = {
                        if (!validateAddressInfo)
                            onSubmitClick(addressTitle, addressInfo)
                    },
                    modifier = Modifier
                        .height(height_44)
                        .weight(1f),
                    shape = RoundedCornerShape(marginStandard),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Tiffany_Blue_500
                    )
                ) {
                    Text(
                        text = stringResource(R.string.next),
                        style = MaterialTheme.typography.button,
                        overflow = TextOverflow.Ellipsis,
                        color = White
                    )
                }
            }
        }
    }
}
