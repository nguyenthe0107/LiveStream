package olmo.wellness.android.ui.screen.dialog

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.extension.sizeInMb
import olmo.wellness.android.ui.common.components.upload.UploadIInstructionUI
import olmo.wellness.android.ui.common.extensions.getFileName
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.UploadFileViewModel
import olmo.wellness.android.ui.theme.*
import java.io.File

@Composable
fun AddDocumentDialog(
    viewModel: UploadFileViewModel = hiltViewModel(),
    openDialogCustom: MutableState<Boolean>,
    openInstructionUI: Boolean,
    documentUri: Uri?,
    onReadyReadInstruction: () -> Unit,
    onSubmitClick: (Uri, String) -> Unit,
    onCancelClick: () -> Unit,
    isShowUpdateGuide: Boolean ?= null,
    onRequestImage: ((Boolean) -> Unit)?=null,
    onRequestDocument: ((Boolean) -> Unit)?=null
) {
    if (openDialogCustom.value) {
        if (openInstructionUI) {
            Dialog(onDismissRequest = { openDialogCustom.value = false }) {
                if(isShowUpdateGuide == true){
                    UploadIInstructionUI(requestImage = onRequestImage, requestDocument = onRequestDocument)
                }else{
                    AddDocumentInstructionDialogUI(onReadyReadInstruction, onCancelClick)
                }
            }
        } else {
            Box {
                val isLoading = viewModel.isLoading.collectAsState()
                Dialog(onDismissRequest = { openDialogCustom.value = false }) {
                    AddDocumentDialogUI(viewModel, documentUri, onSubmitClick, onCancelClick)
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
private fun AddDocumentInstructionDialogUI(
    onReadyReadInstruction: () -> Unit,
    onCancelClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(marginStandard),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
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
            UploadDocumentInstructionUI()
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
private fun AddDocumentDialogUI(
    viewModel: UploadFileViewModel,
    documentUri: Uri?,
    onSubmitClick: (Uri, String) -> Unit,
    onCancelClick: () -> Unit
) {
    var documentUriSelected by remember { mutableStateOf(documentUri) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(marginStandard),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 450.dp)
            .wrapContentHeight()
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
            UploadDocumentUI(documentUri) {
                documentUriSelected = it
            }
            sectionButtonDialog(
                onSubmitClick = {
                    val file = documentUriSelected?.path?.let { path -> File(path) }
                    file?.let {
                        if (file.sizeInMb > 5) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_file_size),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else
                            documentUriSelected?.let {
                                scope.launch {
                                    viewModel.setLoadingState(true)
                                    val documentUpload =
                                        scope.async { viewModel.uploadFile(it) }
                                    val resultDocumentUpload = documentUpload.await()
                                    if (resultDocumentUpload != null) {
                                        onSubmitClick(it, resultDocumentUpload)
                                        viewModel.setLoadingState(false)
                                    }
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
fun sectionButtonDialog(
    onSubmitClick: (String) -> Unit,
    onCancelClick: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = marginDouble, vertical = marginDouble)
            .fillMaxWidth()
            .height(height_60),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                onCancelClick("")
            },
            modifier = Modifier
                .height(height_44)
                .weight(1f)
                .padding(end = marginStandard),
            shape = RoundedCornerShape(marginStandard),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = White
            ),
            border = BorderStroke(defaultBorderWidth, Color_Green_Main)
        ) {
            Text(
                text = stringResource(R.string.cancel),
                style = MaterialTheme.typography.button,
                overflow = TextOverflow.Ellipsis,
                color = Color_Green_Main
            )
        }

        Button(
            onClick = {
                onSubmitClick("")
            },
            modifier = Modifier
                .height(height_44)
                .weight(1f),
            shape = RoundedCornerShape(marginStandard),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color_Green_Main
            )
        ) {
            Text(
                text = stringResource(R.string.continue_value),
                style = MaterialTheme.typography.button,
                overflow = TextOverflow.Ellipsis,
                color = White
            )
        }
    }
}

@Composable
private fun UploadDocumentInstructionUI() {
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
        Text(
            text = stringResource(id = R.string.document_upload_decs_warning),
            color = Neutral_Gray_7,
            style = MaterialTheme.typography.body2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = marginDouble, vertical = padding_10)
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun UploadDocumentUI(documentUri: Uri?, onDocumentSelect: (Uri) -> Unit) {
    val permissionsState = rememberPermissionState(Constants.filePermission)
    val context = LocalContext.current

    val documentFile = remember { mutableStateOf(documentUri) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        if (it != null) {
            documentFile.value = it
            onDocumentSelect(it)
        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White)
    ) {
        Text(
            text = stringResource(id = R.string.verify_additional_document),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(vertical = padding_10)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(id = R.string.upload_document),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.subtitle2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = padding_10)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Neutral_Gray_2)
                .border(
                    BorderStroke(width = defaultBorderWidth, color = Neutral_Gray_4),
                    RoundedCornerShape(marginMinimum)
                )
                .padding(vertical = padding_20, horizontal = marginStandard),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (documentFile.value != null) {
                val filename = File(documentFile.value!!.path.orEmpty()).name
                val fileNameBackup = documentFile.value?.let { getFileName(context, it) }
                Text(
                    modifier = Modifier
                        .padding(horizontal = padding_12, vertical = padding_12),
                    text = fileNameBackup ?: filename,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = Neutral_Gray_9
                )
            }

            val errorPermission = stringResource(id = R.string.permission_upload_identity_error)
            Button(
                onClick = {
                    permissionsState.launchPermissionRequest()
                    if (permissionsState.hasPermission) {
                        launcher.launch(arrayOf("application/pdf"))
                    } else {
                        Toast.makeText(
                            context,
                            errorPermission,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .height(height_36),
                shape = RoundedCornerShape(marginStandard),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = White
                ),
                border = BorderStroke(defaultBorderWidth, Neutral_Gray_4)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = if (documentUri != null) stringResource(R.string.change) else stringResource(
                        R.string.select_file
                    ),
                    style = MaterialTheme.typography.button,
                    overflow = TextOverflow.Ellipsis,
                    color = Neutral_Gray_9,
                    textAlign = TextAlign.Center
                )
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
        text = stringResource(id = R.string.upload_document_warning),
        color = Neutral_Gray_7,
        style = MaterialTheme.typography.caption,
        overflow = TextOverflow.Ellipsis
    )
}