package olmo.wellness.android.core.enums

import olmo.wellness.android.R

sealed class DocumentType(val type: String, val name: Int) {
    object BankAccount : DocumentType("BANK_STATEMENT", R.string.bank_account_statement)
    object CardAccount : DocumentType("CARD_STATEMENT", R.string.card_account_statement)
}