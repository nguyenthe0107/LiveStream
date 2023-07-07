package olmo.wellness.android.util

import java.text.DecimalFormat
import java.text.NumberFormat


fun getConvertCurrency(currency: Float): String {
    val money = currency.toInt()
    val formatter: NumberFormat = DecimalFormat("#,###")
    return formatter.format(money)
}


fun getConvertKCurrency(currency: Float?): String{
    currency?.let {
        val money = currency/1000.toInt()
        val formatter: NumberFormat = DecimalFormat("#,###")
        return formatter.format(money)+"K"
    }
    return ""

}