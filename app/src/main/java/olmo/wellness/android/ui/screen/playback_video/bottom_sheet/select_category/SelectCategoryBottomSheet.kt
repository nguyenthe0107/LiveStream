package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.screen.category_screen.BodySectionCategory
import olmo.wellness.android.ui.screen.category_screen.CategoryScreenViewModel
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category.components.GridCategories
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SelectCategoryBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: CategoryScreenViewModel = hiltViewModel(),
    onCategorySelected: ((List<LiveCategory>?) -> Unit)? = null,
    onCancelBottomSheet: (() -> Unit)? = null
) {
    val isLoading = viewModel.isLoading.collectAsState()
    val categoryList = viewModel.categoryList.collectAsState()
    val hasCategoryChoose = viewModel.hasCategoryChoose.collectAsState()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContentColor = Transparent,
        sheetContent = {

            if (modalBottomSheetState.isVisible) {
                Scaffold(modifier = Modifier.fillMaxHeight(0.8f),
                    bottomBar = {
                        GroupButtonBottomCompose(confirmCallback = {
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                            onCategorySelected?.invoke(viewModel.getListSelected())
                        }, cancelCallback = {
                            scope.launch {
                                modalBottomSheetState.hide()
                            }
                            onCancelBottomSheet?.invoke()
                        }, enable = hasCategoryChoose.value)
                    }, topBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .height(60.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.lb_select_categories),
                                style = MaterialTheme.typography.subtitle2.copy(
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(1.dp)
                                    .fillMaxWidth()
                                    .background(color = Neutral_Gray_3)
                                    .align(Alignment.BottomStart)
                            )
                        }
                    }) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            state = lazyListState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                categoryList.value.forEach { category ->
                                    BodySectionCategory(category, viewModel)
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.padding(vertical = 50.dp))
                            }
                        }
                        LoaderWithAnimation(isPlaying = isLoading.value)
                    }
                }
            }else{
                EmptyBottomSheet()
            }
        }
    ) {

    }


}
