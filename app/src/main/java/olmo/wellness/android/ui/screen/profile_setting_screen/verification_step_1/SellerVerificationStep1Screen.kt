package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.domain.model.defination.BottomSheetType
import olmo.wellness.android.domain.model.defination.CountryType
import olmo.wellness.android.domain.model.defination.StepType
import olmo.wellness.android.domain.model.defination.WrapAddress
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.AutoCompleteTextView
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.components.err_resubmit.ErrorSectionResubmitCompose
import olmo.wellness.android.ui.common.permission.RequireExternalStoragePermission
import olmo.wellness.android.ui.screen.capture_screen.CaptureScreen
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.steps.*
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class,
    coil.annotation.ExperimentalCoilApi::class
)
@Composable
fun SellerVerificationStep1Screen(
    navController: NavController,
    identity: String?=null,
    businessIdOutSide: Int?=null,
    viewModel: SellerVerificationStep1ViewModel = hiltViewModel()
) {
    val countryList = viewModel.countryList.collectAsState()
    val businessTypeList = viewModel.businessTypeList.collectAsState()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val businessId = viewModel.businessId.collectAsState()
    if(businessIdOutSide != null && businessIdOutSide != 0){
        viewModel.setBusinessId(businessIdOutSide)
    }
    val verificationInfo = viewModel.verificationInfo.collectAsState()
    val isErrorReSubmit = viewModel.errorResubmit.collectAsState()
    val addressList = remember {
        mutableStateOf<List<WrapAddress?>>(emptyList())
    }
    val phoneList = remember {
        mutableStateOf<List<WrapAddress?>>(emptyList())
    }
    val identityInput = remember {
        mutableStateOf(identity)
    }
    viewModel.setIdentityInput(identityInput.value)
    verificationInfo.value?.let { verificationData ->
        verificationData.step12?.address?.map {
            addressList.value = listOf(WrapAddress().copy(addressInfo = it.addressInfo, title = it.addressType))
        }
        verificationData.step12?.phone?.map {
            phoneList.value = listOf(WrapAddress().copy(addressInfo = it.phoneNumber))
        }
    }
    val currentStep = viewModel.currentStep.collectAsState()
    var bottomSheetType by remember {
        mutableStateOf(BottomSheetType.GALLERY)
    }
    var stepType by remember {
        mutableStateOf(StepType.STEP1)
    }
    var countryType by remember {
        mutableStateOf(CountryType.BUSINESS_COUNTRY_STEP1)
    }
    val context = LocalContext.current
    val error = viewModel.error.collectAsState()
    if (error.value.isNotEmpty()) {
        Toast.makeText(context, error.value, Toast.LENGTH_SHORT).show()
    }

    val loadingValue = viewModel.isLoading.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    isLoading = loadingValue.value
    var isLoadingForStep by remember { mutableStateOf(false) }

    val listImageSelected = remember { mutableStateOf<List<Uri?>>(emptyList()) }

    val imageSelected = remember { mutableStateOf<Uri?>(null) }
    var isCaptureDisplay by remember { mutableStateOf(false) }

    /* Country Step1 */
    val businessLocationCountrySelected = remember {
        mutableStateOf<Country?>(null)
    }

    val businessAddressCountrySelected = remember {
        mutableStateOf<Country?>(null)
    }

    /* Country Step2 */
    val countryCitizenshipSelected = remember {
        mutableStateOf<Country?>(null)
    }
    val countryIssueSelected = remember {
        mutableStateOf<Country?>(null)
    }

    /* Country Step3 */
    val countrySelectedStep3 = remember {
        mutableStateOf<Country?>(null)
    }
    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    imageSelected.value = null
                    listImageSelected.value = mutableListOf()
                }
                true
            })

    val allImagesFromGallery = viewModel.allImagesFromGallery.collectAsState()
    val permissionState = rememberPermissionState(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    if (modalBottomSheetState.isVisible) {
        if(permissionState.hasPermission){
            viewModel.loadAllImages()   
        }else{
            RequireExternalStoragePermission(navigateToSettingsScreen = { /*TODO*/ }) {
            }
        }
    }
    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxHeight()
            .background(White),
        sheetShape = RoundedCornerShape(topStart = marginStandard, topEnd = marginStandard),
        sheetContent = {
            val imageSelectedUri = remember { mutableStateOf<Uri?>(null) }
            val listImageSelectedUri = remember { mutableStateOf<List<Uri>?>(null) }
            Column(
                modifier = Modifier
                    .requiredHeight(
                        if (bottomSheetType == BottomSheetType.GALLERY) {
                            400.dp
                        } else 380.dp
                    )
                    .background(White),
                verticalArrangement = Arrangement.Top
            ) {
                /* Selection Image */
                when (bottomSheetType) {
                    BottomSheetType.GALLERY -> {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = padding_12, vertical = marginDouble)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_baseline_close_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(sizeIcon_28)
                                    .align(Alignment.CenterStart)
                                    .clickable {
                                        scope.launch {
                                            modalBottomSheetState.hide()
                                        }
                                    }
                            )
                            Text(
                                text = stringResource(R.string.select_image),
                                style = MaterialTheme.typography.body1,
                                overflow = TextOverflow.Ellipsis,
                                color = Neutral_Gray_9,
                                modifier = Modifier
                                    .padding(horizontal = marginDouble)
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.done),
                                style = MaterialTheme.typography.subtitle2,
                                overflow = TextOverflow.Ellipsis,
                                color = if (imageSelectedUri.value != null) Color_Green_Main else Neutral_Gray_3,
                                modifier = Modifier
                                    .padding(horizontal = marginDouble)
                                    .align(Alignment.CenterEnd)
                                    .noRippleClickable {
                                        scope.launch {
                                            modalBottomSheetState.hide()
                                        }
                                        imageSelected.value = imageSelectedUri.value
                                        val list = mutableListOf<Uri?>()
                                        if (imageSelectedUri.value != null) {
                                            if (stepType == StepType.STEP3) {
                                                list.add(imageSelectedUri.value)
                                                listImageSelected.value = list
                                                listImageSelected.value.distinctBy { it?.scheme }
                                            }
                                        }
                                        imageSelectedUri.value = null
                                        listImageSelectedUri.value = null
                                    }
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Neutral_Gray_4)
                                .height(defaultHeightLine)
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.padding(horizontal = padding_12),
                        ) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .size(180.dp)
                                        .noRippleClickable {
                                            isCaptureDisplay = true
                                            scope.launch {
                                                modalBottomSheetState.hide()
                                            }
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_camera),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(sizeIcon_28)
                                    )
                                    Text(
                                        text = stringResource(R.string.take_a_photo),
                                        style = MaterialTheme.typography.body2,
                                        overflow = TextOverflow.Ellipsis,
                                        color = Neutral_Gray_9,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                vertical = marginStandard, horizontal = marginDouble
                                            )
                                    )
                                }
                            }
                            items(allImagesFromGallery.value.size) { index ->
                                Box(
                                    Modifier
                                        .noRippleClickable {
                                            imageSelectedUri.value =
                                                allImagesFromGallery.value[index]
                                            listImageSelectedUri.value =
                                                listOf(allImagesFromGallery.value[index])
                                        }) {
                                    Image(
                                        painter = rememberImagePainter(
                                            data = allImagesFromGallery.value[index]
                                        ),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(180.dp)
                                    )
                                    if (imageSelectedUri.value != null && imageSelectedUri.value == allImagesFromGallery.value[index]) {
                                        Box(
                                            modifier = Modifier
                                                .size(sizeIcon_40)
                                                .align(Alignment.TopEnd)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_check_12_update),
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .size(sizeIcon_16)
                                                    .align(Alignment.Center),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    BottomSheetType.COUNTRY -> {
                        /* Selection Select-country */
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = padding_12, vertical = marginDouble)
                        ) {
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
                            addressPlaceItemPredictions = countryList.value
                            AutoCompleteTextView(
                                modifier = Modifier.fillMaxWidth(),
                                query = textSearch,
                                queryLabel = stringResource(id = R.string.search_country),
                                onQueryChanged = { updatedAddress ->
                                    textSearch = updatedAddress
                                    addressPlaceItemPredictions = addressPlaceItemPredictions.filter { it?.name?.contains(textSearch, true) == true }
                                },
                                predictions = if(textSearch.isEmpty()){
                                    countryList.value
                                }else{
                                    addressPlaceItemPredictions.filter { it?.name?.contains(textSearch, true) == true }
                                },
                                onClearClick = {
                                    textSearch = ""
                                },
                                onDoneActionClick = {
                                },
                                onItemClick = { placeItem ->
                                    when(countryType){
                                        /* Step1 */
                                        CountryType.BUSINESS_COUNTRY_STEP1 -> {
                                            businessLocationCountrySelected.value = placeItem
                                        }
                                        CountryType.ADDRESS_COUNTRY_STEP1 -> {
                                            businessAddressCountrySelected.value = placeItem
                                        }
                                        /* Step2 */
                                        CountryType.CITIZEN_COUNTRY_STEP2 -> {
                                            countryCitizenshipSelected.value = placeItem
                                        }
                                        CountryType.ISSUE_COUNTRY_STEP2 -> {
                                            countryIssueSelected.value = placeItem
                                        }
                                        /* Step3 */
                                        CountryType.ADDRESS_COUNTRY_STEP3 -> {
                                            countrySelectedStep3.value = placeItem
                                        }
                                    }
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                }){
                                    //Define how the items need to be displayed
                                val style = TextStyle(color = Neutral_Gray_9,
                                    fontWeight = FontWeight.Normal, fontFamily = MontserratFont)
                                when(countryType){
                                    /* Step1 */
                                    CountryType.BUSINESS_COUNTRY_STEP1 -> {
                                        if(businessLocationCountrySelected.value != null
                                            && businessLocationCountrySelected.value?.id == it?.id){
                                            it?.name?.let { it1 ->
                                                Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style.copy(color = Tiffany_Blue_500) ) }
                                        }else{
                                            it?.name?.let { it1 -> Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style) }
                                        }
                                    }
                                    CountryType.ADDRESS_COUNTRY_STEP1 -> {
                                        if(businessAddressCountrySelected.value != null
                                            && businessAddressCountrySelected.value?.id == it?.id){
                                            it?.name?.let { it1 ->
                                                Text(it1, fontSize = 14.sp, style = style.copy(color = Tiffany_Blue_500) ) }
                                        }else{
                                            it?.name?.let { it1 -> Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style) }
                                        }
                                    }

                                    /* Step2 */
                                    CountryType.CITIZEN_COUNTRY_STEP2 -> {
                                        if(countryCitizenshipSelected.value != null
                                            && countryCitizenshipSelected.value?.id == it?.id){
                                            it?.name?.let { it1 ->
                                                Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style.copy(color = Tiffany_Blue_500) ) }
                                        }else{
                                            it?.name?.let { it1 -> Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style) }
                                        }
                                    }
                                    CountryType.ISSUE_COUNTRY_STEP2 -> {
                                        if(countryIssueSelected.value != null
                                            && countryIssueSelected.value?.id == it?.id){
                                            it?.name?.let { it1 ->
                                                Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style.copy(color = Tiffany_Blue_500) ) }
                                        }else{
                                            it?.name?.let { it1 -> Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style) }
                                        }
                                    }

                                    /* Step3 */
                                    CountryType.ADDRESS_COUNTRY_STEP3 -> {
                                        if(countrySelectedStep3.value != null
                                            && countrySelectedStep3.value?.id == it?.id){
                                            it?.name?.let { it1 ->
                                                Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style.copy(color = Tiffany_Blue_500) ) }
                                        }else{
                                            it?.name?.let { it1 -> Text(it1, fontSize = 14.sp, lineHeight = 22.sp, style = style) }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // end
            }
        },
        sheetState = modalBottomSheetState
    ) {
        Scaffold(
            topBar = {
                DetailTopBar(
                    stringResource(R.string.seller_identify_verification_title),
                    navController,
                    White,
                    onBackStackFunc = {
                        if (currentStep.value == 1) {
                            navController.popBackStack()
                        } else {
                            viewModel.setCurrentStep(currentStep.value.minus(1))
                        }
                    }
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Neutral_Gray)
                    .padding(
                        vertical = marginStandard
                    )
                    .noRippleClickable {
                        focusManager.clearFocus()
                    }
            ) {
                item {
                    VerificationStep1Desc()
                }
                stickyHeader { Verification1StepByStep(currentStep.value) }
                /* Error Resubmit If Have */
                if(isErrorReSubmit.value.isNotEmpty()){
                    item{
                        ErrorSectionResubmitCompose(isErrorReSubmit.value)
                    }
                }
                item {
                    when (currentStep.value) {
                        1 -> Step1Screen(
                            businessId.value,
                            identityInput.value,
                            verificationInfo.value,
                            countryList.value,
                            businessTypeList.value,
                            businessLocationCountrySelected,
                            businessAddressCountrySelected,
                            modalBottomSheetState,
                            onSubmit = { loading, success, id ->
                                isLoading = loading
                                if (success) {
                                    viewModel.setCurrentStep(currentStep.value.plus(1))
                                    id?.let {
                                        viewModel.setBusinessId(it)
                                    }
                                }
                            }, requestOpenDialogSearch = { status, countryTypeInput ->
                                if (status) {
                                    bottomSheetType = BottomSheetType.COUNTRY
                                    countryType = countryTypeInput
                                }
                            })
                        2 -> Step2Screen(
                            verificationInfo.value,
                            businessId.value,
                            countryList.value,
                            modalBottomSheetState,
                            imageSelected,
                            countryCitizenshipSelected,
                            countryIssueSelected,
                            requestOpenDialogSearch = { status, countryTypeInput ->
                                if (status) {
                                    bottomSheetType = BottomSheetType.COUNTRY
                                    countryType = countryTypeInput
                                }
                            },
                            requestOpenGallery = { status, bottomSheetTypeInput ->
                                if (status) {
                                    bottomSheetType = BottomSheetType.GALLERY
                                }
                            },
                            verificationInfo.value?.step11?.businessName,
                            addressList.value,
                            phoneList.value
                        ) { loading, success ->
                            isLoading = loading
                            if (success) {
                                viewModel.setCurrentStep(currentStep.value.plus(1))
                            }
                        }
                        3 -> Step3Screen(
                            verificationInfo.value,
                            businessId.value, modalBottomSheetState,
                            listImageSelected, requestOpenGallery = { status, type, stepTypeInput ->
                                if (status) {
                                    bottomSheetType = BottomSheetType.GALLERY
                                    stepType = StepType.STEP3
                                }
                            }) { loading, success ->
                            isLoadingForStep = loading
                            if (success) {
                                viewModel.setCurrentStep(currentStep.value.plus(1))
                            }
                        }
                        4 -> Step4Screen(
                            verificationInfo.value,
                            businessId.value, countryList.value,
                            countrySelectedStep3, modalBottomSheetState, requestOpenDialogSearch = { status, countryTypeInput ->
                                if (status) {
                                    bottomSheetType = BottomSheetType.COUNTRY
                                    countryType = countryTypeInput
                                }
                        }) { loading, success ->
                            isLoading = loading
                            if (success) {
                                viewModel.setCurrentStep(currentStep.value.plus(1))
                            }
                        }
                        5 -> Step5Screen {
                            navController.navigate(ScreenName.OnboardLiveScreen.route)
                        }
                    }
                }
            }
        }
    }

    LoadingScreen(isLoading = isLoading || isLoadingForStep)
    CaptureScreen(isDisplay = isCaptureDisplay, callbackUri = {
        isCaptureDisplay = false
        it?.let {
            imageSelected.value = it
        }
        if(stepType == StepType.STEP3){
            val list  = mutableListOf<Uri?>()
            if(imageSelected.value != null){
                list.add(imageSelected.value)
                listImageSelected.value = list
                listImageSelected.value.distinctBy { it?.scheme }
            }
        }
    })
}

