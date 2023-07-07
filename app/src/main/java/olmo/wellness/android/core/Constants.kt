package olmo.wellness.android.core

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE

object Constants {
    const val LIMIT_PER_PAGE = 30
    const val MAX_SELECT_IMAGE=10
    const val ANIMATION_TIME = 200
    const val IS_AUTHORIZABLE = "isAuthorizable"
    const val MIME_IMAGE = "jpg"
    const val UNDERFINE_NUMBER = -1
    const val PAGE_SIZE=20
    const val HOTLINE = "02877777678"
    val permissions = listOf(
        READ_EXTERNAL_STORAGE,
        CAMERA
    )

    val filePermission = READ_EXTERNAL_STORAGE

    /* TAG SCREEN */
    const val IS_SUCCESS_BIO = "IS_SUCCESS_BIO"
    const val IS_SUCCESS_ADDRESS = "IS_SUCCESS_ADDRESS"
    const val IS_SUCCESS_SHARE_PROFILE = "IS_SUCCESS_SHARE_PROFILE"
    const val BUNDLE_DATA = "BUNDLE_DATA"
    const val BUNDLE_DATA_CLOSE_LIVE_STREAM = "BUNDLE_DATA_CLOSE_LIVE_STREAM"
    const val BUNDLE_DATA_RE_OPEN_LIVE_STREAM = "BUNDLE_DATA_RE_OPEN_LIVE_STREAM"
    const val BUNDLE_DATA_RELOAD_HOME_TAB = "BUNDLE_DATA_RELOAD_HOME_TAB"
    const val BUNDLE_DATA_PAYMENT_SUCCESS = "BUNDLE_DATA_PAYMENT_SUCCESS"
    const val BUNDLE_DATA_PAYMENT_BOOKING_SUCCESS = "BUNDLE_DATA_PAYMENT_BOOKING_SUCCESS"
    const val BUNDLE_DATA_PAYMENT_DONATE_SUCCESS = "BUNDLE_DATA_PAYMENT_DONATE_SUCCESS"

    object StatusLivestream{
        const val ON_LIVE="ON_LIVE"
        const val UPCOMING="UPCOMING"
    }
    const val LINK_PRIVACY = "https://keplerapp.vn/legal/privacy-policy?lang=en"
    /* Extra Data */
    const val KEY_SUMMARY_LIVE_DATA = "SUMMARY_LIVE_DATA"
    const val SUMMARY_LIVE_DATA = "?SUMMARY_LIVE_DATA={SUMMARY_LIVE_DATA}"
    const val ID = "?ID={ID}"
    const val KEY_ID = "ID"
    const val ERROR_COMMON = "Sorry We have some issue, We'll comeback!!"

    const val FIRE_BASE_CONFIG_URL = "https://olmo-wellness-a489a-default-rtdb.asia-southeast1.firebasedatabase.app"
    const val ID_SERVER_DEV = "https://dev-api.keplerapp.co/id/"
    const val ID_SERVER_STAG = "https://stg-api.keplerapp.co/id/"
    const val ID_SERVER_PRO = "https://api.keplerapp.co/id/"

    const val SERVER_URL_DEV = "https://dev-api.keplerapp.co/wellness/"
    const val SERVER_URL_STAG = "https://stg-api.keplerapp.co/wellness/"
    const val SERVER_URL_PRO = "https://api.keplerapp.co/wellness/"

    const val CHAT_SERVER_URL_DEV = "https://dev-api.keplerapp.co/chat/"
    const val CHAT_SERVER_URL_STAG = "https://stg-api.keplerapp.co/chat/"
    const val CHAT_SERVER_URL_PRO = "https://api.keplerapp.co/chat/"

    const val SOCKET_URL_DEV = "wss://dev-socket.keplerapp.co/chat"
    const val SOCKET_URL_STAG = "wss://stg-socket.keplerapp.co/chat"
    const val SOCKET_URL_PRO = "wss://socket.keplerapp.co/chat"

    const val UPLOAD_URL_DEV = "https://dev-api.keplerapp.co/file/"
    const val UPLOAD_URL_STAG = "https://stg-api.keplerapp.co/file/"
    const val UPLOAD_URL_PRO = "https://api.keplerapp.co/file/"

    const val APP_VERSION = "APP_VERSION"
    const val HOST_LINK_THUMBNAIL_DEV = "https://dev-livestream-video.olmowellness.com/"
    const val HOST_LINK_THUMBNAIL_STAG = "https://stg-livestream-video.olmowellness.com/"
    const val HOST_LINK_THUMBNAIL_PRO = "https://api-livestream-video.olmowellness.com/"

}