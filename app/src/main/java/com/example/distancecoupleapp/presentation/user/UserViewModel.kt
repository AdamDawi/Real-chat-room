package com.example.distancecoupleapp.presentation.user

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    private val database: DatabaseReference = FirebaseManager().getFirebaseDatabaseReference()

    var userState by mutableStateOf(UserState())
        private set


    init {
        getCurrentUserProfilePicture()
    }
    fun signOut(navigateToLoginScreen: () -> Unit, popScreen: () -> Unit) {
        auth.signOut()
        popScreen()
        popScreen()
        popScreen()
        navigateToLoginScreen()
    }

    fun getUserName(): String{
        if(auth.currentUser !=null){
            return auth.currentUser!!.displayName!!
        }
        else{
            Log.e("Get user name", "current user is null")
        }
        return ""
    }
    private fun getCurrentUserProfilePicture(){
        val currUser = auth.currentUser

        if(currUser!=null){
            database.child("users").child(currUser.uid).child("picture").get().addOnSuccessListener {
                userState = userState.copy(selectedImageUri = it.value.toString())
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        }else{
            Log.e("Get user profile picture", "User is null")
        }
    }

    fun changeUsernameState(newName: String){
        userState = userState.copy(userName = newName)
    }

    fun changeImageState(newState: String){
        userState = userState.copy(imageState = newState)
    }

    fun uploadImageUri(uri: Uri?, context: Context){
        if(uri!=null){
            //delete old picture before upload new
            val selectedImageUri = userState.selectedImageUri
            if(selectedImageUri!=null){
                val pictureName = extractFileNameFromUrl(selectedImageUri)
                if(pictureName!=null){
                    deleteImageFromFirebaseStorage(pictureName)
                }else
                    Log.e("Extract File Name From Url", "Didn't find name in url")
            }else{
                Log.e("Delete picture", "Picture is null")
            }
            //set new picture for image to display on screen
            userState = userState.copy(selectedImageUri = uri.toString())
            //upload picture
            uploadProfilePictureToFirebaseStorage(uri, context)
        }
    }

    fun changeUsername(newName: String, context: Context){
        val user = auth.currentUser
        if(user!=null && newName.isNotEmpty()){
            val profileUpdates = userProfileChangeRequest {
                displayName = newName
            }

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }

            val currentUserId = auth.currentUser!!.uid
            val userData = User(newName, auth.currentUser?.email ?: "Error", currentUserId, "")

            val userValues = userData.toMap()
            val childUpdates = HashMap<String, Any>()
            childUpdates["/users/$currentUserId"] = userValues
            database.updateChildren(childUpdates)

            Toast.makeText(context, "Username changed ", Toast.LENGTH_SHORT).show()
        }else if(newName.isEmpty()){
            Toast.makeText(context, "Username can't be empty ", Toast.LENGTH_SHORT).show()
        }
        else{
            Log.e("change username", "current user is null")
        }
    }

    private fun uploadPictureUriToDatabase(newPictureUri: Uri, context: Context) {
        val user = auth.currentUser
        if(user!=null){
            val currentUserId = auth.currentUser!!.uid
            val userData = User(user.displayName ?: "Error", auth.currentUser?.email ?: "Error", currentUserId, newPictureUri.toString())

            val userValues = userData.toMap()
            val childUpdates = HashMap<String, Any>()
            childUpdates["/users/$currentUserId"] = userValues
            database.updateChildren(childUpdates)

            Toast.makeText(context, "Profile picture changed ", Toast.LENGTH_SHORT).show()
            userState = userState.copy(isUploading = false)
        }
        else{
            Log.e("change profile picture", "current user is null")
        }
    }
    private fun uploadProfilePictureToFirebaseStorage(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            userState = userState.copy(isUploading = true)
            //compress image
            val compressedImage = compressAndRotateImage(context, imageUri)
            if (compressedImage != null) {
                // Firebase storage reference
                val storageRef = FirebaseManager().getFirebaseStoreReference()
                val imagesRef = storageRef.child("profile_pictures")

                // making unique id with date
                val timeStamp: String =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "image_$timeStamp.jpg"

                // Send bitmap to storage
                val imageRef = imagesRef.child(fileName)
                val uploadTask = imageRef.putFile(compressedImage)

                uploadTask.addOnSuccessListener {
                    //if success downloading url to photo in storage
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        uploadPictureUriToDatabase(uri, context)
                    }
                }.addOnFailureListener { e ->
                    Log.e("Uploading picture to firebase store", e.message ?: "Failure")
                }
            } else {
                Log.e("upload profile picture to firebase storage error", "compression failed")
            }
        }



    }

    private suspend fun compressAndRotateImage(context: Context, imageUri: Uri): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                // Read image rotation from Exif
                val rotation = getExifRotation(context, imageUri)

                //Read the original image
                val originalBitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri))

                // Create an image rotation matrix
                val matrix = Matrix()
                matrix.postRotate(rotation.toFloat())

                //Rotate the original image using the matrix
                val rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true)

                // Create a temporary file where the compressed and rotated image will be saved
                val compressedImageFile = File.createTempFile("compressed_rotated_image", ".jpg", context.cacheDir)

                // Create a stream to write the compressed image
                val compressedOutputStream = FileOutputStream(compressedImageFile)

                // Compress the image and write it to the output stream
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, compressedOutputStream)

                // Close the stream
                compressedOutputStream.close()

                // Return the URI of the compressed image
                Uri.fromFile(compressedImageFile)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun getExifRotation(context: Context, uri: Uri): Int {
        var rotation = 0
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val exif = ExifInterface(inputStream!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            rotation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rotation
    }
    private fun deleteImageFromFirebaseStorage(imageUrl: String){
        if(imageUrl!="null"){
            // Create a storage reference from our app
            val storageRef = FirebaseManager().getFirebaseStoreReference()
            // Create a reference to the file to delete
            val imagesRef = storageRef.child("profile_pictures/${extractFileNameFromUrl(imageUrl)}")

            // Delete the file
            imagesRef.delete().addOnSuccessListener {
                Log.d("Deleting", "Success")
            }.addOnFailureListener {
                Log.e("Deleting", "Failure")
            }
        }

    }

    private fun extractFileNameFromUrl(url: String): String? {
        val regex = """image_\d{8}_\d{6}\.jpg""".toRegex()
        val matchResult = regex.find(url)

        return matchResult?.value
    }

    private fun User.toMap(): Map<String, Any> {
        return mapOf(
            "username" to username,
            "email" to email,
            "picture" to picture
        )
    }

}