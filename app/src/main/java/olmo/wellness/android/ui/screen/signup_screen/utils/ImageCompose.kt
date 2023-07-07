package olmo.wellness.android.ui.screen.signup_screen.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import olmo.wellness.android.BuildConfig
import olmo.wellness.android.R
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.theme.MontserratFont

@Preview
@Composable
fun ImageCompose(){
    val image = painterResource(id = R.drawable.ic_logo)
    Column(modifier = Modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(image, contentDescription = "", contentScale = ContentScale.Crop)
        if(provideAppVersion().isNotEmpty()){
            Text(text = provideAppVersion(), style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Bold
            ))
        }
    }
}

fun provideAppVersion() : String{
    val configAppModel = sharedPrefs.getConfigApp()
    var apiUrl : String = ""
    val appNumber = BuildConfig.VERSION_CODE
    apiUrl = when {
        appNumber >= configAppModel.android?.dev ?: 0 -> {
            "Version Code " + BuildConfig.VERSION_CODE
        }
        appNumber >= configAppModel.android?.staging ?: 0 && appNumber < configAppModel.android?.dev ?: 0  -> {
            "Version Code " + BuildConfig.VERSION_CODE
        }
        else -> {
            ""
        }
    }
    return apiUrl
}