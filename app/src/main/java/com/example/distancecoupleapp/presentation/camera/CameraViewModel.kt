package com.example.distancecoupleapp.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.Photo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    private val database: DatabaseReference = FirebaseManager().getFirebaseDatabaseReference()

    private fun addPhoto(imageUrl: String, description: String, roomId: String, navController: NavController) {
        val currentUser = auth.currentUser
        val ownerId = currentUser?.uid

        if (ownerId != null) {
            val photoId = database.child("photos").push().key

            val photo = Photo(imageUrl, ownerId, description, photoId?:"Error", System.currentTimeMillis())
            //class mapping to be able to insert into the database
            val photoValues = photo.toMap()

            val childUpdates = HashMap<String, Any>()
            childUpdates["/rooms/$roomId/photos/$photoId"] = photoValues

            database.updateChildren(childUpdates)
            navController.popBackStack()
        }
    }
    fun takePhoto(
        controller: LifecycleCameraController,
        context: Context,
        roomId: String,
        navController: NavController
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
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )
                    uploadBitmapToFirebase(rotatedBitmap, roomId, navController)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

    fun uploadBitmapToFirebase(bitmap: Bitmap, roomId: String, navController: NavController) {
        // converting bitmap to jpeg
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        val byteArray = stream.toByteArray()

        // Firebase storage reference
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images")

        // making unique id
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "image_$timeStamp.jpg"

        // Send bitmap to storage
        val imageRef = imagesRef.child(fileName)
        val uploadTask = imageRef.putBytes(byteArray)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            //if success downloading url to photo in storage
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                Log.e("Picture", imageUrl)
                addPhoto(imageUrl, "Description", roomId, navController)
            }
        }.addOnFailureListener { exception ->
            Log.e("Uploading photo to firebase store", "Failure")
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