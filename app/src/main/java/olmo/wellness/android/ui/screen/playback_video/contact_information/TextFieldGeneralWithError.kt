package olmo.wellness.android.ui.screen.playback_video.contact_information

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun TextFieldGeneralWithError(
    titleText: String? = null,
    hint: String = "",
    defaultValue: String? = null,
    isValidateError: Boolean = true,
    isError: Boolean = false,
    isErrorResubmit: Boolean? = null,
    errorText: String? = null,
    paddingHorizontal: Dp? = marginDouble,
    paddingVertical: Dp = marginStandard,
    keyboardType: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit){
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
    var value by remember { mutableStateOf(defaultValue) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = paddingHorizontal ?: marginDouble, vertical = paddingVertical)){

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            titleText?.let {
                Text(
                    text = it,
                    color = if(isErrorResubmit == true) Error_500 else Neutral_Gray_9,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = marginMinimum)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
                                fontSize = 14.sp,
                                baselineShift = BaselineShift(+0.15f)
                            )
                        ) {
                            append(" *")
                        }
                    })
            }
            if(isErrorResubmit == true){
                SpaceHorizontalCompose(width = 2.dp)
                Image(painter = painterResource(id = R.drawable.ic_exclamation_circle_regular), contentDescription = "")
            }
        }

        BasicTextField(
            modifier = Modifier
                .background(
                    color = White,
                    shape =  RoundedCornerShape(50.dp)
                )
                .fillMaxWidth()
                .height(heightTextField_48)
                .border(
                    border = BorderStroke(
                        width = defaultBorderWidth,
                        color = if (isValidateError && isError) Error_500 else Neutral_Gray_3
                    ),
                    shape = RoundedCornerShape(50.dp)
                )
                .clearFocusOnKeyboardDismiss {
                    onChange(value ?: "")
                }
                .focusRequester(focusRequester)
                .bringIntoViewRequester(relocationRequester)
                .onFocusChanged {
                    isFocused.value = it.isFocused
                },
            interactionSource = interactionSource,
            value = value ?: "",
            onValueChange = {
                value = it
                onChange(it)
            },
            cursorBrush = SolidColor(Tiffany_Blue_500),
            textStyle = MaterialTheme.typography.body2,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart){
                    if (value.isNullOrEmpty()) {
                        Text(
                            maxLines = 1,
                            text = hint,
                            style = MaterialTheme.typography.body2.copy(fontSize = 12.sp),
                            color = if (value.isNullOrEmpty()) Neutral_Gray_5 else Neutral_Gray_9,
                            textAlign = TextAlign.Start,
                        )
                    }
                    innerTextField()
                }
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = keyboardType,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onChange(value ?: "")
                },
            )
        )

        if (isValidateError && isError) {
            Text(
                text = errorText ?: stringResource(id = R.string.error_empty_textfield),
                color = Error_500,
                style = MaterialTheme.typography.overline.copy(fontSize = 12.sp),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = marginMinimum)
            )
        }
    }
}