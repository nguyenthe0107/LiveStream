package olmo.wellness.android.ui.screen.account_setting.block_contacts.seach_contacts

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.WTF
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.AlertData
import olmo.wellness.android.ui.common.QuerySearch
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogAction
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.isPrefix

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun BlockContactsSearch(navController: NavController, viewModel: SearchBlockContactsViewModel = hiltViewModel()) {
    val showBlockUserDialog = remember {
        mutableStateOf(AlertData(false))
    }
    val isShowBlockUserDialog = remember {
        mutableStateOf(false)
    }
    val isLoading = viewModel.isLoading.collectAsState()
    val listUser = remember(viewModel.userList) {
        viewModel.userList
    }
    val userSelected = remember(viewModel.userSelected) {
        viewModel.userSelected
    }
    Scaffold(
        backgroundColor = Color_Purple_FBC) {
        Column(
            modifier = Modifier
                .padding(top = 16.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp
                    )
                )
                .background(color = Color_gray_FF7)
                .fillMaxSize()
                .fillMaxHeight()
        ){
            val textSearch = remember {
                mutableStateOf("")
            }
            val listData = listUser.value
            val listDataFilter = remember {
                mutableStateOf(listData)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()){
                QuerySearch(
                    label = "Type something",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            16.dp
                        ),
                    query = textSearch.value,
                    onQueryChanged = { updatedAddress ->
                        textSearch.value = updatedAddress
                        listDataFilter.value = listData.filter {
                            textSearch.value.isEmpty()
                                    || textSearch.value.lowercase().isPrefix(it.fullName?.lowercase()?:"")
                        }
                    },
                    onDoneActionClick = {
                        WTF("RESULT: ${textSearch.value}")
                    },
                    onClearClick = {
                        textSearch.value = ""
                    },
                    showIconSearch = false,
                    corner = 35.dp,
                    backgroundColor = Color_gray_3F9
                )

                Text(
                    stringResource(id = R.string.des_search_block_contacts),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Medium,
                        color = Neutral_Gray_5
                    ),
                    modifier = Modifier.padding(16.dp)
                )
                ListUsers(textSearch.value, listDataFilter.value){ user ->
                    isShowBlockUserDialog.value = true
                    showBlockUserDialog.value = AlertData(
                        true,
                        "Block ${user.fullName}?",
                        "Do you want to block this contact?",
                        user
                    )
                    viewModel.blockUser(user)
                }
            }
        }
    }

    LoaderWithAnimation(isPlaying = isLoading.value)

    DialogAction(openDialogCustom = isShowBlockUserDialog,title = showBlockUserDialog.value.title,
        description = showBlockUserDialog.value.text,
        titleBtnCancel = stringResource(id = R.string.no),
        titleBtnConfirm = stringResource(id = R.string.yes),
        btnCancelCallback= {
            isShowBlockUserDialog.value = false
        }, btnConfirmCallback = {
            isShowBlockUserDialog.value = false
            showBlockUserDialog.value.user?.let { it1 -> viewModel.blockUser(it1) }
        })

    if(userSelected.value != null){
        LaunchedEffect(key1 = true){
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(Constants.BUNDLE_DATA, userSelected.value?.id.toString())
            navController.popBackStack()
        }
    }
}

@Composable
fun ListUsers(
    query: String,
    listDataFilter: List<User>,
    onItemUserSelected: (User) -> Unit
) {
    LazyColumn(
        modifier = Modifier,
        state = rememberLazyListState()
    ) {
        items(listDataFilter.size){ index ->
            ItemUsers(
                query,
                listDataFilter[index]
            ){
                onItemUserSelected.invoke(it)
            }
        }
    }
}

@Composable
fun ItemUsers(query: String, user: User, onItemClick: (User) -> Unit) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp
        )
    ) {
        val partsOfName = getPartsOfName(query, user.fullName?:"")
        val (userAvatar, userFullName, blockButton, underline) = createRefs()

        AsyncImage(
            model = user.avatar,
            contentDescription = "${user.fullName}_avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .constrainAs(userAvatar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clip(RoundedCornerShape(20.dp))
        )

        Text(
            buildAnnotatedString {
                  withStyle(
                      SpanStyle(
                          fontFamily = MontserratFont,
                          fontWeight = FontWeight.Normal,
                          fontSize = 12.sp,
                          letterSpacing = 0.4.sp,
                          color = Color_Purple_FBC
                      )
                  ){
                      append(partsOfName.first)
                  }

                  withStyle(
                      SpanStyle(
                          fontFamily = MontserratFont,
                          fontWeight = FontWeight.Normal,
                          fontSize = 12.sp,
                          letterSpacing = 0.4.sp,
                          color = Neutral_Gray_7
                      )
                  ){
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
                    end.linkTo(blockButton.start, 16.dp)
                    width = Dimension.fillToConstraints
                },
        )

        Box(modifier = Modifier
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color_LiveStream_Main_Color,
                        Color_LiveStream_Main_Color,
                        Color_LiveStream_Light_Color,
                    )
                ), shape = RoundedCornerShape(100.dp)
            )
            .constrainAs(blockButton) {
                top.linkTo(userAvatar.top)
                bottom.linkTo(userAvatar.bottom)
                end.linkTo(parent.end)
            }
            .noRippleClickable {
                onItemClick.invoke(user)
            }
        ){
            Text(
                text = stringResource(id = R.string.action_block),
                style = MaterialTheme.typography.subtitle2.copy(
                    color = White
                ),
                modifier = Modifier
                    .padding(
                        vertical = 4.dp,
                        horizontal = 16.dp
                    )
            )
        }

        Box(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Neutral_Gray_3)
            .constrainAs(underline) {
                top.linkTo(userAvatar.bottom, 16.dp)
            }
        )
    }
}

fun getPartsOfName(query: String, fullname: String): Pair<String, String> {
    val loweredFullName = fullname.lowercase()
    val loweredQuery = query.lowercase()

   return when {
       loweredQuery.isEmpty()
               || loweredFullName.first() != loweredQuery.first()
               || loweredFullName.length < loweredQuery.length
       -> {
           "" to fullname
       }
       else -> {
           if (!loweredQuery.isPrefix(loweredFullName)) {
               WTF("here")
               "" to fullname
           }

           fullname.substring(
               0, query.length,
           ) to fullname.substring(
               query.length, fullname.length
           )
       }
   }
}