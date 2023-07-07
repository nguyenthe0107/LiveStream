package olmo.wellness.android.ui.screen.playback_video.contact_information

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.BookingWrapperInfo
import olmo.wellness.android.domain.model.payment.PaymentRequireWrapper
import olmo.wellness.android.domain.model.voucher.VoucherInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.booking.book_now.viewmodel.BookNowViewModel
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.voucher_service.VoucherServiceBottomSheet
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition",
    "CoroutineCreationDuringComposition"
)
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ContactInformationServiceBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: ContactInformationServiceViewModel = hiltViewModel(),
    bookNowViewModel: BookNowViewModel = hiltViewModel(),
    bookingWrapperInfo: BookingWrapperInfo ?= null,
    confirmCallback: ((wrapper: PaymentRequireWrapper) -> Unit) ?= null,
    cancelCallback: ((Boolean) -> Unit) ?= null,
){
    val uiState = viewModel.uiState.collectAsState()
    val bookingNowState = bookNowViewModel.uiState.collectAsState()
    val modelVoucherBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            true
        }
   )

    val mainScope = rememberCoroutineScope()

    ModalBottomSheetLayout(sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContentColor = White,sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Scaffold(modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .background(White),
                    bottomBar = {
                        BottomBar(
                            cancelCallback = cancelCallback, confirmCallback = confirmCallback,
                            modalBottomSheetState = modalBottomSheetState,
                            viewModel = viewModel, bookingViewModel = bookNowViewModel,
                            bookingWrapperInfo = bookingWrapperInfo
                        )
                    }) {
                    Box(modifier = Modifier) {
                        Column(
                            modifier = Modifier
                                .background(White)
                                .fillMaxSize()
                                .padding(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ){
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Title(cancelCallback)
                                Body(
                                    viewModel = viewModel,
                                    bookingViewModel = bookNowViewModel,
                                    bookingWrapperInfo = bookingWrapperInfo,
                                    openVoucher = {
                                        mainScope.launch {
                                            modalBottomSheetState.hide()
                                            modelVoucherBottomSheetState.show()
                                        }
                                    },
                                )
                            }
                        }
                        LoadingScreen(isLoading = uiState.value.isLoading)
                    }
                }
            } else {
                EmptyBottomSheet()
            }
        }) {
    }
    
    VoucherServiceBottomSheet(
        sessionConfirmId = bookingNowState.value.serviceSessionConfirmInfo?.id,
        modalBottomSheetState = modelVoucherBottomSheetState,
        cancelCallback = { status ->
            if(status){
                mainScope.launch {
                    modalBottomSheetState.show()
                }
            }
        }, confirmCallback = { voucherInfo ->
            if(voucherInfo != null){
                viewModel.updateVoucherSelected(voucherInfo)
                mainScope.launch {
                    modalBottomSheetState.show()
                    modelVoucherBottomSheetState.hide()
                }
            }
        })

}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun BottomBar(
    cancelCallback: ((Boolean) -> Unit) ?= null,
    confirmCallback: ((wrapper: PaymentRequireWrapper) -> Unit) ?= null,
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: ContactInformationServiceViewModel,
    bookingViewModel: BookNowViewModel,
    bookingWrapperInfo: BookingWrapperInfo ?= null,
) {
    val state = viewModel.uiState.collectAsState()
    var isEnable by remember {
        mutableStateOf(false)
    }
    val mainScope = rememberCoroutineScope()
    isEnable = state.value.isValidateFirstName == true &&
            state.value.isValidateLastName == true &&
            state.value.isValidateEmail == true &&
            state.value.isValidatePhone == true

    val bookingId = bookingWrapperInfo?.bookingId //bookingState.value.bookingIdResponseInfo?.bookingId
    val sessionSecretId = bookingWrapperInfo?.sessionSecretId
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(White)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    bottom = 32.dp,
                    top = 12.dp,
                    end = 16.dp,
                    start = 16.dp
                )
        ){
            SecondLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                stringResource(id = R.string.action_back),
                onClickFunc = {
                    cancelCallback?.invoke(true)
                }
            )
            PrimaryLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                stringResource(id = R.string.lb_confirm),
                onClickFunc = {
                    val totalMoney = state.value.totalMoney
                    val wrapper = viewModel.getContactWrapper().copy(
                        bookingId = bookingId,
                        totalMoney = totalMoney,
                        sessionSecret = sessionSecretId
                    )
                    mainScope.launch {
                        modalBottomSheetState.hide()
                    }
                    confirmCallback?.invoke(wrapper)
                },
                enable = isEnable
            )
        }
    }
}

