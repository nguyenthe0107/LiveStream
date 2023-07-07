package olmo.wellness.android.ui.screen.signup_screen.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R

@Composable
fun BottomBarCompose() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_chat), contentDescription = null)
            Text(
                stringResource(id = R.string.tv_chat_center),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.caption.copy(
                    color = Color(0xff303037),
                )
            )
        }

        /*Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = null
            )
            Text(
                stringResource(id = R.string.tv_notification),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.caption.copy(
                    color = Color(0xff303037),
                )
            )
        }*/

        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.ic_hotline), contentDescription = null)
            Text(
                stringResource(id = R.string.tv_hotline),
                modifier = Modifier.padding(top = 7.dp),
                style = MaterialTheme.typography.caption.copy(
                    color = Color(0xff303037),
                )
            )
        }
    }
}