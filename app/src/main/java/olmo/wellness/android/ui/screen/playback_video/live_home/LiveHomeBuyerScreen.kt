package olmo.wellness.android.ui.screen.playback_video.live_home

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LiveHomeBuyerScreen(navController: NavController, userType : UserTypeModel?=null) {
    Scaffold(
        topBar = {},
        backgroundColor = Color_LiveStream_Main_Color){
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()){
            val (body, bottomBar) = createRefs()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
                    .constrainAs(body){
                        top.linkTo(parent.top)
                        bottom.linkTo(bottomBar.top)
                        height = Dimension.fillToConstraints
                    }
            ){
                HomeHeader()
                HomeCategoriesList()
            }
        }
    }
}