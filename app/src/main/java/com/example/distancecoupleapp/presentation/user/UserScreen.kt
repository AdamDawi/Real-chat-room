package com.example.distancecoupleapp.presentation.user

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.distancecoupleapp.common.Constants
import com.example.distancecoupleapp.presentation.theme.Secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    viewModel: UserViewModel,
    popToSearchScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    context: Context
) {
    val state = viewModel.userState
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri -> viewModel.changeSelectedImageUriState(uri, context)}
    )

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {CenterAlignedTopAppBar(title = {
        //take() because of long name
        Text(text = viewModel.getUserName().take(Constants.MAX_SIZE_OF_NAME_APPBAR),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
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
            IconButton(onClick = { popToSearchScreen() },
                //u can't click when uploading
                enabled = !state.isUploading
                ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back button",
                    tint = Secondary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    )}) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                .size(250.dp)
                .clickable {
                    //picker for photos
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ){

                if(state.selectedImageUri!=null){
                    AsyncImage(
                        model = state.selectedImageUri,
                        contentDescription = "Profile picture",
                        modifier = Modifier.clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                else{
                        Icon(modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Account icon",
                            tint = Secondary
                        )
                }
                Icon(modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(30.dp),
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Change picture",
                    tint = Secondary
                )
            }
            Column {
                //progress bar when picture is uploading
                if(state.isUploading) {
                    Box(modifier = Modifier.fillMaxSize()){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(3.dp)
                                .fillMaxSize()
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }else{
                    Spacer(modifier = Modifier.height(40.dp))
                    OutlinedTextField(value = state.userName,
                        onValueChange = {
                                newName -> viewModel.changeUsernameState(newName)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(BorderStroke(0.dp, Color.Transparent))
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        placeholder = { Text("Username") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                    //change user name
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                        onClick = { viewModel.changeUsername(state.userName, context) },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Text(text = "Change username",
                            fontWeight = FontWeight.Bold,
                            color = Secondary,
                            fontSize = 18.sp)
                    }
                    Button(onClick = { viewModel.signOut(navigateToLoginScreen) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Text(text = "Sign out",
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            fontSize = 18.sp)
                    }
                }
            }
        }

    }

}