package com.example.distancecoupleapp.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.distancecoupleapp.presentation.camera.CameraScreen
import com.example.distancecoupleapp.presentation.comments.CommentsScreen
import com.example.distancecoupleapp.presentation.login.LoginScreen
import com.example.distancecoupleapp.presentation.main_board.MainBoardScreen
import com.example.distancecoupleapp.presentation.search_user.SearchUserScreen

@Composable
fun Navigation(
    context: Context
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route ){

        composable(route = Screen.LoginScreen.route){
            LoginScreen(viewModel(), context, navigateToMainPhotosScreen = {navController.navigate(Screen.SearchUserScreen.route)})
        }

        composable(route = Screen.SearchUserScreen.route){
            SearchUserScreen(viewModel(),
                navigateToLoginScreen = {navController.popBackStack()},
                navigateToMainBoardScreen = navController)
        }

        composable(route = Screen.MainBoardScreen.route+ "/{roomId}", arguments =
        listOf(
            navArgument("roomId"){
                type = NavType.StringType
                defaultValue = "Error"
                nullable = false
            }
        )
        ){
            val roomId = it.arguments?.getString("roomId")?:"Error"
            MainBoardScreen(viewModel(), navController, roomId)
        }

        composable(route = Screen.CommentsScreen.route+ "/{roomId}/{photoId}", arguments =
        listOf(
            navArgument("roomId"){
                type = NavType.StringType
                defaultValue = "Error"
                nullable = false
            },
            navArgument("photoId"){
                type = NavType.StringType
                defaultValue = "Error"
                nullable = false
            }
        )
        ){
            val roomId = it.arguments?.getString("roomId")?:"Error"
            val photoId = it.arguments?.getString("photoId")?:"Error"
            CommentsScreen(viewModel(), roomId, photoId)
        }

        composable(route = Screen.CameraScreen.route+ "/{roomId}", arguments =
        listOf(
            navArgument("roomId"){
                type = NavType.StringType
                defaultValue = "Error"
                nullable = false
            })){
            val roomId = it.arguments?.getString("roomId")?:"Error"
            CameraScreen(viewModel(), context, navController, roomId)
        }
    }

}