package olmo.wellness.android.data.model.voucher

import olmo.wellness.android.domain.model.voucher.VoucherInfo

data class VoucherModelDTO(
    val id: Int?,
    val createdAt: Long?,
    val lastModified: Long?,
    val voucher: String?,
    val type: String?,
    val shortDescription: String?,
    val description: String?,
    val bannerUrl: String?,
    val storeIds: List<Int>?,
    val serviceIds: List<Int>?,
    val reductionType: String?,
    val reductionLimit: Int?,
    val isPublic: Boolean?,
    val startDate: Long?,
    val endDate: Long?,
    val maxQuantity: Int?,
    val perUserLimit: Int?,
    val expireAt: Long?,
    val reductionAmount: Float?,
    val active: Boolean?,
    val lastSyncAt: Long?,
    val redeemedVoucher: Float?,
    val voucherDbId: Double?
)

fun VoucherModelDTO.toVoucherDomain() = VoucherInfo(
    id = id,
    description = description,
    expireAt = expireAt,
    createdAt = createdAt,
    lastModified = lastModified,
    voucher = voucher,
    type = type,
    shortDescription = shortDescription,
    bannerUrl = bannerUrl,
    storeIds = storeIds,
    serviceIds = serviceIds,
    reductionAmount = reductionAmount,
    reductionLimit = reductionLimit,
    reductionType = reductionType,
    isPublic = isPublic,
    startDate = startDate,
    endDate = endDate,
    maxQuantity = maxQuantity,
    perUserLimit = perUserLimit,
    active = active,
    lastSyncAt = lastSyncAt,
    redeemedVoucher = redeemedVoucher,
    voucherDbId = voucherDbId
)

