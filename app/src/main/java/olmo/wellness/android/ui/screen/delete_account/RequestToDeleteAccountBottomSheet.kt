package olmo.wellness.android.ui.screen.delete_account

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.screen.profile_dashboard.MyProfileDashBoardViewModel
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RequestToDeleteAccountBottomSheet(modalBottomSheetState: ModalBottomSheetState, viewModel : MyProfileDashBoardViewModel?) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var currentStep = remember { mutableStateOf(1) }

    val visibleStep1 = remember { mutableStateOf(true) }
    val visibleStep2 = remember { mutableStateOf(false) }
    val visibleStep3 = remember { mutableStateOf(false) }
    val visibleStep4 = remember { mutableStateOf(false) }

    val description = remember {
        mutableStateOf("")
    }

    val radioReason = listOf(
        "I spend too much time on Kepler",
        "I have a privacy concern",
        "I don’t feel safe on Olmo Wellness",
        "My account was hacked",
        "I am no longer find Kepler useful",
        "Other Reasons"
    )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf<String?>(null) }

    val reason = remember {
        mutableStateOf<String?>(null)
    }
    if (selectedOption!=radioReason.get(5)){
        reason.value= selectedOption
    }else{
        reason.value= description.value
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            Scaffold(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 22.dp), bottomBar = {
                    ActionBottomUI(
                        context = context,
                        currentStep = currentStep.value,
                        onDismiss = {
                            resetData(visibleStep1, visibleStep2, visibleStep3, visibleStep4)
                            currentStep.value = 1
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                        },
                        enableConfirm = true,
                        modifier = Modifier, reason = reason.value,
                        viewModel = viewModel,
                    ) {
                        currentStep.value++
                        when (currentStep.value) {
                            2 -> {
                                visibleStep1.value = !visibleStep1.value
                                visibleStep2.value = !visibleStep2.value
                            }
                            3 -> {
                                visibleStep2.value = !visibleStep2.value
                                visibleStep3.value = !visibleStep3.value
                            }
                            4 -> {
                                visibleStep3.value = !visibleStep3.value
                                visibleStep4.value = !visibleStep4.value
                            }
                        }
                    }
                }, content = {
                    Box(
                        modifier = Modifier
                    ) {

                        this@ModalBottomSheetLayout.RequestStep1(visibleStep1)
                        this@ModalBottomSheetLayout.RequestStep2(visibleStep2)

                        this@ModalBottomSheetLayout.RequestStep3(
                            visibleStep3,
                            radioReason,
                            selectedOption,
                            description,
                            onOptionSelected
                        )
                        this@ModalBottomSheetLayout.RequestStep4(visibleStep4)
                    }
                })

        }
    ) {

    }
}

@Composable
private fun ColumnScope.RequestStep3(
    visibleStep3: MutableState<Boolean>,
    radioReason: List<String>,
    selectedOption: String?,
    description: MutableState<String>,
    onOptionSelected: (String?) -> Unit
) {
    val focusManager = LocalFocusManager.current

    this@RequestStep3.AnimatedVisibility(
        visible = visibleStep3.value,
        enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
            -fullWidth / 3
        } + fadeIn(
            animationSpec = tween(durationMillis = 200)
        ),
        exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
            200
        } + fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Choose Your Reason",
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 16.sp, color = Color_Purple_FBC
                )
            )

            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                radioReason.forEach { reason ->
                    Row(
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .selectable(
                                selected = (reason == selectedOption),
                                onClick = {
                                    onOptionSelected(reason)
                                })
                            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (reason == selectedOption),
                            modifier = Modifier.size(20.dp),
                            colors = RadioButtonDefaults.colors(
                                Color_gray_6CF,
                                Neutral_Bare_Gray
                            ), onClick = {
                                onOptionSelected(reason)
                            })
                        Text(
                            text = reason, modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 14.sp, color = (if (reason == selectedOption) {
                                    Color_Purple_FBC
                                } else {
                                    Neutral_Gray_7
                                })
                            )
                        )
                    }
                }

                TextField(
                    enabled = selectedOption == radioReason[5],
                    value = description.value ?: "", onValueChange = {
                        description.value = it
                    }, modifier = Modifier
                        .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {})
                        .height(100.dp)
                        .fillMaxWidth()
                        .background(color = Color_gray_3F9, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp), placeholder = {
                        Text(
                            text = "We are all ears...",
                            style = MaterialTheme.typography.subtitle1.copy(
                                color = Neutral_Gray_5, fontSize = 14.sp
                            ),
                        )
                    }, textStyle = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 14.sp
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color_gray_3F9,
                        disabledLabelColor = White,
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent,
                        cursorColor = Color.Black
                    ),
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


            }

        }
    }
}

