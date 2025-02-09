package com.example.chatr.repo

import com.example.chatr.model.Habit
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HabitRepository {
    private val db = FirebaseFirestore.getInstance()

    private val habitsRef = db.collection("habits")

    suspend fun addHabit(habit: Habit) {
        val habitId = habit.id.ifEmpty { habitsRef.document().id }
        habitsRef.document(habitId).set(habit.copy(id = habitId)).await()
    }

    suspend fun getHabits(): List<Habit> {
        return habitsRef.get().await().toObjects(Habit::class.java)
    }

    suspend fun updateHabit(habit: Habit) {
        habitsRef.document(habit.id).set(habit).await()
    }

    suspend fun deleteHabit(habitId: String) {
        habitsRef.document(habitId).delete().await()
    }
}
