package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.*

@Composable
fun CreditPaymentSelected(onChange: (String) -> Unit) {
    var paymentSelected by remember { mutableStateOf("credit_card") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = marginDouble, vertical = marginStandard),){
        Column(
            Modifier.padding(vertical = marginStandard)){
            Text(
                text = stringResource(id = R.string.payment_method),
                color = Neutral_Gray_7,
                style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = marginMinimum)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        paymentSelected = "paypal"
                        onChange(paymentSelected)
                    }
                    .padding(top = marginStandard),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_paypal_payment),
                    contentDescription = null,
                    modifier = Modifier
                        .width(width_72)
                        .height(height_44)
                        .padding(end = marginMinimum)
                )
            }
        }
    }
}