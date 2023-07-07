package olmo.wellness.android.data.model.verification_1.step1

import olmo.wellness.android.domain.model.verification1.step1.Address

data class AddressDTO(
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
    val address: String ?= null
)

fun AddressDTO.toAddressDomain() = Address(
    id = id,
    businessId = businessId,
    countryId = countryId,
    zipCode = zipCode,
    city = city,
    district = district,
    addressLine1 = addressLine1,
    addressLine2 = addressLine2,
    districtId = districtId,
    cityId = cityId,
    address = address
)
