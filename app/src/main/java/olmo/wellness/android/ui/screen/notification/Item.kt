package olmo.wellness.android.ui.screen.notification

data class Item(
    val id: Int,
    val title: String,
    val subtitle: String,
    val imageId: Int,
    val source: String = "demo source"
)