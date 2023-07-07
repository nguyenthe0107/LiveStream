package olmo.wellness.android.ui.chat.conversation_list

import olmo.wellness.android.R
import olmo.wellness.android.domain.model.chat.ConversationInfo
import olmo.wellness.android.domain.model.notification.NotificationInfo

object DataProvider {
//    val tweet = NotificationInfo(
//        1,
//        "Jetpack compose is the next thing for andorid. Declarative UI is the way to go for all screens.",
//        "The Verge",
//        "@verge",
//        "12m",
//        R.drawable.food1,
//        100,
//        12,
//        15,
//        "Twitter for web"
//    )

    val room = ConversationInfo(
        id = 1,
        name = "Chelsea",
        avatar = "",
        isOnline = true, lastDate = "12m",
        lastMessage = "Hom nay la ..."
    )

    val roomList = listOf<ConversationInfo>(
        room, room.copy(id = 2, name = "Mu", isOnline = false),
        room.copy(id = 2, name = "Liv", isOnline = false),
        room.copy(id = 3, name = "Arc", isOnline = false),
        room.copy(id = 4, name = "Bar", isOnline = false),
        room.copy(id = 5, name = "Rea", isOnline = false),
        room.copy(id = 6, name = "News", isOnline = false)
    )


//    val tweetList = listOf(
//        tweet.copy(
//            1,
//            "Jetpack compose is the next thing for andorid. Declarative UI is the way to go for all screens.",
//            "The Verge",
//            "@verge",
//            "12m",
//            R.drawable.food1,
//            100,
//            12,
//            15,
//            "Twitter for web"
//        ),
//        tweet.copy(
//            id = 2,
//            author = "Google",
//            handle = "@google",
//            authorImageId = R.drawable.p1,
//            tweetImageId = R.drawable.food16,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 3,
//            author = "Amazon",
//            handle = "@Amazon",
//            authorImageId = R.drawable.p2,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 4,
//            author = "Facebook",
//            handle = "@Facebook",
//            authorImageId = R.drawable.p3,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 5,
//            author = "Instagram",
//            handle = "@Instagram",
//            authorImageId = R.drawable.p4,
//            tweetImageId = R.drawable.food15,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 6,
//            author = "Twitter",
//            handle = "@Twitter",
//            authorImageId = R.drawable.p5,
//            tweetImageId = R.drawable.food3,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 7,
//            author = "Netflix",
//            handle = "@Netflix",
//            authorImageId = R.drawable.p6,
//            tweetImageId = R.drawable.food4,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 8,
//            author = "Tesla",
//            handle = "@Tesla",
//            authorImageId = R.drawable.p7,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 9,
//            author = "Microsoft",
//            handle = "@Microsoft",
//            authorImageId = R.drawable.p8,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 3,
//            author = "Tencent",
//            handle = "@Tencent",
//            authorImageId = R.drawable.p9,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 4,
//            author = "Snapchat",
//            handle = "@Snapchat",
//            authorImageId = R.drawable.p10,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 5,
//            author = "Snapchat",
//            handle = "@Snapchat",
//            authorImageId = R.drawable.p11,
//            tweetImageId = R.drawable.food5,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 6,
//            author = "Tiktok",
//            handle = "@Tiktok",
//            authorImageId = R.drawable.p1,
//            tweetImageId = R.drawable.food6,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 7,
//            author = "Samsung",
//            handle = "@Samsung",
//            authorImageId = R.drawable.p2,
//            tweetImageId = R.drawable.food7,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 8,
//            author = "Youtube",
//            handle = "@Youtube",
//            authorImageId = R.drawable.p3,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 9,
//            author = "Gmail",
//            handle = "@Gmail",
//            authorImageId = R.drawable.p4,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 3,
//            author = "Android",
//            handle = "@Android",
//            authorImageId = R.drawable.p5,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 4,
//            author = "Whatsapp",
//            handle = "@Whatsapp",
//            authorImageId = R.drawable.p6,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 5,
//            author = "Telegram",
//            handle = "@Telegram",
//            authorImageId = R.drawable.p7,
//            tweetImageId = R.drawable.food8,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 6,
//            author = "Spotify",
//            handle = "@Spotify",
//            authorImageId = R.drawable.p8,
//            tweetImageId = R.drawable.food9,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 7,
//            author = "WeChat",
//            handle = "@WeChat",
//            authorImageId = R.drawable.p9,
//            tweetImageId = R.drawable.food10,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 8,
//            author = "Airbnb",
//            handle = "@Airbnb",
//            authorImageId = R.drawable.p10,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 9,
//            author = "LinkedIn",
//            handle = "@LinkedIn",
//            authorImageId = R.drawable.p11,
//            time = "1h"
//        ),
//        tweet.copy(
//            id = 6,
//            author = "Shazam",
//            handle = "@Shazam",
//            authorImageId = R.drawable.p8,
//            tweetImageId = R.drawable.food11,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 7,
//            author = "Chrome",
//            handle = "@Chrome",
//            authorImageId = R.drawable.p9,
//            tweetImageId = R.drawable.food12,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 6,
//            author = "Safari",
//            handle = "@Safari",
//            authorImageId = R.drawable.p8,
//            tweetImageId = R.drawable.food13,
//            time = "11m"
//        ),
//        tweet.copy(
//            id = 7,
//            author = "Disney",
//            handle = "@Disney",
//            authorImageId = R.drawable.p9,
//            tweetImageId = R.drawable.food14,
//            time = "11m"
//        )
//
//
//    )
    const val longText =
        "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae"

}