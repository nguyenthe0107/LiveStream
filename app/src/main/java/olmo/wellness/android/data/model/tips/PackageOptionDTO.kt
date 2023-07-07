package olmo.wellness.android.data.model.tips

import olmo.wellness.android.domain.tips.PackageOptionInfo

data class PackageOptionDTO(
    val id: Int,
    val createAt: Long?,
    val lastModified: Long?,
    val coin: Float,
    val curencyVN: Float,
    val curencyUS: Float
)

fun PackageOptionDTO.toPackageOptionDomain() : PackageOptionInfo {
    return PackageOptionInfo(id = id, createAt = createAt,
        lastModified = lastModified, coin = coin, curencyVN = curencyVN, curencyUS = curencyUS)
}