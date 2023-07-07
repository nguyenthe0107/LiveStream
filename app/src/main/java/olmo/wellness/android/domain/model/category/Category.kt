package olmo.wellness.android.domain.model.category

data class Category(
    val id: Int,
    val name: String,
    val color: String?,
    val icon: String?,
    val categories: List<SubCategory>
)
