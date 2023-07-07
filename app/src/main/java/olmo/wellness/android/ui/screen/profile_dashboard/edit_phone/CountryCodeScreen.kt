package olmo.wellness.android.ui.screen.profile_dashboard.edit_phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.country.Country
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.MontserratFont

@Composable
fun CountryCodeScreen(listCountry: List<Country>, onSelectedCallBack : (Country) -> Unit){
    val height = 500.dp
    LazyColumn(modifier = Modifier.fillMaxWidth()
        .requiredHeight(height)
        .background(color = Color.White), state = rememberLazyListState(),  content = {
        items(listCountry.size){ index ->
            ItemCountry(listCountry[index], onSelected = {
                onSelectedCallBack?.invoke(it)
            })
        }
    })
}

@Composable
fun ItemCountry(country: Country, onSelected : (Country) -> Unit){
    Row(modifier = Modifier.padding(16.dp)
        .noRippleClickable {
            onSelected?.invoke(country)
        }.fillMaxWidth().wrapContentHeight(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically){
        Card(shape = RoundedCornerShape(4.dp)){
            val painter =
                rememberImagePainter(data = country.flagIconUrl,
                    builder = {
                        placeholder(R.drawable.ic_baseline_image_24)
                    })
            Image(painter = painter, modifier = Modifier
                .size(25.dp),contentDescription = "", contentScale = ContentScale.FillBounds)
        }
        SpaceHorizontalCompose(width = 20.dp)
        Text(
            text = country.phonePrefix ?: "+84",
            style = MaterialTheme.typography.body1,
            fontFamily = MontserratFont,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}