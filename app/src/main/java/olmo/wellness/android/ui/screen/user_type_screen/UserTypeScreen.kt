package olmo.wellness.android.ui.screen.user_type_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.enums.UserRole
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserTypeScreen(
    modalBottomSheetState: ModalBottomSheetState,
    navController: NavController,
    viewModel: UserTypeScreenViewModel = hiltViewModel(),
    onNavigationSelectCategory: ((status: Boolean, userType: UserTypeModel) -> Unit)? = null
) {
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                viewModel.getUserType()
            }
        }
    }
    val scope = rememberCoroutineScope()
    val userDefault = viewModel.userRole.collectAsState()
    Column(
        modifier = Modifier
            .background(White)
            .fillMaxHeight(0.85f)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val activeColor = Color_Purple_7F4_20
        val shadowColor = Color_Purple_7F4_20
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = R.drawable.olmo_img_welcome_signup_png,
                        contentDescription = "",
                        contentScale = ContentScale.Inside
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(), horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_ellipse),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = space_52, end = space_52)
            ) {
                Row(
                    modifier = Modifier
                        .noRippleClickable {
                            viewModel.updateRole(UserRole.BUYER)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentSize()
                            .size(50.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                spotColor = shadowColor,
                                ambientColor = shadowColor
                            ),
                        color = if (userDefault.value == UserRole.BUYER) {
                            activeColor
                        } else {
                            White
                        }
                    ) {
                        AsyncImage(
                            model = "",
                            contentDescription = "",
                            error = painterResource(R.drawable.ic_buyer),
                            modifier = Modifier
                                .size(18.dp, 25.dp)
                                .border(
                                    if (userDefault.value != UserRole.BUYER) {
                                        1.dp
                                    } else 0.dp,
                                    if (userDefault.value != UserRole.BUYER) {
                                        Color_LiveStream_Main_Color
                                    } else {
                                        White
                                    }, CircleShape
                                ),
                            contentScale = ContentScale.Inside
                        )
                    }
                    SpaceHorizontalCompose(width = 20.dp)
                    Column {
                        Text(
                            text = stringResource(R.string.buyer),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(
                                top = marginStandard,
                                bottom = marginMinimum
                            )
                        )
                        Text(
                            text = stringResource(R.string.buyer_action),
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(bottom = marginStandard)
                        )
                    }
                }

                SpaceCompose(height = 20.dp)

                Row(
                    modifier = Modifier
                        .noRippleClickable {
                            viewModel.updateRole(UserRole.SELLER)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentSize()
                            .size(50.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                spotColor = shadowColor,
                                ambientColor = shadowColor
                            ),
                        color = if (userDefault.value == UserRole.SELLER) {
                            activeColor
                        } else {
                            White
                        }
                    ) {
                        AsyncImage(
                            model = "",
                            contentDescription = "",
                            error = painterResource(R.drawable.ic_shop),
                            modifier = Modifier
                                .size(18.dp, 25.dp)
                                .border(
                                    if (userDefault.value != UserRole.SELLER) {
                                        1.dp
                                    } else 0.dp,
                                    if (userDefault.value != UserRole.SELLER) {
                                        Color_LiveStream_Main_Color
                                    } else {
                                        White
                                    },
                                    CircleShape
                                ),
                            contentScale = ContentScale.Inside
                        )
                    }
                    SpaceHorizontalCompose(width = 20.dp)
                    Column {
                        Text(
                            text = stringResource(R.string.shop_owner),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(
                                top = marginStandard,
                                bottom = marginMinimum
                            )
                        )
                        Text(
                            text = stringResource(R.string.shop_owner_action),
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(bottom = marginStandard)
                        )
                    }
                }

                SpaceCompose(height = 20.dp)

                Row(
                    modifier = Modifier
                        .noRippleClickable {
                            viewModel.updateRole(UserRole.KOL)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Row {
                        Surface(
                            modifier = Modifier
                                .wrapContentSize()
                                .size(50.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = CircleShape,
                                    spotColor = shadowColor,
                                    ambientColor = shadowColor
                                ),
                            color = if (userDefault.value == UserRole.KOL) {
                                activeColor
                            } else {
                                White
                            }
                        ) {
                            AsyncImage(
                                model = "",
                                contentDescription = "",
                                error = painterResource(R.drawable.ic_kols),
                                modifier = Modifier
                                    .size(18.dp, 25.dp)
                                    .border(
                                        if (userDefault.value != UserRole.KOL) {
                                            1.dp
                                        } else 0.dp,
                                        if (userDefault.value != UserRole.KOL) {
                                            Color_LiveStream_Main_Color
                                        } else {
                                            White
                                        },
                                        CircleShape
                                    ),
                                contentScale = ContentScale.Inside
                            )
                        }
                        SpaceHorizontalCompose(width = 20.dp)
                        Column {
                            Text(
                                text = stringResource(R.string.kols),
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(
                                    top = marginStandard,
                                    bottom = marginMinimum
                                )
                            )
                            Text(
                                text = stringResource(R.string.kols_action),
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.padding(bottom = marginStandard)
                            )
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GroupButtonBottomCompose(confirmCallback = {
                userDefault.value.let {
                    viewModel.onUserRoleClick(it)
                    onNavigationSelectCategory?.invoke(true, UserTypeModel.invoke(userDefault.value.name))
                }
                scope.launch {
                    modalBottomSheetState.hide()
                }
            }, cancelCallback = {
                scope.launch {
                    modalBottomSheetState.hide()
                }
            })
        }
    }
}