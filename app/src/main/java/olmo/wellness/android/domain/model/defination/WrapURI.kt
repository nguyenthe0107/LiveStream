package olmo.wellness.android.domain.model.defination

import android.net.Uri

data class WrapURI(
    val uri: Uri ?= null,
    val typeURI : TypeURI ?= null
)
