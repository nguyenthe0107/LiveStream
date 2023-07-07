package olmo.wellness.android.domain.model.livestream

import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.category.dao.SubCategoryEntity
import olmo.wellness.android.data.model.live_stream.category.NameLocaleX
import olmo.wellness.android.domain.model.category.SubCategory

data class LiveCategory(
    @SerializedName("categories")
    val categories: List<LiveCategory>?=null,
    @SerializedName("color")
    val color: Any?=null,
    @SerializedName("icon")
    val icon: String?=null,
    @SerializedName("id")
    val id: Int?=null,
    @SerializedName("name")
    val name: String?=null,
    @SerializedName("nameLocale")
    val nameLocale: NameLocaleX?=null,
    @SerializedName("parentId")
    val parentId: Int?=null,
    var isSelected: Boolean = false
)

fun LiveCategory.toSubCategoryEntity() = id?.let { name?.let { it1 -> SubCategoryEntity(id = it, name = it1) } }
