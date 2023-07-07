package olmo.wellness.android.data.model.booking


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.business.StoreDTO
import olmo.wellness.android.data.model.business.toStoreOwnerDomain
import olmo.wellness.android.domain.model.booking.PhotoBooking
import olmo.wellness.android.domain.model.booking.ServiceBooking
import java.io.Serializable

data class ServiceDTO(
    @SerializedName("before24hCancelFeePercent")
    @Expose
    val before24hCancelFeePercent: Int?,

    @SerializedName("before48hCancelFeePercent")
    @Expose
    val before48hCancelFeePercent: Int?,

    @SerializedName("categoryId")
    @Expose
    val categoryId: Int?,

    @SerializedName("createdAt")
    @Expose
    val createdAt: Long?,

    @SerializedName("description")
    @Expose
    val description: String?,

    @SerializedName("duration")
    @Expose
    val duration: Int?,

    @SerializedName("id")
    @Expose
    val id: Int?,

    @SerializedName("lastMinuteCancelFeePercent")
    @Expose
    val lastMinuteCancelFeePercent: Int?,

    @SerializedName("lastModified")
    @Expose
    val lastModified: Long?,

    @SerializedName("locations")
    @Expose
    val locationDTOS: List<LocationDTO?>?,

    @SerializedName("photos")
    @Expose
    val photos: List<Any?>?,
//    val photos: List<String?>?,

    @SerializedName("reviewMessage")
    @Expose
    val reviewMessage: String?,

    @SerializedName("reviewUpdateTimestamp")
    @Expose
    val reviewUpdateTimestamp: Long?,

    @SerializedName("reviewUserId")
    @Expose
    val reviewUserId: Int?,

    @SerializedName("serviceTimeZone")
    @Expose
    val serviceTimeZone: Int?,

    @SerializedName("serviceType")
    @Expose
    val serviceType: Any?,

    @SerializedName("sessionType")
    @Expose
    val sessionType: String?,

    @SerializedName("shortDescription")
    @Expose
    val shortDescription: Any?,

    @SerializedName("startingPrice")
    @Expose
    val startingPrice: Float?,

    @SerializedName("status")
    @Expose
    val status: String?,

    @SerializedName("storeId")
    @Expose
    val storeId: Int?,

    @SerializedName("title")
    @Expose
    val title: String?,

    @SerializedName("usageDescription")
    @Expose
    val usageDescription: String?,

    @SerializedName("usagePolicy")
    @Expose
    val usagePolicy: String?,

    @SerializedName("storeInfo")
    @Expose
    val storeInfo: StoreDTO?,

    @SerializedName("currency")
    @Expose
    val currency: String?,

    @SerializedName("durationType")
    @Expose
    val durationType: String?,

    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("bookmark")
    @Expose
    val bookmark: Int?,

    @SerializedName("serviceName")
    @Expose
    val serviceName: String?,

    @SerializedName("servicePhoto")
    @Expose
    val servicePhoto: String?,

    @SerializedName("serviceId")
    @Expose
    val serviceId: Int?,

    @SerializedName("sessions")
    @Expose
    val sessionInfoDTO: List<ServiceSessionInfoDTO> ?= null

): Serializable

fun ServiceDTO.toServiceBooking(): ServiceBooking {
    return ServiceBooking(
        before24hCancelFeePercent = before24hCancelFeePercent,
        before48hCancelFeePercent = before48hCancelFeePercent,
        categoryId = categoryId,
        createdAt = createdAt,
        description = description,
        duration = duration,
        id = id,
        lastMinuteCancelFeePercent = lastMinuteCancelFeePercent,
        lastModified = lastModified,
        locations = locationDTOS?.map { it?.toLocationBooking() },
        reviewMessage = reviewMessage, reviewUpdateTimestamp = reviewUpdateTimestamp,
        reviewUserId = reviewUserId,
        serviceTimeZone = serviceTimeZone,
        serviceType = serviceType,
        sessionType = sessionType,
        shortDescription = shortDescription,
        startingPrice = startingPrice,
        status = status,
        storeId = storeId,
        title = title,
        usageDescription = usageDescription,
        usagePolicy = usagePolicy,
        storeInfo = storeInfo?.toStoreOwnerDomain(),
        currency = currency,
        durationType = durationType,
        name = name,
        serviceName = serviceName,
        servicePhoto = servicePhoto,
        bookmark = bookmark,
        photos = photos?.map {
            when (it) {
                is PhotoDTO -> {
                    it.toPhotoBooking()
                }
                is String -> {
                    PhotoBooking(url = it)
                }
                else -> PhotoBooking()
            }
        },
        sessionInfoDTO = sessionInfoDTO?.map {
            it.toServiceSessionDomain()
        }
    )
}