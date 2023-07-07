package olmo.wellness.android.ui.screen.category_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.utils.getScreenHeight
import olmo.wellness.android.core.utils.pxToDp
import olmo.wellness.android.domain.model.category.Category
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.ui.screen.category_screen.cell.SectionCategorySignUp
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.theme.*
private const val CELL_COUNT = 3
@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun CategoryScreen(
    modalBottomSheetState: ModalBottomSheetState,
    onSelectedCategory : ((Boolean) -> Unit) ?= null,
    onConfirm : (()->Unit)?=null,
    onCancel :(() ->Unit)?=null,
    viewModel: CategoryScreenViewModel = hiltViewModel()
){
    val categoryList = viewModel.categoryList.collectAsState()
    val hasCategoryChoose = viewModel.hasCategoryChoose.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier
        .background(White)){
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .background(White)
                .fillMaxHeight(0.86f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = stringResource(R.string.text_choose_category),
                        style = MaterialTheme.typography.h6.copy(
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        ),
                        overflow = TextOverflow.Ellipsis,
                        color = Neutral_Gray_9,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 18.dp,
                            bottom = 12.dp
                        )
                    )

                    Text(
                        text = stringResource(R.string.text_title_category),
                        style = MaterialTheme.typography.subtitle2.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Ellipsis,
                        color = Neutral_Gray_9,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                    )
                }
            }

            item {
                categoryList.value.forEach { category ->
                    BodySectionCategory(category,viewModel)
                }
            }

            item {
                GroupButtonBottomCompose(confirmCallback = {
                    viewModel.onNextButtonClicked()
                    onSelectedCategory?.invoke(true)
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                }, cancelCallback = {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                })
            }
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            GroupButtonBottomCompose(confirmCallback = {
                viewModel.onNextButtonClicked()
                onSelectedCategory?.invoke(true)
                scope.launch {
                    modalBottomSheetState.hide()
                }
            }, cancelCallback = {
                scope.launch {
                    modalBottomSheetState.hide()
                }
            })
        }
    }
}

@Composable
fun BodySectionCategory(
    category: LiveCategory,
    viewModel: CategoryScreenViewModel,
    modifier: Modifier = Modifier,
){
    val sectionData = remember(category) {
        category
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                bottom = 16.dp
            )
    ){
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        SectionHeaderCategory(
            sectionName = sectionData.nameLocale?.en?:"",
            modifier = Modifier.padding(
                top = 16.dp
            )
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(1),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .heightIn(
                    80.dp,
                    125.dp
                )
                .padding(
                    top = 8.dp
                )
        ){
            items(sectionData.categories?.size?:0, key = { it }){ index ->
                sectionData.categories?.get(index)?.apply {
                    SectionCategorySignUp(
                        category,
                        this,
                        false,
                        onSelected = { categoryId, subCateGoryId ->
                            viewModel.onSubCategoryClicked(categoryId, subCateGoryId)
                        })
                }
            }
        }
    }
}

@Composable
fun SectionHeaderCategory(
    modifier: Modifier = Modifier,
    sectionName: String,
    color: Color ?= null,
    fontSize : Int? = null
){
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
            )
    ){
        Text(
            text = sectionName,
            style = MaterialTheme.typography.subtitle1.copy(
                color = color ?: Color_Purple_FBC,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
                fontSize = fontSize?.sp ?: 14.sp
            )
        )
    }
}
