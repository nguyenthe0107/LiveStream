package olmo.wellness.android.ui.screen.notification

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.notification.NotificationInfo
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun NotificationListItem(item: NotificationInfo,
                         clickListener: ((NotificationInfo) -> Unit) ?= null,
                         context: Context ?= null,
                         isShowDivider: Boolean ?= false
){
    ConstraintLayout(
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .clickable { clickListener?.let { it(item) } }
            .padding(horizontal = 12.dp)
            .height(88.dp))
    {
        val (image, title, source, unRead, time, statusView, underline) = createRefs()
        val bottomBarrier = createBottomBarrier(image, source, statusView)
        val infoData = item.dataNotification?.payload?.livestream
        val rememberResource = rememberAsyncImagePainter(model = R.drawable.olmo_ic_group_default_place_holder)
        AsyncImage(
            model = item.image,
            error = rememberResource,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .constrainAs(image) {
                    linkTo(start = parent.start, end = title.start)
                    top.linkTo(title.top)
                }
        )
        var titleFinal = infoData?.title ?: ""
        if(titleFinal.length >= 17){
            titleFinal = titleFinal.substring(0, 15)
        }
        Text(
            text = titleFinal,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Neutral_Gray_9,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            modifier = Modifier.constrainAs(title) {
                start.linkTo(image.end, 16.dp)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            text = if(item.createdAt != null){
                DateTimeHelper.showDateConversationToString(dateAt = item.createdAt,
                    context = context
                ).toString()
            }else{ "" },
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Neutral_Gray_7
            ),
            modifier = Modifier.constrainAs(time) {
                end.linkTo(parent.end, margin = 8.dp)
                top.linkTo(title.top)
            }
        )
        Text(
            text = item.description?:"",
            style = MaterialTheme.typography.bodySmall.copy(
                color = Black_037,
                fontSize = 10.sp
            ),
            maxLines = 2,
            modifier = Modifier
                .constrainAs(source) {
                    linkTo(start = title.start, end = unRead.start)
                    top.linkTo(title.bottom, 2.dp)
                    width = Dimension.fillToConstraints
                }
                .alpha(0.6f)
        )
        if(item.isSeen == false){
            Box(
                modifier = Modifier
                    .background(
                        color = Color_BLUE_7FF,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .constrainAs(statusView) {
                        start.linkTo(source.start)
                        top.linkTo(source.bottom, 10.dp)
                    }
            ){
                Text(
                    text = "New", style = MaterialTheme.typography.labelSmall.copy(
                        color = Color_BLUE_9FF
                    ),
                    modifier = Modifier
                        .padding(
                            horizontal = 6.dp
                        )
                        .align(Center),
                    textAlign = TextAlign.Center
                )
            }
        }else{
            Box(
                modifier = Modifier
                    .constrainAs(statusView) {
                        start.linkTo(source.start)
                        top.linkTo(source.bottom, 10.dp)
                    }
            ){
                Text(
                    text = "",
                    modifier = Modifier
                        .padding(
                            horizontal = 6.dp
                        )
                        .align(Center),
                    textAlign = TextAlign.Center
                )
                SpaceCompose(height = 10.dp)
            }
        }
        if(isShowDivider == true){
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .background(
                        Gray_EF3
                    )
                    .constrainAs(underline) {
                        start.linkTo(source.start)
                        end.linkTo(parent.end)
                        top.linkTo(bottomBarrier, 25.dp)
                        width = Dimension.fillToConstraints
                    }
            )
        }
    }
}
