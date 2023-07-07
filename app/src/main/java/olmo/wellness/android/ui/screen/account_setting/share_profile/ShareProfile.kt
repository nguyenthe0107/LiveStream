package olmo.wellness.android.ui.screen.account_setting.share_profile

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.core.utils.shareSocialMedia
import olmo.wellness.android.core.utils.shareVideoTikTok
import olmo.wellness.android.core.utils.shareWithEmail
import olmo.wellness.android.domain.model.SocialNetwork
import olmo.wellness.android.ui.common.ItemSwitch
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.business_hours.AvatarMascot
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.share.ShareBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.share.ShareScreenType
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ShareProfile(navController: NavController,
    viewModel: ShareProfileViewModel = hiltViewModel()) {

    val turnOnShare  = remember {
        mutableStateOf(false)
    }

    val linkShare by remember {
        mutableStateOf("https://itviec.com/nha-tuyen-dung/olmo-technology")
    }

    val context = LocalContext.current

    Scaffold(topBar = {
        ToolbarSchedule(
            title = stringResource(R.string.lb_share_profile),
            backIconDrawable = R.drawable.ic_back_calendar,
            navController = navController,
            backgroundColor = Color_LiveStream_Main_Color
        )
    }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color_LiveStream_Main_Color)
        ) {
            val (options, imageCompose, endCompose) = createRefs()
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 32.dp,
                            topEnd = 32.dp
                        )
                    )
                    .background(color = Color_gray_FF7)
                    .fillMaxSize()
                    .fillMaxHeight()
                    .constrainAs(options) {
                        start.linkTo(parent.start)
                        top.linkTo(imageCompose.top, 36.dp)
                    }
                    .padding(top = 50.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color_gray_FF7)
                        .padding(
                            vertical = marginStandard
                        )
                ) {

                    Spacer(modifier = Modifier.padding(vertical = 20.dp))

                    ItemSwitch(title = stringResource(R.string.lb_share_your_kepler_profile), modifier = Modifier,switchDefault = turnOnShare.value,onSwitch ={
                        turnOnShare.value=it
                        if (turnOnShare.value){
                            shareProfile(context,viewModel,linkShare)
                        }
                    })
                }
            }

            AvatarMascot(modifier = Modifier.constrainAs(imageCompose) {
                top.linkTo(parent.top, 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, uri = null, callbackFun = {
            }, src = R.drawable.ic_business_hours)

        }
    }
}

private fun shareProfile(context : Context,viewModel: ShareProfileViewModel,linkShare : String){

    context.let {
        (context as MainActivity).showAsBottomSheet {
            ShareBottomSheet(
                type = ShareScreenType.LIVE_SCHEDULING,
                userList = viewModel.getListUser(),
                showMore = {},
                onCopyLinkRequest = {},
                onSocialNetworkShareRequest = { socialNetwork ->
                    when (socialNetwork) {
                        SocialNetwork.EMAIL,
                        SocialNetwork.EMAIL_SCHEDULING -> {
                            shareWithEmail(context, linkShare)
                        }
                        SocialNetwork.TIKTOK -> {
                            shareVideoTikTok(context, linkShare)
                        }
                        else -> {
                            shareSocialMedia(
                                context,
                                socialNetwork,
                                linkShare
                            )
                        }
                    }
                },
                onUserShareRequest = {}
            )
        }
    }

}

