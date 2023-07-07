package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.steps

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.category.Category
import olmo.wellness.android.domain.model.defination.BottomSheetType
import olmo.wellness.android.domain.model.defination.StepType
import olmo.wellness.android.domain.model.defination.TypeURI
import olmo.wellness.android.domain.model.defination.WrapURI
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import olmo.wellness.android.ui.common.components.upload.DialogUploadInstruction
import olmo.wellness.android.ui.screen.category_screen.CategoryScreenViewModel
import olmo.wellness.android.ui.screen.category_screen.cell.MainCategory
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.*
import olmo.wellness.android.ui.theme.*

@ExperimentalCoroutinesApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Step3Screen(
    verificationData: VerificationData?,
    businessId: Int?,
    modalBottomSheetState: ModalBottomSheetState,
    listImageSelectedInputSelected: MutableState<List<Uri?>>,
    requestOpenGallery : (Boolean, BottomSheetType, StepType) -> Unit,
    categoryViewModel: CategoryScreenViewModel = hiltViewModel(),
    viewModel: Step3ScreenViewModel = hiltViewModel(),
    onSubmit: (Boolean, Boolean) -> Unit
){
//    viewModel.setBusinessId(businessId)
//    viewModel.setVerificationData(verificationData)
//
//    val expandedIdsList = categoryViewModel.expandedIdsList.collectAsState().value
//    val categoryList: List<Category> = categoryViewModel.categoryList.collectAsState().value
//
//    if (viewModel.listSubcategoryDefaultSelected.isNotEmpty()){
//        categoryList.onEach { mainCategory ->
//            mainCategory.categories.onEach { subCategory ->
//                if (viewModel.listSubcategoryDefaultSelected.any { idCacheSelected ->
//                        subCategory.id == idCacheSelected
//                }){
//                    subCategory.isSelected = true
//                    viewModel.removeIndexMapped(subCategory.id)
//                    categoryViewModel.requestExpandCategory(mainCategory.id)
//                }
//            }
//        }
//    }
//
//    val selectedSubCategory = categoryList
//        .map { mainCategory ->
//            mainCategory.categories
//        }
//        .flatten()
//        .filter { subCategory ->
//            subCategory.isSelected
//        }
//        .map { subCategory ->
//            subCategory.id
//        }
//
//    viewModel.setSubCategoryIds(selectedSubCategory)
//
//    val scope = rememberCoroutineScope()
//    val context = LocalContext.current
//    val error = viewModel.error.collectAsState()
//    if (error.value.isNotEmpty()) {
//        Toast.makeText(context, error.value, Toast.LENGTH_SHORT).show()
//        onSubmit(false, false)
//    }
//
//    if (viewModel.isLoading.collectAsState().value) {
//        onSubmit(true, false)
//    }
//
//    LaunchedEffect(true) {
//        viewModel.isResubmit()
//    }
//
//
//    /* Resubmit Part */
//    val hasErrServiceLicense = viewModel.hasErrServiceLicense.collectAsState()
//    val hasErrServiceName = viewModel.hasErrServiceName.collectAsState()
//    val hasErrTypeDocument = viewModel.hasErrTypeDocument.collectAsState()
//
//    if (viewModel.isSuccess.collectAsState().value) {
//        onSubmit(false, true)
//        viewModel.setIsSuccess(false)
//    }
//
//    var validateStoreName by remember { mutableStateOf(false) }
//    val isStoreNameAvailable = viewModel.isStoreNameAvailable.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Neutral_Gray)
//    ){
//        Text(
//            text = stringResource(id = R.string.store_info),
//            color = Neutral_Gray_9,
//            style = MaterialTheme.typography.button,
//            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.padding(horizontal = marginDouble, vertical = marginMinimum)
//        )
//        Text(
//            text = stringResource(id = R.string.store_info_decs),
//            color = Neutral_Gray_7,
//            style = MaterialTheme.typography.overline,
//            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.padding(horizontal = marginDouble, vertical = marginMinimum)
//        )
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(White)
//        ) {
//            TextFieldWithError(
//                titleText = stringResource(id = R.string.store_name),
//                isError = validateStoreName,
//                isErrorResubmit = hasErrServiceName.value,
//                hint = stringResource(id = R.string.name),
//                paddingVertical = padding_2,
//                defaultValue = viewModel.getStoreName()
//            ) {
//                viewModel.setStoreName(it)
//                validateStoreName = it.isEmpty()
//            }
//            if (isStoreNameAvailable.value != null)
//                if (isStoreNameAvailable.value == true) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(White)
//                            .padding(horizontal = marginDouble, vertical = marginStandard),
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_check_green),
//                            contentDescription = "check",
//                            modifier = Modifier
//                                .size(sizeIcon_16)
//                                .padding(end = marginMinimum)
//                        )
//                        Text(
//                            text = stringResource(id = R.string.available),
//                            color = Success_500,
//                            style = MaterialTheme.typography.overline,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                    }
//                } else {
//                    Text(
//                        text = stringResource(id = R.string.error_name_store),
//                        color = Error_500,
//                        style = MaterialTheme.typography.overline,
//                        overflow = TextOverflow.Ellipsis,
//                        modifier = Modifier.padding(
//                            vertical = marginMinimum,
//                            horizontal = marginDouble
//                        )
//                    )
//                }
//        }
//
//        DividerView()
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(White)
//        ) {
//            Text(
//                text = stringResource(id = R.string.brand_category_title),
//                color = Neutral_Gray_9,
//                style = MaterialTheme.typography.subtitle2,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.padding(horizontal = marginDouble, vertical = marginMinimum)
//            )
//        }
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(White)
//        ) {
//            Column {
//                categoryList.forEachIndexed { index, category ->
//                    if (categoryViewModel.isLoadMore(index)) {
//                        categoryViewModel.onLoadMore()
//                    }
//                    MainCategory(
//                        category,
//                        expandedIdsList.any { category.id == it },
//                        onExpandClick = {
//                            categoryViewModel.onCategoryExpandClicked(
//                                category.id
//                            )
//                        },
//                        onSelected = { categoryId, subCateGoryId ->
//                            categoryViewModel.onSubCategoryClicked(categoryId, subCateGoryId)
//                        }
//                    )
//                }
//            }
//
//            val error = viewModel.error.collectAsState()
//            if (error.value.isNotBlank()) {
//                Column(
//                    modifier = Modifier.fillMaxHeight(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = error.value,
//                        color = MaterialTheme.colors.error,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    )
//                    Button(
//                        onClick = {
//                            categoryViewModel.getCategoryList()
//                        },
//                        colors = ButtonDefaults.textButtonColors(
//                            backgroundColor = Color.Gray
//                        )
//                    ) {
//                        Text(stringResource(R.string.retry))
//                    }
//                }
//            } else
//                if (viewModel.isLoading.collectAsState().value && categoryList.isNullOrEmpty()) {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .padding(vertical = padding_20)
//                    )
//                }
//        }
//
//        val getBrandLicense = viewModel.getBrandLicense.collectAsState()
//        val answersOfOwnerBranch = listOf(
//            stringResource(id = R.string.yes),
//            stringResource(id = R.string.no)
//        )
//        GroupRadioButtonSelected(
//            titleText = stringResource(id = R.string.brand_license_title),
//            selectionList = answersOfOwnerBranch,
//            defaultValue = viewModel.getOptionBranchLicense()
//        ) {
//            viewModel.setHasBrandLicense(it == answersOfOwnerBranch[0])
//        }
//
//        val onReadyReadInstructionDialog = remember { mutableStateOf(false) }
//        val listIdentifyCombineSelected = viewModel.identifyList.collectAsState()
//
//        if(listImageSelectedInputSelected.value.isNotEmpty()){
//            listImageSelectedInputSelected.value.distinctBy { it?.scheme }.map { it ->
//                val newList = listOf(WrapURI(uri = it, typeURI = TypeURI.IMAGE_TYPE)).distinctBy { it.uri?.scheme }
//                viewModel.setIdentifyList(newList)
//            }
//        }
//        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
//            if (it != null) {
//                val newList = listOf(WrapURI().copy(uri = it, typeURI = TypeURI.DOCUMENT_TYPE))
//                viewModel.setIdentifyList(newList)
//            }
//        }
//
//        if(onReadyReadInstructionDialog.value){
//            DialogUploadInstruction(openDialogCustom = onReadyReadInstructionDialog, onRequestImage = {
//                requestOpenGallery.invoke(true, BottomSheetType.GALLERY, StepType.STEP3)
//                scope.launch {
//                    modalBottomSheetState.show()
//                }
//                onReadyReadInstructionDialog.value = false
//            }, onRequestDocument = {
//                launcher.launch(arrayOf("application/pdf"))
//                onReadyReadInstructionDialog.value = false
//            })
//        }
//
//        if(getBrandLicense.value == true || (viewModel.getOptionBranchLicense() == true)){
//            Box(modifier = Modifier.background(color = Color.White)) {
//                UploadDocumentMultiType(
//                    listIdentity = listIdentifyCombineSelected.value,
//                    listIdentityDefault = viewModel.getIdentity(),
//                    titleButtonUpload = R.string.title_button_upload_license,
//                    onSelectDocumentType = {},
//                    onUploadClick = {
//                        onReadyReadInstructionDialog.value = true
//                    },
//                    isErrorResubmit = hasErrServiceLicense.value || hasErrTypeDocument.value)
//            }
//        }
//
//        ButtonNextStep(
//            stringResource(R.string.submit),
//            viewModel.isValidate.collectAsState().value
//        ) {
//            viewModel.submitStep3()
//        }
//    }
}
