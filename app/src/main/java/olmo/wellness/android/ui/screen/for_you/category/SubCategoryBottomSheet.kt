package olmo.wellness.android.ui.screen.for_you.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.ui.screen.playback_video.home.ViewItemCategory
import olmo.wellness.android.ui.theme.Neutral_Gray_3
import olmo.wellness.android.ui.theme.Transparent
import olmo.wellness.android.ui.theme.White

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubCategoryBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    titleCategory : String?,
    listCategories: List<LiveCategory>?,
    onClick: (LiveCategory) -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = White, shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .fillMaxHeight(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = titleCategory?:"", style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 18.sp
                    ), modifier = Modifier.padding(vertical = 20.dp)
                )
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(color = Neutral_Gray_3)
                )

                if (!listCategories.isNullOrEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.padding(top = 15.dp)
                    ) {
                        items(listCategories.size, key = {
                            it.toString()
                        }) { index ->
                            val item = listCategories[index]
                            ViewItemCategory(
                                category = item,
                                modifier = Modifier.padding(bottom = 25.dp),
                                circleSize = 80.dp,
                                onViewAllClick = {
                                    onClick.invoke(it)
                                })
                        }
                    }
                }
            }
        }) {
    }
}