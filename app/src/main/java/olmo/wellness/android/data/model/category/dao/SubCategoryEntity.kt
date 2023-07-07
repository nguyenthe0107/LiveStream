package olmo.wellness.android.data.model.category.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import olmo.wellness.android.domain.model.category.SubCategory

@Entity(tableName = "sub_category_table")
data class SubCategoryEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)

fun SubCategoryEntity.toSubCategoryDomain(): SubCategory {
    return SubCategory(id = id, name = name)
}
