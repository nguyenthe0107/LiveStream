package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Black_466
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.White

@Composable
fun CategoryLiveStreamItem(item: LiveCategory, selectedIndex: Boolean, onItemClick: ((isSelected: Boolean) -> Unit )?= null,
                           disableClick: Boolean ?= null){
    var isSelected by rememberSaveable {
        mutableStateOf(selectedIndex)
    }
    if(item.isSelected && (disableClick == null || disableClick == false)){
        isSelected = true
    }
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(
                top = 10.dp,
                end = 10.dp
            )
            .noRippleClickable {
                if (disableClick==false) {
                    isSelected = !isSelected
                    onItemClick?.invoke(isSelected)
                }
            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val activeColor = if (isSelected) Color_LiveStream_Main_Color else White
        val textColor = if (isSelected) Color_LiveStream_Main_Color else Black_466
        val shadowColor = if (isSelected) Color_LiveStream_Main_Color else Color.Gray
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = shadowColor,
                    ambientColor = shadowColor
                ),
            color = activeColor
        ) {
            AsyncImage(
                model = item.icon,
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .padding(20.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Inside
            )
        }

        Text(
            text = item.nameLocale?.en?:"",
            style = MaterialTheme.typography.body2.copy(
                color = textColor
            ),
            modifier = Modifier
                .padding(
                    top = 4.dp
                )
        )
    }
}