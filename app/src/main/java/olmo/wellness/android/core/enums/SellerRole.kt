package olmo.wellness.android.core.enums

import olmo.wellness.android.R

sealed class SellerRole(val type: String, val name: Int) {
    object BeneficialOwner : SellerRole("BENEFICIAL_OWNER", R.string.owner_of_business)
    object LegalRepresentative :
        SellerRole("LEGAL_REPRESENTATIVE", R.string.representative_of_business)
}