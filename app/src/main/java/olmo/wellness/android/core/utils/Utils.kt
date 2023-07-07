package olmo.wellness.android.core.utils

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import com.google.i18n.phonenumbers.nano.Phonemetadata
import olmo.wellness.android.core.Constants
import olmo.wellness.android.domain.model.SocialNetwork
import java.io.File
import java.util.*


const val FACEBOOK_PACKAGE_NAME = "com.facebook.katana"
const val INSTAGRAM_PACKAGE_NAME = "com.instagram.android"
const val TIKTOK_M_PACKAGE = "com.zhiliaoapp.musically"
const val TIKTOK_T_PACKAGE = "com.ss.android.ugc.trill"

fun getFilename(context: Context, uri: Uri): String? {
    var mimeType: String? = null
    mimeType = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
        val cr: ContentResolver = context.getContentResolver()
        cr.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
            uri
                .toString()
        )
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileExtension.lowercase()
        )
    }
    return mimeType
}

fun generateMyQRCode(base64String: String): Bitmap? {
    //encode image to base64 string
    val base64Image: String = base64String.split(",")[1]
    val decodedString: ByteArray = Base64.decode(base64Image, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}

fun shareSocialMedia(context: Context, socialType: SocialNetwork, linkShare: String){
    var application = FACEBOOK_PACKAGE_NAME
    when(socialType){
        SocialNetwork.FACEBOOK -> {
            application = FACEBOOK_PACKAGE_NAME
        }
        SocialNetwork.INSTAGRAM -> {
            application = INSTAGRAM_PACKAGE_NAME
        }
        SocialNetwork.TIKTOK -> {
            application = TIKTOK_M_PACKAGE
        }
    }
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.setPackage(application)
    intent.putExtra(Intent.EXTRA_TITLE, "Olmo")
    intent.putExtra(Intent.EXTRA_TEXT, linkShare)
    intent.type = "text/plain"
    try {
        // Start the specific social application
        context.startActivity(intent)
    } catch (ex: ActivityNotFoundException) {
        // The application does not exist
        Toast.makeText(context, "app have not been installed or not SignIn to App.Please check 2 conditions before share link", Toast.LENGTH_SHORT).show()
        shareLinkVideo(context, "linkVideo")
    }
}

fun shareLinkVideo(context: Context, linkVideo: String){
    context.let {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, linkVideo)
        it.startActivity(Intent.createChooser(shareIntent, "Olmo"))
    }
}

fun shareVideoTikTok(context: Context, videoLink: String){
    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.putExtra(Intent.EXTRA_STREAM, videoLink)
    shareIntent.type = "video/*" //text/plain
    shareIntent.setPackage(TIKTOK_M_PACKAGE)
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(Intent.createChooser(shareIntent, "Olmo"))
}

fun shareWithEmail(context: Context, linkShare: String){
    context.let {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, "")
        intent.putExtra(Intent.EXTRA_SUBJECT, linkShare)
        try {
            it.startActivity(intent)
        }catch (ex: Exception){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }
}

fun shareVideoWithInsta(context: Context,filename: String) {
    context.let {
        val settype = "video/*"
        // Create the new Intent using the 'Send' action.
        val share = Intent(Intent.ACTION_SEND)
        // Set the MIME type
        share.type = settype
        share.setPackage("com.instagram.android")

        // Create the URI from the media
        val media = File(filename)
        val uri = Uri.fromFile(media)

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri)
        // Broadcast the Intent.
        it.startActivity(Intent.createChooser(share, "Share to"))
    }
}

private fun checkAppInstall(context: Context, uri: String): Boolean {
    val pm: PackageManager = context.packageManager
    try {
        pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
    }
    return false
}

fun getFormattedNumber(phoneNumber: String): String? {
    var phoneNumber: String? = phoneNumber
    val phoneNumberUtil = PhoneNumberUtil.getInstance()
    val numberFormat = Phonemetadata.NumberFormat()
    numberFormat.pattern = "(\\d{3})(\\d{3})(\\d{4})"
    numberFormat.format = "($1) $2-$3"
    val newNumberFormats: MutableList<Phonemetadata.NumberFormat> = ArrayList()
    newNumberFormats.add(numberFormat)
    var phoneNumberPN: PhoneNumber? = null
    try {
        phoneNumberPN = phoneNumberUtil.parse(phoneNumber, Locale.US.country)
        phoneNumber = phoneNumberUtil.formatByPattern(
            phoneNumberPN,
            PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL,
            newNumberFormats
        )
    } catch (e: NumberParseException) {
        e.printStackTrace()
    }
    return phoneNumber
}

fun getPhoneNumber(phoneNumber: String){
    try {
        // phone must begin with '+'
        val phoneUtil = PhoneNumberUtil.getInstance()
        val numberProto = phoneUtil.parse("phoneNumber", "")
        val countryCode = numberProto.countryCode
        val nationalNumber = numberProto.nationalNumber
    } catch (e: NumberParseException) {
        System.err.println("NumberParseException was thrown: $e")
    }
}

fun callHotline(context: Context){
    val intentDial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.HOTLINE))
    context.startActivity(intentDial)
}


