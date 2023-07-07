package olmo.wellness.android.data.model.category.dto

import olmo.wellness.android.domain.model.category.Category

data class CategoryDTO(
    val id: Int,
    val name: String,
    val color: String,
    val icon: String,
    val categories: List<SubCategoryDTO>
)

fun CategoryDTO.toCategoryDomain(): Category {
    return Category(id = id,
        name = name,
        color = color,
        icon = icon,
        categories = categories.map { it.toSubCategoryDomain() })
}