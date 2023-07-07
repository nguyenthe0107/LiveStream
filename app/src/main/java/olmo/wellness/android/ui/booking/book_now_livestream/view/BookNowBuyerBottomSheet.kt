package olmo.wellness.android.ui.booking.book_now_livestream.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import olmo.wellness.android.ui.booking.book_now_livestream.cell.ItemServiceBookingBuyer
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookNowBuyerBottomSheet(
    serviceBookings: List<ServiceBooking>?,
    bookNowCallback: ((ServiceBooking) -> Unit)? = null,
    openCart: () -> Unit,
    modalBottomSheetState: ModalBottomSheetState,
    onAddCart: ((ServiceBooking) -> Unit)? = null,
    onDetailService: ((ServiceBooking) -> Unit)? = null,
) {

    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = White,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            Scaffold(topBar = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.lb_book_now),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 18.sp, lineHeight = 26.sp
                        ),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    Icon(painter = painterResource(id = R.drawable.ic_cart),
                        contentDescription = "Cart",
                        tint = Color_LiveStream_Main_Color,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(12.dp)
                            .noRippleClickable {
//                                scope.launch {
//                                    modalBottomSheetState.hide()
//                                }
                                openCart.invoke()
                            })

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .align(Alignment.BottomCenter)
                            .background(Gray_EF3)
                    )
                }
            }, modifier = Modifier.fillMaxHeight(0.8f)) {
                if (serviceBookings?.isNotEmpty() == true) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Neutral_Gray_9,
                                        fontFamily = MontserratFont,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                    )
                                ) {
                                    append(stringResource(id = R.string.lb_list_of_services) + ": ")
                                }

                                withStyle(
                                    style = SpanStyle(
                                        color = Neutral_Gray_9,
                                        fontFamily = MontserratFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                ) {
                                    append(serviceBookings.size.toString())
                                }

                            },
                            modifier = Modifier
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        LazyColumn(
                            modifier = Modifier
                        ) {
                            if (serviceBookings.isNotEmpty()) {
                                items(serviceBookings.size, key = {
                                    it.toString()
                                }) { index ->
                                    val item = serviceBookings[index]
                                    ItemServiceBookingBuyer(booking = item,
                                        bookNowCallback = { serviceBooking ->
                                            bookNowCallback?.invoke(serviceBooking)
                                            scope.launch {
                                                modalBottomSheetState.hide()
                                            }
                                        },
                                        onAddCart = {
                                            onAddCart?.invoke(it)
                                        },
                                        onDetailService = {
                                            onDetailService?.invoke(it)
                                        })
                                    Spacer(modifier = Modifier.height(10.dp))

                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(20.dp))

                            }
                        }

                    }
                } else {
                    EmptyData()
                }
            }

        }) {
    }
}