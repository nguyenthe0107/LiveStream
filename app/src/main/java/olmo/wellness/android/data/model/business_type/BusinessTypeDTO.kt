package olmo.wellness.android.data.model.business_type

import olmo.wellness.android.domain.model.business_type.BusinessType

data class BusinessTypeDTO(
    val id: Int,
    val name: String
)

fun BusinessTypeDTO.toBusinessType() = BusinessType(id = id, name = name)
