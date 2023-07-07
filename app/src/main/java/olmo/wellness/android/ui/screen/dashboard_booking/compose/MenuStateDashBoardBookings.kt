package olmo.wellness.android.ui.screen.dashboard_booking.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.domain.model.state_dashboard_booking.StatusBookingDashBoardModel
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.signup_screen.utils.DividerHorizontal
import olmo.wellness.android.ui.theme.Color_GRAY_HALF_BLACK
import olmo.wellness.android.ui.theme.Neutral_Gray_3
import olmo.wellness.android.ui.theme.Neutral_Gray_9

@Composable
fun MenuStateDashBoardBookings(listMenu: List<StatusBookingDashBoardModel>, state: Boolean, onSelectMenu: ((String) -> Unit) ?= null) {
    AnimatedVisibility(visible = state){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp)
            .background(
                color = Color_GRAY_HALF_BLACK
            )){
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
                    color = Color.White
                )
                .wrapContentHeight()){
                LazyColumn(content = {
                    items(listMenu.size){ index ->
                        Column(modifier = Modifier
                            .fillMaxWidth()) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .noRippleClickable {
                                    onSelectMenu?.invoke(listMenu[index].type)
                                }
                                .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(text = stringResource(id = listMenu[index].name), style = MaterialTheme.typography.subtitle2.copy(
                                    color = Neutral_Gray_9,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                ))
                            }
                            if(index != listMenu.size - 1){
                                DividerHorizontal(color = Neutral_Gray_3)
                            }
                        }
                    }
                })
            }
        }
    }
}