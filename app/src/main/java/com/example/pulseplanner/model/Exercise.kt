package com.example.pulseplanner.model

data class Exercise (
    val name: String,
    val categories: List<Category>,
    val description: String
)