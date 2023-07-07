package olmo.wellness.android.data.model.booking

import olmo.wellness.android.domain.model.booking.DatePriceInfo

data class DatePriceDTO(
    val startTimestamp : Long?,
    val endTimestamp: Long?,
    val adultPrice: Float?,
    val childPrice: Float?,
    val optionAdultPrice: Float?,
    val optionChildPrice: Float?,
    val typeSession: String?,
    val id: Double?
)

fun DatePriceDTO.toDatePriceDomain() : DatePriceInfo{
    return DatePriceInfo(
        startTimestamp, endTimestamp, adultPrice, childPrice,
        optionAdultPrice, optionChildPrice,
        typeSession, id
    )
}
