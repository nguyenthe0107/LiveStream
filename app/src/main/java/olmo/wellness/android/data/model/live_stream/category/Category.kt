package olmo.wellness.android.data.model.live_stream.category


import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.live_stream.category.NameLocaleX

data class Category(
    @SerializedName("categories")
    val categories: List<Category>?=null,
    @SerializedName("color")
    val color: Any?=null,
    @SerializedName("createdAt")
    val createdAt: Long?=null,
    @SerializedName("icon")
    val icon: String?=null,
    @SerializedName("id")
    val id: Int?=null,
    @SerializedName("lastModified")
    val lastModified: Long?=null,
    @SerializedName("name")
    val name: String?=null,
    @SerializedName("nameLocale")
    val nameLocale: NameLocaleX?=null,
    @SerializedName("parentId")
    val parentId: Int?=null
)