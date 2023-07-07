package olmo.wellness.android.ui.screen.profile_dashboard.edit_phone

import android.annotation.SuppressLint
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.profile_dashboard.items.InvalidateCompose
import olmo.wellness.android.ui.screen.profile_dashboard.items.ValidateSuccessCompose
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun EditPhoneScreen(modalBottomSheetState: ModalBottomSheetState, title: Int?, content: String = "",
                       onSuccess: (Boolean, String) -> Unit,
                       onFailed: (status: Boolean) -> Unit,
                       viewModel: EditPhoneViewModel = hiltViewModel()){

    val context = LocalContext.current
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                (context as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            Lifecycle.Event.ON_PAUSE -> {
            }
            Lifecycle.Event.ON_STOP -> {
                (context as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
            Lifecycle.Event.ON_DESTROY -> {
            }
            else -> {
            }
        }
    }
    val profileModel = viewModel.profileModel.collectAsState()
    var phoneNumberDefault = profileModel.value.phoneNumber ?:""
    var countryCodeDefault = "+84"
    try {
        val phoneUtil = PhoneNumberUtil.getInstance()
        val numberProto = phoneUtil.parse(phoneNumberDefault, "ZZ")
        countryCodeDefault = numberProto.countryCode.toString()
        phoneNumberDefault = numberProto.nationalNumber.toString()
        if(!countryCodeDefault.contains("+")){
            countryCodeDefault = "+".plus(countryCodeDefault)
        }
    } catch (e: NumberParseException) {
        System.err.println("NumberParseException was thrown: $e")
    }
    var phoneInput by remember {
        mutableStateOf(phoneNumberDefault)
    }
    val isSuccess = viewModel.isSuccess.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val countryCodeSelected = viewModel.countryCodeSelected.collectAsState()
    val scope = rememberCoroutineScope()
    val isErrorValidatePhone = viewModel.isErrorValidate.collectAsState()
    val err = viewModel.error.collectAsState()
    if(isSuccess.value){
        scope.launch {
            modalBottomSheetState.hide()
        }
        onSuccess.invoke(true, countryCodeSelected.value + phoneInput)
        viewModel.resetState()
    }
    Column(
        modifier = Modifier
            .background(Color_gray_FF7)
            .fillMaxHeight(0.6f),
        verticalArrangement = Arrangement.SpaceBetween){
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Top){
            if (title != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color_gray_FF7)
                        .padding(marginDouble),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(title),
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color_Purple_FBC,
                        modifier = Modifier.padding(horizontal = marginDouble),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Column(modifier = Modifier.padding(start = marginDouble, end = marginDouble)) {
                PhoneInputProfileCompose(
                    hint = stringResource(id = R.string.hint_phone_input),
                    onTextChanged = { text ->
                        phoneInput = text.trim()
                        viewModel.isPhoneValid(phoneInput)
                    },
                    listCountry = viewModel.countryList.collectAsState().value,
                    countryCodeValue = {
                        viewModel.updateCountrySelected(it)
                    },
                    onClickSelectFlag = {
                    },
                    defaultContent = phoneInput,
                    defaultCountryCode = countryCodeDefault
                )
            }
            Column(modifier = Modifier
                .background(Color_gray_FF7)
                .wrapContentHeight()
                .padding(top = 14.dp)) {
                if (err.value.isEmpty() && phoneInput.isNotEmpty() && !isErrorValidatePhone.value) {
                    ValidateSuccessCompose(horizontalDp = marginDouble, verticalDp = marginStandard)
                } else {
                    if(err.value.isNotEmpty()){
                        InvalidateCompose(contentNormal = err.value,
                            contentError = err.value,
                            iconDefault = R.drawable.olmo_ic_warning_highest, paddingStart = marginDouble,paddingEnd = marginDouble,
                            isError = err.value.isNotEmpty()
                        )
                    }
                }

                if(phoneInput.isEmpty()){
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = marginDouble)) {
                        Text(
                            text = "Enter your phone number",
                            style = MaterialTheme.typography.subtitle2.copy(
                                color = Color_gray_F92,
                                fontSize = 12.sp,
                                lineHeight = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom){
            GroupButtonBottomCompose(cancelCallback = {
                scope.launch {
                    modalBottomSheetState.hide()
                }
                onFailed?.invoke(true)
            }, confirmCallback = {
                if(phoneInput.isNotEmpty()){
                    viewModel.updatePhone(phoneInput)
                }
            },
            enable = phoneInput.isNotEmpty() && !isErrorValidatePhone.value)
        }
    }
}