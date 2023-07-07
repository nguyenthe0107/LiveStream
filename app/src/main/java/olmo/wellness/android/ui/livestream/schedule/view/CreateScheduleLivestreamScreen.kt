package olmo.wellness.android.ui.livestream.schedule.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.enums.ConfirmType
import olmo.wellness.android.core.enums.UploadFileType
import olmo.wellness.android.core.hideSystemUI
import olmo.wellness.android.data.model.schedule.FillDataSchedule
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.extension.hideKeyboard
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.booking.list_service.view.ListOfServiceBottomSheet
import olmo.wellness.android.ui.livestream.schedule.viewmodel.CreateScheduleLivestreamViewModel
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.livestream.schedule.dialog.AddDescriptionScheduleBottomSheet
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.booking_service.add_booking.AddBookingServiceBottomSheet
import olmo.wellness.android.ui.screen.capture_screen.CaptureScreen
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.broadcast_audience.AudienceType
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category.SelectCategoryBottomSheet
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category.components.HorizontalGridCategories
import olmo.wellness.android.ui.screen.profile_dashboard.gallery_compose.GalleryCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

val roundRadius = 32.dp

@SuppressLint("MutableCollectionMutableState", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class,
    ExperimentalCoilApi::class, ExperimentalCoroutinesApi::class
)
@Composable
fun CreateScheduleLivestreamScreen(
    navController: NavController?,
    viewModel: CreateScheduleLivestreamViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    (context as MainActivity).hideSystemUI()

    val scope = rememberCoroutineScope()

    val fillDataScheduler = viewModel.fillDataSchedule.collectAsState()

    val enableButton = fillDataScheduler.value.title?.isNotEmpty() == true
//                && fillDataScheduler.value.description?.isNotEmpty() == true && fillDataScheduler.value.dateCreate != null && fillDataScheduler.value.isPrivate != null && fillDataScheduler.value.isEvent != null

    val closeDialogBroadcast = remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    var isCaptureDisplay by remember { mutableStateOf(false) }

    val imageSelected = viewModel.uriImageSelect.collectAsState()

    val radioEvent = listOf(ConfirmType.Yes, ConfirmType.No)
    val (selectedEventOption, onOptionEventSelected) = remember { mutableStateOf<ConfirmType?>(null) }

    if (fillDataScheduler.value.isEvent!=null){
        onOptionEventSelected.invoke( (if (fillDataScheduler.value.isEvent==true) ConfirmType.Yes else ConfirmType.No))
    }

    val radioAudience = listOf(AudienceType.EVERYONE, AudienceType.FOLLOWERS)

    val (selectedAudienceOption, onOptionAudienceSelected) = remember { mutableStateOf<AudienceType?>(null) }
    if (fillDataScheduler.value.isPrivate!=null){
        onOptionAudienceSelected.invoke( if (fillDataScheduler.value.isPrivate==true) AudienceType.FOLLOWERS else AudienceType.EVERYONE )
    }

    val modalDescriptionBottomSheetSate =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )

    val modalAddBookingServiceBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalListOfServiceBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val categoryListSelected = remember {
        mutableStateOf<MutableList<LiveCategory>>(mutableListOf())
    }

    /* Booking Service */
    val servicesBookingSelected = viewModel.bookingServiceSelected.collectAsState()
    val modalImageBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    viewModel.setUri(null)
                }
                true
            })

    val modalSelectCategoryBottomSheetSate =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                false
            })


    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                context.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
                context.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
            Lifecycle.Event.ON_DESTROY -> {
            }
            else -> {

            }
        }
    }
    val imageSelectedGallery = remember { mutableStateOf<Uri?>(null) }
    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxHeight(),
        sheetShape = RoundedCornerShape(topStart = marginStandard, topEnd = marginStandard),
        sheetContent = {
            GalleryCompose(onSelectedImage = {
                if(it!=null){
                    imageSelectedGallery.value = it
                }
            }, modalImageBottomSheetState, isOpenCaptureImage = {
                isCaptureDisplay = true
            }, onSubmit = { _, _ ->

            },
                getSelectedImage = { imageString ->
                    if (imageString != null) {
                        viewModel.updateSchedule(
                            fillDataScheduler.value.copy(
                                thumbnailUrl = imageString
                            )
                        )
                    }
                }, typeUpload = UploadFileType.OTHER
            )
            LaunchedEffect(imageSelectedGallery.value){
                snapshotFlow { imageSelectedGallery.value }.collectLatest {
                    if(it != null){
                        viewModel.setUri(it)
                    }
                }
            }
        },
        sheetState = modalImageBottomSheetState
    ) {
        Scaffold(
            topBar = {
                navController?.let {
                    ToolbarSchedule(
                        title = stringResource(id = R.string.lb_live_scheduling),
                        backIconDrawable = R.drawable.ic_back_calendar,
                        navController = it,
                        backgroundColor = Transparent
                    )
                }
            }, modifier = Modifier.fillMaxSize(),
            backgroundColor = Color_LiveStream_Main_Color

        ) {
            LazyColumn(modifier = Modifier) {
                item {
                    InfoUI(
                        context,
                        navController,
                        imageSelected,
                        scope,
                        modalImageBottomSheetState,
                        modalDescriptionBottomSheetSate,
                        fillDataScheduler.value,
                        focusManager,
                        radioEvent,
                        selectedEventOption,
                        onOptionEventSelected,
                        radioAudience,
                        selectedAudienceOption,
                        onOptionAudienceSelected,
                        eventChange = {
                            viewModel.updateSchedule(
                                fillDataScheduler.value.copy(
                                    isEvent = (it == ConfirmType.Yes)
                                )
                            )
                        },
                        audienceChange = {
                            viewModel.updateSchedule(
                                fillDataScheduler.value.copy(
                                    isPrivate = (it == AudienceType.FOLLOWERS)
                                )
                            )
                        },
                        onConfirm = {
                            viewModel.handleRequestLiveStream(context, navController)
                        },
                        onDismiss = {
                            navController?.popBackStack()
                        },
                        viewModel = viewModel,
                        dateTime = fillDataScheduler.value.dateCreate,
                        modalSelectCategoryBottomSheetSate = modalSelectCategoryBottomSheetSate,
                        enable = enableButton,
                        openAddService = { status ->
                            if(status){
                                scope.launch {
                                    modalAddBookingServiceBottomSheetState.show()
                                }
                            }
                        },
                        listServices = servicesBookingSelected.value
                    )
                }
            }
        }
    }

    CaptureScreen(isDisplay = isCaptureDisplay, callbackUri = {
        isCaptureDisplay = false
        it?.let {
            viewModel.setUri(it)
//            scope.launch {
//                uploadViewModel.uploadFile(it,UploadFileType.OTHER)
//            }
        }
    })

    AddDescriptionScheduleBottomSheet(modalDescriptionBottomSheetSate, fillDataScheduler.value) {
        viewModel.updateSchedule(
            fillDataScheduler.value.copy(
                description = it
            )
        )
    }

    /* Booking Service */
    AddBookingServiceBottomSheet(
        isLiveStream = false,
        servicesSelected = servicesBookingSelected.value,
        modalBottomSheetState = modalAddBookingServiceBottomSheetState, confirmCallback = { listSelected ->
            listSelected?.let { viewModel.bindSelectedServiceBooking(it) }
            scope.launch {
                modalAddBookingServiceBottomSheetState.hide()
            }
        }, cancelCallback = { status ->
            if(status){
                scope.launch {
                    modalAddBookingServiceBottomSheetState.hide()
                }
            }
        }, openListOfService = { status ->
            if(status){
                scope.launch {
                    modalListOfServiceBottomSheetState.show()
                }
            }
        }, onSelectedItemToShowCallback = {
        }, notifyChangeListService = { newList ->
            if(newList?.isNotEmpty() == true){
                viewModel.bindSelectedServiceBooking(newList)
            }else{
                viewModel.bindSelectedServiceBooking(emptyList())
            }
        })

    ListOfServiceBottomSheet(modalBottomSheetState = modalListOfServiceBottomSheetState, onDone = {
        if(it?.isNotEmpty() == true){
            viewModel.bindSelectedServiceBooking(it)
            scope.launch {
                modalListOfServiceBottomSheetState.hide()
            }
        }
    })

    SelectCategoryBottomSheet(
        modalBottomSheetState = modalSelectCategoryBottomSheetSate,
        onCategorySelected = { categorySelected ->
            if (categorySelected != null && categorySelected.isNotEmpty()) {
                categoryListSelected.value.addAll(categorySelected)
                viewModel.updateSchedule(
                    fillDataScheduler.value.copy(listCategory = categoryListSelected.value.distinctBy { it.id })
                )
            }
            scope.launch {
                modalSelectCategoryBottomSheetSate.hide()
            }
        },
        onCancelBottomSheet = {
            scope.launch {
                modalSelectCategoryBottomSheetSate.hide()
            }
        },
    )
    navController?.currentBackStackEntry
        ?.savedStateHandle?.getLiveData<Long>(Constants.BUNDLE_DATA)
        ?.observe(LocalLifecycleOwner.current) {
            viewModel.setDateTime(it)

            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<Long>(Constants.BUNDLE_DATA)
        }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun InfoUI(
    context: Activity,
    navController: NavController?,
    imageSelected: State<Uri?>,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    modalDescriptionBottomSheetSate: ModalBottomSheetState,
    fillDataScheduler: FillDataSchedule,
    focusManager: FocusManager,
    radioEvent: List<ConfirmType>,
    selectedOption: ConfirmType?,
    onOptionSelected: (ConfirmType?) -> Unit,
    radioAudience: List<AudienceType>,
    selectedAudienceOption: AudienceType?,
    onOptionAudienceSelected: (AudienceType?) -> Unit,
    eventChange: (ConfirmType?) -> Unit,
    audienceChange: (AudienceType?) -> Unit,
    modalSelectCategoryBottomSheetSate : ModalBottomSheetState,
    viewModel: CreateScheduleLivestreamViewModel,
    dateTime: Long?,
    enable: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    openAddService: ((Boolean) -> Unit)?=null,
    listServices: List<ServiceBooking> ?= null
) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxHeight()
            .clip(
                RoundedCornerShape(
                    topStart = roundRadius,
                    topEnd = roundRadius
                )
            )
            .background(color = White)
            .padding(15.dp)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (box) = createRefs()
            val guidelineBottom = createGuidelineFromBottom(0f)
            Box(modifier = Modifier
                .clip(RoundedCornerShape(roundRadius))
                .constrainAs(box) {
                    linkTo(start = parent.start, end = parent.end)
                    linkTo(parent.top, guidelineBottom)

                    width = Dimension.ratio("3:2")
                    height = Dimension.fillToConstraints
                }) {

                Image(
                    painter = (if (imageSelected.value == null) {
                        painterResource(id = R.drawable.olmo_ic_group_default_place_holder)
                    } else {
                        rememberImagePainter(data = imageSelected.value)
                    }),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(roundRadius))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(roundRadius)
                        .height(roundRadius)
                        .clip(
                            RoundedCornerShape(
                                bottomEnd = roundRadius,
                                bottomStart = roundRadius
                            )
                        )
                        .background(color = Color_gray_4D0)
                        .align(Alignment.BottomCenter)
                        .clickable {
                            scope.launch {
                                modalBottomSheetState.show()
                            }
                        },
                ) {
                    Text(
                        text = stringResource(R.string.lb_change_photo),
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = White, fontSize = 12.sp
                        ),
                    )
                }

            }
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        InputTitle(fillDataScheduler.title, focusManager) {
            viewModel.updateSchedule(
                fillDataScheduler.copy(
                    title = it
                )
            )
        }

        InputServices(description = stringResource(id = R.string.title_add_booking_services), openServices = {
            openAddService?.invoke(true)
        }, listServices = listServices)

        InputDescription(
            description = fillDataScheduler.description,
            openDescription = {
                hideKeyboard(context)
                scope.launch {
                    modalDescriptionBottomSheetSate.show()
                }
            })

        InputCategories(fillDataScheduler.listCategory) {
            scope.launch {
                modalSelectCategoryBottomSheetSate.show()
            }
        }

        InputTime(dateTime) {
           val temp = FillDataSchedule().apply {
               title = fillDataScheduler.title
               description = fillDataScheduler.description
           }
            navController?.navigate(
                ScreenName.CalendarScreen.route.plus(
                    "?fillData=${
                        Gson().toJson(
                            temp
                        )
                    }"
                )
            )
        }

        EventCommunityUI(radioEvent, selectedOption, onOptionSelected, eventChange)

        AudienceUI(radioAudience, selectedAudienceOption, onOptionAudienceSelected, audienceChange)

        ActionBottomUI(
            context = context,
            onDismiss = onDismiss,
            enableConfirm = enable,
            onConfirm = onConfirm
        )
    }
}


