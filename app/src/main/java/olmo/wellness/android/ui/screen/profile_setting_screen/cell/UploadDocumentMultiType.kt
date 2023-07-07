package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.enums.DocumentType
import olmo.wellness.android.domain.model.defination.TypeURI
import olmo.wellness.android.domain.model.defination.WrapURI
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.err_resubmit.ErrorTitleReSubmitCompose
import olmo.wellness.android.ui.common.extensions.getFileName
import olmo.wellness.android.ui.theme.*
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UploadDocumentMultiType(
    titleText: Int? = null,
    isErrorResubmit: Boolean?= false,
    titleButtonUpload: Int? = null,
    listIdentity: List<WrapURI?>?= emptyList(),
    listIdentityDefault: List<WrapURI?>?= emptyList(),
    onSelectDocumentType: (String) -> Unit,
    onUploadClick: () -> Unit
) {
    val ime = LocalWindowInsets.current.ime
    val relocationRequester = remember { BringIntoViewRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val interactionSourceState = interactionSource.collectIsFocusedAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val documentTypes = listOf(
        DocumentType.BankAccount, DocumentType.CardAccount
    )
    var listEntityChange by remember {
        mutableStateOf(listIdentityDefault)
    }
    if(listIdentity?.isNotEmpty() == true){
        listEntityChange = listIdentity
    }
    val typeDocument by remember { mutableStateOf("") }
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
                horizontalArrangement = Arrangement.Start) {
                Text(
                    text = stringResource(id = titleText),
                    color = Neutral_Gray_9,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(marginDouble)
                )
                if(isErrorResubmit == true){
                    ErrorTitleReSubmitCompose()
                }
            }
        }
        LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    if (listEntityChange.isNullOrEmpty()) {
                        34.dp
                    } else {
                        150.dp
                    }
                )
        ) {
            items(listEntityChange?.size ?: 0) { index ->
                Row(
                    modifier = Modifier
                        .padding(marginStandard)
                        .noRippleClickable {
                        }, horizontalArrangement = Arrangement.Center
                ) {
                    Column() {
                        Box(Modifier.padding(horizontal = marginMinimum)) {
                            if (listEntityChange?.get(index)?.typeURI == TypeURI.IMAGE_TYPE) {
                                Image(
                                    painter = rememberImagePainter(
                                        data = listEntityChange?.get(index)?.uri
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
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.img_document_default),
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
                            }
                            Image(
                                painter = painterResource(id = R.drawable.ic_closed_16),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(sizeIcon_16)
                                    .padding(top = marginMinimum, start = marginMinimum)
                                    .align(Alignment.TopEnd),
                            )
                        }
                        if (listEntityChange?.get(index)?.uri != null) {
                            val filename =
                                File(listEntityChange?.get(index)?.uri!!.path.orEmpty()).name
                            val fileNameBackup =
                                listEntityChange?.get(index)?.uri?.let {
                                    getFileName(
                                        context,
                                        it
                                    )
                                }
                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                text = fileNameBackup ?: filename,
                                style = MaterialTheme.typography.caption.copy(fontFamily = MontserratFont),
                                overflow = TextOverflow.Ellipsis,
                                color = Neutral_Gray_9,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
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
                Button(
                    onClick = {
                        onUploadClick.invoke()
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
                        text = if (listEntityChange?.isNotEmpty() == true) stringResource(R.string.upload_document) else stringResource(
                            titleButtonUpload ?: R.string.upload_document
                        ),
                        style = MaterialTheme.typography.button,
                        overflow = TextOverflow.Ellipsis,
                        color = Neutral_Gray_9,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}