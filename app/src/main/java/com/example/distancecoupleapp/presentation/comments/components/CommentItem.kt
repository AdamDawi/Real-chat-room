package com.example.distancecoupleapp.presentation.comments.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.distancecoupleapp.presentation.comments.CommentsState
import com.example.distancecoupleapp.presentation.comments.CommentsViewModel

@Composable
fun CommentItem(
    viewModel: CommentsViewModel,
    state: CommentsState,
    it: Int
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .border(BorderStroke(3.dp, Color.DarkGray), shape = RoundedCornerShape(8.dp))
        .padding(20.dp)
    ) {
        Icon(Icons.Default.AccountCircle, contentDescription = null)
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(text = viewModel.getUserNameById(state.commentsList[it].user))
            Text(text = state.commentsList[it].text)
        }

    }
}