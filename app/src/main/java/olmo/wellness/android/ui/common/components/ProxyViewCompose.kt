package olmo.wellness.android.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import olmo.wellness.android.ui.theme.marginStandard

@Composable
fun ProxyViewCompose(openPrivacy: (() -> Unit) ?= null) {
    Row(modifier = Modifier, horizontalArrangement = Arrangement.Center){
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Neutral_Gray_9,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(stringResource(id = R.string.tv_privacy_policy))
                }
                withStyle(
                    style = SpanStyle(
                        color = Color_LiveStream_Main_Color,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append( " " + stringResource(id = R.string.tv_privacy_policy_1))
                }
                withStyle(
                    style = SpanStyle(
                        color = Neutral_Gray_9,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(" " + stringResource(id = R.string.tv_and) + "\n")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color_LiveStream_Main_Color,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(stringResource(id = R.string.tv_privacy_policy_2))
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = marginStandard, end = marginStandard).noRippleClickable {
                openPrivacy?.invoke()
            }
        )
    }
}