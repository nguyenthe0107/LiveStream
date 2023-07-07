package olmo.wellness.android.ui.screen.profile_dashboard.social_media

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
@Composable
fun WebViewSocialMediaScreen(
    navController: NavController,
    socialType: SocialType?=null,
    urlWebView:String?=null
){
    var isLoading by remember {
        mutableStateOf(true)
    }
    LoaderWithAnimation(isPlaying = isLoading)
    Column(
        modifier = Modifier
            .background(Color_gray_FF7)
            .padding(marginStandard)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var mUrl = ""
        mUrl = if(socialType != null){
            if(socialType == SocialType.TIKTOK_SOCIAL){
                "https://www.tiktok.com/login"
            }else{
                "https://www.instagram.com/accounts/login/"
            }
        }else{
            urlWebView.orEmpty()
        }
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient().apply {
                    settings.javaScriptEnabled = true
                    settings.javaScriptCanOpenWindowsAutomatically = true
                    settings.domStorageEnabled = true
                }
                loadUrl(mUrl)
                isLoading = false
            }
        }, update = {
            it.loadUrl(mUrl)
            isLoading = false
        })
    }
}

