package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_2

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.enums.DocumentTypeUri
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.common.components.ButtonConfirmCompose
import olmo.wellness.android.ui.screen.dialog.AddDocumentDialog
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.UploadIdentityWithHeaderForm
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.TextStyleBlack
import olmo.wellness.android.ui.theme.*


@SuppressLint("MutableCollectionMutableState", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun SellerVerificationStep2Screen(
    navController: NavController,
    viewModel: SellerVerificationStep2ViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val error = viewModel.error.collectAsState()
    if (error.value.isNotEmpty()) {
        Toast.makeText(context, error.value, Toast.LENGTH_SHORT).show()
    }

    val loadingValue = viewModel.isLoading.collectAsState()
    val imageSelected = remember { mutableStateOf<Uri?>(null) }

    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    imageSelected.value = null
                }
                true
            })

    val allImagesFromGallery = viewModel.allImagesFromGallery.collectAsState()
    if (modalBottomSheetState.isVisible) {
        viewModel.loadAllImages()
    }

    LaunchedEffect(Unit){
        viewModel.isSuccess.collect {
            if (it){
                navController.navigate(ScreenName.IdentifySuccessScreen.route)
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
            Column(
                modifier = Modifier
                    .requiredHeight(400.dp)
                    .background(White),
                verticalArrangement = Arrangement.Top
            ) {
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
                        color = if (imageSelectedUri.value != null) Tiffany_Blue_500 else Neutral_Gray_3,
                        modifier = Modifier
                            .padding(horizontal = marginDouble)
                            .align(Alignment.CenterEnd)
                            .noRippleClickable {
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                                imageSelected.value = imageSelectedUri.value
                                imageSelectedUri.value = null
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
                                    imageSelectedUri.value = allImagesFromGallery.value[index]
                                }) {
                            Image(
                                painter = rememberImagePainter(
                                    data = allImagesFromGallery.value[index]
                                ),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(180.dp)
                            )
                            if (imageSelectedUri.value != null && imageSelectedUri.value == allImagesFromGallery.value[index])
                                Box(
                                    modifier = Modifier
                                        .size(sizeIcon_40)
                                        .align(Alignment.TopEnd)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_image_check_12),
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
        },
        sheetState = modalBottomSheetState
    ) {
        Scaffold(
            topBar = {
                DetailTopBar(
                    stringResource(R.string.seller_identify_verification_title),
                    navController,
                    White
                )
            }
        ) {
            /*  dialog upload identity  */
            var documentBrand by remember { mutableStateOf<Uri?>(null) }
            var documentBusiness by remember { mutableStateOf<Uri?>(null) }
            var documentElectric by remember { mutableStateOf<Uri?>(null) }
            var documentHistory by remember { mutableStateOf<Uri?>(null) }
            var documentCachedUri by remember { mutableStateOf<MutableMap<DocumentTypeUri?, Uri?>>(HashMap()) }
            val openUploadDialog = remember { mutableStateOf(false) }
            val requestUploadDialog = remember { mutableStateOf<DocumentTypeUri?>(null)}
            val onReadyReadInstructionDialog = remember { mutableStateOf(true) }
            var _isFullDocumentType by remember {
                mutableStateOf(false)
            }
            _isFullDocumentType = documentBrand != null && documentBusiness != null && documentElectric != null &&
                    documentHistory != null
            AddDocumentDialog(
                openDialogCustom = openUploadDialog,
                openInstructionUI = onReadyReadInstructionDialog.value,
                documentUri = documentCachedUri[requestUploadDialog.value],
                onReadyReadInstruction = { onReadyReadInstructionDialog.value = false },
                onSubmitClick = { uri, url ->
                    openUploadDialog.value = false
                    if(requestUploadDialog.value != null){
                        documentCachedUri[requestUploadDialog.value] = uri
                        viewModel.setDocumentUrl(requestUploadDialog.value,url)
                        when(requestUploadDialog.value){
                            DocumentTypeUri.BusinessLicense -> {
                                documentBusiness = uri
                            }
                            DocumentTypeUri.BrandLicense -> {
                                documentBrand = uri
                            }
                            DocumentTypeUri.ElectricityBill -> {
                                documentElectric = uri
                            }
                            DocumentTypeUri.HistorySellReport -> {
                                documentHistory = uri
                            }
                        }
                    }
                },
                onCancelClick = {
                    openUploadDialog.value = false
                }
            )

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
                    .scrollEnabled(enabled = true)
            ) {
                item{
                    /* header */
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = space_20)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            TextStyleBlack(content = "Seller identity Verification 2(SIV2)",
                            fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.subtitle1.copy(
                                    lineHeight = heightText_24
                                )
                            )
                        }
                        SpaceCompose(height = marginDouble)
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = marginDouble),
                            horizontalArrangement = Arrangement.Start) {
                            TextStyleBlack(content = "Upload Document",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.subtitle2.copy(
                                    lineHeight = heightText_22
                                )
                            )
                        }
                        SpaceCompose(height = marginMinimum)
                    }
                }

                item {
                    Box(modifier = Modifier.background(color = Color.White)) {
                        UploadIdentityWithHeaderForm(
                            titleText = R.string.title_business_license,
                            titleButtonUpload = R.string.title_button_business_license,
                            onSelectDocumentType = {},
                            documentUri = documentBusiness,
                            onUploadClick = {
                                openUploadDialog.value = true
                                requestUploadDialog.value = DocumentTypeUri.BusinessLicense
                            }
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.background(color = Color.White)) {
                        UploadIdentityWithHeaderForm(
                            titleText = R.string.title_brand_license,
                            titleButtonUpload = R.string.title_button_brand_license,
                            onSelectDocumentType = {},
                            documentUri = documentBrand,
                            onUploadClick = {
                                openUploadDialog.value = true
                                requestUploadDialog.value = DocumentTypeUri.BrandLicense
                            })
                    }
                }

                item {
                    Box(modifier = Modifier.background(color = Color.White)) {
                        UploadIdentityWithHeaderForm(
                            titleText = R.string.title_electricity_bill,
                            titleButtonUpload = R.string.title_button_electricity_bill,
                            onSelectDocumentType = {},
                            documentUri = documentElectric,
                            onUploadClick = {
                                openUploadDialog.value = true
                                requestUploadDialog.value = DocumentTypeUri.ElectricityBill
                            })
                    }
                }

                item {
                    Box(modifier = Modifier.background(color = Color.White)) {
                        UploadIdentityWithHeaderForm(
                            titleText = R.string.title_history_sell_report,
                            titleButtonUpload = R.string.title_button_history_sell_report,
                            onSelectDocumentType = {},
                            documentUri = documentHistory,
                            onUploadClick = {
                                openUploadDialog.value = true
                                requestUploadDialog.value = DocumentTypeUri.HistorySellReport
                            })
                    }
                }

                item{
                    Box(modifier = Modifier) {
                        ButtonConfirmCompose(buttonText = stringResource(id = R.string.submit), colorEnable = Color_Green_Main,
                            isEnable = _isFullDocumentType) {
                            viewModel.uploadDocumentFinal()
                        }
                    }
                }
            }
        }
    }
    LoadingScreen(isLoading = loadingValue.value)
}

fun Modifier.scrollEnabled(
    enabled: Boolean,
) = nestedScroll(
    connection = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource
        ): Offset = if(enabled) Offset.Zero else available
    }
)


