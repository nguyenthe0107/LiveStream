package olmo.wellness.android.ui.screen.profile_setting_screen.cell

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
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.ui.common.components.err_resubmit.ErrorTitleReSubmitCompose
import olmo.wellness.android.ui.common.validate.phoneFormatWithoutZeroFirst
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable
fun NationCodePhoneSelected(
    titleText: String? = null,
    dropdownList: List<Country> = listOf(),
    hint: String = "",
    defaultValue: String? = null,
    isError: Boolean = false,
    isErrorResubmit: Boolean? = false,
    onChange: (Int, String) -> Unit
) {
    var selectedNationFlags by remember { mutableStateOf("") }
    var selectedNationCode by remember { mutableStateOf("") }

    var selectedNationId by remember { mutableStateOf(0) }


    if (selectedNationFlags.isEmpty())
        selectedNationFlags = dropdownList.firstOrNull()?.flagIconUrl.orEmpty()
    if (selectedNationCode.isEmpty())
        selectedNationCode = dropdownList.firstOrNull()?.phonePrefix.orEmpty()
    if (selectedNationId == 0)
        selectedNationId = dropdownList.firstOrNull()?.id ?: 0

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
    var phoneNumber by remember { mutableStateOf(defaultValue ?: "") }

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
                val painter =
                    rememberImagePainter(data = selectedNationFlags,
                        builder = {
                            placeholder(R.drawable.ic_baseline_image_24)
                        })
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(sizeIcon_28)
                        .padding(start = marginMinimum)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_down_24),
                    contentDescription = "Expandable Arrow",
                    modifier = Modifier
                        .rotate(if (expanded) 180f else 0f),
                    tint = Neutral_Gray_9,
                )
            }
            Divider(
                modifier = Modifier
                    .height(heightTextField_36)
                    .width(defaultHeightLine)
                    .background(Neutral_Gray_4)
            )

            Text(
                text = selectedNationCode,
                color = Neutral_Gray_9,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = marginStandard)
            )

            BasicTextField(modifier = Modifier
                .background(
                    color = White,
                )
                .fillMaxWidth()
                .height(heightTextField_36)
                .clearFocusOnKeyboardDismiss {
                    onChange(
                        selectedNationId,
                        if (phoneNumber.isNotEmpty()) "$selectedNationCode$phoneNumber" else ""
                    )
                }
                .focusRequester(focusRequester)
                .bringIntoViewRequester(relocationRequester)
                .onFocusChanged {
                    isFocused.value = it.isFocused
                },
                interactionSource = interactionSource,
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = phoneFormatWithoutZeroFirst(it.trim())
                    onChange(
                        selectedNationId,
                        if (phoneNumber.isNotEmpty()) "$selectedNationCode$phoneNumber" else ""
                    )
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
                        if (phoneNumber.isEmpty())
                            Text(
                                maxLines = 1,
                                text = phoneNumber,
                                style = MaterialTheme.typography.body2,
                                color = if (phoneNumber.isEmpty()) Neutral_Gray_5 else Neutral_Gray_9,
                                textAlign = TextAlign.Start,
                            )
                        innerTextField()
                    }
                },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onChange(
                            selectedNationId,
                            if (phoneNumber.isNotEmpty()) "$selectedNationCode$phoneNumber" else ""
                        )
                    }
                )
            )
        }

        if (isError) {
            Text(
                text = stringResource(id = R.string.tv_err_phone_invalid),
                color = Error_500,
                style = MaterialTheme.typography.overline,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = marginMinimum)
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
            dropdownList.forEach { country ->
                DropdownMenuItem(
                    onClick = {
                        selectedNationFlags = country.flagIconUrl
                        selectedNationCode = country.phonePrefix
                        selectedNationId = country.id
                        expanded = false
                    }
                ) {
                    val painter =
                        rememberImagePainter(data = country.flagIconUrl,
                            builder = {
                                placeholder(R.drawable.ic_baseline_image_24)
                            })
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.size(sizeIcon_28)
                    )
                }
            }
        }
    }
}