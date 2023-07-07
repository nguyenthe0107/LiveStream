package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.share

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.core.utils.getScreenHeight
import olmo.wellness.android.core.utils.pxToDp
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.WTF
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.ui.common.QuerySearch
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.isPrefix
import olmo.wellness.android.util.splitPrefix

@Composable
fun ListUserShareBottomSheet(
    modifier: Modifier = Modifier,
    listData: List<User>,
    onUsersSelected: (List<User>) -> Unit
) {

    val context: Context = LocalContext.current

    val textSearch = remember {
        mutableStateOf("")
    }

    val listDataFilter = remember {
        mutableStateOf(listData)
    }

    val listUsersSelected = remember {
        mutableStateOf(emptyList<User>())
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                Black_466.copy(
                    alpha = 0.5f
                ),
                shape = RoundedCornerShape(
                    topEnd = 24.dp,
                    topStart = 24.dp
                )
            )
            .background(
                GRAY_99F,
                shape = RoundedCornerShape(
                    topEnd = 24.dp,
                    topStart = 24.dp
                )
            )
    ) {
        QuerySearch(
            label = "Search",
            modifier = Modifier
                .padding(
                    horizontal = 12.dp,
                    vertical = 12.dp
                ),
            query = textSearch.value,
            onQueryChanged = { updatedAddress ->
                textSearch.value = updatedAddress
                listDataFilter.value = listData.filter {
                    textSearch.value.isEmpty()
                            || textSearch.value.lowercase().isPrefix(it.name?:"".lowercase())
                }
            },
            onDoneActionClick = {
                WTF("RESULT: ${textSearch.value}")
            },
            onClearClick = {
                textSearch.value = ""
                listDataFilter.value = listData
            },
            corner = 16.dp,
            textSize = 14.sp,
            iconSize = 20.dp,
            showIconSearch = true,
        )
        ListUsers(
            textSearch.value,
            listDataFilter.value,
            listUsersSelected.value
        ) { user, isSelected ->
            val listSelectedTmp = listUsersSelected.value
            if (isSelected){
                if (!listSelectedTmp.any { user.id == it.id }){
                    listSelectedTmp.toMutableList().also {
                        it.add(user)
                        it.toList()
                    }.apply {
                        listUsersSelected.value = this
                    }
                }
            }
            else {
                if (listUsersSelected.value.any { user.id == it.id }){
                    listSelectedTmp.toMutableList().also {
                        it.remove(user)
                        it.toList()
                    }.apply {
                        listUsersSelected.value = this
                    }
                }
            }
        }
        SendButton {
            onUsersSelected.invoke(listUsersSelected.value)
            (context as Activity).onBackPressed()
        }
    }

}

@Composable
private fun ListUsers(
    query: String,
    listDataFilter: List<User>,
    listUsersSelected: List<User>,
    onUserSelected: (User, Boolean) -> Unit
) {
    val screenHeight = remember {
        getScreenHeight()
    }

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .heightIn(
                min = (screenHeight * 0.3)
                    .toInt()
                    .pxToDp(context).dp,
                max = (screenHeight * 0.5)
                    .toInt()
                    .pxToDp(context).dp
            )
            .background(Transparent)
            .fillMaxWidth()
            .padding(
                bottom = 24.dp
            ),
        state = rememberLazyListState()
    ) {
        items(listDataFilter.size) { index ->
            val userItem = listDataFilter[index]
            ItemUser(
                query,
                listDataFilter[index],
                listUsersSelected.any {
                    it.id == userItem.id
                }
            ) { user, isSelected ->
                onUserSelected.invoke(user, isSelected)
            }
        }
    }
}

@Composable
private fun ItemUser(
    query: String,
    user: User,
    isSelectedUser: Boolean,
    onItemClick: (User, Boolean) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(Transparent)
            .wrapContentHeight()
            .padding(
                top = 16.dp,
            )
    ) {
        val fullName = getNameUserChat(user)
        val partsOfName = fullName.splitPrefix(query)
        val (userAvatar, userFullName, imgChat, underline) = createRefs()
        val textColorDefault = White
        val textColorSelected = Color_LiveStream_Main_Color

        AsyncImage(
            model = user.avatar ?: R.drawable.olmo_ic_profile,
            contentDescription = "_avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(36.dp)
                .constrainAs(userAvatar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 16.dp)
                }
                .clip(RoundedCornerShape(20.dp))
        )

        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontFamily = MontserratFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = textColorSelected
                    )
                ) {
                    append(partsOfName.first)
                }

                withStyle(
                    SpanStyle(
                        fontFamily = MontserratFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = textColorDefault
                    )
                ) {
                    append(partsOfName.second)
                }
            },
            textAlign = TextAlign.Left,
            modifier = Modifier
                .wrapContentHeight()
                .constrainAs(userFullName) {
                    top.linkTo(userAvatar.top)
                    bottom.linkTo(userAvatar.bottom)
                    start.linkTo(userAvatar.end, 16.dp)
                    end.linkTo(imgChat.start, 16.dp)
                    width = Dimension.fillToConstraints
                },
        )

        RadioButton(
            selected = isSelectedUser,
            modifier = Modifier
                .size(20.dp)
                .constrainAs(imgChat) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, 16.dp)
                },
            colors = RadioButtonDefaults.colors(
                Color_gray_6CF,
                Neutral_Bare_Gray
            ),
            onClick = {
                onItemClick.invoke(user, !isSelectedUser)
            })

        Box(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Gray_FE3)
            .constrainAs(underline) {
                top.linkTo(userAvatar.bottom, 8.dp)
            }
        )
    }
}


@Composable
private fun SendButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White)
    ) {
        PrimaryLiveButton(
            text = "Send",
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    bottom = 34.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            isWrapContentWidth = false,
            paddingVertical = 8.dp,
            paddingHorizontal = 16.dp,
            onClickFunc = {
                onClick.invoke()
            }
        )
    }
}