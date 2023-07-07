package olmo.wellness.android.domain.model.defination

enum class UserFiledType(val nameType: String) {
    SERVICE_NAME("service_name"),
    STORE_NAME("store_name"),
    YOUR_NAME("your_name"),
    BIO("bio"),
    GENDER("gender"),
    BIRTHDAY("birthday"),
    PHONE("phone"),
    EMAIL("email"),
    ADDRESS("address"),
    PASSWORD("password"),
    SOCIAL_MEDIA("social_media"),
    CAPTURE("capture"),
    VERIFY_CODE("verify_code"),
    CHECK_MAIL("check_mail"),
    UN_KNOW("unknow")
}