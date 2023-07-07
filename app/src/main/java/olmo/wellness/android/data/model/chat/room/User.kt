package olmo.wellness.android.data.model.chat.room

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.business.StoreOwner
import java.io.Serializable

data class User(
    @SerializedName("avatar")
    @Expose
    val avatar: String? = null,
    @SerializedName("email")
    @Expose
    val email: String? = null,
    @SerializedName("id")
    @Expose
    val id: Int? = null,
    @SerializedName("is_ban")
    @Expose
    val isBan: Boolean? = null,
    @SerializedName("is_muted")
    @Expose
    val isMuted: Boolean? = null,
    @SerializedName("is_online")
    @Expose
    val isOnline: Boolean? = null,
    @SerializedName("store")
    @Expose
    val store: StoreOwner? = null,
    @SerializedName("name")
    @Expose
    val name: String? = null,
    @SerializedName("fullName")
    @Expose
    val fullName: String? = null,
    @SerializedName("countReaction")
    @Expose
    val countReaction: Int? = null,
    @SerializedName("nameLowerCaseIUS")
    @Expose
    val nameLowerCaseIUS: String? = null,
    @SerializedName("indexMessageRead")
    @Expose
    val indexMessageRead: Int? = null,
) : Serializable {
    companion object {
        val TEST_USER =
            User(
                null,
                "hehe@olmo.olmo",
                -1,
                false,
                name = "The Nguyen"
            )
        val TEST_LIST_USER = listOf(
            TEST_USER,
            TEST_USER,
            TEST_USER,
            TEST_USER,
            TEST_USER,
            TEST_USER,
            TEST_USER,
            TEST_USER,
        )

        val TEST = User(
            id = (0 until 10).random(),
            avatar = "https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            name = "The ne"
        )

        val TEST1 = User(
            id = 1,
            avatar = "https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            name = "anh van "
        )

        val TEST2 = User(
            id = 2,
            avatar = "https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            name = "tieng viet"
        )
    }
}

fun createUser(id: Int?, avatar: String?, email: String?, name: String?) =
    User(id = id, avatar = avatar, email = email, name = name)