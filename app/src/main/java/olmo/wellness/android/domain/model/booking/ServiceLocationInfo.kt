package olmo.wellness.android.domain.model.booking

data class ServiceLocationInfo(
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
