package olmo.wellness.android.ui.screen.profile_dashboard.component_common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.ui.theme.Color_gray_3F9
import olmo.wellness.android.ui.theme.Neutral_Gray_5
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import olmo.wellness.android.ui.theme.marginDouble

@Composable
fun InputValueProfileCompose(
    contentDefault: String?, focusManager: FocusManager,
    contentChange: (String?) -> Unit
){
    var text by remember {
        mutableStateOf(TextFieldValue(contentDefault ?: ""))
    }
    TextField(
        value = text,
        onValueChange = {
            text = it
            contentChange.invoke(text.text)
        },
        placeholder = {
            Text(
                text = stringResource(R.string.title_hint_enter_input_filed),
                style = MaterialTheme.typography.subtitle1.copy(
                    color = Neutral_Gray_5, fontSize = 12.sp
                ),
            )
        },
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }),
        modifier = Modifier
            .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {
                focusManager.clearFocus()
            })
            .padding(horizontal = marginDouble)
            .clip(RoundedCornerShape(50.dp))
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color_gray_3F9,
            textColor = Neutral_Gray_9,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        textStyle = MaterialTheme.typography.caption.copy(
            fontSize = 12.sp,
            color = Neutral_Gray_9,
        ),
        singleLine = true
    )
}