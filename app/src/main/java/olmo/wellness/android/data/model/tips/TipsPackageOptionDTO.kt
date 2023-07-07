package olmo.wellness.android.data.model.tips

import olmo.wellness.android.domain.tips.TipsPackageOptionInfo

data class TipsPackageOptionDTO(
    val id: Int,
    val createAt: Long?,
    val lastModified: Long?,
    val name: String?,
    val image: String?,
    val coin: Float,
)

fun TipsPackageOptionDTO.toTipPackageDomain() : TipsPackageOptionInfo{
    return TipsPackageOptionInfo(id = id, createAt = createAt,
        lastModified = lastModified, name = name, image = image, coin = coin)
}