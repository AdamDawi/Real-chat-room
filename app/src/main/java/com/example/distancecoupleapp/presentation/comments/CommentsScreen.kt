package com.example.distancecoupleapp.presentation.comments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.distancecoupleapp.presentation.comments.components.AddCommentItem
import com.example.distancecoupleapp.presentation.comments.components.CommentItem
import com.example.distancecoupleapp.presentation.theme.Secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    viewModel: CommentsViewModel,
    roomId: String,
    photoId: String,
    popToMainBoardScreen: () -> Unit
) {
    val state = viewModel.commentsState
    viewModel.getCommentsFromDatabase(roomId, photoId)
    viewModel.getUsersFromDatabase(roomId)

    Box(modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(10f)
            ) {
                item { Spacer(modifier = Modifier.height(46.dp)) }
                items(state.commentsList.size
                ) {
                    CommentItem(viewModel = viewModel, state = state, it)
                }
            }
            AddCommentItem(viewModel = viewModel, state = state, roomId = roomId, photoId = photoId)
        }
        CenterAlignedTopAppBar(title = {
            Text(text = "Comments",
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
                IconButton(onClick = { popToMainBoardScreen() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back button",
                        tint = Secondary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        )
    }
}