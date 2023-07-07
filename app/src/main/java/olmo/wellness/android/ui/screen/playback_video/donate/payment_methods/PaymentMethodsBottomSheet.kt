package olmo.wellness.android.ui.screen.playback_video.donate.payment_methods

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.WrapperUrlPayment
import olmo.wellness.android.domain.model.payment.PaymentRequireWrapper
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.screen.playback_video.donate.data.PaymentMethodModel
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency

@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentMethodsBottomSheet(modalBottomSheetState: ModalBottomSheetState,
                              packageOptionInfo: PackageOptionInfo ?= null,
                              navigationWebView: ((Boolean, WrapperUrlPayment) -> Unit) ?= null,
                              paymentRequireWrapper: PaymentRequireWrapper ?= null,
                              viewModel: PaymentMethodViewModel = hiltViewModel()
){

    val isError = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(packageOptionInfo?.toString() + paymentRequireWrapper?.toString()){
        viewModel.onBindData(packageOptionInfo, paymentRequireWrapper)
    }
    val scope = rememberCoroutineScope()
    val state = viewModel.uiState.collectAsState()
    val paymentMethods = state.value.paymentMethods?: emptyList()
    val pricePackageInfo = remember(state.value.pricePackageInfo) {
        mutableStateOf(state.value.pricePackageInfo)
    }
    val wrapperBooking = remember(state.value.paymentRequireWrapper) {
        mutableStateOf(state.value.paymentRequireWrapper)
    }
    val isNavigationWebView = state.value.isNavigationWebView
    val paymentMethodSelected = state.value.paymentMethodViewSelected
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            if (modalBottomSheetState.isVisible){
            if (!isError.value) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = stringResource(R.string.lb_payment_methods),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 18.sp, lineHeight = 26.sp
                        ), modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(paymentMethods, key = { it.id }) { paymentMethod ->
                            PaymentMethodItemCompose(paymentMethod, viewModel)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "VAT", style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 14.sp, lineHeight = 21.sp
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if(pricePackageInfo.value?.vat != null){
                            Text(
                                text = "đ ${getConvertCurrency(pricePackageInfo.value?.vat ?: 0F)}",
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontSize = 14.sp, lineHeight = 21.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total", style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 14.sp, lineHeight = 21.sp
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = MontserratFont,
                                        color = Neutral_Gray_9
                                    )
                                ) {
                                    append("đ ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp,
                                        fontFamily = MontserratFont,
                                        color = Color_LiveStream_Main_Color
                                    )
                                ) {
                                    if(packageOptionInfo != null){
                                        if(pricePackageInfo.value?.totalPrice != null){
                                            append(getConvertCurrency(pricePackageInfo.value?.totalPrice?:0f))
                                        }
                                    }else{
                                        if(wrapperBooking.value != null){
                                            append(getConvertCurrency(wrapperBooking.value?.totalMoney ?: 0F))
                                        }
                                    }
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "* Kepler không trực tiếp lưu thẻ của bạn. Để đảm bảo an toàn, thông tin thẻ của bạn chỉ được lưu bởi CyberSource, công ty quản lý thanh toán lớn nhất thế giới (thuộc tổ chức VISA)\n",
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Color_BLUE_OF8,
                            fontSize = 10.sp,
                            lineHeight = 12.sp,
                        ), fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(12.dp))


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = if (paymentMethodSelected != null) {
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color_LiveStream_Main_Color,
                                            Color_LiveStream_Main_Color,
                                            Color_LiveStream_Light_Color,
                                        )
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        listOf(
                                            Neutral_Gray_4,
                                            Neutral_Gray_4,
                                            Neutral_Gray_4,
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(12.dp)
                            .noRippleClickable {
                                viewModel.onPay(packageOptionInfo)
                            }
                    ) {
                        Text(
                            text = stringResource(R.string.lb_pay_now),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 16.sp, color = White, lineHeight = 24.sp
                            ), modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

            }else {
                Column(
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_purchase_failure),
                        contentDescription = "ic_purchase_failure"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.lb_purchase_failure),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 18.sp, lineHeight = 26.sp
                        ), modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Error message: Account not enough money",
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Error_500,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush =
                                Brush.horizontalGradient(
                                    listOf(
                                        Color_LiveStream_Main_Color,
                                        Color_LiveStream_Main_Color,
                                        Color_LiveStream_Light_Color,
                                    )
                                ),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(12.dp)
                            .clickable {
                            }
                    ) {
                        Text(
                            text = stringResource(R.string.lb_try_again),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 16.sp, color = White, lineHeight = 24.sp
                            ), modifier = Modifier.align(Alignment.Center)
                        )
                    }

                }
            }
            }else{
                EmptyBottomSheet()
            }
        }){
    }

    if(isNavigationWebView == true){
        val link = state.value.urlPayment ?: ""
        val bookingId = state.value.paymentRequireWrapper?.bookingId
        navigationWebView?.invoke(true, WrapperUrlPayment(url = Uri.encode(link), bookingId = bookingId))
        viewModel.resetState()
        scope.launch {
            modalBottomSheetState.hide()
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PaymentMethodItemCompose(paymentMethodModel: PaymentMethodModel, viewModel: PaymentMethodViewModel){
    Column(modifier = Modifier) {
        val isSelected = remember {
            mutableStateOf(false)
        }
        val paymentSelected = viewModel.uiState.value.paymentMethodViewSelected
        isSelected.value = paymentSelected?.id == paymentMethodModel.id
        Row(
            modifier = Modifier
                .padding(0.dp)
                .height(68.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color_Purple_FBC,
                    shape = RoundedCornerShape(10.dp)
                )
                .noRippleClickable {
                    viewModel.onPaymentMethodSelected(paymentMethodModel)
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = R.drawable.ic_onepay,
                contentDescription = "onePay",
                modifier = Modifier
                    .width(60.dp)
                    .height(20.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ){
                Text(
                    text = paymentMethodModel.name,
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 12.sp, lineHeight = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = paymentMethodModel.description,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 10.sp, lineHeight = 20.sp
                    )
                )
            }
            if(isSelected.value){
                Image(
                    painter = painterResource(id = R.drawable.ic_radio_selected),
                    contentDescription = "Select",
                    modifier = Modifier.size(22.dp)
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.ic_radio_unselected),
                    contentDescription = "Select",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}