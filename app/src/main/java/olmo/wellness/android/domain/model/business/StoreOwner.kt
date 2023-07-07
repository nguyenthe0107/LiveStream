package olmo.wellness.android.domain.model.business

import olmo.wellness.android.data.model.business.StoreDTO

data class StoreOwner(
    val id : Int,
    val storeId: Int?=null,
    val isMainStore : Boolean ?= null,
    val storeName: String ?= null,
    val name : String ?= null,
    val ownerUserId : Int ?= null,
    val countryId: Int ?= null,
    val zipCode: String ?= null,
    val cityId: Int ?= null,
    val address: String ?= null,
)

fun StoreDTO.toStoreOwnerDomain() = StoreOwner(
    id = id,
    storeId = storeId,
    isMainStore= isMainStore,
    name = name,
    storeName = storeName,
    ownerUserId = ownerUserId
)

