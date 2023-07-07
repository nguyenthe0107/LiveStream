package olmo.wellness.android.ui.screen.profile_dashboard.business_location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.ui.common.AutoCompleteTextView
import olmo.wellness.android.ui.theme.*

@Composable
fun BusinessLocationBottomSheet(countryList: List<Country>, countrySelect: Country ?= null,
                                onSelectedCountry: ((Country) -> Unit) ?= null){
    val businessLocationCountrySelected = remember {
        mutableStateOf<Country?>(countrySelect)
    }
    Column(
        modifier = Modifier
            .requiredHeight(
                450.dp
            )
            .background(White)){
        /* Selection Select-country */
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = padding_12, vertical = marginDouble), horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = stringResource(R.string.business_location),
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
                color = Neutral_Gray_9,
                modifier = Modifier
                    .padding(horizontal = marginDouble),
                textAlign = TextAlign.Center
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .background(Neutral_Gray_4)
                .height(defaultHeightLine)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()){
            var textSearch by remember {
                mutableStateOf("")
            }
            var addressPlaceItemPredictions by remember {
                mutableStateOf<List<Country?>>(emptyList())
            }
            addressPlaceItemPredictions = countryList
            AutoCompleteTextView(
                modifier = Modifier.fillMaxWidth(),
                query = textSearch,
                queryLabel = stringResource(id = R.string.search_country),
                onQueryChanged = { updatedAddress ->
                    textSearch = updatedAddress
                    addressPlaceItemPredictions =
                        addressPlaceItemPredictions.filter {
                            it?.name?.contains(
                                textSearch,
                                true
                            ) == true
                        }
                },
                predictions = if (textSearch.isEmpty()) {
                    countryList
                } else {
                    addressPlaceItemPredictions.filter {
                        it?.name?.contains(
                            textSearch,
                            true
                        ) == true
                    }
                },
                onClearClick = {
                    textSearch = ""
                },
                onDoneActionClick = {
                },
                onItemClick = { placeItem ->
                    if (placeItem != null) {
                        onSelectedCountry?.invoke(placeItem)
                    }
                }) {
                //Define how the items need to be displayed
                val style = TextStyle(
                    color = Neutral_Gray_9,
                    fontWeight = FontWeight.Normal, fontFamily = MontserratFont
                )
                if(businessLocationCountrySelected.value != null
                    && businessLocationCountrySelected.value?.id == it?.id){
                    it?.name?.let { it1 ->
                        Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style.copy(color = Color_Green_Main) ) }
                }else{
                    it?.name?.let { it1 -> Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style) }
                }
            }
        }
        // end
    }
}