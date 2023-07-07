package olmo.wellness.android.ui.screen.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import olmo.wellness.android.core.Constants.APP_VERSION
import olmo.wellness.android.core.Constants.ERROR_COMMON
import olmo.wellness.android.core.Constants.FIRE_BASE_CONFIG_URL
import olmo.wellness.android.core.fromJson
import olmo.wellness.android.domain.model.config_app.ConfigAppModel
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.screen.MainActivity
import java.lang.Exception


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance(FIRE_BASE_CONFIG_URL).reference
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children.filter { it.key == APP_VERSION }) {
                            val snapshotString = snapshot.value.toString()
                            val androidConfig: ConfigAppModel? = try {
                                fromJson(snapshotString)
                            } catch (e: Exception) {
                                null
                            }
                            androidConfig?.let { sharedPrefs.setConfigApp(it) }
                        }
                    }else{
                        showError(ERROR_COMMON)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    showError(err = error.message ?: ERROR_COMMON)
                }
            })

        val intentData: Uri? = intent?.data
        if(intentData != null){
            val businessId: String? = intentData.getQueryParameter("businessId")
            val status: String? = intentData.getQueryParameter("status")
            if(businessId?.isNotEmpty() == true){
                lifecycleScope.launchWhenCreated {
                    delay(1000)
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.putExtra(MainActivity.INTENT_DATA, true)
                    intent.putExtra(MainActivity.INTENT_DATA_ID, businessId.toInt())
                    intent.putExtra(MainActivity.INTENT_DATA_STATUS, status)
                    startActivity(intent)
                    finish()
                }
            }
        }else{
            lifecycleScope.launchWhenCreated {
                delay(1000)
                val hasNotification = intent?.getBooleanExtra(MainActivity.INTENT_DATA_NOTIFICATION, false)
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.putExtra(MainActivity.INTENT_DATA, false)
                intent.putExtra(MainActivity.INTENT_DATA_NOTIFICATION, hasNotification)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showError(err: String){
        Toast.makeText(this,  err, Toast.LENGTH_LONG).show()
    }
}

