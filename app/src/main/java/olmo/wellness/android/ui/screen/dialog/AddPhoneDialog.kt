package olmo.wellness.android.ui.screen.dialog

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.NationCodePhoneSelected
import olmo.wellness.android.ui.theme.*

@Composable
fun AddPhoneDialog(
    openDialogCustom: MutableState<Boolean>,
    countryList: List<Country> = listOf(),
    onSubmitClick: (Int, String) -> Unit,
    onCancelClick: () -> Unit
) {
    if (openDialogCustom.value)
        Dialog(onDismissRequest = { openDialogCustom.value = false }) {
            AddPhoneDialogUI(countryList, onSubmitClick, onCancelClick)
        }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AddPhoneDialogUI(
    countryList: List<Country>,
    onSubmitClick: (Int, String) -> Unit,
    onCancelClick: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var phoneNationId by remember { mutableStateOf(0) }
    var validatePhone by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(marginStandard),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        elevation = defaultShadow
    ) {
        Column(
            Modifier
                .padding(horizontal = marginDouble)
                .fillMaxWidth()
                .background(White)
        ) {
            NationCodePhoneSelected(
                titleText = stringResource(id = R.string.add_more_mobile_number),
                dropdownList = countryList,
                isError = validatePhone
            ) { selectedNationId, phoneContact ->
                phone = phoneContact
                validatePhone = !Patterns.PHONE.matcher(phoneContact).matches()
                phoneNationId = selectedNationId
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
                        if (!validatePhone)
                            onSubmitClick(phoneNationId, phone)
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
