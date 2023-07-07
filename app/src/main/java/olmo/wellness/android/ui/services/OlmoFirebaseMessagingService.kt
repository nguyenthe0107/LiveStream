package olmo.wellness.android.ui.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import olmo.wellness.android.R
import olmo.wellness.android.data.model.notification.PayloadNotification
import olmo.wellness.android.domain.model.fcm.NotificationModel
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.splash_screen.SplashActivity

//@AndroidEntryPoint
open class OlmoFirebaseMessagingService: FirebaseMessagingService() {

    /*@Inject
    lateinit var repository : ApiChatRepository*/
    private val observer: RemoteMessageObserver = RemoteMessageObserver.getInstance()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if(token.isNotEmpty()){
            sendRegistrationToServer(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        displayNotification(message)
        if(message.data.isNotEmpty()){
            observer.observe(message)
        }
    }

    // [END refresh_token]
    private fun sendRegistrationToServer(token: String) {
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun displayNotification(message: RemoteMessage) {
        val contentTitle = "Kepler notification"
        var contentText = "LiveStream-Kepler"
        try {
            val payLoad : NotificationModel = Gson().fromJson(message.data.toString(), NotificationModel::class.java)
            if(payLoad.payload?.livestream?.title?.isNotEmpty() == true){
                contentText = payLoad.payload.livestream.title
            }
        }catch (ex: Exception){
        }
        createNotificationChannel()
        val channelId = "all_notifications" // Use same Channel ID
        val intent = Intent(applicationContext, SplashActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        var pendingIntent: PendingIntent? = null
        intent.putExtra(MainActivity.INTENT_DATA_NOTIFICATION, true)
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val builder = NotificationCompat.Builder(
            applicationContext,
            channelId
        ) // Create notification with channel Id
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_MAX)
        builder.setContentIntent(pendingIntent).setAutoCancel(true)
        builder.setSmallIcon(R.drawable.ic_logo).color = resources.getColor(R.color.color_main)
        val mNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        with(mNotificationManager) {
            notify(123, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "all_notifications" // You should create a String resource for this instead of storing in a variable
            val mChannel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mChannel.description = "This is default channel used for all other notifications"
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}