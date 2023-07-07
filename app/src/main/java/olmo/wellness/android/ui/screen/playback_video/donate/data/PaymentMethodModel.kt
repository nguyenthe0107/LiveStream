package olmo.wellness.android.ui.screen.playback_video.donate.data

data class PaymentMethodModel(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    var selected: Boolean ?= false
)
