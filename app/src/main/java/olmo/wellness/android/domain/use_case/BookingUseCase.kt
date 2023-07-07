package olmo.wellness.android.domain.use_case

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.booking.RequestCancelBooking
import olmo.wellness.android.domain.model.booking.*
import olmo.wellness.android.domain.model.voucher.VoucherInfo
import olmo.wellness.android.domain.model.voucher.VoucherValidateInfo
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class BookingUseCase @Inject constructor(private val repository: ApiRepository) {

    fun getServices(fields : String?,search : String?=null,page : Int?=null) : Flow<Result<List<ServiceBooking>>> = repository.getService(fields,search,page)

    fun getServicesLivestream(livestreamId : Int?) : Flow<Result<List<ServiceBooking>>> = repository.getServiceLivestream(livestreamId)

    fun deleteServiceLiveStream(userId : Int?, livestreamId: Int?,serviceId: Int?) : Flow<Result<String>> = repository.deleteServiceLivestream(userId, livestreamId, serviceId)

    fun getVoucherList(userId : Int?=null) : Flow<Result<List<VoucherInfo>>> = repository.getListVoucher(userId)

    fun getValidateVoucher(voucherCode: String?=null, sessionConfirmId: Double?=null) : Flow<Result<VoucherValidateInfo>> =
        repository.getValidateVoucher(voucherCode = voucherCode, sessionConfirmId = sessionConfirmId)

    fun getListBookingBuyer(
        bookingTitle: String?=null,
        bookingId: Int?=null,
        page: Int?,
        limit: Int?
    ): Flow<Result<List<BookingHistoryInfo>>> = repository.getListBookingBuyer(
        bookingTitle = bookingTitle,
        bookingId = bookingId,
        page = page, limit = limit
    )

    fun getListBookingSeller(
        bookingTitle: String?=null,
        bookingId: Int?=null,
        page: Int?,
        limit: Int?
    ): Flow<Result<List<BookingHistoryInfo>>> = repository.getListBookingSeller(
        bookingTitle = bookingTitle,
        bookingId = bookingId,
        page = page, limit = limit
    )

    fun getServicePublicById(
        id: Int?
    ): Flow<Result<ServiceBookingForCart>> = repository.getServicePublicById(
        id = id
    )

    fun getServiceLocation(
        query: String?
    ): Flow<Result<List<ServiceLocationInfo>>> = repository.getServiceLocation(
        query = query
    )

    fun getServiceCalendar(fromDate: Long, toDate: Long, serviceId: Int) : Flow<Result<List<DatePriceInfo>>> = repository.getServiceCalendar(
        fromDate,
        toDate,
        serviceId
    )

    fun getServiceSessionByDate(fromDate: Long,serviceId: Int, id: Double, typeSession: String) : Flow<Result<TimeBookingInfo>> = repository.getServiceSessionByDate(
        fromDate,
        serviceId,
        id,
        typeSession
    )

    fun postServiceSessionConfirm(bodyRequest: List<ServiceSessionInfo>) : Flow<Result<ServiceSessionConfirmInfo>> = repository.postServiceSessionConfirm(
        bodyRequest
    )

    fun createBookingId(bodyRequest: BookingRequestInfo): Flow<Result<BookingIdResponseInfo>> = repository.createBookingId(
        bodyRequest
    )

    fun requestCancelBooking(bodyRequest: RequestCancelBooking) : Flow<Result<Boolean>> = repository.requestDeleteServiceSessionConfirm(bodyRequest)

    fun requestRescheduleBooking(bodyRequest: RequestCancelBooking) : Flow<Result<Boolean>> = repository.requestRescheduleServiceSessionConfirm(bodyRequest)

}