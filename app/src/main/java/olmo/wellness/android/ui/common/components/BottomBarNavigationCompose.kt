package olmo.wellness.android.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import olmo.wellness.android.ui.theme.MontserratFont
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.Neutral_Gray_5

@Composable
fun BottomBarNavigationCompose(modifier: Modifier = Modifier,navController: NavController){
    Row(modifier = Modifier.fillMaxWidth()) {
        BottomAppBar(cutoutShape = RoundedCornerShape(50)) {
            val styleForText = MaterialTheme.typography.overline.copy(
                fontFamily = MontserratFont, color = Color.White
            )
            val sizeIcon = 25.dp
            BottomNavigation(modifier = modifier.fillMaxWidth(),
                backgroundColor = Neutral_Gray_5) {
                BottomNavigationItem(
                    icon = {
                        Image(painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "", Modifier.size(sizeIcon))
                    },
                    label = { Text(text = "Home", style = styleForText)},
                    selected = false,
                    onClick = {
                    },
                    alwaysShowLabel = true
                )

                BottomNavigationItem(
                    icon = {
                        Image(painter = painterResource(id = R.drawable.ic_feeds),
                            contentDescription = "", Modifier.size(sizeIcon))
                    },
                    label = { Text(text = "For You", style = styleForText)},
                    selected = false,
                    onClick = {},
                    alwaysShowLabel = true
                )

                BottomNavigationItem(
                    icon = {
                        Image(painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = "", Modifier.size(sizeIcon))
                    },
                    label = { Text(text = "Calendar", style = styleForText)},
                    selected = false,
                    onClick = {},
                    alwaysShowLabel = true
                )

                BottomNavigationItem(
                    icon = {
                        val imageVector = ImageVector.vectorResource(
                            id = R.drawable.ic_profile
                        )
                        Icon(imageVector ,"", Modifier.size(sizeIcon))
                    },
                    label = { Text(text = "Profile", style = styleForText)},
                    selected = false,
                    onClick = {},
                    alwaysShowLabel = true
                )
            }
        }
    }
}