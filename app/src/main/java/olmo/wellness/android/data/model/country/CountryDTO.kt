package olmo.wellness.android.data.model.country

import olmo.wellness.android.domain.model.country.Country

data class CountryDTO(
    val id: Int,
    val name: String,
    val phonePrefix: String,
    val flagIconUrl: String
)

fun CountryDTO.toCountryDomain(): Country {
    return Country(
        id = id,
        name = name,
        phonePrefix = phonePrefix,
        flagIconUrl = flagIconUrl
    )
}
