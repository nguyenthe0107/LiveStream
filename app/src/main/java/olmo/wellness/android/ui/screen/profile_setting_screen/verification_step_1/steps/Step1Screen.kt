package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.steps

import android.annotation.SuppressLint
import android.util.Log
import android.util.Patterns.PHONE
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.business_type.BusinessType
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.defination.CountryType
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.*
import olmo.wellness.android.ui.theme.*

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Step1Screen(
    businessIdResubmit: Int?=null,
    identityInput: String?=null,
    verificationData: VerificationData?,
    countryList: List<Country>,
    businessTypeList: List<BusinessType>,
    businessLocationCountrySelected: MutableState<Country?>,
    businessAddressCountrySelected: MutableState<Country?>,
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: Step1ScreenViewModel = hiltViewModel(),
    onSubmit: (Boolean, Boolean, Int?) -> Unit,
    requestOpenDialogSearch : (Boolean, CountryType) -> Unit
) {
    val scope = rememberCoroutineScope()
    val businessTypeNameList = businessTypeList.map { it.name }
    viewModel.setCountryList(countryList)
    viewModel.setBusinessTypeList(businessTypeList)
    viewModel.setVerificationData(verificationData)
    viewModel.setBusinessResubmitId(businessIdResubmit)
    val context = LocalContext.current
    val isSuccess = viewModel.isSuccess.collectAsState()
    val error = viewModel.error.collectAsState()
    if (error.value.isNotEmpty()) {
        Toast.makeText(context, error.value, Toast.LENGTH_SHORT).show()
        onSubmit(false, false, null)
    }

    if (viewModel.isLoading.collectAsState().value) {
        onSubmit(true, false, null)
    }

    val businessId = viewModel.businessId.collectAsState()
    if (businessId.value != null && isSuccess.value) {
        onSubmit(false, true, businessId.value)
        viewModel.setIsSuccess(false)
    }
    /* Email or Phone */
    viewModel.setIdentityInput(identityInput)
    val validateBusinessLocation by remember { mutableStateOf(false) }
    var validateBusinessType by remember { mutableStateOf(false) }
    var validateBusinessName by remember { mutableStateOf(false) }
    var validateBusinessAddressCountry by remember { mutableStateOf(false) }
    var validateBusinessAddressPostalCode by remember { mutableStateOf(false) }
    var validateBusinessAddressCity by remember { mutableStateOf(false) }
    var validateBusinessAddressDistrict by remember { mutableStateOf(false) }
    var validateBusinessAddressLine1 by remember { mutableStateOf(false) }
    var validateBusinessContact by remember { mutableStateOf(false) }

    /* Show error */
    val hasErrBusinessLocation = viewModel.hasErrBusinessLocation.collectAsState()
    val hasErrBusinessType = viewModel.hasErrBusinessType.collectAsState()
    val hasErrBusinessName = viewModel.hasErrBusinessName.collectAsState()
    val hasErrBusinessAddress = viewModel.hasErrBusinessAddress.collectAsState()
    val hasErrContact = viewModel.hasErrContact.collectAsState()
    val hasErrPhone = viewModel.hasErrPhone.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral_Gray)
    ) {

        Text(
            text = stringResource(id = R.string.business_info),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.button,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = marginDouble, vertical = marginMinimum)
        )
        Column {
            ItemDefaultMenuSelected(
                titleText = stringResource(id = R.string.business_info),
                hint = stringResource(id = R.string.select_country), onChange = {
                    requestOpenDialogSearch.invoke(true, CountryType.BUSINESS_COUNTRY_STEP1)
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                }, paddingVertical = padding_2,
                isError = validateBusinessLocation,
                isErrorResubmit = hasErrBusinessLocation.value,
                valueContent = if(businessLocationCountrySelected.value == null || businessLocationCountrySelected.value?.name.isNullOrEmpty()){
                    viewModel.getCountryName(true)
                }else{
                    businessLocationCountrySelected.value?.name
                }
            )

            if(businessLocationCountrySelected.value != null && businessLocationCountrySelected.value?.name?.isNotEmpty() == true){
                viewModel.setBusinessLocation(businessLocationCountrySelected.value?.name!!)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(vertical = marginMinimum, horizontal = marginDouble)
            ) {
                Text(
                    text = stringResource(id = R.string.tip_for_select_country),
                    color = Neutral_Gray_7,
                    style = MaterialTheme.typography.overline,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            DropdownMenuSelected(
                titleText = stringResource(id = R.string.business_type),
                dropdownList = businessTypeNameList,
                hint = stringResource(id = R.string.hint_business_type),
                defaultValue = viewModel.getBusinessType(),
                isError = validateBusinessType,
            ) {
                viewModel.setBusinessType(it)
                validateBusinessType = it.isEmpty()
            }

            TextFieldWithError(
                titleText = stringResource(id = R.string.business_name),
                defaultValue = viewModel.getBusinessName(),
                isError = validateBusinessName,
                isErrorResubmit = hasErrBusinessName.value,
                isValidateError = hasErrBusinessName.value,
                hint = stringResource(id = R.string.hint_business_name)
            ) {
                viewModel.setBusinessName(it)
                validateBusinessName = it.isEmpty()
            }

            ItemDefaultMenuSelected(
                titleText = stringResource(id = R.string.business_address),
                optionTitle = stringResource(id = R.string.text_hint_optional),
                hint = stringResource(id = R.string.select_country), onChange = {
                    requestOpenDialogSearch.invoke(true, CountryType.ADDRESS_COUNTRY_STEP1)
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                }, paddingVertical = padding_2,
                isError = validateBusinessAddressCountry,
                isErrorResubmit = hasErrBusinessAddress.value,
                valueContent = if(businessAddressCountrySelected.value == null || businessAddressCountrySelected.value?.name.isNullOrEmpty()) {
                    viewModel.getCountryName(false)
                }else{ businessAddressCountrySelected.value?.name}
            )

            if(businessAddressCountrySelected.value != null && businessAddressCountrySelected.value?.name?.isNotEmpty() == true){
                viewModel.setBusinessAddressCountry(businessAddressCountrySelected.value?.name!!)
                validateBusinessAddressCountry = false
            }

            TextFieldWithError(
                isError = validateBusinessAddressPostalCode,
                hint = stringResource(id = R.string.zip_code),
                paddingVertical = padding_2,
                keyboardType = KeyboardType.Number,
                defaultValue = viewModel.getAddressInfo()?.zipCode
            ) {
                viewModel.setBusinessAddressPostalCode(it)
                validateBusinessAddressPostalCode = false
            }

            TextFieldWithError(
                isError = validateBusinessAddressCity,
                hint = stringResource(id = R.string.detail_city),
                paddingVertical = padding_2,
                defaultValue = viewModel.getAddressInfo()?.city
            ) {
                viewModel.setBusinessAddressCity(it)
                validateBusinessAddressCity = false
            }

            TextFieldWithError(
                isError = validateBusinessAddressDistrict,
                hint = stringResource(id = R.string.district),
                paddingVertical = padding_2,
                defaultValue = viewModel.getAddressInfo()?.district
            ) {
                viewModel.setBusinessAddressDistrict(it)
                validateBusinessAddressDistrict = false
            }

            TextFieldWithError(
                isError = validateBusinessAddressLine1,
                hint = stringResource(id = R.string.address_line_1),
                paddingVertical = padding_2,
                defaultValue = viewModel.getAddressInfo()?.addressLine1
            ) {
                viewModel.setBusinessAddressLine1(it)
                validateBusinessAddressLine1 = false
            }

            TextFieldWithError(
                isError = false,
                hint = stringResource(id = R.string.address_line_2),
                paddingVertical = padding_2,
                defaultValue = viewModel.getAddressInfo()?.addressLine2
            ) {
                viewModel.setBusinessAddressLine2(it)
            }

            DividerView()

            NationCodePhoneSelected(
                titleText = stringResource(id = R.string.contact),
                dropdownList = countryList,
                isError = validateBusinessContact,
                isErrorResubmit = hasErrContact.value,
                defaultValue = viewModel.getContactInfo()?.phoneNumber
            ) { selectedNationId, phoneContact ->
                viewModel.setBusinessContact(phoneContact)
                viewModel.setBusinessContactNationId(selectedNationId)
                validateBusinessContact = !PHONE.matcher(phoneContact).matches()
            }
            ButtonNextStep(
                stringResource(R.string.next),
                viewModel.isValidate.collectAsState().value
            ) {
                viewModel.submitStep1()
            }
        }
    }
}