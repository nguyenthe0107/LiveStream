package olmo.wellness.android.data.model.live_stream

import olmo.wellness.android.domain.model.livestream.HashTag

data class HashTagDTO(
    val id: Int?,
    val maxViewCount: Int?,
    val name: String?
)

fun HashTagDTO.toDomain(): HashTag {
    return HashTag(this.id, this.maxViewCount, this.name)
}