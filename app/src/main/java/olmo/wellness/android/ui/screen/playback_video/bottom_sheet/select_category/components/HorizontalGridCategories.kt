package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import olmo.wellness.android.core.utils.getScreenHeight
import olmo.wellness.android.core.utils.pxToDp
import olmo.wellness.android.domain.model.livestream.LiveCategory

@Composable
internal fun HorizontalGridCategories(
    data: List<LiveCategory>,
    modifier: Modifier = Modifier,
    selectedIndex: MutableList<Boolean>?= mutableListOf(),
    onCategorySelected : ((List<LiveCategory>) -> Unit) ?= null,
    disableClick : Boolean ?= null){

    val context = LocalContext.current
    val categorySelected = remember {
        mutableStateOf<List<LiveCategory>>(emptyList())
    }
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .heightIn(
                20.dp,
                100.dp
            )
            .padding(
                bottom = 5.dp
            )
    ){
        val totalList : MutableList<LiveCategory> = mutableListOf()
        items(data.size){ index ->
            val category = data[index]
            val isSelected =  if(selectedIndex.isNullOrEmpty()){
                false
            }else{
                selectedIndex[index]
            }
            CategoryLiveStreamItem(category, isSelected,{
                val itemSelected = data[index]
                if(selectedIndex?.isNotEmpty() == true){
                    selectedIndex[index] = it
                }
                totalList.add(itemSelected)
                categorySelected.value = totalList
                onCategorySelected?.invoke(categorySelected.value)
            }, disableClick)
        }
    }
}
