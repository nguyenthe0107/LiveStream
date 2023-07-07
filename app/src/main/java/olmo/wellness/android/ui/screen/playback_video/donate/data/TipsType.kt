package olmo.wellness.android.ui.screen.playback_video.donate.data

import olmo.wellness.android.R


sealed class TipsType(val id: Int, val name: String, val resource: Int, val value: Float) {

    object ThumbUp : TipsType(1, "Thumb up", R.drawable.ic_lucky_crystal, 5f)
    object SendingLove : TipsType(2, "Sending love", R.drawable.ic_lucky_crystal, 10f)
    object LuckyCrystal : TipsType(3, "Lucky Crystal", R.drawable.ic_lucky_crystal, 99f)
    object PurpleCrystal : TipsType(4, "Purple Crystal", R.drawable.ic_purple_crystal, 199f)
    object HealthCrystal : TipsType(5, "Health Crystal", R.drawable.ic_healthy_crystal, 199f)
    object HealingCrystal : TipsType(6, "Healing Crystal", R.drawable.ic_healing_crystal, 199f)
    object GoldenBox : TipsType(7, "Golden Box", R.drawable.ic_gloden_box, 1499f)
    object DiamondBox : TipsType(8, "Diamond Box", R.drawable.ic_diamond_box, 2150f)
}