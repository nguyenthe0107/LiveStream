package olmo.wellness.android.data.model.definition

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

enum class UserTypeModel constructor(var value: String) {
    @Expose
    @SerializedName("BUYER")
    BUYER("BUYER"),

    @Expose
    @SerializedName("SELLER")
    SELLER("SELLER"),

    @Expose
    @SerializedName("BUSINESS")
    BUSINESS("BUSINESS"),

    @Expose
    @SerializedName("KOL")
    KOL("KOL");


    companion object {
        operator fun invoke(rawValue: String) = values().find { it.value == rawValue } ?: BUYER
    }
}