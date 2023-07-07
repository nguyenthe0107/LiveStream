package olmo.wellness.android.ui.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BasePasswordInputNewStyle(
    modifier: Modifier = Modifier,
    labelText: String = "",
    trailingIcon: Int = R.drawable.ic_eye_slash,
    hasError: Boolean = false,
    onTextChanged: (text: String) -> Unit,
    lastInput: Boolean = false,
    isNextStep: Boolean? = false,
    onKeyboardDropFunc: (() -> Unit)? = null,
    contentError: String? = null,
    isSuccess: Boolean? = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val focusManager = LocalFocusManager.current
        val modifierTextFiled = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
        var passwordVisibility: Boolean by remember { mutableStateOf(false) }
        var hasErrorVisible by remember { mutableStateOf(hasError) }
        hasErrorVisible = hasError
        var hasTextChange by remember { mutableStateOf(false) }
        Surface(
            modifier = modifier.onFocusEvent {
                if (!it.isFocused) {
                    onKeyboardDropFunc?.invoke()
                    hasTextChange = false
                }
            },
            border = BorderStroke(
                1.dp,
                if (hasErrorVisible && (!hasTextChange || isNextStep == true)) Error_500 else {
                    when {
                        isSuccess == true -> {
                            Color_Purple_FBC
                        }
                        hasTextChange -> {
                            Color.Transparent
                        }
                        else -> {
                            Gray_FE3
                        }
                    }
                }
            ),
            shape = RoundedCornerShape(50.dp)
        ) {
            var text by remember {
                mutableStateOf(TextFieldValue(""))
            }
            TextField(
                modifier = modifierTextFiled,
                value = text, onValueChange = {
                    text = it
                    onTextChanged(text.text)
                    hasTextChange = true
                    hasErrorVisible = false
                },
                placeholder = {
                    Text(
                        text = labelText, color = Neutral_Gray_5,
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontWeight = FontWeight.Medium,
                            fontFamily = MontserratFont,
                            color = Neutral_Gray_5
                        )
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (lastInput) {
                            focusManager.clearFocus()
                        } else {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        hasTextChange = false
                        onKeyboardDropFunc?.invoke()
                    }
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.subtitle2.copy(
                    fontWeight = FontWeight.Medium,
                    fontFamily = MontserratFont,
                    color = Neutral_Gray_9
                ),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val imageVectorOpen = painterResource(R.drawable.ic_eye_open)
                    val imageVectorClose = painterResource(R.drawable.ic_eye_slash)
                    val image = if (passwordVisibility)
                        imageVectorOpen
                    else imageVectorClose
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            painter = image,
                            contentDescription = "",
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = if (hasTextChange) {
                        Color_PURPLE_7F7
                    } else {
                        Color.White
                    },
                    textColor = Neutral_Gray_9,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )
        }
        AnimatedVisibility(visible = (hasErrorVisible && (!hasTextChange || isNextStep == true))) {
            Text(
                text = if (contentError?.isNotEmpty() == true) {
                    contentError
                } else {
                    stringResource(id = R.string.tv_err_password_incorrect)
                },
                style = MaterialTheme.typography.overline.copy(
                    fontWeight = FontWeight.Medium,
                    fontFamily = MontserratFont,
                    color = Color(0xFFFF3236)
                ), modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}