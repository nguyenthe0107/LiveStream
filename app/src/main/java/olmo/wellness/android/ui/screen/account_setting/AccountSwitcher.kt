package olmo.wellness.android.ui.screen.account_setting
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest
import olmo.wellness.android.R
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.livestream.utils.Effects
import olmo.wellness.android.ui.screen.account_setting.component_common.OptionItemUI
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.theme.*

@Composable
fun AccountSwitcher(navController: NavController, viewModel: AccountSwitcherViewModel = hiltViewModel()) {

    val lifecycleOwner = LocalLifecycleOwner.current
    Effects.Disposable(
        lifeCycleOwner = lifecycleOwner,
        onStart = {
            viewModel.getProfile()
        },
        onStop = {
        }
    )
    val isLoading = viewModel.isNeedLoading.collectAsState()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color_LiveStream_Main_Color)){
        val profileModel = viewModel.profileModel.collectAsState()
        val avatarLink = profileModel.value.avatar.orEmpty()
        val nameProfile = viewModel.nameUser.collectAsState()
        val nameProfileFinal = nameProfile.value ?: ""
        val typeUser = sharedPrefs.getUserInfoLocal().userTypeModel ?: UserTypeModel.SELLER
        val (avatar, options, bottomItem, icClose) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.olmo_ic_close_white),
            contentDescription = "",
            modifier = Modifier
                .size(20.dp)
                .constrainAs(icClose) {
                    top.linkTo(avatar.top)
                    end.linkTo(parent.end, 18.dp)
                }
                .noRippleClickable {
                    navController.popBackStack()
                },
            colorFilter = ColorFilter.tint(Color_gray_FF7)
        )
        ListOptionItems(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp
                    )
                )
                .constrainAs(options) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(avatar.top, 48.dp)
                    bottom.linkTo(bottomItem.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }, navController,typeUser
        )
        Box(modifier = Modifier
            .background(shape = CircleShape, color = White)
            .constrainAs(avatar) {
                top.linkTo(parent.top, 20.dp)
                start.linkTo(parent.start, 44.dp)
            }
            .padding(paddingValues = PaddingValues(12.dp))){
            AvatarProfile(
                modifier = Modifier,
                avatarLink
            )
        }
        BottomOptionButtons(
            modifier = Modifier
                .constrainAs(bottomItem){
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, navController,
            nameProfileFinal,
            viewModel
        )
    }
    LoadingScreen(isLoading = isLoading.value)
}

@Composable
fun AvatarProfile(modifier: Modifier, avatarLink : String) {
    Surface(
        modifier = Modifier
            .background(color = Color_Black_Alpha_20, shape = CircleShape)
            .shadow(elevation = 8.dp, shape = CircleShape)){
        Box(
            modifier = modifier
                .size(72.dp)
                .background(
                    White,
                    shape = CircleShape
                )
        ){
            val painter = painterResource(R.drawable.olmo_ic_group_default_place_holder)
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                shape = CircleShape){
                AsyncImage(
                    model = avatarLink,
                    error = painter,
                    contentDescription = "avatar",
                    modifier = modifier
                        .size(72.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .border(5.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun ListOptionItems(
    modifier: Modifier = Modifier,
    navController: NavController,
    typeUser: UserTypeModel
){
    val listOptionItems: List<OptionItemViewData> = listOf(
        OptionItemViewData(
            R.drawable.ic_setting_solid,
            stringResource(id = R.string.account_switcher_account_setting)
        ) {
            navController.navigate(ScreenName.MyProfileDashboardScreen.route)
        },
        OptionItemViewData(
            R.drawable.ic_live_scheduling,
            stringResource(id = R.string.account_switcher_live_scheduling)
        ){
            navController.navigate(ScreenName.LiveSchedulingScreen.route)
        },
        OptionItemViewData(
            R.drawable.ic_olmo_my_bookings,
            stringResource(id = R.string.account_switcher_my_bookings)
        ){
            navController.navigate(ScreenName.MyDashBoardBookingScreen.route)
        },
       /* OptionItemViewData(
            R.drawable.ic_kepler_balance,
            stringResource(id = R.string.kepler_balance)
        ){
            navController.navigate(ScreenName.KeplerBalanceSellerScreen.route)
        },*/
        /*OptionItemViewData(
            R.drawable.ic_refferral,
            stringResource(id = R.string.account_switcher_refferal_friends)
        ){
        },
        OptionItemViewData(
            R.drawable.ic_call_support,
            stringResource(id = R.string.account_switcher_support_center),
            ::onHelpCallKeplerClick
        ),
        OptionItemViewData(
            R.drawable.ic_talk_kepler,
            stringResource(id = R.string.account_switcher_talk_with_kepler),
            ::onTalkKeplerClick
        )*/
    )

    var listOptionFinal = listOf<OptionItemViewData>()

    val listOptionItemsBuyer: List<OptionItemViewData> = listOf(
        OptionItemViewData(
            R.drawable.ic_setting_solid,
            stringResource(id = R.string.account_switcher_account_setting)
        ) {
            navController.navigate(ScreenName.MyProfileDashboardScreen.route)
        },
        OptionItemViewData(
            R.drawable.ic_calendar_scheduling,
            stringResource(id = R.string.account_switcher_calendar_scheduling)
        ){
            navController.navigate(ScreenName.CalendarScreen.route)
        },
        OptionItemViewData(
            R.drawable.ic_olmo_my_bookings,
            stringResource(id = R.string.account_switcher_my_bookings)
        ){
            navController.navigate(ScreenName.MyDashBoardBookingScreen.route)
        },
    )

    listOptionFinal = when(typeUser){
        UserTypeModel.BUYER -> {
            listOptionItemsBuyer
        }
        else -> {
            listOptionItems
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color_gray_FF7
            )
    ){
        Column(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
        ) {
            listOptionFinal.forEachIndexed { index, optionItemViewData ->
                OptionItemUI(listOptionFinal[index])
            }
        }
    }

}

@Composable
private fun BottomOptionButtons(
    modifier: Modifier = Modifier,
    navController: NavController,
    name: String,
    viewModel: AccountSwitcherViewModel
){
    var nameFinal = ""
    if(name.isNotEmpty()){
        nameFinal = name
    }
    val isLogoutSuccess = viewModel.isLogoutSuccess.collectAsState()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(Color_gray_FF7)
    ){
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()){
            OptionItemUI(
                optionItemViewData = OptionItemViewData(
                    iconSrcResId = R.drawable.olmo_ic_logout,
                    title = "Log Out $nameFinal"
                ){
                    viewModel.handleLogout()
                },
                false,
                Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 16.dp
                    )
            )
        }
        LaunchedEffect(isLogoutSuccess.value){
            snapshotFlow { isLogoutSuccess.value }.collectLatest { status ->
                if(status){
                    val isDeepLink = false
                    navController.navigate(ScreenName.SignInEmailScreen.route + "/$isDeepLink")
                }
            }
        }
    }
}



