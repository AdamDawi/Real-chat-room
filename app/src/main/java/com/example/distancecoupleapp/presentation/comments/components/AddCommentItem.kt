package com.example.distancecoupleapp.presentation.comments.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.distancecoupleapp.presentation.comments.CommentsState
import com.example.distancecoupleapp.presentation.comments.CommentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCommentItem(
    viewModel: CommentsViewModel,
    state: CommentsState,
    roomId: String,
    photoId: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = state.text, onValueChange = {viewModel.changeText(it)})

        Button(onClick = {
            viewModel.addComment(photoId, state.text, roomId)
            viewModel.changeText("")
        }) {
            Text(text = "Add")
        }
    }

}