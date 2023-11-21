package com.example.distancecoupleapp.presentation.main_board

import com.example.distancecoupleapp.data.Photo
import com.example.distancecoupleapp.data.User

data class MainBoardState (
    var userList: ArrayList<User> = ArrayList(),
    var photoList: ArrayList<Photo> = ArrayList()
    )
