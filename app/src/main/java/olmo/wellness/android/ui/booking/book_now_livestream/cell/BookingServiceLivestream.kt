package olmo.wellness.android.ui.booking.book_now_livestream.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.livestream.chatlivestream.view.WidthView
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency


@Composable
fun BookingService(booking: ServiceBooking?, isBuyer: Boolean,
                   onBookingCallback: ((ServiceBooking?) -> Unit)? = null,
                   onCloseBook: (() -> Unit)? = null) {
    Box(modifier = Modifier
        .width(WidthView)
        .background(color = White, shape = RoundedCornerShape(4.dp))
        .height(90.dp)
        .noRippleClickable {
            onBookingCallback?.invoke(booking)
        }
        .padding()) {

        if (!isBuyer) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                contentDescription = "close", tint = Neutral_Gray_5, modifier = Modifier
                    .noRippleClickable {
                        onCloseBook?.invoke()
                    }
                    .padding(5.dp)
                    .align(Alignment.TopEnd)
                    .size(15.dp)
            )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {
            if (!isBuyer) {
                Row(modifier = Modifier
                    .background(brush = Brush.horizontalGradient(
                        listOf(
                            Color_Purple_ECD,
                            Color_Purple_2E8,
                        )
                    ), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .align(Alignment.BottomEnd), verticalAlignment = Alignment.CenterVertically) {

                    Text(text = stringResource(id = R.string.lb_book),
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = White, fontSize = 8.sp, lineHeight = 16.sp
                        ))

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(painter = painterResource(id = R.drawable.ic_cart),
                        contentDescription = "cart",
                        tint = White,
                        modifier = Modifier.size(15.dp))

                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                val img = ((if (booking?.photos?.isNotEmpty() == true) booking.photos.firstOrNull()?.url
                    ?: "" else ""))
                RoundedAsyncImage(imageUrl = img,
                    cornerRadius = 4.dp,
                    size = 70.dp)

                Column(modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp), verticalArrangement = Arrangement.SpaceBetween) {

                    Text(text = booking?.name ?: "",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        style = MaterialTheme.typography.subtitle1.copy(
                            lineHeight = 16.sp, fontSize = 10.sp
                        ),
                        modifier = Modifier.weight(1f))

                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Neutral_Gray_9,
                                    fontFamily = MontserratFont,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 8.sp
                                )
                            ) {
                                append("\nFrom\n")
                            }

                            withStyle(
                                style = SpanStyle(
                                    color = Black_037,
                                    fontFamily = MontserratFont,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp
                                )
                            ) {
                                append(booking?.currency + " ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color_LiveStream_Main_Color,
                                    fontFamily = MontserratFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            ) {
                                append(
                                    getConvertCurrency(
                                        booking?.startingPrice ?: 0f
                                    )
                                )
                            }
//                            withStyle(
//                                style = SpanStyle(
//                                    color = Neutral_Gray_9,
//                                    fontFamily = MontserratFont,
//                                    fontWeight = FontWeight.Normal,
//                                    fontSize = 8.sp
//                                )
//                            ) {
//                                append("/"+booking?.durationType)
//                            }

                        },
                        modifier = Modifier
                    )
                }


            }
        }

    }

}