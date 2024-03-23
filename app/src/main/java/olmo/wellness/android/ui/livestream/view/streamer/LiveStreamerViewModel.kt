package olmo.wellness.android.ui.livestream.view.streamer

import ai.deepar.ar.*
import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.CountDownTimer
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.amazonaws.ivs.broadcast.*
import com.amazonaws.ivs.broadcast.BroadcastConfiguration.Mixer.Slot
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.live_stream.LiveStreamRequest
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.request.GetProfileRequest
import olmo.wellness.android.domain.use_case.BookingUseCase
import olmo.wellness.android.domain.use_case.GetProfileUseCase
import olmo.wellness.android.domain.use_case.LivestreamUseCase
import olmo.wellness.android.domain.use_case.socket.booking.SendAddServiceUseCase
import olmo.wellness.android.extension.getAvailableCameraSize
import olmo.wellness.android.extension.getSelectedCamera
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.livestream.schedule.data.Person
import olmo.wellness.android.ui.livestream.stream.config.ConfigVideoStream
import olmo.wellness.android.ui.livestream.stream.config.TAG
import olmo.wellness.android.ui.livestream.stream.data.CameraPosition
import olmo.wellness.android.ui.livestream.stream.data.LivestreamStatus
import olmo.wellness.android.ui.livestream.stream.data.UpdateLivestreamRequest
import olmo.wellness.android.ui.livestream.stream.data.UpdateLivestreamWrapRequest
import olmo.wellness.android.ui.livestream.view.ar.*
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.playback_video.bottom_sheet.broadcast_audience.AudienceType
import olmo.wellness.android.webrtc.rtc.RtcUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ExecutionException
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

