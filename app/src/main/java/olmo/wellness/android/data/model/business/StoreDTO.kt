package olmo.wellness.android.data.model.business
import olmo.wellness.android.domain.model.business.StoreOwner

data class StoreDTO(
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
    isMainStore = isMainStore,
    storeName = storeName,
    name = name,
    ownerUserId = ownerUserId,
    countryId = countryId,
    zipCode = zipCode,
    cityId = cityId,
    address = address
)