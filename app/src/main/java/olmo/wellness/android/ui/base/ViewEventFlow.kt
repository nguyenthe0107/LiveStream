package olmo.wellness.android.ui.base

import kotlinx.coroutines.flow.Flow

interface ViewEventFlow<I> {

    fun viewEvents(): Flow<I>
}
