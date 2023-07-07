package olmo.wellness.android.ui.screen.dashboard_booking.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.BookingHistoryInfo
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.ServiceBookingForCart
import olmo.wellness.android.domain.model.booking.ServiceLocationInfo
import olmo.wellness.android.domain.model.payment.PaymentStatus
import olmo.wellness.android.domain.model.state_dashboard_booking.StatusBookingDashBoardModel
import olmo.wellness.android.domain.model.wrapper.NumberCustomerWrapper
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.booking.calendar.CalendarBookingBottomSheet
import olmo.wellness.android.ui.booking.book_now.view.ClientNumberBottomSheet
import olmo.wellness.android.ui.booking.book_now.view.PlaceBottomSheet
import olmo.wellness.android.ui.booking.book_now.time_booking.TimeBookingBottomSheet
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.extensions.showMessage
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.screen.signup_screen.utils.DividerHorizontal
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.ui.toDate
import java.time.LocalDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "NewApi",
    "CoroutineCreationDuringComposition"
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyServiceBookingDetails(
    bookingInfo: BookingHistoryInfo,
    navController: NavHostController,
    viewModel: MyDashBoardBookingDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit){
        viewModel.onBindBookingDefault(bookingInfo)
    }
    val configuration = LocalConfiguration.current
    val width = (configuration.screenWidthDp).dp
    val mainScope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()
    val bookingDetail: BookingHistoryInfo ?= uiState.value.bookingDetailItem
    val storeBookingDetail: ServiceBookingForCart?= uiState.value.storeBookingInfo
    val placeDetail: ServiceLocationInfo?= uiState.value.placeInfoBooked
    val listPlaceSelection = uiState.value.listPlaceInfo
    val datePicked = uiState.value.kalendarMoney
    val context = LocalContext.current

    val updateSuccess = uiState.value.updateSuccess
    val errorMes = uiState.value.contentError

    val addressModalBottomSheet =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            })

    val optionQuantityCustomerModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            true
        })

    val calendarModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            true
        })

    val dateDetailModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            true
        })

    val cancelRequestModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            true
        })

    val scheduleRequestModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            true
        })


    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(modifier = Modifier.weight(1f)) {
                    SecondLiveButton(
                        modifier = Modifier.weight(1f),
                        text = "Cancel",
                        radius = 50.dp,
                        alignText = Alignment.Center,
                        borderColor = Color_Red_F33,
                        textColor = Color_Red_F33
                    ){
                        mainScope.launch {
                            cancelRequestModalBottomSheetState.show()
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    PrimaryLiveButton(
                        modifier = Modifier.weight(1f),
                        text = "Reschedule",
                        textColor = Color.White
                    ){
                       mainScope.launch {
                           addressModalBottomSheet.show()
                       }
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier) {
            LazyColumn(modifier = Modifier) {
                item {
                    RoundedAsyncImage(
                        imageUrl = bookingDetail?.serviceSessionInfo?.serviceInfo?.servicePhoto.orEmpty(),
                        cornerRadius = 0.dp,
                        size = width
                    )
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
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
                                        append(bookingDetail?.serviceSessionInfo?.title.orEmpty())
                                    }
                                },
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .weight(1f)
                            )

                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                val rememberResource = rememberAsyncImagePainter(model = R.drawable.olmo_ic_group_default_place_holder)
                                AsyncImage(
                                    model = bookingDetail?.serviceSessionInfo?.serviceInfo?.servicePhoto.orEmpty(),
                                    contentDescription = "share",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(35.dp),
                                    error = rememberResource,
                                    contentScale = ContentScale.Crop,
                                    placeholder = rememberResource,
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = storeBookingDetail?.name.orEmpty(),
                                    style = MaterialTheme.typography.subtitle2.copy(
                                        color = Color_LiveStream_Main_Color,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        /* Description Part */
                        ItemDescriptionStatusBooking(title = stringResource(id = R.string.title_status_booking_details), status = bookingDetail?.bookingInfo?.status.orEmpty())
                        Spacer(modifier = Modifier.height(16.dp))
                        ItemDescriptionBooking(title = stringResource(id = R.string.title_id_booking_details), content = bookingDetail?.bookingInfo?.id.toString())
                        Spacer(modifier = Modifier.height(16.dp))
                        val timeString = bookingDetail?.serviceSessionInfo?.sessionTimestamp?.let {
                            DateTimeHelper.convertDataToString(
                                it,
                                DateTimeHelper.FORMAT_TIME_BIRTHDAY
                            )
                        }
                        timeString?.let { it1 ->
                            ItemDescriptionBooking(title = stringResource(id = R.string.title_time_created_booking_details),
                                content = it1
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        ItemQuantityBooking(title = stringResource(id = R.string.title_quantity_customers_booking_details),
                            content = "Người lớn x" + bookingDetail?.serviceSessionInfo?.numberOfAdult)
                        Spacer(modifier = Modifier.height(16.dp))
                        ItemDescriptionBooking(title = stringResource(id = R.string.title_duration_booking_details),
                            content = "" + bookingDetail?.serviceSessionInfo?.sessionDuration + " " +
                                    bookingDetail?.serviceSessionInfo?.sessionType)
                        Spacer(modifier = Modifier.height(16.dp))
                        ItemDescriptionBooking(title = stringResource(id = R.string.title_address_booking_details),
                            content = placeDetail?.address.orEmpty())

                        /* Next Phase */
                        Spacer(modifier = Modifier.height(24.dp))
                        DividerHorizontal(color = Neutral_Gray_3)
                        Spacer(modifier = Modifier.height(16.dp))

                        ItemDescriptionBooking(title = stringResource(id = R.string.title_name_customer_booking_details),
                            content = bookingDetail?.userInfo?.username.orEmpty())
                        Spacer(modifier = Modifier.height(16.dp))
                        ItemDescriptionBooking(title = stringResource(id = R.string.title_phone_customer_booking_details),
                            content = bookingDetail?.userInfo?.phoneNumber.orEmpty())
                        Spacer(modifier = Modifier.height(16.dp))
                        ItemDescriptionBooking(title = stringResource(id = R.string.title_email_customer_booking_details),
                            content = bookingDetail?.userInfo?.email.orEmpty())
                        Spacer(modifier = Modifier.height(16.dp))
                        if(bookingDetail?.paymentInfo != null){
                            ItemDescriptionBooking(title = stringResource(id = R.string.title_payment_method_customer_booking_details),
                                content = "" + bookingDetail.paymentInfo.paymentMethod)
                            Spacer(modifier = Modifier.height(16.dp))
                            ItemDescriptionStatusPayment(title = stringResource(id = R.string.title_status_payment_method_booking_details),
                                status = bookingDetail.paymentInfo.paymentStatus)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.ic_back_left_main_color),
                contentDescription = "", modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 16.dp)
                    .noRippleClickable {
                        navController.popBackStack()
                    }
                    .size(28.dp)
            )

        }
    }

    CancelRequestBookingBottomSheet(modalBottomSheetState = cancelRequestModalBottomSheetState, callbackConfirmed = { status ->
        if(status){
            viewModel.requestDeleteBooking()
        }
    })

    /* Select Address  */
    listPlaceSelection?.let { PlaceBottomSheet(places = it, modalBottomSheetState = addressModalBottomSheet, onPlaceSelected = { addressInfo ->
        if(addressInfo != null){
            viewModel.onBindPlaceSelected(addressInfo)
            mainScope.launch {
                optionQuantityCustomerModalBottomSheetState.show()
            }
        }
    })}

    /* Select option quantity customer */
    val numberCustomerWrapper = NumberCustomerWrapper(
        numberOfAdult = bookingDetail?.serviceSessionInfo?.numberOfAdult,
        numberOfChild = bookingDetail?.serviceSessionInfo?.numberOfChild,
        numberOfOptionalAdult = bookingDetail?.serviceSessionInfo?.numberOfOptionalAdult,
        numberOfOptionalChild = bookingDetail?.serviceSessionInfo?.numberOfOptionalChild,
        timeStamp = System.currentTimeMillis()
    )
    ClientNumberBottomSheet(
        numberCustomer = numberCustomerWrapper,
        onCallbackNumberCustomer = { wrapper ->
            viewModel.updateNumberCustomer(wrapper)
            mainScope.launch {
                calendarModalBottomSheetState.show()
            }
        },
        modalBottomSheetState = optionQuantityCustomerModalBottomSheetState
    )

    bookingInfo.serviceSessionInfo?.serviceId?.let {
        CalendarBookingBottomSheet(serviceId = it, onCallbackCalendarBooking = {
        mainScope.launch {
            viewModel.updateDateSelected(it)
            dateDetailModalBottomSheetState.show()
        }
    } , modalBottomSheetState = calendarModalBottomSheetState)
    }

    bookingInfo.serviceSessionInfo?.serviceId?.let { serviceId ->
        datePicked?.let {
            TimeBookingBottomSheet(serviceId = serviceId,
                kalendarMoney = it, onCallbackTimeBooking = { kalendarMoney ->
                    viewModel.updateKalendarMoney(kalendarMoney)
                    mainScope.launch {
                        scheduleRequestModalBottomSheetState.show()
                    }
                }, modalBottomSheetState = dateDetailModalBottomSheetState
            )
        }
    }

    RescheduleRequestBookingBottomSheet(modalBottomSheetState = scheduleRequestModalBottomSheetState, callbackConfirmedReschedule = { status ->
        if(status){
            viewModel.updateBookingInfo()
        }
    })

    if(updateSuccess == true){
        LaunchedEffect(true){
            snapshotFlow { true }.collectLatest {
                navController.popBackStack()
                viewModel.resetStatus()
            }
        }
    }

    if(errorMes?.isNotEmpty() == true){
        LaunchedEffect(true){
            snapshotFlow { errorMes }.collectLatest {
                showMessage(context = context, errorMes)
            }
        }
    }

}

@Composable
fun ItemDescriptionBooking(title: String, content: String, colorStatus: Color ?= null){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = title, style = MaterialTheme.typography.subtitle2.copy(
            color = Neutral_Gray_9,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ), modifier = Modifier.padding(end = 30.dp))

        val colorStatus = colorStatus ?: Neutral_Gray_9

        Text(text = content, style = MaterialTheme.typography.subtitle2.copy(
            color = colorStatus,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        ))
    }
}