@Composable
private fun InputTime(dateTime: Long?, openTime: () -> Unit) {
    Column(modifier = Modifier.padding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable {
                    openTime.invoke()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = stringResource(R.string.lb_set_time),
                style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.padding(start = 30.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically

            ) {
                if (dateTime != null) {
                    Text(text = DateTimeHelper.convertToStringSchedule(dateTime),
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp),
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable {
                                openTime.invoke()
                            }
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null,
                    tint = Black_037
                )
            }
        }
    }
}


@Composable
private fun InputCategories(
    listCategory: List<LiveCategory>? = null,
    openCategories: () -> Unit
) {
    Column(modifier = Modifier.padding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable {
                    openCategories.invoke()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            Text(
                text = stringResource(R.string.lb_categories),
                style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
                tint = Black_037
            )
        }
        listCategory?.size?.let { it ->
            HorizontalGridCategories(
                data = listCategory,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
//                    .padding(start = 16.dp, end = 16.dp)
                ,
                disableClick = true
            )
        }
    }
}

@Composable
private fun InputTitle(
    title: String?, focusManager: FocusManager,
    titleChange: (String?) -> Unit
) {
    Text(
        text = stringResource(id = R.string.lb_title),
        style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp, color = Black_037),
        modifier = Modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
    )
    TextField(
        value = title ?: "",
        onValueChange = {
            titleChange.invoke(it)
        },
        placeholder = {
            Text(
                text = stringResource(R.string.lb_entet_title),
                style = MaterialTheme.typography.subtitle1.copy(
                    color = Neutral_Gray_5, fontSize = 14.sp
                ),
            )
        },
        modifier = Modifier
            .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {
                focusManager.clearFocus()
            })
            .clip(RoundedCornerShape(50.dp))
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color_gray_3F9,
            textColor = Color(0xFF303037),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        textStyle = MaterialTheme.typography.caption.copy(
            fontSize = 14.sp
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )

    Text(
        text = stringResource(R.string.lb_50_characters),
        style = MaterialTheme.typography.subtitle1.copy(
            fontSize = 10.sp, color = Neutral_Gray_7
        ),
        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )
}

