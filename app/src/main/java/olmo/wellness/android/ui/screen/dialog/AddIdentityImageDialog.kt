package olmo.wellness.android.ui.screen.dialog

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.*
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants.permissions
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.UploadFileViewModel
import olmo.wellness.android.ui.theme.*

@Composable
fun AddIdentityImageDialog(
    viewModel: UploadFileViewModel = hiltViewModel(),
    openDialogCustom: Boolean,
    openInstructionUI: Boolean,
    nameIdentity: String,
    firstImage: Uri?,
    secondImage: Uri?,
    onReadyReadInstruction: () -> Unit,
    onSelectImageFirst: () -> Unit,
    onSelectImageSecond: () -> Unit,
    onSubmitClick: (String, String) -> Unit,
    onCancelClick: () -> Unit
) {
    val context= LocalContext.current
    if (openDialogCustom) {
        if (openInstructionUI) {
            Dialog(onDismissRequest = { onCancelClick() }) {
                UploadIdentityInstructionDialogUI(onReadyReadInstruction, onCancelClick)
            }
        } else {
            Box {
                val isLoading = viewModel.isLoading.collectAsState()
                Dialog(onDismissRequest = { onCancelClick() }) {
                    UploadImageIdentityDialogUI(
                        viewModel,
                        nameIdentity,
                        firstImage,
                        secondImage,
                        context,
                        onSelectImageFirst,
                        onSelectImageSecond,
                        onSubmitClick,
                        onCancelClick
                    )
                }
                if (isLoading.value)
                    Dialog(
                        onDismissRequest = {},
                        properties = DialogProperties(
                            dismissOnClickOutside = false,
                            dismissOnBackPress = true,
                        )
                    ) {
                        CircularProgressIndicator()
                    }
            }
        }
    }
}

@Composable
private fun UploadIdentityInstructionDialogUI(
    onReadyReadInstruction: () -> Unit,
    onCancelClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(marginStandard),
        modifier = Modifier
            .fillMaxWidth()
            .requiredWidth(350.dp)
            .background(Color.Transparent),
        elevation = defaultShadow
    ) {
        Column(
            Modifier
                .padding(horizontal = marginDouble)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White)
        ) {
            UploadIdentityInstructionUI()
            sectionButtonDialog(
                onSubmitClick = {
                    onReadyReadInstruction()
                },
                onCancelClick = {
                    onCancelClick()
                })
        }
    }
}

@Composable
private fun UploadImageIdentityDialogUI(
    viewModel: UploadFileViewModel,
    nameIdentity: String,
    firstImage: Uri?,
    secondImage: Uri?,
    context: Context,
    onSelectImageFirst: () -> Unit,
    onSelectImageSecond: () -> Unit,
    onSubmitClick: (String, String) -> Unit,
    onCancelClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val error = viewModel.error.collectAsState()
    if (error.value.isNotEmpty()) {
        viewModel.setLoadingState(false)
        Toast.makeText(context, error.value, Toast.LENGTH_SHORT).show()
    }
    Card(
        shape = RoundedCornerShape(marginStandard),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .requiredWidth(350.dp)
            .background(Color.Transparent),
        elevation = defaultShadow
    ) {
        Column(
            Modifier
                .padding(horizontal = marginDouble)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White)
        ) {
            UploadImageIdentityUI(
                nameIdentity,
                firstImage,
                secondImage,
                context,
                onSelectImageFirst,
                onSelectImageSecond
            )
            sectionButtonDialog(
                onSubmitClick = {
                    if (firstImage != null && secondImage != null) {
                        scope.launch {
                            viewModel.setLoadingState(true)
                            val firstImageUpload = scope.async { viewModel.uploadFile(firstImage) }
                            val secondImageUpload =
                                scope.async { viewModel.uploadFile(secondImage) }
                            val resultFirstImage = firstImageUpload.await()
                            val resultSecondImage = secondImageUpload.await()
                            if (resultFirstImage != null && resultSecondImage != null) {
                                onSubmitClick(resultFirstImage, resultSecondImage)
                                viewModel.setLoadingState(false)
                            }
                        }
                    }
                },
                onCancelClick = {
                    onCancelClick()
                })
        }
    }
}

