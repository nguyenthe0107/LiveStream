package olmo.wellness.android.ui.screen.verification_success

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.*

@Composable
fun NotificationCompose(
    onSubmitClick: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(marginStandard),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .defaultMinSize(150.dp)
            .padding(start = 16.dp, top = 60.dp, end = 16.dp)
            .background(White),
        elevation = defaultShadow,
        border = BorderStroke(width = 1.dp, color = Color.Transparent)) {
        Box(
            Modifier
                .padding(horizontal = marginDouble)
                .fillMaxWidth()
                .background(White)
        ) {
            Image(
                modifier = Modifier.align(Alignment.TopEnd),
                painter = painterResource(id = R.drawable.ic_close_popup),
                contentDescription = ""
            )
            Column() {
                Text(
                    text = "Verification SIV1 successful",
                    color = Neutral_Gray_9,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(
                            start = marginDouble,
                            end = marginDouble,
                            top = marginDouble
                        )
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = marginDouble, vertical = marginDouble)
                        .fillMaxWidth()
                        .height(height_60),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onSubmitClick?.invoke() },
                        modifier = Modifier
                            .height(height_44)
                            .width(132.dp),
                        shape = RoundedCornerShape(marginStandard),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Tiffany_Blue_500
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.next),
                            style = MaterialTheme.typography.button,
                            overflow = TextOverflow.Ellipsis,
                            color = White
                        )
                    }
                }
            }
        }
    }
}