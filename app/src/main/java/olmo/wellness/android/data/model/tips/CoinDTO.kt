package olmo.wellness.android.data.model.tips

import olmo.wellness.android.domain.tips.CoinInfo

data class CoinDTO(
    val total: Float,
    val price : Float
)

fun CoinDTO.toCoinDomain() : CoinInfo {
    return CoinInfo(total = total,price= price)
}