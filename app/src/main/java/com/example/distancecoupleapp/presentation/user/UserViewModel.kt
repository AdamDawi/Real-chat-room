package com.example.distancecoupleapp.presentation.user

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
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
    fun signOut(navigateToLoginScreen: () -> Unit) {
        auth.signOut()
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
        userState = userState.copy(isUploading = true)
        // Firebase storage reference
        val storageRef = FirebaseManager().getFirebaseStoreReference()
        val imagesRef = storageRef.child("profile_pictures")

        // making unique id with date
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "image_$timeStamp.jpg"

        // Send bitmap to storage
        val imageRef = imagesRef.child(fileName)
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            //if success downloading url to photo in storage
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                uploadPictureUriToDatabase(uri, context)
            }
        }.addOnFailureListener { e ->
            Log.e("Uploading picture to firebase store", e.message?:"Failure")
        }
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