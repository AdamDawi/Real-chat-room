package com.example.distancecoupleapp.presentation

sealed class Screen(val route: String){
    object LoginScreen: Screen("login_screen")
    object SearchUserScreen: Screen("search_user_screen")
    object MainBoardScreen: Screen("main_board_screen")
    object CommentsScreen: Screen("comments_screen")
    object CameraScreen: Screen("camera_screen")
    object UserScreen: Screen("user_screen")

    fun withArgs(vararg args: String):String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}