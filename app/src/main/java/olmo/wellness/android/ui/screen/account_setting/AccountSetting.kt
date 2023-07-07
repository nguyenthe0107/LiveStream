package olmo.wellness.android.ui.screen.account_setting

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.account_setting.component_common.OptionItemUI
import olmo.wellness.android.ui.screen.delete_account.RequestToDeleteAccountBottomSheet
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountSetting(navController: NavController) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color_LiveStream_Main_Color)){
        val (options, navigationItem) = createRefs()
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
                    top.linkTo(navigationItem.top, 36.dp)
                }
                .padding(top = 50.dp)
        ) {
            ListOptionAccountSettingItems(
                navController
            )
        }
        NavigationItem(
            modifier = Modifier
                .constrainAs(navigationItem) {
                    top.linkTo(parent.top, 20.dp)
                    start.linkTo(parent.start, 44.dp)
                }.noRippleClickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun NavigationItem(modifier: Modifier) {
    Box(
        modifier = modifier
            .size(58.dp)
            .background(
                Color_LiveStream_Main_Color,
                shape = CircleShape
            )){
        Card(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            shape = CircleShape,
            elevation = 2.dp,
            backgroundColor = Color_LiveStream_Main_Color){
            Image(
                painter = painterResource(id = R.drawable.ic_back_calendar),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ListOptionAccountSettingItems(
    navController: NavController
) {
    val modalRequestDelete =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                false
            }, skipHalfExpanded = true)

    val scope = rememberCoroutineScope()
    val listOptionSectionMyAccount: List<OptionItemViewData> = listOf(
        OptionItemViewData(
            R.drawable.olmo_ic_profile_filled_purple,
            stringResource(id = R.string.title_item_my_profile)
        ) {
            navController.navigate(ScreenName.MyProfileDashboardScreen.route)
        },
        OptionItemViewData(
            R.drawable.olmo_ic_delete_filled_purple,
            stringResource(id = R.string.title_item_request_delete_account)
        ) {
            scope.launch {
                modalRequestDelete.show()
            }
        },
        OptionItemViewData(
            R.drawable.olmo_ic_business_hour_filled_purple,
            stringResource(id = R.string.title_item_business_hour)
        ){
            navController.navigate(ScreenName.BusinessHoursScreen.route)
        }
    )

    val listOptionSectionSettings = listOf(
        OptionItemViewData(
            R.drawable.olmo_ic_chat_setting_filled_purple,
            stringResource(id = R.string.title_item_chat_setting)
        ) {
            navController.navigate(ScreenName.ChatSettingScreen.route)
        },
        OptionItemViewData(
            R.drawable.olmo_ic_noti_filled_purple,
            stringResource(id = R.string.title_item_notification_setting)
        ) {
            navController.navigate(ScreenName.NotificationSettingScreen.route)
        },
        OptionItemViewData(
            R.drawable.olmo_ic_privacy_filled_purple,
            stringResource(id = R.string.title_item_privacy_setting)
        ) {
            navController.navigate(ScreenName.PrivacySettingsScreen.route)
        },
        OptionItemViewData(
            R.drawable.olmo_ic_language_filled_purple,
            stringResource(id = R.string.title_item_language)
        ) {
            navController.navigate(ScreenName.ChooseLanguageScreen.route)
        },
        OptionItemViewData(
            R.drawable.olmo_ic_contact_syncing_filled,
            stringResource(id = R.string.title_item_contact_syncing)
        ) {
            navController.navigate(ScreenName.ContactSyncingScreen.route)
        },
        OptionItemViewData(
            R.drawable.olmo_ic_sharing_filled,
            stringResource(id = R.string.title_item_share_profile)
        ) {
            navController.navigate(ScreenName.ShareProfileScreen.route)
        },
    )
    LazyColumn {
        item {
            Row(
                modifier = Modifier
                    .padding(top = marginStandard, start = marginDouble, end = marginDouble)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.title_item_header_my_profile),
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Color_Purple_FBC,
                        fontFamily = MontserratFont,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        items(listOptionSectionMyAccount.size) { optionItemIndex ->
            OptionItemUI(listOptionSectionMyAccount[optionItemIndex])
        }
        item {
            Row(
                modifier = Modifier
                    .padding(top = marginStandard, start = marginDouble, end = marginDouble)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.title_item_header_settings),
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Color_Purple_FBC,
                        fontFamily = MontserratFont,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        items(listOptionSectionSettings.size) { optionItemIndex ->
            OptionItemUI(listOptionSectionSettings[optionItemIndex])
        }
    }
//    RequestToDeleteAccountBottomSheet(modalBottomSheetState = modalRequestDelete, viewModel = null)
}