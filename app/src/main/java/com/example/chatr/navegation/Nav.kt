package com.example.chatr.navegation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatr.viewModels.HabitViewModel
import com.example.chatr.views.MainScreen
import com.example.chatr.views.HabitFormScreen
import com.example.chatr.views.HabitStatsScreen

@Composable
fun Nav(navController: NavHostController) {
    val habitViewModel: HabitViewModel = viewModel()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController, habitViewModel) }
        composable("habit_form") { HabitFormScreen(navController, habitViewModel) }
        composable("habit_stats/{habitId}") { backStackEntry ->

            val habitId = backStackEntry.arguments?.getString("habitId") ?: ""
            HabitStatsScreen(navController, habitViewModel, habitId)
        }
    }
}