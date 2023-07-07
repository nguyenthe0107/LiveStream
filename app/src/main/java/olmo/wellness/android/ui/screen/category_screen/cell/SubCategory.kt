package olmo.wellness.android.ui.screen.category_screen.cell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import olmo.wellness.android.core.Constants
import olmo.wellness.android.domain.model.category.Category
import olmo.wellness.android.ui.theme.*


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SubCategory(
    category: Category,
    visible: Boolean = true,
    onSelected: (categoryId: Int, subCateGoryId: Int) -> Unit,
) {
    val enterExpand = remember {
        expandVertically(animationSpec = tween(Constants.ANIMATION_TIME))
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(Constants.ANIMATION_TIME))
    }
    AnimatedVisibility(
        visible = visible,
        enter = enterExpand,
        exit = exitCollapse
    ) {
        Column {
            category.categories.forEach { subCategory ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = marginOct, start = marginOct),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isChecked = remember { mutableStateOf(subCategory.isSelected) }
                    Checkbox(
                        checked = isChecked.value,
                        onCheckedChange = {
                            isChecked.value = it
                            onSelected(category.id, subCategory.id)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color_Green_Main,
                            uncheckedColor = Neutral,
                            checkmarkColor = White
                        )
                    )
                    Text(
                        text = subCategory.name,
                        style = MaterialTheme.typography.body2,
                        color = Neutral_Gray_9,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}