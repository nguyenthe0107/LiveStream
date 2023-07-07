package olmo.wellness.android.ui.common.components.err_resubmit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.defination.StepType
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun ErrorSectionResubmitCompose(steps: List<StepType>? = emptyList()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = marginDouble,
                end = marginDouble,
                top = padding_12,
                bottom = padding_12
            )
            .background(color = ERROR_BACKGROUND_RESUBMIT)
            .border(
                color = Color.Transparent,
                width = 2.dp,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(start = marginDouble,
                end = marginDouble,
                top = padding_12,
                bottom = padding_12),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = R.drawable.ic_error_validate), contentDescription = "")
        SpaceHorizontalCompose(width = marginStandard)
        if(steps != null && steps.isNotEmpty()){
            var contentErr = ""
            val content1 = "Error Business information, "
            val content2 = "Seller information, "
            val content3 = "Billing information, "
            val content4 = "Store information, "
            steps.map { stepType ->
                contentErr = when(stepType){
                    StepType.STEP1 -> {
                        contentErr.plus(content1)
                    }
                    StepType.STEP2 -> {
                        contentErr.plus(content2)
                    }
                    StepType.STEP3 -> {
                        contentErr.plus(content3)
                    }
                    StepType.STEP4 -> {
                        contentErr.plus(content4)
                    }
                }
            }
            Text(
                text = contentErr,
                style = MaterialTheme.typography.subtitle2.copy(
                    color = Neutral_Gray_9, fontFamily = MontserratFont,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Medium,
                )
            )
        }

    }
}