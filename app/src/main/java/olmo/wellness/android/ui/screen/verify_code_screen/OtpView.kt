package olmo.wellness.android.ui.screen.verify_code_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.ui.theme.*

const val OTP_VIEW_TYPE_UNDERLINE = 1
const val OTP_VIEW_TYPE_BORDER = 2

@Composable
fun OtpView(
    modifier: Modifier = Modifier,
    otpText: String?="",
    charColor: Color = Color.Black,
    charBackground: Color = Color.Transparent,
    charSize: TextUnit = 16.sp,
    containerSize: Dp = charSize.value.dp * 2,
    otpCount: Int = 6,
    type: Int = OTP_VIEW_TYPE_UNDERLINE,
    enabled: Boolean = true,
    password: Boolean = false,
    passwordChar: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    onOtpTextChange: ((String) -> Unit) ?= null
) {
    val text = remember {
        mutableStateOf(
            TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        )
    }
    var isSet by remember {
        mutableStateOf(false)
    }
    if(otpText?.isNotEmpty() == true && text.value.text.isEmpty() && !isSet){
        text.value = text.value.copy(text = otpText, selection = TextRange(otpText.length))
        isSet = true
    }
    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = text.value,
        onValueChange = {
            if(isSet){
                text.value = text.value.copy(text = "", selection = TextRange(0))
            }
            if(it.text.length <= 6){
                text.value = text.value.copy(text = it.text, selection = TextRange(it.text.length))
                onOtpTextChange?.invoke(text.value.text)
            }
        },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                repeat(otpCount) { index ->
                    Row(modifier = Modifier.weight(1f)) {
                        Spacer(modifier = Modifier.width(2.dp))
                        CharView(
                            index = index,
                            text = text.value.text,
                            charColor = charColor,
                            charSize = charSize,
                            containerSize = containerSize,
                            type = type,
                            charBackground = charBackground,
                            password = password,
                            passwordChar = passwordChar,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
            }
        })
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    charColor: Color,
    charSize: TextUnit,
    containerSize: Dp,
    type: Int = OTP_VIEW_TYPE_UNDERLINE,
    charBackground: Color = Color.Transparent,
    password: Boolean = false,
    passwordChar: String = ""){

    val modifier = if (type == OTP_VIEW_TYPE_BORDER) {
        Modifier
            .defaultMinSize(minWidth = containerSize)
            .border(
                width = 1.dp,
                color = charColor,
                shape = MaterialTheme.shapes.medium
            )
            .padding(bottom = 4.dp)
            .background(charBackground)
    } else Modifier
        .width(containerSize)
        .background(charBackground)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val char = when {
            index >= text.length -> ""
            password -> passwordChar
            else -> text[index].toString()
        }
        Text(
            text = char,
            color = charColor,
            modifier = modifier.wrapContentHeight(),
            style = MaterialTheme.typography.h5.copy(
                color = Neutral_Gray_9,
                fontFamily = MontserratFont,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Medium
            ),
            fontSize = charSize,
            textAlign = TextAlign.Center,
            fontFamily = MontserratFont,
        )
        if (type == OTP_VIEW_TYPE_UNDERLINE) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .background(Color_Purple_FBC)
                    .height(1.5.dp)
                    .width(containerSize)
            )
        }
    }
}