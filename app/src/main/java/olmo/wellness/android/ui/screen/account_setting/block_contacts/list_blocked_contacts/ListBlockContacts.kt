package olmo.wellness.android.ui.screen.account_setting.block_contacts.list_blocked_contacts

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.account_setting.component_common.ButtonWithDot
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun BlockContacts(
    navController: NavController,
    viewModel: ListBlockContactsViewModel = hiltViewModel()
){
    val isLoading = viewModel.isLoading.collectAsState()
    val listUserBlocked = remember(viewModel.userList) {
        viewModel.userList
    }

    Scaffold(
        backgroundColor = Color_Purple_FBC,
        bottomBar = {
            BottomButton(navController)
        }) {
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
        ) {
            SpaceCompose(height = 39.dp)
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(id = R.string.title_block_contacts),
                    style = MaterialTheme.typography.h6
                        .copy(color = Color_Purple_FBC, textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold)
                )
            }
            if (listUserBlocked.value.isNullOrEmpty())
                EmptyBody()
            else
                ListBlockedUsers(listUserBlocked.value)
        }
    }
    LoaderWithAnimation(isPlaying = isLoading.value)
    var isObserverResultFromSearch by remember {
        mutableStateOf(false)
    }
    var userId by remember {
        mutableStateOf(-1)
    }
    navController.currentBackStackEntry
        ?.savedStateHandle?.getLiveData<String>(Constants.BUNDLE_DATA)
        ?.observe(LocalLifecycleOwner.current) { result ->
            if(result != null){
                isObserverResultFromSearch = true
                userId = result.trim().toInt()
            }
        }

    if(isObserverResultFromSearch){
        LaunchedEffect(key1 = true){
            viewModel.updateUserList(userId)
            isObserverResultFromSearch = false
        }
    }

}

@Composable
fun ListBlockedUsers(listBlockedUsers: List<User>) {
    LazyColumn(
        modifier = Modifier,
        state = rememberLazyListState()
    ){
        items(listBlockedUsers.size) { index ->
            ItemBlockedUser(
                listBlockedUsers[index]){
            }
        }
    }
}

@Composable
fun ItemBlockedUser(
    user: User,
    onItemClick: (User) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        val (userAvatar, userFullName, blockButton, underline) = createRefs()

        AsyncImage(
            model = user.avatar,
            contentDescription = "${user.name}_avatar",
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
                        color = Neutral_Gray_9
                    )
                ) {
                    append(user.name?:"")
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
                ),
                shape = RoundedCornerShape(100.dp)
            )
            .constrainAs(blockButton) {
                top.linkTo(userAvatar.top)
                bottom.linkTo(userAvatar.bottom)
                end.linkTo(parent.end)
            }
            .noRippleClickable {
                onItemClick.invoke(user)
            }
        ) {
            Text(
                text = stringResource(id = R.string.action_unblock),
                style = MaterialTheme.typography.body2.copy(
                    color = Color.White
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

@Composable
fun EmptyBody() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SpaceCompose(height = 8.dp)
        Text(
            text = stringResource(id = R.string.des_top_block_add_disturber),
            style = MaterialTheme.typography.h6
                .copy(color = Neutral_Gray_5,fontSize = 16.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(
            modifier = Modifier.height(85.dp)
        )
        AsyncImage(
            model = R.drawable.olmo_img_check_mail,
            contentDescription = "",
            modifier = Modifier.size(151.dp, 150.dp)
        )
        SpaceCompose(height = 6.dp)
        Text(text = stringResource(id = R.string.des_img_check_mail), style = MaterialTheme.typography.subtitle2.copy(color = Color.Black))
        Text(
            text = stringResource(id = R.string.des_block_add_disturber),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body2.copy(
                color = Neutral_Gray_5
            ),
            modifier = Modifier.padding(
                top = 14.dp
            )
        )
    }
}

@Composable
private fun BottomButton(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        ButtonWithDot(
            isHideLeftIcon = true,
            modifier = Modifier
                .padding(bottom = 16.dp),
            onButtonClick = {
                navController.navigate(
                    ScreenName.BlockContactsSearch.route
                )
            },
            title = stringResource(R.string.button_add_disturber)
        )
    }
}
