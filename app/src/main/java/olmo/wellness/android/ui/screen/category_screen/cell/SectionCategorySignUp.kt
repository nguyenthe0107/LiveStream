package olmo.wellness.android.ui.screen.category_screen.cell

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.core.utils.getScreenWidth
import olmo.wellness.android.core.utils.pxToDp
import olmo.wellness.android.domain.model.category.Category
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun SectionCategorySignUp(
    category: LiveCategory,
    subCateGory: LiveCategory,
    expanded: Boolean,
    onSelected: ((categoryId: Int, subCateGoryId: Int) -> Unit)? = null
) {
    SubCategoryItem(category, subCateGory, expanded, onSelected)
}

@Composable
fun SubCategoryItem(
    category: LiveCategory,
    item: LiveCategory,
    selectedIndex: Boolean,
    onItemClick: ((categoryId: Int, subCateGoryId: Int) -> Unit)? = null
) {
    var isSelected by rememberSaveable {
        mutableStateOf(selectedIndex)
    }
    if (item.isSelected) {
        isSelected = true
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .size(width = (getScreenWidth() / 4).pxToDp(context).dp, height = 125.dp)
            .wrapContentSize()
            .noRippleClickable {
                isSelected = !isSelected
                category.id?.let { item.id?.let { it1 -> onItemClick?.invoke(it, it1) } }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textColor = if (isSelected) Neutral_Gray_9 else Neutral_Gray_9
        val shadowColor = if (isSelected) Color_Purple_7F4 else Color.Gray
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = shadowColor,
                    ambientColor = shadowColor
                ),
            color = White
        ) {
            Box(
                Modifier
                    .size(80.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = item.icon ?: "",
                    contentDescription = "",
                    error = painterResource(R.drawable.olmo_ic_group_default_place_holder),
                    modifier = Modifier
                        .size(80.dp)
                        .padding(20.dp)
                        .clip(CircleShape).align(Alignment.Center),
                    contentScale = ContentScale.Inside
                )
            }
            if (isSelected){
                Image(painter = painterResource(id = R.drawable.ic_category_select), contentDescription = "select",
                modifier = Modifier.size(80.dp).clip(CircleShape))
            }

        }

        SpaceCompose(height = 16.dp)

        Text(
            text = item.nameLocale?.en?:"",
            maxLines = 2,
            overflow = TextOverflow.Clip,
            style = MaterialTheme.typography.body2.copy(
                color = textColor,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}