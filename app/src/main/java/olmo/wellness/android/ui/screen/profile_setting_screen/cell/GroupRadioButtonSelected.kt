package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.*

@Composable
fun GroupRadioButtonSelected(
    modifier: Modifier = Modifier,
    titleText: String,
    selectionList: List<String>, paddingHorizontal: Dp = marginDouble,
    paddingVertical: Dp = marginStandard,
    defaultValue: Boolean? = null,
    swapPosition: Boolean? = false,
    hideTitle: Boolean? = false,
    colorBackground: Color? = White,
    showDivider: Boolean? = false,
    onSelected: (String) -> Unit
) {
    var isSelectedCheckbox by remember { mutableStateOf(false) }
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(selectionList[0]) }
    if (defaultValue != null && defaultValue == true) {
        isSelectedCheckbox = defaultValue
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorBackground ?: White)
            .padding(
                start = paddingHorizontal,
                end = paddingHorizontal,
                top = paddingVertical,
                bottom = paddingVertical
            )
            .padding(
                start = if (showDivider == true) {
                    paddingHorizontal
                } else {
                    0.dp
                },
                end = if (showDivider == true) {
                    0.dp
                } else {
                    paddingHorizontal
                },
                top = if (showDivider == true) {
                    0.dp
                } else {
                    paddingVertical
                },
                bottom = if (showDivider == true) {
                    0.dp
                } else {
                    paddingVertical
                },
            ),
        verticalArrangement = Arrangement.Top
    ) {
        if (hideTitle == false) {
            Text(
                text = titleText,
                color = Neutral_Gray_9,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = marginMinimum)
            )
        }
        selectionList.forEach { text ->
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .selectable(
                            selected = if (isSelectedCheckbox) {
                                (text == selectedOption)
                            } else {
                                false
                            },
                            onClick = {
                                onOptionSelected(text)
                                onSelected(text)
                                isSelectedCheckbox = true
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (swapPosition == true) {
                        Arrangement.SpaceBetween
                    } else {
                        Arrangement.Start
                    }
                ) {
                    if (swapPosition == true) {
                        Text(
                            text = text,
                            color = Color.Black.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.body2,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )

                        RadioButton(
                            selected = if (isSelectedCheckbox) {
                                (text == selectedOption)
                            } else {
                                false
                            },
                            onClick = {
                                onOptionSelected(text)
                                onSelected(text)
                                isSelectedCheckbox = true
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color_LiveStream_Main_Color,
                                unselectedColor = Neutral_Bare_Gray,
                                disabledColor = Neutral_Bare_Gray
                            ),
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 10.dp)
                        )
                    } else {
                        RadioButton(
                            selected = if (isSelectedCheckbox) {
                                (text == selectedOption)
                            } else {
                                false
                            },
                            onClick = {
                                onOptionSelected(text)
                                onSelected(text)
                                isSelectedCheckbox = true
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color_LiveStream_Main_Color,
                                unselectedColor = Neutral_Bare_Gray,
                                disabledColor = Neutral_Bare_Gray
                            ),
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 10.dp)
                        )
                        Text(
                            text = text,
                            color = Color.Black.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.body2,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
                if (showDivider == true) {
                    DividerView(paddingEnd = 0.dp)
                }
            }
        }
    }
}