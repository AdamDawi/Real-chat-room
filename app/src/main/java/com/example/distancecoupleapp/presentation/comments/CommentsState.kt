package com.example.distancecoupleapp.presentation.comments

import com.example.distancecoupleapp.data.Comment
import com.example.distancecoupleapp.data.Photo
import com.example.distancecoupleapp.data.User

data class CommentsState (
    var commentsList: ArrayList<Comment> = ArrayList(),
    //the currently logged in user
    var user1: User = User(),
    //user which is connected with the currently logged in user
    var user2: User = User(),
    var text: String = ""
)