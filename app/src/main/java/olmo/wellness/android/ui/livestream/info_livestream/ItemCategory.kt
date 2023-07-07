package olmo.wellness.android.ui.livestream.info_livestream

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import olmo.wellness.android.ui.theme.marginStandard
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.ui.theme.Color_DOT_SLIDE_DISABLE

private fun addOrClearCategory(listSelected: MutableList<LiveCategory>, category: LiveCategory) {
    val position = listSelected.indexOfFirst { it.id == category.id }
    if (position < 0) listSelected.add(category) else listSelected.removeAt(position)
}

fun checkItemSelect(listSelected: MutableList<LiveCategory>, category: LiveCategory): Boolean {
    val position = listSelected.indexOfFirst { it.id == category.id }
    if (position >= 0) return true
    return false
}

@Composable
fun ItemCategory(modifier: Modifier= Modifier,category: LiveCategory, isSelected: Boolean, onClick: (LiveCategory) -> Unit) {
    val shape = RoundedCornerShape(7.dp)
    Box(
        modifier = modifier
            .padding(marginStandard)
            .clip(shape)
            .background(Color_DOT_SLIDE_DISABLE)
            .clickable {
                onClick.invoke(category)
            }
    ) {
        AsyncImage(
            model = category.icon,
            contentDescription = "categorySelected",
            modifier = Modifier
                .size(96.dp)
                .padding(4.dp)
                .clip(CircleShape)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
    }
}
