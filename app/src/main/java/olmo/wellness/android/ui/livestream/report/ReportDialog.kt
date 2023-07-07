package olmo.wellness.android.ui.livestream.report

import android.annotation.SuppressLint
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDialog(
    liveStreamId: Int?,
    viewModel: ReportVideoViewModel = hiltViewModel(),
    onConfirm: ((Boolean) -> Unit)? = null
){
    val listSelected = mutableListOf<String>()
    val listSection by remember {
        mutableStateOf(listSelected)
    }
    val isLoading = viewModel.isLoading.collectAsState()
    val categoriesStates = viewModel.reportTypeList.collectAsState()
    val categories = categoriesStates.value
    viewModel.bindLiveStreamId(liveStreamId)
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                viewModel.getReportList()
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            else -> {
            }
        }
    }
    Box(
        modifier = Modifier
            .background(Neutral_Gray_2)
    ){
        Column(
            modifier = Modifier
                .background(Neutral_Gray_2)
                .padding(bottom = 72.dp)
                .fillMaxHeight(0.8f), verticalArrangement = Arrangement.Top
        ){
            Row(
                modifier = Modifier
                    .background(color = White)
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.title_text_report),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        color = Neutral_Gray_9
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .background(color = Neutral_Gray_2)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.title_select_reason),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 18.sp,
                            lineHeight = 26.sp,
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ){
                    categories.forEach { subCategory ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val isChecked = remember { mutableStateOf(subCategory.isSelected) }
                            Text(
                                text = subCategory.reportType.value,
                                style = MaterialTheme.typography.body2,
                                color = Neutral_Gray_9,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                            )
                            androidx.compose.material.Checkbox(
                                checked = isChecked.value ?: false,
                                onCheckedChange = {
                                    isChecked.value = it
                                    listSection.add(subCategory.reportType.value)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color_LiveStream_Main_Color,
                                    uncheckedColor = Neutral,
                                    checkmarkColor = White
                                )
                            )
                        }
                    }
                }
            }
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Neutral_Gray_2)
                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp, top = 30.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    PrimaryLiveButton(
                        enable = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 6.dp),
                        text = stringResource(id = R.string.submit),
                        onClickFunc = {
                            viewModel.onSubmit(listSection)
                            onConfirm?.invoke(true)
                        }
                    )
                }
            }
        }
        LoadingScreen(isLoading = isLoading.value)
    }
}