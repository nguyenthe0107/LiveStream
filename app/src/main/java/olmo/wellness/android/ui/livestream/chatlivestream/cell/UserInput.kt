/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package olmo.wellness.android.ui.livestream.chatlivestream.cell

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.InsertPhoto
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.BackPressHandler
import olmo.wellness.android.ui.common.permission.RequireExternalStoragePermission
import olmo.wellness.android.ui.screen.profile_dashboard.gallery_compose.GalleryViewModel
import olmo.wellness.android.ui.theme.*

enum class InputSelector {
    NONE,
    MAP,
    DM,
    EMOJI,
    PHONE,
    PICTURE
}

enum class EmojiStickerSelector {
    EMOJI,
    STICKER
}

@Preview
@Composable
fun UserInputPreview() {
    UserInput(onMessageSent = {})
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserInput(
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPrivateChat: Boolean = false,
    resetScroll: () -> Unit = {},
    viewModel: GalleryViewModel = hiltViewModel(),
    textMessageValueState: MutableState<TextFieldValue>? = null,
    roomId: String? = null,
    imgTakePhoto: MutableState<Uri?>? = null,
    onImageSent: ((List<String>) -> Unit)? = null,
    onClearFocusOnKeyboardDismiss: (() -> Unit)? = null,
    isOpenCaptureImage: ((isOpen: Boolean) -> Unit)? = null,
    isAutoTextFocus: Boolean = false,
) {
    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    val dismissKeyboard = { currentInputSelector = InputSelector.NONE }

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    // Intercept back navigation if there's a InputSelector visible
    if (currentInputSelector != InputSelector.NONE) {
        BackPressHandler(onBackPressed = dismissKeyboard)
    }

    var textState by remember { mutableStateOf(TextFieldValue()) }
    if (textMessageValueState != null) {
        textState = textMessageValueState.value
    }

    val imageSelects = remember {
        mutableStateOf<MutableList<ImageSelect>>(mutableListOf())
    }

    if (imgTakePhoto?.value != null) {
        viewModel.uploadMultiImage(uris = mutableListOf<Uri>().apply {
            add(imgTakePhoto.value!!)
        },
            roomId = roomId,
            _onSuccess = {
                onImageSent?.invoke(it)
                imageSelects.value = mutableListOf()
            })
        imgTakePhoto.value=null
        currentInputSelector =InputSelector.NONE

    }

    // Used to decide if the keyboard should be shown
    var textFieldFocusState by remember { mutableStateOf(false) }

    Surface(tonalElevation = 2.dp, color = Neutral_Gray_2) {
        Column() {
            Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
                UserInputSelector(
                    onSelectorChange = { currentInputSelector = it },
                    currentInputSelector = currentInputSelector,
                    isPrivateChat = isPrivateChat
                )
                Box(modifier = Modifier.weight(1f)) {
                    UserInputText(
                        textFieldValue = textState,
                        onTextChanged = { textState = it },
                        // Only show the keyboard if there's no input selector and text field has focus
                        keyboardShown = currentInputSelector == InputSelector.NONE && textFieldFocusState,
                        // Close extended selector if text field receives focus
                        onTextFieldFocused = { focused ->
                            if (focused) {
                                currentInputSelector = InputSelector.NONE
                                resetScroll()
                            }
                            textFieldFocusState = focused
                        },
                        focusState = textFieldFocusState,
                        onClearFocusOnKeyboardDismiss = onClearFocusOnKeyboardDismiss,
                        sendMessageEnabled = textState.text.isNotBlank() || imageSelects.value.isNotEmpty(),
                        onMessageSent = {
                            resetScroll()
//                            dismissKeyboard()
                            focusManager.clearFocus()
                            when (currentInputSelector) {
                                InputSelector.PICTURE -> {
                                    viewModel.uploadMultiImage(uris = imageSelects.value.map { it.uri },
                                        roomId = roomId,
                                        _onSuccess = {
                                            onImageSent?.invoke(it)
                                            imageSelects.value = mutableListOf()
                                        })
                                }
                                else -> {
                                    onMessageSent(textState.text)
                                    // Reset text field and close keyboard
                                    textState = textState.copy(
                                        text = "",
                                        selection = TextRange.Zero
                                    )
                                }
                            }
                            dismissKeyboard()
//                            onMessageSent(textState.text)
//                            // Reset text field and close keyboard
//                            textState = textState.copy(
//                                text = "",
//                                selection = TextRange.Zero
//                            )
                            // Move scroll to bottom

                        },
                        isAutoTextFocus = isAutoTextFocus
                    )
                }
            }
            SelectorExpanded(
                onCloseRequested = dismissKeyboard,
                imageSelects = imageSelects,
                onTextAdded = { textState = textState.addText(it) },
                isOpenCaptureImage = isOpenCaptureImage,
                currentSelector = currentInputSelector,
                viewModel = viewModel
            )
        }
    }

}

