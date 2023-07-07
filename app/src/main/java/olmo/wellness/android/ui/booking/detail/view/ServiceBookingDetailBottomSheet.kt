package olmo.wellness.android.ui.booking.detail.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.common.components.live_button.ButtonIcon
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServiceBookingDetailBottomSheet(
    booking: ServiceBooking?,
    onAddToCart : (ServiceBooking)->Unit,
    onBookNow :(ServiceBooking)->Unit,
    modalBottomSheetState: ModalBottomSheetState,
) {

    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val width = (configuration.screenWidthDp).dp

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = White,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            Scaffold(
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(White)
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundedAsyncImage(
                            imageUrl = "",
                            cornerRadius = 20.dp,
                            size = 50.dp,
                            shape = CircleShape
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        Row(modifier = Modifier.weight(1f)) {

                            ButtonIcon(title = stringResource(id = R.string.lb_add_to_cart),
                                iconRight = R.drawable.ic_cart,
                                modifier = Modifier.weight(1f),
                                onClickFunc = {
                                    if (booking != null) {
                                        onAddToCart.invoke(booking)
                                    }
                                })

                            Spacer(modifier = Modifier.width(16.dp))

                            ButtonIcon(title = stringResource(id = R.string.lb_book_now),
                                iconRight = R.drawable.ic_book_now,
                                modifier = Modifier.weight(1f),
                                onClickFunc = {
                                    if (booking != null) {
                                        onBookNow.invoke(booking)
                                    }
                                })

                        }
                    }
                },
                modifier = Modifier.fillMaxHeight()
            ) {
                Box{
                    LazyColumn(modifier = Modifier) {
                        item {
                            RoundedAsyncImage(
                                imageUrl = ((if (booking?.photos?.isNotEmpty() == true) booking.photos.firstOrNull()?.url
                                    ?: "" else "")),
                                cornerRadius = 24.dp,
                                size = width,
                                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                            )
                            Column(modifier = Modifier.padding(20.dp)) {

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    color = Neutral_Gray_9,
                                                    fontFamily = MontserratFont,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 12.sp
                                                )
                                            ) {
                                                append("From")
                                            }

                                            withStyle(
                                                style = SpanStyle(
                                                    color = Black_037,
                                                    fontFamily = MontserratFont,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 12.sp
                                                )
                                            ) {
                                                val currency= booking?.currency?:"VND"
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
                                                        booking?.startingPrice ?: 0f
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
                                                append(booking?.sessionType ?: "")
                                            }


                                        },
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .weight(1f)
                                            .noRippleClickable {
                                            }
                                    )

                                    Row(modifier = Modifier.padding(start = 12.dp)) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_share_solid),
                                            contentDescription = "share",
                                            tint = Color_LiveStream_Main_Color,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_cart),
                                            contentDescription = "Cart",
                                            tint = Color_LiveStream_Main_Color,
                                            modifier = Modifier.size(20.dp).noRippleClickable {
                                                if (booking != null) {
                                                    onAddToCart.invoke(booking)
                                                }
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    color = Black_037,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 12.sp
                                                )
                                            ) {
                                                append("Được xác thực bởi" + " ")
                                            }


                                            withStyle(
                                                style = SpanStyle(
                                                    color = Color_LiveStream_Main_Color,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp
                                                )
                                            ) {
                                                append(stringResource(id = R.string.app_name))
                                            }
                                        },
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(end = 12.dp)
                                    )

                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_reward),
                                        contentDescription = "reward",
                                        tint = Color_LiveStream_Main_Color
                                    )

                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(modifier = Modifier) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_currency),
                                        contentDescription = "currency",
                                        tint = Color_LiveStream_Main_Color
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))

                                    Text(
                                        text = "Giao dịch nhanh chóng",
                                        style = MaterialTheme.typography.subtitle1.copy(
                                            lineHeight = 24.sp,
                                            fontSize = 12.sp
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(modifier = Modifier) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_tag),
                                        contentDescription = "Tag",
                                        tint = Color_LiveStream_Main_Color
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))

                                    Text(
                                        text = "Xác nhận tức thời",
                                        style = MaterialTheme.typography.subtitle1.copy(
                                            lineHeight = 24.sp,
                                            fontSize = 12.sp
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(modifier = Modifier) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_time),
                                        contentDescription = "time",
                                        tint = Color_LiveStream_Main_Color
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))

                                    Text(
                                        text = "Thời lượng 1 giờ",
                                        style = MaterialTheme.typography.subtitle1.copy(
                                            lineHeight = 24.sp,
                                            fontSize = 12.sp
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Về dịch vụ này",
                                    style = MaterialTheme.typography.subtitle2.copy(
                                        fontSize = 14.sp, lineHeight = 24.sp
                                    )
                                )

                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = booking?.description?:"",
                                    style = MaterialTheme.typography.subtitle1.copy(
                                        fontSize = 12.sp, lineHeight = 16.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }

                    Icon(painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "close",
                        tint = Black_037,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .noRippleClickable {
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                            })
                }

            }


        }) {

    }
}