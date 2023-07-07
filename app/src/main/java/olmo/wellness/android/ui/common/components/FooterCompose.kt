package olmo.wellness.android.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.MontserratFont
import olmo.wellness.android.ui.theme.Neutral_Gray_9

@Composable
fun FooterCompose(
    onGoogleClick: (() -> Unit) ?= null,
    onFacebookCLick: (() -> Unit)? = null,
    onNavigation: (() -> Unit)? = null) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.padding(top = 20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Text(
                text = " Or ", style = TextStyle(
                    color = Neutral_Gray_9,
                    fontFamily = MontserratFont
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Card(
                elevation = 1.dp,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .size(48.dp)
            ) {
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            onGoogleClick?.invoke()
                        },
                    painter = painterResource(id = R.drawable.olmo_ic_google_purple),
                    contentDescription = null
                )
            }
            Card(
                elevation = 1.dp,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .size(48.dp)
            ) {
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                        },
                    painter = painterResource(id = R.drawable.olmo_ic_tiktok_purple),
                    contentDescription = null
                )
            }
            Card(
                elevation = 1.dp,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .size(48.dp)
            ) {
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onFacebookCLick?.invoke() },
                    painter = painterResource(id = R.drawable.olmo_ic_instagram_purple),
                    contentDescription = null
                )
            }
        }
    }
}