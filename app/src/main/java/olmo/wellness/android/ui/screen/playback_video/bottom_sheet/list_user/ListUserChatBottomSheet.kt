package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.list_user

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.QuerySearch
import olmo.wellness.android.ui.common.avatar.UserAvatar
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.isPrefix

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListUserChatBottomSheet(
    modifier: Modifier = Modifier,
    listData: List<User>?,
    modalBottomSheetState: ModalBottomSheetState,
    onUserSelected: ((User) -> Unit)? = null
) {
    val textSearch = remember {
        mutableStateOf("")
    }

    val listDataFilter = remember {
        mutableStateOf(listData)
    }
    listDataFilter.value = listData

    val focusManager = LocalFocusManager.current

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContentColor = Transparent,
        sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                ) {
//                QuerySearch(
//                    label = "Search",
//                    modifier = Modifier
//                        .padding(
//                            horizontal = 12.dp,
//                            vertical = 12.dp
//                        ),
//                    query = textSearch.value,
//                    onQueryChanged = { updatedAddress ->
//                        textSearch.value = updatedAddress
//                        listDataFilter.value = listData?.filter {
//                            textSearch.value.isEmpty()
//                                    || textSearch.value.lowercase()
//                                .isPrefix(it.name ?: "".lowercase())
//                        }
//                    },
//                    onDoneActionClick = {
//                    },
//                    onClearClick = {
//                        textSearch.value = ""
//                        listDataFilter.value = listData
//                    },
//                    corner = 16.dp,
//                    textSize = 14.sp,
//                    iconSize = 20.dp,
//                    showIconSearch = true,
//                )


                    TextField(
                        value = textSearch.value,
                        singleLine = true,
                        onValueChange = {
                            textSearch.value = it
                            listDataFilter.value = listData?.filter {
                                textSearch.value.isEmpty()
                                        || textSearch.value.lowercase()
                                    .isPrefix(it.name ?: "".lowercase())
                            }
                        },
                        trailingIcon = {
                            if (textSearch.value.isNotBlank()) {
                                Icon(
                                    Icons.Filled.Close,
                                    null,
                                    tint = Color_gray_6CF,
                                    modifier = Modifier.clickable {
                                        textSearch.value = ""
                                        listDataFilter.value = listData
                                        focusManager.clearFocus()
                                    })
                            }
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search_home),
                                contentDescription = "search",
                                tint = Color_Green_Main
                            )
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.lb_search),
                                style = MaterialTheme.typography.subtitle1.copy(
                                    color = Neutral_Gray_5, fontSize = 14.sp
                                ),
                            )
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {
                                focusManager.clearFocus()
                            })
                            .border(
                                BorderStroke(width = 1.dp, color = Color_Green_Main),
                                shape = RoundedCornerShape(50)
                            )
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = White,
                            textColor = Color_Green_Main,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            cursorColor = Color_Green_Main
                        ),
                        textStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            autoCorrect = true,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                            }
                        )
                    )

                    ListLiveUsers(
                        textSearch.value,
                        listDataFilter.value.orEmpty(),
                    ) { user ->
                        onUserSelected?.invoke(user)
                    }

                    if (listDataFilter?.value?.isEmpty() == true) {
                        EmptyData()
                    }
                }
            } else {
                EmptyBottomSheet()
            }
        }) {
    }


}

@Composable
private fun ListLiveUsers(
    query: String,
    listDataFilter: List<User>,
    onItemUserSelected: (User) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 24.dp
            ),
        state = rememberLazyListState()
    ) {
        items(listDataFilter.size) { index ->
            ItemLiveUsers(
                query,
                listDataFilter[index],
            ) {
                onItemUserSelected.invoke(it)
            }
        }
    }
}

@Composable
private fun ItemLiveUsers(
    query: String,
    user: User,
    onItemClick: ((User) -> Unit)? = null
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .noRippleClickable {
                if (user.id != sharedPrefs.getUserInfoLocal().userId) {
                    onItemClick?.invoke(user)
                }
            },
    ) {
        Spacer(
            modifier = Modifier
                .height(14.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            UserAvatar(
                name = getNameUserChat(user),
                urlAvatar = user.avatar ?: "",
                modifier = Modifier
                    .size(30.dp)
            )

            Text(
                text = getNameUserChat(user), style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            if (sharedPrefs.getUserInfoLocal().userId != user.id) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_comments_chat),
                    contentDescription = "comment",
                    tint = Neutral_Gray_5
                )
            }
        }

        Spacer(
            modifier = Modifier
                .padding(top = 14.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(Neutral_Bare_Gray)
        )
    }
}