@Composable
fun VerificationStep1Desc() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Neutral_Gray)
    ) {
        Text(
            text = stringResource(R.string.verification_step_1_desc),
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            color = Neutral_Gray_9,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = marginStandard, horizontal = marginDouble
                )
        )
        Text(
            text = stringResource(R.string.verification_step_1_desc_sub),
            style = MaterialTheme.typography.caption,
            overflow = TextOverflow.Ellipsis,
            color = Neutral_Gray_7,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = marginDouble
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = marginDouble,
                    horizontal = marginDouble
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            VerificationDetail(
                R.drawable.ic_business_48,
                R.string.verification_step_1_business,
                paddingEnd = padding_14
            )
            VerificationDetail(
                R.drawable.ic_phone_48,
                R.string.verification_step_1_phone,
                paddingEnd = padding_14
            )
            VerificationDetail(
                R.drawable.ic_credit_card_48,
                R.string.verification_step_1_credit_cart,
                paddingEnd = marginQuad
            )
            VerificationDetail(
                R.drawable.ic_identity_cart_48,
                R.string.verification_step_1_identity,
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = marginDouble, vertical = marginStandard)
                .height(defaultHeightLine)
                .background(Neutral_Gray_3)
        )
    }
}

@Composable
fun VerificationDetail(
    drawable: Int,
    resourceString: Int,
    paddingStart: Dp = zeroDimen,
    paddingEnd: Dp = zeroDimen,
) {
    Column(
        modifier = Modifier
            .background(Neutral_Gray)
            .padding(
                top = marginStandard,
                bottom = marginStandard,
                start = paddingStart,
                end = paddingEnd
            )
            .defaultMinSize(minWidth = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
        Image(
            painter = painterResource(id = drawable),
            contentDescription = null,
            modifier = Modifier.size(sizeIcon_48)
        )
        Text(
            text = stringResource(resourceString),
            style = MaterialTheme.typography.overline,
            overflow = TextOverflow.Ellipsis,
            color = Neutral_Gray_7,
            maxLines = 3,
            modifier = Modifier
                .width(80.dp)
                .padding(vertical = marginMinimum), textAlign = TextAlign.Center
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun Verification1StepByStep(currentStep: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Neutral_Gray)
    ) {
        Text(
            text = stringResource(R.string.verification_step_1_step_by_step),
            style = MaterialTheme.typography.button,
            overflow = TextOverflow.Ellipsis,
            color = Neutral_Gray_9,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = marginStandard, horizontal = marginDouble
                ),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = marginDouble, horizontal = marginDouble
                ),
            contentAlignment = Alignment.Center
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(50.dp)
                    .defaultMinSize(minWidth = 50.dp)
                    .padding(
                        bottom = marginQuad
                    )
                    .height(defaultHeightLine)
                    .background(Neutral_Gray_5)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                items(5) { index ->
                    StepInfoState(index + 1, currentStep)
                }
            }
        }
    }
}

