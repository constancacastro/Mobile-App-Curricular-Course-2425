package com.example.chatr.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatr.model.Habit
import com.example.chatr.repo.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HabitViewModel : ViewModel() {
    private val repository = HabitRepository()

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    init {
        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            _habits.value = repository.getHabits()
        }
    }

    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            val existingHabits = repository.getHabits().map { it.name }
            if (habit.name !in existingHabits) {
                repository.addHabit(habit)
                loadHabits()
            }
        }
    }

    fun updateCompletionCount(habit: Habit): Map<String, Int> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val today = calendar.time

        val dateStr = dateFormat.format(today)
        val completions = habit.complete.toMutableMap()
        val count = completions[dateStr] ?: 0
        completions[dateStr] = count + 1

        return completions
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            val updatedHabit = habit.copy(complete = updateCompletionCount(habit))
            repository.updateHabit(updatedHabit)
            loadHabits()
        }
    }

    fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            repository.deleteHabit(habitId)
            loadHabits()
        }
    }

    fun getLastWeekCompletionCount(habit: Habit): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        //val today = calendar.time

        var count = 0

        for (i in 0 until 7) {
            val dateStr = dateFormat.format(calendar.time)
            val completions = habit.complete[dateStr] ?: 0
            if (completions >= habit.targetCount) {
                count++
            }
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        return count
    }
}
