package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.broadcast_audience

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BroadCastAudienceBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    modifier: Modifier = Modifier,
    onSelected: (AudienceType) -> Unit,
    dismissDialog: (() -> Unit)? = null,
    defaultType: AudienceType? = null,
) {
    val context = LocalContext.current
    ModalBottomSheetLayout(sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = Transparent,
        sheetContentColor = Transparent,sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Title()
                Body(context, defaultType, onSelected, dismissDialog)
            }else{
                EmptyBottomSheet()
            }
        }){
    }
}

@Composable
private fun Body(
    context: Context, defaultType: AudienceType? = null,
    onSelected: (AudienceType) -> Unit,
    dismissDialog: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            val data = listOf(
                AudienceType.EVERYONE,
                AudienceType.FOLLOWERS
            )
            var selectedItem by remember {
                mutableStateOf(defaultType ?: data[0])
            }

            Text(
                text = "Who can see this livestream?",
                style = MaterialTheme.typography.subtitle1.copy(
                    color = Black_037, fontSize = 14.sp
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        top = 8.dp
                    ),
                verticalAlignment = CenterVertically
            ) {

                data.forEach { itemData ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = CenterVertically
                    ) {
                        RadioButton(
                            modifier = Modifier
                                .size(16.dp),
                            selected = (itemData == selectedItem),
                            onClick = {
                                selectedItem = itemData
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color_LiveStream_Main_Color
                            )
                        )
                        Text(
                            text = itemData.value,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.subtitle1.copy(
                                color = Black_037, fontSize = 14.sp
                            )
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White)
            ) {
                PrimaryLiveButton(
                    text = "Save",
                    modifier = Modifier.padding(
                        top = 12.dp,
                        end = 16.dp,
                        start = 16.dp,
                        bottom = 24.dp
                    ),
                    onClickFunc = {
                        onSelected.invoke(selectedItem)
                        dismissDialog?.invoke()
                    }
                )
            }

        }
    }
}

@Composable
private fun Title() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(60.dp)
    ) {
        Text(
            text = stringResource(R.string.lb_notify_followers),
            style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 18.sp
            ),
            modifier = Modifier
                .align(Alignment.Center)
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