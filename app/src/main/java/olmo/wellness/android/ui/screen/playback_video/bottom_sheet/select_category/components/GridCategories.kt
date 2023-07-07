package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import olmo.wellness.android.core.utils.getScreenHeight
import olmo.wellness.android.core.utils.pxToDp
import olmo.wellness.android.domain.model.livestream.LiveCategory

@Composable
internal fun GridCategories(
    data: List<LiveCategory>?,
    modifier: Modifier = Modifier,
    selectedIndex: MutableList<Boolean>?= mutableListOf(),
    onCategorySelected : ((List<LiveCategory>) -> Unit) ?= null,
    disableClick : Boolean ?= null){

    val context = LocalContext.current
    val categorySelected = remember {
        mutableStateOf<List<LiveCategory>>(emptyList())
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .heightIn(
                20.dp,
                (getScreenHeight() * 0.5)
                    .toInt()
                    .pxToDp(context).dp
            )
            .padding(
                bottom = 20.dp
            )
    ){
        val totalList : MutableList<LiveCategory> = mutableListOf()
        data?.size?.let {
            items(it){ index ->
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
}
