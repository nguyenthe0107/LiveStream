package olmo.wellness.android.domain.use_case

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import olmo.wellness.android.domain.model.config_app.ConfigAppModel
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.user_info.UserInfoLocal
import olmo.wellness.android.domain.model.verification1.step1.Address

class SharedPrefUtils(val context: Context) {
    private var sp: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun setToken(token: String?) {
        sp.edit().putString(TOKEN, token).apply()
    }

    fun getToken(): String? {
        return sp.getString(TOKEN, "")
    }

    fun setLoginSuccess(isLogin: Boolean) {
        sp.edit().putBoolean(IS_LOGIN_SUCCESS, isLogin).apply()
    }

    fun getStatusLogin(): Boolean {
        return sp.getBoolean(IS_LOGIN_SUCCESS, false)
    }

    fun setProfile(profileInfo: ProfileInfo) {
        val gson = Gson().toJson(profileInfo)
        sp.edit().putString(PROFILE_INFO, gson).apply()
    }

    fun getProfile(): ProfileInfo {
        return try {
            val json = sp.getString(PROFILE_INFO, "")
            if(json != null){
                Gson().fromJson(json, ProfileInfo::class.java)
            }else{
                ProfileInfo()
            }
        } catch (ex: Exception) {
            ProfileInfo()
        }
    }

    fun setAddressDetail(address: Address) {
        val gson = Gson().toJson(address)
        sp.edit().putString(ADDRESS_BUSINESS_DETAIL, gson).apply()
    }

    fun getAddressDetail(): Address {
        return try {
            val json = sp.getString(ADDRESS_BUSINESS_DETAIL, "")
            if(json != null){
                Gson().fromJson(json, Address::class.java)
            }else{
                Address()
            }
        } catch (ex: Exception) {
            Address()
        }
    }

    fun setUserRole(userRole: String?) {
        sp.edit().putString(USER_ROLE, userRole).apply()
    }

    fun getUserRoleLocal(): String? {
        return sp.getString(USER_ROLE, "")
    }

    fun setTimeLiveCreated(createdTime: Long) {
        sp.edit().putLong(TIME_CREATED_AT_LIVE_STREAM, createdTime).apply()
    }

    fun getTimeLiveCreated(): Long {
        return sp.getLong(TIME_CREATED_AT_LIVE_STREAM, 0L)
    }

    fun setUserInfoLocal(userType: UserInfoLocal) {
        sp.edit().putString(USER_INFO_LOCAL, Gson().toJson(userType)).apply()
    }

    fun getUserInfoLocal(): UserInfoLocal{
        return try {
            val json = sp.getString(USER_INFO_LOCAL, "")
            if(json != null){
                Gson().fromJson(json, UserInfoLocal::class.java)
            }else{
                UserInfoLocal()
            }
        } catch (ex: Exception) {
            UserInfoLocal()
        }
    }

    fun setConfigApp(configAppModel: ConfigAppModel) {
        val gson = Gson().toJson(configAppModel)
        sp.edit().putString(CONFIG_APP, gson).apply()
    }

    fun getConfigApp(): ConfigAppModel {
        return try {
            val json = sp.getString(CONFIG_APP, "")
            if(json != null){
                Gson().fromJson(json, ConfigAppModel::class.java)
            }else{
                ConfigAppModel()
            }
        } catch (ex: Exception) {
            ConfigAppModel()
        }
    }

    fun setValue(key: String, value: String?) {
        sp.edit().putString(key, value).apply()
    }

    fun getValue(key: String): String? {
        return sp.getString(key, null)
    }

    fun setValue(key: String, value: Int) {
        sp.edit().putInt(key, value).apply()
    }

    fun getValue(key: String, default: Int?): Int {
        return sp.getInt(key, default ?: 0)
    }

    fun setSetValues(key: String, set: Set<String>?) {
        sp.edit().putStringSet(key, set).apply()
    }

    fun getSetValues(key: String): Set<String>? {
        return sp.getStringSet(key, null)
    }

    fun clear(key: String) {
        sp.edit().remove(key).apply()
    }

    fun clear(keys: List<String>) {
        keys.forEach {
            clear(it)
        }
    }

    fun logOut() {
        sp.edit().clear().apply()
    }

    fun clearToken(){
        sp.edit().remove(TOKEN).apply()
    }

    fun clearUserRole(){
        sp.edit().remove(USER_ROLE).apply()
    }

    fun clearTimeCreatedLive(){
        sp.edit().remove(TIME_CREATED_AT_LIVE_STREAM).apply()
    }

    fun setTotalUnSeenNotification(total : Int){
        sp.edit().putInt(TOTAL_NOTI_UNSEEN, total).apply()
    }

    fun getTotalUnSeenNotification() : Int{
        return sp.getInt(TOTAL_NOTI_UNSEEN, 0)
    }

    fun setUrlPaymentMethod(url : String){
        sp.edit().putString(URL_PAYMENT_METHOD, url).apply()
    }

    fun getUrlPaymentMethod() : String{
        return sp.getString(URL_PAYMENT_METHOD, "").orEmpty()
    }

    companion object {
        const val TOKEN = "TOKEN"
        const val IS_LOGIN_SUCCESS = "IS_LOGIN_SUCCESS"
        const val FIREBASE_TOKEN = "FIREBASE_TOKEN"
        const val PROFILE_INFO = "PROFILE_INFO"
        const val USER_INFO_LOCAL = "USER_INFO_LOCAL"
        const val URI_LOCAL = "URI_LOCAL"
        const val ADDRESS_BUSINESS_DETAIL = "ADDRESS_BUSINESS_DETAIL"
        const val TIME_CREATED_AT_LIVE_STREAM = "TIME_CREATED_AT_LIVE_STREAM"
        const val USER_ROLE = "USER_ROLE"
        const val CONFIG_APP = "CONFIG_APP"
        const val TOTAL_NOTI_UNSEEN = "TOTAL_NOTI_UNSEEN"
        const val URL_PAYMENT_METHOD = "URL_PAYMENT_METHOD"
    }
}