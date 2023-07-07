package olmo.wellness.android.ui.screen.account_setting.choose_language

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.data.model.LanguageModel
import olmo.wellness.android.data.model.LanguageModel.Companion.ENGLISH
import olmo.wellness.android.data.model.LanguageModel.Companion.VIETNAM
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.screen.business_hours.*
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChooseLanguage(navController: NavController) {

    val listLanguages=  listOf(ENGLISH, VIETNAM)


    Scaffold(topBar = {
        ToolbarSchedule(
            title = stringResource(R.string.lb_languages),
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
            val (options, imageCompose, endCompose) = createRefs()
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
                ) {

                    Spacer(modifier = Modifier.padding(vertical = 20.dp))

                    ListLanguagesView(
                        listLanguages,
                    ){ languageSelected ->
//                        updateLanguage(languageSelected)
                    }
                }
            }

            AvatarMascot(modifier = Modifier.constrainAs(imageCompose) {
                top.linkTo(parent.top, 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, uri = null, callbackFun = {
            }, src = R.drawable.ic_business_hours)

        }
    }
}

private fun updateLanguage(languageSelected: LanguageModel) {
}
