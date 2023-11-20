package com.example.distancecoupleapp.presentation.main_photos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPhotosScreen(
    viewModel: MainPhotosViewModel,
    navigateToLoginScreen: () -> Unit
) {
    val state = viewModel.mainPhotosState
    viewModel.getUserName()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { viewModel.signOut(navigateToLoginScreen) }) {
                Text(text = "SignOut")
            }
        }
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Your nick: ${state.name}")
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(value = state.secondUserEmail,
                onValueChange = { viewModel.changeSecondUserEmailState(it)},
                modifier = Modifier
                    .onFocusChanged { viewModel.getUsersFromDatabase() }

            )

//            Button(onClick = { viewModel.addPhoto("dd", "Vacation")}) {
//                Text(text = "Add Photo")
//            }
//            Button(onClick = { viewModel.addComment("-NjgoJnoWgwHSkyN0V9a", "like it")}) {
//                Text(text = "Add Comment")
//            }
//            Button(onClick = { viewModel.getUsersFromDatabase()}) {
//                Text(text = "Get users")
//            }
            
            LazyColumn(modifier = Modifier
                .fillMaxHeight()
                .weight(10f)) {
                items(state.userList.size){
                    Spacer(modifier = Modifier.height(10.dp))
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (state.selectedUser == it) Color.Green
                                else Color.DarkGray
                            )
                            .clickable {
                                viewModel.changeSelectedUserState(it)
                            }
                            .padding(10.dp)
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)){
                            Icon(Icons.Default.AccountCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = state.userList[it].email)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "(${state.userList[it].username})")
                            }
                        }

                }
            }

            Button(onClick = { viewModel.connectWithUser()},
                modifier = Modifier.weight(1f)) {
                Text(text = "Connect with user")
            }
        }
    }
}