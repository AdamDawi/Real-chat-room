package com.example.distancecoupleapp.presentation.login

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel,
                context: Context,
                navigateToMainBoardScreen: () -> Unit,
                navController: NavController
) {
    val state = viewModel.loginState

    if(state.isLogged)
    {
        //pop login screen because we don't need that when person is log in
        navController.popBackStack()
        //anytime user is logged navigate to main photos screen
        navigateToMainBoardScreen()
        //prevents from navigating to the main photos screen again
        viewModel.changeIsLoggedState(false)
    }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                //animations for color
                val textColor1 by animateFloatAsState(
                    targetValue = state.registerTextAlpha,
                    animationSpec = tween(durationMillis = 300), label = ""
                )
                val textColor2 by animateFloatAsState(
                    targetValue = state.loginTextAlpha,
                    animationSpec = tween(durationMillis = 300), label = ""
                )
                Text(
                    text = "Register",
                    Modifier
                        //click without ripple
                        .pointerInput(Unit) {
                            detectTapGestures { viewModel.changeIsRegisteringState(true)
                                viewModel.changeTextAlphaState(1.0f, 0.2f) }
                        },
                    color = Color.White.copy(alpha = textColor1),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))

                Box(modifier = Modifier
                    .height(22.dp)
                    .width(2.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = "Log in",
                    Modifier
                        //click without ripple
                        .pointerInput(Unit) {
                            detectTapGestures { viewModel.changeIsRegisteringState(false)
                                viewModel.changeTextAlphaState(0.2f, 1f)}
                        },
                    color = Color.White.copy(alpha = textColor2),
                    fontWeight = FontWeight.Bold
                )
            }
            if(state.isLoading){
                Box(modifier = Modifier.fillMaxHeight()
                ){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(3.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }else {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(value = state.email,
                        onValueChange = {
                            viewModel.changeEmailState(it)
                        },
                        modifier = Modifier
                            .border(BorderStroke(0.dp, Color.Transparent))
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxWidth(),
                        placeholder = { Text("Email") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Go
                        ),
                        keyboardActions = KeyboardActions(
                            onGo = {
                                //because we have shared email text field for login screen and register screen
                                if(state.isRegistering) {
                                    viewModel.createAccount(context) //navigate to next screen if sign in is success
                                }else{
                                    viewModel.signIn(context) //navigate to next screen if sign in is success
                                }
                            }
                        )
                    )
//                    if (state.isRegistering){
//                        RegisterColumn(viewModel = viewModel, state = state, context = context)
//                    }else{
//                        LoginColumn(viewModel = viewModel, state = state, context = context)
//                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    AnimatedContent(targetState = state.isRegistering,
                        transitionSpec = {
                            slideIntoContainer(
                                animationSpec = tween(300, easing = EaseIn),
                                towards = Start
                            ).togetherWith(
                                slideOutOfContainer(
                                    animationSpec = tween(300, easing = EaseOut),
                                    towards = End
                                )
                            )
                        },
                        label = ""
                    ) {targetValue ->
                        if(targetValue){

                            OutlinedTextField(value = state.name,
                                modifier = Modifier
                                    .border(BorderStroke(0.dp, Color.Transparent))
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .fillMaxWidth(),
                                onValueChange ={
                                    viewModel.changeNameState(it)
                                },
                                placeholder = { Text("Username") },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = MaterialTheme.colorScheme.secondary,
                                    cursorColor = MaterialTheme.colorScheme.secondary,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Go
                                ),
                                keyboardActions = KeyboardActions(
                                    onGo = {
                                        viewModel.createAccount(context) //navigate to next screen if sign in is success
                                    }
                                )
                            )
                        }
                    }
                    if(state.isRegistering) Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .border(BorderStroke(0.dp, Color.Transparent))
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxWidth(),
                        value = state.password,
                        onValueChange ={
                            viewModel.changePasswordState(it)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        placeholder = { Text("Password") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Go
                        ),
                        keyboardActions = KeyboardActions(
                            onGo = {
                                viewModel.signIn(context) //navigate to next screen if sign in is success
                            }
                        )
                    )

                    AnimatedContent(targetState = state.isRegistering,
                        transitionSpec = {
                            slideInHorizontally(
                                animationSpec = tween(300, easing = EaseIn),
                                initialOffsetX = {
                                    if (state.isRegistering) -it else it
                                },
                            ).togetherWith(
                                slideOutHorizontally(
                                    animationSpec = tween(300, easing = EaseOut),
                                    targetOffsetX = {
                                        if (state.isRegistering) it else -it
                                    }
                                )
                            )
                        },
                        label = ""
                    ) {targetValue ->
                        if(targetValue){
                            Button(
                                modifier = Modifier
                                    .padding(top = 50.dp)
                                    .fillMaxWidth()
                                    .height(52.dp),
                                onClick = {
                                    viewModel.createAccount(context)
                                }
                            ) {
                                Text("Register",
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontSize = 16.sp)
                            }
                        }else{
                            Button(
                                modifier = Modifier
                                    .padding(top = 50.dp)
                                    .fillMaxWidth()
                                    .height(52.dp),
                                onClick = {
                                    viewModel.signIn(context) //navigate to next screen if sign in is success
                                }) {
                                Text("Log in",
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
    }
}
