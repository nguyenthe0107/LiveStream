package olmo.wellness.android.domain.model.category

import olmo.wellness.android.data.model.category.dao.SubCategoryEntity

data class SubCategory(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false,
    val icon: String?=null,
    val parentId : Int ?= null
)

fun SubCategory.toSubCategoryEntity() = SubCategoryEntity(id = id, name = name)
