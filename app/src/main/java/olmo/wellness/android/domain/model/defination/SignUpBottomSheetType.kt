package olmo.wellness.android.domain.model.defination

enum class SignUpBottomSheetType(val nameType: String) {
    MAIN_SIGN_UP("MAIN_SIGN_UP"),
    USER_TYPE("USER_TYPE"),
    SELECT_CATEGORY("SELECT_CATEGORY"),
    USER_REGISTERED("USER_REGISTERED"),
    RESET_PASSWORD("RESET_PASSWORD"),
    UN_KNOW("unknow")
}