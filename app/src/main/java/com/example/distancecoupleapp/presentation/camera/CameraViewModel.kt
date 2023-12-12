package com.example.distancecoupleapp.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.Photo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    private val database: DatabaseReference = FirebaseManager().getFirebaseDatabaseReference()

    var cameraState by mutableStateOf(CameraState())
        private set

    private fun changeIsLoadingState(isLoading: Boolean){
        cameraState = cameraState.copy(isLoading = isLoading)
    }

    fun changeDescriptionState(newText: String){
        cameraState = cameraState.copy(descriptionTextField = newText)
    }

    fun changeIconState(controller: LifecycleCameraController){
        val newImageVector = when (controller.imageCaptureFlashMode) {
            ImageCapture.FLASH_MODE_OFF -> Icons.Default.FlashOff
            ImageCapture.FLASH_MODE_ON -> Icons.Default.FlashOn
            else -> Icons.Default.FlashOff}

        cameraState = cameraState.copy(flashIcon = newImageVector)
    }

    fun changeCameraMode(controller: LifecycleCameraController){
        controller.cameraSelector =
            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else CameraSelector.DEFAULT_BACK_CAMERA
    }

    fun changeFlashMode(controller: LifecycleCameraController){
        val newFlashMode = when (controller.imageCaptureFlashMode) {
            ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
            ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_OFF
            else -> ImageCapture.FLASH_MODE_OFF
        }

        controller.imageCaptureFlashMode = newFlashMode
        changeIconState(controller)
    }

    fun addPhotoToDatabase(description: String, roomId: String, navController: NavController) {
        val ownerId = auth.currentUser?.uid
        val imageUrl = cameraState.imageUrl

        if (ownerId != null) {
            //make unique photo id and add it to photos in database
            val photoId = database.child("photos").push().key
            //make photo instance with appropriate data
            val photo = Photo(imageUrl, ownerId, description, photoId?:"Error", System.currentTimeMillis())
            //class mapping to be able to insert into the database
            val photoValues = photo.toMap()

            //preparing data for insertion into the database
            val childUpdates = HashMap<String, Any>()
            childUpdates["/rooms/$roomId/photos/$photoId"] = photoValues

            database.updateChildren(childUpdates)
            //after adding photo to database we can go back to the previous screen
            changeIsLoadingState(false)
            navController.popBackStack()
        }else
            Log.e("Uploading photo to database", "Owner id is null")
    }
    fun takePhoto(
        controller: LifecycleCameraController,
        context: Context
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    //when taking image is success
                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    //rotate bitmap to good orientation
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )
                    //loading is true when you taking photo
                    changeIsLoadingState(true)
                    cameraState = cameraState.copy(image = rotatedBitmap)
                    uploadBitmapToFirebaseStore(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Taking photo with camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

    fun uploadBitmapToFirebaseStore(bitmap: Bitmap) {
        // converting bitmap to jpeg
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        val byteArray = stream.toByteArray()

        // Firebase storage reference
        val storageRef = FirebaseManager().getFirebaseStoreReference()
        val imagesRef = storageRef.child("images")

        // making unique id with date
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "image_$timeStamp.jpg"

        // Send bitmap to storage
        val imageRef = imagesRef.child(fileName)
        val uploadTask = imageRef.putBytes(byteArray)

        uploadTask.addOnSuccessListener {
            //if success downloading url to photo in storage
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                cameraState = cameraState.copy(isPhotoTaken = true, imageUrl = imageUrl)
                //addPhotoToDatabase(imageUrl, "Description", roomId, navController)
            }
        }.addOnFailureListener { e ->
            Log.e("Uploading photo to firebase store", e.message?:"Failure")
        }
    }

    private fun Photo.toMap(): Map<String, Any> {
        return mapOf(
            "imageUrl" to imageUrl,
            "owner" to owner,
            "description" to description,
            "id" to id,
            "timestamp" to timestamp
        )
    }
}