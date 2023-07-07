package olmo.wellness.android.ui.helpers

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import org.webrtc.ContextUtils.getApplicationContext

import java.util.*

@SuppressLint("HardwareIds")
fun getUniqueDeviceId(): String {
    val m_szDevIDShort =
        "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10
    var serial: String? = null
    try {
        serial = Settings.Secure.getString(
            getApplicationContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    } catch (e: Exception) {
        serial = UUID.randomUUID().toString()
    }
    return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
}