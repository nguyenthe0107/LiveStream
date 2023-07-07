package olmo.wellness.android.ui.chat.conversation_list.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.*

@Composable
fun SearchConversation(hint : String,onTextChange: (String) -> Unit) {
    val value = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    TextField(
        value = value.value,
        onValueChange = {
            onTextChange.invoke(it)
            value.value = it
        },
        textStyle = MaterialTheme.typography.subtitle2,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search_home),
                null,
                tint = Color_gray_6CF
            )
        },
        trailingIcon = {
            if (value.value.isNotBlank()) {
                Icon(
                    Icons.Filled.Close,
                    null,
                    tint = Color_gray_6CF,
                    modifier = Modifier.clickable {
                        value.value = ""
                        onTextChange.invoke("")
                        focusManager.clearFocus()
                    })
            }
        },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(Neutral_Gray, RoundedCornerShape(30.dp)),
        singleLine = true,
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = Color_D6D
                ),
                modifier = Modifier,
                textAlign = TextAlign.Center
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
            },

            ),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            cursorColor = Color.Black
        )
    )
}