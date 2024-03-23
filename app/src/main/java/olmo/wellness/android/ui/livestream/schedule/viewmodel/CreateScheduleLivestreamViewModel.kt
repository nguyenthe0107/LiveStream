package olmo.wellness.android.ui.livestream.schedule.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.enums.UploadFileType
import olmo.wellness.android.data.model.live_stream.LiveStreamRequest
import olmo.wellness.android.data.model.schedule.FillDataSchedule
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.use_case.GetUploadIdServerUrlInfoUseCase
import olmo.wellness.android.domain.use_case.GetUploadUrlInfoUseCase
import olmo.wellness.android.domain.use_case.LivestreamUseCase
import olmo.wellness.android.domain.use_case.UploadFileIdServerUseCase
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.UploadFileIdServerViewModel
import javax.inject.Inject

@HiltViewModel
class CreateScheduleLivestreamViewModel @Inject constructor(
    val livestreamUseCase: LivestreamUseCase,
    application: Application,
    getUploadUrlInfoUseCase: GetUploadIdServerUrlInfoUseCase,
    uploadFileUseCase: UploadFileIdServerUseCase,
    uploadUrlInfoUseCase : GetUploadUrlInfoUseCase,
) : UploadFileIdServerViewModel(application, getUploadUrlInfoUseCase,uploadUrlInfoUseCase ,uploadFileUseCase)  {

    private val _dateTime = MutableStateFlow<Long?>(null)

    private val _uriImageSelect = MutableStateFlow<Uri?>(null)
    val uriImageSelect : StateFlow<Uri?> = _uriImageSelect

    private val _fillDataSchedule = MutableStateFlow(FillDataSchedule())
    val  fillDataSchedule : StateFlow<FillDataSchedule> = _fillDataSchedule

    fun updateSchedule(fillDataSchedule: FillDataSchedule){
        _fillDataSchedule.update {
            fillDataSchedule
        }
    }

    /* Booking Service */
    private val _bookingServiceSelected = MutableStateFlow<List<ServiceBooking>>(emptyList())
    val bookingServiceSelected : StateFlow<List<ServiceBooking>> = _bookingServiceSelected

    fun setUri(uri : Uri?){
        _uriImageSelect.value=uri
        if (uri!=null){
            viewModelScope.launch(Dispatchers.IO) {
                val imageUpload= viewModelScope.async { uploadFile(uri,UploadFileType.OTHER) }
                val resultImage = imageUpload.await()
                if (resultImage!=null){
                    updateSchedule( _fillDataSchedule.value.copy(
                        thumbnailUrl = resultImage
                    ))
                }
            }
        }
    }

    fun setDateTime(dateCreate : Long?){
        _fillDataSchedule.value= fillDataSchedule.value.copy(
            dateCreate = dateCreate
        )
        _dateTime.value= dateCreate
    }

    fun handleRequestLiveStream(context : Context,
        navController: NavController?
    ) {
        viewModelScope.launch {
            val request = LiveStreamRequest(
                title = _fillDataSchedule.value.title,
                description = _fillDataSchedule.value.description,
                isPrivate = _fillDataSchedule.value.isPrivate,
                isEvent = _fillDataSchedule.value.isEvent,
                startTime = _fillDataSchedule.value.dateCreate,
                categoryIds = _fillDataSchedule.value.listCategory?.map { it.id?:0 }?.toList(),
                thumbnailUrl = _fillDataSchedule.value.thumbnailUrl,
                isSchedule = true,
                serviceIds = fillDataSchedule.value.servicesId ?: emptyList()
            )
            livestreamUseCase.requestLivestream(request).collect { res ->
                Log.e("WTF", "handleRequestLiveStream: ".plus("-->from CreateScheduleLivestreamViewModel"))
                res.onResultReceived(
                    onSuccess = {
                        navController?.popBackStack()
                    },
                    onError = {
                           it?.let {
                               Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
                           }
                    },
                )
            }

        }

    }

    fun bindSelectedServiceBooking(serviceList: List<ServiceBooking>){
        _bookingServiceSelected.value = serviceList
        if(serviceList.isNotEmpty()){
            val listId: List<Int> = serviceList.map {
                it.id ?: 0
            }
            _fillDataSchedule.update {
                it.copy(
                    servicesId = listId
                )
            }
        }else{
            _fillDataSchedule.update {
                it.copy(
                    servicesId = null
                )
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}