@Composable
private fun InputServices(openServices: (() -> Unit)?=null, description: String?, listServices: List<ServiceBooking>?=null) {
    Column(modifier = Modifier.padding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable {
                    openServices?.invoke()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = description.orEmpty(),
                style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                modifier = Modifier
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = (if(listServices?.isNotEmpty() == true){
                        (listServices.size ?: 0).toString() + (" Services")
                    }else "").toString(),
                    style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                    modifier = Modifier
                )
                SpaceHorizontalCompose(width = 10.dp)
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "next",
                    tint = Black_037
                )
            }
        }
    }
}

@Composable
private fun InputDescription(openDescription: () -> Unit, description: String?) {
    Column(modifier = Modifier.padding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable {
                    openDescription.invoke()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.lb_description),
                style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
            if (description?.isBlank() == true) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null,
                    tint = Black_037
                )
            }
        }
        if (description?.isBlank() == false) {
            Text(text = description,
                style = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        openDescription.invoke()
                    }
                    .padding(bottom = 10.dp)
            )
        }
    }
}


@Composable
private fun AudienceUI(
    radioAudience: List<AudienceType>,
    selectedAudienceOption: AudienceType?,
    onOptionAudienceSelected: (AudienceType?) -> Unit,
    audienceChange: (AudienceType?) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = White)
