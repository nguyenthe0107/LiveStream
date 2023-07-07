package olmo.wellness.android.core.enums

sealed class VeificationStatus(val status: String) {
    object Pending : VeificationStatus("PENDING")
    object Submitted :
        VeificationStatus("SUBMITTED")

    object Failed :
        VeificationStatus("FAILED")

    object Success :
        VeificationStatus("SUCCESS")

    object Banned :
        VeificationStatus("BANNED")
}