package com.example.chatr.views

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chatr.viewModels.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitStatsScreen(
    navController: NavController,
    viewModel: HabitViewModel = viewModel(),
    habitId: String
) {
    val habits by viewModel.habits.collectAsState()
    val habit = habits.find { it.id == habitId }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Statistics",
                    style = MaterialTheme.typography.headlineMedium
                )
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = habit != null,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(),
                exit = fadeOut(animationSpec = tween(300)) + slideOutVertically()
            ) {
                habit?.let {
                    viewModel.updateCompletionCount(it)

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("${it.name}: ${(it.currentCount * 100 / it.targetCount)}%")
                            Text("✅ Goal reached ${viewModel.getLastWeekCompletionCount(it)} times in the last 7 days")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("main") }) {
                Text("Go Back")
            }
        }
    }
}