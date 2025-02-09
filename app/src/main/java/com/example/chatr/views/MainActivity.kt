package com.example.chatr.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatr.components.HabitItems
import com.example.chatr.viewModels.HabitViewModel
import androidx.navigation.compose.rememberNavController
import com.example.chatr.R
import com.example.chatr.navegation.Nav
import com.example.chatr.ui.theme.CHaTrTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CHaTrTheme {
                val navController = rememberNavController()
                Nav(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: HabitViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val habits by viewModel.habits.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "CHaTr - Habits",
                    style = MaterialTheme.typography.headlineMedium
                )
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("habit_form") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Habit",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (habits.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.empty_state),
                    contentDescription = "No Habits yet...",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Habits Yet! Click on the '+' button to get started.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(habits) { habit ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(),
                            exit = fadeOut(animationSpec = tween(300)) + slideOutVertically()
                        ) {
                            HabitItems(
                                habit = habit,
                                onUpdate = { updatedHabit -> viewModel.updateHabit(updatedHabit) },
                                onDelete = { habitId -> viewModel.deleteHabit(habitId) },
                                onClick = {
                                    navController.navigate(
                                        "habit_stats/${
                                            habit.id
                                        }"
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /*  Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceEvenly
              ) {
                  Button(
                      onClick = { navController.navigate("habit_stats") },
                      shape = RoundedCornerShape(50),
                  ) {
                      Text("Go to Stats")
                  }
              }*/
        }
    }
}
