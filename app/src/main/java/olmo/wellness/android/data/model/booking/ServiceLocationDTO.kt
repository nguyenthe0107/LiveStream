package olmo.wellness.android.data.model.booking

import olmo.wellness.android.domain.model.booking.ServiceLocationInfo

data class ServiceLocationDTO(
    val id: Long?,
    val createAt: Long?,
    val lastModified : Long?,
    val serviceId : Int?,
    val title : String?,
    val countryId: Int?,
    val districtId: Int?,
    val wardId: Int?,
    val address: String?,
    val zipCode: String?
)

fun ServiceLocationDTO.toServiceLocationDomain() : ServiceLocationInfo{
    return ServiceLocationInfo(
        id, createAt, lastModified, serviceId, title, countryId, districtId, wardId, address, zipCode
    )
}
