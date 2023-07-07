package olmo.wellness.android.domain.model.verification1.step1

import olmo.wellness.android.data.model.verification_1.step1.AddressDTO

data class Address(
    val id: Int?=null,
    val businessId: Int?=null,
    val countryId: Int ?= null,
    val zipCode: String ?= null,
    val city: String ?= null,
    val district: String ?= null,
    val addressLine1: String ?= null,
    val addressLine2: String? = null,
    val districtId : Int ?= null,
    val cityId : Int ?= null,
    val address: String ?= null,
    val isDefaultAddress : Boolean ?= null,
    val isPickupAddress : Boolean ?= null,
    val isReturnAddress : Boolean ?= null,
    val phoneCountryId: Int ?= null,
    val label : String ?= null,
    val fullName: String ?= null,
    val timeStamp: Long ?= null,
)

fun Address.toAddressDTO() = AddressDTO(
    id = id,
    businessId = businessId,
    countryId = countryId,
    zipCode = zipCode,
    city = city,
    district = district,
    addressLine1 = addressLine1,
    addressLine2 = addressLine2,
    address = address,
    districtId = districtId
)
