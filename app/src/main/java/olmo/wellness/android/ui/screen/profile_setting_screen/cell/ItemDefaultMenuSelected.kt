package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

@ExperimentalMaterialApi
@Composable
fun ItemDefaultMenuSelected(
    titleText: String? = null,
    optionTitle: String?= null,
    valueContent: String? = null,
    defaultContent: String? = null,
    hint: String = "",
    isError: Boolean = false,
    isErrorResubmit: Boolean? = false,
    paddingHorizontal: Dp = marginDouble,
    paddingVertical: Dp = marginStandard,
    onChange: (Boolean) -> Unit,
) {
    val expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(defaultContent.orEmpty()) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical)){
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            titleText?.let {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = if (isErrorResubmit == true) Error_500 else Neutral_Gray_9, fontWeight = FontWeight.Bold )) {
                            append(it)
                        }
                        withStyle(style = SpanStyle(color = if (isErrorResubmit == true) Error_500 else Neutral_Gray_5, fontWeight = FontWeight.Normal )) {
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
            if (isErrorResubmit == true){
                SpaceHorizontalCompose(width = 2.dp)
                Image(painter = painterResource(id = R.drawable.ic_exclamation_circle_regular), contentDescription = "")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightTextField_36)
                .border(
                    BorderStroke(
                        width = defaultBorderWidth,
                        color = if (isError) Error_500 else Neutral_Gray_4
                    ),
                    RoundedCornerShape(marginStandard)
                )
                .clickable {
                    onChange.invoke(true)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Text(
                text = if (valueContent.isNullOrEmpty()) hint else valueContent.orEmpty(),
                color = if (valueContent.isNullOrEmpty()) Neutral_Gray_5 else Neutral_Gray_9,
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
        if (isError) {
            Text(
                text = stringResource(id = R.string.error_empty_dropdown_pick),
                color = Error_500,
                style = MaterialTheme.typography.overline,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = marginMinimum)
            )
        }
    }
}

