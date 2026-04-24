package com.app.myjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.myjournal.data.JournalDatabase
import com.app.myjournal.data.JournalRepository
import com.app.myjournal.ui.JournalViewModel
import com.app.myjournal.ui.create.CreateEntryScreen
import com.app.myjournal.ui.edit.EditEntryScreen
import com.app.myjournal.ui.home.HomeScreen
import com.app.myjournal.ui.theme.MyJournalTheme
import com.app.myjournal.ui.view.ViewEntryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = JournalDatabase.getDatabase(this)
        val repository = JournalRepository(database.journalDao())
        val viewModelFactory = JournalViewModel.Factory(repository)

        setContent {
            MyJournalTheme {
                val navController = rememberNavController()
                val viewModel: JournalViewModel = viewModel(factory = viewModelFactory)

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onNavigateToCreateEntry = { navController.navigate("create_entry") },
                            onNavigateToViewEntry = { entryId -> 
                                navController.navigate("view_entry/$entryId") 
                            }
                        )
                    }
                    composable("create_entry") {
                        CreateEntryScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "view_entry/{entryId}",
                        arguments = listOf(navArgument("entryId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val entryId = backStackEntry.arguments?.getLong("entryId") ?: return@composable
                        ViewEntryScreen(
                            entryId = entryId,
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToEdit = { id -> 
                                navController.navigate("edit_entry/$id") 
                            }
                        )
                    }
                    composable(
                        route = "edit_entry/{entryId}",
                        arguments = listOf(navArgument("entryId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val entryId = backStackEntry.arguments?.getLong("entryId") ?: return@composable
                        EditEntryScreen(
                            entryId = entryId,
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
