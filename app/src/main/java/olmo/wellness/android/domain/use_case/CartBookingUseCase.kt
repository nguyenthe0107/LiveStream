package olmo.wellness.android.domain.use_case

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.PostRequest
import olmo.wellness.android.data.model.booking.RequestAddCart
import olmo.wellness.android.domain.model.booking.CartBooking
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class CartBookingUseCase @Inject constructor(private val repository: ApiRepository) {
    fun addToCart(rqt: PostRequest<List<RequestAddCart>>): Flow<Result<String>> =
        repository.addToCart(rqt)

    fun getUserCart(fields: String?, page: Int): Flow<Result<List<CartBooking>>> =
        repository.getUserCart(fields = fields, page = page)

    fun deleteUserCart(returning: Boolean=true,fields: String?) : Flow<Result<String>> =
        repository.deleteToCart(returning = returning, fields = fields)
}