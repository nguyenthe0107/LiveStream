package olmo.wellness.android.ui.booking.book_now.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.wrapper.NumberCustomerWrapper
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClientNumberBottomSheet(
    numberCustomer: NumberCustomerWrapper?,
    onCallbackNumberCustomer: ((NumberCustomerWrapper) -> Unit)? = null,
    onCancelCallback: (() -> Unit) ?= null,
    modalBottomSheetState: ModalBottomSheetState,
){
    val scope = rememberCoroutineScope()

    val numberDefault = remember {
        mutableStateOf(numberCustomer)
    }
    numberDefault.value = numberCustomer

    /*  numberAdults */
    val numberAdultsValueDefault = numberDefault.value?.numberOfAdult ?: 0
    val numberAdults = remember(numberAdultsValueDefault) {
        mutableStateOf(numberAdultsValueDefault)
    }

    /*  numberChilds */
    val numberChildsValueDefault = numberDefault.value?.numberOfChild ?: 0
    val numberChilds = remember(numberChildsValueDefault) {
        mutableStateOf(numberChildsValueDefault)
    }

    /*  numberAdultsOther */
   val numberAdultsOtherValueDefault = numberDefault.value?.numberOfOptionalAdult ?: 0
    val numberAdultsOther = remember(numberAdultsOtherValueDefault) {
        mutableStateOf(numberAdultsOtherValueDefault)
    }

    /* Customer after selected */
    val customerSelected = remember {
        mutableStateOf(NumberCustomerWrapper())
    }
    customerSelected.value.numberOfAdult = numberAdults.value
    customerSelected.value.numberOfChild = numberChilds.value
    customerSelected.value.numberOfOptionalAdult = numberAdultsOther.value

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = White,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            Scaffold(topBar = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Số lượng khách",
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 18.sp, lineHeight = 26.sp
                        ),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Icon(painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "close",
                        tint = Black_037,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(12.dp)
                            .noRippleClickable {
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                            })

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .align(Alignment.BottomCenter)
                            .background(Gray_EF3)
                    )
                }
            }, bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .padding(top = 20.dp, end = 20.dp, start = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SecondLiveButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 6.dp),
                            stringResource(R.string.lb_back),
                            onClickFunc = {
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                                onCancelCallback?.invoke()
                            }
                        )
                        PrimaryLiveButton(
                            enable = true,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 6.dp),
                            text = stringResource(R.string.lb_confirm),
                            onClickFunc = {
                                onCallbackNumberCustomer?.invoke(customerSelected.value)
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                            }
                        )
                    }
                }
            }, modifier = Modifier.fillMaxHeight(0.8f)) {
                Column(modifier = Modifier) {
                    ItemClient(
                        title = "Người lớn (Tuổi từ 13+)",
                        description = "Chiều cao tối thiểu xxx",
                        numberAdults
                    )

                    ItemClient(
                        title = "Trẻ em (Dưới 13 tuổi)",
                        description = "Chiều cao tối đa 1m20",
                        numberChilds
                    )


                    Text(
                        text = "Lựa chọn khác", style = MaterialTheme.typography.subtitle2.copy(
                            lineHeight = 20.sp, fontSize = 16.sp
                        ), modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp)
                    )


                    ItemClient(
                        title = "Người lớn (Từ 13 tuổi)",
                        description = "Biết chèo Sup",
                        numberAdultsOther
                    )

                }
            }
        }) {
    }

}

@Composable
private fun ItemClient(title: String, description: String, numberClients: MutableState<Int>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.caption.copy(
                    fontSize = 14.sp, lineHeight = 24.sp,
                    color = Color_gray_743
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.caption.copy(
                    fontSize = 12.sp, lineHeight = 25.sp,
                    color = Color_D6D
                )
            )

        }
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            val colorMinus =
                if (numberClients.value > 0) Color_gray_743 else Color_gray_743.copy(alpha = 0.3f)
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(13.dp),
                        color = colorMinus
                    )
                    .clip(RoundedCornerShape(13.dp))
                    .clickable(enabled = (numberClients.value > 0)) {
                        numberClients.value = numberClients.value - 1
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_minus),
                    contentDescription = "minus",
                    tint = colorMinus,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            Text(
                text = numberClients.value.toString(),
                modifier = Modifier.width(45.dp),
                style = MaterialTheme.typography.caption.copy(
                    fontSize = 16.sp, lineHeight = 24.sp
                ), textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(13.dp),
                        color = Color_LiveStream_Main_Color
                    )
                    .clip(RoundedCornerShape(13.dp))
                    .clickable {
                        numberClients.value = numberClients.value + 1
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = "plus",
                    tint = Color_LiveStream_Main_Color,
                    modifier = Modifier
                        .align(Alignment.Center)

                )
            }
        }

    }
}