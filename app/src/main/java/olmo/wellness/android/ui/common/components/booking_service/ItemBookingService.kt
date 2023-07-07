package olmo.wellness.android.ui.common.components.booking_service

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency

@Composable
fun ItemBookingService(
    serviceBooking: ServiceBooking?,
    isLiveStreaming: Boolean? = false,
    onSelected: ((ServiceBooking?) -> Unit)? = null
) {

    val colorSelected =
        if (serviceBooking?.bookmark == 1) Color_Purple_7F4_20 else Color_PINK_BFB
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color_PINK_BFB,
        elevation = 2.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(color = colorSelected)
                .fillMaxWidth()
                .padding(PaddingValues(8.dp))
                .wrapContentHeight()
        )
        {
            val (image, description, money, icon_right, item_bottom_right) = createRefs()
            RoundedAsyncImage(
                imageUrl =((if (serviceBooking?.photos?.isNotEmpty() == true) serviceBooking.photos.firstOrNull()?.url
                    ?: "" else "")),
                cornerRadius = 4.dp,
                modifier = Modifier
                    .constrainAs(image) {
                        linkTo(start = parent.start, end = description.start)
                        top.linkTo(description.top)
                    },
                size = 91.dp
            )
            Column(
                modifier = Modifier.constrainAs(description) {
                    start.linkTo(image.end, 12.dp)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                }
            ) {
//                var titleFinal = serviceBooking?.title ?: ""
//                if (titleFinal.length >= 30) {
//                    titleFinal = titleFinal.substring(0, 29)
//                }
                Text(
                    text =  serviceBooking?.title ?: serviceBooking?.name?:"",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        color = Neutral_Gray_9,
                        fontWeight = FontWeight.Normal,
                        fontFamily = MontserratFont
                    )
                )
            }

            Column(
                modifier = Modifier.constrainAs(money) {
                    start.linkTo(image.end, 12.dp)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            ) {

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
                            append("from")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Black_037,
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                        ) {
                            val currency = serviceBooking?.currency?:"VND"
                            append(" $currency ")
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
                                    serviceBooking?.startingPrice ?: 0f
                                )
                            )
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Neutral_Gray_9,
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        ) {
                            append("/")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Neutral_Gray_9,
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        ) {
                            append(serviceBooking?.sessionType ?: serviceBooking?.durationType?:"")
                        }


                    },
                    modifier = Modifier

                )
//                Text(
//                    text = "from ",
//                    style = MaterialTheme.typography.subtitle1.copy(
//                        fontSize = 10.sp,
//                        lineHeight = 18.sp,
//                    )
//                )
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 10.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    if (serviceBooking.currency?.isNotEmpty() == true) {
//                        Text(
//                            text = serviceBooking.currency.plus("  ") ?: " ",
//                            style = MaterialTheme.typography.subtitle1.copy(
//                                fontSize = 12.sp, lineHeight = 18.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = Neutral_Gray_9
//                            )
//                        )
//                    }
//
//                    if (serviceBooking.startingPrice != null) {
//                        Text(
//                            text = buildAnnotatedString {
//                                withStyle(
//                                    style = SpanStyle(
//                                        color = Color_LiveStream_Main_Color,
//                                        fontSize = 12.sp
//                                    )
//                                ) {
//                                    append(serviceBooking.startingPrice.toString())
//                                }
//                            })
//                    }
//
//                    if (serviceBooking.duration != null && serviceBooking.durationType != null) {
//                        Text(
//                            text = buildAnnotatedString {
//                                withStyle(
//                                    style = SpanStyle(
//                                        fontSize = 10.sp,
//                                        baselineShift = BaselineShift(-0.15f)
//                                    )
//                                ) {
//                                    append(
//                                        " /" + serviceBooking.duration.toString()
//                                            .plus(serviceBooking.durationType)
//                                    )
//                                }
//                            })
//                    }
//                }
            }

            if (isLiveStreaming == null || isLiveStreaming == false) {
                AsyncImage(
                    model = R.drawable.ic_hambuger,
                    colorFilter = ColorFilter.tint(Neutral_Gray_7),
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                        .constrainAs(icon_right) {
                            end.linkTo(parent.end)
                            top.linkTo(description.top)
                        }
                )
            }

            if (isLiveStreaming == true) {
                if (serviceBooking?.bookmark == 1) {
                    Box(
                        modifier = Modifier
                            .constrainAs(item_bottom_right) {
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                    ) {
                        Text(
                            text = "Showing", style = MaterialTheme.typography.subtitle2.copy(
                                color = Color_LiveStream_Main_Color,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                lineHeight = 22.sp
                            )
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .constrainAs(item_bottom_right) {
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .defaultMinSize(minWidth = 62.dp, minHeight = 28.dp)
                            .background(
                                White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color_PURPLE_7F4,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .noRippleClickable {
                                onSelected?.invoke(serviceBooking)
                            }
                            .padding(
                                vertical = 8.dp,
                                horizontal = 12.dp
                            )
                    ) {
                        Text(
                            text = "Show",
                            style = MaterialTheme.typography.subtitle2.copy(
                                color = Color_LiveStream_Main_Color,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}