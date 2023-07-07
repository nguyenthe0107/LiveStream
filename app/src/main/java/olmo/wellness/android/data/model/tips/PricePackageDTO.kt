package olmo.wellness.android.data.model.tips
import olmo.wellness.android.domain.tips.PricePackageInfo

data class PricePackageDTO(
    val originPrice: Float,
    val vat : Float,
    val priceVat: Float,
    val totalPrice: Float
)

fun PricePackageDTO.toPricePackageInfo(): PricePackageInfo{
    return PricePackageInfo(originPrice = originPrice, vat = vat, priceVat = priceVat, totalPrice = totalPrice)
}
