package com.example.distancecoupleapp.presentation

sealed class Screen(val route: String){
    object LoginScreen: Screen("login_screen")
    object MainPhotosScreen: Screen("main_photos_screen")

    fun withArgs(vararg args: String):String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}