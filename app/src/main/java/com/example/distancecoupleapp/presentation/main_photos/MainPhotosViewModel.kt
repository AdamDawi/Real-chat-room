package com.example.distancecoupleapp.presentation.main_photos

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.distancecoupleapp.common.FirebaseUtil
import com.example.distancecoupleapp.data.Comment
import com.example.distancecoupleapp.data.Photo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainPhotosViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseUtil.getFirebaseAuth()
    private val database: FirebaseDatabase = FirebaseUtil.getFirebaseDatabase()

    var mainPhotosState by mutableStateOf(MainPhotosState())
        private set

    fun getUserName() {
        mainPhotosState = mainPhotosState.copy(name = auth.currentUser?.displayName.toString())
    }

    fun changeSecondUserEmail(newEmail: String){
        mainPhotosState = mainPhotosState.copy(secondUserEmail = newEmail)
    }

    fun connectWithUser(){
        if(auth.currentUser!= null && mainPhotosState.secondUserEmail.isNotEmpty()){
            val roomId = getChatroomId(auth.currentUser!!.uid, mainPhotosState.secondUserEmail)
            addTestingComment(roomId)
        }else Log.e("Connecting with user error", "Current user is null")
    }

    fun addTestingComment(roomId: String){
        database.reference.child("rooms").child(roomId).child("photos").child("photo_1").child("comments").push().setValue("hi")
    }

    fun getChatroomId(userId1: String , userId2: String): String{
        if(userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2
        }
        return userId2+"_"+userId1
    }

    fun signOut(navigateToLoginScreen: () -> Unit){
        auth.signOut()
        navigateToLoginScreen()
    }

    fun addComment(photoId: String, text: String) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val commentId = database.reference.child("comments").push().key

            val comment = Comment(userId, text, System.currentTimeMillis())
            val commentValues = comment.toMap()

            val childUpdates = HashMap<String, Any>()
            childUpdates["/photos/$photoId/comments/$commentId"] = commentValues

            database.reference.updateChildren(childUpdates)
        }
    }

    fun addPhoto(imageUrl: String) {
        val currentUser = auth.currentUser
        val ownerId = currentUser?.uid

        if (ownerId != null) {
            val photoId = database.reference.child("photos").push().key

            val photo = Photo(imageUrl, ownerId, emptyMap())
            val photoValues = photo.toMap()

            val childUpdates = HashMap<String, Any>()
            childUpdates["/photos/$photoId"] = photoValues
            childUpdates["/users/$ownerId/photos/$photoId"] = true

            database.reference.updateChildren(childUpdates)
        }
    }

    fun addConnection(photoId: String, userId: String) {
        val childUpdates = HashMap<String, Any>()
        childUpdates["/photos/$photoId/connections/$userId"] = true
        childUpdates["/users/$userId/photos/$photoId"] = true

        database.reference.updateChildren(childUpdates)
    }

    fun Comment.toMap(): Map<String, Any> {
        return mapOf(
            "user" to user,
            "text" to text,
            "timestamp" to timestamp
        )
    }

    fun Photo.toMap(): Map<String, Any> {
        return mapOf(
            "imageUrl" to imageUrl,
            "owner" to owner,
            "comments" to comments,
        )
    }


}