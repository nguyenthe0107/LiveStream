@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package olmo.wellness.android.ui.screen.signup_screen.utils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.MontserratFont

@Composable
fun PhoneTextInput(hint: String?, onTextChanged: (text: String) -> Unit?){
    Surface(modifier = Modifier
        .padding(top = 4.dp, bottom = 4.dp)
        .fillMaxWidth()
        .wrapContentHeight(),
        border = BorderStroke(1.dp, Color(0xFFDDDFE3)),
        shape = RoundedCornerShape(10.dp)) {
        var text by remember {
            mutableStateOf(TextFieldValue(""))
        }
        val image = painterResource(id = R.drawable.ic_flag_simple)
        val imageDropMenu = painterResource(id = R.drawable.ic_meu_down)
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(image, contentDescription = "", contentScale = ContentScale.Crop)
            Image(imageDropMenu, contentDescription = "", contentScale = ContentScale.Crop)
            Divider(
                color = Color(0xFFDDDFE3),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            TextField(
                value = text, onValueChange = {
                    text = it
                    onTextChanged(text.text)
                },
                placeholder = { Text(text = hint.orEmpty(),
                    style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Medium, fontFamily = MontserratFont, color = Color(0xFFAEB1B7))) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    textColor = Color(0xFF303037),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )
        }
    }
}

@Preview
@Composable
fun EditText(hint: String?, onTextChanged: (text: String) -> Unit?){
    Surface(modifier = Modifier
        .padding(top = 4.dp, bottom = 4.dp)
        .fillMaxWidth()
        .wrapContentHeight(),
        border = BorderStroke(1.dp, Color(0xFFDDDFE3)),
        shape = RoundedCornerShape(10.dp)) {
        var text by remember {
            mutableStateOf(TextFieldValue(""))
        }
        val image = painterResource(id = R.drawable.ic_flag_simple)
        val imageDropMenu = painterResource(id = R.drawable.ic_meu_down)
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
            ) {
            Image(image, contentDescription = "", contentScale = ContentScale.Crop)
            Image(imageDropMenu, contentDescription = "", contentScale = ContentScale.Crop)
            Divider(
                color = Color(0xFFDDDFE3),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            TextField(
                value = text, onValueChange = {
                    text = it
                    onTextChanged(text.text)
                },
                placeholder = { Text(text = hint.orEmpty(),
                    style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Medium, fontFamily = MontserratFont, color = Color(0xFFAEB1B7))) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    textColor = Color(0xFF303037),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )
        }
    }
}