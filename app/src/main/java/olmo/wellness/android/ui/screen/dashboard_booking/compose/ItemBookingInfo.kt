package olmo.wellness.android.ui.screen.dashboard_booking.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.domain.model.booking.BookingHistoryInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.screen.signup_screen.utils.DividerHorizontal
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.Color_PURPLE_7F4
import olmo.wellness.android.ui.theme.Neutral_Gray_3
import olmo.wellness.android.ui.theme.Neutral_Gray_9

@Composable
fun ItemBookingInfo(bookingInfo: BookingHistoryInfo, navigationDetails: ((Int) -> Unit) ?= null){
    val serviceSessionInfo by remember {
        mutableStateOf(bookingInfo.serviceSessionInfo)
    }
    val serviceInfo by remember {
        mutableStateOf(bookingInfo.serviceSessionInfo?.serviceInfo)
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .background(
            color = Color.White,
            shape = RoundedCornerShape(8.dp)
        )
        .noRippleClickable {
            navigationDetails?.invoke(1)
        }
        .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = Color_PURPLE_7F4)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            RoundedAsyncImage(imageUrl = serviceInfo?.servicePhoto.orEmpty(), cornerRadius = 4.dp, size = 91.dp, modifier = Modifier.padding(end = 12.dp))
            Column(modifier = Modifier) {
                Text(
                    text = serviceSessionInfo?.title.orEmpty(),
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Neutral_Gray_9,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                SpaceCompose(height = 8.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Transaction ID",
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 10.sp,
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = serviceSessionInfo?.id.toString(), style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 10.sp,
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                SpaceCompose(height = 12.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Booking ID",
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 10.sp,
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = serviceSessionInfo?.id.toString(), style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 10.sp,
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                SpaceCompose(height = 30.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "Người lớn", style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 10.sp,
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = "x"+serviceSessionInfo?.numberOfAdult, style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 10.sp,
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Text(
                        text = "VND " + serviceSessionInfo?.adultPrice,
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 10.sp,
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                if(serviceSessionInfo?.numberOfChild != null && serviceSessionInfo?.numberOfChild != 0){
                    SpaceCompose(height = 12.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Trẻ em", style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 10.sp,
                                color = Neutral_Gray_9,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Text(
                            text = "x" + serviceSessionInfo?.numberOfChild, style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 10.sp,
                                color = Neutral_Gray_9,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Text(
                            text = "VND "+ serviceSessionInfo?.childPrice,
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 10.sp,
                                color = Neutral_Gray_9,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
                SpaceCompose(height = 18.dp)
                DividerHorizontal(color = Neutral_Gray_3)
                SpaceCompose(height = 15.dp)
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "Total", style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 12.sp,
                            color = Neutral_Gray_9
                        ), modifier = Modifier.padding(end = 13.dp)
                    )
                    Text(
                        text = "VND", style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 12.sp,
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(
                        text = serviceSessionInfo?.total.toString(), style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 12.sp,
                            color = Color_PURPLE_7F4,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}