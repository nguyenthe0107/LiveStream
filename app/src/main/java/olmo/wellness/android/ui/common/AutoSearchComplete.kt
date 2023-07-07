@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package olmo.wellness.android.ui.common
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {}
) {
    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .heightIn(max = TextFieldDefaults.MinHeight * 6)
            .padding(horizontal = padding_12, vertical = marginStandard)
    ) {
        item {
            QuerySearch(
                query = query,
                label = queryLabel,
                onQueryChanged = onQueryChanged,
                onDoneActionClick = {
                    view.clearFocus()
                    onDoneActionClick()
                },
                onClearClick = {
                    onClearClick()
                }
            )
        }

        if (predictions.count() > 0) {
            items(predictions) { prediction ->
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            view.clearFocus()
                            onItemClick(prediction)
                        }
                ) {
                    itemContent(prediction)
                }
            }
        }
    }
}

@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    label: String,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit,
    textSize: TextUnit = 16.sp,
    iconSize: Dp = 16.dp,
    corner: Dp = 4.dp,
    showIconSearch: Boolean = true,
    backgroundColor : Color = Neutral_Gray_3,
    iconSearch : Int ?= null,
    iconClose : Int ?= null
){
    Surface(
        shape = RoundedCornerShape(corner),
        modifier = modifier
            .fillMaxWidth(),
        color = backgroundColor
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (showIconSearch) {
                IconButton(modifier = Modifier.defaultMinSize(iconSize, iconSize), onClick = {

                }) {
                    val vectorImage = iconSearch ?: R.drawable.ic_search
                    Icon(
                        imageVector = ImageVector.vectorResource(id = vectorImage),
                        contentDescription = "Search",
                    )
                }
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = query,
                placeholder = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Medium,
                            fontFamily = MontserratFont,
                            color = Color(0xFFAEB1B7),
                            fontSize = textSize
                        )
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Neutral_Gray_3,
                    cursorColor = Color_CUSOR_SEARCH,
                    disabledLabelColor = Neutral_Gray_3,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = onQueryChanged,
                singleLine = true,
                textStyle = MaterialTheme.typography.body2.copy(
                    fontSize = textSize
                ),
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = onClearClick){
                            val vectorImage = iconClose ?: R.drawable.ic_closed_filled
                            Icon(
                                painter = painterResource(id = vectorImage),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(sizeIcon_16)
                            )
                        }
                    }
                },
                keyboardActions = KeyboardActions(onDone = {
                    onDoneActionClick()
                }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                )
            )
        }
    }
}
