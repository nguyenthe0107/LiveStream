package olmo.wellness.android.ui.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.systemBarsPadding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.BackPressHandler
import olmo.wellness.android.ui.common.LocalBackPressedDispatcher
import olmo.wellness.android.ui.screen.navigation.NavHostController
import olmo.wellness.android.ui.screen.video_small.closePip
import olmo.wellness.android.ui.services.TokenAuthenObserver
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.network_handler.ConnectionLiveData
import java.lang.Runnable


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val INTENT_DATA = "INTENT_DATA"
        const val INTENT_DATA_ID = "INTENT_DATA_ID"
        const val INTENT_DATA_STATUS = "INTENT_DATA_STATUS"
        const val INTENT_DATA_NOTIFICATION = "INTENT_DATA_NOTIFICATION"
    }

    private val intentData by lazy { intent?.getBooleanExtra(INTENT_DATA, false) }
    private val intentDataId by lazy { intent?.getIntExtra(INTENT_DATA_ID, 0) }
    private val intentDataStatus by lazy { intent?.getStringExtra(INTENT_DATA_STATUS) }
    private var doubleBackToExitPressedOnce = false
    private val intentDataFromNotification by lazy {
        intent?.getBooleanExtra(
            INTENT_DATA_NOTIFICATION,
            false
        )
    }
    private val remoteTokenAuthenticationObserver: TokenAuthenObserver by lazy { TokenAuthenObserver.getInstance() }
    private var isReLogin = false

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint(
        "ObsoleteSdkInt", "UnusedMaterialScaffoldPaddingParameter",
        "CoroutineCreationDuringComposition"
    )
    @OptIn(
        ExperimentalCoroutinesApi::class,
        ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = remember { SystemUiController(window) }
            systemUiController.setStatusBarColor(color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
            ProvideWindowInsets {
                OlmoAndroidTheme {
                    val drawerState =
                        rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val isHasInternet = remember {
                        mutableStateOf(true)
                    }

                    val isExpireAuthenticator = remember {
                        mutableStateOf(false)
                    }

                    LaunchedEffect(Unit) {
                        val connection = ConnectionLiveData(this@MainActivity)
                        connection.observe(this@MainActivity) {
                            isHasInternet.value = it
                        }
                    }

                    LaunchedEffect(Unit){
                        remoteTokenAuthenticationObserver.result.collectLatest { status ->
                            if(status == true){
                                isExpireAuthenticator.value = true
                                isReLogin = true
                            }
                        }
                    }

                    Scaffold(
                        modifier = Modifier
                            .fillMaxWidth()
                            .systemBarsPadding()
                            .navigationBarsWithImePadding(),
                        content = {
                            CompositionLocalProvider(
                                LocalBackPressedDispatcher provides this@MainActivity.onBackPressedDispatcher
                            ) {
                                if (drawerState.isOpen) {
                                    BackPressHandler {
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    }
                                }
                                Box(modifier = Modifier.padding()) {
                                    NavHostController(
                                        intentData,
                                        intentDataId,
                                        intentDataStatus,
                                        intentDataFromNotification,
                                        isTokenNotValid = isExpireAuthenticator.value
                                    )
                                    if (!isHasInternet.value) {
                                        NoInternet()
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    private fun NoInternet() {
        Surface(
            elevation = 4.dp,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .noRippleClickable {
                    val panelIntent =
                        Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                    this@MainActivity.startActivityForResult(
                        panelIntent,
                        0
                    )
                }
                .background(
                    color = White,
                    shape = RoundedCornerShape(8.dp),
                ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(
                        White.copy(
                            alpha = 0.7f
                        )
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.lb_no_connection_found).uppercase(),
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Color_LiveStream_Main_Color,
                            fontSize = 14.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.lb_please_turn_on_internet),
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Color_LiveStream_Main_Color,
                            fontSize = 12.sp
                        )
                    )

                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_wifi_off),
                    tint = Color_gray_F92,
                    contentDescription = "Wifi off",
                    modifier = Modifier.size(24.dp)
                )

            }
        }
    }

    override fun onStop() {
        super.onStop()
        closePip()
    }


    private fun exitApp() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            getString(olmo.wellness.android.R.string.lb_warning_exit_app),
            Toast.LENGTH_SHORT
        ).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }


    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp()
            true
        } else super.onKeyDown(keyCode, event)
    }*/
}
