package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.share

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import olmo.wellness.android.domain.model.SocialNetwork
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.screen.signup_screen.utils.DividerHorizontal
import olmo.wellness.android.ui.theme.*

@Composable
fun ShareBottomSheet(
    modifier: Modifier = Modifier,
    type: ShareScreenType = ShareScreenType.LIVE_STREAM,
    userList : List<User> = emptyList(),
    showMore: () -> Unit,
    onSocialNetworkShareRequest: (SocialNetwork) -> Unit,
    onUserShareRequest: (User) -> Unit,
    onCopyLinkRequest: (String) -> Unit
) {
    val context = LocalContext.current
    val backgroundColor =  when (type) {
        ShareScreenType.LIVE_STREAM -> Color.White
        ShareScreenType.LIVE_SCHEDULING -> Color_gray_FF7
    }

    val titleColor = when (type) {
        ShareScreenType.LIVE_STREAM -> Black_037
        ShareScreenType.LIVE_SCHEDULING -> Color_LiveStream_Main_Color
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                backgroundColor,
                shape = RoundedCornerShape(
                    topEnd = 24.dp,
                    topStart = 24.dp
                )
            )
    ) {
        Title(titleColor)
        DividerHorizontal()
        SuggestUser(
            type = type,
            listUsers = userList,
            showMoreRequest = {
                showMore.invoke()
            },
            shareUserRequest = { user ->
                onUserShareRequest.invoke(user)
            }
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
        )
        SocialNetwork(
            context,
            networks = listOf(
                SocialNetwork.INSTAGRAM,
                SocialNetwork.FACEBOOK,
                SocialNetwork.TIKTOK,
                if (type == ShareScreenType.LIVE_STREAM) SocialNetwork.EMAIL else SocialNetwork.EMAIL_SCHEDULING
            )
        ){ socialNetwork ->
            onSocialNetworkShareRequest.invoke(socialNetwork)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
        )
        ShareThisLike(context, "https://itviec.com/nha-tuyen-dung/olmo-technology"){
            onCopyLinkRequest.invoke(it)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
        )
    }
}

@Composable
private fun SuggestUser(
    type: ShareScreenType,
    listUsers: List<User>,
    showMoreRequest: () -> Unit,
    shareUserRequest: (User) -> Unit
) {
    val iconMore = when(type){
        ShareScreenType.LIVE_STREAM -> painterResource(id = R.drawable.ic_arrow_right_short)
        ShareScreenType.LIVE_SCHEDULING -> painterResource(id = R.drawable.ic_more_dots)
    }

    val iconMoreColorBackground = when(type){
        ShareScreenType.LIVE_STREAM -> Neutral
        ShareScreenType.LIVE_SCHEDULING -> Color_LiveStream_Main_Color
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        if(listUsers.isNotEmpty()){
            val maxQuantity = if(listUsers.size > 4){
                minOf(listUsers.size - 1, 3)
            }else{
                listUsers.size
            }
            for (index in 0 until maxQuantity){
                Box(modifier = Modifier
                    .weight(1f)
                    .height(88.dp)){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val rememberResource = rememberAsyncImagePainter(model = R.drawable.olmo_ic_group_default_place_holder)
                        AsyncImage(
                            listUsers[index].avatar ?: R.drawable.olmo_ic_profile,
                            contentDescription = "",
                            error = rememberResource,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    shareUserRequest.invoke(listUsers[index])
                                },
                            contentScale = ContentScale.Crop
                        )
                        val name = getNameUserChat(listUsers[index])
                        Text(
                            text = if (name.length <= 12) {
                                name
                            } else {
                                name.take(12).plus("..")
                            },
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp)
                                .align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.body2.copy(
                                color = Black_037,
                                fontSize = 12.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        Box(modifier = Modifier
            .weight(1f)
            .height(88.dp)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            iconMoreColorBackground,
                            shape = CircleShape
                        )
                ){
                    Image(
                        painter = iconMore,
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable {
                                showMoreRequest.invoke()
                            }
                    )
                }
                Text(
                    text = "More",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body2.copy(
                        color = Black_037,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SocialNetwork(
    context: Context,
    networks: List<SocialNetwork>,
    onSocialNetworkShareRequest: (SocialNetwork) -> Unit
) {
    Row(
        modifier = Modifier
            .height(88.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        networks.forEach { socialNetwork ->
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = socialNetwork.logo),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                onSocialNetworkShareRequest.invoke(socialNetwork)
                            },
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = socialNetwork.socialName,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.body2.copy(
                            color = Black_037,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ShareThisLike(
    context: Context,
    link: String,
    onCopyLinkRequest: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (title, linkBox, copyButton) = createRefs()

            Text(
                text = "Share This Link",
                style = MaterialTheme.typography.button.copy(
                    fontSize = 14.sp
                ),
                modifier = Modifier
                    .constrainAs(title){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            Box(modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Gray_FE3,
                    shape = RoundedCornerShape(8.dp)
                )
                .width(0.dp)
                .constrainAs(linkBox) {
                    start.linkTo(parent.start)
                    top.linkTo(title.bottom, 8.dp)
                    end.linkTo(copyButton.start, 12.dp)
                    width = Dimension.fillToConstraints
                }
            ){
                Text(
                    modifier = Modifier
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        ),
                    text = link,
                    style = MaterialTheme.typography.button.copy(
                        fontSize = 14.sp,
                        color = Neutral_Gray_4
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            PrimaryLiveButton(
                text = "Copy",
                modifier = Modifier
                    .constrainAs(copyButton) {
                        end.linkTo(parent.end)
                        top.linkTo(linkBox.top)
                        bottom.linkTo(linkBox.bottom)
                    }
                    .noRippleClickable {
                        onCopyLinkRequest.invoke(link)
                    },
                isWrapContentWidth = true,
                paddingVertical = 8.dp,
                paddingHorizontal = 16.dp,
                onClickFunc = {
                    onCopyLinkRequest.invoke(link)
                }
            )
        }
    }
}

@Composable
private fun Title(
    color: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(
                RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .padding(
                16.dp
            )
    ){
        Text(
            text = "Share To",
            style = MaterialTheme.typography.body1.copy(
                color = color,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}