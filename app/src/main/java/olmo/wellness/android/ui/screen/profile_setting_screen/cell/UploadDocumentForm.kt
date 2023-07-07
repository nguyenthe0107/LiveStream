package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.core.enums.DocumentType
import olmo.wellness.android.ui.theme.*
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UploadDocumentForm(
    documentUri: Uri?,
    paddingHorizontal: Dp = marginDouble,
    paddingVertical: Dp = marginStandard,
    onSelectDocumentType: (String) -> Unit,
    onUploadClick: () -> Unit,
) {
    val documentTypes = listOf(
        DocumentType.BankAccount, DocumentType.CardAccount
    )

    var typeDocument by remember { mutableStateOf("") }
    var validateTypeDocument by remember { mutableStateOf(false) }

    if (typeDocument.isNotEmpty()) {
        onSelectDocumentType(documentTypes.firstOrNull { stringResource(it.name) == typeDocument }?.type.orEmpty())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral_Gray)
    ) {

        Text(
            text = stringResource(id = R.string.verify_additional_document),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.button,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(
                start = marginDouble,
                end = marginDouble,
                top = marginDouble,
                bottom = marginMinimum
            )
        )


        DropdownMenuSelected(
            titleText = stringResource(id = R.string.additional_document),
            dropdownList = documentTypes.map { stringResource(it.name) },
            hint = stringResource(id = R.string.select_document_type),
            isError = validateTypeDocument
        ) {
            typeDocument = it
            validateTypeDocument = it.isEmpty()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .requiredHeight(150.dp)
                .background(White)
                .padding(horizontal = marginDouble)
                .border(
                    BorderStroke(width = defaultBorderWidth, color = Neutral_Gray_4),
                    RoundedCornerShape(marginMinimum)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (documentUri != null) {
                val filename = File(documentUri.path.orEmpty()).name
                Text(
                    modifier = Modifier
                        .padding(horizontal = padding_12, vertical = padding_12),
                    text = filename,
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
                        R.string.upload_document
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