package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.toSize
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.enums.IdentityType
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.err_resubmit.ErrorTitleReSubmitCompose
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UploadIdentityForm(
    titleText: String? = null,
    isError: Boolean = false,
    isErrorResubmit : Boolean?= false,
    imageList: List<Uri?> = emptyList(),
    defaultImageList : List<Uri>? = emptyList(),
    defaultIdentity : IdentityType? = null,
    defaultNumberIdentity: String= "",
    onChange: (IdentityType, String) -> Unit,
    onUploadClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val ime = LocalWindowInsets.current.ime
    val relocationRequester = remember { BringIntoViewRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val interactionSourceState = interactionSource.collectIsFocusedAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(ime.isVisible, interactionSourceState.value) {
        if (ime.isVisible && interactionSourceState.value) {
            scope.launch {
                delay(300)
                relocationRequester.bringIntoView()
            }
        }
    }
    val focusRequester = FocusRequester()
    val isFocused = remember { mutableStateOf(false) }

    val identityList =
        listOf(IdentityType.Passport, IdentityType.IdCard, IdentityType.DriverLicense)

    var typeIdentity by remember { mutableStateOf<IdentityType?>(defaultIdentity) }
    var numberIdentity by remember { mutableStateOf(defaultNumberIdentity) }

    var defaultImageIdentity by remember {
        mutableStateOf(imageList.ifEmpty {
            defaultImageList
        })
    }
    if(imageList.isNotEmpty()){
        defaultImageIdentity = imageList
    }

    var expanded by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(Size.Zero) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = marginDouble, vertical = marginStandard),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            titleText?.let {
                Text(
                    text = it,
                    color = Neutral_Gray_9,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = marginMinimum)
                )
            }
            if(isErrorResubmit == true){
                ErrorTitleReSubmitCompose()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightTextField_36)
                .padding(bottom = marginStandard)
                .border(
                    BorderStroke(width = defaultBorderWidth, color = Neutral_Gray_4),
                    RoundedCornerShape(marginStandard)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier
                .padding(horizontal = marginMinimum)
                .onGloballyPositioned { coordinates ->
                    rowSize = coordinates.size.toSize()
                }
                .clickable {
                    expanded = !expanded
                },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (typeIdentity == null) stringResource(id = R.string.passport) else stringResource(
                        id = typeIdentity?.name ?: 0
                    ),
                    color = if (typeIdentity == null) Neutral_Gray_5 else Neutral_Gray_9,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = padding_12)
                        .width(width_120)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_down_24),
                    contentDescription = "Expandable Arrow",
                    modifier = Modifier
                        .rotate(if (expanded) 180f else 0f),
                    tint = Neutral_Gray_9,
                )
            }
            DropdownMenu(
                modifier = Modifier
                    .width(with(LocalDensity.current) { rowSize.width.toDp() })
                    .background(White)
                    .border(
                        BorderStroke(width = marginStandard, color = White),
                        RoundedCornerShape(
                            topStart = marginStandard,
                            topEnd = marginStandard,
                            bottomStart = marginStandard,
                            bottomEnd = marginStandard
                        )
                    )
                    .shadow(elevation = defaultShadow)
                    .padding(top = marginMinimum),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                identityList.forEach { type ->
                    DropdownMenuItem(
                        onClick = {
                            typeIdentity = type
                            expanded = false
                        }
                    ) {
                        Text(
                            text = stringResource(id = type.name),
                            color = Neutral_Gray_9,
                            style = MaterialTheme.typography.body2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(
                                horizontal = padding_12,
                                vertical = marginStandard
                            )
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier
                    .height(heightTextField_36)
                    .width(defaultHeightLine)
                    .background(Neutral_Gray_4)
            )
            BasicTextField(modifier = Modifier
                .background(White)
                .fillMaxWidth()
                .height(heightTextField_36)
                .clearFocusOnKeyboardDismiss {
                    typeIdentity?.let {
                        onChange(it, numberIdentity)
                    }
                }
                .focusRequester(focusRequester)
                .bringIntoViewRequester(relocationRequester)
                .onFocusChanged {
                    isFocused.value = it.isFocused
                },
                interactionSource = interactionSource,
                value = numberIdentity,
                onValueChange = {
                    numberIdentity = it
                    typeIdentity?.let {
                        onChange(it, numberIdentity)
                    }
                },
                cursorBrush = SolidColor(Tiffany_Blue_500),
                textStyle = MaterialTheme.typography.body2,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = marginStandard),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (numberIdentity.isEmpty())
                            Text(
                                maxLines = 1,
                                text = numberIdentity,
                                style = MaterialTheme.typography.body2,
                                color = if (numberIdentity.isEmpty()) Neutral_Gray_5 else Neutral_Gray_9,
                                textAlign = TextAlign.Start,
                            )
                        innerTextField()
                    }
                },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        typeIdentity?.let {
                            onChange(it, numberIdentity)
                        }
                    }
                )
            )
        }
        if (isError) {
            Text(
                text = stringResource(id = R.string.error_empty_dropdown_pick),
                color = Error_500,
                style = MaterialTheme.typography.overline,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = with(LocalDensity.current) { rowSize.width.toDp() + marginDouble },
                        bottom = marginMinimum
                    ),
                textAlign = TextAlign.Start
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(
                    BorderStroke(width = defaultBorderWidth, color = Neutral_Gray_4),
                    RoundedCornerShape(marginStandard)
                )
                .padding(vertical = padding_20),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (defaultImageIdentity?.isNotEmpty() == true) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = marginStandard)
                            .noRippleClickable {
                                if (typeIdentity != null) {
                                    onUploadClick()
                                }
                            }, horizontalArrangement = Arrangement.Center
                    ) {
                        defaultImageIdentity?.forEach { imageUri ->
                            imageUri.let {
                                Box(Modifier.padding(horizontal = marginMinimum)) {
                                    Image(
                                        painter = rememberImagePainter(
                                            data = imageUri
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
                }
                val uploadType =
                    if (typeIdentity != null) stringResource(id = typeIdentity!!.name) else ""
                Button(
                    onClick = {
                        if (typeIdentity != null)
                            onUploadClick()
                    },
                    modifier = Modifier
                        .height(height_36),
                    shape = RoundedCornerShape(marginStandard),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (defaultImageIdentity?.isNullOrEmpty() == true) White else Neutral_Gray_3
                    ),
                    border = BorderStroke(defaultBorderWidth, Neutral_Gray_4)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = stringResource(
                            R.string.upload, uploadType
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