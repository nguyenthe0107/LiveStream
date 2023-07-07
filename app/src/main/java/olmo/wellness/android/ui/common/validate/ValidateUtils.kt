package olmo.wellness.android.ui.common.validate

import android.telephony.PhoneNumberUtils
import com.google.gson.Gson
import olmo.wellness.android.BuildConfig
import olmo.wellness.android.core.Constants.HOST_LINK_THUMBNAIL_DEV
import olmo.wellness.android.core.Constants.HOST_LINK_THUMBNAIL_PRO
import olmo.wellness.android.core.Constants.HOST_LINK_THUMBNAIL_STAG
import olmo.wellness.android.data.model.ErrorResponseDTO
import olmo.wellness.android.sharedPrefs
import retrofit2.HttpException
import java.lang.Exception
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

fun emailValidator(email: String?): Boolean {
    val pattern: Pattern
    val EMAIL_PATTERN =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    pattern = Pattern.compile(EMAIL_PATTERN)
    val matcher: Matcher = pattern.matcher(email)
    return matcher.matches()
}

fun validatePassword(password: String): Boolean {
    /*
    ^                 # start-of-string
    (?=.*[0-9])       # a digit must occur at least once
    (?=.*[a-z])       # a lower case letter must occur at least once
    (?=.*[A-Z])       # an upper case letter must occur at least once
    (?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
    (?=\\S+$)          # no whitespace allowed in the entire string
    .{4,}             # anything, at least six places though
     $
     */
    val passwordREGEX = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#\$%^&+=])" +
            ".{3,}" +               //at least 8 characters
            "$")
    return passwordREGEX.matcher(password).matches()
}

fun numberRulePassword(password: String): Boolean {

    val numberREGEX = Pattern.compile("^" +
            "(?=.*[0-9])" +
            ".{1,}" +
            "$")
    return numberREGEX.matcher(password).matches()
}

fun letterRulePassword(password: String): Boolean {

    val letterREGEX = Pattern.compile("^" +
            "(?=.*[a-z])"   +
            ".{1,}" +
            "$")

    return letterREGEX.matcher(password).matches()
}

fun upperCaseRulePassword(password: String): Boolean {

    val upperCaseREGEX = Pattern.compile("^" +
            "(?=.*[A-Z])" +
            ".{1,}" +
            "$")

    return upperCaseREGEX.matcher(password).matches()
}

fun specialCharacterRulePassword(password: String): Boolean {

    val specialCharacterREGEX = Pattern.compile("^" +
            "(?=.*[@#\$%^&+=])" +
            ".{1,}" +
            "$")
    return specialCharacterREGEX.matcher(password).matches()
}

fun validateRegexPhone(phone: String) : Boolean{
    val phoneREGEX = Pattern.compile("^" +
            "(?=.*[0-9])" +         
            ".{9,}" +
            "$")
    return phoneREGEX.matcher(phone).matches()
}

fun getFormatPhone(phoneRaw: String) : String{
    var postfixPhone = PhoneNumberUtils.formatNumber(phoneRaw, Locale.getDefault().country).trim()
    if(postfixPhone.first().toString().compareTo("0") == 0){
        postfixPhone = postfixPhone.substring(1).trim()
    }
    return postfixPhone.replace(" ", "")
}

fun phoneFormatWithoutZeroFirst(phone: String) : String{
    if(phone.isNotEmpty() && phone.first().toString() == "0"){
        return phoneFormatWithoutZeroFirst(phone.substring(1).trim())
    }
    return phone
}

fun phoneFormatWithoutSpecialCharacter(phone: String) : String{
    if(phone.isNotEmpty() && phone.first().toString() == "+"){
        return phoneFormatWithoutZeroFirst(phone.substring(1).trim())
    }
    return phone
}

fun parseErrorResponse(throwable: Exception): ErrorResponseDTO{
    var errorResponse = ErrorResponseDTO("", "")
    if (throwable is HttpException) {
        val errorBody: String? = throwable.response()?.errorBody()?.string()
        errorResponse =
            Gson().fromJson(errorBody, ErrorResponseDTO::class.java)
    }
    return errorResponse
}

fun checkValidateUserName(username: String) : Boolean{
    val size = username.length
    val condition1 = Pattern.compile("^" +
            "(?=.*[0-9])" + //at least 1 digit
            ".{8,}" +
            "$")
    val condition2 = Pattern.compile("^" +
            "(?=.*[@#\$%^&+=])" +
            ".{8,}" +
            "$")
    return condition1.matcher(username).matches() || condition2.matcher(username).matches() || size > 30
}

fun checkValidateServiceName(username: String) : Boolean{
    val size = username.length
    val condition1 = Pattern.compile("^" +
            "(?=.*[0-9])" +
            "(?=.*[@#\$%^&+=])" +
            "(?=.*[a-z])" +
            ".{8,}" +
            "$")
    return condition1.matcher(username).matches() && size <= 30
}

fun provideAppVersion() : String{
    val configAppModel = sharedPrefs.getConfigApp()
    var apiUrl: String = "Dev"
    val appNumber = BuildConfig.VERSION_CODE
    apiUrl = when {
        appNumber >= configAppModel.android?.dev ?: 0 -> {
            "App-Dev " + BuildConfig.VERSION_CODE
        }
        appNumber >= configAppModel.android?.staging ?: 0 && appNumber < configAppModel.android?.dev ?: 0  -> {
            "App-Staging " + BuildConfig.VERSION_CODE
        }
        else -> {
            ""
        }
    }
    return apiUrl
}

fun provideThumbnailUrl() : String{
    val configAppModel = sharedPrefs.getConfigApp()
    var apiUrl = HOST_LINK_THUMBNAIL_STAG
    val appNumber = BuildConfig.VERSION_CODE
    apiUrl = when {
        appNumber >= (configAppModel.android?.dev ?: 0) -> {
            HOST_LINK_THUMBNAIL_DEV
        }
        appNumber >= (configAppModel.android?.staging
            ?: 0) && appNumber < (configAppModel.android?.dev ?: 0) -> {
            HOST_LINK_THUMBNAIL_STAG
        }
        else -> {
            HOST_LINK_THUMBNAIL_PRO
        }
    }
    return apiUrl
}
