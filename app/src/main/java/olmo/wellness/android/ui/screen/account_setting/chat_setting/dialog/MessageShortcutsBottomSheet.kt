package olmo.wellness.android.ui.screen.account_setting.chat_setting.dialog

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.dashedBorder
import olmo.wellness.android.ui.theme.*

@SuppressLint(
    "UnusedMaterialScaffoldPaddingParameter", "MutableCollectionMutableState",
    "UnrememberedMutableState"
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MessageShortcutsBottomSheet(modalBottomSheetState: ModalBottomSheetState) {

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    var hashTagShortCuts by mutableStateOf(mutableListOf<String>())

    val editMessageModal = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            false
        }
    )

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {

            Scaffold(modifier = Modifier.fillMaxHeight(0.6f), bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                editMessageModal.show()
                                modalBottomSheetState.hide()
                            }
                        }, modifier = Modifier
                            .dashedBorder(
                                width = 1.dp,
                                color = Color_BLUE_7F4,
                                shape = RoundedCornerShape(50.dp),
                                10.dp, 10.dp
                            )
                            .clip(RoundedCornerShape(40.dp))
                            .height(50.dp),
                        border = BorderStroke(0.dp, Color.Transparent),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.lb_create_your_message_shortcut),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 14.sp, color = Color_BLUE_7F4
                            )
                        )
                    }
                }

            }) {
                Column(modifier = Modifier) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.title_message_shortcut),
                            style = MaterialTheme.typography.subtitle2.copy(
                                color = Color_Purple_FBC, fontSize = 18.sp
                            )
                        )

                        Text(
                            text = "You have no Message shortcuts",
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 14.sp, color = Neutral_Gray_6
                            ),
                            modifier = Modifier.padding(top = 10.dp, bottom = 15.dp)
                        )
                    }

                    HashTagList(hashTagShortCuts)

                }

            }

        }
    ) {

    }

    EditingMessageShortcutsBottomSheet(
        modalBottomSheetState = editMessageModal,
        onDismiss = {
            scope.launch {
                editMessageModal.hide()
                modalBottomSheetState.show()

                focusManager.clearFocus()
            }
        },
        onConfirm = {
            val temp = mutableListOf<String>().apply {
                add(it)
            }
            hashTagShortCuts = (hashTagShortCuts + temp) as MutableList<String>
            focusManager.clearFocus()
        }
    )
}

@Composable
fun HashTagList(hashTags: MutableList<String>) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 20.dp),
        mainAxisSize = SizeMode.Expand,
        crossAxisSpacing = 12.dp,
        mainAxisSpacing = 8.dp
    ) {
        hashTags.forEach { hashTag ->
            Text(
                text = hashTag,
                modifier = Modifier
                    .background(
                        color = Color_PINK_2FF,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(8.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1.copy(
                    color = White, fontSize = 14.sp
                )
            )
        }
    }
}