package com.example.distancecoupleapp.presentation.main_board

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.distancecoupleapp.presentation.main_board.components.PhotoItem
import com.example.distancecoupleapp.presentation.search_user.components.UserItem

@Composable
fun MainBoardScreen(viewModel: MainBoardViewModel, roomId: String) {
    val state = viewModel.mainBoardState
    viewModel.getPhotosFromDatabase(roomId)

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(modifier = Modifier
            .fillMaxHeight()
            .weight(10f)) {
            items(state.photoList.size){
                PhotoItem(viewModel = viewModel, state = state, it, roomId)
            }
        }
        Button(onClick = { viewModel.addPhoto("dd", "Description", roomId) }) {
            Text(text = "Add Photo")
        }
    }

}