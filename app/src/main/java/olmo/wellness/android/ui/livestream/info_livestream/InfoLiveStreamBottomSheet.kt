package olmo.wellness.android.ui.livestream.info_livestream

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.core.enums.ConfirmType
import olmo.wellness.android.data.model.live_stream.FillDataLiveStream
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category.components.HorizontalGridCategories
import olmo.wellness.android.ui.theme.*


const val MAX_TITLE_LENGTH = 50

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfoLiveStreamBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    fillData: MutableState<FillDataLiveStream>,
    onDismiss: () -> Unit,
    openDescription: () -> Unit, openCategories: () -> Unit,
    onConfirm: (FillDataLiveStream?) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val enableConfirm = remember {
        mutableStateOf(false)
    }

    enableConfirm.value =
        fillData.value.title?.isBlank() == false && fillData.value.isEvent != null && fillData.value.description != null
                && fillData.value.listCategory?.isEmpty() == false

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContentColor = Transparent,
        sheetContent = {
            if (modalBottomSheetState.isVisible) {
                InfoUi(
                    context,
                    fillData.value.title,
                    fillData.value.description,
                    fillData.value.listCategory,
                    titleChange = {
                        fillData.value = fillData.value.copy(
                            title = it.orEmpty()
                        )
                    },
                    focusManager,
                    enableConfirm = enableConfirm.value,
                    onDismiss = onDismiss,
                    openDescription = openDescription,
                    openCategories = openCategories,
                    onConfirm = {
                        onConfirm.invoke(fillData.value)
                    }, eventChange = {
                        it?.let {
                            fillData.value = fillData.value.copy(isEvent = it == ConfirmType.Yes)
                        }
                    }
                )
            } else {
                EmptyBottomSheet()
            }

        }) {

    }

}

@SuppressLint("MissingInflatedId", "InflateParams")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun InfoUi(
    context: Context,
    title: String?,
    description: String?,
    listCategory: List<LiveCategory>? = null,
    titleChange: (String?) -> Unit,
    focusManager: FocusManager,
    enableConfirm: Boolean,
    openDescription: () -> Unit,
    openCategories: () -> Unit,
    eventChange: (ConfirmType?) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val radioEvent = listOf(ConfirmType.Yes, ConfirmType.No)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf<ConfirmType?>(null) }
    val state = rememberLazyListState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = stringResource(R.string.lb_add_formation),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .align(Alignment.Center)
            )
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = Neutral_Gray_3)
                    .align(Alignment.BottomStart)
            )
        }
        LazyColumn(modifier = Modifier, state = state) {
            item {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                ) {
                    InputTitle(title, focusManager, titleChange)

                    InputDescription(openDescription, description)

                    InputCategories(openCategories)

                    listCategory?.size?.let { it ->
                        HorizontalGridCategories(
                            data = listCategory,
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp),
                            disableClick = true
                        )
                    }
                    EventCommunityUI(
                        radioEvent,
                        selectedOption,
                        onOptionSelected,
                        eventChange
                    )
                    ActionBottomUI(context, onDismiss, enableConfirm, onConfirm)
                }
            }
        }
    }


}

@Composable
private fun EventCommunityUI(
    radioEvent: List<ConfirmType>,
    selectedOption: ConfirmType?,
    onOptionSelected: (ConfirmType?) -> Unit,
    eventChange: (ConfirmType?) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = White)
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.lb_is_this_an_event_for_yout_community),
                style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = stringResource(R.string.lb_your_livestream_will_be_displayed_on_kepler_events),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 10.sp, color = Neutral_Gray_7
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                radioEvent.forEach { type ->
                    Row(modifier = Modifier
                        .selectable(
                            selected = (type == selectedOption), onClick = {
                                onOptionSelected(type)
                                eventChange.invoke(type)
                            }
                        )
                        .fillMaxWidth()
                        .weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (type == selectedOption),
                            modifier = Modifier.size(20.dp),
                            colors = RadioButtonDefaults.colors(
                                Color_gray_6CF,
                                Neutral_Bare_Gray
                            ),
                            onClick = {
                                onOptionSelected(type)
                                eventChange.invoke(type)
                            })
                        Text(
                            text = stringResource(type.name),
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 14.sp, color = Neutral_Gray_9
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionBottomUI(
    context: Context,
    onDismiss: () -> Unit,
    enableConfirm: Boolean,
    onConfirm: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SecondLiveButton(
            modifier = Modifier
                .weight(1f)
                .padding(end = 6.dp)
                .noRippleClickable {
                    (context as Activity).onBackPressed()
                },
            stringResource(R.string.cancel),
            onClickFunc = {
                onDismiss.invoke()
            }
        )
        PrimaryLiveButton(
            enable = enableConfirm,
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp),
            text = stringResource(R.string.lb_confirm),
            onClickFunc = {
                onConfirm.invoke()
            }
        )
    }
}

@Composable
private fun InputCategories(openCategories: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable {
                    openCategories.invoke()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            Text(
                text = stringResource(R.string.lb_categories),
                style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun InputDescription(openDescription: () -> Unit, description: String?) {
    val isDataDescription = description?.isNotBlank()
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable {
                    openDescription.invoke()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.lb_description),
                style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
            if (isDataDescription != true) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null
                )
            }
        }
        if (!TextUtils.isEmpty(description))
            Text(text = description!!,
                style = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        openDescription.invoke()
                    }
                    .padding(bottom = 10.dp)
            )
    }
}

@Composable
private fun InputTitle(
    title: String?, focusManager: FocusManager,
    titleChange: (String?) -> Unit
) {
    Text(
        text = stringResource(R.string.lb_title),
        style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
        modifier = Modifier
            .padding(bottom = 4.dp, start = 16.dp)
            .fillMaxWidth()
            .background(color = White)
    )
    val textChange = remember {
        mutableStateOf(title)
    }
    titleChange(textChange.value)
    TextField(
        value = textChange.value ?: "",
        singleLine = true,
        onValueChange = {
            if (it.length <= MAX_TITLE_LENGTH) {
                textChange.value = it
            }
        },
        placeholder = {
            Text(
                text = stringResource(R.string.lb_entet_title),
                style = MaterialTheme.typography.subtitle1.copy(
                    color = Neutral_Gray_5, fontSize = 14.sp
                ),
            )
        },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {
                focusManager.clearFocus()
            })
            .border(
                BorderStroke(width = 1.dp, color = Neutral_Gray_4),
                shape = RoundedCornerShape(50)
            )
            .fillMaxWidth()
            .height(50.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = White,
            textColor = Color(0xFF303037),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        textStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp),
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )

    Text(
        text = stringResource(R.string.lb_50_characters),
        style = MaterialTheme.typography.subtitle1.copy(
            fontSize = 10.sp, color = Neutral_Gray_7
        ),
        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp, start = 16.dp)
    )
}
