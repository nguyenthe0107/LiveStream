package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.defination.WrapAddress
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.dialog.AddAddressDialog
import olmo.wellness.android.ui.screen.dialog.AddPhoneDialog
import olmo.wellness.android.ui.theme.*

@Composable
fun DetailList(
    titleText: String,
    detailList: List<WrapAddress?>,
    countryList: List<Country> = listOf(),
    isAddressDetail: Boolean = true,
    descButtonAdd: String,
    paddingHorizontal: Dp = marginDouble,
    paddingVertical: Dp = marginStandard,
    onSubmit: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
    ) {
        Text(
            text = titleText,
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.subtitle2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = marginMinimum)
        )
        Column(
            Modifier
                .padding(vertical = marginMinimum)
        ) {
            detailList.forEach {
                Column(modifier = Modifier) {
                    if(it?.title?.isNotEmpty() == true){
                        Text(
                            text = it?.title,
                            color = Color.Black.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.subtitle2
                                .copy(color = Neutral_Gray_9, fontWeight = FontWeight.Bold),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.padding(vertical = marginMinimum)
                        )
                    }
                    it?.let {
                        Text(
                            text = it.addressInfo ?: "",
                            color = Color.Black.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.body2,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.padding(vertical = marginMinimum)
                        )
                    }
                }
            }
        }
        val openDialog = remember { mutableStateOf(false) }
        if (isAddressDetail) {
            AddAddressDialog(
                openDialogCustom = openDialog,
                onCancelClick = { openDialog.value = false },
                onSubmitClick = { addressTitle, addressInfo ->
                    openDialog.value = false
                    onSubmit(addressTitle, addressInfo)
                })
        } else {
            AddPhoneDialog(openDialogCustom = openDialog,
                countryList = countryList,
                onCancelClick = { openDialog.value = false },
                onSubmitClick = { phoneNationId, phoneContact ->
                    openDialog.value = false
                    onSubmit(phoneNationId.toString(), phoneContact)
                })
        }
        Row(Modifier.noRippleClickable {
            openDialog.value = true
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_add_detail),
                contentDescription = null,
                modifier = Modifier.size(sizeImage_20)
            )
            Text(
                text = descButtonAdd,
                color = Neutral_Gray_9,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = marginStandard)
            )
        }
    }
}