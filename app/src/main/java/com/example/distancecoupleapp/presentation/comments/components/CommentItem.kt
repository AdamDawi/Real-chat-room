package com.example.distancecoupleapp.presentation.comments.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
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
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.AccountCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(50.dp).align(Alignment.Top)
            )

        Spacer(modifier = Modifier.width(3.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Row {
                Text(text = viewModel.getUserNameById(state.commentsList[it].user),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(text = viewModel.convertMillisToReadableDateTime(state.commentsList[it].timestamp),
                    color = Gray
                )
            }
            Text(text = state.commentsList[it].text,
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }
}