package olmo.wellness.android.domain.model.booking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.booking.PhotoDTO
import olmo.wellness.android.domain.model.business.StoreOwner
import java.io.Serializable

data class ServiceBooking(
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
    val locations: List<LocationBooking?>?,

    @SerializedName("photos")
    @Expose
    val photos: List<PhotoBooking?>?=null,
//    val photos: List<String?>?=null,

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
    val storeInfo: StoreOwner?,

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

    val serviceName: String?,

    val servicePhoto: String?,

    val isShowing: Boolean? = false,

    @SerializedName("sessions")
    @Expose
    val sessionInfoDTO: List<ServiceSessionInfo>?= null,

    val tempStamp : Long ?= null

): Serializable

fun ServiceBooking.toServiceBookingForCart() : ServiceBookingForCart{
    return ServiceBookingForCart(
        id = id,
        before24hCancelFeePercent = before24hCancelFeePercent,
        before48hCancelFeePercent = before48hCancelFeePercent,
        categoryId= categoryId,
        createdAt = createdAt,
        description = description,
        duration = duration,
        lastMinuteCancelFeePercent = lastMinuteCancelFeePercent,
        lastModified = lastModified,
        locations = locations,
        reviewMessage = reviewMessage,
        reviewUpdateTimestamp = reviewUpdateTimestamp,
        reviewUserId = reviewUserId,
        serviceTimeZone = serviceTimeZone,
        serviceType = serviceType,
        sessionType = sessionType,
        shortDescription =  shortDescription,
        startingPrice = startingPrice,
        status = status,
        storeId = storeId,
        title = title,
        usageDescription = usageDescription,
        usagePolicy = usagePolicy,
        storeInfo = storeInfo,
        currency = currency,
        durationType = durationType,
        name = name,
        bookmark = bookmark,
        serviceName = serviceName,
        servicePhoto = servicePhoto,
        isShowing = isShowing,
        sessionInfoDTO = sessionInfoDTO,
        tempStamp = tempStamp
    )
}