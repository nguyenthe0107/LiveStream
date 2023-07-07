package olmo.wellness.android.data.model.bank

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.bank.BankInfo

data class BankDTO(
    @SerializedName("accountName")
    @Expose
    val accountName: String?,
    @SerializedName("accountNumber")
    @Expose
    val accountNumber: String?,
    @SerializedName("bankBranchId")
    @Expose
    val bankBranchId: Int?,
    @SerializedName("bankBranchName")
    @Expose
    val bankBranchName: String?,
    @SerializedName("bankId")
    @Expose
    val bankId: Int?,
    @SerializedName("bankName")
    @Expose
    val bankName: String?,
    @SerializedName("countryId")
    @Expose
    val countryId: Int?,
    @SerializedName("countryName")
    @Expose
    val countryName: String?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("userId")
    @Expose
    val userId: Int?
)

fun BankDTO.toBankDomain() = BankInfo(accountName, accountNumber, bankBranchId, bankBranchName, bankId, bankName, countryId, countryName, id, userId)