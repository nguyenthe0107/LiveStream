package olmo.wellness.android.domain.tips

data class PackageOptionInfo(
    val id: Int,
    val createAt: Long?=null,
    val lastModified: Long?=null,
    val coin: Float?=null,
    val isSelected : Boolean?= false,
    val curencyVN: Float,
    val curencyUS: Float,
    val option: String = "KP_COIN"
)
