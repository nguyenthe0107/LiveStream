package olmo.wellness.android.ui.screen.profile_dashboard.gallery_compose

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.upload.UploadFilesRequest
import olmo.wellness.android.domain.use_case.*
import olmo.wellness.android.extension.getImageMimeType
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1.UploadFileIdServerViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    application: Application,
    private val getUploadUrlInfoUseCase: GetUploadIdServerUrlInfoUseCase,
    private val uploadFileUseCase: UploadFileIdServerUseCase,
    private val uploadUrlInfoUseCase: GetUploadUrlInfoUseCase,
    private val getMultiUploadUrlInfoUseCase: GetMultiUploadUrlInfoUseCase,
    private val roomChatUseCase: RoomChatUseCase,
) : UploadFileIdServerViewModel(application,
    getUploadUrlInfoUseCase,
    uploadUrlInfoUseCase,
    uploadFileUseCase) {

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorInternal = MutableStateFlow("")
    val errorInternal: StateFlow<String> = _errorInternal

    private val _typeIdentity = MutableStateFlow("")
    val typeIdentity: StateFlow<String> = _typeIdentity

    fun setTypeIdentity(type: String) {
        _typeIdentity.value = type
    }

    private val _isOpenUploadDialog = MutableStateFlow(false)
    val isOpenUploadDialog: StateFlow<Boolean> = _isOpenUploadDialog

    fun setOpenUploadDialog(open: Boolean) {
        _isOpenUploadDialog.value = open
    }

    private val _isLoading = MutableStateFlow(false)
    val isNeedLoading: StateFlow<Boolean> = _isLoading

    private val _identityDocumentUri = MutableStateFlow<String>("")
    val identityDocumentUri: StateFlow<String> = _identityDocumentUri
    fun setIdentityDocumentUris(imageSelected: String) {
        _identityDocumentUri.value = imageSelected
    }


    private val _allImagesFromGallery: MutableStateFlow<List<Uri>> = MutableStateFlow(listOf())
    val allImagesFromGallery: StateFlow<List<Uri>> = _allImagesFromGallery

    private fun getAllImages() {
        val allImages = mutableListOf<Uri>()

        val imageProjection = arrayOf(
            MediaStore.Images.Media._ID
        )
        val imageSortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val cursor = Contexts.getApplication(context).contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            imageSortOrder
        )
        cursor.use {
            if (cursor != null) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    allImages.add(
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            cursor.getLong(idColumn)
                        )
                    )
                }
            }
        }
        _allImagesFromGallery.value = allImages
    }

    fun loadAllImages() {
        viewModelScope.launch {
            getAllImages()
        }
    }


    fun uploadMultiImage(uris: List<Uri>, roomId: String?, _onSuccess : (List<String>)->Unit) {
        viewModelScope.launch {
            val mimeTypes = uris.map { it.getImageMimeType(context) }
            val uploadInfos = try {
                viewModelScope.async {
                    getMultiUploadUrlInfoUseCase.invoke(GetMultiUploadUrlInfoUseCase.Params(
                        mimeTypes))
                        .last()
                }

            } catch (e: Exception) {
                null
            }
            uploadInfos?.await()?.data?.let { listUploads ->
                viewModelScope.launch(Dispatchers.IO) {
                    var position = -1
                    val results = listUploads.map {
                        async {
                            position++
                            requestUploadFile(
                                it.putPresignedUrl.orEmpty(),
                                uris[position]
                            ).last()
                        }
                    }.awaitAll()

                    val resultUpload = results.find { it.data == false }
                    if (resultUpload == null) {
                        val uploadChats = try {
                            viewModelScope.async {
                                val rqt =
                                    UploadFilesRequest(tmpUris = listUploads.map { it.objectKey },
                                        roomChatId = roomId)
                                roomChatUseCase.uploadFiles(rqt).last()
                            }
                        } catch (e: Exception) {
                            null
                        }
                        uploadChats?.await()?.data?.let {
                            _onSuccess.invoke(it)
                        }
                    }
                }
            }
        }
    }
}