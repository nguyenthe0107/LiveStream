package olmo.wellness.android.ui.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.*

@Composable
fun SectionButtonDialog(
    titleSuccess : String? = null,
    titleCancel : String? = null,
    onSubmitClick: (String) -> Unit,
    onCancelClick: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = marginDouble, vertical = marginDouble)
            .fillMaxWidth()
            .height(height_60),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                onCancelClick("")
            },
            modifier = Modifier
                .height(height_44)
                .weight(1f)
                .padding(end = marginStandard),
            shape = RoundedCornerShape(marginStandard),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = White
            ),
            border = BorderStroke(defaultBorderWidth, Color_Green_Main)
        ) {
            Text(
                text = titleCancel ?: stringResource(R.string.cancel),
                style = MaterialTheme.typography.button,
                overflow = TextOverflow.Ellipsis,
                color = Color_Green_Main
            )
        }

        Button(
            onClick = {
                onSubmitClick("")
            },
            modifier = Modifier
                .height(height_44)
                .weight(1f),
            shape = RoundedCornerShape(marginStandard),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color_Green_Main
            )
        ) {
            Text(
                text = titleSuccess ?: stringResource(R.string.continue_value),
                style = MaterialTheme.typography.button,
                overflow = TextOverflow.Ellipsis,
                color = White
            )
        }
    }
}