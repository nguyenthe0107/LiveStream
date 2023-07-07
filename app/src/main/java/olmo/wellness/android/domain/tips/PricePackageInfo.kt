package olmo.wellness.android.domain.tips

data class PricePackageInfo(
    val originPrice: Float,
    val vat : Float,
    val priceVat: Float,
    val totalPrice: Float,
    val timeStamp: Long?=null
)
