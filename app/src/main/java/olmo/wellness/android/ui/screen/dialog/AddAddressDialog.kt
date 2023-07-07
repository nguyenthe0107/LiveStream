package olmo.wellness.android.ui.screen.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.TextFieldWithError
import olmo.wellness.android.ui.theme.*

@Composable
fun AddAddressDialog(
    openDialogCustom: MutableState<Boolean>,
    onSubmitClick: (String, String) -> Unit,
    onCancelClick: () -> Unit
) {
    if (openDialogCustom.value)
        Dialog(onDismissRequest = { openDialogCustom.value = false }) {
            AddAddressDialogUI(onSubmitClick, onCancelClick)
        }
}

@Composable
private fun AddAddressDialogUI(
    onSubmitClick: (String, String) -> Unit,
    onCancelClick: () -> Unit
) {
    var validateAddressInfo by remember { mutableStateOf(false) }
    var addressTitle by remember { mutableStateOf("") }
    var addressInfo by remember { mutableStateOf("") }
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
                text = stringResource(id = R.string.add_more_address),
                color = Neutral_Gray_9,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(
                        start = marginDouble,
                        end = marginDouble,
                        top = marginDouble
                    )
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            TextFieldWithError(
                titleText = stringResource(id = R.string.address_title),
                isValidateError = false,
                hint = stringResource(id = R.string.address)
            ) {
                addressTitle = it
            }

            Text(
                text = stringResource(id = R.string.address_title_decs),
                color = Neutral_Gray_7,
                style = MaterialTheme.typography.overline,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = marginDouble)
            )
            TextFieldWithError(
                titleText = stringResource(id = R.string.address_info),
                isError = validateAddressInfo,
                hint = stringResource(id = R.string.address)
            ) {
                addressInfo = it
                validateAddressInfo = it.isEmpty()
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
                    border = BorderStroke(defaultBorderWidth, Color_Green_Main)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.button,
                        overflow = TextOverflow.Ellipsis,
                        color = Color_Green_Main
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
                        backgroundColor = Color_Green_Main
                    )
                ) {
                    Text(
                        text = stringResource(R.string.ok),
                        style = MaterialTheme.typography.button,
                        overflow = TextOverflow.Ellipsis,
                        color = White
                    )
                }
            }
        }
    }
}
