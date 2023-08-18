package olmo.wellness.android.di.interceptors

import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import olmo.wellness.android.di.wrapper.AccessTokenWrapper
import olmo.wellness.android.ui.services.TokenAuthenObserver
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val accessTokenWrapper: AccessTokenWrapper
) : Authenticator {
    private val observer: TokenAuthenObserver = TokenAuthenObserver.getInstance()
    override fun authenticate(route: Route?, response: Response): Request? {
        return if(accessTokenWrapper.getNewAccessToken()?.isNotEmpty() == true){
            Log.e("WTF", " accessTokenWrapper.getNewAccessToken() ")
            response.request.newBuilder()
                .header("Authorization", accessTokenWrapper.getNewAccessToken().orEmpty())
                .build()
        }else{
            observer.observe(true)
            return null
        }
    }
}