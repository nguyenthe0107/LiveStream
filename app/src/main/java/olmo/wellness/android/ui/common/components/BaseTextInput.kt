@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package olmo.wellness.android.ui.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@OptIn(
    ExperimentalAnimationApi::class,
    androidx.compose.foundation.ExperimentalFoundationApi::class
)
@Composable
fun BaseTextInput(
    modifier: Modifier = Modifier,
    hint: String?,
    title: String = "",
    onTextChanged: (text: String) -> Unit,
    lastInput: Boolean = false,
    onKeyboardDropFunc: (() -> Unit)? = null,
    hasError: Boolean? = false,
    isSuccess: Boolean? = false,
    defaultValue: String ?= null
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()){
        var hasErrorVisible by remember { mutableStateOf(hasError ?: false) }
        var hasTextChange by remember { mutableStateOf(false) }
        hasErrorVisible = hasError ?: false
        val focusManager = LocalFocusManager.current
        val isFocused = remember { mutableStateOf(false) }
        if (title.isNotEmpty()) {
            Text(
                text = title, style = MaterialTheme.typography.subtitle2.copy(
                    color = Neutral_Gray_9,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
            )
            SpaceCompose(height = 5.dp)
        }
        Surface(
            border = BorderStroke(
                1.dp, if (hasErrorVisible && !hasTextChange) Error_500 else {
                    when {
                        isSuccess == true -> {
                            Color_Purple_FBC
                        }
                        hasTextChange -> {
                            Transparent
                        }
                        else -> {
                            Neutral_Gray_4
                        }
                    }
                }
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = modifier
                .fillMaxWidth()
                .onFocusEvent {
                    if (!it.isFocused) {
                        onKeyboardDropFunc?.invoke()
                        hasTextChange = false
                    }
                }) {
            var text by remember {
                mutableStateOf(TextFieldValue(defaultValue ?: ""))
            }
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    onTextChanged(text.text)
                    hasTextChange = true
                },
                singleLine = true,
                maxLines = 1,
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
                    backgroundColor = if(hasTextChange) Color_PURPLE_7F7 else  Color.White ,
                    textColor = Neutral_Gray_9,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        handleMoveAction(lastInput, focusManager, onKeyboardDropFunc)
                        hasTextChange = false
                    },
                    onNext = {
                        handleMoveAction(lastInput, focusManager, onKeyboardDropFunc)
                        hasTextChange = false
                    },
                    onGo = {
                        handleMoveAction(lastInput, focusManager, onKeyboardDropFunc)
                        hasTextChange = false
                    }
                ),
            )
        }
        AnimatedVisibility(visible = hasErrorVisible) {
            Text(
                text = stringResource(id = R.string.tv_err_email_invalid),
                style = MaterialTheme.typography.overline.copy(
                    fontWeight = FontWeight.Medium,
                    fontFamily = MontserratFont,
                    color = Error_500
                ), modifier = Modifier.padding(top = 4.dp)
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