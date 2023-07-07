package olmo.wellness.android.ui.booking.list_service.event

import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.ui.chat.private_chat.event.PrivateChatEvent

data class ListOfServiceState(
    val showLoading: Boolean = false,
    val listOfServices: List<ServiceBooking>?=null
)

sealed class ListOfServiceEvent {
    data class ShowLoading(val isLoading: Boolean = false) : ListOfServiceEvent()

    data class UpdateListOfServices(val data: List<ServiceBooking>) : ListOfServiceEvent()
}