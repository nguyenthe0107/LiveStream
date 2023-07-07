package olmo.wellness.android.domain.tips

data class TipsPackageOptionInfo(
    val id: Int,
    val createAt: Long?=null,
    val lastModified: Long?=null,
    val name: String?=null,
    val image: String?=null,
    val coin: Float?=null,
    val keplerPrice: Float?=null,
    val moneyVND: Float?=null,
    val isSelected : Boolean?= false
)

