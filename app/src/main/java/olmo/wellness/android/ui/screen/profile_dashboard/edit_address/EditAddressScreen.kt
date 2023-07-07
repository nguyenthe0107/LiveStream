package olmo.wellness.android.ui.screen.profile_dashboard.edit_address

import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.profile_dashboard.business_location.BusinessLocationBottomSheet
import olmo.wellness.android.ui.screen.profile_dashboard.business_location.TextFieldProfileWithError
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditAddressScreen(
    modalBottomSheetState: ModalBottomSheetState,
    onSuccess: ((Boolean) -> Unit)? = null,
    onFailed: ((Boolean) -> Unit)? = null,
    viewModel: EditAddressViewModel = hiltViewModel()){
    var validateBusinessAddressCountry by remember { mutableStateOf(false) }
    var validateBusinessAddressPostalCode by remember { mutableStateOf(false) }
    var validateBusinessAddressCity by remember { mutableStateOf(false) }
    var validateBusinessAddressLine by remember { mutableStateOf(false) }
    val addressModel = viewModel.businessAddress.collectAsState()
    val isSuccess = viewModel.isSuccess.collectAsState()
    val businessLocationCountrySelected = remember {
        mutableStateOf<Country?>(null)
    }
    val context = LocalContext.current
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                (context as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
                (context as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
            Lifecycle.Event.ON_DESTROY -> {
            }
            else -> {
            }
        }
    }

    if(modalBottomSheetState.isVisible){
        LaunchedEffect(modalBottomSheetState.isVisible){
            viewModel.reloadApi()
        }
    }
    val countryList = viewModel.countryList.collectAsState()
    val closePopup = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .background(Color_gray_FF7)
            .fillMaxHeight(0.6f)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween){
        Column(
            modifier = Modifier
                .padding(top = marginStandard),
            verticalArrangement = Arrangement.Top){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color_gray_FF7)
                    .padding(marginDouble),
                horizontalArrangement = Arrangement.Center){
                Text(
                    text = stringResource(R.string.title_my_profile_address),
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color_Purple_FBC,
                    modifier = Modifier.padding(horizontal = marginDouble),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            SpaceCompose(height = 8.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly){
                Row(modifier = Modifier
                    .weight(1.3f)
                    .fillMaxWidth()) {
                    ItemDefaultMenuProfileSelected(
                        modifier = Modifier,
                        optionTitle = stringResource(id = R.string.text_hint_optional),
                        hint = stringResource(id = R.string.select_country),
                        onChange = {
                            (context as MainActivity).showAsBottomSheet(closePopup){
                                BusinessLocationBottomSheet(countryList = countryList.value,
                                    countrySelect = businessLocationCountrySelected.value, onSelectedCountry = {
                                        businessLocationCountrySelected.value = it
                                        closePopup.value = true
                                    })
                            }
                        },
                        paddingVertical = padding_2,
                        paddingHorizontal = 4.dp,
                        isError = validateBusinessAddressCountry,
                        isErrorResubmit = false,
                        valueContent = viewModel.getCountryName().ifEmpty {
                            businessLocationCountrySelected.value?.name
                        },
                    )
                }

                if (businessLocationCountrySelected.value != null) {
                    viewModel.setBusinessAddressCountry(businessLocationCountrySelected.value?.id)
                    validateBusinessAddressCountry = false
                }

                Row(modifier = Modifier.weight(2f)) {
                    TextFieldProfileWithError(
                        isError = validateBusinessAddressPostalCode,
                        hint = stringResource(id = R.string.zip_code),
                        defaultValue = addressModel.value.zipCode,
                        paddingVertical = padding_2,
                        paddingHorizontal = 4.dp,
                        keyboardType = KeyboardType.Number){
                        viewModel.setBusinessAddressPostalCode(it)
                        validateBusinessAddressPostalCode = false
                    }
                }
            }
            
            SpaceCompose(height = 24.dp)
            
            TextFieldProfileWithError(
                isError = validateBusinessAddressCity,
                hint = stringResource(id = R.string.detail_city),
                defaultValue = addressModel.value.city,
                paddingVertical = padding_2){
                viewModel.setBusinessAddressCity(it)
                validateBusinessAddressCity = false
            }
            SpaceCompose(height = 24.dp)
            TextFieldProfileWithError(
                isError = validateBusinessAddressLine,
                hint = stringResource(id = R.string.address),
                paddingVertical = padding_2,
                defaultValue = addressModel.value.address){
                viewModel.setBusinessAddressLine1(it)
                validateBusinessAddressLine = false
            }
            SpaceCompose(height = 14.dp)
        }
        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom){
            GroupButtonBottomCompose(confirmCallback = {
                viewModel.updateAddress()
            }, cancelCallback = {
                onFailed?.invoke(true)
            }, contentConfirm = stringResource(id = R.string.save_value))
        }
    }
    LaunchedEffect(isSuccess) {
        snapshotFlow { isSuccess.value }
            .collect { status ->
                if(status){
                    onSuccess?.invoke(true)
                    viewModel.resetState()
                }
            }
    }
}