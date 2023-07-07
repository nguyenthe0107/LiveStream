package olmo.wellness.android.data.model.profile.business_address
class BusinessUpdateDTO(
    val id: List<Int>?=null,
    val businessId: List<Int?>?=null,
    val userId: List<Int?>?=null,
    val countryId: Int ?= null,
    val zipCode: String ?= null,
    val city: String ?= null,
    val district: String ?= null,
    val addressLine1: String ?= null,
    val addressLine2: String? = null,
)
