package olmo.wellness.android.data.model.voucher

import olmo.wellness.android.domain.model.voucher.VoucherMetadataInfo

data class VoucherMetadataModelDTO(
    val id: Int?,
    val name: String?,
    val description: String?,
    val image: String?,
    val time: Long?,
    val code: String?,
    val campaign: String?,
    val info: String?,
    val startedAt: Long?,
    val expireAt: Long?,
    val createdAt: Long?,
    val lastModified: Long?,
    val voucher: String?,
    val type: String?,
    val shortDescription: String?,
    val bannerUrl: String?,
    val isPublic: Boolean?,
    val startDate: Long?,
    val endDate: Long?,
    val maxQuantity: Int?,
    val perUserLimit: Int?,
    val active: Boolean?,
    val lastSyncAt: Long?
)

fun VoucherMetadataModelDTO.toVoucherMetadataDomain() : VoucherMetadataInfo{
    return VoucherMetadataInfo(
        id, name, description, image, time, code, campaign, info, startedAt, expireAt,
        createdAt, lastModified, voucher, type, shortDescription, bannerUrl, isPublic,
        startDate, endDate, maxQuantity, perUserLimit, active, lastSyncAt
    )
}


