package olmo.wellness.android.domain.model.wrapper

import olmo.wellness.android.data.model.definition.AuthMethod
import java.io.Serializable

data class VerifyCodeWrapper(
    var identity: String ?= null,
    val password: String ?= null,
    val isForget: Boolean ?= false,
    val isPhone: Boolean ?= false,
    val userName: String ?= "",
    val userType: String ?= "",
    val authData: ArrayList<String> ?= null,
    val authMethod: AuthMethod?= null,
    val caseNotVerified : Boolean ?= null,
    val tokenDevice : String ?= null
): Serializable