//                .padding(vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.lb_who_can_see_your_livestream),
                style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                modifier = Modifier.padding(top = 10.dp)
            )

            Row(
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                radioAudience.forEach { type ->
                    Row(modifier = Modifier
                        .selectable(
                            selected = (type == selectedAudienceOption), onClick = {
                                onOptionAudienceSelected(type)
                                audienceChange.invoke(type)
                            }
                        )
                        .fillMaxWidth()
                        .weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (type == selectedAudienceOption),
                            modifier = Modifier.size(20.dp),
                            colors = RadioButtonDefaults.colors(
                                Color_gray_6CF,
                                Neutral_Bare_Gray
                            ),
                            onClick = {
                                onOptionAudienceSelected(type)
                                audienceChange.invoke(type)
                            })
                        Text(
                            text = type.value,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 14.sp, color = Neutral_Gray_9
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun EventCommunityUI(
    radioEvent: List<ConfirmType>,
    selectedOption: ConfirmType?,
    onOptionSelected: (ConfirmType?) -> Unit,
    eventChange: (ConfirmType?) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = White)
        ) {
            Text(
                text = stringResource(R.string.lb_is_this_an_event_for_yout_community),
                style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = stringResource(R.string.lb_your_livestream_will_be_displayed_on_kepler_events),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 10.sp, color = Neutral_Gray_7
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                radioEvent.forEach { type ->
                    Row(modifier = Modifier
                        .selectable(
                            selected = (type == selectedOption), onClick = {
                                onOptionSelected(type)
                                eventChange.invoke(type)
                            }
                        )
                        .fillMaxWidth()
                        .weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (type == selectedOption),
                            modifier = Modifier.size(20.dp),
                            colors = RadioButtonDefaults.colors(
                                Color_gray_6CF,
                                Neutral_Bare_Gray
                            ),
                            onClick = {
                                onOptionSelected(type)
                                eventChange.invoke(type)
                            })
                        Text(
                            text = stringResource(type.name),
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 14.sp, color = Neutral_Gray_9
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionBottomUI(
    context: Context,
    onDismiss: () -> Unit,
    enableConfirm: Boolean,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .background(White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SecondLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
                    .noRippleClickable {
                        onDismiss.invoke()
                    },
                stringResource(R.string.cancel),
                onClickFunc = {
                    onDismiss.invoke()
                }
            )
            PrimaryLiveButton(
                enable = enableConfirm,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                text = stringResource(R.string.lb_confirm),
                onClickFunc = {
                    onConfirm.invoke()
                }
            )
        }
    }
}