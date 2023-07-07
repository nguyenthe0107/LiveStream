package olmo.wellness.android.domain.model.booking

data class DatePriceInfo(
    val startTimestamp : Long?,
    val endTimestamp: Long?,
    val adultPrice: Float?,
    val childPrice: Float?,
    val optionAdultPrice: Float?,
    val optionChildPrice: Float?,
    val typeSession: String?,
    val id: Double?
)
