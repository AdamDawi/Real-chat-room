package com.example.distancecoupleapp.presentation.main_board

import com.example.distancecoupleapp.data.Photo
import com.example.distancecoupleapp.data.User

data class MainBoardState(
    //list of photos from particular room id from database
    var photoList: ArrayList<Photo> = ArrayList(),
    //the currently logged in user
    var user1: User = User(),
    //user which is connected with the currently logged in user
    var user2: User = User(),
    //returns which photo does expanded description
    var descriptionExpandedList: ArrayList<Boolean> = ArrayList(),
    var isLoadingUsers: Boolean = true,
    var isLoadingPhotos: Boolean = true
)
