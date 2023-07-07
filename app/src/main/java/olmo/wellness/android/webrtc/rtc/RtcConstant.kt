package olmo.wellness.android.webrtc.rtc

object RtcConstant {
    const val PLAIN="__PLAIN__"
    const val READY="__READY__"
    const val PING_PONG="__PING_PONG__"
    const val TYPE="type"
    const val DATA="data"
    const val CHANNEL="channel"
    const val MESSAGE="message"
    const val MESSAGES="messages"
    const val ROOM="room"
    const val USER="user"
    const val USER_ID="userId"
    const val COUNT_REACTION="countReaction"
    const val RECORDS="records"

    // livestream
    const val INFO_USER="InfoUser"
    const val JOIN_ROOM_LIVE_STREAM ="JoinRoomLiveStream"
    const val LOAD_MESSAGE_LIVE_STREAM="LoadMessageLiveStream"
    const val PRIVATE_CHAT_LIVE_STREAM="PrivateChatLiveStream"
    const val INFO_ROOM_LIVE_STEAM="InfoRoomLiveStream"
    const val LEAVE_ROOM_LIVE_STREAM="LeaveRoomLiveStream"
    const val UPDATE_REACTION_LIVE_STREAM="UpdateReactionLivestream"
    const val UPDATE_REACTION_MESSAGE_LIVE_STREAM="UpdateReactionMessageLivestream"
    const val SEND_TIP_PACKAGE_LIVESTREAM="SendTipPackageLivestream"

    // RoomChat
    const val INFO_ROOM_CHAT="InfoRoomChat"
    const val JOIN_ROOM_CHAT ="JoinRoomChat"
    const val LOAD_MESSAGE_CHAT ="LoadMessageChat"
    const val PRIVATE_CHAT ="PrivateChat"
    const val LEAVE_ROOM_CHAT="LeaveRoomChat"

    //Booking Service
    const val ADD_SERVICE_TO_LIVESTREAM="AddServiceToLivestream"
    const val BOOK_MARK_SERVICE_OF_LIVESTREAM="BookmarkServiceOfLivestream"
    const val DELETE_SERVICE_OF_LIVESTREAM="DeleteServiceOfLivestream"
    const val UPDATE_INDEX_SERVICE_TO_LIVESTREAM="UpdateIndexServiceToLivestream"
    const val REPLACE_ALL_SERVICE_TO_LIVESTREAM="ReplaceAllServiceToLivestream"
    const val GET_SERVICE_OF_LIVESTREAM="GetServiceOfLivestream"
}