@Composable
private fun Body(viewModel: ContactInformationServiceViewModel,
                 bookingViewModel: BookNowViewModel,
                 bookingWrapperInfo: BookingWrapperInfo ?= null,
                 openVoucher: (() -> Unit)?=null
){
    val stateLazy = rememberLazyListState()
    val uiState = viewModel.uiState.collectAsState()
    val voucherSelected = uiState.value.voucherInfo

    val bookNowUiState = bookingViewModel.uiState.collectAsState()
    val totalMoney = bookingWrapperInfo?.totalMoney ?: 0F //bookNowUiState.value.serviceSessionConfirmInfo?.totalPrice ?: 0F

    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.White)
    ){
        LazyColumn(content = {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ){
                    TextFieldGeneralWithError(
                        titleText = stringResource(id = R.string.title_surname_information_booking),
                        isError = uiState.value.lastName?.isEmpty() == true,
                        hint = stringResource(id = R.string.hilt_surname_information_booking),
                        defaultValue = uiState.value.lastName,
                        paddingVertical = padding_2
                    ){
                        viewModel.updateLastName(it)
                    }
                    SpaceCompose(height = 12.dp)
                    TextFieldGeneralWithError(
                        titleText = stringResource(id = R.string.title_name_information_booking),
                        isError = uiState.value.firstName?.isEmpty() == true,
                        hint = stringResource(id = R.string.hilt_name_information_booking),
                        defaultValue = uiState.value.firstName,
                        paddingVertical = padding_2){
                        viewModel.updateFirstName(it)
                    }
                    SpaceCompose(height = 12.dp)
                    TextFieldGeneralWithError(
                        titleText = stringResource(id = R.string.title_phone_information_booking),
                        isError = uiState.value.phone?.isEmpty() == true || uiState.value.isValidatePhone == false,
                        hint = stringResource(id = R.string.hilt_phone_information_booking),
                        defaultValue = uiState.value.phone,
                        keyboardType = KeyboardType.Number ,
                        paddingVertical = padding_2){
                        viewModel.updatePhone(it)
                    }
                    SpaceCompose(height = 12.dp)
                    TextFieldGeneralWithError(
                        titleText = stringResource(id = R.string.title_email_information_booking),
                        isError = uiState.value.email?.isEmpty() == true || uiState.value.isValidateEmail == false,
                        hint = stringResource(id = R.string.hilt_email_information_booking),
                        defaultValue = uiState.value.email,
                        paddingVertical = padding_2){
                        viewModel.updateEmail(it?.trim())
                    }
                    SpaceCompose(height = 12.dp)
                    PromotionItem(openVoucher = openVoucher, voucherSelected, viewModel)
                    SpaceCompose(height = 27.dp)
                    TotalPriceItem(totalMoney, voucherSelected, viewModel)
                    SpaceCompose(height = 60.dp)
                }
            }
            item {
               SpaceCompose(height = 40.dp)
            }
            
        }, state = stateLazy)
    }
}

