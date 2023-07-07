package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.toSize
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.components.err_resubmit.ErrorTitleReSubmitCompose
import olmo.wellness.android.ui.theme.*

@ExperimentalMaterialApi
@Composable
fun DropdownMenuSelected(
    titleText: String? = null,
    dropdownList: List<String?>,
    hint: String = "",
    defaultValue: String? = null,
    isError: Boolean = false,
    isErrorResubmit: Boolean? = false,
    paddingHorizontal: Dp = marginDouble,
    paddingVertical: Dp = marginStandard,
    controller: ((MutableState<String>) -> Unit)? = null,
    onChange: (String) -> Unit,
    ) {
    var expanded by remember { mutableStateOf(false) }

    val selectedOptionText = remember {
        mutableStateOf(defaultValue.orEmpty())
    }

    var rowSize by remember { mutableStateOf(Size.Zero) }

    controller?.invoke(
        selectedOptionText
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            titleText?.let {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Neutral_Gray_9, fontWeight = FontWeight.Normal )) {
                            append(it)
                        }
                        withStyle(style = SpanStyle(color = Neutral_Gray_5, fontWeight = FontWeight.Normal )) {
                            append(" " )
                        }
                    },
                    textAlign = TextAlign.Center,
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
                    BorderStroke(
                        width = defaultBorderWidth,
                        color = if (isError) Error_500 else Neutral_Gray_4
                    ),
                    RoundedCornerShape(marginStandard)
                )
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
                text = if (dropdownList.isEmpty()) hint else selectedOptionText.value.ifEmpty { hint },
                color = if (dropdownList.isEmpty()) Neutral_Gray_5 else if (selectedOptionText.value.isEmpty()) Neutral_Gray_5 else Neutral_Gray_9,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = padding_12, vertical = marginStandard)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_down_24),
                contentDescription = "Expandable Arrow",
                modifier = Modifier
                    .rotate(if (expanded) 180f else 0f)
                    .padding(horizontal = padding_12, vertical = marginStandard),
                tint = Neutral_Gray_9,
            )
        }

        if (isError) {
            Text(
                text = stringResource(id = R.string.error_empty_dropdown_pick),
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
                onChange(selectedOptionText.value)
            }
        ) {
            dropdownList.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText.value = selectionOption?:""
                        expanded = false
                        onChange(selectionOption?:"")
                    }
                ) {
                    Text(
                        text = selectionOption?:"",
                        color = if (selectedOptionText.value == selectionOption) Tiffany_Blue_500 else Neutral_Gray_9,
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