@Composable
fun ItemDescriptionStatusBooking(title: String, status: String?){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = title, style = MaterialTheme.typography.subtitle2.copy(
            color = Neutral_Gray_9,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ), modifier = Modifier.padding(end = 30.dp))

        var colorStatus = Neutral_Gray_9
        var contentStatus = ""
        if(status != null){
            contentStatus = stringResource(id = getStatusBookingStatus(status))
            colorStatus = getStatusBookingColor(status)
        }

        Text(text = contentStatus, style = MaterialTheme.typography.subtitle2.copy(
            color = colorStatus,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        ))
    }
}

@Composable
fun ItemQuantityBooking(title: String, content: String,){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        
        Text(text = title, style = MaterialTheme.typography.subtitle2.copy(
            color = Neutral_Gray_9,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ))

        Column(horizontalAlignment = Alignment.End) {
            Text(text = "Người lớn x1", style = MaterialTheme.typography.subtitle2.copy(
                color = Neutral_Gray_9,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ))
            SpaceCompose(height = 16.dp)
            Text(text = "Trẻ em x1", style = MaterialTheme.typography.subtitle2.copy(
                color = Neutral_Gray_9,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ))
        }
    }
}

private fun getStatusBookingStatus(status: String?): Int{
    var result = R.string.draft_booking_status
    when(status){
        StatusBookingDashBoardModel.Draft.type -> {
            result = R.string.draft_booking_status
        }
        StatusBookingDashBoardModel.PaymentPending.type -> {
            result = R.string.payment_pending_booking_status
        }
        StatusBookingDashBoardModel.Fail.type -> {
            result = R.string.fail_booking_status
        }
        StatusBookingDashBoardModel.Completed.type-> {
            result = R.string.completed_booking_status
        }
        StatusBookingDashBoardModel.NoUsed.type -> {
            result = R.string.no_used_booking_status
        }
        StatusBookingDashBoardModel.Used.type-> {
            result = R.string.used_booking_status
        }
        StatusBookingDashBoardModel.RequestToCancel.type -> {
            result = R.string.request_to_cancel_booking_status
        }
        StatusBookingDashBoardModel.NoRefund.type -> {
            result = R.string.no_refund_booking_status
        }
        StatusBookingDashBoardModel.Refunded.type -> {
            result = R.string.refunded_booking_status
        }
        StatusBookingDashBoardModel.RefundPending.type -> {
            result = R.string.refunding_booking_status
        }
    }
    return result
}

