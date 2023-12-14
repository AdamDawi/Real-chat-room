package com.example.distancecoupleapp.presentation.camera

import android.content.Context
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distancecoupleapp.presentation.camera.components.CameraPreview
import com.example.distancecoupleapp.presentation.theme.CustomBackground
import com.example.distancecoupleapp.presentation.theme.Secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(viewModel: CameraViewModel,
    context: Context,
    navController: NavController,
    roomId: String
) {
        //controller must be in composable
        val controller = remember {
            LifecycleCameraController(context).apply {
                setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE
                )
            }
        }

        val state = viewModel.cameraState
        viewModel.changeIconState(controller)
    
    if(!state.isPhotoTaken){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black)
                .padding(bottom = 150.dp, top = 50.dp)
                .clip(RoundedCornerShape(19.dp))

        ) {
            if(!state.isLoading)
                CameraPreview(
                    controller = controller,
                    modifier = Modifier
                        .fillMaxSize()
                )else{
                //temporary screen view, which is a photo taken when the button was clicked
                Image(
                    bitmap = state.image.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(3.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom,
        ) {

            IconButton(
                onClick = {
                    viewModel.changeFlashMode(controller)
                },
                modifier = Modifier
                    .size(90.dp)
                    .padding(end = 20.dp)
            ) {
                Icon(
                    imageVector = state.flashIcon,
                    contentDescription = "Switch flash",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            Box(modifier = Modifier
                .border(7.dp, Color.White, CircleShape)
                .size(90.dp)
                .clip(CircleShape)
                .clickable {
                    viewModel.takePhoto(
                        controller = controller,
                        context
                    )
                }
                .indication(interactionSource = MutableInteractionSource(), rememberRipple(radius = 50.dp))
            )

            IconButton(
                onClick = {
                    viewModel.changeCameraMode(controller)
                },
                modifier = Modifier
                    .size(90.dp)
                    .padding(start = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Switch camera",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
    else{
        //Screen to write description to photo and add photo to database
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Black)
                    .padding(bottom = 250.dp, top = 20.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(19.dp))


            ) {
                Image(
                    bitmap = state.image.asImageBitmap(),
                    contentDescription = "taken photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Column{
                    OutlinedTextField(value = state.descriptionTextField,
                        onValueChange = {viewModel.changeDescriptionState(it) },
                        modifier = Modifier
                            .border(BorderStroke(0.dp, Color.Transparent))
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxWidth()
                            .height(140.dp),
                        placeholder = { Text("Description") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        maxLines = 6,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                viewModel.addPhotoToDatabase(state.descriptionTextField, roomId, navController)
                            }
                        )
                    )
                    Text(text = "${state.descriptionTextField.length} / 210",
                    modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(CustomBackground)
                        .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        Row(modifier = Modifier.clickable { viewModel.addPhotoToDatabase(state.descriptionTextField, roomId, navController)},
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Upload ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 40.sp,
                                color = Secondary)
                            Icon(imageVector = Icons.Default.Send,
                                contentDescription = "Image",
                                tint = Secondary,
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }

                    }
                }

            }
        }
}