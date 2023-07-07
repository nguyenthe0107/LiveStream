package olmo.wellness.android.ui.screen.profile_dashboard.component_common

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnrememberedMutableState")
@Composable
fun AreaInputValueProfileCompose(
    contentDefault: String?, focusManager: FocusManager,
    contentChange: (String?) -> Unit
){
    var textInput by remember {
        mutableStateOf(value = TextFieldValue(contentDefault?: ""))
    }
    TextField(
        value = textInput,
        onValueChange = {
            textInput = it
            contentChange.invoke(textInput.text)
        },
        placeholder = {
            Text(
                text = stringResource(R.string.title_hint_enter_input_filed),
                style = MaterialTheme.typography.subtitle1.copy(
                    color = Neutral_Gray_5, fontSize = 13.sp
                ),
            )
        },
        modifier = Modifier
            .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {
                focusManager.clearFocus()
            })
            .padding(horizontal = marginDouble)
            .clip(RoundedCornerShape(30.dp))
            .height(152.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager?.clearFocus()
            },
            onGo = {
                focusManager?.clearFocus()
            },
            onNext = {
                focusManager?.clearFocus()
            }
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color_PURPLE_7F7,
            textColor = Neutral_Gray_9,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        textStyle = MaterialTheme.typography.subtitle1.copy(
            fontSize = 12.sp,
            color = Neutral_Gray_9,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal
        )
    )
}