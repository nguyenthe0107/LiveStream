package olmo.wellness.android.ui.common.components.err_resubmit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose

@Composable
fun ErrorTitleReSubmitCompose(){
    Row(modifier = Modifier) {
        SpaceHorizontalCompose(width = 2.dp)
        Image(painter = painterResource(id = R.drawable.ic_exclamation_circle_regular), contentDescription = "")
    }
}