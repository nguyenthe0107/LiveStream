package olmo.wellness.android.data.model.live_stream.category

import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.livestream.LiveCategory

data class LiveCategoryDTO(
    @SerializedName("categories")
    val categories: List<LiveCategoryDTO>? = null,
    @SerializedName("color")
    val color: Any? = null,
    @SerializedName("icon")
    val icon: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("nameLocale")
    val nameLocale: NameLocaleX? = null,
    @SerializedName("parentId")
    val parentId: Int? = null
)

fun LiveCategoryDTO.toDomain(): LiveCategory {
    return LiveCategory(categories?.map {
        it.toDomain()
    }, color, icon, id, name, nameLocale, parentId)
}