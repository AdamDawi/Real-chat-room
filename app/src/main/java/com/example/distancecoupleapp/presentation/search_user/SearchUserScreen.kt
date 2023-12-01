package com.example.distancecoupleapp.presentation.search_user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.distancecoupleapp.presentation.search_user.components.UserItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUserScreen(
    viewModel: SearchUserViewModel,
    navigateToLoginScreen: () -> Unit,
    navigateToMainBoardScreen: NavController
) {
    val state = viewModel.searchUserState
    viewModel.getUsersFromDatabase()

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
            OutlinedTextField(value = state.searchUserField,
                onValueChange = { viewModel.changeSearchUserFieldState(it)}
            )

            LazyColumn(modifier = Modifier
                .fillMaxHeight()
                .weight(10f)) {
                items(state.filteredUserList.size){
                    UserItem(viewModel = viewModel, state = state, it)
                }
            }

            Button(onClick = { viewModel.connectWithUser(navigateToMainBoardScreen)},
                modifier = Modifier.weight(1f)) {
                Text(text = "Connect with user")
            }
        }
    }
}