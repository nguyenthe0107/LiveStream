package olmo.wellness.android.core.enums

sealed class DocumentTypeUri(val type: String) {
    object BusinessLicense : DocumentTypeUri("BusinessLicense")
    object BrandLicense : DocumentTypeUri("BrandLicense")
    object ElectricityBill : DocumentTypeUri("ElectricityBill")
    object HistorySellReport : DocumentTypeUri("HistorySellReport")
}