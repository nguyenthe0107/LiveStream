package olmo.wellness.android.di.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import olmo.wellness.android.core.Constants.IS_AUTHORIZABLE
import olmo.wellness.android.di.wrapper.AccessTokenWrapper
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val accessTokenWrapper: AccessTokenWrapper) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val shouldAddAuthHeaders = originalRequest.headers[IS_AUTHORIZABLE] != "false"
        val requestBuilder = originalRequest
            .newBuilder()
            .method(originalRequest.method, originalRequest.body)
            .removeHeader(IS_AUTHORIZABLE)

        if (shouldAddAuthHeaders) {
            if(accessTokenWrapper.getAccessToken()?.isNotEmpty() == true){
                requestBuilder.addHeader(
                    "Authorization",
                    "Bearer " + accessTokenWrapper.getAccessToken()
                )
                .header("Accept", "application/json")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}