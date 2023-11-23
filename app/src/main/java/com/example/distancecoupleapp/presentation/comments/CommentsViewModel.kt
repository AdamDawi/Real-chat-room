package com.example.distancecoupleapp.presentation.comments

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.distancecoupleapp.data.Comment
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class CommentsViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    private val database: DatabaseReference = FirebaseManager().getFirebaseDatabaseReference()

    var commentsState by mutableStateOf(CommentsState())
        private set
    fun getCommentsFromDatabase(roomId: String, photoId: String) {
        val commentList: ArrayList<Comment> = ArrayList()
        val postReference = FirebaseManager().getFirebaseDatabaseCommentsReference(roomId, photoId)

        val commentsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // for all photos in particular room
                for (commentSnapshot in dataSnapshot.children) {
                    // getting one user
                    val comment = commentSnapshot.getValue(Comment::class.java)

                    if (comment != null) {
                        //adding all photos to list without the currently logged in user
                        commentList.add(comment)
                    }
                }
                commentsState = commentsState.copy(commentsList = commentList)

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(commentsListener)
    }

    fun addComment(photoId: String, text: String, roomId: String) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val commentId = database.child("comments").push().key

            val comment = Comment(userId, text, System.currentTimeMillis())
            //class mapping to be able to insert into the database
            val commentValues = comment.toMap()

            val childUpdates = HashMap<String, Any>()
            childUpdates["/rooms/$roomId/photos/$photoId/comments/$commentId"] = commentValues

            database.updateChildren(childUpdates)
            getCommentsFromDatabase(roomId, photoId)
        }else{
            Log.e("Add comment Error", "current user is null")
        }
    }

    fun getUsersFromDatabase(roomId: String) {
        val userList: ArrayList<User> = ArrayList()
        val postReference = FirebaseManager().getFirebaseDatabaseUserReference()
        //getting 2 users ids because only 2 people have access to this room
        val usersId = roomId.split('_')

        val usersListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // for all users in database
                for (userSnapshot in dataSnapshot.children) {
                    // getting one user
                    var user = userSnapshot.getValue(User::class.java)
                    user = user?.copy(id = userSnapshot.key ?: "Error")

                    if (user != null && usersId.contains(user.id)) {
                        //adding to list
                        userList.add(user)
                    }
                }
                commentsState = commentsState.copy(user1 = userList[0])
                commentsState = commentsState.copy(user2 = userList[1])

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(usersListener)
    }

    fun getUserNameById(id: String): String{
        return if(commentsState.user1.id==id) commentsState.user1.username else commentsState.user2.username
    }

    fun changeText(newText: String){
        commentsState = commentsState.copy(text = newText)
    }


    private fun Comment.toMap(): Map<String, Any> {
        return mapOf(
            "user" to user,
            "text" to text,
            "timestamp" to timestamp
        )
    }
}