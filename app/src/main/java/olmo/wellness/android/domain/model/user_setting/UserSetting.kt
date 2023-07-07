package olmo.wellness.android.domain.model.user_setting

import olmo.wellness.android.data.model.user_setting.UserSettingDTO

data class UserSetting(
    val id: Int?=null,
    val userId: List<Int> ?=null,
    val createdAt: Long ?=null,
    val lastModified : Long ?=null,
    val isPrivateActivity : Boolean ?= null,
    val isAllowContactSyncing : Boolean ?= null,
    val isAllowShareProfile : Boolean ?= null,
    val language : String ?= null,
    val isAllowChatOnLivestream : Boolean ?= null,
    val isAllowPrivateChatOnLivestream : Boolean ?= null,
    val isAllowSendAutoReply : Boolean ?= null,
    val autoReply : String ?= null,
    val isShowMessageShortcut : Boolean ?= null,
    val serviceUpdate : Boolean ?= null,
    val orderUpdate : Boolean ?= null,
    val yourCircle : Boolean ?= null,
    val promotions : Boolean ?= null,
    val olmoFeed : Boolean ?= null,
    val livestream : Boolean ?= null,
    val walletUpdate : Boolean ?= null
)

fun UserSetting.toUserSettingDTO(): UserSettingDTO {
    return UserSettingDTO(
        id = id,
        createdAt = createdAt,
        lastModified = lastModified,
        isPrivateActivity = isPrivateActivity,
        isAllowContactSyncing = isAllowContactSyncing,
        isAllowShareProfile= isAllowShareProfile,
        language = language,
        isAllowChatOnLivestream =isAllowChatOnLivestream,
        isAllowPrivateChatOnLivestream = isAllowPrivateChatOnLivestream,
        isAllowSendAutoReply = isAllowSendAutoReply,
        autoReply = autoReply,
        isShowMessageShortcut = isShowMessageShortcut,
        serviceUpdate = serviceUpdate,
        orderUpdate = orderUpdate,
        yourCircle = yourCircle,
        promotions = promotions,
        olmoFeed =olmoFeed,
        livestream = livestream,
        walletUpdate = walletUpdate
    )
}
