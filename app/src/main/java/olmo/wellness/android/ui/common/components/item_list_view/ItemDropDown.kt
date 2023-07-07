package olmo.wellness.android.ui.common.components.item_list_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.TextStyleBlack
import olmo.wellness.android.ui.theme.Neutral_Gray_3

@Composable
fun ItemDropDown(iconLeftResource: Int = R.drawable.ic_flag,
                 iconRightResource: Int = R.drawable.ic_meu_down) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 22.dp, top = 16.dp, bottom = 16.dp)
            .border(
                width = 1.dp,
                color = Neutral_Gray_3,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(start = 19.dp, end = 19.dp, top = 16.dp, bottom = 16.dp)
    ) {

        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = iconLeftResource), contentDescription = "")
            SpaceHorizontalCompose(width = 11.dp)
            TextStyleBlack(content = "My Product")
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = iconRightResource), contentDescription = "")
        }
    }
}