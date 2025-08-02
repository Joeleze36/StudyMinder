package com.joel_eze.studyminder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

class TodoViewModel : ViewModel() {
    val todoItems = mutableStateListOf<String>()

    fun addTask(task: String) {
        todoItems.add(task)
    }

    fun removeTask(task: String) {
        todoItems.remove(task)
    }
}