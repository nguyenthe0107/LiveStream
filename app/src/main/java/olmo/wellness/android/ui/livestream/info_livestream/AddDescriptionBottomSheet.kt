package olmo.wellness.android.ui.livestream.info_livestream

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.data.model.live_stream.FillDataLiveStream
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.theme.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddDescriptionBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    fillData: FillDataLiveStream?,
    onSave: (String?) -> Unit
) {
    val scope = rememberCoroutineScope()


    val description = remember {
        mutableStateOf(fillData?.description)
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            if (modalBottomSheetState.isVisible) {
                DescriptionUI(description.value, onSave) {
                    description.value = it
                }
            }else{
                EmptyBottomSheet()
            }
        }) {

    }

//    if (modalBottomSheetState.isVisible){
//        scope.launch {
//            focusRequest.requestFocus()
//        }
//    }

}

@SuppressLint("ResourceAsColor")
@Composable
private fun DescriptionUI(description: String?, onSave: (String?) -> Unit, onDesChange: (String?) -> Unit) {
    val focusManager = LocalFocusManager.current
    Column(modifier = Modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = stringResource(R.string.lb_add_description),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .align(Alignment.Center)
            )

            TextButton(
                onClick = {
//                    scope.launch {
//                        focusManager.clearFocus()
//                    }
                    onSave.invoke(description)
                }, modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save), style = MaterialTheme.typography.h6.copy(
                        fontSize = 14.sp, color = Color_gray_6CF
                    )
                )
            }

            Spacer (
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = Neutral_Gray_3)
                    .align(Alignment.BottomStart)
            )
        }

        TextField(
            value = description ?: "", onValueChange = {
                onDesChange.invoke(it)
            }, modifier = Modifier
                .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {})
                .height(200.dp)
                .fillMaxWidth()
                .padding(10.dp)
              , placeholder = {
                Text(
                    text = stringResource(R.string.hint_description_livestream),
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = Neutral_Gray_5, fontSize = 14.sp))
            }, textStyle = MaterialTheme.typography.subtitle1.copy(
                fontSize = 14.sp
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = White,
                disabledLabelColor= White,
                focusedIndicatorColor= Transparent,
                unfocusedIndicatorColor= Transparent,
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
