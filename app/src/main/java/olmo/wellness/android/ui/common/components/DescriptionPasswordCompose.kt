package olmo.wellness.android.ui.common.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.validate.letterRulePassword
import olmo.wellness.android.ui.common.validate.numberRulePassword
import olmo.wellness.android.ui.common.validate.specialCharacterRulePassword
import olmo.wellness.android.ui.common.validate.upperCaseRulePassword
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DescriptionPasswordCompose(
    paddingStart: Dp = zeroDimen,
    paddingEnd: Dp = zeroDimen, paddingTop: Dp = zeroDimen,
    paddingBottom: Dp = zeroDimen,
    normalStateIcon: Int? = null,
    activeStateIcon: Int? = null,
    errorStateIcon: Int? = null,
    content: String? = "",
    isChecking: Boolean = false,
    isValidAllFiledCallBack: ((Boolean) -> Unit) ?= null,
    defaultValueValidate : Boolean ?= false
) {
    Column(
        modifier = Modifier
    ){
        var isValidAllFiled by remember {
            mutableStateOf(defaultValueValidate ?: false)
        }
        if(content != null){
            if(content.isNotEmpty()){
                isValidAllFiledCallBack?.invoke(isValidAllFiled)
            }
        }
        val sizePasswordCondition = if(content != null && isValidAllFiled){
            content.isNotEmpty()
        }else{
            false
        }
        SpaceCompose(height = 15.dp)
        Text(
            text = stringResource(id = R.string.tv_des_condition_pw),
            style = MaterialTheme.typography.overline.copy(
                color = if(sizePasswordCondition){
                    Neutral_Gray_9
                }else{
                    Neutral_Gray_5
                },
                fontFamily = MontserratFont,
                fontWeight = FontWeight.Medium,
                lineHeight = 14.sp,
                textAlign = TextAlign.Start
            ),
        )
        SpaceCompose(height = 4.dp)
        val painterNormal = painterResource(id = normalStateIcon ?: R.drawable.ic_normal_state_pw)
        val painterActive = painterResource(id = activeStateIcon ?: R.drawable.ic_active_pw_purple)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isChecking) {
                if (content?.isNotEmpty() == true) {
                    if (letterRulePassword(content)) {
                        BuildSuccess(painterActive, R.string.tv_des_pw_letter)
                        isValidAllFiled = true
                    } else {
                        BuildError(painterNormal, R.string.tv_des_pw_letter)
                        isValidAllFiled = false
                    }
                }else {
                    BuildNormal(painterNormal, R.string.tv_des_pw_letter)
                }
            } else {
                BuildNormal(painterNormal, R.string.tv_des_pw_letter)
            }
        }
        SpaceCompose(height = 5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isChecking) {
                if (content?.isNotEmpty() == true) {
                    if (upperCaseRulePassword(content)) {
                        BuildSuccess(painterActive, R.string.tv_des_pw_uppercase)
                        isValidAllFiled = true
                    } else {
                        BuildError(painterNormal, R.string.tv_des_pw_uppercase)
                        isValidAllFiled = false
                    }
                }else {
                    BuildNormal(painterNormal, R.string.tv_des_pw_uppercase)
                }
            } else {
                BuildNormal(painterNormal, R.string.tv_des_pw_uppercase)
            }
        }
        SpaceCompose(height = 5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isChecking) {
                if (content?.isNotEmpty() == true) {
                    if (numberRulePassword(content)) {
                        BuildSuccess(painterActive, R.string.tv_des_pw_number)
                        isValidAllFiled = true
                    } else {
                        BuildError(painterNormal, R.string.tv_des_pw_number)
                        isValidAllFiled = false
                    }
                } else {
                    BuildNormal(painterNormal, R.string.tv_des_pw_number)
                }
            }else{
                BuildNormal(painterNormal, R.string.tv_des_pw_number)
            }
        }
        SpaceCompose(height = 5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isChecking) {
                if (content?.isNotEmpty() == true) {
                    if (specialCharacterRulePassword(content)) {
                        BuildSuccess(painterActive, R.string.tv_des_pw_special_charactor)
                        isValidAllFiled = true
                    } else {
                        BuildError(painterNormal, R.string.tv_des_pw_special_charactor)
                        isValidAllFiled = false
                    }
                } else {
                    BuildNormal(painterNormal, R.string.tv_des_pw_special_charactor)
                }
            }else{
                BuildNormal(painterNormal, R.string.tv_des_pw_special_charactor)
            }
        }
    }
}


@Composable
fun BuildNormal(painterNormal: Painter, contentResource: Int) {
    Image(
        painter = painterNormal,
        contentDescription = "",
        modifier = Modifier.padding(end = 4.dp)
    )
    Text(
        text = stringResource(id = contentResource),
        style = MaterialTheme.typography.overline.copy(
            color = Neutral_Gray_5,
            fontFamily = MontserratFont,
            fontWeight = FontWeight.SemiBold
        ),
    )
}

@Composable
fun BuildError(painterError: Painter, contentResource: Int) {
    Image(
        painter = painterError,
        contentDescription = "",
        modifier = Modifier.padding(end = 4.dp)
    )
    Text(
        text = stringResource(id = contentResource),
        style = MaterialTheme.typography.overline.copy(
            color = Neutral_Gray_5,
            fontFamily = MontserratFont,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp
        ),
    )
}

@Composable
fun BuildSuccess(painterActive: Painter, contentResource: Int) {
    Image(
        painter = painterActive,
        contentDescription = "",
        modifier = Modifier.padding(end = 4.dp)
    )
    Text(
        text = stringResource(id = contentResource),
        style = MaterialTheme.typography.overline.copy(
            color = Color_Purple_FBC,
            fontFamily = MontserratFont,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp
        ),
    )
}
