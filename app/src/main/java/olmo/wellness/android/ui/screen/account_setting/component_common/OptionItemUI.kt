package olmo.wellness.android.ui.screen.account_setting.component_common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.account_setting.*
import olmo.wellness.android.ui.screen.account_setting.OptionItemViewData
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.theme.Black_037
import olmo.wellness.android.ui.theme.Color_gray_FF7

@Composable
fun OptionItemUI(
    optionItemViewData: OptionItemViewData,
    showArrow: Boolean = true,
    modifier: Modifier = Modifier) {
    ConstraintLayout(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .noRippleClickable {
                optionItemViewData.onItemClick.invoke()
            }
            .padding(
                horizontal = 16.dp
            ),
    ) {
        val (icon, optionName, arrow, underLine) = createRefs()

        Box(modifier = Modifier
            .background(Color.White, shape = CircleShape)
            .size(24.dp)
            .constrainAs(icon) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        ){
            Image(
                painter = painterResource(id = optionItemViewData.iconSrcResId),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .size(24.dp)
            )
        }

        Text(
            text = optionItemViewData.title,
            style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.Medium,
            ),
            color = Black_037,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(optionName) {
                    start.linkTo(icon.end, 8.dp)
                    end.linkTo(arrow.start)
                    top.linkTo(icon.top)
                    bottom.linkTo(icon.bottom)
                    width = Dimension.fillToConstraints
                },
        )

        if (showArrow) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right_short),
                contentDescription = null,
                tint = Black_037,
                modifier = Modifier
                    .padding(
                        end = 16.dp
                    )
                    .constrainAs(arrow) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

        Box(modifier = Modifier
            .height(16.dp)
            .constrainAs(underLine) {
                top.linkTo(icon.bottom)
                start.linkTo(optionName.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

    }
}