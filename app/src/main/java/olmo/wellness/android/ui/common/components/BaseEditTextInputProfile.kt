@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package olmo.wellness.android.ui.common.components
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BaseEditTextInputProfile(
    modifier: Modifier = Modifier,
    hint: String?,
    contentDefault: String? = "",
    onTextChanged: (text: String) -> Unit,
    lastInput: Boolean = false,
    topPadding: Dp = zeroDimen,
    bottomPadding: Dp = zeroDimen,
    isMiniHeight: Boolean?= true,
    isShowTitle: Boolean?= false,
    title: String?= "",
    colorBackground: Color?= White,
    onKeyboardDropFunc: (() -> Unit)?=null){
    Column(modifier = Modifier
        .padding(top = topPadding, bottom = bottomPadding)
        .background(colorBackground ?: White)
        .fillMaxWidth()
        .height(
            if (isMiniHeight == true) {
                68.dp
            } else {
                250.dp
            }
        )){

        if(isShowTitle == true){
            Text(text = title.orEmpty(), style = MaterialTheme.typography.subtitle2.copy(
                color = Neutral_Gray_9,
                fontWeight = FontWeight.Normal,
                fontFamily = MontserratFont,
            ), modifier = Modifier.padding(top = marginStandard,start = marginDouble))
        }

        val focusManager = LocalFocusManager.current
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .onFocusEvent {
                    if (!it.isFocused) {
                        onKeyboardDropFunc?.invoke()
                    }
                }){
                var text by remember {
                    mutableStateOf(TextFieldValue(contentDefault ?: ""))
                }
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                        onTextChanged(text.text)
                    },
                    singleLine = isMiniHeight == true,
                    textStyle = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = MontserratFont,
                        color = Neutral_Gray_9
                    ),
                    placeholder = {
                        Text(
                            text = hint.orEmpty(),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontWeight = FontWeight.Medium,
                                fontFamily = MontserratFont,
                                color = Neutral_Gray_5
                            )
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        textColor = Neutral_Gray_9,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            handleMoveAction(lastInput, focusManager, onKeyboardDropFunc)
                        },
                        onNext = {
                            handleMoveAction(lastInput, focusManager, onKeyboardDropFunc)
                        },
                        onGo = {
                            handleMoveAction(lastInput, focusManager, onKeyboardDropFunc)
                        }
                    ),
                )
        }
    }
}

private fun handleMoveAction(
    lastInput: Boolean,
    focusManager: FocusManager,
    onKeyboardDropFunc: (() -> Unit)?
) {
    if (lastInput) {
        focusManager.clearFocus()
    } else {
        focusManager.moveFocus(FocusDirection.Down)
    }
    onKeyboardDropFunc?.invoke()
}