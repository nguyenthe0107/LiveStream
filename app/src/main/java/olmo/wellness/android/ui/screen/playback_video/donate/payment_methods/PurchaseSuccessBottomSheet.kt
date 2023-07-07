package olmo.wellness.android.ui.screen.playback_video.donate.payment_methods

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.WrapperUrlPayment
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.theme.*

@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PurchaseSuccessBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    wrapperUrlPayment: WrapperUrlPayment ?= null,
    onCancelCallback: (() -> Unit) ?= null,
    onNavigationDetail: (() -> Unit) ?= null,
){
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Column(
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    AsyncImage(
                        model = R.drawable.img_purchase_success,
                        contentDescription = "ic_purchase_failure",
                        modifier = Modifier.size(200.dp, 196.dp).padding(top = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.lb_purchase_success),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 18.sp, lineHeight = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral_Gray_9,
                        ), modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.lb_order_id_general),
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Neutral_Gray_9,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "B-" + wrapperUrlPayment?.bookingId,
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Neutral_Gray_9,
                            fontSize = 16.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(22.dp))

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
                                    end = 12.dp,
                                    start = 12.dp
                                )
                        ){
                            SecondLiveButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp),
                                stringResource(id = R.string.lb_action_booking_detail),
                                onClickFunc = {
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                    onNavigationDetail?.invoke()
                                }
                            )
                            PrimaryLiveButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp),
                                stringResource(id = R.string.lb_action_booking_livestream),
                                onClickFunc = {
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                    onCancelCallback?.invoke()
                                }
                            )
                        }
                    }
                }
            } else {
                EmptyBottomSheet()
            }
        }) {
    }
}