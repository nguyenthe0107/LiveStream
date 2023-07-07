package olmo.wellness.android.ui.screen.profile_dashboard

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.enums.UploadFileType
import olmo.wellness.android.data.model.definition.AuthMethod
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.defination.ProfileSuccessType
import olmo.wellness.android.domain.model.defination.UserFiledType
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.screen.capture_screen.CaptureScreen
import olmo.wellness.android.ui.screen.delete_account.RequestToDeleteAccountBottomSheet
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.verification_code.VerifyCodeProfileScreen
import olmo.wellness.android.ui.screen.profile_dashboard.edit_address.EditAddressScreen
import olmo.wellness.android.ui.screen.profile_dashboard.edit_bio.EditBioScreen
import olmo.wellness.android.ui.screen.profile_dashboard.edit_birthday.EditBirthdayProfileScreen
import olmo.wellness.android.ui.screen.profile_dashboard.edit_gender.EditGenderScreen
import olmo.wellness.android.ui.screen.profile_dashboard.edit_mail.CheckMailScreen
import olmo.wellness.android.ui.screen.profile_dashboard.edit_mail.EditMailScreen
import olmo.wellness.android.ui.screen.profile_dashboard.edit_phone.EditPhoneScreen
import olmo.wellness.android.ui.screen.profile_dashboard.edit_service_name.EditServiceNameInfoScreen
import olmo.wellness.android.ui.screen.profile_dashboard.edit_username.EditUserNameScreen
import olmo.wellness.android.ui.screen.profile_dashboard.items.AvatarInfoProfile
import olmo.wellness.android.ui.screen.profile_dashboard.gallery_compose.GalleryCompose
import olmo.wellness.android.ui.screen.profile_dashboard.items.ItemInfoProfile
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint(
    "CoroutineCreationDuringComposition", "SimpleDateFormat",
    "StateFlowValueCalledInComposition", "MutableCollectionMutableState",
    "UnusedMaterialScaffoldPaddingParameter"
)
@ExperimentalCoroutinesApi
@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    com.google.accompanist.permissions.ExperimentalPermissionsApi::class,
    coil.annotation.ExperimentalCoilApi::class
)
@Composable
fun MyProfileDashBoard(
    navController: NavController,
    viewModel: MyProfileDashBoardViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val loadingValue = viewModel.isLoading.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    isLoading = loadingValue.value
    val imageSelected = remember { mutableStateOf<Uri?>(null) }
    val imageTakePicture = remember { mutableStateOf<Uri?>(null) }
    var isCaptureDisplay by remember { mutableStateOf(false) }

    val typeSuccess = remember {
        mutableStateOf<MutableList<ProfileSuccessType>>(mutableListOf())
    }
    val avatar = viewModel.avatar.collectAsState()
    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    imageSelected.value = null
                }
                true
            })

    val modalRequestDelete =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                false
            }, skipHalfExpanded = true
        )

    if (modalBottomSheetState.isVisible) {
        viewModel.loadAllImages()
    }
    var userTypeSelected by remember {
        mutableStateOf(UserFiledType.STORE_NAME)
    }
    val dismissPopupCheckMail = remember {
        mutableStateOf(false)
    }
    var phoneIdentity by remember {
        mutableStateOf("")
    }
    var emailIdentity by remember {
        mutableStateOf("")
    }
    var isPhoneIdentity by remember {
        mutableStateOf(false)
    }
    val storeID = viewModel.storeID.collectAsState()
    val userType = viewModel.userLocal.collectAsState()
    val storeName = viewModel.storeName.collectAsState()
    val isLogoutSuccess = viewModel.isLogoutSuccess.collectAsState()

    LaunchedEffect(isLogoutSuccess.value){
        snapshotFlow { isLogoutSuccess.value }.collectLatest { status ->
            if(status){
                val isDeepLink = false
                navController.navigate(ScreenName.SignInEmailScreen.route + "/$isDeepLink")
            }
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxHeight(),
        sheetShape = RoundedCornerShape(topStart = space_30, topEnd = space_30),
        sheetContent = {
            when (userTypeSelected) {
                UserFiledType.CAPTURE -> {
                    GalleryCompose(onSelectedImage = {
                        imageSelected.value = it
                    }, modalBottomSheetState, isOpenCaptureImage = {
                        isCaptureDisplay = true
                    }, onSubmit = { _, _ ->
                        isCaptureDisplay = false
                        typeSuccess.value.add(ProfileSuccessType.AVATAR)
                    },
                        getSelectedImage = { imageString ->
                            if (imageString != null) {
                                viewModel.updateAvatar(imageString)
                                viewModel.reCallProfileInfo()
                                imageTakePicture.value = null
                            }
                        }, typeUpload = UploadFileType.PROFILE
                    )
                }

                UserFiledType.SERVICE_NAME -> {
                    EditServiceNameInfoScreen(title = R.string.title_my_profile_name, "Your Name",
                        onSuccess = { isSuccess ->
                            viewModel.resetState()
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                            typeSuccess.value.add(ProfileSuccessType.SERVICE_NAME)
                        }, onFailed = {
                        })
                }

                UserFiledType.STORE_NAME -> {
                    val title = R.string.title_my_profile_store_name
                    EditServiceNameInfoScreen(title = title,
                        onSuccess = {
                            viewModel.resetState()
                            viewModel.reCallProfileInfo()
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                            typeSuccess.value.add(ProfileSuccessType.STORE_NAME)
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        }, onFailed = {
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        })
                }

                UserFiledType.YOUR_NAME -> {
                    val title = R.string.title_my_profile_your_name
                    EditUserNameScreen(title = title,
                        onSuccess = {
                            viewModel.resetState()
                            viewModel.reCallProfileInfo()
                            viewModel.reCallProfileInfo()
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                            typeSuccess.value.add(ProfileSuccessType.YOUR_NAME)
                        }, onFailed = {
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        })
                }

                UserFiledType.GENDER -> {
                    EditGenderScreen(title = R.string.title_my_profile_gender, "Gender",
                        onSuccess = { isSuccess ->
                            viewModel.resetState()
                            viewModel.reCallProfileInfo()
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                            typeSuccess.value.add(ProfileSuccessType.GENDER)
                        }, onFailed = { isFailed ->
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        })
                }

                UserFiledType.BIRTHDAY -> {
                    EditBirthdayProfileScreen(onSuccess = { status ->
                        viewModel.resetState()
                        viewModel.reCallProfileInfo()
                        scope.launch {
                            modalBottomSheetState.hide()
                        }
                        typeSuccess.value.add(ProfileSuccessType.BIRTHDAY)
                    }, onFailed = {
                        scope.launch {
                            modalBottomSheetState.hide()
                        }
                    })
                }

                UserFiledType.VERIFY_CODE -> {
                    if (isPhoneIdentity) {
                        VerifyCodeProfileScreen(
                            modalBottomSheetState,
                            identity = phoneIdentity,
                            isPhone = true,
                            onSuccess = {
                                viewModel.resetState()
                                viewModel.reCallProfileInfo()
                                typeSuccess.value.add(ProfileSuccessType.PHONE)
                            },
                            onFailed = {
                            })
                    } else {
                        VerifyCodeProfileScreen(
                            modalBottomSheetState,
                            identity = emailIdentity,
                            isPhone = false,
                            onSuccess = {
                                viewModel.resetState()
                                viewModel.reCallProfileInfo()
                                typeSuccess.value.add(ProfileSuccessType.EMAIL)
                            },
                            onFailed = {
                            })
                    }
                }

                UserFiledType.CHECK_MAIL -> {
                    CheckMailScreen(email = emailIdentity, onSuccess = {
                        isPhoneIdentity = false
                        userTypeSelected = UserFiledType.VERIFY_CODE
                    })
                }

                UserFiledType.PHONE -> {
                    EditPhoneScreen(
                        modalBottomSheetState, title = R.string.title_my_profile_phone, "Phone",
                        onSuccess = { status, phone ->
                            /*phoneIdentity = phone
                            userTypeSelected = UserFiledType.VERIFY_CODE
                            isPhoneIdentity = true
                            scope.launch {
                                modalBottomSheetState.show()
                            }*/
                            viewModel.resetState()
                            viewModel.reCallProfileInfo()
                            typeSuccess.value.add(ProfileSuccessType.PHONE)
                        },
                        onFailed = {
                        },
                    )
                }

                UserFiledType.EMAIL -> {
                    EditMailScreen(modalBottomSheetState, title = R.string.title_my_profile_email,
                        onSuccess = { status, emailCallBack ->
                            /*emailIdentity = emailCallBack
                            userTypeSelected = UserFiledType.CHECK_MAIL*/
                            viewModel.resetState()
                            viewModel.reCallProfileInfo()
                            typeSuccess.value.add(ProfileSuccessType.EMAIL)
                        }, onFailed = {
                            dismissPopupCheckMail.value = true
                        })
                }

                UserFiledType.ADDRESS -> {
                    EditAddressScreen(
                        modalBottomSheetState,
                        onSuccess = { status ->
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                            viewModel.resetState()
                            typeSuccess.value.add(ProfileSuccessType.ADDRESS)
                            viewModel.reCallAddressDetailInfo()
                            viewModel.reCallAddressDetailInfo()
                            viewModel.reCallAddressDetailInfo()
                        }, onFailed = {
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        })
                }

                UserFiledType.BIO -> {
                    EditBioScreen(modalBottomSheetState, onSuccess = {
                        viewModel.resetState()
                        viewModel.reCallProfileInfo()
                        viewModel.reCallProfileInfo()
                        typeSuccess.value.add(ProfileSuccessType.BIO)
                    }, onFailed = {
                    })
                }

                else -> {}
            }
        },
        sheetState = modalBottomSheetState
    ) {
        Scaffold(topBar = {
            ToolbarSchedule(
                title = stringResource(id = R.string.title_my_profile_dashboard),
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
                val (options, imageCompose, endCompose, accountDeletion) = createRefs()
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
                            .verticalScroll(rememberScrollState())
                    ) {
                        val profileModel = viewModel.profileModel.collectAsState()
                        val authMethod = viewModel.authMethod.collectAsState()
                        var hiltAddress = stringResource(R.string.hilt_seller_profile_address)
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(Color_gray_FF7),
                            verticalArrangement = Arrangement.Top
                        ) {
                            SpaceCompose(height = 32.dp)
                            when (userType.value.userTypeModel) {
                                UserTypeModel.BUYER -> {
                                    ItemInfoProfile(
                                        stringResource(R.string.title_my_profile_your_name),
                                        stringResource(R.string.title_my_profile_your_name),
                                        profileModel.value.name.orEmpty(), callbackFun = {
                                            userTypeSelected = UserFiledType.YOUR_NAME
                                            scope.launch {
                                                modalBottomSheetState.show()
                                            }
                                        },
                                        userFiledType = ProfileSuccessType.YOUR_NAME,
                                        typeSuccess = typeSuccess
                                    )
                                    hiltAddress =
                                        stringResource(R.string.hilt_my_profile_user_address)
                                }
                                else -> {
                                    ItemInfoProfile(
                                        stringResource(R.string.title_my_profile_store_name),
                                        stringResource(R.string.hilt_my_profile_store_name),
                                        storeName.value.orEmpty(), callbackFun = {
                                            userTypeSelected = UserFiledType.STORE_NAME
                                            scope.launch {
                                                modalBottomSheetState.show()
                                            }
                                        },
                                        userFiledType = ProfileSuccessType.STORE_NAME,
                                        typeSuccess = typeSuccess
                                    )
                                    ItemInfoProfile(
                                        stringResource(R.string.title_my_profile_store_id),
                                        stringResource(R.string.title_my_profile_store_id),
                                        stringResource(R.string.title_my_profile_store_id) + "-" + storeID.value.toString(),
                                        callbackFun = {},
                                        userFiledType = ProfileSuccessType.UNDEFINED,
                                    )
                                    hiltAddress =
                                        stringResource(R.string.hilt_seller_profile_address)
                                }
                            }
                            ItemInfoProfile(
                                stringResource(R.string.title_my_profile_bio),
                                stringResource(R.string.hilt_my_profile_bio),
                                if (profileModel.value.bio?.isNotEmpty() == true) {
                                    if (profileModel.value.bio.orEmpty().length > 20) {
                                        profileModel.value.bio.orEmpty().substring(0, 19).plus("...")
                                    } else {
                                        profileModel.value.bio.orEmpty()
                                    }
                                } else {
                                    profileModel.value.bio.orEmpty()
                                },
                                callbackFun = {
                                    userTypeSelected = UserFiledType.BIO
                                    scope.launch {
                                        modalBottomSheetState.show()
                                    }
                                },
                                userFiledType = ProfileSuccessType.BIO,
                                typeSuccess = typeSuccess
                            )
                            ItemInfoProfile(
                                stringResource(R.string.title_my_profile_birthday),
                                stringResource(R.string.hilt_my_profile_birthday),
                                if (profileModel.value.birthday?.isNotEmpty() == true) {
                                    val formatBirthday =
                                        profileModel.value.birthday.orEmpty().replace("/", "-")
                                    formatBirthday
                                } else {
                                    profileModel.value.birthday.orEmpty()
                                },
                                callbackFun = {
                                    userTypeSelected = UserFiledType.BIRTHDAY
                                    scope.launch {
                                        modalBottomSheetState.show()
                                    }
                                },
                                userFiledType = ProfileSuccessType.BIRTHDAY,
                                typeSuccess = typeSuccess
                            )
                            if (authMethod.value == AuthMethod.USER_PASS.name) {
                                ItemInfoProfile(
                                    stringResource(R.string.title_my_profile_phone),
                                    stringResource(R.string.hilt_my_profile_phone),
                                    if (profileModel.value.phoneNumber?.isNotEmpty() == true) {
                                        val phoneOrigin = profileModel.value.phoneNumber
                                        val conditionPhone = phoneOrigin?.take(3)
                                        val firstPhone = "(" + conditionPhone?.plus(")")
                                        val lastPhone = phoneOrigin?.takeLast(2)
                                        val finalPhone = firstPhone.plus("*****").plus(lastPhone)
                                        finalPhone
                                    } else {
                                        profileModel.value.phoneNumber.orEmpty()
                                    },
                                    callbackFun = {
                                        userTypeSelected = UserFiledType.PHONE
                                        scope.launch {
                                            modalBottomSheetState.show()
                                        }
                                    },
                                    userFiledType = ProfileSuccessType.PHONE,
                                    typeSuccess = typeSuccess
                                )

                                ItemInfoProfile(
                                    stringResource(R.string.title_my_profile_email),
                                    stringResource(R.string.hilt_my_profile_email),
                                    if (profileModel.value.email?.isNotEmpty() == true) {
                                        val emailOrigin = profileModel.value.email.orEmpty()
                                        val firstEmail = emailOrigin.take(2)
                                        val lastEmail = emailOrigin.substringAfter("@")
                                        val finalEmail = firstEmail.plus("*****").plus(lastEmail)
                                        finalEmail
                                    } else {
                                        profileModel.value.email.orEmpty()
                                    },
                                    callbackFun = {
                                        userTypeSelected = UserFiledType.EMAIL
                                        scope.launch {
                                            modalBottomSheetState.show()
                                        }
                                    },
                                    userFiledType = ProfileSuccessType.EMAIL,
                                    typeSuccess = typeSuccess
                                )
                            }
                            ItemInfoProfile(
                                stringResource(R.string.title_my_profile_address),
                                hiltAddress,
                                viewModel.getAddressDetail(),
                                callbackFun = {
                                    userTypeSelected = UserFiledType.ADDRESS
                                    scope.launch {
                                        modalBottomSheetState.show()
                                    }
                                },
                                userFiledType = ProfileSuccessType.ADDRESS,
                                typeSuccess = typeSuccess
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .constrainAs(endCompose) {
                            start.linkTo(imageCompose.end)
                            end.linkTo(imageCompose.end)
                            bottom.linkTo(imageCompose.bottom, (-4).dp) //-4
                        }
                        .background(
                            color = Transparent,
                            shape = CircleShape
                        )
                        .offset((-28).dp, 0.dp)
                        .size(46.dp)) {
                    Column(
                        modifier = Modifier
                            .clip(
                                shape = CircleShape
                            )
                            .background(Color_LiveStream_Main_Color)
                            .fillMaxSize()
                    ) {
                    }
                }
                AvatarInfoProfile(modifier = Modifier.constrainAs(imageCompose) {
                    top.linkTo(parent.top, 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, url = avatar.value, callbackFun = {
                    userTypeSelected = UserFiledType.CAPTURE
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                }, uriTakePicture = imageTakePicture.value)

                Row(
                    modifier = Modifier
                        .constrainAs(accountDeletion) {
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                        .padding(horizontal = marginDouble, vertical = 24.dp)
                        .noRippleClickable {
                            scope.launch {
                                modalRequestDelete.show()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.lb_account_deletion),
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color_Black_019,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_account_deletion),
                        contentDescription = "account deletion",
                        tint = Color_LiveStream_Main_Color
                    )
                }
            }
        }
    }
    LoaderWithAnimation(isPlaying = isLoading)
    CaptureScreen(isDisplay = isCaptureDisplay, callbackUri = { uriSelected ->
        uriSelected?.let {
            viewModel.handleUploadCapture(uriSelected)
            imageTakePicture.value = uriSelected
        }
        isCaptureDisplay = false
    })
    RequestToDeleteAccountBottomSheet(modalBottomSheetState = modalRequestDelete,viewModel= viewModel)
}

@Composable
fun ImageProfileItem(modifier: Modifier) {
    Box(
        modifier = modifier
            .size(122.dp)
            .background(
                Color_LiveStream_Main_Color,
                shape = CircleShape
            )
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            shape = CircleShape,
            elevation = 2.dp,
            backgroundColor = Color_LiveStream_Main_Color
        ) {
            AsyncImage(
                model = R.drawable.olmo_ic_ellipse_profile,
                contentDescription = null,
                modifier = Modifier
                    .size(85.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }
    }
}