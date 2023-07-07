package olmo.wellness.android.ui.livestream.schedule.cell

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.livestream.schedule.data.ServiceBuy
import olmo.wellness.android.ui.theme.marginStandard
import olmo.wellness.android.ui.theme.padding_10
import olmo.wellness.android.ui.theme.padding_20

@Composable
fun ItemMyService(item: ServiceBuy, onDeleteClick: (ServiceBuy) -> Unit) {
    Column(modifier = Modifier.padding(vertical = marginStandard)) {
        Card(
            modifier = Modifier
                .padding(horizontal = padding_20)
                .fillMaxWidth()
                .height(100.dp),
            elevation = 10.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = padding_10)
                    .noRippleClickable {

                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_business_48),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(65.dp)
                            .clip(CircleShape)
                    )

                    Column(modifier = Modifier.padding(horizontal = marginStandard)) {
                        Text(
                            text = item.name, style = MaterialTheme.typography.h6.copy(
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = "Supplements & Nutrition # Mass",
                            style = MaterialTheme.typography.caption
                        )
                        Text(text = "Available", style = MaterialTheme.typography.caption)
                        Text(
                            text = "From: 10/05/2022",
                            style = MaterialTheme.typography.caption
                        )
                    }
                }


            }
        }
    }
}

@Preview
@Composable
fun Build(){
    ItemMyService(ServiceBuy(1,"Spa")){

    }
}