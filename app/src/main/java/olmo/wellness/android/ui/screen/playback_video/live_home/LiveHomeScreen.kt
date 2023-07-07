package olmo.wellness.android.ui.screen.playback_video.live_home

import android.annotation.SuppressLint
import android.widget.LinearLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.defaultHeightButton
import olmo.wellness.android.ui.theme.space_64

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LiveHomeScreen() {
    Scaffold(
        topBar = {},
        backgroundColor = Color_LiveStream_Main_Color){
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (body, topSectionIcon, bottomBar) = createRefs()
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .constrainAs(body) {
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)
                        height = Dimension.fillToConstraints
                    }
                    .background(Color.Black.copy(alpha = 0.6f))) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black),
                        factory = { ctx ->
                            LinearLayout(ctx).apply {
                            }
                        },
                        update = {}
                    )
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.black_overlay),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(topSectionIcon) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }) {
            }

            Row(
                modifier = Modifier
                    .padding(bottom = defaultHeightButton)
                    .constrainAs(bottomBar) {
                        bottom.linkTo(parent.bottom)
                        centerHorizontallyTo(parent)
                    }
                    .size(space_64),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                val resourceDefault = remember {
                    mutableStateOf((R.drawable.olmo_ic_live_stream))
                }
                Image(
                    painter = painterResource(
                        id = resourceDefault.value
                    ),
                    contentDescription = "Circle Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(space_64)
                        .clip(CircleShape)
                )
            }
        }
    }
}
