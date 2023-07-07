package olmo.wellness.android.data.model.order

import olmo.wellness.android.domain.model.order.OrderPaymentResponse

data class OrderPaymentResponseDTO(
    val redirectUrl: String,
)

fun OrderPaymentResponseDTO.toOrderPaymentResponseDomain(): OrderPaymentResponse {
    return OrderPaymentResponse(redirectUrl = redirectUrl)
}
