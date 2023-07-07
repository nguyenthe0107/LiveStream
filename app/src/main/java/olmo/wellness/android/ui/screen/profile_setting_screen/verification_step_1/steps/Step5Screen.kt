package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.steps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.ButtonNextStep
import olmo.wellness.android.ui.theme.*

@Composable
fun Step5Screen(onSubmit: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(sizeIcon_40)
                .padding(top = padding_12),
            painter = painterResource(id = R.drawable.ic_check_green),
            contentDescription = "verification step 1 success"
        )
        Text(
            text = stringResource(id = R.string.thank_for_request),
            color = Success_500,
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = padding_20)
        )
        Text(
            text = stringResource(id = R.string.verification_1_desc),
            color = Neutral_Gray_9,
            style = MaterialTheme.typography.body2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(
                start = padding_20,
                end = padding_20,
                bottom = padding_20
            ),
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Neutral_Gray)
                .height(space_120)
        )
    }
    ButtonNextStep(stringResource(R.string.next), onSubmit = onSubmit)
}

