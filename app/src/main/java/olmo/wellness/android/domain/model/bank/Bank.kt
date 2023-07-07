package olmo.wellness.android.domain.model.bank


import com.google.gson.annotations.SerializedName

data class Bank(
    @SerializedName("accountName")
    val accountName: String?,
    @SerializedName("accountNumber")
    val accountNumber: String?,
    @SerializedName("bankBranchId")
    val bankBranchId: Int?,
    @SerializedName("bankBranchName")
    val bankBranchName: String?,
    @SerializedName("bankId")
    val bankId: Int?,
    @SerializedName("bankName")
    val bankName: String?,
    @SerializedName("countryId")
    val countryId: Int?,
    @SerializedName("countryName")
    val countryName: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("userId")
    val userId: Int?
)