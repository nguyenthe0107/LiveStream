package olmo.wellness.android.ui.livestream.schedule.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.DividerWithBorder
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.theme.Color_BLUE_OF8
import olmo.wellness.android.ui.theme.Color_PINK_BFB
import olmo.wellness.android.ui.theme.Neutral_Gray_9

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaningItemCompose(info: LivestreamInfo, callbackFunc: (() -> Unit)?=null) {
    val heightDefault = 80.dp
    Card(
        shape = RoundedCornerShape(5.dp),
        backgroundColor = Color_PINK_BFB,
        elevation = 1.dp,
        modifier = Modifier
            .padding(top = 6.dp)
            .defaultMinSize(minHeight = heightDefault)
            .wrapContentHeight()
            .fillMaxWidth().noRippleClickable {
                callbackFunc?.invoke()
            }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .defaultMinSize(minHeight = heightDefault)
                .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
        ) {
            val (divider, textInfo, infoRightPosition) = createRefs()
            DividerWithBorder(modifier = Modifier.constrainAs(divider){
                start.linkTo(parent.start)
            })
            Row(modifier = Modifier.constrainAs(textInfo){
                start.linkTo(divider.end, 10.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }, verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Neutral_Gray_9, fontWeight = FontWeight.Bold )) {
                            append("${info.title} \n")
                        }
                        withStyle(style = SpanStyle(color = Neutral_Gray_9, fontWeight = FontWeight.Normal )) {
                            append(info.hashtag?:"")
                        }
                    },
                    style = MaterialTheme.typography.caption,
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .constrainAs(infoRightPosition) {
                        end.linkTo(parent.end, 10.dp)
                        centerVerticallyTo(textInfo)
                    }
            ) {
                Text(
                    text = DateTimeHelper.convertToStringHour(info.startTime),
                    style = TextStyle(
                        color = Color_BLUE_OF8,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp
                    )
                )
            }
        }
    }
}
