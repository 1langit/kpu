package com.example.kpu

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.kpu.data.AppDatabase
import com.example.kpu.data.Entry
import com.example.kpu.ui.navigation.Screen
import com.example.kpu.ui.screen.AppViewModel
import com.example.kpu.ui.screen.AppViewModelFactory
import com.example.kpu.ui.screen.DashboardScreen
import com.example.kpu.ui.screen.DetailScreen
import com.example.kpu.ui.screen.FormScreen
import com.example.kpu.ui.screen.InfoScreen
import com.example.kpu.ui.screen.ListScreen
import com.example.kpu.ui.theme.KPUTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, AppViewModelFactory(AppDatabase.getDatabase(this).entryDao()))[AppViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            KPUTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Dashboard) {
                    composable<Screen.Dashboard> {
                        DashboardScreen(
                            onInfoNavigate = { navController.navigate(Screen.Info) },
                            onFormNavigate = { navController.navigate(Screen.Form) },
                            onListNavigate = { navController.navigate(Screen.List) },
                            onLogout = { finishAndRemoveTask() }
                        )
                    }

                    composable<Screen.Info> {
                        InfoScreen { navController.navigateUp() }
                    }

                    composable<Screen.Form> {
                        FormScreen(
                            onBackClick = { navController.navigateUp() },
                            onSubmitClick = {
                                viewModel.addEntry(it) { isSuccess ->
                                    if (isSuccess) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            getString(R.string.form_submitted),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigateUp()
                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            getString(R.string.nik_existed),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }

                    composable<Screen.List> {
                        ListScreen(
                            viewModel = viewModel,
                            onItemClick = { navController.navigate(it) },
                            onBackClick = { navController.navigateUp() },
                            onFormNavigate = { navController.navigate(Screen.Form) }
                        )
                    }

                    composable<Entry> {
                        DetailScreen(
                            entry = it.toRoute<Entry>(),
                            onBackClick = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }
}