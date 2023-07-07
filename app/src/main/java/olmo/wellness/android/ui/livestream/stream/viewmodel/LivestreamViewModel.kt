package olmo.wellness.android.ui.livestream.stream.viewmodel

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.compose.runtime.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.amazonaws.ivs.broadcast.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.data.model.live_stream.LiveStreamRequest
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.domain.use_case.LivestreamUseCase
import java.lang.Exception
import olmo.wellness.android.extension.getAvailableCameraSize
import olmo.wellness.android.extension.getSelectedCamera
import olmo.wellness.android.ui.livestream.stream.data.CameraPosition
import javax.inject.Inject

@HiltViewModel
class LivestreamViewModel @Inject constructor(
    private val livestreamUseCase: LivestreamUseCase,application: Application
) : BaseViewModel(application) {

    private val TAG = LivestreamViewModel::class.java.name
    private val SLOT_CAMERA_NAME = "camera"

    private var broadcastSession: BroadcastSession? = null
    var livestreamId by mutableStateOf<Int?>(null);

    var livestreamList by mutableStateOf(emptyList<LivestreamInfo>())
    var liveStreaming by mutableStateOf(false)

    var previewView by mutableStateOf<View?>(null)

    var refreshing by mutableStateOf(false)

    private var cameraDevice by mutableStateOf<Device.Descriptor?>(null)
    private var captureMode = mutableStateOf(false)

    var disconnectHappened = mutableStateOf(false)

    private var positionCamera by mutableStateOf(CameraPosition.FRONT)

    private val screenCaptureEnabled get() = captureMode.value ?: false
    private val configuration
        get() = if (screenCaptureEnabled)
            Presets.Configuration.GAMING_PORTRAIT
        else Presets.Configuration.STANDARD_PORTRAIT


    fun gotoSetting(context: Context) {
        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        })
    }

    fun startLive() {
        liveStreaming = true
        startBroadcast()
    }

    fun stopLive() {
        liveStreaming = false
        viewModelScope.launch {
            try {
                for (device in broadcastSession!!.listAttachedDevices()) {
                    broadcastSession!!.detachDevice(device)
                }
                livestreamId?.let {
                    //livestreamUseCase.stopLivestream(it)
                    livestreamId = null
                    previewView = null
                    broadcastSession?.stop()
                    broadcastSession?.release()
                    broadcastSession = null
                }
            } catch (throwable: Exception) {
                print(throwable.stackTrace)
            }
        }
    }

    private fun startBroadcast() {
        //--get livestream key, then start board cast
        println("Start broadcast")
        viewModelScope.launch {
            try {
                val request = LiveStreamRequest(title = "test_live")
                livestreamUseCase.requestLivestream(request).collect { res ->
                    livestreamId = res.data?.livestreamId
                    broadcastSession?.start( res.data?.channel,  res.data?.streamKey)
                }
            } catch (throwable: Exception) {
                print(throwable.stackTrace)
            }
        }
    }

    private val broadcastListener = object : BroadcastSession.Listener() {
        override fun onStateChanged(state: BroadcastSession.State) {
            Log.d(ContentValues.TAG, "State=$state")
        }

        override fun onError(exception: BroadcastException) {
            Log.e(ContentValues.TAG, "Exception: $exception")
        }
    }

    fun setSwitchCamera(context: Context) {
        Log.d(TAG, "Camera device changed")
        var position = when (positionCamera) {
            CameraPosition.FRONT -> {
                positionCamera = CameraPosition.BACK
                CameraPosition.BACK.value
            }
            else -> {
                positionCamera = CameraPosition.FRONT
                CameraPosition.FRONT.value
            }
        }
        if (position < context.getAvailableCameraSize()) {
            val device = context.getSelectedCamera(position)
            cameraDevice?.let {
                if (it.deviceId != device.deviceId && broadcastSession != null) {
                    previewView = null
                    try {
                        broadcastSession?.exchangeDevices(it, device) { camera ->
                            displayCameraOutput(camera)
                            cameraDevice = camera.descriptor
                        }
                    } catch (e: BroadcastException) {
                        Log.d(TAG, "Camera exchange exception $e")
                        attachCameraDevice(device, context)
                    }
                }
            }

            if (cameraDevice == null) {
                attachCameraDevice(device, context)
            }

            if (broadcastSession == null) cameraDevice = device
        }
    }

    private fun displayCameraOutput(device: Device) {
        device as ImageDevice
        Log.d(TAG, "Displaying camera output")
        device.previewView?.run {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            previewView = this
        }
    }

    private fun attachCameraDevice(device: Device.Descriptor, context: Context) {
        broadcastSession?.isReady?.let { ready ->
            if (ready) {
                if (!screenCaptureEnabled) {
                    broadcastSession?.attachDevice(device) {
                        broadcastSession?.mixer?.bind(it, SLOT_CAMERA_NAME)
                        cameraDevice = it.descriptor
                        displayCameraOutput(it)
                    }
                }
            } else {
                Log.d(TAG, "Couldn't attach camera device. Session not ready")
                Toast.makeText(context, "error_attach_device", Toast.LENGTH_SHORT).show()
                disconnectHappened.value = true
            }
        }
    }

    fun setupBroadcastSession(applicationContext: Context) {

        broadcastSession = BroadcastSession(
            applicationContext,
            broadcastListener,
            configuration,
            Presets.Devices.FRONT_CAMERA(applicationContext)
        )

        return broadcastSession!!.awaitDeviceChanges {
            for (device in broadcastSession!!.listAttachedDevices()) {
                // Find the camera we attached earlier
                if (device.descriptor.type === Device.Descriptor.DeviceType.CAMERA) {
                    cameraDevice = device.descriptor
                    val preview = (device as ImageDevice).previewView
                    preview.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                    )
                    previewView = preview
                }
            }
        }
    }

}
