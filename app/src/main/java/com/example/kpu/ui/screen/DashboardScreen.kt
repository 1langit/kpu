package com.example.kpu.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kpu.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onInfoNavigate: () -> Unit = {},
    onFormNavigate: () -> Unit = {},
    onListNavigate: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name),)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "")
            }
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(R.drawable.img_election),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Pendataan Calon Pemilih",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(48.dp))
            OutlinedButton(
                onClick = onInfoNavigate,
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .fillMaxWidth()
            ) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.InsertDriveFile, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = stringResource(R.string.information))
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onFormNavigate,
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = stringResource(R.string.form))
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onListNavigate,
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .fillMaxWidth()
            ) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.List, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = stringResource(R.string.show_list))
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Outlined.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = stringResource(R.string.logout))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardPreview() {
    DashboardScreen()
}