package olmo.wellness.android.ui.common.bottom_sheet

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import olmo.wellness.android.core.utils.getScreenHeight
import olmo.wellness.android.ui.theme.space_30


// Extension for Activity
fun Activity.showAsBottomSheet(
    closeDialog: MutableState<Boolean>? = null,
    content: @Composable (() -> Unit) -> Unit){
    val viewGroup = this.findViewById(android.R.id.content) as ViewGroup
    addContentToView(viewGroup, content, closeDialog)
}


fun Activity.showAsBottomSheet(
    closeDialog: MutableState<Boolean>? = null,
    content: @Composable (() -> Unit) -> Unit,
    isCancel: Boolean
) {
    val viewGroup = this.findViewById(android.R.id.content) as ViewGroup
    addContentToView(viewGroup, content, closeDialog,isCancel)
}

// Helper method
private fun addContentToView(
    viewGroup: ViewGroup,
    content: @Composable (() -> Unit) -> Unit,
    closeDialog: MutableState<Boolean>? = null,
    isCancel: Boolean = false,
) {
    viewGroup.addView(
        ComposeView(viewGroup.context).apply {
            setContent {
                BottomSheetWrapper(viewGroup, this, content, closeDialog,isCancel)
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheetWrapper(
    parent: ViewGroup,
    composeView: ComposeView,
    content: @Composable (() -> Unit) -> Unit,
    closeDialog: MutableState<Boolean>? = null,
    isCancel: Boolean = false,
) {
    val TAG = parent::class.java.simpleName
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            !isCancel
        })
    var isSheetOpened by remember { mutableStateOf(false) }
    val hasBottomNav = remember {
        parent.bottom - getScreenHeight() > 1 // 1 is min satisfied gap
    }
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = space_30, topEnd = space_30),
        sheetContent = {
            content {
                // Action passed for clicking close button in the content
                coroutineScope.launch {
                    modalBottomSheetState.hide() // will trigger the LaunchedEffect
                }
            }
        },
        sheetBackgroundColor = Color.Transparent,
        modifier = Modifier
            .padding(
                bottom = if (hasBottomNav) 32.dp else 0.dp
            )
    ) {}

    BackHandler {
        coroutineScope.launch {
            modalBottomSheetState.hide() // will trigger the LaunchedEffect
        }
    }

    if (closeDialog?.value == true) {
        coroutineScope.launch {
            modalBottomSheetState.hide() // will trigger the LaunchedEffect
            closeDialog.value = false
        }
    }

    // Take action based on hidden state
    LaunchedEffect(modalBottomSheetState.currentValue) {
        when (modalBottomSheetState.currentValue) {
            ModalBottomSheetValue.Hidden -> {
                when {
                    isSheetOpened -> parent.removeView(composeView)
                    else -> {
                        isSheetOpened = true
                        modalBottomSheetState.show()
                    }
                }
            }
            else -> {
                Log.i(TAG, "Bottom sheet ${modalBottomSheetState.currentValue} state")
            }
        }
    }
}

fun Activity.hasNavBar(): Boolean {
    val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        display
    } else {
        windowManager.defaultDisplay
    } ?: return true

    val realDisplayMetrics = DisplayMetrics()
    display.getRealMetrics(realDisplayMetrics)

    val realHeight = realDisplayMetrics.heightPixels
    val realWidth = realDisplayMetrics.widthPixels

    val displayMetrics = DisplayMetrics()
    display.getMetrics(displayMetrics)

    val displayHeight = displayMetrics.heightPixels
    val displayWidth = displayMetrics.widthPixels

    return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
}