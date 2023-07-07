package olmo.wellness.android.data.model.booking

import olmo.wellness.android.domain.model.booking.ServiceSessionInfo

data class ServiceSessionInfoDTO(
    val id: Int?=null,
    val serviceId: Int?=null,
    val locationId: Long?=null,
    val sessionTimestamp: Long? = null,
    val title: String ?= null,
    val total: Double?= null,
    val adultPrice: Float?= null,
    val childPrice : Float?= null,
    val optionAdultPrice: Float?= null,
    val optionChildPrice: Float?= null,
    val numberOfAdult: Int?= null,
    val numberOfChild: Int?= null,
    val numberOfOptionalAdult: Int?= null,
    val numberOfOptionalChild: Int?= null,
    val status: String?=null,
    val sessionDuration: Long?=null,
    val sessionType: String?=null,
    val notes: String?=null,
    val serviceInfo: ServiceDTO?=null,
)

fun ServiceSessionInfoDTO.toServiceSessionDomain() : ServiceSessionInfo {
    return ServiceSessionInfo(
        id = id,
        serviceId = serviceId,
        locationId = locationId,
        sessionTimestamp = sessionTimestamp,
        title = title,
        total = total,
        adultPrice = adultPrice,
        childPrice = childPrice,
        optionAdultPrice = optionAdultPrice,
        optionChildPrice = optionChildPrice,
        numberOfAdult = numberOfAdult,
        numberOfChild = numberOfChild,
        numberOfOptionalAdult = numberOfOptionalAdult,
        numberOfOptionalChild = numberOfOptionalChild,
        status = status,
        sessionDuration = sessionDuration,
        sessionType = sessionType,
        notes = notes,
        serviceInfo = serviceInfo?.toServiceBooking()
    )
}