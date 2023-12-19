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
        getUserProfilePicture()
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
    //change profile picture in user auth
    private fun getUserProfilePicture(){
        val user = auth.currentUser
        if(user != null){
            userState = userState.copy(selectedImageUri = user.photoUrl)
        }
    }

    fun changeUsernameState(newName: String){
        userState = userState.copy(userName = newName)
    }

    fun changeSelectedImageUriState(uri: Uri?, context: Context){
        if(uri!=null){
            userState = userState.copy(selectedImageUri = uri)
            userState.selectedImageUri?.let { uploadBitmapToFirebaseStore(it, context) }
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

    private fun changeProfilePicture(newPicture: Uri, context: Context) {
        val user = auth.currentUser

        if (user != null) {
            //delete old image from firebase storage
            deleteImageFromFirebaseStorage(user.photoUrl.toString())

            val profileUpdates = userProfileChangeRequest {
                photoUri = newPicture
            }

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Profile picture changed ", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Profile picture changed failure", Toast.LENGTH_SHORT).show()
                    }
                    userState = userState.copy(isUploading = false)
                }
        } else {
            Log.e("change user picture", "current user is null")
        }
    }
    private fun uploadBitmapToFirebaseStore(imageUri: Uri, context: Context) {
        userState = userState.copy(isUploading = true)
        // Firebase storage reference
        val storageRef = FirebaseManager().getFirebaseStoreReference()
        val imagesRef = storageRef.child("images")

        // making unique id with date
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "image_$timeStamp.jpg"

        // Send bitmap to storage
        val imageRef = imagesRef.child(fileName)
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            //if success downloading url to photo in storage

            imageRef.downloadUrl.addOnSuccessListener { uri ->
                changeProfilePicture(uri, context)
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
            val imagesRef = storageRef.child("images/${extractFileNameFromUrl(imageUrl)}")

            // Delete the file
            imagesRef.delete().addOnSuccessListener {
                Log.d("Deleting", "Success")
            }.addOnFailureListener {
                Log.e("Deleting", "Fail")
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