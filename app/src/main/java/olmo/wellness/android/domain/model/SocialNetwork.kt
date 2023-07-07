package olmo.wellness.android.domain.model

import androidx.annotation.DrawableRes
import olmo.wellness.android.R

enum class SocialNetwork(
    val socialName: String,
    @DrawableRes val logo: Int,
){
    INSTAGRAM("Instagram", R.drawable.ic_instagarm_colored),
    FACEBOOK("Facebook", R.drawable.ic_facebook_colored),
    TIKTOK("Tiktok", R.drawable.ic_tiktok_colored),
    EMAIL("Email", R.drawable.ic_email_colored),
    EMAIL_SCHEDULING("Email", R.drawable.ic_email_scheduling),
}
