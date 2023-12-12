package com.example.distancecoupleapp.presentation.user

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.distancecoupleapp.presentation.theme.Secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    viewModel: UserViewModel,
    popToSearchScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    context: Context
) {
    val state = viewModel.userState
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {CenterAlignedTopAppBar(title = {
        Text(text = viewModel.getUserName(),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            maxLines = 1
        ) },
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .heightIn(max = 34.dp)
            .height(34.dp),
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Secondary,
        ),
        navigationIcon = {
            IconButton(onClick = { popToSearchScreen() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back button",
                    tint = Secondary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    )}) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = state.userName,
                onValueChange = {viewModel.changeUsernameState(it)})
            //change user name
            Button(modifier = Modifier.padding(8.dp), onClick = { viewModel.changeUsername(state.userName, context) }) {
                Text(text = "Change username",
                    fontWeight = FontWeight.Bold,
                    color = Secondary)
            }
            Button(modifier = Modifier.padding(it), onClick = { viewModel.signOut(navigateToLoginScreen) },
            ) {
                Text(text = "SignOut",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red)
            }
        }

    }

}