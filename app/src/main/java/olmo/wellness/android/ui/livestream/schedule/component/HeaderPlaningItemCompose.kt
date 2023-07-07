package olmo.wellness.android.ui.livestream.schedule.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import olmo.wellness.android.R
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.components.DividerWithBorder
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.theme.Color_BLUE_OF8
import olmo.wellness.android.ui.theme.Color_PINK_BFB
import olmo.wellness.android.ui.theme.Neutral_Gray_9

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HeaderPlaningItemCompose() {
    val heightDefault = 25.dp
    Card(
        shape = RoundedCornerShape(5.dp),
        backgroundColor = Color_PINK_BFB,
        elevation = 1.dp,
        modifier = Modifier
            .padding(top = 6.dp)
            .defaultMinSize(minHeight = heightDefault)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .defaultMinSize(minHeight = heightDefault)
                .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
        ) {
            val (image, infoRightPosition) = createRefs()
            Row(modifier = Modifier.constrainAs(image){
                start.linkTo(parent.start, 10.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }, verticalAlignment = Alignment.CenterVertically){
                Image(painter = painterResource(id = R.drawable.olmo_ic_sunrise_filled), contentDescription = "header_item_live")
            }
            Column(
                modifier = Modifier
                    .constrainAs(infoRightPosition) {
                        start.linkTo(image.end, 8.dp)
                        centerVerticallyTo(infoRightPosition)
                    }
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Neutral_Gray_9, fontWeight = FontWeight.Bold )) {
                            append("${DateTimeHelper.getSessionDay()}, ")
                        }
                        withStyle(style = SpanStyle(color = Neutral_Gray_9, fontWeight = FontWeight.Normal )) {
                            val username = sharedPrefs.getProfile().username ?: ""
                            sharedPrefs.getUserInfoLocal()
                            append(username)
                        }
                    },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}


