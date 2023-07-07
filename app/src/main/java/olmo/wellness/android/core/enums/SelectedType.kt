package olmo.wellness.android.core.enums

import olmo.wellness.android.R

sealed class SelectedType(val type: String, val name: Int) {
    object Yes : SelectedType("YES", R.string.yes)
    object No : SelectedType("NO", R.string.no)
    object Some : SelectedType("SOME", R.string.some_of_them)
}