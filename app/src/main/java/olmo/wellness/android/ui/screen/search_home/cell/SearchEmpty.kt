package olmo.wellness.android.ui.screen.search_home.cell

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SearchEmpty(isVisible : Boolean) {
    if (isVisible){
        Box(modifier = Modifier.fillMaxSize()){
            Text(text = "Empty", modifier = Modifier.align(Alignment.Center))
        }
    }
}