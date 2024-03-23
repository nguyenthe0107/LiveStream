package olmo.wellness.android.di.interceptors

import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import olmo.wellness.android.core.Constants.IS_AUTHORIZABLE
import olmo.wellness.android.data.model.ErrorResponseDTO
import olmo.wellness.android.di.wrapper.AccessTokenWrapper
import olmo.wellness.android.ui.services.TokenAuthenObserver
import java.util.Locale
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val accessTokenWrapper: AccessTokenWrapper) :
    Interceptor {
    private val observer: TokenAuthenObserver by lazy { TokenAuthenObserver.getInstance() }
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val shouldAddAuthHeaders = originalRequest.headers[IS_AUTHORIZABLE] != "false"
        val requestBuilder = originalRequest
            .newBuilder()
            .method(originalRequest.method, originalRequest.body)
            .removeHeader(IS_AUTHORIZABLE)
        val url = originalRequest.url.toString()
        if (shouldAddAuthHeaders) {
            val accessToken = accessTokenWrapper.getAccessToken()
            if (accessToken?.isNotEmpty() == true) {
                requestBuilder.addHeader(
                    "Authorization",
                    "Bearer $accessToken"
                ).header("Accept", "application/json")
                val response = chain.proceed(requestBuilder.build())
                val errorBody = response.body?.string()
                val errorMes = Gson().fromJson(errorBody, ErrorResponseDTO::class.java)
                Log.e("WTF", " errorBody $errorMes")
                Log.e("WTF", " response.code ${response.code}")
                Log.e("WTF", " accessToken $accessToken")
                Log.e("WTF", " mes " + errorMes?.message?.lowercase(Locale.ROOT)?.trim())
                val errorInvalid = "invalid user token"
                val expired = "jwt expired"
                if ((response.code == 401 || response.code == 403) && errorMes.message.isNotEmpty() && (errorMes.message.lowercase(
                        Locale.ROOT
                    ).trim().contains(errorInvalid)) || errorMes?.message?.lowercase(
                        Locale.ROOT
                    )?.trim()?.contains(expired) == true
                ){
                    observer.observe(true)
                    Log.e("WTF", " observer.observe(true) $url")
                    return response
                }
            } else {
                observer.observe(true)
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}
