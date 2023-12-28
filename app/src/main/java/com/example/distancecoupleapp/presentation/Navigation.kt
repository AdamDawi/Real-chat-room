package com.example.distancecoupleapp.presentation

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.example.distancecoupleapp.presentation.user.UserScreen

@Composable
fun Navigation(
    context: Context
){
    // Initialize NavController using rememberNavController() function, which allows maintaining the navigation state
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ){

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(
                viewModel(),
                context,
                // Navigate to the user search screen after successful login
                navigateToMainBoardScreen = { navController.navigate(Screen.SearchUserScreen.route) },
                navController
            )
        }

        composable(route = Screen.SearchUserScreen.route,
            //animations
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )},
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }
            ){
            SearchUserScreen(
                viewModel(),
                // Pass NavController to the user search screen
                navController = navController
            )
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
            // Retrieve room ID from the navigation arguments
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
        ),
            //animations
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        400, easing = EaseIn
                    )
                )
                              },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        400, easing = EaseOut
                    )
                )
            }
        ){
            // Retrieve room ID and photo ID from the navigation arguments
            val roomId = it.arguments?.getString("roomId")?:"Error"
            val photoId = it.arguments?.getString("photoId")?:"Error"
            CommentsScreen(viewModel(), roomId, photoId) { navController.popBackStack() }
        }

        composable(route = Screen.CameraScreen.route+ "/{roomId}", arguments =
        listOf(
            navArgument("roomId"){
                type = NavType.StringType
                defaultValue = "Error"
                nullable = false
            }),
            //animations
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        400, easing = EaseIn
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        400, easing = EaseOut
                    )
                )
            }
            ){
            // Retrieve room ID from the navigation arguments
            val roomId = it.arguments?.getString("roomId")?:"Error"
            CameraScreen(viewModel(), context, navController, roomId)
        }

        composable(route = Screen.UserScreen.route,
            //animations
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )},
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
            ){
            UserScreen(viewModel = viewModel(),
                { navController.popBackStack() },
                // Navigate to the previous screen (login screen)
                navigateToLoginScreen = { navController.navigate(Screen.LoginScreen.route) },
                context
            )
        }
    }
    


}

