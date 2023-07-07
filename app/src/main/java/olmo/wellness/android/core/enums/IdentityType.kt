package olmo.wellness.android.core.enums

import olmo.wellness.android.R

sealed class IdentityType(val type: String, val name: Int) {
    object Passport : IdentityType("PASSPORT", R.string.passport)
    object IdCard : IdentityType("ID_CARD", R.string.id_card)
    object DriverLicense : IdentityType("DRIVER_LICENSE", R.string.driver_license)
}