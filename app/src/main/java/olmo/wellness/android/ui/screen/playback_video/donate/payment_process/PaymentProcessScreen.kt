package olmo.wellness.android.ui.screen.playback_video.donate.payment_process

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.core.Constants
import olmo.wellness.android.domain.model.booking.WrapperUrlPayment
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.Color_Purple_FBC
import olmo.wellness.android.ui.theme.Color_gray_FF7

@SuppressLint("NewApi", "SetJavaScriptEnabled", "UnusedMaterialScaffoldPaddingParameter",
    "StateFlowValueCalledInComposition"
)
@Composable
fun PaymentProcessScreen(wrapperUrlPayment: WrapperUrlPayment ?= null,
                         navController: NavController,
                         viewModel: PaymentProcessViewModel = hiltViewModel()
){
    var isLoading by remember {
        mutableStateOf(true)
    }
    val urlPayment = viewModel.urlPayment.value
    BackHandler(enabled = true){
        navController.popBackStack()
    }
    val isBooking = wrapperUrlPayment?.bookingId != null
    Scaffold(
        backgroundColor = Color_Purple_FBC,
        topBar = {
            DetailTopBar(
                title = "Payment",
                navController = navController,
                onBackStackFunc = {
                    navController.popBackStack()
                }
            )
        }){
        Box(
            modifier = Modifier
                .background(Color_gray_FF7)
                .fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .background(Color_gray_FF7)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AndroidView(factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webViewClient = WebViewClient().apply {
                            settings.javaScriptEnabled = true
                            settings.allowContentAccess = true
                            settings.javaScriptCanOpenWindowsAutomatically = true
                            settings.domStorageEnabled = true
                            settings.displayZoomControls = false
                            settings.loadWithOverviewMode = true
                            settings.builtInZoomControls = true
                            settings.setSupportZoom(true)
                        }
                        webViewClient = object : WebViewClient() {
                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                isLoading = false
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val urlFinal = request?.url.toString()
                                //khi thanh toán xong sẽ đá về 1 deeplink có param, vpc_Message,
                                // vpc_TxnResponseCode === “0” là success ngược lại là lỗi
                                val uri = Uri.parse(urlFinal)
                                val vpcResponseCode = uri.getQueryParameter("vpc_TxnResponseCode")
                                val vpcMessage = uri.getQueryParameter("vpc_Message")
                                if(vpcResponseCode == "0" && vpcMessage == "Approved"){
                                    if(isBooking){
                                        navController.previousBackStackEntry
                                            ?.savedStateHandle
                                            ?.set(
                                                Constants.BUNDLE_DATA_PAYMENT_SUCCESS,
                                                Constants.BUNDLE_DATA_PAYMENT_BOOKING_SUCCESS,
                                            )
                                    }else{
                                        navController.previousBackStackEntry
                                            ?.savedStateHandle
                                            ?.set(
                                                Constants.BUNDLE_DATA_PAYMENT_SUCCESS,
                                                Constants.BUNDLE_DATA_PAYMENT_DONATE_SUCCESS,
                                            )
                                    }
                                    navController.popBackStack()
                                    return true
                                }
                                return false
                            }

                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                            }

                            override fun onReceivedSslError(
                                view: WebView?,
                                handler: SslErrorHandler?,
                                error: SslError?
                            ) {
                                super.onReceivedSslError(view, handler, error)
                                isLoading = false
                            }
                        }
                        loadUrl(urlPayment)
                        isLoading = false
                    }
                }, update = {
                    it.loadUrl(urlPayment)
                    isLoading = false
                })
            }
            LoaderWithAnimation(isPlaying = isLoading)
        }
    }
 }