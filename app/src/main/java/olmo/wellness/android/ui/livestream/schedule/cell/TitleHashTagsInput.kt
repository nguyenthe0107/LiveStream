package olmo.wellness.android.ui.livestream.schedule.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.ui.theme.*


@Composable
fun TitleHashTagsInput(valueTitle : String?,valueHasTag : String?, title: (String) -> Unit, hasTag: (String) -> Unit) {

    var textTitle by remember {
        mutableStateOf(TextFieldValue(valueTitle?:""))
    }

    var textHashTag by remember {
        mutableStateOf(TextFieldValue(valueHasTag?:""))
    }
    Box(modifier = Modifier.padding(top = marginDouble)) {
        Column(
            modifier = Modifier
                .background(color = White)
                .fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White),
                shape = RectangleShape,
                singleLine = true,
                value = textTitle,
                onValueChange = {
                    textTitle = it
                    title.invoke(textTitle.text)
                },
                placeholder = {
                    Text(
                        text = "Add a Title", style = MaterialTheme.typography.subtitle2.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = MontserratFont,
                            color = Neutral_Gray_10
                        )
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = White,
                    textColor = Color(0xFF303037),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                textStyle = MaterialTheme.typography.subtitle2
            )
            Divider(
                color = Neutral_Gray_10,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = marginDouble)
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White),
                singleLine = true,
                value = textHashTag,
                onValueChange = {
                    textHashTag = it
                    hasTag.invoke(textHashTag.text)
                },
                shape = RectangleShape,
                placeholder = {
                    Text(
                        text = "Add hashtags to make your livestream trending ...",
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontWeight = FontWeight.Normal,
                            fontFamily = MontserratFont,
                            fontSize = 12.sp,
                            color = Neutral_Gray_10
                        )
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = White,
                    textColor = Color(0xFF303037),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                textStyle = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 12.sp
                )
            )
        }
    }
}