package olmo.wellness.android.ui.screen.signup_screen.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.*

@SuppressLint("DefaultLocale")
@ExperimentalMaterialApi
@Composable
fun ItemUserTypeMenuSelected(
    titleText: String? = null,
    hint: String = "",
    optionTitle: String?= null,
    valueContent: String? = null,
    defaultContent: String? = null,
    onChange: (Boolean) -> Unit,
) {
    val expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)){
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            titleText?.let {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Neutral_Gray_9, fontWeight = FontWeight.Bold )) {
                            append(it)
                        }
                        withStyle(style = SpanStyle(color = Neutral_Gray_5, fontWeight = FontWeight.Normal )) {
                            if(optionTitle!= null){
                                append(optionTitle)
                            } else{
                                append("")
                            }
                        }
                    },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = marginMinimum)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(marginHep)
                .border(
                    BorderStroke(
                        width = defaultBorderWidth,
                        color =  if (valueContent.isNullOrEmpty()) Neutral_Gray_4 else Color_Purple_FBC
                    ),
                    RoundedCornerShape(50.dp)
                )
                .clickable {
                    onChange.invoke(true)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Text(
                text = if (valueContent.isNullOrEmpty()) hint else valueContent.orEmpty().toLowerCase().capitalize(),
                color = if (valueContent.isNullOrEmpty()) Neutral_Gray_5 else Color_LiveStream_Main_Color,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = padding_12, vertical = marginStandard)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_down_24),
                contentDescription = "Expandable Arrow",
                modifier = Modifier
                    .rotate(if (expanded) 180f else 0f)
                    .padding(horizontal = padding_12, vertical = marginStandard),
                tint = Neutral_Gray_9,
            )
        }
    }
}

