package olmo.wellness.android.ui.booking.book_now_livestream.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency

@Composable
fun ItemServiceBookingBuyer(
    booking: ServiceBooking?,
    bookNowCallback: ((ServiceBooking) -> Unit)? = null,
    onAddCart: ((ServiceBooking) -> Unit)? = null,
    onDetailService: ((ServiceBooking) -> Unit)? = null,
) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(110.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color_PINK_BFB, elevation = 2.dp) {
        Row(modifier = Modifier
            .padding(10.dp)
            .noRippleClickable {
                if (booking != null) {
                    onDetailService?.invoke(booking)
                }
            }, horizontalArrangement = Arrangement.SpaceBetween) {
            val photo = ((if (booking?.photos?.isNotEmpty() == true) booking.photos.firstOrNull()?.url
                ?: "" else ""))
            RoundedAsyncImage(imageUrl = photo,
                cornerRadius = 4.dp,
                size = 90.dp)
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp), verticalArrangement = Arrangement.SpaceBetween) {

                Text(text = booking?.name ?: "", style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp, lineHeight = 20.sp
                ), maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))


                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Neutral_Gray_9,
                                    fontFamily = MontserratFont,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 8.sp,
                                )
                            ) {
                                append(stringResource(R.string.lb_from) + "\n")
                            }

                            withStyle(
                                style = SpanStyle(
                                    color = Neutral_Gray_9,
                                    fontFamily = MontserratFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            ) {
                                append("VND" + " ")
                            }

                            withStyle(
                                style = SpanStyle(
                                    color = Color_LiveStream_Main_Color,
                                    fontFamily = MontserratFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            ) {
                                append(getConvertCurrency(booking?.startingPrice ?: 0f))
                            }

                        },
                        modifier = Modifier
                    )

                    Row(modifier = Modifier
                        .height(20.dp)
                        .width(100.dp)
                        .border(width = 1.dp,
                            shape = RoundedCornerShape(4.dp),
                            color = Color_LiveStream_Main_Color),
                        verticalAlignment = Alignment.CenterVertically) {

                        Box(modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .noRippleClickable {
                                if (booking != null) {
                                    onAddCart?.invoke(booking)
                                }
                            }) {
                            Icon(painter = painterResource(id = R.drawable.ic_cart),
                                contentDescription = "cart",
                                tint = Color_LiveStream_Main_Color,
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(color = White,
                                        shape = RoundedCornerShape(topStart = 4.dp,
                                            bottomStart = 4.dp))
                                    .align(Alignment.Center))
                        }

                        Box(modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .noRippleClickable {
                                if (booking != null) {
                                    bookNowCallback?.invoke(booking)
                                }
                            }
                            .background(color = Color_LiveStream_Main_Color,
                                shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))) {

                            Text(text = stringResource(id = R.string.lb_book),
                                style = MaterialTheme.typography.subtitle2.copy(color = White,
                                    fontSize = 8.sp,
                                    lineHeight = 10.sp),
                                modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center)
                        }
                    }

                }

            }
        }
    }
}