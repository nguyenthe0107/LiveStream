package olmo.wellness.android.ui.screen.account_setting

import androidx.annotation.DrawableRes

data class OptionItemViewData(
    @DrawableRes val iconSrcResId: Int,
    val title: String,
    val onItemClick: () -> Unit
)