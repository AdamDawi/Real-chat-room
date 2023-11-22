package com.example.distancecoupleapp.presentation.comments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.distancecoupleapp.presentation.comments.components.AddCommentItem
import com.example.distancecoupleapp.presentation.comments.components.CommentItem

@Composable
fun CommentsScreen(
    viewModel: CommentsViewModel,
    roomId: String,
    photoId: String
) {
    val state = viewModel.commentsState
    viewModel.getCommentsFromDatabase(roomId, photoId)
    viewModel.getUsersFromDatabase(roomId)

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier
            .fillMaxHeight()
            .weight(10f)) {
            items(state.commentsList.size){
                CommentItem(viewModel = viewModel, state = state, it, roomId)
            }
        }
        AddCommentItem(viewModel = viewModel, state = state, roomId = roomId, photoId = photoId)
    }
}