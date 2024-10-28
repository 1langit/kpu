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
import com.example.kpu.ui.screen.LoginScreen
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
                NavHost(navController = navController, startDestination = Screen.Login) {

                    composable<Screen.Login> {
                        LoginScreen { username, password ->
                            viewModel.loginUser(username, password) { result ->
                                showOperationResult(
                                    result = result,
                                    onSuccess = {
                                        navController.navigate(Screen.Dashboard) {
                                            popUpTo(Screen.Login) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    successText = getString(R.string.login_success),
                                    failureText = getString(R.string.login_fail)
                                )
                            }
                        }
                    }

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
                                viewModel.addEntry(it) { result ->
                                    showOperationResult(
                                        result = result,
                                        onSuccess = { navController.navigateUp() },
                                        successText = getString(R.string.form_submitted),
                                        failureText = getString(R.string.nik_existed)
                                    )
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

                    composable<Entry> { selectedEntry ->
                        DetailScreen(
                            entry = selectedEntry.toRoute<Entry>(),
                            onDeleteEntry = {
                                viewModel.deleteEntry(it) { result ->
                                    showOperationResult(
                                        result = result,
                                        onSuccess = { navController.navigateUp() },
                                        successText = getString(R.string.delete_success),
                                        failureText = getString(R.string.delete_fail)
                                    )
                                }
                            },
                            onBackClick = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }

    private fun showOperationResult(
        result: Result<Unit>,
        onSuccess: () -> Unit,
        successText: String,
        failureText: String
    ) {
        if (result.isSuccess) {
            Toast.makeText(
                this@MainActivity,
                successText,
                Toast.LENGTH_SHORT
            ).show()
            onSuccess()
        } else {
            Toast.makeText(
                this@MainActivity,
                failureText,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}