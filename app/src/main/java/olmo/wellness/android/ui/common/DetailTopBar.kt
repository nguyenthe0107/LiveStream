package olmo.wellness.android.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.systemBarsPadding
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import olmo.wellness.android.ui.theme.White

@Composable
fun DetailTopBar(
    title: String?,
    navController: NavController,
    backgroundColor: Color = White,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    isDisableNavigation: Boolean? = false,
    onBackStackFunc: (() -> Unit) ?= null,
    @DrawableRes
    backIconDrawable: Int = R.drawable.ic_baseline_arrow_back_24,
    backIconDrawableTintColor: Color = Neutral_Gray_9,
    actions: @Composable RowScope.() -> Unit = {},
    onOpenDrawer: (() -> Unit) ?= null,
    titleColor: Color = Neutral_Gray_9,
    ) {
    val focusManager = LocalFocusManager.current
    CenterTopAppBar(
        title = {
            Text(
                text = title ?: "",
                color = titleColor,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            if (isDisableNavigation != true) {
                IconButton(onClick = {
                    if(onBackStackFunc != null){
                        onBackStackFunc.invoke()
                    }
                    else if(onOpenDrawer != null){
                        onOpenDrawer.invoke()
                    }
                    else{
                        navController.popBackStack()
                    }
                    focusManager.clearFocus()
                }) {
                    Icon(
                        painter = painterResource(id = backIconDrawable),
                        contentDescription = null,
                        tint = backIconDrawableTintColor
                    )
                }
            }
        },
        actions = actions,
        backgroundColor = backgroundColor,
        elevation = elevation
    )
}