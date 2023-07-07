package olmo.wellness.android.ui.booking.book_now_livestream.cell

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.CartBooking
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency

@Composable
fun ItemCartBuyer(cartBooking: CartBooking, onSelected: ((CartBooking) -> Unit)? = null) {

    val serviceBooking = cartBooking.serviceBooking
    val isChecked = remember(cartBooking.isChecked) {
        mutableStateOf(cartBooking.isChecked)
    }
    Surface(modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth()
        .height(150.dp),
        shape = RoundedCornerShape(8.dp), elevation = 2.dp,
        color = White) {

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                RoundedAsyncImage(imageUrl = serviceBooking?.photos?.firstOrNull().orEmpty(),
                    cornerRadius = 4.dp,
                    size = 96.dp)
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)) {

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {

                        Text(text = serviceBooking?.name.orEmpty(),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 12.sp, lineHeight = 20.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f))

                        Checkbox(
                            checked = isChecked.value ?: false, onCheckedChange = {
                                cartBooking.isChecked = it
                                isChecked.value = !(isChecked.value ?: false)
                                onSelected?.invoke(cartBooking)
                            }, modifier = Modifier.size(16.dp),
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = White,
                                checkedColor = Color_LiveStream_Main_Color
                            )
                        )
                    }

                    Column(modifier = Modifier.padding(start = 20.dp, top = 12.dp)) {
                        ItemClient(cartBooking)
                    }
                }

            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                var numberClient = 0
                cartBooking.numberOfChild?.let {
                    numberClient += it

                }
                cartBooking.numberOfAdult?.let {
                    numberClient += it
                }

                Text(text = numberClient.toString(), style = MaterialTheme.typography.caption.copy(
                    fontSize = 12.sp, lineHeight = 16.sp
                ), modifier = Modifier.weight(1f))

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Neutral_Gray_9,
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                            )
                        ) {
                            append(stringResource(R.string.lb_total) + "    ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Neutral_Gray_9,
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        ) {
                            append(cartBooking.serviceBooking?.currency ?: ("VND"))
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Color_LiveStream_Main_Color,
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        ) {
                            append(" "+getConvertCurrency(cartBooking.serviceBooking?.startingPrice
                                ?: 0f))
                        }

                    },
                    modifier = Modifier
                )
            }

        }

    }
}

@Composable
private fun ItemClient(cartBooking: CartBooking) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
            val numberOfAdult = cartBooking.numberOfAdult
            if (numberOfAdult != null && numberOfAdult > 0) {
                ItemPerson(numberOfPerson =numberOfAdult, title = "Người lớn", currency = cartBooking.serviceBooking?.currency, price = cartBooking.serviceBooking?.startingPrice)
            }

        val numberOfChild = cartBooking.numberOfChild
        if (numberOfChild != null && numberOfChild > 0) {
            Spacer(modifier = Modifier.height(15.dp))
            ItemPerson(numberOfPerson =numberOfChild, title = "Trẻ em", currency = cartBooking.serviceBooking?.currency, price = cartBooking.serviceBooking?.startingPrice)
        }

    }
}

@Composable
private fun ItemPerson(
    numberOfPerson: Int?,
    title : String,
    currency : String?,
    price : Float?
) {
    Row(modifier = Modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title,
            style = MaterialTheme.typography.caption.copy(fontSize = 10.sp,
                lineHeight = 16.sp),
            modifier = Modifier.Companion.weight(1f))
        Text(text = ("x$numberOfPerson"),
            style = MaterialTheme.typography.caption.copy(fontSize = 10.sp,
                lineHeight = 16.sp),
            modifier = Modifier)
        Text(text = "${currency ?: "VND"} " + getConvertCurrency(price ?: 0f),
            style = MaterialTheme.typography.caption.copy(fontSize = 10.sp,
                lineHeight = 16.sp),
            modifier = Modifier.Companion.weight(1f),
            textAlign = TextAlign.End)
    }
}