private fun getStatusBookingColor(status: String?): Color {
    var result = Color_Yellow_B00
    when(status){
        StatusBookingDashBoardModel.Draft.type -> {
            result = Color_Yellow_B00
        }
        StatusBookingDashBoardModel.PaymentPending.type -> {
            result = Color_GREEN_671
        }
        StatusBookingDashBoardModel.Fail.type,
        StatusBookingDashBoardModel.NoUsed.type,
        StatusBookingDashBoardModel.RequestToCancel.type,
        StatusBookingDashBoardModel.Refunded.type,
        -> {
            result = Color_Red_027
        }

        StatusBookingDashBoardModel.Completed.type,
        StatusBookingDashBoardModel.NoRefund.type,
        StatusBookingDashBoardModel.Used.type-> {
            result = Color_BLUE_OF8
        }
    }
    return result
}

@Composable
fun ItemDescriptionStatusPayment(title: String, status: String?){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = title, style = MaterialTheme.typography.subtitle2.copy(
            color = Neutral_Gray_9,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ), modifier = Modifier.padding(end = 30.dp))

        var colorStatus = Neutral_Gray_9
        var contentStatusPayment = ""

        when(status){
            PaymentStatus.SUCCESS.type -> {
                colorStatus = Color_BLUE_OF8
                contentStatusPayment = stringResource(id = R.string.content_payment_success)
            }

            PaymentStatus.Fail.type -> {
                colorStatus = Color_RED_F65
                contentStatusPayment = stringResource(id = R.string.content_payment_failed)
            }

            PaymentStatus.PaymentPending.type -> {
                colorStatus = Color_GREEN_671
                contentStatusPayment = stringResource(id = R.string.content_payment_processing)
            }
            
            else -> {
                colorStatus =  Color_Yellow_B00
                contentStatusPayment = stringResource(id = R.string.content_payment_processing)
            }
        }

        Text(text = contentStatusPayment, style = MaterialTheme.typography.subtitle2.copy(
            color = colorStatus,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        ))
    }
}