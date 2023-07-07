package olmo.wellness.android.core.enums

import olmo.wellness.android.R

sealed class ConfirmType(val type: String, val name: Int) {
    object Yes : ConfirmType("YES", R.string.yes)
    object No : ConfirmType("NO", R.string.no)
}