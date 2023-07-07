package olmo.wellness.android.domain.model.defination.re_submit

enum class ErrorResubmitType constructor(val err: String) {
    STEP1_BUSINESS_LOCATION("siv1.step1.business-location"),
    STEP1_BUSINESS_TYPE("siv1.step1.business-type"),
    STEP1_BUSINESS_NAME("siv1.step1.business-name"),
    STEP1_BUSINESS_ADDRESS("siv1.step1.business-address"),
    STEP1_CONTACT_EMAIL("siv1.step1.contact.email"),
    STEP1_CONTACT_PHONE("siv1.step1.contact.phone"),

    STEP2_COUNTRY_CITIZEN("siv1.step2.country-citizen"),
    STEP2_BIRTHDAY("siv1.step2.birthday"),
    STEP2_ID_TYPE("siv1.step2.id.type"),
    STEP2_ID_NUMBER("siv1.step2.id.number"),
    STEP2_ID_EXPIRATION("siv1.step2.id.expiration"),
    STEP2_ID_COUNTRY("siv1.step2.id.country"),

    STEP3_SERVICE_NAME("siv1.step3.service-name"),
    STEP3_SERVICE_LICENSE("siv1.step3.service-license"),
    STEP3_CATEGORIES("siv1.step3.categories"),

    STEP4_BANK_NAME("siv1.step4.bank-name"),
    STEP4_BANK_COUNTRY("siv1.step4.bank-country"),
    STEP4_HOLDER_NAME("siv1.step4.holder-name"),
    STEP4_ACCOUNT_NUMBER("siv1.step4.account-number"),
    STEP4_BANK_BRANCH("siv1.step4.bank-branch");

    companion object {
        operator fun invoke(rawValue: String) = values()?.find { it.err == rawValue } ?: ""
    }
}