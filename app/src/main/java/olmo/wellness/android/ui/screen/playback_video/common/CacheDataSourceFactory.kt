package olmo.wellness.android.ui.screen.playback_video.common

import android.content.Context
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import java.io.File


class CacheDataSourceFactory(context: Context, maxCacheSize: Long, maxFileSize: Long) :
    DataSource.Factory {
    private val context: Context
    private val defaultDatasourceFactory: DefaultDataSourceFactory
    private val maxFileSize: Long
    private val maxCacheSize: Long
    override fun createDataSource(): DataSource {
        val evictor = LeastRecentlyUsedCacheEvictor(maxCacheSize)
        val simpleCache = SimpleCache(File(context.getCacheDir(), "media"), evictor)
        return CacheDataSource(
            simpleCache, defaultDatasourceFactory.createDataSource(),
            FileDataSource(), CacheDataSink(simpleCache, maxFileSize),
            CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
        )
    }

    init {
        this.context = context
        this.maxCacheSize = maxCacheSize
        this.maxFileSize = maxFileSize
        val userAgent: String = Util.getUserAgent(context, "Olmo")
        // Build a HttpDataSource.Factory with cross-protocol redirects enabled.
        val httpDataSourceFactory: HttpDataSource.Factory =
            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
        val bandwidthMeter = DefaultBandwidthMeter()
        defaultDatasourceFactory = DefaultDataSourceFactory(
            this.context,
            httpDataSourceFactory
        )
    }
}
