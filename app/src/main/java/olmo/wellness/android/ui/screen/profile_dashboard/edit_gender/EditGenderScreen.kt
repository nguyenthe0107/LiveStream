package olmo.wellness.android.ui.screen.profile_dashboard.edit_gender

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.defination.GenderType
import olmo.wellness.android.ui.common.components.SectionButtonDialog
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.GroupRadioButtonSelected
import olmo.wellness.android.ui.theme.*

@Composable
fun EditGenderScreen(title: Int?, content: String = "",
                       onSuccess: (status: Boolean) -> Unit,
                       onFailed: (status: Boolean) -> Unit,
                       viewModel: GenderViewModel = hiltViewModel()) {

    val maxHeight = 300.dp
    var genderSelected by remember {
        mutableStateOf(GenderType.FEMALE)
    }
    val isSuccess = viewModel.isSuccess.collectAsState()
    if(isSuccess.value){
        onSuccess.invoke(true)
        viewModel.resetState()
    }
    Column(
        modifier = Modifier
            .background(Neutral_Gray_2)
            .requiredHeight(maxHeight),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Top
        ) {
            if (title != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White)
                        .padding(marginDouble),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(title),
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        color = Neutral_Gray_9,
                        modifier = Modifier.padding(horizontal = marginDouble),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Neutral_Gray_2)
                    .height(marginStandard)
            )
            val genders = listOf(
                stringResource(id = R.string.title_male),
                stringResource(id = R.string.title_female),
                stringResource(id = R.string.title_other)
            )
            GroupRadioButtonSelected(
                titleText = stringResource(id = R.string.brand_license_title),
                selectionList = genders,
                swapPosition = true,
                hideTitle = true){
                genderSelected = GenderType.valueOf(it.uppercase())
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom){
            SectionButtonDialog(
                titleSuccess = stringResource(id = R.string.save_value),
                onSubmitClick = {
                     viewModel.updateGender(genderSelected)
                },
                onCancelClick = {
                })
        }
    }
}