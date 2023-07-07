package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.enums.DocumentType
import olmo.wellness.android.ui.common.extensions.getFileName
import olmo.wellness.android.ui.theme.*
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UploadIdentityWithHeaderForm(
    titleText: Int? = null,
    titleButtonUpload: Int? = null,
    isError: Boolean = false,
    documentUri: Uri?,
    onSelectDocumentType: (String) -> Unit,
    onUploadClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val ime = LocalWindowInsets.current.ime
    val relocationRequester = remember { BringIntoViewRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val interactionSourceState = interactionSource.collectIsFocusedAsState()
    val scope = rememberCoroutineScope()

    val documentTypes = listOf(
        DocumentType.BankAccount, DocumentType.CardAccount
    )
    val context = LocalContext.current
    var typeDocument by remember { mutableStateOf("") }
    var validateTypeDocument by remember { mutableStateOf(false) }

    if (typeDocument.isNotEmpty()) {
        onSelectDocumentType(documentTypes.firstOrNull { stringResource(it.name) == typeDocument }?.type.orEmpty())
    }

    LaunchedEffect(ime.isVisible, interactionSourceState.value) {
        if (ime.isVisible && interactionSourceState.value) {
            scope.launch {
                delay(300)
                relocationRequester.bringIntoView()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = marginDouble, vertical = marginStandard)
            .border(
                BorderStroke(width = defaultBorderWidth, color = Neutral_Gray_4),
                RoundedCornerShape(marginStandard)
            )
            .background(White)
    ) {
        titleText?.let {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color = Neutral_Gray_3)
                .height(36.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
                ) {
                Text(
                    text = stringResource(id = titleText),
                    color = Neutral_Gray_9,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(marginDouble)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding_20)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (documentUri != null) {
                    val filename = File(documentUri.path.orEmpty()).name
                    val fileNameBackup = documentUri?.let { getFileName(context, it) }
                    Text(
                        modifier = Modifier
                            .padding(horizontal = padding_12, vertical = padding_12),
                        text = fileNameBackup ?: filename,
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        color = Neutral_Gray_9
                    )
                }
                Button(
                    onClick = {
                            onUploadClick()
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
                            titleButtonUpload ?: R.string.upload_document
                        ),
                        style = MaterialTheme.typography.button,
                        overflow = TextOverflow.Ellipsis,
                        color = Neutral_Gray_9,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}