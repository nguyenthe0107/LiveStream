package olmo.wellness.android.ui.livestream.schedule.cell

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.*

@ExperimentalMaterialApi
@Composable
fun ItemMenuSelected(
    titleText: String? = null,
    valueContent: String? = null,
    isError: Boolean = false,
    isErrorResubmit: Boolean? = false,
    paddingHorizontal: Dp = marginStandard,
    paddingVertical: Dp = marginStandard,
    onChange: (Boolean) -> Unit,
) {
    val expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightTextField_36)
                .clickable {
                    onChange.invoke(true)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (!titleText.isNullOrBlank()) titleText else "",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 14.sp
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = padding_12, vertical = marginStandard))

            if (!valueContent.isNullOrBlank()){
                Text(text = valueContent,textAlign = TextAlign.Right, style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 12.sp
                ), modifier = Modifier.weight(1f))
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right_thin),
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

