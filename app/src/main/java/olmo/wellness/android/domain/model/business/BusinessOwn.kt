package olmo.wellness.android.domain.model.business

import olmo.wellness.android.data.model.business.StoreDTO

data class BusinessOwn(
    val businessId: Int,
    val verification1Status: String?,
    val verification2Status: String?,
    val stores: List<StoreDTO> ?= null
)
