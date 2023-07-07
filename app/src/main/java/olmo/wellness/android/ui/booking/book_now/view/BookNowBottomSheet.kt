package olmo.wellness.android.ui.booking.book_now.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants.ERROR_COMMON
import olmo.wellness.android.domain.model.booking.BookingWrapperInfo
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.ServiceLocationInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.booking.book_now.time_booking.TimeBookingBottomSheet
import olmo.wellness.android.ui.booking.book_now.viewmodel.BookNowViewModel
import olmo.wellness.android.ui.booking.calendar.CalendarBookingBottomSheet
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.ui.toDate
import olmo.wellness.android.util.getConvertCurrency

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "NewApi",
    "CoroutineCreationDuringComposition"
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookNowBottomSheet(booking: ServiceBooking?,
                       onOpenPlace: ((listPlace: List<ServiceLocationInfo>) -> Unit) ?= null,
                       onOpenNumberCustomer: (() -> Unit) ?= null,
                       onOpenContactInformation: ((BookingWrapperInfo) -> Unit) ?= null,
                       viewModel: BookNowViewModel = hiltViewModel(),
                       modalBottomSheetState: ModalBottomSheetState) {

    val scope = rememberCoroutineScope()
    LaunchedEffect(booking){
        viewModel.onBindServiceBooking(booking) //copy(id = 47)
    }
    val uiState = viewModel.uiState.collectAsState()
    val localContext = LocalContext.current
    val listPlaceBooking = uiState.value.listPlaceBooking
    val locationSelected = uiState.value.serviceLocationSelected
    val customerWrapper = uiState.value.numberCustomerWrapper
    val serviceBooking = uiState.value.serviceBooking
    val datePicked = uiState.value.datedPicked
    val timePicked = uiState.value.kalendarHourMoney
    val numberCustomer = (customerWrapper?.numberOfAdult
            ?.plus(customerWrapper.numberOfChild ?: 0)
            ?.plus(customerWrapper.numberOfOptionalAdult ?: 0)
            ?.plus(customerWrapper.numberOfOptionalChild ?: 0) ?: 0)

    val serviceSessionConfirmInfo = uiState.value.serviceSessionConfirmInfo
    val bookingIdResponseInfo = uiState.value.bookingIdResponseInfo
    val confirmedBooking = uiState.value.confirmedBooking
    val modalPlaceSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalNumberCustomerSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val calendarModalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalTimeHoursSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = White,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            Box() {
                Column(modifier = Modifier) {
                    Row(modifier = Modifier.padding(20.dp)) {
                        RoundedAsyncImage(imageUrl = "", cornerRadius = 4.dp, size = 92.dp)
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
                                    append("From\n")
                                }

                                withStyle(
                                    style = SpanStyle(
                                        color = Black_037,
                                        fontFamily = MontserratFont,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 12.sp
                                    )
                                ) {
                                    append(" " + "VND" + " ")
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
                                .padding(start = 12.dp)
                                .weight(1f)
                                .noRippleClickable {
                                }
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Gray_EF3)
                    )
                    ItemBookNow(title = "Địa điểm", description = "Vui lòng chọn", callBack = {
                        if (listPlaceBooking != null) {
                            onOpenPlace?.invoke(listPlaceBooking)
                        }
                        scope.launch {
                            modalBottomSheetState.hide()
                            modalPlaceSheetState.show()
                        }
                    }, contentAfterSelected = locationSelected?.address.orEmpty())

                    Spacer(modifier = Modifier.height(8.dp))

                    ItemBookNow("Số lượng khách", "Vui lòng chọn", callBack = {
                        onOpenNumberCustomer?.invoke()
                        scope.launch {
                            modalBottomSheetState.hide()
                            modalNumberCustomerSheetState.show()
                        }
                    }, contentAfterSelected = numberCustomer.toString().plus(" khách") )

                    Spacer(modifier = Modifier.height(8.dp))

                    ItemBookNow("Chọn ngày & giờ", "Vui lòng chọn", callBack = {
                        scope.launch {
                            modalBottomSheetState.hide()
                            calendarModalBottomSheetState.show()
                        }
                    }, contentAfterSelected = datePicked?.date?.toString(), timeHour = DateTimeHelper.convertToStringHour(timePicked?.timeStamp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ){
                        Text(
                            text = "Tổng cộng", style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 16.sp, lineHeight = 20.sp
                            ), modifier = Modifier.weight(1f)
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Black_037,
                                        fontFamily = MontserratFont,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp
                                    )
                                ) {
                                    append("VND" + " ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color_LiveStream_Main_Color,
                                        fontFamily = MontserratFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                ) {
                                    append(
                                        getConvertCurrency(
                                            serviceSessionConfirmInfo?.totalPrice ?: 0F
                                        )
                                    )
                                }
                            },
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .weight(1f)
                                .noRippleClickable {
                                }
                        )
                    }

                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color_LiveStream_Main_Color,
                                    fontFamily = MontserratFont,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 10.sp
                                )
                            ) {
                                append("Nếu bạn có nhu cầu xuất hóa đơn vui lòng liên hệ\n\n")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color_LiveStream_Main_Color,
                                    fontFamily = MontserratFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                )
                            ) {
                                append(
                                    "invoice@olmowellness.com"
                                )
                            }
                        },
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        RoundedAsyncImage(
                            imageUrl = booking?.servicePhoto?.firstOrNull().toString(),
                            cornerRadius = 20.dp,
                            size = 50.dp,
                            shape = CircleShape
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        Row(modifier = Modifier.weight(1f)) {
                            SecondLiveButton(
                                modifier = Modifier.weight(1f),
                                text = "Book Now",
                                alignText= Alignment.Center,
                                iconRight = R.drawable.ic_book_now,
                                enable = locationSelected != null && timePicked != null && customerWrapper != null
                            ){
                                viewModel.createBookingId()
                            }
                        }
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
        }) {
    }

    listPlaceBooking?.let {
        PlaceBottomSheet(places = it, modalBottomSheetState = modalPlaceSheetState, onPlaceSelected = { serviceLocation ->
            viewModel.updateLocationBooking(serviceLocation)
            scope.launch {
                modalBottomSheetState.show()
            }
        }, onCancelCallback = {
            scope.launch {
                modalBottomSheetState.show()
            }
        })}

    ClientNumberBottomSheet (numberCustomer = customerWrapper, modalBottomSheetState = modalNumberCustomerSheetState, onCallbackNumberCustomer = { numberCustomer ->
        viewModel.updateNumberCustomer(numberCustomer)
        scope.launch {
            modalBottomSheetState.show()
        }
    }, onCancelCallback = {
        scope.launch {
            modalBottomSheetState.show()
        }
    })

    serviceBooking?.let {
        it.id?.let { it1 ->
            CalendarBookingBottomSheet(serviceId = it1, modalBottomSheetState = calendarModalBottomSheetState, onCallbackCalendarBooking = { calendarBooking ->
                viewModel.updateDateSelected(calendarBooking)
                scope.launch {
                    modalTimeHoursSheetState.show()
                }
            }, onCancelCallback = {
                scope.launch {
                    modalBottomSheetState.show()
                }
            })
        }

        it.id?.let { it1 ->
            datePicked?.let { it2 ->
                TimeBookingBottomSheet(serviceId = it1, kalendarMoney = it2, modalBottomSheetState = modalTimeHoursSheetState, onCallbackTimeBooking = { kalendarMoney ->
                    if(kalendarMoney.money != null){
                        viewModel.updateHourTime(kalendarMoney)
                    }
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                }, onCancelCallback = {
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                })
            }
        }
    }

    /* handle when create booking success */
    if(bookingIdResponseInfo != null && confirmedBooking == true){
        scope.launch {
            modalBottomSheetState.hide()
        }
        val bookingWrapperInfo = BookingWrapperInfo(
            totalMoney = uiState.value.serviceSessionConfirmInfo?.totalPrice ?: 0F,
            bookingId = uiState.value.bookingIdResponseInfo?.bookingId,
            serviceSessionConfirmId = uiState.value.serviceSessionConfirmInfo?.id ?: 0.0,
            sessionSecretId = uiState.value.sessionSecret
        )
        onOpenContactInformation?.invoke(bookingWrapperInfo)
        viewModel.clearBooking()
    }

    if(uiState.value.errorMes?.isNotEmpty() == true){
        showMessage(context = localContext, messageContent = uiState.value.errorMes?: ERROR_COMMON)
    }

}

@Composable
private fun ItemBookNow(title: String,
                        description: String,
                        contentAfterSelected: String?,
                        timeHour: String ?= null,
                        callBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    callBack.invoke()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 14.sp, lineHeight = 17.sp
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if(contentAfterSelected?.isNotEmpty() == true){
                        contentAfterSelected +
                        if(timeHour?.isNotEmpty() == true){
                            " - $timeHour"
                        }else{
                            ""
                        }
                    }else {
                        description
                    },
                    style = MaterialTheme.typography.caption.copy(
                        fontSize = 12.sp, lineHeight = 24.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_down),
                contentDescription = "down"
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray_EF3)
        )
    }
}