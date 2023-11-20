package com.example.distancecoupleapp.presentation.main_photos

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.Comment
import com.example.distancecoupleapp.data.Photo
import com.example.distancecoupleapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class MainPhotosViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    private val database: DatabaseReference = FirebaseManager().getFirebaseDatabaseReference()
    private var roomId: String? = null

    var mainPhotosState by mutableStateOf(MainPhotosState())
        private set

    fun getUserName() {
        mainPhotosState = mainPhotosState.copy(name = auth.currentUser?.displayName.toString())
    }

    fun changeSecondUserEmailState(newEmail: String){
        mainPhotosState = mainPhotosState.copy(secondUserEmail = newEmail)

        if(newEmail.isEmpty()) getUsersFromDatabase()
        else
            mainPhotosState = mainPhotosState.copy(userList = mainPhotosState.userList.filter {
            it.email.contains(newEmail, ignoreCase = true) || it.username.contains(newEmail, ignoreCase = true)
        } as ArrayList<User>)
    }

    fun changeSelectedUserState(selected: Int){
        mainPhotosState = mainPhotosState.copy(selectedUser = selected)
    }

    fun connectWithUser(){
        if(auth.currentUser!= null && mainPhotosState.secondUserEmail.isNotEmpty()){
            roomId = getChatroomId(auth.currentUser!!.uid, mainPhotosState.secondUserEmail)
        }else Log.e("Connecting with user error", "Current user is null")
    }

    private fun getChatroomId(userId1: String, userId2: String): String{
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
            val commentId = database.child("comments").push().key

            val comment = Comment(userId, text, System.currentTimeMillis())
            val commentValues = comment.toMap()

            val childUpdates = HashMap<String, Any>()
            childUpdates["/rooms/$roomId/photos/$photoId/comments/$commentId"] = commentValues

            database.updateChildren(childUpdates)
        }
    }

    fun addPhoto(imageUrl: String, description: String) {
        val currentUser = auth.currentUser
        val ownerId = currentUser?.uid

        if (ownerId != null) {
            val photoId = database.child("photos").push().key

            val photo = Photo(imageUrl, ownerId, description,  emptyMap())
            val photoValues = photo.toMap()

            val childUpdates = HashMap<String, Any>()
            childUpdates["/rooms/$roomId/photos/$photoId"] = photoValues

            database.updateChildren(childUpdates)
        }
    }

    fun getUsersFromDatabase() {
        var userList: ArrayList<User> = ArrayList()
        val postReference = FirebaseManager().getFirebaseDatabaseUserReference()

        val usersListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // for all users in database
                for (userSnapshot in dataSnapshot.children) {
                    // getting one user
                    var user = userSnapshot.getValue(User::class.java)
                    user = user?.copy(id = userSnapshot.key ?: "Error")

                    if (user != null) {
                        //adding to list
                        userList.add(user)
                    }
                }

                mainPhotosState = mainPhotosState.copy(userList = userList)
                FirebaseManager().getFirebaseDatabaseUserReference().get().addOnSuccessListener {
                    Log.i("firebase", "Got value ${it.value}")
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(usersListener)
    }

    private fun Comment.toMap(): Map<String, Any> {
        return mapOf(
            "user" to user,
            "text" to text,
            "timestamp" to timestamp
        )
    }

    private fun Photo.toMap(): Map<String, Any> {
        return mapOf(
            "imageUrl" to imageUrl,
            "owner" to owner,
            "description" to description,
            "comments" to comments,
        )
    }


}