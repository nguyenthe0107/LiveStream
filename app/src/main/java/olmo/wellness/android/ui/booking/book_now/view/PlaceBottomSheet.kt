package olmo.wellness.android.ui.booking.book_now.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.ServiceLocationInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaceBottomSheet(places: List<ServiceLocationInfo>,
                     onCancelCallback: (() -> Unit) ?= null,
                     onPlaceSelected: ((ServiceLocationInfo) -> Unit) ?= null,
                     modalBottomSheetState: ModalBottomSheetState) {

    val scope = rememberCoroutineScope()

    val placeSelect = remember {
        mutableStateOf(places.getOrNull(0))
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = White,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            Scaffold(topBar = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Địa điểm",
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
                            enable = placeSelect.value != null,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 6.dp),
                            text = stringResource(R.string.lb_confirm),
                            onClickFunc = {
                                placeSelect?.value?.let { onPlaceSelected?.invoke(it) }
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                            }
                        )
                    }
                }
            }, modifier = Modifier.fillMaxHeight(0.8f)) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        items(places.size, key = {
                            it.toString()
                        }) { index ->
                            val item = places[index]
                            ItemPlace(locationInfo = item, placeSelect = placeSelect)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

            }

        }) {

    }

}

@Composable
private fun ItemPlace(locationInfo: ServiceLocationInfo, placeSelect: MutableState<ServiceLocationInfo?>) {
    val shadowColor = if (true) Color_Purple_7F4 else Color.Gray

    val modifier = (if (locationInfo.id == placeSelect.value?.id) {
        Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true,
                spotColor = shadowColor,
                ambientColor = shadowColor
            )
            .background(
                color = Color_BLUE_7F4.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
    } else {
        Modifier
            .fillMaxWidth()
            .background(
                color = White
            )
            .border(width = 1.dp, color = Color_BLUE_7F4, shape = RoundedCornerShape(8.dp))
    }
            )
    Surface(
        modifier = modifier.clickable {
            placeSelect.value = locationInfo
        }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(
                (if (locationInfo.id == placeSelect.value?.id) Color_Purple_7F4.copy(
                    alpha = 0.3f
                ) else White)
            )
        ) {
            Text(
                text = locationInfo.address.orEmpty(),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 14.sp, lineHeight = 24.sp
                ), modifier = Modifier.padding(12.dp)
            )
        }

    }

    Spacer(modifier = Modifier.height(20.dp))
}