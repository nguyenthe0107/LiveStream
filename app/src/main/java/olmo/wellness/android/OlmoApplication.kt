package olmo.wellness.android

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache
import dagger.hilt.android.HiltAndroidApp
import olmo.wellness.android.domain.use_case.SharedPrefUtils
import olmo.wellness.android.ui.analytics.AnalyticsManager

val sharedPrefs: SharedPrefUtils by lazy { OlmoApplication.prefs!! }
@HiltAndroidApp
class OlmoApplication: Application() {

    lateinit var memoryCache: LruCache<String, Bitmap>
    companion object {
        @SuppressLint("StaticFieldLeak")
        var prefs: SharedPrefUtils? = null
        private lateinit var instance: OlmoApplication
        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        prefs = SharedPrefUtils(applicationContext)
        instance = this
        // analytics
        AnalyticsManager.initialize(this.applicationContext)
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }
    }
}
