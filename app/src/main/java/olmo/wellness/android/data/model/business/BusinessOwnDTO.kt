package olmo.wellness.android.data.model.business

import olmo.wellness.android.domain.model.business.BusinessOwn

data class BusinessOwnDTO(
    val businessId: Int,
    val verification1Status: String?,
    val verification2Status: String?,
    val stores: List<StoreDTO> ?= null
)

fun BusinessOwnDTO.toBusinessOwnDomain() = BusinessOwn(
    businessId = businessId,
    verification1Status = verification1Status,
    verification2Status = verification2Status,
    stores = stores
)