package com.example.distancecoupleapp.presentation.main_board

import com.example.distancecoupleapp.data.Photo
import com.example.distancecoupleapp.data.User

data class MainBoardState (
    var photoList: ArrayList<Photo> = ArrayList(),
    //the currently logged in user
    var user1: User = User(),
    //user which is connected with the currently logged in user
    var user2: User = User()
    )
