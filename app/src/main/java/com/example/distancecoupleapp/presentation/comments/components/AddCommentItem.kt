package com.example.distancecoupleapp.presentation.comments.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        OutlinedTextField(
            value = state.text,
            onValueChange = { viewModel.changeText(it)},
            modifier = Modifier
                .border(BorderStroke(0.dp, Color.Transparent))
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .height(52.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.secondary,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Search",
                    Modifier
                        .size(30.dp)
                        .clickable {
                            viewModel.addComment(photoId, state.text, roomId)
                            viewModel.changeText("")
                        },
                    tint = MaterialTheme.colorScheme.secondary
                )
            },
            placeholder = { Text("Add comment...",
                fontSize = 15.sp
            ) },
        )
    }

}