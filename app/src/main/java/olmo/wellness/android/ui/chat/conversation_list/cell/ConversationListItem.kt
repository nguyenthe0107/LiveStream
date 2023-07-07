package olmo.wellness.android.ui.chat.conversation_list.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.extension.getUserChat
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.avatar.UserAvatar
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.theme.Neutral_Gray
import olmo.wellness.android.ui.theme.Neutral_Gray_7

@Composable
fun MessageListItem(item: DetailRoom, clickListener: (DetailRoom) -> Unit) {

    val context = LocalContext.current
    val user = remember {
        mutableStateOf<User?>(null)
    }
    user.value = item.users?.let { getUserChat(it) }


    Column(modifier = Modifier.fillMaxWidth().clickable {
        clickListener.invoke(item)
    }) {

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatar(
                    name = getNameUserChat(user = user.value),
                    urlAvatar = user.value?.avatar,
                    modifier = Modifier
                        .size(50.dp),
                    isOnline = user.value?.isOnline ?: false
                )

                Column(modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)) {
                    Text(
                        text = getNameUserChat(user = user.value),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    )

                    val styleText =
                        if (user.value?.indexMessageRead != null && item.totalMessage != null) {
                            if (user.value?.indexMessageRead!! < item.totalMessage) {
                                MaterialTheme.typography.subtitle2
                            } else {
                                MaterialTheme.typography.subtitle1
                            }
                        } else MaterialTheme.typography.subtitle1

                    Text(
                        text = item.lastMessage?.content ?: "",
                        style = styleText,
                        maxLines = 2,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                }
            }

            Text(
                text = DateTimeHelper.showDateConversationToString(
                    item.lastMessage?.createdAtTimestamp,
                    context
                ) ?: "",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 10.sp,
                    color = Neutral_Gray_7, lineHeight = 14.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Neutral_Gray
                )
        )
    }
}
