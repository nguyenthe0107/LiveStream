package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.steps

import android.content.Intent
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
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.defination.CountryType
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.*
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Step4Screen(
    verificationData: VerificationData?,
    businessId: Int?,
    countryList: List<Country>,
    countrySelected: MutableState<Country?>,
    modalBottomSheetState: ModalBottomSheetState,
    requestOpenDialogSearch : (Boolean, CountryType) -> Unit,
    viewModel: Step4ScreenViewModel = hiltViewModel(),
    onSubmit: (Boolean, Boolean) -> Unit
) {
    viewModel.setBusinessId(businessId)
    viewModel.setCountryList(countryList)
    viewModel.setVerificationData(verificationData)

    val context = LocalContext.current
    val error = viewModel.error.collectAsState()
    if (error.value.isNotEmpty()) {
        Toast.makeText(context, error.value, Toast.LENGTH_SHORT).show()
        onSubmit(false, false)
    }

    if (viewModel.isLoading.collectAsState().value) {
        onSubmit(true, false)
    }

    if (viewModel.isSuccess.collectAsState().value) {
        onSubmit(false, true)
        viewModel.setIsSuccess(false)
    }

    LaunchedEffect(true){
        viewModel.isResubmit()
    }

    /* Resubmit Part */
    val hasErrAccountNumber = viewModel.hasErrAccountNumber.collectAsState()
    val hasErrBankBranch = viewModel.hasErrBankBranch.collectAsState()
    val hasErrBankCountry = viewModel.hasErrBankCountry.collectAsState()
    val hasErrBankName = viewModel.hasErrBankName.collectAsState()
    val hasErrHolderName = viewModel.hasErrHolderName.collectAsState()

    val bankList = viewModel.bankInfoList.collectAsState()
    val bankyNameList = bankList.value.map { it.bankName }
    if(bankyNameList.isEmpty())
        viewModel.setBankName("")

    val validateAccountCountry by remember { mutableStateOf(false) }
    var validateBankName by remember { mutableStateOf(false) }
    var validateAccountName by remember { mutableStateOf(false) }
    var validateAccountNumber by remember { mutableStateOf(false) }
    var validateAccountBankBranch by remember { mutableStateOf(false) }
    var bankNameController: MutableState<String>? = null

    countrySelected.value?.name?.apply {
        viewModel.setCountryBank(this)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral_Gray)
    ) {

        Text(
            text = stringResource(id = R.string.billing_info),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.button,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = marginDouble, vertical = marginMinimum)
        )

        DividerView()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(
                    start = marginDouble,
                    end = marginDouble
                )
        ) {
            Text(
                text = stringResource(id = R.string.bank_account_details),
                color = Neutral_Gray_7,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = marginMinimum)
            )
        }

        ItemDefaultMenuSelected(
            titleText = stringResource(id = R.string.country),
            hint = stringResource(id = R.string.select_country), onChange = {
                requestOpenDialogSearch.invoke(true, CountryType.ADDRESS_COUNTRY_STEP3)
                scope.launch {
                    modalBottomSheetState.show()
                }
            }, paddingVertical = padding_2,
            isError = validateAccountCountry,
            isErrorResubmit = hasErrBankCountry.value,
            valueContent = if(countrySelected.value == null || (countrySelected.value?.name.isNullOrEmpty())){
                viewModel.getCountry()
            }else{
                countrySelected.value?.name
            }
        )

        val bankNameSaved = viewModel.bankNameTemp.collectAsState()

        DropdownMenuSelected(
            titleText = stringResource(id = R.string.financial_institution),
            dropdownList = bankyNameList,
            hint = stringResource(id = R.string.name),
            isError = validateBankName,
            isErrorResubmit = hasErrBankName.value,
            controller = {
                bankNameController = it
                bankNameController?.value = bankNameSaved.value
            },
            defaultValue = bankNameController?.value
        ) { bankNameSelected ->
            viewModel.setBankName(bankNameSelected)
            validateBankName = bankNameSelected.isEmpty()
        }

        TextFieldWithError(
            titleText = stringResource(id = R.string.account_holder_name),
            isError = validateAccountName,
            isErrorResubmit = hasErrHolderName.value,
            hint = stringResource(id = R.string.name),
            defaultValue = viewModel.getHolderName()
        ) {
            viewModel.setHolderName(it)
            validateAccountName = it.isEmpty()
        }

        TextFieldWithError(
            titleText = stringResource(id = R.string.bank_account_number),
            isError = validateAccountNumber,
            isErrorResubmit = hasErrAccountNumber.value,
            hint = stringResource(id = R.string.number),
            keyboardType = KeyboardType.Number,
            defaultValue = viewModel.getBankAccountNumber()
        ) {
            viewModel.setAccountNumber(it)
            validateAccountNumber = it.isEmpty()
        }

        TextFieldWithError(
            titleText = stringResource(id = R.string.bank_branch),
            isError = validateAccountBankBranch,
            isErrorResubmit = hasErrBankBranch.value,
            defaultValue = viewModel.getBankBranch()
        ) {
            viewModel.setBankBranch(it)
            validateAccountBankBranch = it.isEmpty()
        }

        CreditPaymentSelected {
        }

        ButtonNextStep(stringResource(R.string.next), viewModel.isValidate.collectAsState().value) {
            viewModel.submitStep4()
        }
    }
}