const val SLOT_CAMERA_NAME = "camera"

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class LiveStreamerViewModel @Inject constructor(
    application: Application,
    private val bookingUseCase: BookingUseCase,
    private val livestreamUseCase: LivestreamUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val addServiceUseCase: SendAddServiceUseCase,
) : BaseViewModel(application), AREventListener {

    private var broadcastSession: BroadcastSession? = null

    private lateinit var broadcastConfig: BroadcastConfiguration
    var livestreamId by mutableStateOf<Int?>(null)

    var livestreamList by mutableStateOf(emptyList<LivestreamInfo>())
    var liveStreaming by mutableStateOf(false)

    private var _previewView: MutableStateFlow<View?> = MutableStateFlow(null)
    var previewView: StateFlow<View?> = _previewView

//    private var _serviceBookings : MutableStateFlow<List<ServiceBooking>> = MutableStateFlow(emptyList())
//    var serviceBookings : StateFlow<List<ServiceBooking>> = _serviceBookings

    var refreshing by mutableStateOf(false)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun setLoading(status: Boolean) {
        _isLoading.value = status
    }

    private var _roomLSID: MutableStateFlow<String?> = MutableStateFlow(null)
    val roomLSID: StateFlow<String?> = _roomLSID

    private val _totalFollower: MutableStateFlow<Int> = MutableStateFlow(0)
    val totalFollower: StateFlow<Int> = _totalFollower

    private val _profile = MutableStateFlow<ProfileInfo?>(ProfileInfo())
    val profile: StateFlow<ProfileInfo?> = _profile

    /* Setup Camera */
    private var cameraDevice by mutableStateOf<Device.Descriptor?>(null)
    private var captureMode = mutableStateOf(false)
    private var positionCamera by mutableStateOf(CameraPosition.FRONT)
    private val screenCaptureEnabled get() = captureMode.value ?: false

    var disconnectHappened = mutableStateOf(false)
    var isStartStream = mutableStateOf(false)

    private val defaultLensFacing = CameraSelector.LENS_FACING_FRONT
    private var surfaceProvider: ARSurfaceProvider? = null

    private var lensFacing = defaultLensFacing
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null

    private var CUSTOM_SLOT = "custom"

    private lateinit var buffers: Array<ByteBuffer?>

    private var currentBuffer = 0
    private val NUMBER_OF_BUFFERS = 2

    private var deepAR: DeepAR? = null

    private var currentMask = 0
    private var currentEffect = 0
    private var currentFilter = 0

    private var masks = getMasks
    private var effects = getEffects
    private var filters = getFilters

    private val activeFilterType = 0

    private var width = 0
    private var height = 0

    private var surfaceSource: SurfaceSource? = null
    private var surface: Surface? = null
    private val useExternalCameraTexture = true

    /* Booking Service */
    private val _bookingServiceSelectedFromListService =
        MutableStateFlow<List<ServiceBooking>>(emptyList())
    val bookingServiceSelectedFromListService: StateFlow<List<ServiceBooking>> =
        _bookingServiceSelectedFromListService

    // To Show LiveStrewam
    private val _bookingServiceShowing = MutableStateFlow<ServiceBooking?>(null)
    val bookingServiceShowing: StateFlow<ServiceBooking?> = _bookingServiceShowing

    init {
        getProfile()
    }

    fun initializeDeepAR(context: MainActivity, lifecycleOwner: LifecycleOwner) {
//        deepAR = DeepAR(context)
//        deepAR?.setLicenseKey(context.getString(R.string.key_deepar))
//        deepAR?.initialize(context, this)
        setupIVS(context)
//        setupCamera(context, lifecycleOwner)
    }

    fun gotoNext(filter: FilterAR) {

        when (filter.type) {
            TypeFilter.MASK -> {
//                currentMask = (currentMask + 1) % masks!!.size
                deepAR?.switchEffect("mask", getFilterPath(filter.name))
                deepAR?.switchEffect("effect", getFilterPath("none"))
                deepAR?.switchEffect("filter", getFilterPath("none"))
            }
            TypeFilter.EFFECT -> {
//                currentEffect = (currentEffect + 1) % effects!!.size
                deepAR?.switchEffect("effect", getFilterPath(filter.name))
                deepAR?.switchEffect("filter", getFilterPath("none"))
                deepAR?.switchEffect("mask", getFilterPath("none"))
            }
            TypeFilter.FILTER -> {
//                currentFilter = (currentFilter + 1) % filters!!.size
                deepAR?.switchEffect("filter", getFilterPath(filter.name))
                deepAR?.switchEffect("mask", getFilterPath("none"))
                deepAR?.switchEffect("effect", getFilterPath("none"))
            }
        }
    }

    private fun setupCamera(context: MainActivity, lifecycleOwner: LifecycleOwner) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture?.addListener(Runnable {
            try {
                val cameraProvider = cameraProviderFuture?.get()
                bindImageAnalysis(cameraProvider, context, lifecycleOwner)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindImageAnalysis(
        cameraProvider: ProcessCameraProvider?,
        context: MainActivity,
        lifecycleOwner: LifecycleOwner,
    ) {
        val cameraResolutionPreset = CameraResolutionPreset.P1280x720
        val width: Int
        val height: Int
        val orientation: Int = getScreenOrientation(context)
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            width = cameraResolutionPreset.width
            height = cameraResolutionPreset.height
        } else {
            width = cameraResolutionPreset.height
            height = cameraResolutionPreset.width
        }
        val cameraResolution = Size(width, height)
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        if (useExternalCameraTexture) {
            val preview = Preview.Builder()
                .setTargetResolution(cameraResolution)
                .build()
            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            if (surfaceProvider == null) {
                surfaceProvider = deepAR?.let { ARSurfaceProvider(context, it) }
            }
            preview.setSurfaceProvider(surfaceProvider)
            surfaceProvider?.setMirror(lensFacing == CameraSelector.LENS_FACING_FRONT)
        } else {
            buffers = arrayOfNulls(NUMBER_OF_BUFFERS)

            for (i in 0 until NUMBER_OF_BUFFERS) {
                buffers[i] = ByteBuffer.allocateDirect(width * height * 3)
                buffers[i]?.order(ByteOrder.nativeOrder())
                buffers[i]?.position(0)
            }
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(cameraResolution)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), imageAnalyzer)
            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis)
        }
    }

    private val imageAnalyzer =
        ImageAnalysis.Analyzer { image ->
            val byteData: ByteArray
            val yBuffer = image.planes[0].buffer
            val uBuffer = image.planes[1].buffer
            val vBuffer = image.planes[2].buffer
            val ySize = yBuffer.remaining()
            val uSize = uBuffer.remaining()
            val vSize = vBuffer.remaining()
            byteData = ByteArray(ySize + uSize + vSize)

            //U and V are swapped
            yBuffer[byteData, 0, ySize]
            vBuffer[byteData, ySize, vSize]
            uBuffer[byteData, ySize + vSize, uSize]
            buffers[currentBuffer]!!.put(byteData)
            buffers[currentBuffer]!!.position(0)
            if (deepAR != null) {
                deepAR?.receiveFrame(
                    buffers[currentBuffer],
                    image.width, image.height,
                    image.imageInfo.rotationDegrees,
                    lensFacing == CameraSelector.LENS_FACING_FRONT,
                    DeepARImageFormat.YUV_420_888,
                    image.planes[1].pixelStride
                )
            }
            currentBuffer = (currentBuffer + 1) % NUMBER_OF_BUFFERS
            image.close()
        }

    private fun getScreenOrientation(context: MainActivity): Int {
        val rotation: Int = context.windowManager.defaultDisplay.rotation
        val dm = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)
        width = dm.widthPixels
        height = dm.heightPixels
        // if the device's natural orientation is portrait:
        val orientation: Int = if ((rotation == Surface.ROTATION_0
                    || rotation == Surface.ROTATION_180) && height > width ||
            (rotation == Surface.ROTATION_90
                    || rotation == Surface.ROTATION_270) && width > height
        ) {
            when (rotation) {
                Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        } else {
            when (rotation) {
                Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                else -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
        return orientation
    }

    private fun setupIVS(activity: MainActivity) {
        val streamingWidth = ConfigVideoStream.bigSize.y.toInt()
        val streamingHeight = ConfigVideoStream.bigSize.x.toInt()

        broadcastConfig = BroadcastConfiguration.with { broad: BroadcastConfiguration ->
            broad.video.setSize(streamingWidth, streamingHeight)
            broad.mixer.slots = arrayOf(
                Slot.with { slot: Slot ->
                    slot.preferredVideoInput = Device.Descriptor.DeviceType.CAMERA
                    slot.preferredAudioInput = Device.Descriptor.DeviceType.MICROPHONE
                    slot.aspect = BroadcastConfiguration.AspectMode.FILL
//                    slot.name = CUSTOM_SLOT
                    slot
                }
            )
            broad
        }

        broadcastSession = BroadcastSession(
            activity,
            broadcastListener,
            broadcastConfig,
            Presets.Devices.FRONT_CAMERA(context)
        )

//        surfaceSource = broadcastSession?.createImageInputSource()
//        check(surfaceSource?.isValid == true) { "Amazon IVS surface not valid!" }
//        surfaceSource?.setSize(streamingWidth, streamingHeight)
//        surfaceSource?.setRotation(ImageDevice.Rotation.ROTATION_0)
//        surface = surfaceSource?.inputSurface
//        deepAR?.setRenderSurface(surface, streamingWidth, streamingHeight)

        broadcastSession?.awaitDeviceChanges {
//        val preview = broadcastSession?.getPreviewView(BroadcastConfiguration.AspectMode.FILL) as TextureView
//        _previewView.value = preview

            for (device in broadcastSession!!.listAttachedDevices()) {
                // Find the camera we attached earlier
                if (device.descriptor.type === Device.Descriptor.DeviceType.CAMERA) {
                    cameraDevice = device.descriptor
                    val preview =
                        (device as ImageDevice).getPreviewView(BroadcastConfiguration.AspectMode.FILL)
                    _previewView.value = preview
                }
            }

        }
    }

    private suspend fun clearFilter() {
        withContext(Dispatchers.Main) {
            var cameraProvider: ProcessCameraProvider? = null
            try {
                cameraProvider = cameraProviderFuture!!.get()
                cameraProvider.unbindAll()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            if (surfaceProvider != null) {
                surfaceProvider!!.stop()
                surfaceProvider = null
            }
            deepAR?.release()
            deepAR = null

            if (surfaceProvider != null) {
                surfaceProvider?.stop()
            }
            if (deepAR == null) {
                return@withContext
            }
            deepAR?.setAREventListener(null)
            deepAR?.release()
            surfaceSource = null
            surface = null
            deepAR = null
        }
    }

    fun gotoSetting(context: Context) {
        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        })
    }

    fun startLive(rqt: LiveStreamRequest, startSuccess: () -> Unit) {
        if (!liveStreaming) {
            liveStreaming = true
            startBroadcast(rqt, startSuccess)
        }
    }

    fun stopLive() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _roomLSID.value = null
            _time.value = maxProcess
            isStartStream.value = false
            liveStreaming = false
            withContext(Dispatchers.Main) {
//                clearFilter()
                if (broadcastSession != null) {
                    try {
                        for (device in broadcastSession?.listAttachedDevices()!!) {
                            broadcastSession?.detachDevice(device)
                        }
                        if (livestreamId == null && livestreamList.isNotEmpty()) {
                            livestreamId =
                                livestreamList.findLast { it.userId == sharedPrefs.getUserInfoLocal().userId }?.id
                            livestreamId?.let {
                                handleStopLiveStream(it)
                            }
                        } else {
                            livestreamId?.let {
                                handleStopLiveStream(it)
                            }
                        }
                        _isLoading.value = false
                    } catch (throwable: Exception) {
                        print(throwable.stackTrace)
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    private fun createUpdateLiveRequest(): UpdateLivestreamWrapRequest {
        val request = UpdateLivestreamRequest(status = LivestreamStatus.FINISHED, isRecord = true)
        return UpdateLivestreamWrapRequest(request)
    }

    private fun handleStopLiveStream(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val listId = listOf(it)
            val query = "{\"id\":$listId}"
            livestreamUseCase.updateStatusLivestream(query, createUpdateLiveRequest())
                .collectLatest {
                    withContext(Dispatchers.Main) {
                        livestreamId = null
                        _previewView.value = null
                        _isLoading.value = false
                        broadcastSession?.stop()
                        broadcastSession?.release()
                    }
                }
        }
    }

    private fun startBroadcast(rqt: LiveStreamRequest, startSuccess: () -> Unit) {
        //--get livestream key, then start board cast
        println("Start broadcast")
        viewModelScope.launch(Dispatchers.Default) {
            try {
                _isLoading.value = true
                if (livestreamList.isEmpty()) {
                    handleRequestLiveStream(rqt, startSuccess)
                } else {
                    val lastLive =
                        livestreamList.findLast { it.userId == sharedPrefs.getUserInfoLocal().userId }
                    if (lastLive != null) {
                        if (lastLive.status?.trim() == LivestreamStatus.FINISHED.name) {
                            handleRequestLiveStream(rqt, startSuccess)
                        } else {
                            viewModelScope.launch {
                                try {
                                    val liveInfoId = lastLive.id
                                    liveInfoId?.let {
                                        val listId = listOf(it)
                                        val query = "{\"id\":$listId}"
                                        livestreamUseCase.updateStatusLivestream(
                                            query,
                                            createUpdateLiveRequest()
                                        ).collectLatest {
                                            handleRequestLiveStream(rqt, startSuccess)
                                        }
                                    }
                                    _isLoading.value = false
                                } catch (throwable: Exception) {
                                    print(throwable.stackTrace)
                                    _isLoading.value = false
                                }
                            }
                        }
                    } else {
                        handleRequestLiveStream(rqt, startSuccess)
                    }
                }
            } catch (throwable: Exception) {
                print(throwable.stackTrace)
                _isLoading.value = false
            }
        }
    }

    private suspend fun handleRequestLiveStream(rqt: LiveStreamRequest, startSuccess: () -> Unit) {
        livestreamUseCase.requestLivestream(rqt).collect { res ->
            res.onResultReceived(
                onLoading = {
                    _isLoading.value = true
                },
                onSuccess = {
                    Log.e("Live", "handleRequestLiveStream: ${it?.channel} -- ${it?.streamKey}" )
                    livestreamId = it?.livestreamId
                    _roomLSID.value = it?.roomChatId
                    viewModelScope.launch(Dispatchers.Main) {
                        broadcastSession?.start(it?.channel, it?.streamKey)
                    }
                    _isLoading.value = false
                    viewModelScope.launch(Dispatchers.IO) {
                        sharedPrefs.setTimeLiveCreated(System.currentTimeMillis())
                    }
                    startSuccess.invoke()
                },
                onError = {
                    _isLoading.value = false
                }
            )
        }
    }

    private var broadcastListener = object : BroadcastSession.Listener() {
        override fun onStateChanged(state: BroadcastSession.State) {
            Log.d(ContentValues.TAG, "State=$state")
        }

        override fun onError(exception: BroadcastException) {
            Log.e(ContentValues.TAG, "Exception: $exception")
        }
    }

    fun fetchListOfStreams() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val status = "[".plus("\"ON_LIVE\"").plus("]")
                val query = "{\"status\":$status}"
                val projection =
                    "[\"id\",\"createdAt\",\"lastModified\",\"userId\",\"status\",\"playbackUrl\",\"channel\",\"streamKey\",\"isPrivate\"]"
                livestreamUseCase.getLivestreams(query, projection).collect { res ->
                    livestreamList = res.data ?: emptyList()
                    _isLoading.value = false
                }
            } catch (throwable: Exception) {
                print(throwable.stackTrace)
                _isLoading.value = false
            }
        }
    }

    fun setSwitchCamera(context: MainActivity, lifecycleOwner: LifecycleOwner) {
        Log.d(TAG, "Camera device changed")
        lensFacing =
            if (lensFacing == CameraSelector.LENS_FACING_FRONT) CameraSelector.LENS_FACING_BACK else CameraSelector.LENS_FACING_FRONT
        //unbind immediately to avoid mirrored frame.
        var cameraProvider: ProcessCameraProvider? = null
        try {
            cameraProvider = cameraProviderFuture!!.get()
            cameraProvider.unbindAll()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        setupCamera(context, lifecycleOwner)
        setLoading(false)
    }

    private fun displayCameraOutput(device: Device) {
        device as ImageDevice
        device.getPreviewView(BroadcastConfiguration.AspectMode.FILL)?.run {
            _previewView.value = this
            _isLoading.value = false
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
                Log.d("TAG", "Couldn't attach camera device. Session not ready")
                Toast.makeText(context, "error_attach_device", Toast.LENGTH_SHORT).show()
                disconnectHappened.value = true
            }
        }
    }

    fun setSwitchCamera(context: Context) {
        val position = when (positionCamera) {
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
                    _previewView.value = null
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

    private fun showLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }

    /* Timer */
    private val step = 1000L
    private val maxDuration = 4000L
    val maxProcess = 3

    private val _time: MutableStateFlow<Int> = MutableStateFlow(maxProcess)
    val time: StateFlow<Int> = _time

    val timer = object : CountDownTimer(maxDuration, step) {
        override fun onTick(millisUntilFinished: Long) {
            val current = millisUntilFinished / step
            if (current <= 0) {
                onFinish()
            } else {
                _time.value = current.toInt()
            }
        }

        override fun onFinish() {
            isStartStream.value = true
        }

    }


    private val _personSee = MutableStateFlow<Person?>(Person(1, AudienceType.EVERYONE.value))
    val personSee: StateFlow<Person?> = _personSee


    fun setPersonSee(type: AudienceType) {
        when (type) {
            AudienceType.EVERYONE -> {
                _personSee.value = Person(1, AudienceType.EVERYONE.value)
            }
            AudienceType.FOLLOWERS -> {
                _personSee.value = Person(2, AudienceType.FOLLOWERS.value)
            }
        }
    }

    private fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val userLocal = sharedPrefs.getUserInfoLocal()
            val filed =
                "[\"name\",\"bio\",\"gender\",\"birthday\",\"avatar\",\"phoneNumber\",\"email\"]"
            val profileRequest = GetProfileRequest(userLocal.userId, filed)
            getProfileUseCase.invoke(GetProfileUseCase.Params(profileRequest)).collectLatest {
                when (it) {
                    is Result.Success -> {
                        if (it.data?.isNotEmpty() == true) {
                            val response = it.data.last()
                            _profile.value = response
                        }
                        _isLoading.value = false
                        sharedPrefs.clearTimeCreatedLive()
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }

    override fun screenshotTaken(p0: Bitmap?) {
    }

    override fun videoRecordingStarted() {
    }

    override fun videoRecordingFinished() {
    }

    override fun videoRecordingFailed() {
    }

    override fun videoRecordingPrepared() {
    }

    override fun shutdownFinished() {
    }

    override fun initialized() {

        // Restore effect state after deepar release
        deepAR?.switchEffect("mask", getFilterPath(masks[currentMask]))
        deepAR?.switchEffect("effect", getFilterPath(effects[currentEffect]))
        deepAR?.switchEffect("filter", getFilterPath(filters[currentFilter]))
    }

    override fun faceVisibilityChanged(p0: Boolean) {
    }

    override fun imageVisibilityChanged(p0: String?, p1: Boolean) {
    }

    override fun frameAvailable(p0: Image?) {
    }

    override fun error(p0: ARErrorType?, p1: String?) {
    }

    override fun effectSwitched(p0: String?) {
    }

    private fun getFilterPath(filterName: String): String? {
        return if (filterName == "none") {
            null
        } else "file:///android_asset/$filterName"
    }

    fun bindSelectedServiceBooking(serviceList: List<ServiceBooking>) {
        _bookingServiceSelectedFromListService.value = serviceList
    }

    fun sendAddService(
        serviceList: List<ServiceBooking>?, roomId: String?,
        liveStreamId: Int?,
    ) {
        RtcUtils.addServiceLivestream(roomId=roomId, liveStreamId = liveStreamId,serviceList= serviceList, addServiceUseCase = addServiceUseCase)
    }


    fun updateStatusSelectedServiceBooking(serviceSelected: ServiceBooking?) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _bookingServiceSelectedFromListService.value = bookingServiceSelectedFromListService.value.map { oldInfo ->
//                if(serviceSelected?.id == oldInfo.id){
//                    oldInfo.copy(bookmark = 1)
//                }else{
//                    oldInfo
//                }
//            }
//            _bookingServiceShowing.update {
//                serviceSelected
//            }
        _bookingServiceShowing.value = serviceSelected
    }
//    }

//    private fun getServiceLivestream(livestreamId : Int?){
//        viewModelScope.launch(Dispatchers.IO)  {
//            bookingUseCase.getServicesLivestream(livestreamId = livestreamId).collectLatest { res->
//                res.onResultReceived(
//                    onSuccess = {
//                        if (it != null) {
//                            _serviceBookings.value= it
//                        }
//                    },
//                    onError = {
//                    }
//                )
//            }
//        }
//
//    }
}
