package olmo.wellness.android.ui.livestream.stream.config

import com.amazonaws.ivs.broadcast.BroadcastConfiguration
import com.amazonaws.ivs.broadcast.Presets

object ConfigVideoStream {
    val bigSize: BroadcastConfiguration.Vec2 = BroadcastConfiguration.Vec2(1280f, 720f)
    val smallSize: BroadcastConfiguration.Vec2 = BroadcastConfiguration.Vec2(320f, 180f)

    const val frameRate60=60
    const val frameRate30=30

    fun getLiveStreamDefaultConfiguration() : BroadcastConfiguration{
        val config = Presets.Configuration.STANDARD_PORTRAIT.apply {
            video.size= bigSize
            video.targetFramerate= frameRate30
        }
        return config
    }
}