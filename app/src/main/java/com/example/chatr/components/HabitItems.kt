package com.example.chatr.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatr.model.Habit


@Composable
fun HabitItems(
    habit: Habit,
    onUpdate: (Habit) -> Unit,
    onDelete: (String) -> Unit,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        if (isPressed) Color(0xFFD79FE2) else Color.White,
        label = "Background Color"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                isPressed = !isPressed
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = habit.name, style = MaterialTheme.typography.titleLarge)
            Text(text = habit.description, style = MaterialTheme.typography.bodyMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Progress: ${habit.currentCount} / ${habit.targetCount}")

                Button(onClick = {
                    if (habit.currentCount < habit.targetCount) {
                        onUpdate(habit.copy(currentCount = habit.currentCount + 1))
                    }
                }) {
                    Text("+")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = { onDelete(habit.id) }) {
                    Text("Exclude")
                }
            }
        }
    }
}
