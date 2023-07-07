package olmo.wellness.android.ui.common.live_stream.carousel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor.FLAG_WORKAROUND_IGNORE_EDIT_LISTS
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory.*
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.MimeTypes
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen

@SuppressLint("MutableCollectionMutableState")
@Composable
fun OlmoLiveStreamPlayer(
    context: Context,
    url: String,
    selected: Boolean,
    isMute: Boolean? = null,
    idVideo: Int?,
    onFinishedVideo : ((status: Boolean) -> Unit) ?= null
){
    val trackSelector = DefaultTrackSelector(context)
    val loadControl = DefaultLoadControl.Builder()
        .setBufferDurationsMs(10000, 64 * 1024, 1024, 1024)
        .setAllocator(DefaultAllocator(true, 2 * 1024 * 1024))
        .build()
    url.let {
        val bandwidthMeter: DefaultBandwidthMeter = DefaultBandwidthMeter.Builder(context)
            .build()
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri(Uri.parse(url))
            .setLiveConfiguration(
                MediaItem.LiveConfiguration.Builder()
                    .setMaxPlaybackSpeed(1.02f)
                    .build()
            )
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
        val defaultHlsExtractorFactory = DefaultHlsExtractorFactory(
            FLAG_ALLOW_NON_IDR_KEYFRAMES
                    or FLAG_DETECT_ACCESS_UNITS
                    or FLAG_WORKAROUND_IGNORE_EDIT_LISTS
                    or FLAG_ENABLE_HDMV_DTS_AUDIO_STREAMS, false
        )
        val mediaSource = HlsMediaSource.Factory(mediaDataSourceFactory)
            .setAllowChunklessPreparation(true)
            .setExtractorFactory(defaultHlsExtractorFactory)
            .createMediaSource(mediaItem)
        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)
        val olmoPlayer = remember {
            ExoPlayer.Builder(context)
                .setLivePlaybackSpeedControl(
                    DefaultLivePlaybackSpeedControl.Builder()
                        .setFallbackMaxPlaybackSpeed(1.04f)
                        .build()
                )
                .setLoadControl(loadControl)
                .setBandwidthMeter(bandwidthMeter)
                .setTrackSelector(trackSelector)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()
        }
        var isLoading by remember {
            mutableStateOf(false)
        }
        LoadingScreen(isLoading)
        olmoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        AndroidView({
            PlayerView(it).apply {
                useController = false
                controllerAutoShow = false
                player = olmoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                requestFocus()
            }
        })
        if (selected) {
            olmoPlayer.addMediaSource(mediaSource)
            olmoPlayer.prepare()
            olmoPlayer.playWhenReady = true
            olmoPlayer.playbackState
        } else {
            olmoPlayer.playWhenReady = false
            olmoPlayer.pause()
        }
        val currentVol = olmoPlayer.volume
        olmoPlayer.volume = currentVol
        OnLifecycleEvent { owner, event ->
            // do stuff on event
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    olmoPlayer.playWhenReady = false
                    olmoPlayer.pause()
                }
                else -> {}
            }
        }
        olmoPlayer.addListener(object : Player.Listener {
            @SuppressLint("SwitchIntDef")
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayWhenReadyChanged(playWhenReady, playbackState)
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        isLoading = true
                    }
                    Player.STATE_READY -> {
                        isLoading = false
                    }
                    Player.STATE_ENDED -> {
                    }
                    Player.PLAY_WHEN_READY_CHANGE_REASON_AUDIO_FOCUS_LOSS -> {
                    }
                    Player.PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM -> {
                    }
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == ExoPlayer.STATE_ENDED){
                    onFinishedVideo?.invoke(true)
                }
            }
        })

        if (olmoPlayer.playerError != null) {
            olmoPlayer.stop()
            olmoPlayer.addMediaSource(mediaSource)
            olmoPlayer.prepare()
            olmoPlayer.playWhenReady = true
            olmoPlayer.playbackState
        }

        DisposableEffect(key1 = url) {
            onDispose {
                olmoPlayer.release()
            }
        }
    }
}