private fun resetData(
    visibleStep1: MutableState<Boolean>,
    visibleStep2: MutableState<Boolean>,
    visibleStep3: MutableState<Boolean>,
    visibleStep4: MutableState<Boolean>
) {
    visibleStep1.value = true
    visibleStep2.value = false
    visibleStep3.value = false
    visibleStep4.value = false
}

@Composable
private fun ColumnScope.RequestStep1(visibleStep1: MutableState<Boolean>) {
    this.AnimatedVisibility(
        visible = visibleStep1.value,
        enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
            -fullWidth / 3
        } + fadeIn(
            animationSpec = tween(durationMillis = 200)
        ),
        exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
            200
        } + fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Request To Delete Account",
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 16.sp, color = Color_Purple_FBC
                )
            )

            Spacer(modifier = Modifier.padding(vertical = 20.dp))

            Text(
                text = "We're sad to see you leave. \n" +
                        "Please note that account deletion is irreversible",
                style = MaterialTheme.typography.caption.copy(
                    fontSize = 14.sp, color = Color_Purple_FBC
                ), modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ColumnScope.RequestStep4(visibleStep: MutableState<Boolean>) {
    this.AnimatedVisibility(
        visible = visibleStep.value,
        enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
            -fullWidth / 3
        } + fadeIn(
            animationSpec = tween(durationMillis = 200)
        ),
        exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
            200
        } + fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.padding(vertical = 20.dp))

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Neutral_Gray_7,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("We’ll inform via email ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color_Purple_FBC,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("${sharedPrefs.getUserInfoLocal().identity}")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = Neutral_Gray_9
                        )
                    ) {
                        append(" in 3 working days In case of you change your mind, you can touch ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = Color_Purple_FBC
                        )
                    ) {
                        append("here")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = Neutral_Gray_9
                        )
                    ) {
                        append("  to turn on your vacation mode.")
                    }
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption.copy(
                    fontSize = 14.sp
                ),
            )
        }
    }
}


@Composable
private fun ColumnScope.RequestStep2(visibleStep: MutableState<Boolean>) {
    this.AnimatedVisibility(
        visible = visibleStep.value,
        enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
            -fullWidth / 3
        } + fadeIn(
            animationSpec = tween(durationMillis = 200)
        ),
        exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
            200
        } + fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Important",
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 16.sp, color = Color_Purple_FBC
                )
            )

            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            Text(
                text = "Please read our policies and terms carefully before deleting:",
                style = MaterialTheme.typography.caption.copy(
                    fontSize = 14.sp, color = Color_Red_F33
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "・Your account should have no pending purchase and/or sales.\n" +
                        "・After successful deletion of your account, Kepler will continue to hold transactional data for financial audit purposes.\n" +
                        "・After successful deletion of your account, you will not be able to log in to a deleted account and view previous account history.\n" +
                        "・Kepler reserves the right to reject future account creation requests.",
                style = MaterialTheme.typography.caption.copy(
                    fontSize = 14.sp, color = Color_Purple_FBC
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp),
            )
        }
    }
}


@Composable
fun ActionBottomUI(
    context: Context,
    currentStep: Int,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    enableConfirm: Boolean,
    reason: String?,
    viewModel: MyProfileDashBoardViewModel?,
    onConfirm: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .background(White)
    ) {
        if (currentStep == 4) {
            PrimaryLiveButton(
                enable = enableConfirm,
                modifier = Modifier
                    .padding(horizontal = 30.dp),
                text = stringResource(R.string.lb_proceed_anyway),
                onClickFunc = {
                    onDismiss.invoke()
                    viewModel?.deleteAccount(reason)
                }
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PrimaryLiveButton(
                    enable = enableConfirm,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp),
                    text = stringResource(R.string.cancel),
                    onClickFunc = {
                        onDismiss.invoke()
                    }
                )

                SecondLiveButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 6.dp)
                        .noRippleClickable {
                            (context as Activity).onBackPressed()
                        },
                    stringResource(R.string.lb_prceed),
                    onClickFunc = {
                        onConfirm.invoke()
                    }
                )

            }
        }

    }
}