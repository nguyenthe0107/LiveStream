package olmo.wellness.android.ui.screen.profile_dashboard.edit_phone

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PhoneInputProfileCompose(
    modifier: Modifier = Modifier,
    hint: String?,
    onTextChanged: ((text: String) -> Unit)? = null,
    lastInput: Boolean = false,
    onKeyboardDropFunc: (() -> Unit)?=null,
    countryCodeValue: ((text: String) -> Unit)? = null,
    onClickSelectFlag: (() -> Unit)?=null,
    listCountry: List<Country>? = listOf(Country(id = 0, phonePrefix = "+84", name = "VietNam", flagIconUrl = "")),
    defaultContent : String ?= "",
    defaultCountryCode : String ?= "+84"){

    var countryCode by remember {
        mutableStateOf(defaultCountryCode ?: "+84")
    }

    var countryFlag by remember {
        mutableStateOf("")
    }

    var initCompose by remember {
        mutableStateOf(true)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Surface(
            modifier = modifier
                .padding(top = 4.dp, bottom = 4.dp)
                .fillMaxWidth()
                .onFocusEvent {
                    if (!it.isFocused) {
                        onKeyboardDropFunc?.invoke()
                    }
                }) {
            val focusManager = LocalFocusManager.current
            var text by remember {
                mutableStateOf(TextFieldValue(defaultContent?: ""))
            }
            var mExpanded by remember { mutableStateOf(false) }
            val painterFlag = if(initCompose || listCountry?.isEmpty() == true) {
                painterResource(id = R.mipmap.ic_flag)
            }else{rememberImagePainter(data = countryFlag, builder = {
                placeholder(R.drawable.ic_baseline_image_24)
            })}
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SpaceHorizontalCompose(width = 4.dp)
                Card(
                    shape = RoundedCornerShape(4.dp)){
                    Image(painter = painterFlag, modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            mExpanded = !mExpanded
                            onClickSelectFlag?.invoke()
                        }
                        ,contentDescription = "", contentScale = ContentScale.FillBounds)
                }
                SpaceHorizontalCompose(width = 10.dp)
                TextField(
                    singleLine = true,
                    leadingIcon = {
                        Text(
                            text = countryCode,
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontWeight = FontWeight.Normal,
                                fontFamily = MontserratFont,
                                color = Color_Purple_FBC
                            ),
                            color = Color_Purple_FBC,
                            modifier = Modifier.padding(start = 10.dp, end = 4.dp)
                        )
                    },
                    value = text,
                    onValueChange = {
                        text = it
                        onTextChanged?.invoke(text.text)
                    },
                    placeholder = {
                        Text(
                            text = hint.orEmpty(),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontWeight = FontWeight.Medium,
                                fontFamily = MontserratFont,
                                color = Color(0xFFAEB1B7)
                            )
                        )
                    },
                    textStyle = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = MontserratFont,
                        color = Neutral_Gray_9
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color_gray_3F9,
                        textColor = Neutral_Gray_9,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(shape = RoundedCornerShape(35.dp))
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            handleKeyboard(
                                lastInput,
                                focusManager,
                                onKeyboardDropFunc
                            )
                        },
                        onGo = {
                            handleKeyboard(
                                lastInput,
                                focusManager,
                                onKeyboardDropFunc
                            )
                        },
                        onNext = {
                            handleKeyboard(
                                lastInput,
                                focusManager,
                                onKeyboardDropFunc
                            )
                        }
                    )
                )
            }
            val items = listCountry ?: listOf(Country(id = 0, phonePrefix = "+84", name = "vi", flagIconUrl = ""))
            val context = LocalContext.current
            val closeDialog = remember {
                mutableStateOf(false)
            }
            if(mExpanded){
                LaunchedEffect(key1 = true){
                    context.let {
                        (context as MainActivity).showAsBottomSheet(closeDialog){
                            CountryCodeScreen(listCountry = items, onSelectedCallBack = {
                                closeDialog.value = true
                                countryCodeValue?.invoke(it.phonePrefix)
                                countryCode = it?.phonePrefix ?: "+84"
                                countryFlag = it?.flagIconUrl ?: "+84"
                                initCompose = false
                                mExpanded = false
                            })
                        }
                    }
                }
            }
        }
    }
}

private fun handleKeyboard(
    lastInput: Boolean,
    focusManager: FocusManager,
    onKeyboardDropFunc: (() -> Unit)?
) {
    if (lastInput) {
        focusManager.clearFocus()
    } else {
        focusManager.moveFocus(FocusDirection.Down)
    }
    onKeyboardDropFunc?.invoke()
}