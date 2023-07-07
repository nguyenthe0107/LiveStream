package olmo.wellness.android.ui.livestream.view.streamer

import android.app.Activity
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.WTF
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.share.ListUserShareBottomSheet

fun showListShareUser(listUser: List<User>, context: Activity) {
    context.showAsBottomSheet{
        ListUserShareBottomSheet(
            listData = listUser){
        }
    }
}
