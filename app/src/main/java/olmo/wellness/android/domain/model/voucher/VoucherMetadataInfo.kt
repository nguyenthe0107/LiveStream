package olmo.wellness.android.domain.model.voucher

data class VoucherMetadataInfo(
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


