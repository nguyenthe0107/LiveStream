package olmo.wellness.android.data.model.chat

import java.util.Date

data class ReactionCount (val countReaction : Int=0, val time : Long = Date().time, val countType: CountType)