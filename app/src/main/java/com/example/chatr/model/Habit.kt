package com.example.chatr.model

data class Habit(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val targetCount: Int = 1,
    val currentCount: Int = 0,
    val complete: Map<String, Int> = emptyMap()
)