@Composable
private fun Title(cancelCallback: ((Boolean) -> Unit) ?= null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Color.White)
                .height(60.dp)
        ){
            Text(
                text = stringResource(R.string.title_contact_information_booking),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    color = Neutral_Gray_9,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.Center)
            )

            Image(
                painterResource(id = R.drawable.ic_close),
                contentDescription = "ic_close",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .noRippleClickable {
                        cancelCallback?.invoke(true)
                    }
            )

            Spacer (
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = Neutral_Gray_3)
                    .align(Alignment.BottomStart)
            )
    }
}

@Composable
fun PromotionItem(openVoucher: (() -> Unit) ?= null, voucherInfo: VoucherInfo?, viewModel: ContactInformationServiceViewModel){
    Column(modifier = Modifier
        .fillMaxWidth()
        .noRippleClickable {
            openVoucher?.invoke()
        }
        .padding(horizontal = 16.dp)){
        
        if(voucherInfo == null){
            val title = stringResource(R.string.title_promotion_information_booking)
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 14.sp,
                    lineHeight = 26.sp,
                    color = Neutral_Gray_9,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(bottom = 19.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.olmo_ic_coupon), contentDescription = "")
                SpaceHorizontalCompose(width = 10.dp)
                Text(
                    text = stringResource(R.string.title_select_promotion_information_booking),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 14.sp,
                        color = Color_LiveStream_Main_Color,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = MontserratFont
                    )
                )
            }
            
        }else{
            val title = stringResource(R.string.title_has_promotion_information_booking)
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 14.sp,
                    lineHeight = 26.sp,
                    color = Neutral_Gray_9,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(bottom = 19.dp)
            )
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Image(painter = painterResource(id = R.drawable.olmo_ic_coupon), contentDescription = "")
                    SpaceHorizontalCompose(width = 10.dp)
                    Text(
                        text = voucherInfo.voucher.toString().uppercase(),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 16.sp,
                            color = Color_LiveStream_Main_Color,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MontserratFont
                        )
                    )
                }
                Text(
                    text = stringResource(id = R.string.action_clear_promotion_information_booking),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 12.sp,
                        color = Neutral_Gray_7,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = MontserratFont
                    ),
                    modifier = Modifier.noRippleClickable {
                        viewModel.clearVoucher()
                    }
                )
            }
            if(voucherInfo.reductionAmount != null){
                SpaceCompose(height = 16.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = stringResource(R.string.label_has_promotion_information_booking),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 14.sp,
                            color = Neutral_Gray_9,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Row {
                        Text(
                            text = "VND",
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 14.sp,
                                color = Neutral_Gray_9,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        SpaceHorizontalCompose(width = 12.dp)
                        Text(
                            text = getConvertCurrency(voucherInfo.reductionAmount ?: 0F),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 14.sp,
                                color = Neutral_Gray_9,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TotalPriceItem(totalMoney: Float, voucherInfo: VoucherInfo?, viewModel: ContactInformationServiceViewModel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.title_total_money_information_booking),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 14.sp,
                    color = Neutral_Gray_9,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Row {
                Text(
                    text = "VND",
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 14.sp,
                        color = Neutral_Gray_9,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                SpaceHorizontalCompose(width = 12.dp)
                Text(
                    text = getConvertCurrency(totalMoney.minus(voucherInfo?.reductionAmount ?: 0F)),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 14.sp,
                        color = Neutral_Gray_9,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                viewModel.updateMoney(totalMoney.minus(voucherInfo?.reductionAmount ?: 0F))
            }
        }
        SpaceCompose(height = 28.dp)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.title_content_contact),
                style = MaterialTheme.typography.subtitle2.copy(
                    color = Color_BLUE_OF8,
                    fontSize = 10.sp,
                    lineHeight = 24.sp,
                    fontStyle = FontStyle.Italic
                )
            )
        }
        SpaceCompose(height = 8.dp)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.email_invoice_company),
                style = MaterialTheme.typography.subtitle2.copy(
                    color = Color_BLUE_OF8,
                    fontSize = 10.sp,
                    lineHeight = 24.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}