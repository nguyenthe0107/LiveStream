package olmo.wellness.android.data.model.category.dto

import olmo.wellness.android.domain.model.category.SubCategory

data class SubCategoryDTO(
    val id: Int,
    val name: String,
    val icon: String,
    val parentId: Int ?= null
)

fun SubCategoryDTO.toSubCategoryDomain(): SubCategory {
    return SubCategory(
        id = id,
        name = name,
        icon = icon,
        parentId = parentId,
        isSelected = false
    )
}