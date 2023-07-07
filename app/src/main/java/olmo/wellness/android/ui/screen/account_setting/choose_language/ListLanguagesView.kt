package olmo.wellness.android.ui.screen.account_setting.choose_language

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.data.model.LanguageModel
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.ItemSwitch
import olmo.wellness.android.ui.common.SwitchOnOff
import olmo.wellness.android.ui.theme.Color_Purple_FBC
import olmo.wellness.android.ui.theme.White

@Composable
fun ListLanguagesView(
    listLanguages: List<LanguageModel>,
    onLanguageChanged: (LanguageModel?) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(
            items = listLanguages,
            key = { item -> item.id },
            itemContent = { item: LanguageModel ->

            }
        )

        listLanguages.forEach {
            item(
            content = {
                ItemSingleChoiceView(it)
            })
        }

    }
}

@Composable
private fun ItemSingleChoiceView(
    language: LanguageModel,
) {

    ItemSwitch(title = language.languageName, modifier = Modifier,switchDefault = language.select.value,onSwitch ={
        language.select.value=it
    })
}


