package olmo.wellness.android.data.model.business

import olmo.wellness.android.domain.model.business.StoreOwnerResponseDM

data class StoreOwnerResponse(
    val store: StoreDTO?
)

fun StoreOwnerResponse.toStoreMainDomain() = StoreOwnerResponseDM(
    store = store?.toStoreOwnerDomain()
)