private fun TextFieldValue.addText(newString: String): TextFieldValue {
    val newText = this.text.replaceRange(
        this.selection.start,
        this.selection.end,
        newString
    )
    val newSelection = TextRange(
        start = newText.length,
        end = newText.length
    )

    return this.copy(text = newText, selection = newSelection)
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SelectorExpanded(
    currentSelector: InputSelector,
    imageSelects: MutableState<MutableList<ImageSelect>>,
    viewModel: GalleryViewModel,
    isOpenCaptureImage: ((isOpen: Boolean) -> Unit)? = null,
    onCloseRequested: () -> Unit,
    onTextAdded: (String) -> Unit,
) {
    if (currentSelector == InputSelector.NONE) return

    val scope = rememberCoroutineScope()
    // Request focus to force the TextField to lose it
    val focusRequester = FocusRequester()
    // If the selector is shown, always request focus to trigger a TextField.onFocusChange.
    SideEffect {
        if (currentSelector == InputSelector.EMOJI) {
            focusRequester.requestFocus()
        } else if (currentSelector == InputSelector.PICTURE) {
            focusRequester.requestFocus()
        }
    }

    Surface(tonalElevation = 8.dp) {
        when (currentSelector) {
            InputSelector.EMOJI -> EmojiSelector(onTextAdded, focusRequester)
            InputSelector.DM -> NotAvailablePopup(onCloseRequested)
            InputSelector.PICTURE -> PhotoSelector(focusRequester = focusRequester,
                imageSelects = imageSelects,
                viewModel = viewModel,
                isOpenCaptureImage = isOpenCaptureImage)
            InputSelector.MAP -> FunctionalityNotAvailablePanel()
            InputSelector.PHONE -> FunctionalityNotAvailablePanel()
            else -> {
                throw NotImplementedError()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FunctionalityNotAvailablePanel() {
    AnimatedVisibility(
        visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
        enter = expandHorizontally() + fadeIn(),
        exit = shrinkHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .height(320.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.not_available),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(id = R.string.not_available_subtitle),
                modifier = Modifier.paddingFrom(FirstBaseline, before = 32.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun UserInputSelector(
    onSelectorChange: (InputSelector) -> Unit,
    isPrivateChat: Boolean,
    currentInputSelector: InputSelector,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(64.dp)
//            .padding(horizontal = 8.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        /*InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.EMOJI) },
            icon = Icons.Outlined.Mood,
            selected = currentInputSelector == InputSelector.EMOJI,
            description = stringResource(id = R.string.emoji_selector_bt_desc)
        )*/
//        InputSelectorButton(
//            onClick = { onSelectorChange(InputSelector.DM) },
//            icon = Icons.Outlined.AlternateEmail,
//            selected = currentInputSelector == InputSelector.DM,
//            description = stringResource(id = R.string.dm_desc)
//        )
        if (isPrivateChat) {
            InputSelectorButton(
                onClick = { onSelectorChange(InputSelector.PICTURE) },
                icon = Icons.Outlined.InsertPhoto,
                selected = currentInputSelector == InputSelector.PICTURE,
                description = stringResource(id = R.string.attach_photo_desc)
            )
        }
//        InputSelectorButton(
//            onClick = { onSelectorChange(InputSelector.MAP) },
//            icon = Icons.Outlined.Place,
//            selected = currentInputSelector == InputSelector.MAP,
//            description = stringResource(id = R.string.map_selector_desc)
//        )
//        InputSelectorButton(
//            onClick = { onSelectorChange(InputSelector.PHONE) },
//            icon = Icons.Outlined.Duo,
//            selected = currentInputSelector == InputSelector.PHONE,
//            description = stringResource(id = R.string.videochat_desc)
//        )
    }
}

@Composable
private fun InputSelectorButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    selected: Boolean,
) {
    val backgroundModifier = if (selected) {
        Modifier.background(
            color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(14.dp)
        )
    } else {
        Modifier
    }
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(56.dp)
            .then(backgroundModifier)
    ) {
        val tint = if (selected) {
            MaterialTheme.colorScheme.onSecondary
        } else {
            MaterialTheme.colorScheme.secondary
        }
        Icon(
            icon,
            tint = tint,
            modifier = Modifier.padding(12.dp),
            contentDescription = description
        )
    }
}

@Composable
private fun NotAvailablePopup(onDismissed: () -> Unit) {
    FunctionalityNotAvailablePopup(onDismissed)
}

val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    keyboardShown: Boolean,
    sendMessageEnabled: Boolean,
    onMessageSent: () -> Unit,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean,
    isAutoTextFocus: Boolean,
    onClearFocusOnKeyboardDismiss: (() -> Unit)? = null,

    ) {
    val a11ylabel = stringResource(id = R.string.text_field_desc)
//
    Row(
        modifier = Modifier
            .background(Transparent)
            .height(64.dp)
            .fillMaxWidth()
            .padding(end = 8.dp)
            .semantics {
                contentDescription = a11ylabel
                keyboardShownProperty = keyboardShown
            },
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var lastFocusState by remember { mutableStateOf(false) }
        val focusRequester = FocusRequester()
        val scope = rememberCoroutineScope()

        TextField(
            value = textFieldValue,
            singleLine = true,
            onValueChange = {
                onTextChanged(it)
            },
            placeholder = {
                androidx.compose.material.Text(
                    text = stringResource(id = R.string.text_field_hint),
                    style = androidx.compose.material.MaterialTheme.typography.subtitle1.copy(
                        color = Neutral_Gray_5, fontSize = 14.sp
                    ),
                )
            },
            modifier = Modifier
                .padding()
                .fillMaxHeight()
                .weight(1f)
                .clearFocusOnKeyboardDismiss {
                    onClearFocusOnKeyboardDismiss?.invoke()
                }
                .onFocusChanged { state ->
                    if (lastFocusState != state.isFocused) {
                        onTextFieldFocused(state.isFocused)
                    }
                    lastFocusState = state.isFocused
                }
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Neutral_Gray_2,
                textColor = Neutral_Gray_5,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = Neutral_Gray_5
            ),
            textStyle = androidx.compose.material.MaterialTheme.typography.subtitle1.copy(
                fontSize = 14.sp
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onMessageSent.invoke()
                }
            )
        )

        IconButton(onClick = onMessageSent, enabled = sendMessageEnabled) {
            Icon(
                painter = painterResource(id = R.drawable.ic_send_message),
                contentDescription = "Send message", tint =
                if (sendMessageEnabled) {
                    Color_Green_Main
                } else {
                    Neutral_Gray_5
                }
            )
        }
        LaunchedEffect(Unit) {
            if (isAutoTextFocus) {
                scope.launch {
                    focusRequester.requestFocus()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun PhotoSelector(
    focusRequester: FocusRequester,
    viewModel: GalleryViewModel,
    imageSelects: MutableState<MutableList<ImageSelect>>,
    isOpenCaptureImage: ((isOpen: Boolean) -> Unit)? = null,
) {
    val permissionState = rememberPermissionState(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    viewModel.loadAllImages()
    val allImagesFromGallery = viewModel.allImagesFromGallery.collectAsState()

    val a11yLabel = stringResource(id = R.string.picture_selector_desc)

    val configuration = LocalConfiguration.current
    val screenWidth = (configuration.screenWidthDp.dp - padding_12 - padding_12) / 3


    AnimatedVisibility(
        visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
        enter = expandHorizontally() + fadeIn(),
        exit = shrinkHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .height(320.dp)
                .fillMaxWidth()
                .background(Neutral_Gray_2),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(5.dp))

            Card(modifier = Modifier
                .focusRequester(focusRequester)
                .focusTarget()
                .semantics { contentDescription = a11yLabel }, elevation = 4.dp,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (permissionState.hasPermission) {
                        viewModel.loadAllImages()
                        Text(text = stringResource(R.string.lb_maximim_10_images),
                            style = androidx.compose.material.MaterialTheme.typography.subtitle1.copy(
                                fontSize = 10.sp, color = Black_466, lineHeight = 18.sp
                            ),
                            modifier = Modifier.padding(12.dp))

                    } else {
                        RequireExternalStoragePermission(navigateToSettingsScreen = { /*TODO*/ }) {}
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.padding(horizontal = padding_12),
                    ) {
                        item {
                            Column(
                                modifier = Modifier
                                    .size(screenWidth)
                                    .noRippleClickable {
                                        isOpenCaptureImage?.invoke(true)
//                                isCaptureDisplay = true
//                                scope.launch {
//                                    modalBottomSheetState.hide()
//                                    isLoading = false
//                                }
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_camera),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(sizeIcon_28)
                                )
                                androidx.compose.material.Text(
                                    text = stringResource(R.string.take_a_photo),
                                    style = androidx.compose.material.MaterialTheme.typography.body2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Neutral_Gray_9,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = marginStandard, horizontal = marginStandard
                                        )
                                )
                            }
                        }
                        items(allImagesFromGallery.value.size) { index ->
                            val uri = allImagesFromGallery.value.get(index)
                            Box(
                                Modifier
                                    .noRippleClickable {
                                        val image = imageSelects.value.find { it.index == index }
                                        if (image != null) {
                                            imageSelects.value = imageSelects.value.map {
                                                it.apply {
                                                    if (it.position > image.position) {
                                                        it.position = it.position - 1
                                                    }
                                                }
                                            }.toMutableList().apply {
                                                remove(image)
                                            }
                                        } else {
                                            if (imageSelects.value.size < Constants.MAX_SELECT_IMAGE) {
                                                imageSelects.value =
                                                    imageSelects.value.map { it }.toMutableList()
                                                        .apply {
                                                            add(ImageSelect(position = imageSelects.value.size + 1,
                                                                index = index, uri = uri))
                                                        }
                                            }
                                        }
                                    }) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = uri
                                    ),
                                    contentDescription = "",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .size(screenWidth)
                                )

                                Box(modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(10.dp)) {
                                    RadioCheck(imageSelects, index)
                                }
//                        if (imageSelectedUri.value != null && imageSelectedUri.value == allImagesFromGallery.value[index])
//                            Box(
//                                modifier = Modifier
//                                    .size(sizeIcon_40)
//                                    .align(Alignment.TopEnd)
//                            ) {
//                                Image(
//                                    painter = painterResource(id = R.drawable.ic_image_check_12),
//                                    contentDescription = "",
//                                    modifier = Modifier
//                                        .size(sizeIcon_16)
//                                        .align(Alignment.Center),
//                                    colorFilter = ColorFilter.tint(Color_LiveStream_Main_Color)
//                                )
//                            }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RadioCheck(imageSelects: MutableState<MutableList<ImageSelect>>, index: Int) {
    val image = imageSelects.value.find { it.index == index }
    if (image != null) {
        Box(modifier = Modifier
            .size(20.dp)
            .background(color = White, shape = CircleShape)
            .clip(CircleShape)
            .padding(1.dp)) {

            Spacer(modifier = Modifier
                .fillMaxSize()
                .background(color = Color_LiveStream_Main_Color, shape = CircleShape)
                .clip(
                    CircleShape))

            Text(text = "${image.position}",
                style = androidx.compose.material.MaterialTheme.typography.subtitle1.copy(
                    fontSize = 10.sp, color = White, lineHeight = 10.sp
                ),
                modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun EmojiSelector(
    onTextAdded: (String) -> Unit,
    focusRequester: FocusRequester,
) {
    var selected by remember { mutableStateOf(EmojiStickerSelector.EMOJI) }

    val a11yLabel = stringResource(id = R.string.emoji_selector_desc)
    Column(
        modifier = Modifier
            .focusRequester(focusRequester) // Requests focus when the Emoji selector is displayed
            // Make the emoji selector focusable so it can steal focus from TextField
            .focusTarget()
            .semantics { contentDescription = a11yLabel }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            ExtendedSelectorInnerButton(
                text = stringResource(id = R.string.emojis_label),
                onClick = { selected = EmojiStickerSelector.EMOJI },
                selected = true,
                modifier = Modifier.weight(1f)
            )
//            ExtendedSelectorInnerButton(
//                text = stringResource(id = R.string.stickers_label),
//                onClick = { selected = EmojiStickerSelector.STICKER },
//                selected = false,
//                modifier = Modifier.weight(1f)
//            )
        }
        Row(modifier = Modifier.verticalScroll(rememberScrollState())) {
            EmojiTable(onTextAdded, modifier = Modifier.padding(8.dp))
        }
    }
    if (selected == EmojiStickerSelector.STICKER) {
        NotAvailablePopup(onDismissed = { selected = EmojiStickerSelector.EMOJI })
    }
}

@Composable
fun ExtendedSelectorInnerButton(
    text: String,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
        disabledContainerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f)
    )
    TextButton(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp)
            .height(36.dp),
        enabled = selected,
        colors = colors,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun EmojiTable(
    onTextAdded: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        repeat(4) { x ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(EMOJI_COLUMNS) { y ->
                    val emoji = emojis[x * EMOJI_COLUMNS + y]
                    Text(
                        modifier = Modifier
                            .clickable(onClick = { onTextAdded(emoji) })
                            .sizeIn(minWidth = 42.dp, minHeight = 42.dp)
                            .padding(8.dp),
                        text = emoji,
                        style = LocalTextStyle.current.copy(
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

private const val EMOJI_COLUMNS = 10

private val emojis = listOf(
    "\ud83d\ude00", // Grinning Face
    "\ud83d\ude01", // Grinning Face With Smiling Eyes
    "\ud83d\ude02", // Face With Tears of Joy
    "\ud83d\ude03", // Smiling Face With Open Mouth
    "\ud83d\ude04", // Smiling Face With Open Mouth and Smiling Eyes
    "\ud83d\ude05", // Smiling Face With Open Mouth and Cold Sweat
    "\ud83d\ude06", // Smiling Face With Open Mouth and Tightly-Closed Eyes
    "\ud83d\ude09", // Winking Face
    "\ud83d\ude0a", // Smiling Face With Smiling Eyes
    "\ud83d\ude0b", // Face Savouring Delicious Food
    "\ud83d\ude0e", // Smiling Face With Sunglasses
    "\ud83d\ude0d", // Smiling Face With Heart-Shaped Eyes
    "\ud83d\ude18", // Face Throwing a Kiss
    "\ud83d\ude17", // Kissing Face
    "\ud83d\ude19", // Kissing Face With Smiling Eyes
    "\ud83d\ude1a", // Kissing Face With Closed Eyes
    "\u263a", // White Smiling Face
    "\ud83d\ude42", // Slightly Smiling Face
    "\ud83e\udd17", // Hugging Face
    "\ud83d\ude07", // Smiling Face With Halo
    "\ud83e\udd13", // Nerd Face
    "\ud83e\udd14", // Thinking Face
    "\ud83d\ude10", // Neutral Face
    "\ud83d\ude11", // Expressionless Face
    "\ud83d\ude36", // Face Without Mouth
    "\ud83d\ude44", // Face With Rolling Eyes
    "\ud83d\ude0f", // Smirking Face
    "\ud83d\ude23", // Persevering Face
    "\ud83d\ude25", // Disappointed but Relieved Face
    "\ud83d\ude2e", // Face With Open Mouth
    "\ud83e\udd10", // Zipper-Mouth Face
    "\ud83d\ude2f", // Hushed Face
    "\ud83d\ude2a", // Sleepy Face
    "\ud83d\ude2b", // Tired Face
    "\ud83d\ude34", // Sleeping Face
    "\ud83d\ude0c", // Relieved Face
    "\ud83d\ude1b", // Face With Stuck-Out Tongue
    "\ud83d\ude1c", // Face With Stuck-Out Tongue and Winking Eye
    "\ud83d\ude1d", // Face With Stuck-Out Tongue and Tightly-Closed Eyes
    "\ud83d\ude12", // Unamused Face
    "\ud83d\ude13", // Face With Cold Sweat
    "\ud83d\ude14", // Pensive Face
    "\ud83d\ude15", // Confused Face
    "\ud83d\ude43", // Upside-Down Face
    "\ud83e\udd11", // Money-Mouth Face
    "\ud83d\ude32", // Astonished Face
    "\ud83d\ude37", // Face With Medical Mask
    "\ud83e\udd12", // Face With Thermometer
    "\ud83e\udd15", // Face With Head-Bandage
    "\u2639", // White Frowning Face
    "\ud83d\ude41", // Slightly Frowning Face
    "\ud83d\ude16", // Confounded Face
    "\ud83d\ude1e", // Disappointed Face
    "\ud83d\ude1f", // Worried Face
    "\ud83d\ude24", // Face With Look of Triumph
    "\ud83d\ude22", // Crying Face
    "\ud83d\ude2d", // Loudly Crying Face
    "\ud83d\ude26", // Frowning Face With Open Mouth
    "\ud83d\ude27", // Anguished Face
    "\ud83d\ude28", // Fearful Face
    "\ud83d\ude29", // Weary Face
    "\ud83d\ude2c", // Grimacing Face
    "\ud83d\ude30", // Face With Open Mouth and Cold Sweat
    "\ud83d\ude31", // Face Screaming in Fear
    "\ud83d\ude33", // Flushed Face
    "\ud83d\ude35", // Dizzy Face
    "\ud83d\ude21", // Pouting Face
    "\ud83d\ude20", // Angry Face
    "\ud83d\ude08", // Smiling Face With Horns
    "\ud83d\udc7f", // Imp
    "\ud83d\udc79", // Japanese Ogre
    "\ud83d\udc7a", // Japanese Goblin
    "\ud83d\udc80", // Skull
    "\ud83d\udc7b", // Ghost
    "\ud83d\udc7d", // Extraterrestrial Alien
    "\ud83e\udd16", // Robot Face
    "\ud83d\udca9", // Pile of Poo
    "\ud83d\ude3a", // Smiling Cat Face With Open Mouth
    "\ud83d\ude38", // Grinning Cat Face With Smiling Eyes
    "\ud83d\ude39", // Cat Face With Tears of Joy
    "\ud83d\ude3b", // Smiling Cat Face With Heart-Shaped Eyes
    "\ud83d\ude3c", // Cat Face With Wry Smile
    "\ud83d\ude3d", // Kissing Cat Face With Closed Eyes
    "\ud83d\ude40", // Weary Cat Face
    "\ud83d\ude3f", // Crying Cat Face
    "\ud83d\ude3e", // Pouting Cat Face
    "\ud83d\udc66", // Boy
    "\ud83d\udc67", // Girl
    "\ud83d\udc68", // Man
    "\ud83d\udc69", // Woman
    "\ud83d\udc74", // Older Man
    "\ud83d\udc75", // Older Woman
    "\ud83d\udc76", // Baby
    "\ud83d\udc71", // Person With Blond Hair
    "\ud83d\udc6e", // Police Officer
    "\ud83d\udc72", // Man With Gua Pi Mao
    "\ud83d\udc73", // Man With Turban
    "\ud83d\udc77", // Construction Worker
    "\u26d1", // Helmet With White Cross
    "\ud83d\udc78", // Princess
    "\ud83d\udc82", // Guardsman
    "\ud83d\udd75", // Sleuth or Spy
    "\ud83c\udf85", // Father Christmas
    "\ud83d\udc70", // Bride With Veil
    "\ud83d\udc7c", // Baby Angel
    "\ud83d\udc86", // Face Massage
    "\ud83d\udc87", // Haircut
    "\ud83d\ude4d", // Person Frowning
    "\ud83d\ude4e", // Person With Pouting Face
    "\ud83d\ude45", // Face With No Good Gesture
    "\ud83d\ude46", // Face With OK Gesture
    "\ud83d\udc81", // Information Desk Person
    "\ud83d\ude4b", // Happy Person Raising One Hand
    "\ud83d\ude47", // Person Bowing Deeply
    "\ud83d\ude4c", // Person Raising Both Hands in Celebration
    "\ud83d\ude4f", // Person With Folded Hands
    "\ud83d\udde3", // Speaking Head in Silhouette
    "\ud83d\udc64", // Bust in Silhouette
    "\ud83d\udc65", // Busts in Silhouette
    "\ud83d\udeb6", // Pedestrian
    "\ud83c\udfc3", // Runner
    "\ud83d\udc6f", // Woman With Bunny Ears
    "\ud83d\udc83", // Dancer
    "\ud83d\udd74", // Man in Business Suit Levitating
    "\ud83d\udc6b", // Man and Woman Holding Hands
    "\ud83d\udc6c", // Two Men Holding Hands
    "\ud83d\udc6d", // Two Women Holding Hands
    "\ud83d\udc8f" // Kiss
)
