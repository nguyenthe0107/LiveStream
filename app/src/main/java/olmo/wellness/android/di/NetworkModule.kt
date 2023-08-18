package olmo.wellness.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import olmo.wellness.android.BuildConfig
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.Constants.CHAT_SERVER_URL_DEV
import olmo.wellness.android.core.Constants.CHAT_SERVER_URL_PRO
import olmo.wellness.android.core.Constants.CHAT_SERVER_URL_STAG
import olmo.wellness.android.core.Constants.ID_SERVER_DEV
import olmo.wellness.android.core.Constants.ID_SERVER_PRO
import olmo.wellness.android.core.Constants.ID_SERVER_STAG
import olmo.wellness.android.core.Constants.SERVER_URL_DEV
import olmo.wellness.android.core.Constants.SERVER_URL_PRO
import olmo.wellness.android.core.Constants.SERVER_URL_STAG
import olmo.wellness.android.core.Constants.UPLOAD_URL_DEV
import olmo.wellness.android.core.Constants.UPLOAD_URL_PRO
import olmo.wellness.android.core.Constants.UPLOAD_URL_STAG
import olmo.wellness.android.data.ApiChatService
import olmo.wellness.android.data.ApiIDTypeService
import olmo.wellness.android.data.ApiService
import olmo.wellness.android.data.ApiUploadService
import olmo.wellness.android.di.interceptors.AuthInterceptor
import olmo.wellness.android.di.interceptors.HttpCustomLoggingInterceptor
import olmo.wellness.android.di.interceptors.TokenAuthenticator
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.webrtc.rtc.RtcService
import olmo.wellness.android.webrtc.rtc.RtcServiceApi
import olmo.wellness.android.webrtc.socket.SocketApi
import olmo.wellness.android.webrtc.socket.SocketService
import org.webrtc.EglBase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val cacheSize = (5 * 1024 * 1024).toLong()

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): ApiService {
        val okHttpClientBuilder = createClientBuilder(authInterceptor, tokenAuthenticator)
        val okHttpClient = okHttpClientBuilder.build()
        val retrofit = Retrofit.Builder().baseUrl(provideUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun createClientBuilder(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient.Builder {
        val httpLoggingInterceptor =
            HttpCustomLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClientBuilder = OkHttpClient().newBuilder()
        okHttpClientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
        okHttpClientBuilder.addInterceptor(authInterceptor)
        okHttpClientBuilder.addInterceptor { chain ->
            val newRequestBuilder = chain.request().newBuilder()
                .header("Origin", "https://dev-api.kepler.asia/wellness")
            chain.proceed(newRequestBuilder.build())
        }
        okHttpClientBuilder.connectTimeout(30L, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(30L, TimeUnit.SECONDS)
        okHttpClientBuilder.authenticator(tokenAuthenticator)
        return okHttpClientBuilder
    }

    @Provides
    @Singleton
    fun provideApiChatService(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): ApiChatService {
        val okHttpClientBuilder = createClientBuilder(authInterceptor, tokenAuthenticator)
        val okHttpClient = okHttpClientBuilder.build()
        val retrofit = Retrofit.Builder().baseUrl(provideUrlForChatService())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(ApiChatService::class.java)
    }

    @Provides
    @Singleton
    fun provideIdServiceApi(): ApiIDTypeService {
        val loggingHeader = HttpLoggingInterceptor()
        loggingHeader.level = HttpLoggingInterceptor.Level.HEADERS
        val loggingBody = HttpLoggingInterceptor()
        loggingBody.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()

        client.addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
            level = HttpLoggingInterceptor.Level.BODY
        })

        client.addInterceptor { chain ->
            val newRequestBuilder = chain.request().newBuilder()
                .header("Origin", "https://dev-api.kepler.asia/wellness")
            val originalRequest = chain.request()
            val shouldAddAuthHeaders = originalRequest.headers[Constants.IS_AUTHORIZABLE] != "false"
            if (shouldAddAuthHeaders) {
                if (sharedPrefs.getToken()?.isNotEmpty() == true) {
                    newRequestBuilder.addHeader(
                        "Authorization",
                        "Bearer " + sharedPrefs.getToken()
                    )
                }
            }
            chain.proceed(newRequestBuilder.build())
        }

        client.addInterceptor {
            val original = it.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            val request = requestBuilder.method(original.method, original.body).build()
            return@addInterceptor it.proceed(request)
        }

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .baseUrl(provideUrlIDAPI()).build().create(ApiIDTypeService::class.java)
    }

    @Provides
    @Singleton
    fun provideUploadServiceApi(): ApiUploadService {
        val loggingHeader = HttpLoggingInterceptor()
        loggingHeader.level = HttpLoggingInterceptor.Level.HEADERS
        val loggingBody = HttpLoggingInterceptor()
        loggingBody.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()

        client.addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
            level = HttpLoggingInterceptor.Level.BODY
        })

        client.addInterceptor { chain ->
            val newRequestBuilder = chain.request().newBuilder()
            val originalRequest = chain.request()
            val shouldAddAuthHeaders = originalRequest.headers[Constants.IS_AUTHORIZABLE] != "false"
            if (shouldAddAuthHeaders) {
                if (sharedPrefs.getToken()?.isNotEmpty() == true) {
                    newRequestBuilder.addHeader(
                        "Authorization",
                        "Bearer " + sharedPrefs.getToken()
                    )
                }
            }
            chain.proceed(newRequestBuilder.build())
        }

        client.addInterceptor {
            val original = it.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            val request = requestBuilder.method(original.method, original.body).build()
            return@addInterceptor it.proceed(request)
        }

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .baseUrl(provideUrlUploadAPI()).build().create(ApiUploadService::class.java)
    }

    @Provides
    @Singleton
    fun provideRtcService(socket: SocketApi): RtcServiceApi = RtcService(socket)

    @Provides
    @Singleton
    fun provideSocketService(socketService: SocketService): SocketApi = socketService


    @Provides
    @Singleton
    fun provideEglContext(): EglBase.Context = EglBase.create().eglBaseContext

    private fun provideUrl(): String {
        val configAppModel = sharedPrefs.getConfigApp()
        var apiUrl = SERVER_URL_STAG
        val appNumber = BuildConfig.VERSION_CODE
        apiUrl = when {
            appNumber >= configAppModel.android?.dev ?: 0 -> {
                SERVER_URL_DEV
            }

            appNumber >= configAppModel.android?.staging ?: 0 && appNumber < configAppModel.android?.dev ?: 0 -> {
                SERVER_URL_STAG
            }

            else -> {
                SERVER_URL_PRO
            }
        }
        return apiUrl
    }

    private fun provideUrlForChatService(): String {
        val configAppModel = sharedPrefs.getConfigApp()
        var apiUrl = CHAT_SERVER_URL_STAG
        val appNumber = BuildConfig.VERSION_CODE
        apiUrl = when {
            appNumber >= configAppModel.android?.dev ?: 0 -> {
                CHAT_SERVER_URL_DEV
            }

            appNumber >= configAppModel.android?.staging ?: 0 && appNumber < configAppModel.android?.dev ?: 0 -> {
                CHAT_SERVER_URL_STAG
            }

            else -> {
                CHAT_SERVER_URL_PRO
            }
        }
        return apiUrl
    }

    private fun provideUrlIDAPI(): String {
        val configAppModel = sharedPrefs.getConfigApp()
        var apiUrl = ID_SERVER_STAG
        val appNumber = BuildConfig.VERSION_CODE
        apiUrl = when {
            appNumber >= configAppModel.android?.dev ?: 0 -> {
                ID_SERVER_DEV
            }

            appNumber >= configAppModel.android?.staging ?: 0 && appNumber < configAppModel.android?.dev ?: 0 -> {
                ID_SERVER_STAG
            }

            else -> {
                ID_SERVER_PRO
            }
        }
        return apiUrl
    }

    private fun provideUrlUploadAPI(): String {
        val configAppModel = sharedPrefs.getConfigApp()
        var apiUrl = UPLOAD_URL_STAG
        val appNumber = BuildConfig.VERSION_CODE
        apiUrl = when {
            appNumber >= configAppModel.android?.dev ?: 0 -> {
                UPLOAD_URL_DEV
            }

            appNumber >= configAppModel.android?.staging ?: 0 && appNumber < configAppModel.android?.dev ?: 0 -> {
                UPLOAD_URL_STAG
            }

            else -> {
                UPLOAD_URL_PRO
            }
        }
        return apiUrl
    }
}