@Composable
fun StepInfoState(
    step: Int,
    currentStep: Int
) {
    val finalStep = 5
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minWidth = 61.dp, minHeight = 72.dp)
            .background(Neutral_Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            val stepBackground =
                if (currentStep > step || currentStep == finalStep) R.drawable.ic_step_completed else (if (currentStep == step) R.drawable.ic_step_processing else R.drawable.ic_step_not_doing)
            Image(
                painter = painterResource(stepBackground),
                contentDescription = "",
                modifier = Modifier
                    .size(sizeIcon_40)
            )
            val stepValue =
                if (currentStep > step || currentStep == finalStep) "" else step.toString()
            val stepColor = if (currentStep >= step) White else Neutral_Gray_9
            Text(
                text = stepValue,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis,
                color = stepColor,
                modifier = Modifier
                    .width(33.dp)
                    .padding(vertical = marginMinimum), textAlign = TextAlign.Center
            )
        }
        var stepNameResource = 0
        when (step) {
            1 -> stepNameResource = R.string.verification_step_1_info_business
            2 -> stepNameResource = R.string.verification_step_1_info_seller
            3 -> stepNameResource = R.string.verification_step_1_info_service
            4 -> stepNameResource = R.string.verification_step_1_info_billing
            5 -> stepNameResource = R.string.verification_step_1_info_completed
        }
        Text(
            text = stringResource(stepNameResource),
            style = MaterialTheme.typography.overline,
            overflow = TextOverflow.Ellipsis,
            color = Neutral_Gray_9,
            modifier = Modifier
                .padding(vertical = marginMinimum), textAlign = TextAlign.Center
        )
    }
}