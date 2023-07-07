/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Stream License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/GetStream/stream-chat-android/blob/main/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package olmo.wellness.android.ui.common.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.Color_DOT_SLIDE_ACTIVE
import olmo.wellness.android.ui.theme.White

/**
 * Component that represents an online indicator to be used with
 * [io.getstream.chat.android.compose.ui.components.avatar.UserAvatar].
 *
 * @param modifier Modifier for styling.
 */
@Composable
public fun OnlineIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(10.dp)
//            .background(White, CircleShape)
//            .padding(2.dp)
            .background(Color_DOT_SLIDE_ACTIVE, CircleShape)
    )
}
