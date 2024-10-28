package com.example.kpu.ui.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kpu.R
import com.example.kpu.data.Entry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    entry: Entry,
    onDeleteEntry: (Entry) -> Unit = {},
    onBackClick: () -> Unit = {}
) {

    val showDeleteDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.voter_data)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = if (entry.gender == stringResource(R.string.male)) {
                            Icons.Outlined.Person
                        } else {
                            Icons.Outlined.Person2
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.inversePrimary
                            )
                            .padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = entry.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "NIK: ${entry.nik}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            item {
                ListItemComponent(
                    title = stringResource(R.string.phone),
                    content = entry.phone,
                    icon = Icons.Outlined.Phone
                )
                ListItemComponent(
                    title = stringResource(R.string.gender),
                    content = entry.gender,
                    icon = if (entry.gender == stringResource(R.string.male)) {
                        Icons.Outlined.Male
                    } else {
                        Icons.Outlined.Female
                    }
                )
                ListItemComponent(
                    title = stringResource(R.string.date),
                    content = entry.date,
                    icon = Icons.Outlined.DateRange
                )
                ListItemComponent(
                    title = stringResource(R.string.address),
                    content = entry.address,
                    icon = Icons.Outlined.LocationOn
                )
            }
            item {
                ListItemComponent(
                    title = stringResource(R.string.image),
                    content = entry.image,
                    icon = Icons.Outlined.Image,
                    isImage = true
                )
            }
        }
    }

    if (showDeleteDialog.value) {
        AlertDialog(
            title = { Text(text = stringResource(R.string.confirm_delete)) },
            text = { Text(text = stringResource(R.string.confirm_delete_desc)) },
            onDismissRequest = { showDeleteDialog.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteEntry(entry)
                        showDeleteDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(text = stringResource(R.string.delete))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog.value = false }) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        )
    }
}

@Composable
private fun ListItemComponent(
    title: String,
    content: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isImage: Boolean = false
) {
    Row(modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(text = content)
            if (isImage) {
                val imageBitmap = loadBitmapFromUri(LocalContext.current, Uri.parse(content))
                imageBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        Log.d("debug", uri.scheme.toString())
        when (uri.scheme) {
            "content" -> {
                val inputStream = context.contentResolver.openInputStream(uri)
                inputStream?.use { BitmapFactory.decodeStream(it) }
            }
            "file" -> {
                BitmapFactory.decodeFile(uri.path)
            }
            else -> null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailPreview() {
//    DetailScreen("")
}