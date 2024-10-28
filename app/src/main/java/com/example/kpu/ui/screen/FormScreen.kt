package com.example.kpu.ui.screen

import android.Manifest
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kpu.R
import com.example.kpu.data.Entry
import com.example.kpu.ui.components.DateFieldComponent
import com.example.kpu.ui.components.ImagePickerComponent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun FormScreen(onBackClick: () -> Unit = {}, onSubmitClick: (Entry) -> Unit = {}) {

    var form by remember {
        mutableStateOf(
            Entry(
                nik = "",
                name = "",
                phone = "",
                gender = "",
                date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
                address = "",
                image = ""
            )
        )
    }
    val context = LocalContext.current
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }

        if (permissionsState.allPermissionsGranted) {
            LocationServices
                .getFusedLocationProviderClient(context)
                .lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        Geocoder(context, Locale.getDefault())
                            .getAddress(location.latitude, location.longitude) { addresses ->
                                form.address = addresses?.getAddressLine(0) ?: ""
                            }
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.location_fail),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.address_permission_not_granted),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.form)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.back)
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp, 8.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.nik)) },
                    value = form.nik,
                    onValueChange = { form = form.copy(nik = it) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.name)) },
                    value = form.name,
                    onValueChange = { form = form.copy(name = it) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.phone)) },
                    value = form.phone,
                    onValueChange = { form = form.copy(phone = it) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Column {
                    Text(text = stringResource(R.string.gender))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (form.gender == stringResource(R.string.male)),
                            onClick = { form = form.copy(gender = context.getString(R.string.male)) }
                        )
                        Text(text = stringResource(R.string.male))
                        RadioButton(
                            selected = (form.gender == stringResource(R.string.female)),
                            onClick = { form = form.copy(gender = context.getString(R.string.female)) }
                        )
                        Text(text = stringResource(R.string.female))
                    }
                }
            }
            item {
                DateFieldComponent(
                    label = stringResource(R.string.date),
                    value = form.date,
                    onValueChange = { form = form.copy(date = it) }
                )
            }
            item {
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.address)) },
                    value = form.address,
                    onValueChange = { form = form.copy(address = it) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Column {
                    Text(text = stringResource(R.string.image))
                    ImagePickerComponent(
                        onImageSelected = { form = form.copy(image = it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        if (isFormComplete(form)) {
                            onSubmitClick(form)
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.form_cannot_blank),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.submit))
                }
            }
        }
    }
}

@Suppress("DEPRECATION")
fun Geocoder.getAddress(
    latitude: Double,
    longitude: Double,
    address: (android.location.Address?) -> Unit
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getFromLocation(latitude, longitude, 1) { address(it.firstOrNull()) }
        return
    }

    try {
        address(getFromLocation(latitude, longitude, 1)?.firstOrNull())
    } catch(e: Exception) {
        address(null)
    }
}

private fun isFormComplete(form: Entry): Boolean {
    return form.nik.isNotBlank() &&
            form.name.isNotBlank() &&
            form.phone.isNotBlank() &&
            form.gender.isNotBlank() &&
            form.date.isNotBlank() &&
            form.address.isNotBlank() &&
            form.image.isNotBlank()
}

@Preview
@Composable
private fun FormPreview() {
    FormScreen()
}