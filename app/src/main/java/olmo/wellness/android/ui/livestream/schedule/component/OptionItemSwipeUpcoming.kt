package olmo.wellness.android.ui.livestream.schedule.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R

@Composable
fun OptionItemSwipeUpcoming(modifier : Modifier,
                            callbackEditItemUpcoming: (() -> Unit) ?= null,
                            callbackSharingItemUpcoming: (() -> Unit) ?= null){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            IconButton(
                onClick = {
                    callbackEditItemUpcoming?.invoke()
                },
                modifier = Modifier
                    .size(25.dp)){
                Icon(
                    painter = painterResource(id = R.drawable.oomo_ic_edit_schedule),
                    contentDescription = "Edit",
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Edit", style = MaterialTheme.typography.subtitle2.copy(
                    color = Color.White
                )
            )
        }
        Spacer(modifier = Modifier
            .width(1.dp)
            .height(88.dp)
            .background(Color.White))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(onClick = {
                callbackSharingItemUpcoming?.invoke()
            },
                modifier = Modifier
                    .size(25.dp)){
                Icon(
                    painter = painterResource(id = R.drawable.olmo_ic_share_filled),
                    contentDescription = "Edit",
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Share", style = MaterialTheme.typography.subtitle2.copy(
                    color = Color.White
                )
            )
        }
    }
}