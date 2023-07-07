package olmo.wellness.android.ui.common.components.dialog_confirm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionDialogUI(
    title: String,
    description: String,
    titleBtn1: String ,
    titleBtn2: String,
    btn1Callback: ((Boolean) -> Unit)? = null,
    btn2Callback: ((Boolean) -> Unit)? = null
) {

    val gradient = Brush.horizontalGradient(
        listOf(
            Color_LiveStream_Main_Color,
//            Color_LiveStream_Main_Color,
            Color_LiveStream_Light_Color,
        )
    )
    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(gradient)
        ) {
            Text(
                text = title,
                color = White,
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 18.sp,
                    lineHeight = 26.sp
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(end = marginDouble, start = marginDouble, top = 15.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = description,
                color = White,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = marginDouble, vertical = padding_10)
                    .align(Alignment.CenterHorizontally).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = marginDouble)
                    .fillMaxWidth()
                    .height(height_60),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = titleBtn1,
                    style = MaterialTheme.typography.button.copy(
                        fontSize = 16.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    color = White,
                    modifier = Modifier
                        .height(height_44)
                        .weight(1f)
                        .padding(end = marginStandard)
                        .noRippleClickable {
                            btn1Callback?.invoke(true)
                        }
                        .clip(RoundedCornerShape(50.dp))
                        .border(width = 1.dp, shape = RoundedCornerShape(50), color = White)
                        .background(color = Transparent, shape =  RoundedCornerShape(50))
                        .padding(12.dp)
                )


                Text(
                    text = titleBtn2,
                    style = MaterialTheme.typography.button.copy(
                        fontSize = 16.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    color = Color_Purple_FBC,
                    modifier = Modifier
                        .height(height_44)
                        .weight(1f)
                        .noRippleClickable{
                            btn2Callback?.invoke(true)
                        }
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            color = White, shape =  RoundedCornerShape(50))
                        .padding(12.dp)
                )
            }
        }
    }


}