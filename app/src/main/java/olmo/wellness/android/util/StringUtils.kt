package olmo.wellness.android.util

fun String.isPrefix(text: String): Boolean {
    if (this.length > text.length)
        return false

    this.forEachIndexed{ index, char ->
        if (this[index] != text[index]){
            return false
        }
    }
    return true
}

fun String.splitPrefix(prefix: String): Pair<String, String> {
    val loweredFullName = this.lowercase()
    val loweredQuery = prefix.lowercase()

    return when {
        loweredQuery.isEmpty()
                || loweredFullName.first() != loweredQuery.first()
                || loweredFullName.length < loweredQuery.length
        -> {
            "" to this
        }
        else -> {
            if (!loweredQuery.isPrefix(loweredFullName)) {
                "" to this
            }

            this.substring(
                0, prefix.length,
            ) to this.substring(
                prefix.length, this.length
            )
        }
    }
}
