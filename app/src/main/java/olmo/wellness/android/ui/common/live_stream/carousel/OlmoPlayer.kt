package olmo.wellness.android.ui.common.live_stream.carousel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import olmo.wellness.android.ui.life_cycle_event.OnLifecycleEvent
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import java.net.CookieManager
import java.net.CookiePolicy

@SuppressLint("MutableCollectionMutableState")
@Composable
fun OlmoPlayer(
    context: Context,
    url: String,
    selected: Boolean,
    isLiveStream : Boolean ?= null,
    startPlay: Boolean ?= false,
    durationRemember: MutableState<Long>,
    isMute: Boolean ?= null,
    onFinishedVideo: ((status: Boolean) -> Unit) ?= null
){
    val trackSelector = DefaultTrackSelector(context)
    val bandwidthMeter: DefaultBandwidthMeter = DefaultBandwidthMeter.Builder(context)
        .build()
    val loadControl = DefaultLoadControl.Builder()
        .setBufferDurationsMs(32*1024, 64*1024, 1024, 1024)
        .setAllocator(DefaultAllocator(true, 2 * 1024 * 1024))
    .build()
    val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
    url.let {
        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri(Uri.parse(url))
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
        val defaultHlsExtractorFactory = DefaultHlsExtractorFactory(
            DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES
                    or DefaultTsPayloadReaderFactory.FLAG_DETECT_ACCESS_UNITS
                    or FragmentedMp4Extractor.FLAG_WORKAROUND_IGNORE_EDIT_LISTS
                    or DefaultTsPayloadReaderFactory.FLAG_ENABLE_HDMV_DTS_AUDIO_STREAMS, false
        )
        val mediaSource = HlsMediaSource.Factory(mediaDataSourceFactory)
            .setAllowChunklessPreparation(true)
            .setExtractorFactory(defaultHlsExtractorFactory)
            .createMediaSource(mediaItem)
        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)
        val olmoPlayer = remember {
            ExoPlayer.Builder(context)
                .setLoadControl(loadControl)
                .setBandwidthMeter(bandwidthMeter)
                .setTrackSelector(trackSelector)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()
        }
        var isLoading by remember {
            mutableStateOf(false)
        }
        var emitFinishedVideo by remember {
            mutableStateOf(false)
        }
        LoadingScreen(isLoading)
        olmoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        olmoPlayer.repeatMode = Player.REPEAT_MODE_ALL
        AndroidView({
            PlayerView(it).apply {
                useController = true
                controllerAutoShow = true
                setShowNextButton(false)
                setShowRewindButton(false)
                setShowPreviousButton(false)
                setShowFastForwardButton(false)
                player = olmoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        })
        if(selected && startPlay == true){
            olmoPlayer.addMediaSource(mediaSource)
            olmoPlayer.prepare()
            olmoPlayer.playWhenReady = true
            olmoPlayer.playbackState
        }else{
            olmoPlayer.playWhenReady = false
            olmoPlayer.pause()
        }
        val currentVol = olmoPlayer.volume
        olmoPlayer.volume = currentVol
        if(isMute == true){
            olmoPlayer.volume = 0f
        }else{
            olmoPlayer.volume = 1f
        }
        OnLifecycleEvent { owner, event ->
            // do stuff on event
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    olmoPlayer.playWhenReady = false
                    olmoPlayer.pause()
                }
                else -> {
                }
            }
        }
        olmoPlayer.addListener(object : Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when(playbackState) {
                    Player.STATE_IDLE -> {
                        isLoading = true
                    }
                    Player.STATE_READY -> {
                        isLoading = false
                        emitFinishedVideo = false
                    }
                    Player.STATE_ENDED -> {
                        isLoading = false
                    }
                    Player.STATE_BUFFERING -> {
                    }
                }
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ){
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                emitFinishedVideo = reason == 0
            }
        })

        if(emitFinishedVideo){
            onFinishedVideo?.invoke(true)
            emitFinishedVideo = false
        }

        if (olmoPlayer.playerError != null) {
            olmoPlayer.retry()
            olmoPlayer.playWhenReady = true
            olmoPlayer.playbackState
            isLoading = false
        }

        DisposableEffect(key1 = url) {
            onDispose {
                emitFinishedVideo = false
                olmoPlayer.release()
            }
        }
    }
}

private fun buildMediaSource(context: Context, uri: Uri): MediaSource? {
    val userAgent = Util.getUserAgent(context, "Kepler")
    val defaultBandwidthMeter = DefaultBandwidthMeter()
    val cookieManager = CookieManager()
    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    val client = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(cookieManager))
        .build()
    val dataSourceFactory = DefaultDataSourceFactory(
        context,
        userAgent,
        defaultBandwidthMeter
    )
    return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri))
}