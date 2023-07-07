package olmo.wellness.android.domain.model.booking

import olmo.wellness.android.data.model.booking.ServiceSessionInfoDTO

data class ServiceSessionInfo(
    val id: Int?=null,
    val serviceId: Int?=null,
    val locationId: Long?=null,
    val sessionTimestamp: Long? = null,
    val title: String ?= null,
    val total: Double?=null,
    val adultPrice: Float?=null,
    val childPrice : Float?=null,
    val optionAdultPrice: Float?=null,
    val optionChildPrice: Float?=null,
    val numberOfAdult: Int?=null,
    val numberOfChild: Int?=null,
    val numberOfOptionalAdult: Int?=null,
    val numberOfOptionalChild: Int?=null,
    val status: String?=null,
    val sessionDuration: Long?=null,
    val sessionType: String?=null,
    val notes: String?=null,
    val type: String ?= null,
    val serviceInfo: ServiceBooking?=null,
)

fun ServiceSessionInfo.toServiceSessionDTO() : ServiceSessionInfoDTO{
    return ServiceSessionInfoDTO(
        id, serviceId, locationId, sessionTimestamp, title, total, adultPrice, childPrice,
        optionAdultPrice, optionChildPrice, numberOfAdult, numberOfChild, numberOfOptionalAdult,
        numberOfOptionalChild
    )
}