@Composable
private fun UploadIdentityInstructionUI(
) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.uploading_instructions),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = marginDouble, vertical = padding_10)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_upload_instruction),
            contentDescription = stringResource(id = R.string.uploading_instructions),
            modifier = Modifier
                .width(width_220)
                .height(height_132)
        )
        Text(
            text = stringResource(id = R.string.uploading_instructions_decs),
            color = Neutral_Gray_7,
            style = MaterialTheme.typography.body2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = marginDouble, vertical = padding_10)
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun UploadImageIdentityUI(
    nameIdentity: String,
    firstImage: Uri?,
    secondImage: Uri?,
    context : Context,
    onSelectImageFirst: () -> Unit,
    onSelectImageSecond: () -> Unit
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White)
    ) {
        Text(
            text = stringResource(id = R.string.identity_verification),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(vertical = padding_10)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(id = R.string.upload_your_identity, nameIdentity),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.subtitle2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = padding_10)
        )

        var rowSize by remember { mutableStateOf(Size.Zero) }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                Modifier
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.page_of_image_upload, nameIdentity),
                    color = Neutral_Gray_7,
                    style = MaterialTheme.typography.overline,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = marginMinimum)
                        .onGloballyPositioned { coordinates ->
                            rowSize = coordinates.size.toSize()
                        }
                )
                val error= stringResource(R.string.permission_upload_identity_error)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .requiredHeight(height_100)
                        .requiredWidth(width_154)
                        .background(Neutral_Gray_2)
                        .border(
                            BorderStroke(width = defaultBorderWidth, color = Neutral_Gray_4),
                            RoundedCornerShape(marginMinimum)
                        )
                        .padding(vertical = padding_20, horizontal = marginStandard)
                        .noRippleClickable {
                            multiplePermissionsState.launchMultiplePermissionRequest()
                            if (multiplePermissionsState.allPermissionsGranted) {
                                onSelectImageFirst()
                            } else {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (firstImage == null) {
                        Text(
                            modifier = Modifier
                                .height(height_36)
                                .border(
                                    BorderStroke(
                                        width = defaultBorderWidth,
                                        color = Neutral_Gray_4
                                    ),
                                    RoundedCornerShape(marginStandard)
                                )
                                .background(White)
                                .padding(horizontal = marginStandard, vertical = marginStandard),
                            text = stringResource(R.string.select_image),
                            style = MaterialTheme.typography.subtitle2,
                            overflow = TextOverflow.Ellipsis,
                            color = Neutral_Gray_9,
                            textAlign = TextAlign.Center
                        )
                    } else
                        Box {
                            Image(
                                painter = rememberImagePainter(
                                    data = firstImage
                                ),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(width_58)
                                    .height(height_56)
                                    .padding(top = marginMinimum)
                                    .border(
                                        borderWidth_2dp,
                                        Neutral_Gray_3,
                                        RoundedCornerShape(marginStandard)
                                    ),
                                alignment = Alignment.BottomCenter
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_closed_16),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(sizeIcon_16)
                                    .padding(top = marginMinimum, start = marginMinimum)
                                    .align(Alignment.TopEnd),
                            )
                        }
                }
            }
            Column(
                Modifier
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.cover_of_image_upload, nameIdentity),
                    color = Neutral_Gray_7,
                    style = MaterialTheme.typography.overline,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = marginMinimum)
                        .height(with(LocalDensity.current) { rowSize.height.toDp() })
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .requiredHeight(height_100)
                        .requiredWidth(width_154)
                        .background(Neutral_Gray_2)
                        .border(
                            BorderStroke(width = defaultBorderWidth, color = Neutral_Gray_4),
                            RoundedCornerShape(marginMinimum)
                        )
                        .padding(vertical = padding_20, horizontal = marginStandard)
                        .noRippleClickable {
                            multiplePermissionsState.launchMultiplePermissionRequest()
                            if (multiplePermissionsState.allPermissionsGranted) {
                                onSelectImageSecond()
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "please grant permission for upload",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (secondImage == null) {
                        Text(
                            modifier = Modifier
                                .height(height_36)
                                .border(
                                    BorderStroke(
                                        width = defaultBorderWidth,
                                        color = Neutral_Gray_4
                                    ),
                                    RoundedCornerShape(marginStandard)
                                )
                                .background(White)
                                .padding(horizontal = marginStandard, vertical = marginStandard),
                            text = stringResource(R.string.select_image),
                            style = MaterialTheme.typography.subtitle2,
                            overflow = TextOverflow.Ellipsis,
                            color = Neutral_Gray_9,
                            textAlign = TextAlign.Center
                        )
                    } else
                        Box {
                            Image(
                                painter = rememberImagePainter(
                                    data = secondImage
                                ),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(width_58)
                                    .height(height_56)
                                    .padding(top = marginMinimum)
                                    .border(
                                        borderWidth_2dp,
                                        Neutral_Gray_3,
                                        RoundedCornerShape(marginStandard)
                                    ),
                                alignment = Alignment.BottomCenter
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_closed_16),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(sizeIcon_16)
                                    .padding(top = marginMinimum, start = marginMinimum)
                                    .align(Alignment.TopEnd),
                            )
                        }
                }
            }
        }
        Text(
            text = stringResource(id = R.string.identity_upload_title_warning),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.subtitle2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = padding_10)
        )
        Text(
            text = stringResource(id = R.string.identity_upload_decs_warning),
            color = Neutral_Gray_7,
            style = MaterialTheme.typography.caption,
            overflow = TextOverflow.Ellipsis
        )
    }
}