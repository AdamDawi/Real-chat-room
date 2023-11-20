package com.example.distancecoupleapp.presentation.main_board

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MainBoardScreen(viewModel: MainBoardViewModel, roomId: String) {

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { viewModel.addPhoto("dd", "Description", roomId) }) {
            Text(text = "Add Photo")
        }

        Button(onClick = { viewModel.addComment("photo_2", "nice", roomId) }) {
            Text(text = "Add Comment")
        }
    }

}