package olmo.wellness.android.ui.screen.profile_dashboard.gallery_compose

import android.Manifest
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.enums.UploadFileType
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.permission.RequireExternalStoragePermission
import olmo.wellness.android.ui.theme.*

@OptIn(
    ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class,
    com.google.accompanist.permissions.ExperimentalPermissionsApi::class,
    coil.annotation.ExperimentalCoilApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class
)
@Composable
fun GalleryCompose(
    onSelectedImage: (uri: Uri?) -> Unit,
    modalBottomSheetState: ModalBottomSheetState,
    typeUpload : UploadFileType,
    viewModel: GalleryViewModel = hiltViewModel(),
    isOpenCaptureImage: (isOpen: Boolean?) -> Unit,
    onSubmit: (Boolean, Boolean) -> Unit,
    getSelectedImage: (uri: String?) -> Unit){
    val scope = rememberCoroutineScope()
    val imageSelected = remember { mutableStateOf<Uri?>(null) }
    if (imageSelected.value != null) {
        onSelectedImage.invoke(imageSelected.value)
    }
    val imageSelectedUri = remember { mutableStateOf<Uri?>(null) }
    var isCaptureDisplay by remember { mutableStateOf(false) }
    if(isCaptureDisplay){
        isOpenCaptureImage.invoke(true)
        isCaptureDisplay = false
    }
    var isLoading by remember { mutableStateOf(false) }
    val allImagesFromGallery = viewModel.allImagesFromGallery.collectAsState()
    val permissionState = rememberPermissionState(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    if (modalBottomSheetState.isVisible) {
        if(permissionState.hasPermission){
            viewModel.loadAllImages()
        }else{
            RequireExternalStoragePermission(navigateToSettingsScreen = { /*TODO*/ }) {}
        }
    }
    Column(
        modifier = Modifier
            .requiredHeight(400.dp)
            .background(White),
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = padding_12, vertical = marginDouble)){
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_close_24),
                contentDescription = "",
                modifier = Modifier
                    .size(sizeIcon_28)
                    .align(Alignment.CenterStart)
                    .clickable {
                        scope.launch {
                            modalBottomSheetState.hide()
                            isLoading = false
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
                color = if (imageSelectedUri.value != null) Color_LiveStream_Main_Color else Neutral_Gray_3,
                modifier = Modifier
                    .padding(horizontal = marginDouble)
                    .align(Alignment.CenterEnd)
                    .noRippleClickable {
                        scope.launch {
                            isLoading = true
                            if (imageSelectedUri.value != null) {
                                val imageUpload = scope.async {
                                    viewModel.uploadFile(
                                        imageSelectedUri.value!!,
                                        typeUpload
                                    )
                                }
                                val resultImage = imageUpload.await()
                                if (resultImage != null) {
                                    viewModel.setLoadingState(false)
                                    modalBottomSheetState.hide()
                                    isLoading = false
                                    getSelectedImage.invoke(resultImage)
                                }
                                imageSelected.value = imageSelectedUri.value
                                imageSelectedUri.value = null
                                onSubmit(false, true)
                            }
                        }
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
            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
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
                                isLoading = false
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
                        painter = rememberAsyncImagePainter(
                            model = allImagesFromGallery.value[index]
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
                                colorFilter = ColorFilter.tint(Color_LiveStream_Main_Color)
                            )
                        }
                }
            }
        }
    }

    if (isLoading)
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = true)){
            CircularProgressIndicator()
        }
}