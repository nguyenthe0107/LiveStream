package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.steps

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.defination.BottomSheetType
import olmo.wellness.android.domain.model.defination.CountryType
import olmo.wellness.android.domain.model.defination.WrapAddress
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.domain.model.verification1.step1.ContactPhone
import olmo.wellness.android.domain.model.verification1.step2.AddressDetail
import olmo.wellness.android.ui.screen.dialog.AddIdentityImageDialog
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.*
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Step2Screen(
    verificationData: VerificationData?,
    businessId: Int?,
    countryList: List<Country>,
    modalBottomSheetState: ModalBottomSheetState,
    imageSelected: MutableState<Uri?>,
    countryCitizenshipSelected: MutableState<Country?>,
    countryIssueSelected: MutableState<Country?>,
    requestOpenDialogSearch : (Boolean, CountryType) -> Unit,
    requestOpenGallery : (Boolean, BottomSheetType) -> Unit,
    sellerName: String? = null,
    sellerAddressList: List<WrapAddress?> = emptyList(),
    sellerPhoneList: List<WrapAddress?> = emptyList(),
    viewModel: Step2ScreenViewModel = hiltViewModel(),
    onSubmit: (Boolean, Boolean) -> Unit,
) {
    val scope = rememberCoroutineScope()
    viewModel.setBusinessId(businessId)
    viewModel.setCountryList(countryList)
    viewModel.setValidateAddress(sellerAddressList.isNotEmpty())
    viewModel.setValidatePhone(sellerPhoneList.isNotEmpty())
    viewModel.setVerificationData(verificationData)
    val typeIdentity = viewModel.typeIdentity.collectAsState()
    val openUploadDialog = viewModel.isOpenUploadDialog.collectAsState()
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

    var firstImageUri by remember { mutableStateOf<Uri?>(null) }
    var secondImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectImage by remember { mutableStateOf(1) }

    imageSelected.value?.let {
        if (selectImage == 1) {
            firstImageUri = it
        } else secondImageUri = it
        viewModel.setOpenUploadDialog(true)
        imageSelected.value = null
    }
    /* Resubmit Part */
    LaunchedEffect(true) {
        viewModel.isResubmit()
    }

    // dialog upload identity
    val onReadyReadInstructionDialog = remember { mutableStateOf(true) }
    AddIdentityImageDialog(
        openDialogCustom = openUploadDialog.value,
        openInstructionUI = onReadyReadInstructionDialog.value,
        nameIdentity = typeIdentity.value,
        firstImage = firstImageUri,
        secondImage = secondImageUri,
        onReadyReadInstruction = { onReadyReadInstructionDialog.value = false },
        onSelectImageFirst = {
            requestOpenGallery.invoke(true, BottomSheetType.GALLERY)
            scope.launch {
                modalBottomSheetState.show()
            }
            viewModel.setOpenUploadDialog(false)
            selectImage = 1
        },
        onSelectImageSecond = {
            requestOpenGallery.invoke(true, BottomSheetType.GALLERY)
            scope.launch {
                modalBottomSheetState.show()
            }
            viewModel.setOpenUploadDialog(false)
            selectImage = 2
        },
        onSubmitClick = { firstImage, secondImage ->
            viewModel.setIdentityDocumentUris(listOf(firstImage, secondImage))
            viewModel.setOpenUploadDialog(false)
        },
        onCancelClick = {
            viewModel.setOpenUploadDialog(false)
        }
    )
    Step2UI(
        viewModel,
        countryList,
        listOf(firstImageUri, secondImageUri),
        sellerName,
        sellerAddressList,
        sellerPhoneList,
        countryCitizenshipSelected,
        countryIssueSelected,
        modalBottomSheetState,
        requestOpenDialogSearch
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Step2UI(
    viewModel: Step2ScreenViewModel,
    countryList: List<Country>,
    identityImages: List<Uri?> = emptyList(),
    sellerName: String? = null,
    sellerAddressList: List<WrapAddress?> = emptyList(),
    sellerPhoneList: List<WrapAddress?> = emptyList(),
    countryCitizenshipSelected: MutableState<Country?>,
    countryIssueSelected: MutableState<Country?>,
    modalBottomSheetState: ModalBottomSheetState,
    requestOpenDialogSearch : (Boolean, CountryType) -> Unit
) {

    val validateCountryCitizenship by remember { mutableStateOf(false) }
    var validateDayOfBirth by remember { mutableStateOf(false) }
    var validateIdentityNumber by remember { mutableStateOf(false) }
    var validateDayOfExpire by remember { mutableStateOf(false) }
    val validateCountryOfIssue by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    /* show Resubmit when error  */
    val hasErrCountryCitizen = viewModel.hasErrCountryCitizen.collectAsState()
    val hasErrCountryOfIssue = viewModel.hasErrCountryOfIssue.collectAsState()
    val hasErrNumberOfId = viewModel.hasErrNumberOfId.collectAsState()
    val hasErrBirthday = viewModel.hasErrBirthday.collectAsState()
    val hasErrDateExpire = viewModel.hasErrDateExpire.collectAsState()
    val hasErrTypeDocument = viewModel.hasErrTypeDocument.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral_Gray)
    ) {
        Text(
            text = stringResource(id = R.string.seller_info),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.button,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = marginDouble, vertical = marginMinimum)
        )
        Column {
            ItemDefaultMenuSelected(
                titleText = stringResource(id = R.string.country_of_citizenship),
                hint = stringResource(id = R.string.select_country), onChange = {
                    requestOpenDialogSearch.invoke(true, CountryType.CITIZEN_COUNTRY_STEP2)
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                }, paddingVertical = padding_2,
                isError = validateCountryCitizenship,
                isErrorResubmit = hasErrCountryCitizen.value,
                valueContent = if(countryCitizenshipSelected.value == null || countryCitizenshipSelected.value?.name == null ||
                    countryCitizenshipSelected.value?.name.isNullOrEmpty()){
                    viewModel.getCountryOfCitizenShip()
                }else{
                    countryCitizenshipSelected.value?.name
                }
            )

            if(countryCitizenshipSelected.value != null && countryCitizenshipSelected.value?.name?.isNotEmpty() == true){
                viewModel.setCountryOfCitizen(countryCitizenshipSelected.value?.name!!)
            }

            DateView(
                titleText = stringResource(id = R.string.date_of_birth),
                isError = validateDayOfBirth,
                isErrorResubmit = hasErrBirthday.value,
                hint = stringResource(id = R.string.hint_date),
                defaultValue = viewModel.getDateOfBirth(),
            ) {
                viewModel.setDateOfBirth(it)
                validateDayOfBirth = it.isEmpty()
            }
            val identityUploadList = viewModel.identityDocumentUris.collectAsState()
            UploadIdentityForm(
                titleText = stringResource(id = R.string.proof_of_identity),
                isError = validateIdentityNumber,
                isErrorResubmit = hasErrTypeDocument.value || hasErrNumberOfId.value,
                imageList = if (identityUploadList.value.isNotEmpty()) identityImages else emptyList(),
                defaultImageList = viewModel.getIdentityURI(),
                defaultIdentity = viewModel.getIdentityObject(),
                defaultNumberIdentity = viewModel.getIdentity()?.identityNumber ?: "",
                onChange = { identityType, number ->
                    viewModel.setTypeIdentity(identityType.type)
                    viewModel.setIdentityNumber(number)
                    validateIdentityNumber = number.isEmpty()
                },
                onUploadClick = {
                    viewModel.setOpenUploadDialog(true)
                })

            DateView(
                titleText = stringResource(id = R.string.date_of_expire),
                isError = validateDayOfExpire,
                isErrorResubmit = hasErrDateExpire.value,
                isExpireDate = true,
                hint = stringResource(id = R.string.hint_date),
                defaultValue = viewModel.getDateOfExpiry()
            ) {
                viewModel.setIdentityDateOfExpiration(it)
                validateDayOfExpire = it.isEmpty()
            }

            ItemDefaultMenuSelected(
                titleText = stringResource(id = R.string.country_of_issue),
                hint = stringResource(id = R.string.select_country), onChange = {
                    requestOpenDialogSearch.invoke(true, CountryType.ISSUE_COUNTRY_STEP2)
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                }, paddingVertical = padding_2,
                isError = validateCountryOfIssue,
                isErrorResubmit = hasErrCountryOfIssue.value,
                valueContent = if(countryIssueSelected.value == null || countryIssueSelected.value?.name.isNullOrEmpty()){
                    viewModel.getCountryOfIssue()
                }else{
                    countryIssueSelected.value?.name
                }
            )

            if(countryIssueSelected.value != null && countryIssueSelected.value?.name?.isNotEmpty() == true){
                viewModel.setIdentityCountryOfIssue(countryIssueSelected.value?.name!!)
            }

            DividerView()

            val sellerAddressNewList = remember(sellerAddressList) {
                mutableStateOf(sellerAddressList)
            }
            if(viewModel.getResidentialAddress().isNotEmpty()){
                sellerAddressNewList.value = viewModel.getResidentialAddress()
            }
            DetailList(
                titleText = stringResource(id = R.string.residential_address),
                detailList = sellerAddressNewList.value,
                descButtonAdd = stringResource(id = R.string.add_more_address)
            ) { addressType, addressInfo ->
                viewModel.setAddress(AddressDetail(addressType, addressInfo))
                val list = ArrayList(sellerAddressNewList.value)
                list.add(WrapAddress().copy(title = addressType, addressInfo = addressInfo))
                sellerAddressNewList.value = list
            }

            DividerView()

            val sellerPhoneNewList = remember(sellerPhoneList) {
                mutableStateOf(sellerPhoneList)
            }
            if(viewModel.getMobileNumber().isNotEmpty()){
                sellerPhoneNewList.value = viewModel.getMobileNumber()
            }
            DetailList(
                titleText = stringResource(id = R.string.mobile_number),
                detailList = sellerPhoneNewList.value,
                isAddressDetail = false,
                countryList = countryList,
                descButtonAdd = stringResource(id = R.string.add_more_mobile_number)
            ) { countryId, phoneNumber ->
                viewModel.setPhone(ContactPhone(countryId.toInt(), phoneNumber))
                val list = ArrayList(sellerPhoneNewList.value)
                list.add(WrapAddress().copy(title = "", addressInfo = phoneNumber))
                sellerPhoneNewList.value = list
            }
            ButtonNextStep(
                stringResource(R.string.next),
                viewModel.isValidate.collectAsState().value
            ) {
                viewModel.submitStep2()
            }
        }
    }
}