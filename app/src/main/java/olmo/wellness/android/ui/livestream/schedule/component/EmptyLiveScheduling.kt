package olmo.wellness.android.ui.livestream.schedule.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.Color_LiveStream_Light_Color
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.White

@Composable
fun EmptyLiveScheduling(
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (imgBody, countBadge, explainText) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.img_live_scheduling_body),
            contentDescription = "",
            modifier = Modifier
                .width(200.dp)
                .height(78.dp)
                .constrainAs(imgBody) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = "No live scheduling has been created yet",
            style = MaterialTheme.typography.body2.copy(
                color = Color_LiveStream_Main_Color,
                fontWeight = FontWeight.Thin
            ),
            modifier = Modifier
                .constrainAs(explainText){
                    top.linkTo(imgBody.bottom, 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    Color_LiveStream_Light_Color,
                    shape = CircleShape
                )
                .constrainAs(countBadge) {
                    end.linkTo(imgBody.end, (-16).dp)
                    top.linkTo(imgBody.top, (-16).dp)
                }
        ){
            Text(
                text = "0",
                style = MaterialTheme.typography.h5.copy(
                    color = White
                ),
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}