package olmo.wellness.android.ui.screen.video_small

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.os.bundleOf
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import olmo.wellness.android.core.hideSystemUI
import olmo.wellness.android.core.utils.getScreenHeight
import olmo.wellness.android.core.utils.getScreenWidth
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.ui.common.LocalBackPressedDispatcher
import olmo.wellness.android.ui.theme.OlmoAndroidTheme
import kotlin.properties.Delegates


@AndroidEntryPoint
class VideoActivity: ComponentActivity() {
    companion object {
        private var _isActive = false
        val isActive get() = _isActive

        private const val LIVE_STREAM_INFO = "LIVE_STREAM_INFO"
        private const val LIVE_STREAM_POSITION = "LIVE_STREAM_POSITION"

        operator fun invoke(
            from: Context,
            liveInfo: LiveSteamShortInfo,
            currentPosition: Long = 0
        ): Intent {
            return Intent(
                from,
                VideoActivity::class.java
            ).apply {
                putExtras(
                    bundleOf(
                        LIVE_STREAM_INFO to liveInfo,
                        LIVE_STREAM_POSITION to currentPosition
                    )
                )
            }
        }
    }

    private var isTerminated = false

    private val pipControllerBR = pipControllerBR(
        onClosePip = {
             this.finish()
        },
        onRequestOpenContent = {
            this.finish()
        }
    )

    private val liveInfo by lazy {
        intent.extras?.getSerializable(LIVE_STREAM_INFO) as? LiveSteamShortInfo
    }

    private val currentPosition by lazy {
        intent.extras?.getLong(LIVE_STREAM_POSITION)?:0
    }

    private var olmoPlayer: ExoPlayer? by Delegates.observable(null){ _, old, new ->
        old?.stop()
        old?.release()
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        olmoPlayer = provideExoPlayer(liveInfo?.recordUrl?:"", currentPosition?:0)
        if (liveInfo?.isLiveStream != true && currentPosition > 0) {
            olmoPlayer?.seekTo(currentPosition)
        }
        registerReceiver(pipControllerBR, IntentFilter(ACTION_PIP_CONTROL))
        _isActive = true
        setContent {
            ProvideWindowInsets {
                OlmoAndroidTheme {
                    Scaffold(modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .navigationBarsWithImePadding(),
                        content = {
                            CompositionLocalProvider(
                                LocalBackPressedDispatcher provides this@VideoActivity.onBackPressedDispatcher
                            ){
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black)
                                ) {
                                    SingleOlmoPlayer(
                                        olmoPlayer?:return@CompositionLocalProvider,
                                        false
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
        enterPipMode()
    }

    private fun provideExoPlayer(url: String, currentPosition: Long = 0): ExoPlayer{
        return ExoPlayer.Builder(this)
            .build()
            .apply {
                val mediaSource = buildMediaSource(this@VideoActivity, Uri.parse(url))
                if (mediaSource != null) {
                    this.setMediaSource(mediaSource, true)
                    this.prepare()
                }
                playWhenReady = true
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            }
            .apply {
                addListener(object : Player.Listener {
                    @SuppressLint("SwitchIntDef")
                    override fun onPlayWhenReadyChanged(
                        playWhenReady: Boolean,
                        playbackState: Int
                    ) {
                        super.onPlayWhenReadyChanged(playWhenReady, playbackState)
                        when (playbackState) {
                            Player.STATE_IDLE -> {
                            }
                            Player.STATE_READY -> {
                            }
                            Player.STATE_ENDED -> {
                            }
                            Player.PLAY_WHEN_READY_CHANGE_REASON_AUDIO_FOCUS_LOSS -> {
                            }
                            Player.PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM -> {
                            }
                        }
                    }
                })
            }
    }

    private fun enterPipMode() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                enterPictureInPictureMode(
                    PictureInPictureParams
                        .Builder()
                        .setAspectRatio(Rational(14, 16))
                        .setActions(listOf())
                        .setSourceRectHint(
                            Rect(
                                0,
                                0,
                                getScreenWidth(),
                                getScreenHeight()

                            )
                        )
                        .build()
                )
            }
            else -> {
                //Not support
            }
        }
    }

    private fun buildMediaSource(context: Context, uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context, "Olmo")
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri))
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        if (newConfig != null && !isInPictureInPictureMode && !isTerminated) {
            _isActive = false
            isTerminated = true
            openPipContent(liveInfo)
        }
    }

    override fun onBackPressed() {

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val newLiveInfo = intent?.extras?.getSerializable(LIVE_STREAM_INFO) as? LiveSteamShortInfo
        if (newLiveInfo != null){
            resetState(newLiveInfo)
        }
    }

    private fun resetState(liveDataInfo: LiveSteamShortInfo){
        olmoPlayer?.run {
            val mediaSource = buildMediaSource(this@VideoActivity, Uri.parse(liveDataInfo.playbackUrl?:""))
            if (mediaSource != null) {
                this.setMediaSource(mediaSource, true)
                this.prepare()
            }
            playWhenReady = true
        }
        enterPipMode()
        isTerminated = false
    }

    override fun onStop() {
        super.onStop()
        olmoPlayer?.playWhenReady = false
        olmoPlayer?.pause()
        _isActive = false
        if (!isTerminated) {
            closePip(liveInfo)
            isTerminated = true
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        olmoPlayer?.release()
        unregisterReceiver(pipControllerBR)
    }
}

fun Context.closePip(liveDataInfo: LiveSteamShortInfo? = null){
    sendBroadcast(Intent(ACTION_PIP_CONTROL).apply {
        putExtra(ACTION_PIP_CONTROL, ACTION_CLOSE_PIP)
        putExtra(LIVE_STREAM_INFO_BR_KEY, liveDataInfo)
    })
}

fun Context.openPipContent(liveInfo: LiveSteamShortInfo? = null){
    sendBroadcast(Intent(ACTION_PIP_CONTROL).apply {
        putExtra(ACTION_PIP_CONTROL, ACTION_REQUEST_OPEN_CONTENT)
        putExtra(LIVE_STREAM_INFO_BR_KEY, liveInfo)
    })
}