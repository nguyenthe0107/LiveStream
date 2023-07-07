package olmo.wellness.android.ui.screen.dashboard_booking.details

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.utils.callHotline
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CancelRequestBookingBottomSheet(
    booking: ServiceBooking?=null,
    callbackConfirmed: ((Boolean) -> Unit) ?= null,
    modalBottomSheetState: ModalBottomSheetState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = White,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
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
                        Row(modifier = Modifier.weight(1f)) {
                            SecondLiveButton(
                                modifier = Modifier.weight(1f),
                                text = "Cancel",
                                radius = 50.dp,
                                alignText= Alignment.Center,
                            ) {
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))

                            PrimaryLiveButton(
                                modifier = Modifier.weight(1f),
                                text = "Send Request",
                            ) {
                                callbackConfirmed?.invoke(true)
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxHeight(0.8f)
            ) {
                Box() {
                    LazyColumn(modifier = Modifier) {
                        item {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Box(modifier = Modifier.fillMaxWidth()){
                                    Row(modifier = Modifier.align(Alignment.Center)) {
                                        Text(text = "Cancellation Request", style = MaterialTheme.typography.subtitle2.copy(
                                            color = Neutral_Gray_9,
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 26.sp,
                                            fontSize = 18.sp,
                                            textAlign = TextAlign.Center
                                        ), modifier = Modifier)
                                    }

                                    Icon(painter = painterResource(id = R.drawable.ic_close),
                                        contentDescription = "close",
                                        tint = Black_037,
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .noRippleClickable {
                                                scope.launch {
                                                    modalBottomSheetState.hide()
                                                }
                                            })
                                }

                                Row(modifier = Modifier
                                    .padding(top = 16.dp)
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(color = Neutral_Gray_3)) {}
                            }
                        }
                        item {
                            Column(modifier = Modifier.padding(top = 30.dp, start = 16.dp, end = 16.dp)) {
                                Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                                    Row(horizontalArrangement = Arrangement.Center) {
                                        AsyncImage(model = R.drawable.img_support_customer,
                                            contentDescription = "",
                                            modifier = Modifier.padding(bottom = 12.dp))
                                    }

                                    Row(
                                        modifier = Modifier
                                            .padding(start = 32.dp, end = 32.dp)
                                            .border(
                                                width = 1.dp,
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color_LiveStream_Main_Color
                                            )
                                            .padding(PaddingValues(10.dp)),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ){
                                        AsyncImage(
                                            model = R.drawable.ic_hotline,
                                            contentDescription = null,
                                            modifier = Modifier.size(39.dp, 42.dp),
                                            colorFilter = ColorFilter.tint(Color_LiveStream_Main_Color)
                                        )
                                        SpaceHorizontalCompose(width = 20.dp)
                                        Column(modifier = Modifier.noRippleClickable {
                                            callHotline(context)
                                        }){
                                            Text(
                                                stringResource(id = R.string.tv_hotline),
                                                modifier = Modifier.padding(top = 7.dp),
                                                style = MaterialTheme.typography.subtitle2.copy(
                                                    color = Color_LiveStream_Main_Color,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp,
                                                    lineHeight = 20.sp,
                                                )
                                            )
                                            SpaceCompose(height = 8.dp)
                                            Text(
                                                stringResource(id = R.string.content_hotline),
                                                modifier = Modifier.padding(top = 7.dp),
                                                style = MaterialTheme.typography.caption.copy(
                                                    color = Color_LiveStream_Main_Color,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 16.sp,
                                                    lineHeight = 20.sp,
                                                ),
                                            )
                                        }
                                    }
                                    SpaceCompose(height = 12.dp)
                                    Text(
                                        text = stringResource(id = R.string.text_hotline_booking_details),
                                        style = MaterialTheme.typography.subtitle2.copy(
                                            color = Neutral_Gray_9,
                                            fontFamily = MontserratFont,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp
                                        ),
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .padding(horizontal = 12.dp)
                                            .fillMaxWidth()
                                    )
                                    SpaceCompose(height = 12.dp)
                                    Text(
                                        text = stringResource(id = R.string.text_time_working_hotline_booking_details),
                                        style = MaterialTheme.typography.subtitle2.copy(
                                            color = Color_LiveStream_Main_Color,
                                            fontFamily = MontserratFont,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp
                                        ),
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .padding(horizontal = 12.dp)
                                    )

                                }
                            }

                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }

        }) {
    }
}