package olmo.wellness.android.ui.screen.follower_following_list

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.user_follow.UserFollowInfo
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.extension.getNameUserFollow
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.QuerySearch
import olmo.wellness.android.ui.common.avatar.UserAvatar
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.isPrefix

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListFollowerBottomSheet(
    isFollowing: Boolean ?= false,
    modifier: Modifier = Modifier,
    listData: List<UserFollowInfo>?,
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: ListFollowerViewModel = hiltViewModel(),
    onUserSelected: ((UserFollowInfo) -> Unit)? = null
){
    val textSearch = remember {
        mutableStateOf("")
    }
    val listDataFilter = remember {
        mutableStateOf(listData)
    }
    LaunchedEffect(key1 = listData){
        if (listData != null) {
            viewModel.bindData(listData)
        }
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
                ){
                    TextField(
                        value = textSearch.value,
                        singleLine = true,
                        onValueChange = {
                            textSearch.value = it
                            listDataFilter.value = listData?.filter {
                                textSearch.value.isEmpty()
                                        || textSearch.value.lowercase()
                                    .isPrefix(getNameUserFollow(it).lowercase())
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
                            .background(color = Neutral_Gray_3, shape = RoundedCornerShape(50))
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Transparent,
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
                    ListUsersInfo(
                        textSearch.value,
                        listDataFilter.value.orEmpty(),
                    ){ user ->
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
private fun ListUsersInfo(
    query: String,
    listDataFilter: List<UserFollowInfo>,
    onItemUserSelected: (UserFollowInfo) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 24.dp
            ),
        state = rememberLazyListState()
    ){
        items(listDataFilter.size) { index ->
            ItemUserInfo(
                query,
                listDataFilter[index],
            ){
                onItemUserSelected.invoke(it)
            }
        }
    }
}

@Composable
private fun ItemUserInfo(
    query: String,
    user: UserFollowInfo,
    onItemClick: ((UserFollowInfo) -> Unit)? = null
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .noRippleClickable {
                onItemClick?.invoke(user)
            },
    ){
        Spacer(
            modifier = Modifier
                .height(14.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            UserAvatar(
                name = user.firstName,
                urlAvatar = user.avatar ?: "",
                modifier = Modifier
                    .size(36.dp)
            )

            Text(
                text = getNameUserFollow(user), style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Neutral_Gray_9
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
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

