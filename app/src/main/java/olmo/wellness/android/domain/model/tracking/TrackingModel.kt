package olmo.wellness.android.domain.model.tracking

import android.os.Build
import olmo.wellness.android.BuildConfig

data class TrackingModel(
    val isSuccess: Boolean?=null,
    val status: String?=null,
    val err_message: String?=null,
    val user_id: Int?=null,
    val method: String?=null,
    val account_name: String ?=null,
    val os: String?="Android",
    val os_ver: String?= "Android_".plus(Build.VERSION.SDK_INT),
    val appver: String?= BuildConfig.VERSION_NAME,
    val device_id : String = Build.MODEL.toString(),
    val category_id: List<Int>? = null,
    val cat2_id: Int? = null,
    val cat3_id: Int? = null,
    val group_content: String? = "",
    val livestream_id : Int ?= null,
    val livestream_tile: String ?= null,
    val seller_name: String ?= null,
    val categories_livestream: List<Int>? = null,
)
