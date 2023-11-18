package com.example.distancecoupleapp.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.distancecoupleapp.presentation.login.LoginScreen
import com.example.distancecoupleapp.presentation.login.LoginViewModel
import com.example.distancecoupleapp.presentation.main_photos.MainPhotosScreen

@Composable
fun Navigation(
    context: Context
) {
    val navController = rememberNavController()
    val viewModel = viewModel<LoginViewModel>()

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route ){

        composable(route = Screen.LoginScreen.route){
            LoginScreen(viewModel = viewModel, context, navigateToMainPhotosScreen = {navController.navigate(Screen.MainPhotosScreen.route)})
        }

        composable(route = Screen.MainPhotosScreen.route){
            MainPhotosScreen()
        }
    }

}