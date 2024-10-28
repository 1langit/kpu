package com.example.kpu.ui.components

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.kpu.R
import java.io.InputStream

@Composable
fun ImagePickerComponent(
    onImageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val photoUri = remember { createImageUri(context) }

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            imageUri = photoUri
        }
    }

    val getImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            takePictureLauncher.launch(photoUri)
        }
    }

    val hasCameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    val showBottomSheet = remember { mutableStateOf(false) }

    if (showBottomSheet.value){
        MyModalBottomSheet(
            onDismiss = { showBottomSheet.value = false },
            onTakePhotoClick = {
                showBottomSheet.value = false
                if (hasCameraPermission) {
                    takePictureLauncher.launch(photoUri)
                } else {
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onPhotoGalleryClick = {
                showBottomSheet.value = false
                getImageLauncher.launch("image/*")
            },
        )
    }

    if (imageUri != null) {
        val imageBitmap = imageUri?.let { imageFromUri(it) }
        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = modifier
                    .clickable { showBottomSheet.value = true }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
        onImageSelected(imageUri!!.toString())
    } else {
        Image(
            painter = painterResource(R.drawable.ic_add_photo),
            contentDescription = null,
            modifier = modifier
                .clickable { showBottomSheet.value = true }
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}

@Composable
fun MyModalBottomSheet(
    onDismiss: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPhotoGalleryClick: () -> Unit
) {
    MyModalBottomSheetContent(
        onDismiss = {
            onDismiss.invoke()
        },
        items = listOf(
            BottomSheetItem(
                title = stringResource(R.string.capture_image),
                icon = Icons.Outlined.CameraAlt,
                onClick = {
                    onTakePhotoClick.invoke()
                }
            ),
            BottomSheetItem(
                title = stringResource(R.string.pick_image),
                icon = Icons.Outlined.Image,
                onClick = {
                    onPhotoGalleryClick.invoke()
                }
            ),
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModalBottomSheetContent(
    onDismiss: () -> Unit,
    items: List<BottomSheetItem> = listOf(),
) {
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    val edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val windowInsets = if (edgeToEdgeEnabled) WindowInsets(0) else BottomSheetDefaults.windowInsets

    ModalBottomSheet(
        shape = MaterialTheme.shapes.medium.copy(
            bottomStart = CornerSize(0),
            bottomEnd = CornerSize(0)
        ),
        onDismissRequest = { onDismiss.invoke() },
        sheetState = bottomSheetState,
        windowInsets = windowInsets
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items.forEach {item ->
                androidx.compose.material3.ListItem(
                    modifier = Modifier.clickable {
                        item.onClick.invoke()
                    },
                    headlineContent = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun imageFromUri(uri: Uri) : ImageBitmap? {
    val context = LocalContext.current

    return remember(uri) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
}

private fun createImageUri(context: Context): Uri {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
}

data class BottomSheetItem(
    val title: String = "",
    val icon: ImageVector,
    val onClick: () -> Unit
)