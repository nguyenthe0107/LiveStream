package olmo.wellness.android.ui.screen.playback_video.donate.view

import android.text.Html
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.White

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfirmWithdrawBottomSheet(modalBottomSheetState: ModalBottomSheetState) {

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 20.dp)
                    .fillMaxHeight(0.7f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Image(
                    painter = painterResource(id = R.drawable.olmo_ic_group_default_welcome_live_stream),
                    contentDescription = "ic_kepler", modifier = Modifier.padding(bottom = 32.dp)
                )

                Text(
                    text = String.format(stringResource(id = R.string.lb_transaction_successfully)),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color_LiveStream_Main_Color,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(12.dp)
                        .clickable {
                        }
                ) {
                    Text(
                        text = stringResource(R.string.lb_select),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 16.sp, color = White, lineHeight = 24.sp
                        ), modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        }) {
    }

}