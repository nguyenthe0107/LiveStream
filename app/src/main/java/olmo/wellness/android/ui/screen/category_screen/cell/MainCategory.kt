package olmo.wellness.android.ui.screen.category_screen.cell

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberImagePainter
import coil.network.HttpException
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.category.Category
import olmo.wellness.android.extension.parseToColor
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import olmo.wellness.android.ui.theme.defaultSizeImage
import olmo.wellness.android.ui.theme.marginHex
import olmo.wellness.android.ui.theme.marginStandard

@Composable
fun MainCategory(
    category: Category,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onSelected: (categoryId: Int, subCateGoryId: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = marginHex, start = marginHex),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onExpandClick()
                }) {
            val painter =
                rememberImagePainter(data = category?.icon,
                    builder = {
                        placeholder(R.drawable.ic_baseline_image_24)
                    })
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(defaultSizeImage)
            )
            Text(
                text = category.name,
                color = category.color?.parseToColor() ?: Neutral_Gray_9,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = marginStandard)
            )
        }
        IconButton(
            onClick = onExpandClick,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_down_24),
                    contentDescription = "Expandable Arrow",
                    modifier = Modifier
                        .rotate(if (expanded) 180f else 0f),
                    tint = Neutral_Gray_9
                )
            }
        )
    }
    SubCategory(category, expanded, onSelected)
}